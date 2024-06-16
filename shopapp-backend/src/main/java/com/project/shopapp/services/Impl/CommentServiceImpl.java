package com.project.shopapp.services.Impl;

import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.Exception.InvalidParamException;
import com.project.shopapp.dtos.CommentDTO;
import com.project.shopapp.models.Comment;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.User;
import com.project.shopapp.repository.CommentRepository;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.repository.UserRepository;
import com.project.shopapp.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public Comment addComment(CommentDTO commentDTO) {
        Product product=productRepository.findById(commentDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Not found id Product:"+commentDTO.getProductId()));
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.getId()!=commentDTO.getUserId()){
            throw new InvalidParamException("Người dùng hiện tại ko đc thêm comment user khác");
        }
        Comment newComment=Comment.builder()
                .product(product)
                .user(user)
                .content(commentDTO.getContent())
                .build();

        return commentRepository.save(newComment);
    }

    @Override
    @Transactional
    public Comment updateComment(long id,CommentDTO commentDTO) {
        Comment comment=commentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Not Found Comment: "+id));
        Product product=productRepository.findById(commentDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Not found id Product:"+commentDTO.getProductId()));
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.getId()!=commentDTO.getUserId()){
            throw new InvalidParamException("Người dùng hiện tại ko đc sửa comment user khác");
        }
        comment.setContent(commentDTO.getContent());
        return commentRepository.saveAndFlush(comment);
    }

    @Override
    @Transactional
    public void deleteComment(long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<Comment> getCommentByUserAndProduct(long userId,long productId) {

        return commentRepository.findByUserIdAndProductId(userId,productId);
    }

    @Override
    public List<Comment> findByProductId(Long productId) {
        return commentRepository.findByProductId(productId);
    }

}
