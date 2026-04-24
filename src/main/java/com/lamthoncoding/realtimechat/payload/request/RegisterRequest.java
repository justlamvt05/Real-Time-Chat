package com.lamthoncoding.realtimechat.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "{email.is.not.empty}")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "{email.is.invalid}"
    )
    private String email;

    @NotBlank(message = "{username.is.not.empty}")
    private String username;

    @NotBlank(message = "{password.is.not.empty}")
    private String password;

    @NotBlank(message = "{confirm.password.is.not.empty}")
    private String confirmPassword;


    @NotBlank(message = "{full.name.is.not.empty}")
    private String fullName;

    private String gender;

    @NotBlank(message = "{phone.is.not.empty}")
    @Pattern(
            regexp = "\\d{10}",
            message = "{phone.is.invalid}"
    )
    private String phone;



}


