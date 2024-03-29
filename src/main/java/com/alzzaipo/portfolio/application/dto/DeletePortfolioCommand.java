package com.alzzaipo.portfolio.application.dto;

import com.alzzaipo.common.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeletePortfolioCommand {

    private final Id memberId;
    private final Id portfolioId;
}
