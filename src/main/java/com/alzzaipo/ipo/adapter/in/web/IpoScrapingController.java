package com.alzzaipo.ipo.adapter.in.web;

import com.alzzaipo.ipo.adapter.in.web.dto.ScrapeAndRegisterIposWebRequest;
import com.alzzaipo.ipo.application.port.in.ScrapeAndRegisterIposUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IpoScrapingController {

	private final ScrapeAndRegisterIposUseCase scrapeAndRegisterIposUseCase;

	@PostMapping("/scraper")
	public ResponseEntity<String> scrapeAndRegisterIpos(@Valid @RequestBody ScrapeAndRegisterIposWebRequest request) {
		int totalRegistrationCount = scrapeAndRegisterIposUseCase.scrapeAndRegister(request.toCommand());
		return ResponseEntity.ok("[성공] " + totalRegistrationCount + "건 업데이트 완료");
	}
}
