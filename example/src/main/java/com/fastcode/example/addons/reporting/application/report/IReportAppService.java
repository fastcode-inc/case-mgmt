package com.fastcode.example.addons.reporting.application.report;

import com.fastcode.example.addons.reporting.application.report.dto.*;
import com.fastcode.example.commons.search.SearchCriteria;
import java.time.*;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

public interface IReportAppService {
    CreateReportOutput create(CreateReportInput report);

    void delete(Long id, String userUsername);

    UpdateReportOutput update(Long id, UpdateReportInput input);

    FindReportByIdOutput findById(Long id);

    List<FindReportByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

    List<ReportDetailsOutput> getReports(String userUsername, String search, Pageable pageable) throws Exception;

    FindReportByIdOutput findByReportIdAndUsersId(Long reportId, String userUsername, String version);

    ReportDetailsOutput publishReport(String userUsername, Long reportId);

    ReportDetailsOutput refreshReport(String userUsername, Long reportId);

    Map<String, String> parseReportdashboardJoinColumn(String keysString);
}
