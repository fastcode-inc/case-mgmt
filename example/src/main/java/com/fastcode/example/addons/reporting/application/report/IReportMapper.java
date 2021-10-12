package com.fastcode.example.addons.reporting.application.report;

import com.fastcode.example.addons.reporting.application.report.dto.*;
import com.fastcode.example.addons.reporting.application.reportversion.dto.CreateReportversionInput;
import com.fastcode.example.addons.reporting.application.reportversion.dto.CreateReportversionOutput;
import com.fastcode.example.addons.reporting.application.reportversion.dto.UpdateReportversionInput;
import com.fastcode.example.addons.reporting.application.reportversion.dto.UpdateReportversionOutput;
import com.fastcode.example.addons.reporting.domain.report.Report;
import com.fastcode.example.addons.reporting.domain.reportversion.Reportversion;
import com.fastcode.example.domain.core.authorization.users.Users;
import java.time.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface IReportMapper {
    Report createReportInputToReport(CreateReportInput reportDto);

    @Mappings({ @Mapping(source = "ownerUsername", target = "userUsername") })
    CreateReportversionInput createReportInputToCreateReportversionInput(CreateReportInput reportDto);

    @Mappings(
        {
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.users.username", target = "ownerUsername"),
            @Mapping(source = "entity.users.username", target = "ownerDescriptiveField"),
        }
    )
    CreateReportOutput reportAndCreateReportversionOutputToCreateReportOutput(
        Report entity,
        CreateReportversionOutput reportversionOutput
    );

    @Mappings(
        {
            @Mapping(source = "report.id", target = "id"),
            @Mapping(source = "report.users.username", target = "ownerUsername"),
            @Mapping(source = "report.users.username", target = "ownerDescriptiveField"),
        }
    )
    CreateReportOutput reportAndReportversionToCreateReportOutput(Report report, Reportversion reportversion);

    FindReportByIdOutput createReportOutputToFindReportByIdOutput(CreateReportOutput report);

    Report updateReportInputToReport(UpdateReportInput reportDto);

    UpdateReportversionInput updateReportInputToUpdateReportversionInput(UpdateReportInput reportDto);

    @Mappings(
        {
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.users.username", target = "ownerUsername"),
            @Mapping(source = "entity.users.username", target = "ownerDescriptiveField"),
        }
    )
    UpdateReportOutput reportAndUpdateReportversionOutputToUpdateReportOutput(
        Report entity,
        UpdateReportversionOutput reportversion
    );

    @Mappings(
        {
            @Mapping(source = "reportversion.userUsername", target = "userUsername"),
            @Mapping(source = "report.users.username", target = "ownerUsername"),
            @Mapping(source = "reportversion.reportId", target = "id"),
            @Mapping(source = "report.versiono", target = "versiono"),
        }
    )
    FindReportByIdOutput reportEntitiesToFindReportByIdOutput(Report report, Reportversion reportversion);

    @Mappings(
        {
            @Mapping(source = "entity.users.username", target = "ownerUsername"),
            @Mapping(source = "entity.versiono", target = "versiono"),
        }
    )
    FindReportByIdOutput reportToFindReportByIdOutput(Report entity, Reportversion reportversion);

    @Mappings(
        {
            @Mapping(source = "users.username", target = "username"),
            @Mapping(source = "report.id", target = "reportId"),
        }
    )
    GetUsersOutput usersToGetUsersOutput(Users users, Report report);

    @Mappings(
        {
            @Mapping(source = "reportversion.userUsername", target = "userUsername"),
            @Mapping(source = "reportversion.reportId", target = "id"),
        }
    )
    ReportDetailsOutput reportEntitiesToReportDetailsOutput(Report report, Reportversion reportversion);
}
