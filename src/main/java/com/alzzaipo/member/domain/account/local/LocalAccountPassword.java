package com.alzzaipo.member.domain.account.local;

import com.alzzaipo.common.exception.CustomException;
import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;

public class LocalAccountPassword {

    private final String accountPassword;

    public LocalAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;

        verifyFormat();
    }

    public String get() {
        return accountPassword;
    }

    private void verifyFormat() {
        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[~!@#$%^&*_\\-+=`|\\\\(){}\\[\\]:;\"'<>,.?/]).{8,16}$";

        if (!Pattern.matches(regex, accountPassword)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "비밀번호 형식 오류");
        }
    }
}
