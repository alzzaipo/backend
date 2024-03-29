package com.alzzaipo.member.application.port.in.account.social;

import com.alzzaipo.member.application.port.in.dto.AuthorizationCode;
import com.alzzaipo.member.application.port.in.dto.LoginResult;

public interface KakaoLoginUseCase {

    LoginResult handleLogin(AuthorizationCode authorizationCode);
}
