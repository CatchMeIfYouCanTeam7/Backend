package com.sparta.catchme.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDto {
    private Long id;
    private String imgUrl;
    private String author;
    private String hint;
    private String answer;
    private List<CommentResponseDto> commentResponseDto;
    private LocalDateTime createdAt;
}
