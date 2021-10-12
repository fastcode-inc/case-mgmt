package com.fastcode.example.application.core.casehistory.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindCaseHistoryByIdOutput {

    private Long caseHistoryId;
    private String message;
    private OffsetTime timestamp;
    private Long caseId;
    private Long casesDescriptiveField;
    private Long versiono;
}
