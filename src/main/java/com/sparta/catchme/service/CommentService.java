package com.sparta.catchme.service;

import com.sparta.catchme.domain.Comment;
import com.sparta.catchme.domain.Member;
import com.sparta.catchme.domain.Question;
import com.sparta.catchme.dto.request.CommentRequestDto;
import com.sparta.catchme.dto.response.CommentResponseDto;
import com.sparta.catchme.dto.response.ResponseDto;
import com.sparta.catchme.jwt.TokenProvider;
import com.sparta.catchme.repository.CommentRepository;
import com.sparta.catchme.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final QuestionService questionService;
    private final TokenProvider tokenProvider;
    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto requestDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Question question = questionService.isPresentQuestion(requestDto.getQuestionId());
        if (null == question) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        Comment comment = Comment.builder()
                .member(member)
                .comment(requestDto.getComment())
                .question(question)
                .build();

        commentRepository.save(comment);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .author(comment.getMember().getNickname())
                        .comment(comment.getComment())
                        .trueOrFalse(successOrFailure(requestDto.getQuestionId(), comment.getComment()))
                        .createdAt(comment.getCreatedAt())
                        .build()
        );

    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getComment(Long commentId) {
        Comment comment = isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .author(comment.getMember().getNickname())
                        .comment(comment.getComment())
                        .trueOrFalse(successOrFailure(comment.getQuestion().getId(), comment.getComment()))
                        .createdAt(comment.getCreatedAt())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllCommentsByQuestion(Long questionId) {
        Question question = questionService.isPresentQuestion(questionId);
        if (null == question) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        List<Comment> commentList = commentRepository.findAllByQuestion(question);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment :  commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .author(comment.getMember().getNickname())
                            .comment(comment.getComment())
                            .trueOrFalse(successOrFailure(questionId, comment.getComment()))
                            .createdAt(comment.getCreatedAt())
                            .build()
            );
        }
        return ResponseDto.success(commentResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> deleteComment(Long commentId, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Comment comment = isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
        return ResponseDto.success("comment delete success");
    }

    @Transactional(readOnly = true)
    public Comment isPresentComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean successOrFailure(Long questionId, String comment) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        String answer = optionalQuestion.orElse(null).getAnswer();
        if (!answer.equals(comment)) { return false; }
        return true;
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
