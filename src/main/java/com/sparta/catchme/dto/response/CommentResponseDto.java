package com.sparta.catchme.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String author;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean trueOrFalse;
}
