package com.project.shopapp.services;

import com.project.shopapp.models.Token;
import com.project.shopapp.models.User;

public interface TokenService {
    public Token addToken(User user, String token, boolean isMobileDevice);
    public Token refreshToken(String refreshToken,User user);
}
