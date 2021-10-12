package com.fastcode.example.addons.reporting.application.dashboard;

import com.fastcode.example.addons.reporting.application.dashboard.dto.*;
import com.fastcode.example.addons.reporting.application.dashboardversion.dto.CreateDashboardversionInput;
import com.fastcode.example.addons.reporting.application.dashboardversion.dto.CreateDashboardversionOutput;
import com.fastcode.example.addons.reporting.application.dashboardversion.dto.UpdateDashboardversionInput;
import com.fastcode.example.addons.reporting.application.dashboardversion.dto.UpdateDashboardversionOutput;
import com.fastcode.example.addons.reporting.application.report.dto.CreateReportInput;
import com.fastcode.example.addons.reporting.domain.dashboard.Dashboard;
import com.fastcode.example.addons.reporting.domain.dashboardversion.Dashboardversion;
import com.fastcode.example.addons.reporting.domain.report.Report;
import com.fastcode.example.domain.core.authorization.users.Users;
import java.time.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface IDashboardMapper {
    @Mappings({ @Mapping(source = "ownerUsername", target = "userUsername") })
    CreateDashboardversionInput creatDashboardInputToCreateDashboardversionInput(CreateDashboardInput dashboardDto);

    Dashboard createDashboardInputToDashboard(CreateDashboardInput dashboardDto);

    CreateDashboardInput addNewReportToNewDashboardInputTocreatDashboardInput(AddNewReportToNewDashboardInput input);

    CreateDashboardInput addExistingReportToNewDashboardInputTocreatDashboardInput(
        AddExistingReportToNewDashboardInput input
    );

    Dashboard addExistingReportToNewDashboardInputToDashboard(AddExistingReportToNewDashboardInput input);

    Report createDashboardAndReportInputToReport(CreateReportInput reportDto);

    @Mappings(
        {
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.users.username", target = "ownerUsername"),
            @Mapping(source = "entity.users.username", target = "ownerDescriptiveField"),
        }
    )
    CreateDashboardOutput dashboardAndCreateDashboardversionOutputToCreateDashboardOutput(
        Dashboard entity,
        CreateDashboardversionOutput dashboardversion
    );

    @Mappings(
        {
            @Mapping(source = "dashboardversion.dashboardId", target = "id"),
            @Mapping(source = "dashboardversion.userUsername", target = "ownerUsername"),
            @Mapping(source = "dashboardversion.users.username", target = "ownerDescriptiveField"),
        }
    )
    CreateDashboardOutput dashboardAndDashboardversionToCreateDashboardOutput(
        Dashboard dashboard,
        Dashboardversion dashboardversion
    );

    UpdateDashboardversionInput updateDashboardInputToUpdateDashboardversionInput(UpdateDashboardInput dashboardDto);

    @Mappings(
        {
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.users.username", target = "ownerUsername"),
            @Mapping(source = "entity.users.username", target = "ownerDescriptiveField"),
        }
    )
    UpdateDashboardOutput dashboardAndUpdateDashboardversionOutputToUpdateDashboardOutput(
        Dashboard entity,
        UpdateDashboardversionOutput dashboardversion
    );

    Dashboard updateDashboardInputToDashboard(UpdateDashboardInput dashboardDto);

    @Mappings({ @Mapping(source = "users.username", target = "ownerUsername") })
    UpdateDashboardOutput dashboardToUpdateDashboardOutput(Dashboard entity);

    @Mappings(
        {
            @Mapping(source = "users.username", target = "ownerUsername"),
            @Mapping(source = "users.username", target = "ownerDescriptiveField"),
        }
    )
    FindDashboardByIdOutput dashboardToFindDashboardByIdOutput(Dashboard entity);

    @Mappings({ @Mapping(source = "ownerUsername", target = "userUsername") })
    FindDashboardByIdOutput dashboardOutputToFindDashboardByIdOutput(CreateDashboardOutput entity);

    @Mappings(
        {
            @Mapping(source = "dashboardversion.userUsername", target = "userUsername"),
            @Mapping(source = "dashboard.users.username", target = "ownerUsername"),
            @Mapping(source = "dashboardversion.dashboardId", target = "id"),
            @Mapping(source = "dashboard.versiono", target = "versiono"),
        }
    )
    FindDashboardByIdOutput dashboardEntitiesToFindDashboardByIdOutput(
        Dashboard dashboard,
        Dashboardversion dashboardversion
    );

    @Mappings(
        {
            @Mapping(source = "dashboardversion.userUsername", target = "userUsername"),
            @Mapping(source = "dashboardversion.dashboardId", target = "id"),
        }
    )
    DashboardDetailsOutput dashboardEntitiesToDashboardDetailsOutput(
        Dashboard dashboard,
        Dashboardversion dashboardversion
    );

    @Mappings(
        {
            @Mapping(source = "users.username", target = "username"),
            @Mapping(source = "dashboard.id", target = "dashboardId"),
        }
    )
    GetUsersOutput usersToGetUsersOutput(Users users, Dashboard dashboard);
}
