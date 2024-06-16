package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@Table(name = "social_accounts")
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "provider",nullable = false,length = 20)
    private String provider;

    @Column(name = "provider_id",nullable = false,length = 50)
    private String providerId;

    @Column(name = "email",nullable = false,length = 50)
    private String email;

    @Column(name = "name",nullable = false,length = 50)
    private String name;
}
