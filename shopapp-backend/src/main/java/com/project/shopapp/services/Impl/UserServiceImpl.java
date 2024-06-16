package com.project.shopapp.services.Impl;

import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.Exception.PermissionDenyException;
import com.project.shopapp.component.JwtTokenUtils;
import com.project.shopapp.dtos.UpdateUserDTO;
import com.project.shopapp.dtos.UserDto;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.Token;
import com.project.shopapp.models.User;
import com.project.shopapp.response.user.UserReponse;
import com.project.shopapp.repository.RoleRepository;
import com.project.shopapp.repository.TokenRepository;
import com.project.shopapp.repository.UserRepository;
import com.project.shopapp.services.UserService;
import com.project.shopapp.component.LocalizationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final LocalizationUtils localizationUtils;
    private final TokenRepository tokenRepository;
    @Override
    @Transactional
    public User createUser(UserDto userDto) {
        String phoneNumber=userDto.getPhoneNumber();
//        Kiem tra sdt ton tai chua
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataNotFoundException("Phone number already exist");
        }

        Role role=roleRepository.findById(userDto.getRoleId()).orElseThrow(() -> new DataNotFoundException("Ko tìm thấy role"));
        if(role.getName().toUpperCase().equals(Role.ADMIN)){
            throw new PermissionDenyException("Bạn ko thể đăng kí tai khoan admin");
        }
        User newUser=User.builder()
                .fullName(userDto.getFullName())
                .phoneNumber(userDto.getPhoneNumber())
                .password(userDto.getPassword())
                .address(userDto.getAddress())
                .dateOfBirth(userDto.getDateOfBirth())
                .facebookAccountId(userDto.getFacebookAccountId())
                .googleAccountId(userDto.getGoogleAccountId())
                .active(true)
                .build();
        newUser.setRole(role);
        if(userDto.getFacebookAccountId()==0 && userDto.getGoogleAccountId()==0){
            String password=userDto.getPassword();
            String encodedPasword=passwordEncoder.encode(password);
            newUser.setPassword(encodedPasword);
        }
        return userRepository.save(newUser);
    }
    @Override
    @Transactional // thao tác làm thay đổi dữ liệu để transaction nếu lỗi n sẽ rollback về trạng thái trước
    public User updateUser(Long userId, UpdateUserDTO updateUserDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        String phoneNumber = updateUserDTO.getPhoneNumber();
        // Check if the phone number is being updated and if it already exists for another user
        if (!existingUser.getPhoneNumber().equals(phoneNumber) && userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataNotFoundException("Phone number already exists");
        }

        // Chỉ cập nhật các thuộc tính nếu chúng không phải là null hoặc trống
        if (updateUserDTO.getFullName() != null && !updateUserDTO.getFullName().isEmpty()) {
            existingUser.setFullName(updateUserDTO.getFullName());
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            existingUser.setPhoneNumber(phoneNumber);
        }
        if (updateUserDTO.getAddress() != null && !updateUserDTO.getAddress().isEmpty()) {
            existingUser.setAddress(updateUserDTO.getAddress());
        }
        if (updateUserDTO.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(updateUserDTO.getDateOfBirth());
        }
        if (updateUserDTO.getFacebookAccountId() != 0) {
            existingUser.setFacebookAccountId(updateUserDTO.getFacebookAccountId());
        }
        if (updateUserDTO.getGoogleAccountId() != 0) {
            existingUser.setGoogleAccountId(updateUserDTO.getGoogleAccountId());
        }

        if (updateUserDTO.getFacebookAccountId() == 0 && updateUserDTO.getGoogleAccountId() == 0 && updateUserDTO.getPassword() != null) {
            if(!updateUserDTO.getPassword().equals(updateUserDTO.getRetypePassword())){
                throw new DataNotFoundException("mật khẩu nhập lại ko khớp");
            }
            String encodedPassword = passwordEncoder.encode(updateUserDTO.getPassword());
            existingUser.setPassword(encodedPassword);
        }

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void resetPassword(Long userId, String newPassword) {
        User user=userRepository.findById(userId).
                orElseThrow(() -> new DataNotFoundException("Lỗi hãm resetPassword ko tìm thấy user"));
        String encodePassword=passwordEncoder.encode(newPassword);
        user.setPassword(encodePassword);
        userRepository.saveAndFlush(user);
        List<Token> tokens=tokenRepository.findByUser(user);
        for(Token token:tokens){
            tokenRepository.delete(token);
        }
    }

    @Override
    @Transactional
    public void blockOrEnable(Long userId,Boolean active) {
        User user=userRepository.findById(userId).
                orElseThrow(() -> new DataNotFoundException("Lỗi hãm resetPassword ko tìm thấy user"));
        if(active){
            user.setActive(true);
        }else {
        user.setActive(false);
        }
        userRepository.saveAndFlush(user);
    }

    @Override
    public String login(String phoneNumber, String password,Long roleId) {
        Optional<User> user=userRepository.findByPhoneNumber(phoneNumber);
        if (user.isEmpty()){
            throw new DataNotFoundException("Sai tài khoản hoặc mật khẩu");
        }
        User existingUser=user.get();
        if(existingUser.getFacebookAccountId()==0 && existingUser.getGoogleAccountId()==0){
            if(!passwordEncoder.matches(password,existingUser.getPassword())){
                throw new PermissionDenyException("Wrong phone number password");
            }
        }
        Optional<Role> optionalRole=roleRepository.findById(roleId);
        if(optionalRole.isEmpty()||!roleId.equals(existingUser.getRole().getId())){
            throw new PermissionDenyException("Bạn ko có quyền đăng nhập");
        }
        if(!user.get().isActive()){
            throw new DataNotFoundException("Your account is blocked");
        }
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                phoneNumber,password
                ,existingUser.getAuthorities()
        );
//        Authen with javaspring
        authenticationManager.authenticate(authenticationToken);

        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public Page<User> findAll(String keyword,Pageable pageable) {
        return userRepository.findAll(keyword,pageable);
    }

    @Override
    public UserReponse getUserById(long id) {
        User user=userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Ko tìm thấy id của user: "+id));
        UserReponse userReponse=UserReponse.builder()
                .phoneNumber(user.getPhoneNumber())
                .fullName(user.getFullName())
                .dateOfBirth(user.getDateOfBirth())
                .role(user.getRole())
                .build();

        return userReponse;
    }

    @Override
    public User getUserDetailsFromToken(String token) {
        if(jwtTokenUtil.isTokenExpired(token)){
            throw new RuntimeException("Token is expired");
        }
        String phonenumber=jwtTokenUtil.extractPhonenumber(token);
        Optional<User> user=userRepository.findByPhoneNumber(phonenumber);
        if(user.isPresent()){
            return user.get();
        }else throw new DataNotFoundException("Not found user");

    }

    @Override
    public User getUserDetailFromRefreshToken(String refreshToken) {
        Token existingToken=tokenRepository.findByRefreshToken(refreshToken);

        return getUserDetailsFromToken(existingToken.getToken());
    }


}
