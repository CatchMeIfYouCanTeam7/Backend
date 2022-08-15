package com.sparta.catchme.domain;

import com.sparta.catchme.dto.request.QuestionRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Question extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private String hint;

    @Column(nullable = false)
    private String answer;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public void update(QuestionRequestDto questionRequestDto, String imgUrl) {
        this.imgUrl = imgUrl;
        this.hint = questionRequestDto.getHint();
        this.answer = questionRequestDto.getAnswer();
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }
}
