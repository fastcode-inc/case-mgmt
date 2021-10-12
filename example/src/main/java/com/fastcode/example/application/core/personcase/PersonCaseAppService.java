package com.fastcode.example.application.core.personcase;

import com.fastcode.example.application.core.personcase.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.domain.core.cases.Cases;
import com.fastcode.example.domain.core.cases.ICasesRepository;
import com.fastcode.example.domain.core.person.IPersonRepository;
import com.fastcode.example.domain.core.person.Person;
import com.fastcode.example.domain.core.personcase.IPersonCaseRepository;
import com.fastcode.example.domain.core.personcase.PersonCase;
import com.fastcode.example.domain.core.personcase.PersonCaseId;
import com.fastcode.example.domain.core.personcase.QPersonCase;
import com.querydsl.core.BooleanBuilder;
import java.time.*;
import java.util.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("personCaseAppService")
@RequiredArgsConstructor
public class PersonCaseAppService implements IPersonCaseAppService {

    @Qualifier("personCaseRepository")
    @NonNull
    protected final IPersonCaseRepository _personCaseRepository;

    @Qualifier("casesRepository")
    @NonNull
    protected final ICasesRepository _casesRepository;

    @Qualifier("personRepository")
    @NonNull
    protected final IPersonRepository _personRepository;

    @Qualifier("IPersonCaseMapperImpl")
    @NonNull
    protected final IPersonCaseMapper mapper;

