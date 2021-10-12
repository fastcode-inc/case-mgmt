package com.fastcode.example.application.core.authorization.userspermission.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CreateUserspermissionInput {

    @NotNull(message = "permissionId Should not be null")
    private Long permissionId;

    private Boolean revoked;

    @NotNull(message = "usersUsername Should not be null")
    @Length(max = 255, message = "usersUsername must be less than 255 characters")
    private String usersUsername;
}
