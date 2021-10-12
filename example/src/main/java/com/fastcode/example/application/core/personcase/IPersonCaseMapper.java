package com.fastcode.example.application.core.personcase;

import com.fastcode.example.application.core.personcase.dto.*;
import com.fastcode.example.domain.core.cases.Cases;
import com.fastcode.example.domain.core.person.Person;
import com.fastcode.example.domain.core.personcase.PersonCase;
import java.time.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface IPersonCaseMapper {
    PersonCase createPersonCaseInputToPersonCase(CreatePersonCaseInput personCaseDto);

    @Mappings(
        {
            @Mapping(source = "entity.cases.caseId", target = "casesDescriptiveField"),
            @Mapping(source = "entity.person.personId", target = "personDescriptiveField"),
        }
    )
    CreatePersonCaseOutput personCaseToCreatePersonCaseOutput(PersonCase entity);

    PersonCase updatePersonCaseInputToPersonCase(UpdatePersonCaseInput personCaseDto);

    @Mappings(
        {
            @Mapping(source = "entity.cases.caseId", target = "casesDescriptiveField"),
            @Mapping(source = "entity.person.personId", target = "personDescriptiveField"),
        }
    )
    UpdatePersonCaseOutput personCaseToUpdatePersonCaseOutput(PersonCase entity);

    @Mappings(
        {
            @Mapping(source = "entity.cases.caseId", target = "casesDescriptiveField"),
            @Mapping(source = "entity.person.personId", target = "personDescriptiveField"),
        }
    )
    FindPersonCaseByIdOutput personCaseToFindPersonCaseByIdOutput(PersonCase entity);

    @Mappings(
        {
            @Mapping(source = "cases.caseId", target = "caseId"),
            @Mapping(source = "foundPersonCase.caseId", target = "personCaseCaseId"),
            @Mapping(source = "foundPersonCase.personId", target = "personCasePersonId"),
        }
    )
    GetCasesOutput casesToGetCasesOutput(Cases cases, PersonCase foundPersonCase);

    @Mappings(
        {
            @Mapping(source = "person.personId", target = "personId"),
            @Mapping(source = "foundPersonCase.caseId", target = "personCaseCaseId"),
            @Mapping(source = "foundPersonCase.personId", target = "personCasePersonId"),
        }
    )
    GetPersonOutput personToGetPersonOutput(Person person, PersonCase foundPersonCase);
}
