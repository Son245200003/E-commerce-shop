package com.project.shopapp.services;

import com.project.shopapp.Exception.PermissionDenyException;
import com.project.shopapp.dtos.UpdateUserDTO;
import com.project.shopapp.dtos.UserDto;
import com.project.shopapp.models.User;
import com.project.shopapp.response.user.UserReponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User createUser(UserDto userDto) throws PermissionDenyException;

    String login(String phoneNumber, String password,Long roleId);

    Page<User> findAll(String keyword,Pageable pageable);
    UserReponse getUserById(long id);
    User getUserDetailsFromToken(String token);
    User getUserDetailFromRefreshToken(String refreshToken);
    User updateUser(Long userId, UpdateUserDTO updateUserDTO);

    void resetPassword(Long userId,String newPassword);
    void blockOrEnable(Long userId,Boolean active);
}
