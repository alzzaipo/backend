package com.alzzaipo.portfolio.adapter.out.persistence;

import com.alzzaipo.common.BaseTimeEntity;
import com.alzzaipo.ipo.adapter.out.persistence.IpoJpaEntity;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PortfolioJpaEntity extends BaseTimeEntity {

    @Id
    @Column(name = "portfolio_uid", unique = true)
    private Long portfolioUID;

    private int sharesCnt;

    private Long profit;

    private Long profitRate;

    private String agents;

    @Column(length = 200)
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_uid")
    private MemberJpaEntity memberJpaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ipo_id")
    private IpoJpaEntity ipoJpaEntity;

    public PortfolioJpaEntity(Long portfolioUID, int sharesCnt, Long profit, Long profitRate, String agents, String memo, MemberJpaEntity memberJpaEntity, IpoJpaEntity ipoJpaEntity) {
        this.portfolioUID = portfolioUID;
        this.sharesCnt = sharesCnt;
        this.profit = profit;
        this.profitRate = profitRate;
        this.agents = agents;
        this.memo = memo;
        this.memberJpaEntity = memberJpaEntity;
        this.ipoJpaEntity = ipoJpaEntity;
    }

    public void setPortfolioUID(Long portfolioUID) {
        this.portfolioUID = portfolioUID;
    }
}