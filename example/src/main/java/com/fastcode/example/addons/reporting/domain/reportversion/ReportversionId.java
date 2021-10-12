package com.fastcode.example.addons.reporting.domain.reportversion;

import java.io.Serializable;
import java.time.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportversionId implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userUsername;
    private Long reportId;
    private String reportVersion;

    public ReportversionId(String userUsername, Long reportId, String reportVersion) {
        super();
        this.userUsername = userUsername;
        this.reportId = reportId;
        this.reportVersion = reportVersion;
    }
}
