package com.sparta.catchme.controller;

import com.sparta.catchme.domain.Question;
import com.sparta.catchme.dto.request.QuestionRequestDto;
import com.sparta.catchme.dto.response.QuestionResponseDto;
import com.sparta.catchme.dto.response.ResponseDto;
import com.sparta.catchme.repository.QuestionRepository;
import com.sparta.catchme.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class QuestionController  {
    private final QuestionService questionService;
    private final QuestionRepository questionRepository;

    @PostMapping(value = "/api/auth/questions")
    public ResponseDto<?> createQuestion(@RequestPart(required = false) MultipartFile multipartFile,
                                         @RequestPart QuestionRequestDto questionRequestDto,
                                         HttpServletRequest request) throws IOException {
        if (multipartFile.isEmpty()) {
            return ResponseDto.fail("EMPTY", "multipart file is empty");
        }
        return questionService.createQuestion(multipartFile, questionRequestDto, request);
    }

    @GetMapping(value = "/api/questions/{questionId}")
    public ResponseDto<?> getQuestion(@PathVariable Long questionId) {
        return questionService.getQuestion(questionId);
    }

    @GetMapping(value = "/api/questions")
    public ResponseDto<?> getAllQuestions() {
        return questionService.getAllQuestion();
    }

    @PutMapping(value = "/api/auth/questions/{questionId}")
    public ResponseDto<?> updateQuestion(@PathVariable Long questionId,
                                         @RequestPart(required = false) MultipartFile multipartFile,
                                         @RequestPart QuestionRequestDto questionRequestDto,
                                         HttpServletRequest request) throws IOException {
        return questionService.updateQuestion(questionId, multipartFile, questionRequestDto, request);
    }

    @DeleteMapping("/api/auth/questions/{questionId}")
    public ResponseDto<?> deleteQuestion(@PathVariable Long questionId, HttpServletRequest request) {
        return questionService.deleteQuestion(questionId, request);
    }

}
