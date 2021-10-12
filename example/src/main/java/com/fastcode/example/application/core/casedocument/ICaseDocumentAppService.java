package com.fastcode.example.application.core.casedocument;

import com.fastcode.example.application.core.casedocument.dto.*;
import com.fastcode.example.commons.search.SearchCriteria;
import com.fastcode.example.domain.core.casedocument.CaseDocumentId;
import java.util.*;
import org.springframework.data.domain.Pageable;

public interface ICaseDocumentAppService {
    //CRUD Operations
    CreateCaseDocumentOutput create(CreateCaseDocumentInput casedocument);

    void delete(CaseDocumentId caseDocumentId);

    UpdateCaseDocumentOutput update(CaseDocumentId caseDocumentId, UpdateCaseDocumentInput input);

    FindCaseDocumentByIdOutput findById(CaseDocumentId caseDocumentId);
    List<FindCaseDocumentByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;
    //Relationship Operations

    GetCasesOutput getCases(CaseDocumentId caseDocumentId);

    //Join Column Parsers

    CaseDocumentId parseCaseDocumentKey(String keysString);
}
