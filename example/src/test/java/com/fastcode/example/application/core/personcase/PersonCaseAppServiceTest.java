package com.fastcode.example.application.core.personcase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fastcode.example.application.core.personcase.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.domain.core.cases.Cases;
import com.fastcode.example.domain.core.cases.ICasesRepository;
import com.fastcode.example.domain.core.person.IPersonRepository;
import com.fastcode.example.domain.core.person.Person;
import com.fastcode.example.domain.core.personcase.*;
import com.fastcode.example.domain.core.personcase.PersonCase;
import com.fastcode.example.domain.core.personcase.PersonCaseId;
import com.fastcode.example.domain.core.personcase.QPersonCase;
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
public class PersonCaseAppServiceTest {

    @InjectMocks
    @Spy
    protected PersonCaseAppService _appService;

    @Mock
    protected IPersonCaseRepository _personCaseRepository;

    @Mock
    protected ICasesRepository _casesRepository;

    @Mock
    protected IPersonRepository _personRepository;

    @Mock
    protected IPersonCaseMapper _mapper;

    @Mock
    protected Logger loggerMock;

    @Mock
    protected LoggingHelper logHelper;

    @Mock
    protected PersonCaseId personCaseId;

    private static final Long ID = 15L;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(_appService);
        when(logHelper.getLogger()).thenReturn(loggerMock);
        doNothing().when(loggerMock).error(anyString());
    }

    @Test
    public void findPersonCaseById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
        Optional<PersonCase> nullOptional = Optional.ofNullable(null);
        Mockito.when(_personCaseRepository.findById(any(PersonCaseId.class))).thenReturn(nullOptional);
        Assertions.assertThat(_appService.findById(personCaseId)).isEqualTo(null);
    }

    @Test
    public void findPersonCaseById_IdIsNotNullAndIdExists_ReturnPersonCase() {
        PersonCase personCase = mock(PersonCase.class);
        Optional<PersonCase> personCaseOptional = Optional.of((PersonCase) personCase);
        Mockito.when(_personCaseRepository.findById(any(PersonCaseId.class))).thenReturn(personCaseOptional);

        Assertions
            .assertThat(_appService.findById(personCaseId))
            .isEqualTo(_mapper.personCaseToFindPersonCaseByIdOutput(personCase));
    }

    @Test
    public void createPersonCase_PersonCaseIsNotNullAndPersonCaseDoesNotExist_StorePersonCase() {
        PersonCase personCaseEntity = mock(PersonCase.class);
        CreatePersonCaseInput personCaseInput = new CreatePersonCaseInput();

        Cases cases = mock(Cases.class);
        Optional<Cases> casesOptional = Optional.of((Cases) cases);
        personCaseInput.setCaseId(15L);

        Mockito.when(_casesRepository.findById(any(Long.class))).thenReturn(casesOptional);

        Person person = mock(Person.class);
        Optional<Person> personOptional = Optional.of((Person) person);
        personCaseInput.setPersonId(15L);

        Mockito.when(_personRepository.findById(any(Long.class))).thenReturn(personOptional);

        Mockito
            .when(_mapper.createPersonCaseInputToPersonCase(any(CreatePersonCaseInput.class)))
            .thenReturn(personCaseEntity);
        Mockito.when(_personCaseRepository.save(any(PersonCase.class))).thenReturn(personCaseEntity);

        Assertions
            .assertThat(_appService.create(personCaseInput))
            .isEqualTo(_mapper.personCaseToCreatePersonCaseOutput(personCaseEntity));
    }

    @Test
    public void createPersonCase_PersonCaseIsNotNullAndPersonCaseDoesNotExistAndChildIsNullAndChildIsNotMandatory_StorePersonCase() {
        PersonCase personCase = mock(PersonCase.class);
        CreatePersonCaseInput personCaseInput = mock(CreatePersonCaseInput.class);

        Mockito
            .when(_mapper.createPersonCaseInputToPersonCase(any(CreatePersonCaseInput.class)))
            .thenReturn(personCase);
        Mockito.when(_personCaseRepository.save(any(PersonCase.class))).thenReturn(personCase);
        Assertions
            .assertThat(_appService.create(personCaseInput))
            .isEqualTo(_mapper.personCaseToCreatePersonCaseOutput(personCase));
    }

    @Test
    public void updatePersonCase_PersonCaseIsNotNullAndPersonCaseDoesNotExistAndChildIsNullAndChildIsNotMandatory_ReturnUpdatedPersonCase() {
        PersonCase personCase = mock(PersonCase.class);
        UpdatePersonCaseInput personCaseInput = mock(UpdatePersonCaseInput.class);

        Optional<PersonCase> personCaseOptional = Optional.of((PersonCase) personCase);
        Mockito.when(_personCaseRepository.findById(any(PersonCaseId.class))).thenReturn(personCaseOptional);

        Mockito
            .when(_mapper.updatePersonCaseInputToPersonCase(any(UpdatePersonCaseInput.class)))
            .thenReturn(personCase);
        Mockito.when(_personCaseRepository.save(any(PersonCase.class))).thenReturn(personCase);
        Assertions
            .assertThat(_appService.update(personCaseId, personCaseInput))
            .isEqualTo(_mapper.personCaseToUpdatePersonCaseOutput(personCase));
    }

    @Test
    public void updatePersonCase_PersonCaseIdIsNotNullAndIdExists_ReturnUpdatedPersonCase() {
        PersonCase personCaseEntity = mock(PersonCase.class);
        UpdatePersonCaseInput personCase = mock(UpdatePersonCaseInput.class);

        Optional<PersonCase> personCaseOptional = Optional.of((PersonCase) personCaseEntity);
        Mockito.when(_personCaseRepository.findById(any(PersonCaseId.class))).thenReturn(personCaseOptional);

        Mockito
            .when(_mapper.updatePersonCaseInputToPersonCase(any(UpdatePersonCaseInput.class)))
            .thenReturn(personCaseEntity);
        Mockito.when(_personCaseRepository.save(any(PersonCase.class))).thenReturn(personCaseEntity);
        Assertions
            .assertThat(_appService.update(personCaseId, personCase))
            .isEqualTo(_mapper.personCaseToUpdatePersonCaseOutput(personCaseEntity));
    }

    @Test
    public void deletePersonCase_PersonCaseIsNotNullAndPersonCaseExists_PersonCaseRemoved() {
        PersonCase personCase = mock(PersonCase.class);
        Optional<PersonCase> personCaseOptional = Optional.of((PersonCase) personCase);
        Mockito.when(_personCaseRepository.findById(any(PersonCaseId.class))).thenReturn(personCaseOptional);

        _appService.delete(personCaseId);
        verify(_personCaseRepository).delete(personCase);
    }

    @Test
    public void find_ListIsEmpty_ReturnList() throws Exception {
        List<PersonCase> list = new ArrayList<>();
        Page<PersonCase> foundPage = new PageImpl(list);
        Pageable pageable = mock(Pageable.class);
        List<FindPersonCaseByIdOutput> output = new ArrayList<>();
        SearchCriteria search = new SearchCriteria();

        Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
        Mockito.when(_personCaseRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(foundPage);
        Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
    }

    @Test
    public void find_ListIsNotEmpty_ReturnList() throws Exception {
        List<PersonCase> list = new ArrayList<>();
        PersonCase personCase = mock(PersonCase.class);
        list.add(personCase);
        Page<PersonCase> foundPage = new PageImpl(list);
        Pageable pageable = mock(Pageable.class);
        List<FindPersonCaseByIdOutput> output = new ArrayList<>();
        SearchCriteria search = new SearchCriteria();

        output.add(_mapper.personCaseToFindPersonCaseByIdOutput(personCase));

        Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
        Mockito.when(_personCaseRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(foundPage);
        Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
    }

    @Test
    public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
        QPersonCase personCase = QPersonCase.personCaseEntity;
        SearchFields searchFields = new SearchFields();
        searchFields.setOperator("equals");
        searchFields.setSearchValue("xyz");
        Map<String, SearchFields> map = new HashMap<>();
        Map<String, String> searchMap = new HashMap<>();
        searchMap.put("xyz", String.valueOf(ID));
        BooleanBuilder builder = new BooleanBuilder();
        Assertions.assertThat(_appService.searchKeyValuePair(personCase, map, searchMap)).isEqualTo(builder);
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
        _appService.checkProperties(list);
    }

    @Test
    public void search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
        Map<String, SearchFields> map = new HashMap<>();
        QPersonCase personCase = QPersonCase.personCaseEntity;
        List<SearchFields> fieldsList = new ArrayList<>();
        SearchFields fields = new SearchFields();
        SearchCriteria search = new SearchCriteria();
        search.setType(3);
        search.setValue("xyz");
        search.setOperator("equals");
        fields.setOperator("equals");
        fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
        BooleanBuilder builder = new BooleanBuilder();
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
        Mockito
            .doReturn(builder)
            .when(_appService)
            .searchKeyValuePair(any(QPersonCase.class), any(HashMap.class), any(HashMap.class));

        Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
    }

    @Test
    public void search_StringIsNull_ReturnNull() throws Exception {
        Assertions.assertThat(_appService.search(null)).isEqualTo(null);
    }

    //Cases
    @Test
    public void GetCases_IfPersonCaseIdAndCasesIdIsNotNullAndPersonCaseExists_ReturnCases() {
        PersonCase personCase = mock(PersonCase.class);
        Optional<PersonCase> personCaseOptional = Optional.of((PersonCase) personCase);
        Cases casesEntity = mock(Cases.class);

        Mockito.when(_personCaseRepository.findById(any(PersonCaseId.class))).thenReturn(personCaseOptional);

        Mockito.when(personCase.getCases()).thenReturn(casesEntity);
        Assertions
            .assertThat(_appService.getCases(personCaseId))
            .isEqualTo(_mapper.casesToGetCasesOutput(casesEntity, personCase));
    }

    @Test
    public void GetCases_IfPersonCaseIdAndCasesIdIsNotNullAndPersonCaseDoesNotExist_ReturnNull() {
        Optional<PersonCase> nullOptional = Optional.ofNullable(null);
        Mockito.when(_personCaseRepository.findById(any(PersonCaseId.class))).thenReturn(nullOptional);
        Assertions.assertThat(_appService.getCases(personCaseId)).isEqualTo(null);
    }

    //Person
    @Test
    public void GetPerson_IfPersonCaseIdAndPersonIdIsNotNullAndPersonCaseExists_ReturnPerson() {
        PersonCase personCase = mock(PersonCase.class);
        Optional<PersonCase> personCaseOptional = Optional.of((PersonCase) personCase);
        Person personEntity = mock(Person.class);

        Mockito.when(_personCaseRepository.findById(any(PersonCaseId.class))).thenReturn(personCaseOptional);

        Mockito.when(personCase.getPerson()).thenReturn(personEntity);
        Assertions
            .assertThat(_appService.getPerson(personCaseId))
            .isEqualTo(_mapper.personToGetPersonOutput(personEntity, personCase));
    }

    @Test
    public void GetPerson_IfPersonCaseIdAndPersonIdIsNotNullAndPersonCaseDoesNotExist_ReturnNull() {
        Optional<PersonCase> nullOptional = Optional.ofNullable(null);
        Mockito.when(_personCaseRepository.findById(any(PersonCaseId.class))).thenReturn(nullOptional);
        Assertions.assertThat(_appService.getPerson(personCaseId)).isEqualTo(null);
    }

    @Test
    public void ParsePersonCaseKey_KeysStringIsNotEmptyAndKeyValuePairExists_ReturnPersonCaseId() {
        String keyString = "caseId=15,personId=15";

        PersonCaseId personCaseId = new PersonCaseId();
        personCaseId.setCaseId(15L);
        personCaseId.setPersonId(15L);

        Assertions.assertThat(_appService.parsePersonCaseKey(keyString)).isEqualToComparingFieldByField(personCaseId);
    }

    @Test
    public void ParsePersonCaseKey_KeysStringIsEmpty_ReturnNull() {
        String keyString = "";
        Assertions.assertThat(_appService.parsePersonCaseKey(keyString)).isEqualTo(null);
    }

    @Test
    public void ParsePersonCaseKey_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull() {
        String keyString = "caseId";

        Assertions.assertThat(_appService.parsePersonCaseKey(keyString)).isEqualTo(null);
    }
}
