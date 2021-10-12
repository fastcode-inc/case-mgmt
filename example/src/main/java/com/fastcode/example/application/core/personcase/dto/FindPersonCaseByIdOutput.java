package com.fastcode.example.application.core.personcase.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindPersonCaseByIdOutput {

    private Long caseId;
    private Long personId;
    private Long casesDescriptiveField;
    private Long personDescriptiveField;
    private Long versiono;
}
