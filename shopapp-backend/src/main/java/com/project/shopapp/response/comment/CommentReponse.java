package com.project.shopapp.response.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Comment;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentReponse {
    private Long id;
    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("user_id")
    private Long userId;

    private String content;
    public static CommentReponse fromComment(Comment comment){
        return CommentReponse.builder()
                .id(comment.getId())
                .productId(comment.getProduct().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .build();
    }
}
