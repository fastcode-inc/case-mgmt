package com.fastcode.example.addons.reporting.domain.dashboard;

import com.fastcode.example.addons.reporting.application.dashboard.dto.DashboardDetailsOutput;
import java.time.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDashboardRepositoryCustom {
    Page<DashboardDetailsOutput> getAllDashboardsByUsersId(String userUsername, String search, Pageable pageable);

    Page<DashboardDetailsOutput> getAvailableDashboardsByUsersId(
        String userUsername,
        Long reportId,
        String search,
        Pageable pageable
    );
}
