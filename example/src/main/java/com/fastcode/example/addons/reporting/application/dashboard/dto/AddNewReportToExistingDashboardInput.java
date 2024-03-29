package com.fastcode.example.addons.reporting.application.dashboard.dto;

import com.fastcode.example.addons.reporting.application.report.dto.CreateReportInput;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddNewReportToExistingDashboardInput {

    private Long id;
    private String description;
    private String title;
    private String ownerUsername;
    private Boolean isPublished;
    List<CreateReportInput> reportDetails = new ArrayList<>();
}
