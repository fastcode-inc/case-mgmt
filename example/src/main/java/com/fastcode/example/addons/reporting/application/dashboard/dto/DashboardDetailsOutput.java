package com.fastcode.example.addons.reporting.application.dashboard.dto;

import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardDetailsOutput {

    private String userUsername;
    private Long id;
    private String description;
    private String dashboardVersion;
    private Boolean isPublished;
    private String title;
    private Boolean isRefreshed;
    private String ownerDescriptiveField;
}
