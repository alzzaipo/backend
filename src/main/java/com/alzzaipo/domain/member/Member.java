package com.alzzaipo.domain.member;

import com.alzzaipo.domain.BaseTimeEntity;
import com.alzzaipo.domain.portfolio.Portfolio;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String uid;

    private String password;

    private String nickname;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Portfolio> portfolios = new ArrayList<>();

    @Builder
    public Member(String uid, String password, String nickname, String email) {
        this.uid = uid;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }

    public void addPortfolio(Portfolio portfolio) {
        this.portfolios.add(portfolio);
        portfolio.setMember(this);
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
}