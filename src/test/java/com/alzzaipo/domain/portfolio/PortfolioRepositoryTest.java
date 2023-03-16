package com.alzzaipo.domain.portfolio;

import com.alzzaipo.domain.ipo.IPO;
import com.alzzaipo.domain.ipo.IPORepository;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.domain.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class PortfolioRepositoryTest {

    private final IPORepository ipoRepository;
    private final PortfolioRepository portfolioRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public PortfolioRepositoryTest(IPORepository ipoRepository, PortfolioRepository portfolioRepository, MemberRepository memberRepository) {
        this.ipoRepository = ipoRepository;
        this.portfolioRepository = portfolioRepository;
        this.memberRepository = memberRepository;
    }

    @Test
    public void 저장조회삭제() throws Exception {
        //given
        IPO ipo = IPO.builder()
                .fixedOfferingPrice(10000)
                .build();

        Member member = Member.builder()
                .accountId("test")
                .accountPassword("test")
                .email("test")
                .nickname("test")
                .build();

        Portfolio portfolio = Portfolio.builder()
                .member(member)
                .ipo(ipo)
                .sharesCnt(2)
                .profit(15000)
                .agents("KB증권")
                .memo("테스트")
                .build();

        ipoRepository.save(ipo);
        memberRepository.save(member);

        //when
        portfolioRepository.save(portfolio);
        Portfolio findPortfolio = portfolioRepository.findById(portfolio.getId()).get();

        //then
        assertThat(portfolio).isSameAs(findPortfolio);


        //when
        portfolioRepository.delete(findPortfolio);

        //then
        assertThat(portfolioRepository.findAll().size()).isEqualTo(0);
    }

}