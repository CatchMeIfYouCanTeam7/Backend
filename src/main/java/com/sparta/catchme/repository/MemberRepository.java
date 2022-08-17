package com.sparta.catchme.repository;

import com.sparta.catchme.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findById(Long id);
  Optional<Member> findByEmail(String email);

  Optional<Member> findByNickname(String nickname);


  long countByEmail(String email);

  long countByNickname(String nickname);
}


