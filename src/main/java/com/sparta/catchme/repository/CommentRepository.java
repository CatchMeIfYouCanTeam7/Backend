package com.sparta.catchme.repository;

import com.sparta.catchme.domain.Comment;
import com.sparta.catchme.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByQuestion(Question question);
}
