package com.fastcode.example.addons.reporting.application.dashboard;

import com.fastcode.example.addons.reporting.application.dashboard.dto.*;
import com.fastcode.example.addons.reporting.application.report.dto.FindReportByIdOutput;
import com.fastcode.example.commons.search.SearchCriteria;
import java.time.*;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

public interface IDashboardAppService {
    CreateDashboardOutput create(CreateDashboardInput dashboard);

    void delete(Long id, String userUsername);

    void deleteReportFromDashboard(Long dashboardId, Long reportId, String userUsername);

    UpdateDashboardOutput update(Long id, UpdateDashboardInput input);

    FindDashboardByIdOutput findById(Long id);

    List<FindDashboardByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

    List<FindReportByIdOutput> setReportsList(Long dashboardId, String userUsername, String version);

    FindDashboardByIdOutput findByDashboardIdAndUsersId(Long dashboardId, String userUsername, String version);

    List<DashboardDetailsOutput> getDashboards(String userUsername, String search, Pageable pageable) throws Exception;

    List<DashboardDetailsOutput> getAvailableDashboards(
        String userUsername,
        Long reportId,
        String search,
        Pageable pageable
    )
        throws Exception;

    FindDashboardByIdOutput publishDashboard(String userUsername, Long dashboardId);

    FindDashboardByIdOutput refreshDashboard(String userUsername, Long dashboardId);

    FindDashboardByIdOutput addNewReportsToNewDashboard(AddNewReportToNewDashboardInput input);

    FindDashboardByIdOutput addNewReportsToExistingDashboard(AddNewReportToExistingDashboardInput input);

    FindDashboardByIdOutput addExistingReportsToNewDashboard(AddExistingReportToNewDashboardInput input);

    FindDashboardByIdOutput addExistingReportsToExistingDashboard(AddExistingReportToExistingDashboardInput input);

    Map<String, String> parseReportdashboardJoinColumn(String keysString);
}
