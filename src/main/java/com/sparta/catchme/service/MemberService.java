package com.sparta.catchme.service;

import com.sparta.catchme.domain.Member;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sparta.catchme.domain.RefreshToken;
import com.sparta.catchme.dto.request.LoginRequestDto;
import com.sparta.catchme.dto.request.MemberRequestDto;
import com.sparta.catchme.dto.request.TokenDto;
import com.sparta.catchme.dto.response.MemberResponseDto;
import com.sparta.catchme.dto.response.ResponseDto;
import com.sparta.catchme.jwt.TokenProvider;
import com.sparta.catchme.repository.MemberRepository;
import io.swagger.models.properties.EmailProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenProvider tokenProvider;

  @Transactional
  public ResponseDto<?> createEmail(MemberRequestDto requestDto) {
    if (null != isPresentMember(requestDto.getEmail())) {
      return ResponseDto.fail("DUPLICATED_NICKNAME",
              "중복된 이메일 입니다.");
    }

    if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
      return ResponseDto.fail("PASSWORDS_NOT_MATCHED",
              "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }

    Member member = Member.builder()
            .email(requestDto.getEmail())
            .nickname(requestDto.getNickname())
            .password(passwordEncoder.encode(requestDto.getPassword()))
            .build();
    memberRepository.save(member);

    return ResponseDto.success(
            MemberResponseDto.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .createdAt(member.getCreatedAt())
                    .modifiedAt(member.getModifiedAt())
                    .build()
    );
  }

  @Transactional
  public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
    Member member = isPresentMember(requestDto.getEmail());
    if (null == member) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "사용자를 찾을 수 없습니다.");
    }

    if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
      return ResponseDto.fail("INVALID_MEMBER", "사용자를 찾을 수 없습니다.");
    }

    TokenDto tokenDto = tokenProvider.generateTokenDto(member);
    tokenToHeaders(tokenDto, response);

    return ResponseDto.success(
            MemberResponseDto.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .createdAt(member.getCreatedAt())
                    .modifiedAt(member.getModifiedAt())
                    .build()
    );
  }

  public ResponseDto<?> logout() {
    Member member = tokenProvider.getMemberFromAuthentication();
    if (null == member) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "사용자를 찾을 수 없습니다.");
    }
    return tokenProvider.deleteRefreshToken(member);
  }

  @Transactional(readOnly = true)
  public Member isPresentMember(String email) {
    Optional<Member> optionalMember = memberRepository.findByEmail(email);
    return optionalMember.orElse(null);
  }
  public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
    response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
    response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
    response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
  }

//  public ResponseDto<?> checkDuplicateEmail(String email) {
//
//          return ResponseDto.fail("DUPLICATED_NICKNAME", "중복된 이메일 입니다.");
//      }
//      return ResponseDto.success("사용가능한 이메일 입니다.");
//  }

//  checkemail >> 함수명 변경
//          is >> 불린반환

  public ResponseDto<?> checkDuplicateEmail(String email) {
    memberRepository.countByEmail(email);
    if (email == null) {
      return ResponseDto.fail("문제있는 코드야 ~", "이메일을 입력해주세요");
    }
    return ResponseDto.success("사용가능한 이메일입니다");
    }
  }


