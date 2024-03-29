package com.alzzaipo.member.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocalAccountPasswordDto {

    @NotBlank
    @Size(min = 8, max = 16)
    String accountPassword;
}
