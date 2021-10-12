package com.fastcode.example.application.core.person;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fastcode.example.application.core.person.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.domain.core.authorization.users.IUsersRepository;
import com.fastcode.example.domain.core.authorization.users.Users;
import com.fastcode.example.domain.core.person.*;
import com.fastcode.example.domain.core.person.Person;
import com.fastcode.example.domain.core.person.QPerson;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class PersonAppServiceTest {

    @InjectMocks
    @Spy
    protected PersonAppService _appService;

    @Mock
    protected IPersonRepository _personRepository;

    @Mock
    protected IUsersRepository _usersRepository;

    @Mock
    protected IPersonMapper _mapper;

    @Mock
    protected Logger loggerMock;

    @Mock
    protected LoggingHelper logHelper;

    protected static Long ID = 15L;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(_appService);
        when(logHelper.getLogger()).thenReturn(loggerMock);
        doNothing().when(loggerMock).error(anyString());
    }

    @Test
    public void findPersonById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
        Optional<Person> nullOptional = Optional.ofNullable(null);
        Mockito.when(_personRepository.findById(anyLong())).thenReturn(nullOptional);
        Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
    }

    @Test
    public void findPersonById_IdIsNotNullAndIdExists_ReturnPerson() {
        Person person = mock(Person.class);
        Optional<Person> personOptional = Optional.of((Person) person);
        Mockito.when(_personRepository.findById(anyLong())).thenReturn(personOptional);

        Assertions.assertThat(_appService.findById(ID)).isEqualTo(_mapper.personToFindPersonByIdOutput(person));
    }

    @Test
    public void createPerson_PersonIsNotNullAndPersonDoesNotExist_StorePerson() {
        Person personEntity = mock(Person.class);
        CreatePersonInput personInput = new CreatePersonInput();

        Users users = mock(Users.class);
        Optional<Users> usersOptional = Optional.of((Users) users);
        personInput.setUsername("15");

        Mockito.when(_usersRepository.findById(any(String.class))).thenReturn(usersOptional);

        Mockito.when(_mapper.createPersonInputToPerson(any(CreatePersonInput.class))).thenReturn(personEntity);
        Mockito.when(_personRepository.save(any(Person.class))).thenReturn(personEntity);

        Assertions
            .assertThat(_appService.create(personInput))
            .isEqualTo(_mapper.personToCreatePersonOutput(personEntity));
    }

    @Test
    public void createPerson_PersonIsNotNullAndPersonDoesNotExistAndChildIsNullAndChildIsNotMandatory_StorePerson() {
        Person person = mock(Person.class);
        CreatePersonInput personInput = mock(CreatePersonInput.class);

        Mockito.when(_mapper.createPersonInputToPerson(any(CreatePersonInput.class))).thenReturn(person);
        Mockito.when(_personRepository.save(any(Person.class))).thenReturn(person);
        Assertions.assertThat(_appService.create(personInput)).isEqualTo(_mapper.personToCreatePersonOutput(person));
    }

    @Test
    public void updatePerson_PersonIsNotNullAndPersonDoesNotExistAndChildIsNullAndChildIsNotMandatory_ReturnUpdatedPerson() {
        Person person = mock(Person.class);
        UpdatePersonInput personInput = mock(UpdatePersonInput.class);

        Optional<Person> personOptional = Optional.of((Person) person);
        Mockito.when(_personRepository.findById(anyLong())).thenReturn(personOptional);

        Mockito.when(_mapper.updatePersonInputToPerson(any(UpdatePersonInput.class))).thenReturn(person);
        Mockito.when(_personRepository.save(any(Person.class))).thenReturn(person);
        Assertions
            .assertThat(_appService.update(ID, personInput))
            .isEqualTo(_mapper.personToUpdatePersonOutput(person));
    }

    @Test
    public void updatePerson_PersonIdIsNotNullAndIdExists_ReturnUpdatedPerson() {
        Person personEntity = mock(Person.class);
        UpdatePersonInput person = mock(UpdatePersonInput.class);

        Optional<Person> personOptional = Optional.of((Person) personEntity);
        Mockito.when(_personRepository.findById(anyLong())).thenReturn(personOptional);

        Mockito.when(_mapper.updatePersonInputToPerson(any(UpdatePersonInput.class))).thenReturn(personEntity);
        Mockito.when(_personRepository.save(any(Person.class))).thenReturn(personEntity);
        Assertions
            .assertThat(_appService.update(ID, person))
            .isEqualTo(_mapper.personToUpdatePersonOutput(personEntity));
    }

    @Test
    public void deletePerson_PersonIsNotNullAndPersonExists_PersonRemoved() {
        Person person = mock(Person.class);
        Optional<Person> personOptional = Optional.of((Person) person);
        Mockito.when(_personRepository.findById(anyLong())).thenReturn(personOptional);

        _appService.delete(ID);
        verify(_personRepository).delete(person);
    }

    @Test
    public void find_ListIsEmpty_ReturnList() throws Exception {
        List<Person> list = new ArrayList<>();
        Page<Person> foundPage = new PageImpl(list);
        Pageable pageable = mock(Pageable.class);
        List<FindPersonByIdOutput> output = new ArrayList<>();
        SearchCriteria search = new SearchCriteria();

        Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
        Mockito.when(_personRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(foundPage);
        Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
    }

    @Test
    public void find_ListIsNotEmpty_ReturnList() throws Exception {
        List<Person> list = new ArrayList<>();
        Person person = mock(Person.class);
        list.add(person);
        Page<Person> foundPage = new PageImpl(list);
        Pageable pageable = mock(Pageable.class);
        List<FindPersonByIdOutput> output = new ArrayList<>();
        SearchCriteria search = new SearchCriteria();

        output.add(_mapper.personToFindPersonByIdOutput(person));

        Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
        Mockito.when(_personRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(foundPage);
        Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
    }

    @Test
    public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
        QPerson person = QPerson.personEntity;
        SearchFields searchFields = new SearchFields();
        searchFields.setOperator("equals");
        searchFields.setSearchValue("xyz");
        Map<String, SearchFields> map = new HashMap<>();
        map.put("comments", searchFields);
        Map<String, String> searchMap = new HashMap<>();
        searchMap.put("xyz", String.valueOf(ID));
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(person.comments.eq("xyz"));
        Assertions.assertThat(_appService.searchKeyValuePair(person, map, searchMap)).isEqualTo(builder);
    }

    @Test(expected = Exception.class)
    public void checkProperties_PropertyDoesNotExist_ThrowException() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("xyz");
        _appService.checkProperties(list);
    }

    @Test
    public void checkProperties_PropertyExists_ReturnNothing() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("comments");
        list.add("givenName");
        list.add("homePhone");
        list.add("surname");
        _appService.checkProperties(list);
    }

    @Test
    public void search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
        Map<String, SearchFields> map = new HashMap<>();
        QPerson person = QPerson.personEntity;
        List<SearchFields> fieldsList = new ArrayList<>();
        SearchFields fields = new SearchFields();
        SearchCriteria search = new SearchCriteria();
        search.setType(3);
        search.setValue("xyz");
        search.setOperator("equals");
        fields.setFieldName("comments");
        fields.setOperator("equals");
        fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
        BooleanBuilder builder = new BooleanBuilder();
        builder.or(person.comments.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
        Mockito
            .doReturn(builder)
            .when(_appService)
            .searchKeyValuePair(any(QPerson.class), any(HashMap.class), any(HashMap.class));

        Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
    }

    @Test
    public void search_StringIsNull_ReturnNull() throws Exception {
        Assertions.assertThat(_appService.search(null)).isEqualTo(null);
    }

    //Users
    @Test
    public void GetUsers_IfPersonIdAndUsersIdIsNotNullAndPersonExists_ReturnUsers() {
        Person person = mock(Person.class);
        Optional<Person> personOptional = Optional.of((Person) person);
        Users usersEntity = mock(Users.class);

        Mockito.when(_personRepository.findById(any(Long.class))).thenReturn(personOptional);

        Mockito.when(person.getUsers()).thenReturn(usersEntity);
        Assertions.assertThat(_appService.getUsers(ID)).isEqualTo(_mapper.usersToGetUsersOutput(usersEntity, person));
    }

    @Test
    public void GetUsers_IfPersonIdAndUsersIdIsNotNullAndPersonDoesNotExist_ReturnNull() {
        Optional<Person> nullOptional = Optional.ofNullable(null);
        Mockito.when(_personRepository.findById(anyLong())).thenReturn(nullOptional);
        Assertions.assertThat(_appService.getUsers(ID)).isEqualTo(null);
    }

    @Test
    public void ParsepersonCasesJoinColumn_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull() {
        Map<String, String> joinColumnMap = new HashMap<String, String>();
        String keyString = "15";
        joinColumnMap.put("personId", keyString);
        Assertions.assertThat(_appService.parsePersonCasesJoinColumn(keyString)).isEqualTo(joinColumnMap);
    }
}
