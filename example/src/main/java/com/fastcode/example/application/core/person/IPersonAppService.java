package com.fastcode.example.application.core.person;

import com.fastcode.example.application.core.person.dto.*;
import com.fastcode.example.commons.search.SearchCriteria;
import java.util.*;
import org.springframework.data.domain.Pageable;

public interface IPersonAppService {
    //CRUD Operations
    CreatePersonOutput create(CreatePersonInput person);

    void delete(Long id);

    UpdatePersonOutput update(Long id, UpdatePersonInput input);

    FindPersonByIdOutput findById(Long id);
    List<FindPersonByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;
    //Relationship Operations

    GetUsersOutput getUsers(Long personid);

    //Join Column Parsers

    Map<String, String> parsePersonCasesJoinColumn(String keysString);
}
