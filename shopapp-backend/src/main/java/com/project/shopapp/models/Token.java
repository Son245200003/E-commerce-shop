package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token",length = 255)
    private String token;

    @Column(name = "token_type",length = 50)
    private String tokenType;

    @Column(name = "expiration_date")
    private LocalDateTime expiration_date;

    private boolean revoked;
    private boolean expired;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User userId;
    @Column(name = "is_mobile")
    private boolean isMobile;

    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "refresh_expiration_date")
    private LocalDateTime refreshExpirationDate;
}
