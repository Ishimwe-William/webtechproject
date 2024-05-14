package com.bunsen.webtechproject.api.model;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginBody {
    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String password;
}