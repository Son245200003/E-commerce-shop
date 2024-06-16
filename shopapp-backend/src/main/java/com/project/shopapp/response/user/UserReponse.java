package com.project.shopapp.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserReponse {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("phonenumber")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;
//    @JsonProperty("password")
//    private String password;
    @JsonProperty("is_active")
    private boolean active;
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    @JsonProperty("facebook_account_id")
    private int facebookAccountId;
    @JsonProperty("google_account_id")
    private int googleAccountId;
    @JsonProperty("role")
    private Role role;
    public static UserReponse fromUser(User user){
        return UserReponse
                .builder()
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .dateOfBirth(user.getDateOfBirth())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .active(user.isActive())
                .facebookAccountId(user.getFacebookAccountId())
                .googleAccountId(user.getGoogleAccountId())
                .build();

    }
    }
