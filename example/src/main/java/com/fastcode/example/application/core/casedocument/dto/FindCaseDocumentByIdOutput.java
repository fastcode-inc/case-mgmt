package com.fastcode.example.application.core.casedocument.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindCaseDocumentByIdOutput {

    private Long caseId;
    private Long fileId;
    private Long casesDescriptiveField;
    private Long versiono;
}
