package com.sparta.catchme.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestDto {
    private String imgUrl;
    private String hint;
    private String answer;
}
