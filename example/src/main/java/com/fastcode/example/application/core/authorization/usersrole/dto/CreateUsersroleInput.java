package com.fastcode.example.application.core.authorization.usersrole.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CreateUsersroleInput {

    @NotNull(message = "roleId Should not be null")
    private Long roleId;

    @NotNull(message = "usersUsername Should not be null")
    @Length(max = 255, message = "usersUsername must be less than 255 characters")
    private String usersUsername;
}
