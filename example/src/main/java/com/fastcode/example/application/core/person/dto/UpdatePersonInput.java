package com.fastcode.example.application.core.person.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePersonInput {

    private LocalDate birthDate;

    private String comments;

    private String givenName;

    private String homePhone;

    @NotNull(message = "personId Should not be null")
    private Long personId;

    private String surname;

    private String username;
    private Long versiono;
}
