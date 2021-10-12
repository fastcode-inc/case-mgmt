package com.fastcode.example.addons.reporting.application.report.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUsersOutput {

    private String displayName;
    private String email;
    private Boolean isActive;
    private Boolean isEmailConfirmed;
    private String username;
    private Long reportId;
}
