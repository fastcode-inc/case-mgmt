package com.fastcode.example.application.core.authorization.users.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserspreferenceOutput {

    private String displayName;
    private Boolean isActive;
    private String password;
    private String email;
    private Boolean isEmailConfirmed;
    private String username;
    private String usersUsername;
}
