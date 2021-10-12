package com.fastcode.example.addons.reporting.application.dashboard.dto;

import com.fastcode.example.addons.reporting.application.report.dto.FindReportByIdOutput;
import java.time.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindDashboardByIdOutput {

    private String description;
    private String title;
    private String userUsername;
    private Long id;
    private Boolean isPublished;
    private Boolean isRefreshed;
    private String ownerUsername;
    private String ownerDescriptiveField;
    private List<FindReportByIdOutput> reportDetails;
    private Long versiono;
}
