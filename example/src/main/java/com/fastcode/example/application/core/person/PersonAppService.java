package com.fastcode.example.application.core.person;

import com.fastcode.example.application.core.person.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.domain.core.authorization.users.IUsersRepository;
import com.fastcode.example.domain.core.authorization.users.Users;
import com.fastcode.example.domain.core.person.IPersonRepository;
import com.fastcode.example.domain.core.person.Person;
import com.fastcode.example.domain.core.person.QPerson;
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

@Service("personAppService")
@RequiredArgsConstructor
public class PersonAppService implements IPersonAppService {

    @Qualifier("personRepository")
    @NonNull
    protected final IPersonRepository _personRepository;

    @Qualifier("usersRepository")
    @NonNull
    protected final IUsersRepository _usersRepository;

    @Qualifier("IPersonMapperImpl")
    @NonNull
    protected final IPersonMapper mapper;

    @NonNull
    protected final LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
    public CreatePersonOutput create(CreatePersonInput input) {
        Person person = mapper.createPersonInputToPerson(input);
        Users foundUsers = null;
        if (input.getUsername() != null) {
            foundUsers = _usersRepository.findById(input.getUsername()).orElse(null);

            if (foundUsers != null) {
                foundUsers.addPersons(person);
                //person.setUsers(foundUsers);
            }
        }

        Person createdPerson = _personRepository.save(person);
        return mapper.personToCreatePersonOutput(createdPerson);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UpdatePersonOutput update(Long personId, UpdatePersonInput input) {
        Person existing = _personRepository.findById(personId).get();

        Person person = mapper.updatePersonInputToPerson(input);
        person.setPersonCasesSet(existing.getPersonCasesSet());
        Users foundUsers = null;

        if (input.getUsername() != null) {
            foundUsers = _usersRepository.findById(input.getUsername()).orElse(null);

            if (foundUsers != null) {
                foundUsers.addPersons(person);
                //	person.setUsers(foundUsers);
            }
        }

        Person updatedPerson = _personRepository.save(person);
        return mapper.personToUpdatePersonOutput(updatedPerson);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long personId) {
        Person existing = _personRepository.findById(personId).orElse(null);

        if (existing.getUsers() != null) {
            existing.getUsers().removePersons(existing);
        }
        _personRepository.delete(existing);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FindPersonByIdOutput findById(Long personId) {
        Person foundPerson = _personRepository.findById(personId).orElse(null);
        if (foundPerson == null) return null;

        return mapper.personToFindPersonByIdOutput(foundPerson);
    }

    //Users
    // ReST API Call - GET /person/1/users
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public GetUsersOutput getUsers(Long personId) {
        Person foundPerson = _personRepository.findById(personId).orElse(null);
        if (foundPerson == null) {
            logHelper.getLogger().error("There does not exist a person wth a id=%s", personId);
            return null;
        }
        Users re = foundPerson.getUsers();
        return mapper.usersToGetUsersOutput(re, foundPerson);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<FindPersonByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception {
        Page<Person> foundPerson = _personRepository.findAll(search(search), pageable);
        List<Person> personList = foundPerson.getContent();
        Iterator<Person> personIterator = personList.iterator();
        List<FindPersonByIdOutput> output = new ArrayList<>();

        while (personIterator.hasNext()) {
            Person person = personIterator.next();
            output.add(mapper.personToFindPersonByIdOutput(person));
        }
        return output;
    }

    protected BooleanBuilder search(SearchCriteria search) throws Exception {
        QPerson person = QPerson.personEntity;
        if (search != null) {
            Map<String, SearchFields> map = new HashMap<>();
            for (SearchFields fieldDetails : search.getFields()) {
                map.put(fieldDetails.getFieldName(), fieldDetails);
            }
            List<String> keysList = new ArrayList<String>(map.keySet());
            checkProperties(keysList);
            return searchKeyValuePair(person, map, search.getJoinColumns());
        }
        return null;
    }

    protected void checkProperties(List<String> list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            if (
                !(
                    list.get(i).replace("%20", "").trim().equals("users") ||
                    list.get(i).replace("%20", "").trim().equals("username") ||
                    list.get(i).replace("%20", "").trim().equals("birthDate") ||
                    list.get(i).replace("%20", "").trim().equals("comments") ||
                    list.get(i).replace("%20", "").trim().equals("givenName") ||
                    list.get(i).replace("%20", "").trim().equals("homePhone") ||
                    list.get(i).replace("%20", "").trim().equals("personId") ||
                    list.get(i).replace("%20", "").trim().equals("surname")
                )
            ) {
                throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!");
            }
        }
    }

    protected BooleanBuilder searchKeyValuePair(
        QPerson person,
        Map<String, SearchFields> map,
        Map<String, String> joinColumns
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if (details.getKey().replace("%20", "").trim().equals("birthDate")) {
                if (
                    details.getValue().getOperator().equals("equals") &&
                    SearchUtils.stringToLocalDate(details.getValue().getSearchValue()) != null
                ) {
                    builder.and(
                        person.birthDate.eq(SearchUtils.stringToLocalDate(details.getValue().getSearchValue()))
                    );
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    SearchUtils.stringToLocalDate(details.getValue().getSearchValue()) != null
                ) {
                    builder.and(
                        person.birthDate.ne(SearchUtils.stringToLocalDate(details.getValue().getSearchValue()))
                    );
                } else if (details.getValue().getOperator().equals("range")) {
                    LocalDate startLocalDate = SearchUtils.stringToLocalDate(details.getValue().getStartingValue());
                    LocalDate endLocalDate = SearchUtils.stringToLocalDate(details.getValue().getEndingValue());
                    if (startLocalDate != null && endLocalDate != null) {
                        builder.and(person.birthDate.between(startLocalDate, endLocalDate));
                    } else if (endLocalDate != null) {
                        builder.and(person.birthDate.loe(endLocalDate));
                    } else if (startLocalDate != null) {
                        builder.and(person.birthDate.goe(startLocalDate));
                    }
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("comments")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(person.comments.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
                } else if (details.getValue().getOperator().equals("equals")) {
                    builder.and(person.comments.eq(details.getValue().getSearchValue()));
                } else if (details.getValue().getOperator().equals("notEqual")) {
                    builder.and(person.comments.ne(details.getValue().getSearchValue()));
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("givenName")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(person.givenName.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
                } else if (details.getValue().getOperator().equals("equals")) {
                    builder.and(person.givenName.eq(details.getValue().getSearchValue()));
                } else if (details.getValue().getOperator().equals("notEqual")) {
                    builder.and(person.givenName.ne(details.getValue().getSearchValue()));
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("homePhone")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(person.homePhone.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
                } else if (details.getValue().getOperator().equals("equals")) {
                    builder.and(person.homePhone.eq(details.getValue().getSearchValue()));
                } else if (details.getValue().getOperator().equals("notEqual")) {
                    builder.and(person.homePhone.ne(details.getValue().getSearchValue()));
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("personId")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(person.personId.like(details.getValue().getSearchValue() + "%"));
                } else if (
                    details.getValue().getOperator().equals("equals") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(person.personId.eq(Long.valueOf(details.getValue().getSearchValue())));
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(person.personId.ne(Long.valueOf(details.getValue().getSearchValue())));
                } else if (details.getValue().getOperator().equals("range")) {
                    if (
                        StringUtils.isNumeric(details.getValue().getStartingValue()) &&
                        StringUtils.isNumeric(details.getValue().getEndingValue())
                    ) {
                        builder.and(
                            person.personId.between(
                                Long.valueOf(details.getValue().getStartingValue()),
                                Long.valueOf(details.getValue().getEndingValue())
                            )
                        );
                    } else if (StringUtils.isNumeric(details.getValue().getStartingValue())) {
                        builder.and(person.personId.goe(Long.valueOf(details.getValue().getStartingValue())));
                    } else if (StringUtils.isNumeric(details.getValue().getEndingValue())) {
                        builder.and(person.personId.loe(Long.valueOf(details.getValue().getEndingValue())));
                    }
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("surname")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(person.surname.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
                } else if (details.getValue().getOperator().equals("equals")) {
                    builder.and(person.surname.eq(details.getValue().getSearchValue()));
                } else if (details.getValue().getOperator().equals("notEqual")) {
                    builder.and(person.surname.ne(details.getValue().getSearchValue()));
                }
            }

            if (details.getKey().replace("%20", "").trim().equals("users")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(person.users.username.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
                } else if (details.getValue().getOperator().equals("equals")) {
                    builder.and(person.users.username.eq(details.getValue().getSearchValue()));
                } else if (details.getValue().getOperator().equals("notEqual")) {
                    builder.and(person.users.username.ne(details.getValue().getSearchValue()));
                }
            }
        }

        for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
            if (joinCol != null && joinCol.getKey().equals("username")) {
                builder.and(person.users.username.eq(joinCol.getValue()));
            }
        }
        return builder;
    }

    public Map<String, String> parsePersonCasesJoinColumn(String keysString) {
        Map<String, String> joinColumnMap = new HashMap<String, String>();
        joinColumnMap.put("personId", keysString);

        return joinColumnMap;
    }
}
