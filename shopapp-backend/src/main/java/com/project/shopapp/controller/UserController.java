package com.project.shopapp.controller;

import com.project.shopapp.component.JwtTokenUtils;
import com.project.shopapp.dtos.RefreshTokenDTO;
import com.project.shopapp.dtos.UpdateUserDTO;
import com.project.shopapp.dtos.UserDto;
import com.project.shopapp.dtos.UserLoginDTO;
import com.project.shopapp.models.Token;
import com.project.shopapp.models.User;
import com.project.shopapp.response.user.LoginReponse;
import com.project.shopapp.response.user.RegisterReponse;
import com.project.shopapp.response.user.UserListResponse;
import com.project.shopapp.response.user.UserReponse;
import com.project.shopapp.services.TokenService;
import com.project.shopapp.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getALlUser(@RequestParam(defaultValue = "") String keyword,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int limit){
        try {
            PageRequest pageRequest=PageRequest.of(page,limit, Sort.by("id").ascending());

            Page<User> users=userService.findAll(keyword,pageRequest);
            int totalPages=users.getTotalPages();

            List<UserReponse> userReponses= new ArrayList<>();
            for(User user:users){
                UserReponse userReponse=UserReponse.fromUser(user);
                userReponses.add(userReponse);
            }
            return ResponseEntity.ok(UserListResponse.builder()
                            .users(userReponses)
                            .totalPages(totalPages)
                    .build());
        }catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/register")
   public ResponseEntity<RegisterReponse> createUser(@Valid @RequestBody UserDto userDto, BindingResult result){
            if (result.hasErrors()) {
                List<String> errorMessage = result.getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(RegisterReponse.builder()
                                .message("Lỗi validation"+errorMessage)
                        .build());
            }
            if(!userDto.getPassword().equals(userDto.getRetypePassword())){
                return ResponseEntity.badRequest().body(RegisterReponse.builder()
                        .message("Pass word nhập lại ko đúng")
                        .build());
            }
            User user=userService.createUser(userDto);

            return ResponseEntity.ok(RegisterReponse.builder()
                            .user(user)
                            .message("Đăng kí thành công")
                    .build());

    }

    private boolean isMobileDevice(String userAgent){
        return userAgent.toLowerCase().contains("mobile");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDto, BindingResult result
                                   , HttpServletRequest request
                                   ){
        if (result.hasErrors()) {
            List<String> errorMessage = result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessage);
        }
        String token= userService.login(
                userLoginDto.getPhoneNumber(),
                userLoginDto.getPassword(),
                userLoginDto.getRoleId()==null?2:userLoginDto.getRoleId());
        String userAgent=request.getHeader("User-Agent");
        User user=userService.getUserDetailsFromToken(token);
        Token jwtToken=tokenService.addToken(user,token,isMobileDevice(userAgent));
        return ResponseEntity.ok(LoginReponse.builder()
                .token(token)
                .message("Đăng nhập thành công")
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .username(user.getUsername())
                .role(user.getRole())
                .id(user.getId())
                .build());
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO){
        try {
            User userDetail=userService.getUserDetailFromRefreshToken(refreshTokenDTO.getRefreshToken());
            Token jwtToken=tokenService.refreshToken(refreshTokenDTO.getRefreshToken(),userDetail);
            return ResponseEntity.ok(LoginReponse.builder()
                    .token(jwtToken.getToken())
                    .message("Refresh Token thành công")
                    .tokenType(jwtToken.getTokenType())
                    .refreshToken(jwtToken.getRefreshToken())
                    .username(userDetail.getUsername())
                    .role(userDetail.getRole())
                    .id(userDetail.getId())
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Refresh thất bại");
        }
    }
    @PutMapping("/details/{userId}")
    public ResponseEntity<?> update(@PathVariable("userId") long userId,
            @RequestBody UpdateUserDTO updateUserDTO,
            @RequestHeader("Authorization")String auhthorizationHeader){
        try {
            String extractedToken=auhthorizationHeader.substring(7);// loại bảo Bearer ra chuỗi token
            User user=userService.getUserDetailsFromToken(extractedToken);
            //xác nhận xem user nhập vào có phải user từ token ko
            if(user.getId()!=userId){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            User updateUser=userService.updateUser(userId,updateUserDTO);
            return ResponseEntity.ok(UserReponse.fromUser(updateUser));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization")String auhthorizationHeader){
        try {
            String extractedToken=auhthorizationHeader.substring(7);// loại bảo Bearer ra chuỗi token
            User user=userService.getUserDetailsFromToken(extractedToken);
            return ResponseEntity.ok(UserReponse.fromUser(user));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//    có 2 cách
    @PostMapping("/test")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal User user){
        try {
            userService.getUserById(user.getId());
            return ResponseEntity.ok(UserReponse.fromUser(user));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/reset-password/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> resetPassword(@PathVariable long userId){
        try{
            String newpassword= UUID.randomUUID().toString().substring(0,5);
            userService.resetPassword(userId,newpassword);
            return ResponseEntity.ok(newpassword);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PutMapping("/block/{userId}/{active}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> blockUser(@PathVariable long userId,@PathVariable Boolean active){
        try{
            userService.blockOrEnable(userId,active);
            return ResponseEntity.ok(active);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
