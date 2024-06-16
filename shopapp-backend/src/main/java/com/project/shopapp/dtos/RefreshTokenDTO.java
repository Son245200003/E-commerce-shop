package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class RefreshTokenDTO {
    @NotBlank
    @JsonProperty("refresh_token")
    private String refreshToken;
}
