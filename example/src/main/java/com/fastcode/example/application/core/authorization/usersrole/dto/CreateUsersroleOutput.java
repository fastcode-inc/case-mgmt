package com.fastcode.example.application.core.authorization.usersrole.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUsersroleOutput {

    private Long roleId;
    private String usersUsername;
    private String roleDescriptiveField;
    private String usersDescriptiveField;
}
