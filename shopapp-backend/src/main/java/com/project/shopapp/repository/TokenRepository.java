package com.project.shopapp.repository;

import com.project.shopapp.models.Token;
import com.project.shopapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t FROM Token t WHERE t.userId=?1")
    List<Token> findByUser(User user);
    Token findByToken(String token);
    Token findByRefreshToken(String refreshToken);
}
