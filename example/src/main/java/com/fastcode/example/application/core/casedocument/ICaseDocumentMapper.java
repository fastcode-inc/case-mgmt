package com.fastcode.example.application.core.casedocument;

import com.fastcode.example.application.core.casedocument.dto.*;
import com.fastcode.example.domain.core.casedocument.CaseDocument;
import com.fastcode.example.domain.core.cases.Cases;
import java.time.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ICaseDocumentMapper {
    CaseDocument createCaseDocumentInputToCaseDocument(CreateCaseDocumentInput caseDocumentDto);

    @Mappings({ @Mapping(source = "entity.cases.caseId", target = "casesDescriptiveField") })
    CreateCaseDocumentOutput caseDocumentToCreateCaseDocumentOutput(CaseDocument entity);

    CaseDocument updateCaseDocumentInputToCaseDocument(UpdateCaseDocumentInput caseDocumentDto);

    @Mappings({ @Mapping(source = "entity.cases.caseId", target = "casesDescriptiveField") })
    UpdateCaseDocumentOutput caseDocumentToUpdateCaseDocumentOutput(CaseDocument entity);

    @Mappings({ @Mapping(source = "entity.cases.caseId", target = "casesDescriptiveField") })
    FindCaseDocumentByIdOutput caseDocumentToFindCaseDocumentByIdOutput(CaseDocument entity);

    @Mappings(
        {
            @Mapping(source = "cases.caseId", target = "caseId"),
            @Mapping(source = "foundCaseDocument.caseId", target = "caseDocumentCaseId"),
            @Mapping(source = "foundCaseDocument.fileId", target = "caseDocumentFileId"),
        }
    )
    GetCasesOutput casesToGetCasesOutput(Cases cases, CaseDocument foundCaseDocument);
}
