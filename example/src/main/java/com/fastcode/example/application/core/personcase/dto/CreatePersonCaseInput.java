package com.fastcode.example.application.core.personcase.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePersonCaseInput {

    @NotNull(message = "caseId Should not be null")
    private Long caseId;

    @NotNull(message = "personId Should not be null")
    private Long personId;
}
