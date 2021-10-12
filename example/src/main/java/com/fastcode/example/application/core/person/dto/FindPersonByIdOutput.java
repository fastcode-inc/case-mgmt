package com.fastcode.example.application.core.person.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindPersonByIdOutput {

    private LocalDate birthDate;
    private String comments;
    private String givenName;
    private String homePhone;
    private Long personId;
    private String surname;
    private String username;
    private String usersDescriptiveField;
    private Long versiono;
}
