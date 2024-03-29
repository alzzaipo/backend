package com.alzzaipo.common.email.adapter.out.smtp;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.common.email.application.port.out.smtp.SendCustomEmailPort;
import com.alzzaipo.common.email.application.port.out.smtp.SendEmailVerificationCodePort;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.common.util.EmailUtil;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Slf4j
@Component
@Repository
@RequiredArgsConstructor
public class SendEmailAdapter implements SendCustomEmailPort, SendEmailVerificationCodePort {

	@Value("${smtp.email}")
	private String from;

	private final JavaMailSender javaMailSender;

	@Override
	public void send(Email to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to.get());
		message.setSubject(subject);
		message.setText(text);

		try {
			javaMailSender.send(message);
		} catch (Exception e) {
			log.error("알림 메일 전송 실패 : " + e.getMessage());
		}
	}

	@Override
	public EmailVerificationCode sendVerificationCode(Email email) {
		EmailVerificationCode verificationCode = generateEmailVerificationCode();

		SimpleMailMessage simpleMailMessage = EmailUtil.createMailMessage(email.get(),
			"[알짜공모주] 이메일 인증코드",
			"이메일 인증코드 : " + verificationCode.get());

		try {
			javaMailSender.send(simpleMailMessage);
		} catch (MailException e) {
			log.error("MailException : {}", e.getMessage());
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송 실패");
		}

		return verificationCode;
	}

	private EmailVerificationCode generateEmailVerificationCode() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder verificationCode = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < EmailVerificationCode.length; i++) {
			int index = random.nextInt(characters.length());
			verificationCode.append(characters.charAt(index));
		}

		return new EmailVerificationCode(verificationCode.toString());
	}
}
