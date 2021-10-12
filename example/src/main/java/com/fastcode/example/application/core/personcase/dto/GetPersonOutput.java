package com.fastcode.example.application.core.personcase.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPersonOutput {

    private LocalDate birthDate;
    private String comments;
    private String givenName;
    private String homePhone;
    private Long personId;
    private String surname;
    private Long personCaseCaseId;
    private Long personCasePersonId;
}
