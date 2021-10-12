package com.fastcode.example.application.core.casehistory.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCaseHistoryInput {

    @NotNull(message = "caseHistoryId Should not be null")
    private Long caseHistoryId;

    private String message;

    @NotNull(message = "timestamp Should not be null")
    private OffsetTime timestamp;

    private Long caseId;
    private Long versiono;
}
