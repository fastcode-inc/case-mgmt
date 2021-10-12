package com.fastcode.example.application.core.authorization.users.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUsersInput {

    private String displayName;

    @Pattern(
        regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
        message = "Email Address should be valid"
    )
    @NotNull(message = "email Should not be null")
    private String email;

    @NotNull(message = "isActive Should not be null")
    private Boolean isActive = false;

    @NotNull(message = "isEmailConfirmed Should not be null")
    private Boolean isEmailConfirmed;

    private String password;

    @NotNull(message = "username Should not be null")
    private String username;

    private Long versiono;
}
