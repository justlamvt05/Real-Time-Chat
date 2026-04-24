package com.lamthoncoding.realtimechat.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "{email.is.not.empty}")
    // @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
    // message = "{email.is.invalid}")
    private String username;

    @NotBlank(message = "{password.is.not.empty}")
    private String password;
}
