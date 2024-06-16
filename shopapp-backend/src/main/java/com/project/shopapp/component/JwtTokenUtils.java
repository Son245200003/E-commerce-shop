package com.project.shopapp.component;

import com.project.shopapp.Exception.InvalidParamException;
import com.project.shopapp.controller.ProductController;
import com.project.shopapp.models.Token;
import com.project.shopapp.models.User;
import com.project.shopapp.repository.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    @Value("${jwt.expiration}")
    private int expiration;
    @Value("${jwt.secretKey}")
    private String secretKey;
    private final TokenRepository tokenRepository;
    private final Logger logger= LoggerFactory.getLogger(JwtTokenUtils.class);
    public String generateToken(User user){
//        properties => claims
        Map<String,Object> claims=new HashMap<>();
        claims.put("phoneNumber",user.getPhoneNumber());
        claims.put("userId",user.getId());
//        claims là trong token có chứa gì (ví dụ ở đây là chứa phoneNumber
//        this.generateSecretKey();

        try {
            String token= Jwts.builder()
                    .setClaims(claims) //
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis()+expiration*1000L))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        }catch (Exception e){
//            có thể dùng logger
           throw  new InvalidParamException("ko tạo đc jwt token"+e.getMessage());
        }
    }
    public int getExpiration() {
        return expiration;
    }
    private Key getSignKey(){
        byte[] bytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }
    private String generateSecretKey(){
        SecureRandom random=new SecureRandom();
        byte[] keyByte=new byte[32];
        random.nextBytes(keyByte);
        String secretKey= Encoders.BASE64.encode(keyByte);
        return secretKey;
    }
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public  <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
       final Claims claims= this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
//    check Expiration
    public boolean isTokenExpired(String token){
        Date expirationDate=this.extractClaim(token,Claims::getExpiration);
        return expirationDate.before(new Date());
    }
    public String extractPhonenumber(String token){
        return extractClaim(token,Claims::getSubject);
    }
    public boolean validateToken(String token, User userDetails){
//        String phoneNumber=extractPhonenumber(token);
//
//        return (phoneNumber.equals((userDetails.getUsername())))
//                && !isTokenExpired(token);
        try {
            String phoneNumber = extractPhonenumber(token);
            logger.info("Extracted phone number: {}", phoneNumber);
            logger.info("Searching for token: {}", token);
            Token existingToken = tokenRepository.findByToken(token);
            if(existingToken == null || existingToken.isRevoked() == true || !userDetails.isActive()) {
                return false;
            }
            return (phoneNumber.equals(userDetails.getUsername()))
                    && !isTokenExpired(token);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
