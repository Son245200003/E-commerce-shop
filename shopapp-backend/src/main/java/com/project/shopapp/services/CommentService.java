package com.project.shopapp.services;

import com.project.shopapp.dtos.CommentDTO;
import com.project.shopapp.models.Comment;

import java.util.List;

public interface CommentService {
    Comment addComment(CommentDTO commentDTO);
    Comment updateComment(long id,CommentDTO commentDTO);
    void deleteComment(long commentId);

    List<Comment> getCommentByUserAndProduct(long userId,long productId);
    List<Comment> findByProductId(Long productId);
}
