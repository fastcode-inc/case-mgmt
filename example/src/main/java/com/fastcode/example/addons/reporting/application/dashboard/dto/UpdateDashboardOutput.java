package com.fastcode.example.addons.reporting.application.dashboard.dto;

import com.fastcode.example.addons.reporting.application.report.dto.FindReportByIdOutput;
import java.time.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDashboardOutput {

    private String description;
    private Long id;
    private String title;
    private String ownerUsername;
    private String ownerDescriptiveField;
    private List<FindReportByIdOutput> reportDetails;
    private Boolean isRefreshed;
    private Long versiono;
}
