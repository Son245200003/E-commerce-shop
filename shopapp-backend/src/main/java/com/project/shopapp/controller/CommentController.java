package com.project.shopapp.controller;

import com.project.shopapp.dtos.CommentDTO;
import com.project.shopapp.models.Comment;
import com.project.shopapp.response.comment.CommentReponse;
import com.project.shopapp.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @GetMapping()
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getCommentByProductAndUser(@RequestParam("user_id") Long userId,
                                         @RequestParam("product_id")Long productId){

        List<Comment> comments=commentService.getCommentByUserAndProduct(userId,productId);
        List<CommentReponse> commentReponses=new ArrayList<>();
        for(Comment comment:comments){
            CommentReponse commentReponse=CommentReponse.fromComment(comment);
            commentReponses.add(commentReponse);
        }
        return ResponseEntity.ok(commentReponses);
    }
    @GetMapping("/product")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getCommentByProduct(@RequestParam("product_id")Long productId){

        List<Comment> comments=commentService.findByProductId(productId);
        List<CommentReponse> commentReponses=new ArrayList<>();
        for(Comment comment:comments){
            CommentReponse commentReponse=CommentReponse.fromComment(comment);
            commentReponses.add(commentReponse);
        }
        return ResponseEntity.ok(commentReponses);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateComment(@PathVariable long id, @RequestBody CommentDTO commentDTO){
        Comment comment=commentService.updateComment(id,commentDTO);
        return ResponseEntity.ok(CommentReponse.fromComment(comment));

    }
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addComment(@RequestBody CommentDTO commentDTO){
        Comment comment=commentService.addComment(commentDTO);
        return ResponseEntity.ok(CommentReponse.fromComment(comment));

    }
}
