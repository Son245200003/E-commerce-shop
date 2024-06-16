package com.project.shopapp.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Data
public class RegisterReponse {

    private User user;
    @JsonProperty("messages")
    private String message;
}