    @NonNull
    protected final LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
    public CreatePersonCaseOutput create(CreatePersonCaseInput input) {
        PersonCase personCase = mapper.createPersonCaseInputToPersonCase(input);
        Cases foundCases = null;
        Person foundPerson = null;
        if (input.getCaseId() != null) {
            foundCases = _casesRepository.findById(input.getCaseId()).orElse(null);

            if (foundCases != null) {
                foundCases.addPersonCases(personCase);
                //personCase.setCases(foundCases);
            }
        }
        if (input.getPersonId() != null) {
            foundPerson = _personRepository.findById(input.getPersonId()).orElse(null);

            if (foundPerson != null) {
                foundPerson.addPersonCases(personCase);
                //personCase.setPerson(foundPerson);
            }
        }

        PersonCase createdPersonCase = _personCaseRepository.save(personCase);
        return mapper.personCaseToCreatePersonCaseOutput(createdPersonCase);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UpdatePersonCaseOutput update(PersonCaseId personCaseId, UpdatePersonCaseInput input) {
        PersonCase existing = _personCaseRepository.findById(personCaseId).get();

        PersonCase personCase = mapper.updatePersonCaseInputToPersonCase(input);
        Cases foundCases = null;
        Person foundPerson = null;

        if (input.getCaseId() != null) {
            foundCases = _casesRepository.findById(input.getCaseId()).orElse(null);

            if (foundCases != null) {
                foundCases.addPersonCases(personCase);
                //	personCase.setCases(foundCases);
            }
        }

        if (input.getPersonId() != null) {
            foundPerson = _personRepository.findById(input.getPersonId()).orElse(null);

            if (foundPerson != null) {
                foundPerson.addPersonCases(personCase);
                //	personCase.setPerson(foundPerson);
            }
        }

        PersonCase updatedPersonCase = _personCaseRepository.save(personCase);
        return mapper.personCaseToUpdatePersonCaseOutput(updatedPersonCase);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(PersonCaseId personCaseId) {
        PersonCase existing = _personCaseRepository.findById(personCaseId).orElse(null);

        if (existing.getCases() != null) {
            existing.getCases().removePersonCases(existing);
        }
        if (existing.getPerson() != null) {
            existing.getPerson().removePersonCases(existing);
        }
        _personCaseRepository.delete(existing);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FindPersonCaseByIdOutput findById(PersonCaseId personCaseId) {
        PersonCase foundPersonCase = _personCaseRepository.findById(personCaseId).orElse(null);
        if (foundPersonCase == null) return null;

        return mapper.personCaseToFindPersonCaseByIdOutput(foundPersonCase);
    }

    //Cases
    // ReST API Call - GET /personCase/1/cases
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public GetCasesOutput getCases(PersonCaseId personCaseId) {
        PersonCase foundPersonCase = _personCaseRepository.findById(personCaseId).orElse(null);
        if (foundPersonCase == null) {
            logHelper.getLogger().error("There does not exist a personCase wth a id=%s", personCaseId);
            return null;
        }
        Cases re = foundPersonCase.getCases();
        return mapper.casesToGetCasesOutput(re, foundPersonCase);
    }

    //Person
    // ReST API Call - GET /personCase/1/person
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public GetPersonOutput getPerson(PersonCaseId personCaseId) {
        PersonCase foundPersonCase = _personCaseRepository.findById(personCaseId).orElse(null);
        if (foundPersonCase == null) {
            logHelper.getLogger().error("There does not exist a personCase wth a id=%s", personCaseId);
            return null;
        }
        Person re = foundPersonCase.getPerson();
        return mapper.personToGetPersonOutput(re, foundPersonCase);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<FindPersonCaseByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception {
        Page<PersonCase> foundPersonCase = _personCaseRepository.findAll(search(search), pageable);
        List<PersonCase> personCaseList = foundPersonCase.getContent();
        Iterator<PersonCase> personCaseIterator = personCaseList.iterator();
        List<FindPersonCaseByIdOutput> output = new ArrayList<>();

        while (personCaseIterator.hasNext()) {
            PersonCase personCase = personCaseIterator.next();
            output.add(mapper.personCaseToFindPersonCaseByIdOutput(personCase));
        }
        return output;
    }

    protected BooleanBuilder search(SearchCriteria search) throws Exception {
        QPersonCase personCase = QPersonCase.personCaseEntity;
        if (search != null) {
            Map<String, SearchFields> map = new HashMap<>();
            for (SearchFields fieldDetails : search.getFields()) {
                map.put(fieldDetails.getFieldName(), fieldDetails);
            }
            List<String> keysList = new ArrayList<String>(map.keySet());
            checkProperties(keysList);
            return searchKeyValuePair(personCase, map, search.getJoinColumns());
        }
        return null;
    }

    protected void checkProperties(List<String> list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            if (
                !(
                    list.get(i).replace("%20", "").trim().equals("cases") ||
                    list.get(i).replace("%20", "").trim().equals("person") ||
                    list.get(i).replace("%20", "").trim().equals("caseId") ||
                    list.get(i).replace("%20", "").trim().equals("personId")
                )
            ) {
                throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!");
            }
        }
    }

    protected BooleanBuilder searchKeyValuePair(
        QPersonCase personCase,
        Map<String, SearchFields> map,
        Map<String, String> joinColumns
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if (details.getKey().replace("%20", "").trim().equals("caseId")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(personCase.caseId.like(details.getValue().getSearchValue() + "%"));
                } else if (
                    details.getValue().getOperator().equals("equals") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(personCase.caseId.eq(Long.valueOf(details.getValue().getSearchValue())));
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(personCase.caseId.ne(Long.valueOf(details.getValue().getSearchValue())));
                } else if (details.getValue().getOperator().equals("range")) {
                    if (
                        StringUtils.isNumeric(details.getValue().getStartingValue()) &&
                        StringUtils.isNumeric(details.getValue().getEndingValue())
                    ) {
                        builder.and(
                            personCase.caseId.between(
                                Long.valueOf(details.getValue().getStartingValue()),
                                Long.valueOf(details.getValue().getEndingValue())
                            )
                        );
                    } else if (StringUtils.isNumeric(details.getValue().getStartingValue())) {
                        builder.and(personCase.caseId.goe(Long.valueOf(details.getValue().getStartingValue())));
                    } else if (StringUtils.isNumeric(details.getValue().getEndingValue())) {
                        builder.and(personCase.caseId.loe(Long.valueOf(details.getValue().getEndingValue())));
                    }
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("personId")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(personCase.personId.like(details.getValue().getSearchValue() + "%"));
                } else if (
                    details.getValue().getOperator().equals("equals") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(personCase.personId.eq(Long.valueOf(details.getValue().getSearchValue())));
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(personCase.personId.ne(Long.valueOf(details.getValue().getSearchValue())));
                } else if (details.getValue().getOperator().equals("range")) {
                    if (
                        StringUtils.isNumeric(details.getValue().getStartingValue()) &&
                        StringUtils.isNumeric(details.getValue().getEndingValue())
                    ) {
                        builder.and(
                            personCase.personId.between(
                                Long.valueOf(details.getValue().getStartingValue()),
                                Long.valueOf(details.getValue().getEndingValue())
                            )
                        );
                    } else if (StringUtils.isNumeric(details.getValue().getStartingValue())) {
                        builder.and(personCase.personId.goe(Long.valueOf(details.getValue().getStartingValue())));
                    } else if (StringUtils.isNumeric(details.getValue().getEndingValue())) {
                        builder.and(personCase.personId.loe(Long.valueOf(details.getValue().getEndingValue())));
                    }
                }
            }

            if (details.getKey().replace("%20", "").trim().equals("cases")) {
                if (
                    details.getValue().getOperator().equals("contains") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(personCase.cases.caseId.like(details.getValue().getSearchValue() + "%"));
                } else if (
                    details.getValue().getOperator().equals("equals") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(personCase.cases.caseId.eq(Long.valueOf(details.getValue().getSearchValue())));
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(personCase.cases.caseId.ne(Long.valueOf(details.getValue().getSearchValue())));
                } else if (details.getValue().getOperator().equals("range")) {
                    if (
                        StringUtils.isNumeric(details.getValue().getStartingValue()) &&
                        StringUtils.isNumeric(details.getValue().getEndingValue())
                    ) {
                        builder.and(
                            personCase.cases.caseId.between(
                                Long.valueOf(details.getValue().getStartingValue()),
                                Long.valueOf(details.getValue().getEndingValue())
                            )
                        );
                    } else if (StringUtils.isNumeric(details.getValue().getStartingValue())) {
                        builder.and(personCase.cases.caseId.goe(Long.valueOf(details.getValue().getStartingValue())));
                    } else if (StringUtils.isNumeric(details.getValue().getEndingValue())) {
                        builder.and(personCase.cases.caseId.loe(Long.valueOf(details.getValue().getEndingValue())));
                    }
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("person")) {
                if (
                    details.getValue().getOperator().equals("contains") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(personCase.person.personId.like(details.getValue().getSearchValue() + "%"));
                } else if (
                    details.getValue().getOperator().equals("equals") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(personCase.person.personId.eq(Long.valueOf(details.getValue().getSearchValue())));
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(personCase.person.personId.ne(Long.valueOf(details.getValue().getSearchValue())));
                } else if (details.getValue().getOperator().equals("range")) {
                    if (
                        StringUtils.isNumeric(details.getValue().getStartingValue()) &&
                        StringUtils.isNumeric(details.getValue().getEndingValue())
                    ) {
                        builder.and(
                            personCase.person.personId.between(
                                Long.valueOf(details.getValue().getStartingValue()),
                                Long.valueOf(details.getValue().getEndingValue())
                            )
                        );
                    } else if (StringUtils.isNumeric(details.getValue().getStartingValue())) {
                        builder.and(
                            personCase.person.personId.goe(Long.valueOf(details.getValue().getStartingValue()))
                        );
                    } else if (StringUtils.isNumeric(details.getValue().getEndingValue())) {
                        builder.and(personCase.person.personId.loe(Long.valueOf(details.getValue().getEndingValue())));
                    }
                }
            }
        }

        for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
            if (joinCol != null && joinCol.getKey().equals("caseId")) {
                builder.and(personCase.cases.caseId.eq(Long.parseLong(joinCol.getValue())));
            }
        }
        for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
            if (joinCol != null && joinCol.getKey().equals("personId")) {
                builder.and(personCase.person.personId.eq(Long.parseLong(joinCol.getValue())));
            }
        }
        return builder;
    }

    public PersonCaseId parsePersonCaseKey(String keysString) {
        String[] keyEntries = keysString.split(",");
        PersonCaseId personCaseId = new PersonCaseId();

        Map<String, String> keyMap = new HashMap<String, String>();
        if (keyEntries.length > 1) {
            for (String keyEntry : keyEntries) {
                String[] keyEntryArr = keyEntry.split("=");
                if (keyEntryArr.length > 1) {
                    keyMap.put(keyEntryArr[0], keyEntryArr[1]);
                } else {
                    return null;
                }
            }
        } else {
            String[] keyEntryArr = keysString.split("=");
            if (keyEntryArr.length > 1) {
                keyMap.put(keyEntryArr[0], keyEntryArr[1]);
            } else return null;
        }

        personCaseId.setCaseId(Long.valueOf(keyMap.get("caseId")));
        personCaseId.setPersonId(Long.valueOf(keyMap.get("personId")));
        return personCaseId;
    }
}
