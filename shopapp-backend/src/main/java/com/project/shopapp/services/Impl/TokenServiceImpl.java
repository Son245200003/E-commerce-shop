package com.project.shopapp.services.Impl;

import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.component.JwtTokenUtils;
import com.project.shopapp.models.Token;
import com.project.shopapp.models.User;
import com.project.shopapp.repository.TokenRepository;
import com.project.shopapp.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private static final int MAX_TOKENS = 3;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.expiration-refresh-token}")
    private Long expirationRefreshToken;
    private final TokenRepository tokenRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @Transactional
    @Override
    public Token addToken(User user, String token, boolean isMobileDevice) {
        List<Token> userTokens = tokenRepository.findByUser(user);
        int tokenCount = userTokens.size();
        if (tokenCount >= MAX_TOKENS) {
            boolean hasNonMobileToken = !userTokens.stream().allMatch(Token::isMobile);
            Token tokenToDelete;
            if (hasNonMobileToken) {
                tokenToDelete = userTokens.stream()
                        .filter(userToken -> !userToken.isMobile())
                        .findFirst().orElse(userTokens.get(0));
            } else {
                tokenToDelete = userTokens.get(0);
            }
            tokenRepository.delete(tokenToDelete);
        }
        long expirationInSecond = expiration ;
        LocalDateTime expirationDateTime= LocalDateTime.now().plusSeconds(expirationInSecond);
        Token newToken=Token.builder()
                .token(token)
                .userId(user)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer ")
                .expiration_date(expirationDateTime)
                .isMobile(isMobileDevice)
                .build();
        newToken.setRefreshToken(UUID.randomUUID().toString());
        newToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        tokenRepository.save(newToken);
        return newToken;
    }
    @Transactional
    @Override
    public Token refreshToken(String refreshToken,User user) {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        if(existingToken == null) {
            throw new DataNotFoundException("Refresh token does not exist");
        }
        if(existingToken.getRefreshExpirationDate().compareTo(LocalDateTime.now()) < 0){
            tokenRepository.delete(existingToken);
            throw new DataNotFoundException("Refresh token is expired");
        }
        String token = jwtTokenUtils.generateToken(user);
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expiration);
        existingToken.setExpiration_date(expirationDateTime);
        existingToken.setToken(token);
        existingToken.setRefreshToken(UUID.randomUUID().toString());
        existingToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        return existingToken;
    }
}
