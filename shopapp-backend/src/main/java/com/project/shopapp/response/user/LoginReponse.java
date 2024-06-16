package com.project.shopapp.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Role;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Data
public class LoginReponse {
    private String token;

    @JsonProperty("messages")
    private String message;
    private String tokenType;
    @JsonProperty("refresh_token")
    private String refreshToken;
    private String username;
    private Role role;
    private long id;
}
