package com.fastcode.example.application.core.casehistory;

import com.fastcode.example.application.core.casehistory.dto.*;
import com.fastcode.example.commons.search.SearchCriteria;
import java.util.*;
import org.springframework.data.domain.Pageable;

public interface ICaseHistoryAppService {
    //CRUD Operations
    CreateCaseHistoryOutput create(CreateCaseHistoryInput casehistory);

    void delete(Long id);

    UpdateCaseHistoryOutput update(Long id, UpdateCaseHistoryInput input);

    FindCaseHistoryByIdOutput findById(Long id);
    List<FindCaseHistoryByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;
    //Relationship Operations

    GetCasesOutput getCases(Long caseHistoryid);
    //Join Column Parsers
}
