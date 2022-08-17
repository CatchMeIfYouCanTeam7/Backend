package com.sparta.catchme.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {


    @NotNull
    private Long questionId;
    @NotNull
    private String comment;
}
