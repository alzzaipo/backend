package com.alzzaipo.hexagonal.member.adapter.in.web;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocalLoginWebRequest {

    private String accountId;
    private String accountPassword;
}