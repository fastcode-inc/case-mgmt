package com.fastcode.example.application.core.authorization.users.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUsersOutput {

    private String displayName;
    private String email;
    private Boolean isActive;
    private Boolean isEmailConfirmed;
    private String username;
    private String theme;
    private String language;
}
