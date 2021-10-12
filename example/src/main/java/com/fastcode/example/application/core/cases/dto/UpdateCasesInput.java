package com.fastcode.example.application.core.cases.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCasesInput {

    @NotNull(message = "caseId Should not be null")
    private Long caseId;

    private String status;

    private String summary;

    private String type;

    private Long versiono;
}
