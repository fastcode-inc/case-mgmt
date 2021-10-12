package com.fastcode.example.application.core.cases;

import com.fastcode.example.application.core.cases.dto.*;
import com.fastcode.example.commons.search.SearchCriteria;
import java.util.*;
import org.springframework.data.domain.Pageable;

public interface ICasesAppService {
    //CRUD Operations
    CreateCasesOutput create(CreateCasesInput cases);

    void delete(Long id);

    UpdateCasesOutput update(Long id, UpdateCasesInput input);

    FindCasesByIdOutput findById(Long id);
    List<FindCasesByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

    //Join Column Parsers

    Map<String, String> parseCaseDocumentsJoinColumn(String keysString);

    Map<String, String> parseCaseHistorysJoinColumn(String keysString);

    Map<String, String> parsePersonCasesJoinColumn(String keysString);

    Map<String, String> parseTasksJoinColumn(String keysString);
}
