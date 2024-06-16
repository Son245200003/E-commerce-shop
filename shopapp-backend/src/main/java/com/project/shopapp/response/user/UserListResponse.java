package com.project.shopapp.response.user;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserListResponse {
    private List<UserReponse> users;
    private int totalPages;
}
