package com.alzzaipo.member.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SendSignUpEmailVerificationCodeWebRequest {

	@NotBlank
	private String accountId;

	@Email
	private String email;

}
