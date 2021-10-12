package com.fastcode.example.application.core.personcase;

import com.fastcode.example.application.core.personcase.dto.*;
import com.fastcode.example.commons.search.SearchCriteria;
import com.fastcode.example.domain.core.personcase.PersonCaseId;
import java.util.*;
import org.springframework.data.domain.Pageable;

public interface IPersonCaseAppService {
    //CRUD Operations
    CreatePersonCaseOutput create(CreatePersonCaseInput personcase);

    void delete(PersonCaseId personCaseId);

    UpdatePersonCaseOutput update(PersonCaseId personCaseId, UpdatePersonCaseInput input);

    FindPersonCaseByIdOutput findById(PersonCaseId personCaseId);
    List<FindPersonCaseByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;
    //Relationship Operations
    //Relationship Operations

    GetCasesOutput getCases(PersonCaseId personCaseId);

    GetPersonOutput getPerson(PersonCaseId personCaseId);

    //Join Column Parsers

    PersonCaseId parsePersonCaseKey(String keysString);
}
