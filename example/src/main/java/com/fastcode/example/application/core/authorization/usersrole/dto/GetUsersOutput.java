package com.fastcode.example.application.core.authorization.usersrole.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUsersOutput {

    private Boolean isActive;
    private String password;
    private String email;
    private Boolean isEmailConfirmed;
    private String username;
    private Long usersroleRoleId;
    private String usersroleUsersUsername;
}
