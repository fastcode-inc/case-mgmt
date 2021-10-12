package com.fastcode.example.application.core.casehistory;

import com.fastcode.example.application.core.casehistory.dto.*;
import com.fastcode.example.domain.core.casehistory.CaseHistory;
import com.fastcode.example.domain.core.cases.Cases;
import java.time.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ICaseHistoryMapper {
    CaseHistory createCaseHistoryInputToCaseHistory(CreateCaseHistoryInput caseHistoryDto);

    @Mappings(
        {
            @Mapping(source = "entity.cases.caseId", target = "caseId"),
            @Mapping(source = "entity.cases.caseId", target = "casesDescriptiveField"),
        }
    )
    CreateCaseHistoryOutput caseHistoryToCreateCaseHistoryOutput(CaseHistory entity);

    CaseHistory updateCaseHistoryInputToCaseHistory(UpdateCaseHistoryInput caseHistoryDto);

    @Mappings(
        {
            @Mapping(source = "entity.cases.caseId", target = "caseId"),
            @Mapping(source = "entity.cases.caseId", target = "casesDescriptiveField"),
        }
    )
    UpdateCaseHistoryOutput caseHistoryToUpdateCaseHistoryOutput(CaseHistory entity);

    @Mappings(
        {
            @Mapping(source = "entity.cases.caseId", target = "caseId"),
            @Mapping(source = "entity.cases.caseId", target = "casesDescriptiveField"),
        }
    )
    FindCaseHistoryByIdOutput caseHistoryToFindCaseHistoryByIdOutput(CaseHistory entity);

    @Mappings({ @Mapping(source = "foundCaseHistory.caseHistoryId", target = "caseHistoryCaseHistoryId") })
    GetCasesOutput casesToGetCasesOutput(Cases cases, CaseHistory foundCaseHistory);
}
