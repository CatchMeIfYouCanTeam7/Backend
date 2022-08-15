package com.sparta.catchme.service;

import com.sparta.catchme.domain.Comment;
import com.sparta.catchme.domain.Member;
import com.sparta.catchme.domain.Question;
import com.sparta.catchme.dto.request.QuestionRequestDto;
import com.sparta.catchme.dto.response.CommentResponseDto;
import com.sparta.catchme.dto.response.QuestionResponseDto;
import com.sparta.catchme.dto.response.ResponseDto;
import com.sparta.catchme.jwt.TokenProvider;
import com.sparta.catchme.repository.CommentRepository;
import com.sparta.catchme.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final AwsS3Service awsS3Service;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final TokenProvider tokenProvider;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public ResponseDto<?> createQuestion(MultipartFile multipartFile, QuestionRequestDto questionRequestDto, HttpServletRequest request) throws IOException {
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember();
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        String imgUrl = awsS3Service.upload(multipartFile);
        Question question = Question.builder()
                .imgUrl(imgUrl)
                .member(member)
                .hint(questionRequestDto.getHint())
                .answer(questionRequestDto.getAnswer())
                .build();
        questionRepository.save(question);
        return ResponseDto.success(
                QuestionResponseDto.builder()
                        .id(question.getId())
                        .author(question.getMember().getNickname())
                        .imgUrl(question.getImgUrl())
                        .hint(question.getHint())
                        .answer(question.getAnswer())
                        .createdAt(question.getCreatedAt())
                        .modifiedAt(question.getModifiedAt())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getQuestion(Long questionId) {
        Question question = isPresentQuestion(questionId);
        if (null == question) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        List<Comment> commentList = commentRepository.findAllByQuestion(question);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .author(comment.getMember().getNickname())
                            .comment(comment.getComment())
                            .trueOrFalse(successOrFailure(questionId, comment.getComment()))
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );

        }
        return ResponseDto.success(
                QuestionResponseDto.builder()
                        .id(question.getId())
                        .author(question.getMember().getNickname())
                        .imgUrl(question.getImgUrl())
                        .hint(question.getHint())
                        .commentResponseDto(commentResponseDtoList)
                        .createdAt(question.getCreatedAt())
                        .modifiedAt(question.getModifiedAt())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllQuestion() {
        List<Question> questionList = questionRepository.findAllByOrderByCreatedAtDesc();
        List<QuestionResponseDto> questionResponseDtoList = new ArrayList<>();
        for (Question question : questionList) {
            questionResponseDtoList.add(
                    QuestionResponseDto.builder()
                            .id(question.getId())
                            .author(question.getMember().getNickname())
                            .imgUrl(question.getImgUrl())
                            .hint(question.getHint())
                            .answer(question.getAnswer())
                            .createdAt(question.getCreatedAt())
                            .modifiedAt(question.getModifiedAt())
                            .build()
            );
        }

        return ResponseDto.success(questionResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> updateQuestion(Long questionId, MultipartFile multipartFile, QuestionRequestDto questionRequestDto, HttpServletRequest request) throws IOException {
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember();
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Question question = isPresentQuestion(questionId);
        if (null == question) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (question.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        String imgUrl = awsS3Service.upload(multipartFile);
        question.update(questionRequestDto, imgUrl);
        return ResponseDto.success(
                QuestionResponseDto.builder()
                        .id(question.getId())
                        .author(question.getMember().getNickname())
                        .imgUrl(question.getImgUrl())
                        .hint(question.getHint())
                        .answer(question.getAnswer())
                        .createdAt(question.getCreatedAt())
                        .modifiedAt(question.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> deleteQuestion(Long questionId, HttpServletRequest request) {
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember();
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Question question = isPresentQuestion(questionId);
        if (null == question) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (question.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        questionRepository.delete(question);
        return ResponseDto.success("delete success");
    }

    @Transactional(readOnly = true)
    public Question isPresentQuestion(Long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        return optionalQuestion.orElse(null);
    }

    @Transactional
    public Member validateMember() {
        return tokenProvider.getMemberFromAuthentication();
    }

    @Transactional(readOnly = true)
    public boolean successOrFailure(Long questionId, String comment) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        String answer = optionalQuestion.orElse(null).getAnswer();
        if (!answer.equals(comment)) { return false; }
        return true;
    }
}
