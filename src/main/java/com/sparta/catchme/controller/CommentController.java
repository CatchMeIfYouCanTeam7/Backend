package com.sparta.catchme.controller;

import com.sparta.catchme.dto.request.CommentRequestDto;
import com.sparta.catchme.dto.response.ResponseDto;
import com.sparta.catchme.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/api/auth/comments")
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        return commentService.createComment(commentRequestDto, request);
    }

    @GetMapping("/api/comments/detail/{commentId}")
    public ResponseDto<?> getComment(@PathVariable Long commentId) {
        return commentService.getComment(commentId);
    }

    @GetMapping("/api/comments/{questionId}")
    public ResponseDto<?> getAllCommentsByQuestion(@PathVariable Long questionId) {
        return commentService.getAllCommentsByQuestion(questionId);
    }

    @DeleteMapping("/api/auth/comments/{commentId}")
    public ResponseDto<?> deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        return commentService.deleteComment(commentId, request);
    }
}
