package com.alzzaipo.portfolio.application.service;

import com.alzzaipo.common.Id;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.ipo.application.port.out.FindIpoByStockCodePort;
import com.alzzaipo.ipo.domain.Ipo;
import com.alzzaipo.portfolio.application.dto.DeletePortfolioCommand;
import com.alzzaipo.portfolio.application.dto.FindPortfolioCommand;
import com.alzzaipo.portfolio.application.dto.MemberPortfolioSummary;
import com.alzzaipo.portfolio.application.dto.PortfolioView;
import com.alzzaipo.portfolio.application.dto.RegisterPortfolioCommand;
import com.alzzaipo.portfolio.application.dto.UpdatePortfolioCommand;
import com.alzzaipo.portfolio.application.port.in.DeletePortfolioUseCase;
import com.alzzaipo.portfolio.application.port.in.FindMemberPortfoliosQuery;
import com.alzzaipo.portfolio.application.port.in.FindPortfolioQuery;
import com.alzzaipo.portfolio.application.port.in.RegisterPortfolioUseCase;
import com.alzzaipo.portfolio.application.port.in.UpdatePortfolioUseCase;
import com.alzzaipo.portfolio.application.port.out.DeletePortfolioPort;
import com.alzzaipo.portfolio.application.port.out.FindMemberPortfoliosPort;
import com.alzzaipo.portfolio.application.port.out.FindPortfolioPort;
import com.alzzaipo.portfolio.application.port.out.RegisterPortfolioPort;
import com.alzzaipo.portfolio.application.port.out.UpdatePortfolioPort;
import com.alzzaipo.portfolio.domain.Portfolio;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PortfolioService implements RegisterPortfolioUseCase,
	FindMemberPortfoliosQuery,
	FindPortfolioQuery,
	UpdatePortfolioUseCase,
	DeletePortfolioUseCase {

	private final FindIpoByStockCodePort findIpoByStockCodePort;
	private final RegisterPortfolioPort registerPortfolioPort;
	private final FindPortfolioPort findPortfolioPort;
	private final FindMemberPortfoliosPort findMemberPortfoliosPort;
	private final DeletePortfolioPort deletePortfolioPort;
	private final UpdatePortfolioPort updatePortfolioPort;

	@Override
	public void registerPortfolio(RegisterPortfolioCommand command) {
		Ipo ipo = findIpoByStockCodePort.findByStockCode(command.getStockCode());

		Portfolio portfolio = Portfolio.build(command.getMemberId(), ipo, command.getProfit(), command.getSharesCnt(),
			command.getAgents(), command.getAgents());

		registerPortfolioPort.registerPortfolio(portfolio);
	}

	@Override
	@Transactional(readOnly = true)
	public PortfolioView findPortfolio(FindPortfolioCommand command) {
		Portfolio portfolio = findPortfolioPort.findPortfolio(command.getPortfolioId())
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "포트폴리오 조회 실패"));

		if (!portfolio.getMemberId().equals(command.getMemberId())) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "포트폴리오 권한 없음");
		}

		return PortfolioView.build(portfolio);
	}

	@Override
	@Transactional(readOnly = true)
	public MemberPortfolioSummary findMemberPortfolios(Id memberId) {
		List<PortfolioView> portfolioList = findMemberPortfoliosPort.findMemberPortfolios(memberId)
			.stream()
			.map(PortfolioView::build)
			.collect(Collectors.toList());

		return MemberPortfolioSummary.build(portfolioList);
	}

	@Override
	public void updatePortfolio(UpdatePortfolioCommand command) {
		Portfolio portfolio = findPortfolioPort.findPortfolio(command.getPortfolioId())
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "포트폴리오 조회 실패"));

		if (!portfolio.getMemberId().equals(command.getMemberId())) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "포트폴리오 권한 없음");
		}

		Ipo ipo = findIpoByStockCodePort.findByStockCode(command.getStockCode());

		portfolio.updatePortfolio(command, ipo);

		updatePortfolioPort.updatePortfolio(portfolio);
	}

	@Override
	public void deletePortfolio(DeletePortfolioCommand command) {
		Portfolio targetPortfolio = findPortfolioPort.findPortfolio(command.getPortfolioId())
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "포트폴리오 조회 실패"));

		if (!targetPortfolio.getMemberId().equals(command.getMemberId())) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "포트폴리오 권한 없음");
		}

		deletePortfolioPort.deletePortfolio(command.getPortfolioId());
	}
}
