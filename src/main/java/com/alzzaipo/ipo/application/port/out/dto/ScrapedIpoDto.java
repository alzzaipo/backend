package com.alzzaipo.ipo.application.port.out.dto;

import com.alzzaipo.ipo.domain.Ipo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ScrapedIpoDto {

    // 종목명
    private final String stockName;

    // 희망공모가 하단
    private final int expectedOfferingPriceMin;

    // 희망공모가 상단
    private final int expectedOfferingPriceMax;

    // 최종공모가
    private final int fixedOfferingPrice;

    // 공모금액
    private final int totalAmount;

    // 기관경쟁률
    private final int competitionRate;

    // 의무보유확약비율
    private final int lockupRate;

    // 주관사
    private final String agents;

    // 청약 시작일
    private final LocalDate subscribeStartDate;

    // 청약 종료일
    private final LocalDate subscribeEndDate;

    // 상장일
    private final LocalDate listedDate;

    // 종목 코드
    private final int stockCode;

    public Ipo toDomainEntity() {
        return new Ipo(
            stockName,
            stockCode,
            expectedOfferingPriceMin,
            expectedOfferingPriceMax,
            fixedOfferingPrice,
            totalAmount,
            competitionRate,
            lockupRate,
            agents,
            subscribeStartDate,
            subscribeEndDate,
            listedDate,
            0, 0);
    }
}
