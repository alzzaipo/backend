package com.alzzaipo.portfolio.adapter.in.web;

import com.alzzaipo.common.Id;
import com.alzzaipo.common.MemberPrincipal;
import com.alzzaipo.portfolio.adapter.in.web.dto.RegisterPortfolioWebRequest;
import com.alzzaipo.portfolio.adapter.in.web.dto.UpdatePortfolioWebRequest;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final RegisterPortfolioUseCase registerPortfolioUseCase;
    private final FindMemberPortfoliosQuery findMemberPortfoliosQuery;
    private final UpdatePortfolioUseCase updatePortfolioUseCase;
    private final DeletePortfolioUseCase deletePortfolioUseCase;
    private final FindPortfolioQuery findPortfolioQuery;

    @PostMapping("/create")
    public ResponseEntity<String> createPortfolio(
        @AuthenticationPrincipal MemberPrincipal principal,
        @Valid @RequestBody RegisterPortfolioWebRequest request
    ) {
        RegisterPortfolioCommand command = request.toCommand(principal.getMemberId());
        registerPortfolioUseCase.registerPortfolio(command);
        return ResponseEntity.ok().body("포트폴리오 생성 완료");
    }

    @GetMapping
    public ResponseEntity<PortfolioView> find(
        @AuthenticationPrincipal MemberPrincipal principal,
        @RequestParam("id") String portfolioId
    ) {
        FindPortfolioCommand command = new FindPortfolioCommand(principal.getMemberId(), Id.fromString(portfolioId));
        PortfolioView portfolio = findPortfolioQuery.findPortfolio(command);
        return ResponseEntity.ok().body(portfolio);
    }

    @GetMapping("/list")
    public ResponseEntity<MemberPortfolioSummary> findPortfolios(@AuthenticationPrincipal MemberPrincipal principal) {
        MemberPortfolioSummary memberPortfolioSummary = findMemberPortfoliosQuery.findMemberPortfolios(
            principal.getMemberId());
        return ResponseEntity.ok().body(memberPortfolioSummary);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateMemberPortfolio(
        @AuthenticationPrincipal MemberPrincipal principal,
        @Valid @RequestBody UpdatePortfolioWebRequest request
    ) {
        UpdatePortfolioCommand command = request.toCommand(principal.getMemberId());
        updatePortfolioUseCase.updatePortfolio(command);
        return ResponseEntity.ok().body("포트폴리오 수정 완료");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal MemberPrincipal principal,
        @RequestParam("id") String id
    ) {
        DeletePortfolioCommand command = new DeletePortfolioCommand(principal.getMemberId(), Id.fromString(id));
        deletePortfolioUseCase.deletePortfolio(command);
        return ResponseEntity.ok().body("포트폴리오 삭제 완료");
    }
}
