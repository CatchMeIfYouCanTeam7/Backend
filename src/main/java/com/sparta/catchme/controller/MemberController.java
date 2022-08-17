package com.sparta.catchme.controller;

import com.sparta.catchme.dto.request.LoginRequestDto;
import com.sparta.catchme.dto.request.MemberRequestDto;
import com.sparta.catchme.dto.response.ResponseDto;
import com.sparta.catchme.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
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


  @RequestMapping(value = "/api/auth/members/logout", method = RequestMethod.GET)
  public ResponseDto<?> logout() {
    return memberService.logout();
  }

  //이메일 중복확인
  @RequestMapping(value = "/api/members/email-check", method = RequestMethod.GET)
  public ResponseDto<?> checkDuplicateEmail(String email) {
      if (email.isEmpty()) {
          return ResponseDto.fail("EMPTY_EMAIL", "이메일을 입력해주세요.");
      }
      if (!memberService.checkDuplicateEmail(email)) {
          return ResponseDto.fail("DUPLICATED_EMAIL", "중복된 이메일입니다.");
      }
      return ResponseDto.success("사용가능한 이메일입니다.");
  }

  //닉네임 중복확인
  @RequestMapping(value = "/api/members/nickname-check", method = RequestMethod.GET)
  public ResponseDto<?> checkDuplicateNickname(String nickname) {
      if (nickname.isEmpty()) {
          return ResponseDto.fail("EMPTY_NICKNAME", "닉네임을 입력해주세요.");
      }
      if (!memberService.checkDuplicateNickname(nickname)) {
          return ResponseDto.fail("DUPLICATED_NICKNAME", "중복된 닉네임입니다.");
      }
      return ResponseDto.success("사용가능한 닉네임입니다.");
  }
}