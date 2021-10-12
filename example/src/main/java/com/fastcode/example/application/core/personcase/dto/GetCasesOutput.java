package com.fastcode.example.application.core.personcase.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCasesOutput {

    private Long caseId;
    private String status;
    private String summary;
    private String type;
    private Long personCaseCaseId;
    private Long personCasePersonId;
}
