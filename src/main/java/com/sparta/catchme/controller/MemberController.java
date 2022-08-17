package com.sparta.catchme.controller;

import com.sparta.catchme.dto.request.LoginRequestDto;
import com.sparta.catchme.dto.request.MemberRequestDto;
import com.sparta.catchme.dto.response.ResponseDto;
import com.sparta.catchme.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {

  private final MemberService memberService;
  @RequestMapping(value = "/api/members/signup", method = RequestMethod.POST)
  public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) {
    return memberService.createEmail(requestDto);
  }

  @RequestMapping(value = "/api/members/login", method = RequestMethod.POST)
  public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto,
                              HttpServletResponse response
  ) {
    return memberService.login(requestDto, response);
  }

  @RequestMapping(value = "/api/auth/members/logout", method = RequestMethod.POST)
  public ResponseDto<?> logout() {
    return memberService.logout();
  }

  //이메일 중복확인
  @RequestMapping(value = "/api/members/email-check", method = RequestMethod.GET)
  public ResponseDto<?> emailCheck(String email){
      return memberService.checkEmail(email);
  }

  //닉네임 중복확인
  @RequestMapping(value = "/api/members/nickname-check", method = RequestMethod.GET)
  public ResponseDto<?> nicknameCheck(String nickname){
      return memberService.checkNickname(nickname);
  }
}
