package com.fastcode.example.application.core.cases;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fastcode.example.application.core.cases.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.domain.core.cases.*;
import com.fastcode.example.domain.core.cases.Cases;
import com.fastcode.example.domain.core.cases.QCases;
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
public class CasesAppServiceTest {

    @InjectMocks
    @Spy
    protected CasesAppService _appService;

    @Mock
    protected ICasesRepository _casesRepository;

    @Mock
    protected ICasesMapper _mapper;

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
    public void findCasesById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
        Optional<Cases> nullOptional = Optional.ofNullable(null);
        Mockito.when(_casesRepository.findById(anyLong())).thenReturn(nullOptional);
        Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
    }

    @Test
    public void findCasesById_IdIsNotNullAndIdExists_ReturnCases() {
        Cases cases = mock(Cases.class);
        Optional<Cases> casesOptional = Optional.of((Cases) cases);
        Mockito.when(_casesRepository.findById(anyLong())).thenReturn(casesOptional);

        Assertions.assertThat(_appService.findById(ID)).isEqualTo(_mapper.casesToFindCasesByIdOutput(cases));
    }

    @Test
    public void createCases_CasesIsNotNullAndCasesDoesNotExist_StoreCases() {
        Cases casesEntity = mock(Cases.class);
        CreateCasesInput casesInput = new CreateCasesInput();

        Mockito.when(_mapper.createCasesInputToCases(any(CreateCasesInput.class))).thenReturn(casesEntity);
        Mockito.when(_casesRepository.save(any(Cases.class))).thenReturn(casesEntity);

        Assertions.assertThat(_appService.create(casesInput)).isEqualTo(_mapper.casesToCreateCasesOutput(casesEntity));
    }

    @Test
    public void updateCases_CasesIdIsNotNullAndIdExists_ReturnUpdatedCases() {
        Cases casesEntity = mock(Cases.class);
        UpdateCasesInput cases = mock(UpdateCasesInput.class);

        Optional<Cases> casesOptional = Optional.of((Cases) casesEntity);
        Mockito.when(_casesRepository.findById(anyLong())).thenReturn(casesOptional);

        Mockito.when(_mapper.updateCasesInputToCases(any(UpdateCasesInput.class))).thenReturn(casesEntity);
        Mockito.when(_casesRepository.save(any(Cases.class))).thenReturn(casesEntity);
        Assertions.assertThat(_appService.update(ID, cases)).isEqualTo(_mapper.casesToUpdateCasesOutput(casesEntity));
    }

    @Test
    public void deleteCases_CasesIsNotNullAndCasesExists_CasesRemoved() {
        Cases cases = mock(Cases.class);
        Optional<Cases> casesOptional = Optional.of((Cases) cases);
        Mockito.when(_casesRepository.findById(anyLong())).thenReturn(casesOptional);

        _appService.delete(ID);
        verify(_casesRepository).delete(cases);
    }

    @Test
    public void find_ListIsEmpty_ReturnList() throws Exception {
        List<Cases> list = new ArrayList<>();
        Page<Cases> foundPage = new PageImpl(list);
        Pageable pageable = mock(Pageable.class);
        List<FindCasesByIdOutput> output = new ArrayList<>();
        SearchCriteria search = new SearchCriteria();

        Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
        Mockito.when(_casesRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(foundPage);
        Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
    }

    @Test
    public void find_ListIsNotEmpty_ReturnList() throws Exception {
        List<Cases> list = new ArrayList<>();
        Cases cases = mock(Cases.class);
        list.add(cases);
        Page<Cases> foundPage = new PageImpl(list);
        Pageable pageable = mock(Pageable.class);
        List<FindCasesByIdOutput> output = new ArrayList<>();
        SearchCriteria search = new SearchCriteria();

        output.add(_mapper.casesToFindCasesByIdOutput(cases));

        Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
        Mockito.when(_casesRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(foundPage);
        Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
    }

    @Test
    public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
        QCases cases = QCases.casesEntity;
        SearchFields searchFields = new SearchFields();
        searchFields.setOperator("equals");
        searchFields.setSearchValue("xyz");
        Map<String, SearchFields> map = new HashMap<>();
        map.put("status", searchFields);
        Map<String, String> searchMap = new HashMap<>();
        searchMap.put("xyz", String.valueOf(ID));
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(cases.status.eq("xyz"));
        Assertions.assertThat(_appService.searchKeyValuePair(cases, map, searchMap)).isEqualTo(builder);
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
        list.add("status");
        list.add("summary");
        list.add("type");
        _appService.checkProperties(list);
    }

    @Test
    public void search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
        Map<String, SearchFields> map = new HashMap<>();
        QCases cases = QCases.casesEntity;
        List<SearchFields> fieldsList = new ArrayList<>();
        SearchFields fields = new SearchFields();
        SearchCriteria search = new SearchCriteria();
        search.setType(3);
        search.setValue("xyz");
        search.setOperator("equals");
        fields.setFieldName("status");
        fields.setOperator("equals");
        fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
        BooleanBuilder builder = new BooleanBuilder();
        builder.or(cases.status.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
        Mockito
            .doReturn(builder)
            .when(_appService)
            .searchKeyValuePair(any(QCases.class), any(HashMap.class), any(HashMap.class));

        Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
    }

    @Test
    public void search_StringIsNull_ReturnNull() throws Exception {
        Assertions.assertThat(_appService.search(null)).isEqualTo(null);
    }

    @Test
    public void ParsecaseDocumentsJoinColumn_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull() {
        Map<String, String> joinColumnMap = new HashMap<String, String>();
        String keyString = "15";
        joinColumnMap.put("caseId", keyString);
        Assertions.assertThat(_appService.parseCaseDocumentsJoinColumn(keyString)).isEqualTo(joinColumnMap);
    }

    @Test
    public void ParsecaseHistorysJoinColumn_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull() {
        Map<String, String> joinColumnMap = new HashMap<String, String>();
        String keyString = "15";
        joinColumnMap.put("caseId", keyString);
        Assertions.assertThat(_appService.parseCaseHistorysJoinColumn(keyString)).isEqualTo(joinColumnMap);
    }

    @Test
    public void ParsepersonCasesJoinColumn_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull() {
        Map<String, String> joinColumnMap = new HashMap<String, String>();
        String keyString = "15";
        joinColumnMap.put("caseId", keyString);
        Assertions.assertThat(_appService.parsePersonCasesJoinColumn(keyString)).isEqualTo(joinColumnMap);
    }

    @Test
    public void ParsetasksJoinColumn_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull() {
        Map<String, String> joinColumnMap = new HashMap<String, String>();
        String keyString = "15";
        joinColumnMap.put("caseId", keyString);
        Assertions.assertThat(_appService.parseTasksJoinColumn(keyString)).isEqualTo(joinColumnMap);
    }
}
