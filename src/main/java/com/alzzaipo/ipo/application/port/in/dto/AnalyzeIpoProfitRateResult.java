package com.alzzaipo.ipo.application.port.in.dto;

import com.alzzaipo.ipo.domain.Ipo;
import lombok.Getter;

import java.util.List;

@Getter
public class AnalyzeIpoProfitRateResult {

    private final int averageProfitRate;
    private final List<Ipo> ipoDtoList;

    public AnalyzeIpoProfitRateResult(int averageProfitRate, List<Ipo> ipoDtoList) {
        this.averageProfitRate = averageProfitRate;
        this.ipoDtoList = ipoDtoList;
    }
}
