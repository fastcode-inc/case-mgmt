package com.fastcode.example.addons.reporting.domain.dashboardversion;

import java.io.Serializable;
import java.time.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DashboardversionId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long dashboardId;
    private String userUsername;
    private String dashboardVersion;

    public DashboardversionId(String userUsername, Long dashboardId, String dashboardVersion) {
        super();
        this.dashboardId = dashboardId;
        this.userUsername = userUsername;
        this.dashboardVersion = dashboardVersion;
    }
}
