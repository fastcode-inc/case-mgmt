package com.fastcode.example.application.core.casedocument.dto;

import java.time.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCaseDocumentInput {

    @NotNull(message = "caseId Should not be null")
    private Long caseId;

    @NotNull(message = "fileId Should not be null")
    private Long fileId;
}
