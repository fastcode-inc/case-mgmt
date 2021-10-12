package com.fastcode.example.application.core.task.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindTaskByIdOutput {

    private String message;
    private String status;
    private Long taskId;
    private String type;
    private String username;
    private Long caseId;
    private Long casesDescriptiveField;
    private Long versiono;
}
