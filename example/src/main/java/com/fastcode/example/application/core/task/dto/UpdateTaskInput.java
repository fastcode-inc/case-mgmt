package com.fastcode.example.application.core.task.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskInput {

    private String message;

    private String status;

    @NotNull(message = "taskId Should not be null")
    private Long taskId;

    private String type;

    private String username;

    private Long caseId;
    private Long versiono;
}
