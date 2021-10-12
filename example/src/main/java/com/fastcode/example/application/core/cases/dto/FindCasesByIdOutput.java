package com.fastcode.example.application.core.cases.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindCasesByIdOutput {

    private Long caseId;
    private String status;
    private String summary;
    private String type;
    private Long versiono;
}
