package com.fastcode.example.application.core.cases;

import com.fastcode.example.application.core.cases.dto.*;
import com.fastcode.example.domain.core.cases.Cases;
import java.time.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICasesMapper {
    Cases createCasesInputToCases(CreateCasesInput casesDto);
    CreateCasesOutput casesToCreateCasesOutput(Cases entity);

    Cases updateCasesInputToCases(UpdateCasesInput casesDto);

    UpdateCasesOutput casesToUpdateCasesOutput(Cases entity);
    FindCasesByIdOutput casesToFindCasesByIdOutput(Cases entity);
}
