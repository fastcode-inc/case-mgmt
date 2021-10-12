package com.fastcode.example.application.core.casehistory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fastcode.example.application.core.casehistory.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.domain.core.casehistory.*;
import com.fastcode.example.domain.core.casehistory.CaseHistory;
import com.fastcode.example.domain.core.casehistory.QCaseHistory;
import com.fastcode.example.domain.core.cases.Cases;
import com.fastcode.example.domain.core.cases.ICasesRepository;
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
public class CaseHistoryAppServiceTest {

    @InjectMocks
    @Spy
    protected CaseHistoryAppService _appService;

    @Mock
    protected ICaseHistoryRepository _caseHistoryRepository;

    @Mock
    protected ICasesRepository _casesRepository;

    @Mock
    protected ICaseHistoryMapper _mapper;

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
    public void findCaseHistoryById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
        Optional<CaseHistory> nullOptional = Optional.ofNullable(null);
        Mockito.when(_caseHistoryRepository.findById(anyLong())).thenReturn(nullOptional);
        Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
    }

    @Test
    public void findCaseHistoryById_IdIsNotNullAndIdExists_ReturnCaseHistory() {
        CaseHistory caseHistory = mock(CaseHistory.class);
        Optional<CaseHistory> caseHistoryOptional = Optional.of((CaseHistory) caseHistory);
        Mockito.when(_caseHistoryRepository.findById(anyLong())).thenReturn(caseHistoryOptional);

        Assertions
            .assertThat(_appService.findById(ID))
            .isEqualTo(_mapper.caseHistoryToFindCaseHistoryByIdOutput(caseHistory));
    }

    @Test
    public void createCaseHistory_CaseHistoryIsNotNullAndCaseHistoryDoesNotExist_StoreCaseHistory() {
        CaseHistory caseHistoryEntity = mock(CaseHistory.class);
        CreateCaseHistoryInput caseHistoryInput = new CreateCaseHistoryInput();

        Cases cases = mock(Cases.class);
        Optional<Cases> casesOptional = Optional.of((Cases) cases);
        caseHistoryInput.setCaseId(15L);

        Mockito.when(_casesRepository.findById(any(Long.class))).thenReturn(casesOptional);

        Mockito
            .when(_mapper.createCaseHistoryInputToCaseHistory(any(CreateCaseHistoryInput.class)))
            .thenReturn(caseHistoryEntity);
        Mockito.when(_caseHistoryRepository.save(any(CaseHistory.class))).thenReturn(caseHistoryEntity);

        Assertions
            .assertThat(_appService.create(caseHistoryInput))
            .isEqualTo(_mapper.caseHistoryToCreateCaseHistoryOutput(caseHistoryEntity));
    }

    @Test
    public void createCaseHistory_CaseHistoryIsNotNullAndCaseHistoryDoesNotExistAndChildIsNullAndChildIsMandatory_ReturnNull() {
        CreateCaseHistoryInput caseHistory = mock(CreateCaseHistoryInput.class);

        Mockito.when(_mapper.createCaseHistoryInputToCaseHistory(any(CreateCaseHistoryInput.class))).thenReturn(null);
        Assertions.assertThat(_appService.create(caseHistory)).isEqualTo(null);
    }

    @Test
    public void createCaseHistory_CaseHistoryIsNotNullAndCaseHistoryDoesNotExistAndChildIsNotNullAndChildIsMandatoryAndFindByIdIsNull_ReturnNull() {
        CreateCaseHistoryInput caseHistory = new CreateCaseHistoryInput();

        caseHistory.setCaseId(15L);

        Optional<Cases> nullOptional = Optional.ofNullable(null);
        Mockito.when(_casesRepository.findById(any(Long.class))).thenReturn(nullOptional);

        //		Mockito.when(_casesRepository.findById(any(Long.class))).thenReturn(nullOptional);
        Assertions.assertThat(_appService.create(caseHistory)).isEqualTo(null);
    }

    @Test
    public void updateCaseHistory_CaseHistoryIsNotNullAndCaseHistoryDoesNotExistAndChildIsNullAndChildIsMandatory_ReturnNull() {
        UpdateCaseHistoryInput caseHistoryInput = mock(UpdateCaseHistoryInput.class);
        CaseHistory caseHistory = mock(CaseHistory.class);

        Optional<CaseHistory> caseHistoryOptional = Optional.of((CaseHistory) caseHistory);
        Mockito.when(_caseHistoryRepository.findById(anyLong())).thenReturn(caseHistoryOptional);

        Mockito
            .when(_mapper.updateCaseHistoryInputToCaseHistory(any(UpdateCaseHistoryInput.class)))
            .thenReturn(caseHistory);
        Assertions.assertThat(_appService.update(ID, caseHistoryInput)).isEqualTo(null);
    }

    @Test
    public void updateCaseHistory_CaseHistoryIsNotNullAndCaseHistoryDoesNotExistAndChildIsNotNullAndChildIsMandatoryAndFindByIdIsNull_ReturnNull() {
        UpdateCaseHistoryInput caseHistoryInput = new UpdateCaseHistoryInput();
        caseHistoryInput.setCaseId(15L);

        CaseHistory caseHistory = mock(CaseHistory.class);

        Optional<CaseHistory> caseHistoryOptional = Optional.of((CaseHistory) caseHistory);
        Mockito.when(_caseHistoryRepository.findById(anyLong())).thenReturn(caseHistoryOptional);

        Mockito
            .when(_mapper.updateCaseHistoryInputToCaseHistory(any(UpdateCaseHistoryInput.class)))
            .thenReturn(caseHistory);
        Optional<Cases> nullOptional = Optional.ofNullable(null);
        Mockito.when(_casesRepository.findById(any(Long.class))).thenReturn(nullOptional);

        //	Mockito.when(_casesRepository.findById(any(Long.class))).thenReturn(nullOptional);
        Assertions.assertThat(_appService.update(ID, caseHistoryInput)).isEqualTo(null);
    }

    @Test
    public void updateCaseHistory_CaseHistoryIdIsNotNullAndIdExists_ReturnUpdatedCaseHistory() {
        CaseHistory caseHistoryEntity = mock(CaseHistory.class);
        UpdateCaseHistoryInput caseHistory = mock(UpdateCaseHistoryInput.class);

        Optional<CaseHistory> caseHistoryOptional = Optional.of((CaseHistory) caseHistoryEntity);
        Mockito.when(_caseHistoryRepository.findById(anyLong())).thenReturn(caseHistoryOptional);

        Mockito
            .when(_mapper.updateCaseHistoryInputToCaseHistory(any(UpdateCaseHistoryInput.class)))
            .thenReturn(caseHistoryEntity);
        Mockito.when(_caseHistoryRepository.save(any(CaseHistory.class))).thenReturn(caseHistoryEntity);
        Assertions
            .assertThat(_appService.update(ID, caseHistory))
            .isEqualTo(_mapper.caseHistoryToUpdateCaseHistoryOutput(caseHistoryEntity));
    }

    @Test
    public void deleteCaseHistory_CaseHistoryIsNotNullAndCaseHistoryExists_CaseHistoryRemoved() {
        CaseHistory caseHistory = mock(CaseHistory.class);
        Optional<CaseHistory> caseHistoryOptional = Optional.of((CaseHistory) caseHistory);
        Mockito.when(_caseHistoryRepository.findById(anyLong())).thenReturn(caseHistoryOptional);

        _appService.delete(ID);
        verify(_caseHistoryRepository).delete(caseHistory);
    }

    @Test
    public void find_ListIsEmpty_ReturnList() throws Exception {
        List<CaseHistory> list = new ArrayList<>();
        Page<CaseHistory> foundPage = new PageImpl(list);
        Pageable pageable = mock(Pageable.class);
        List<FindCaseHistoryByIdOutput> output = new ArrayList<>();
        SearchCriteria search = new SearchCriteria();

        Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
        Mockito.when(_caseHistoryRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(foundPage);
        Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
    }

    @Test
    public void find_ListIsNotEmpty_ReturnList() throws Exception {
        List<CaseHistory> list = new ArrayList<>();
        CaseHistory caseHistory = mock(CaseHistory.class);
        list.add(caseHistory);
        Page<CaseHistory> foundPage = new PageImpl(list);
        Pageable pageable = mock(Pageable.class);
        List<FindCaseHistoryByIdOutput> output = new ArrayList<>();
        SearchCriteria search = new SearchCriteria();

        output.add(_mapper.caseHistoryToFindCaseHistoryByIdOutput(caseHistory));

        Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
        Mockito.when(_caseHistoryRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(foundPage);
        Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
    }

    @Test
    public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
        QCaseHistory caseHistory = QCaseHistory.caseHistoryEntity;
        SearchFields searchFields = new SearchFields();
        searchFields.setOperator("equals");
        searchFields.setSearchValue("xyz");
        Map<String, SearchFields> map = new HashMap<>();
        map.put("message", searchFields);
        Map<String, String> searchMap = new HashMap<>();
        searchMap.put("xyz", String.valueOf(ID));
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(caseHistory.message.eq("xyz"));
        Assertions.assertThat(_appService.searchKeyValuePair(caseHistory, map, searchMap)).isEqualTo(builder);
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
        list.add("message");
        _appService.checkProperties(list);
    }

    @Test
    public void search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
        Map<String, SearchFields> map = new HashMap<>();
        QCaseHistory caseHistory = QCaseHistory.caseHistoryEntity;
        List<SearchFields> fieldsList = new ArrayList<>();
        SearchFields fields = new SearchFields();
        SearchCriteria search = new SearchCriteria();
        search.setType(3);
        search.setValue("xyz");
        search.setOperator("equals");
        fields.setFieldName("message");
        fields.setOperator("equals");
        fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
        BooleanBuilder builder = new BooleanBuilder();
        builder.or(caseHistory.message.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
        Mockito
            .doReturn(builder)
            .when(_appService)
            .searchKeyValuePair(any(QCaseHistory.class), any(HashMap.class), any(HashMap.class));

        Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
    }

    @Test
    public void search_StringIsNull_ReturnNull() throws Exception {
        Assertions.assertThat(_appService.search(null)).isEqualTo(null);
    }

    //Cases
    @Test
    public void GetCases_IfCaseHistoryIdAndCasesIdIsNotNullAndCaseHistoryExists_ReturnCases() {
        CaseHistory caseHistory = mock(CaseHistory.class);
        Optional<CaseHistory> caseHistoryOptional = Optional.of((CaseHistory) caseHistory);
        Cases casesEntity = mock(Cases.class);

        Mockito.when(_caseHistoryRepository.findById(any(Long.class))).thenReturn(caseHistoryOptional);

        Mockito.when(caseHistory.getCases()).thenReturn(casesEntity);
        Assertions
            .assertThat(_appService.getCases(ID))
            .isEqualTo(_mapper.casesToGetCasesOutput(casesEntity, caseHistory));
    }

    @Test
    public void GetCases_IfCaseHistoryIdAndCasesIdIsNotNullAndCaseHistoryDoesNotExist_ReturnNull() {
        Optional<CaseHistory> nullOptional = Optional.ofNullable(null);
        Mockito.when(_caseHistoryRepository.findById(anyLong())).thenReturn(nullOptional);
        Assertions.assertThat(_appService.getCases(ID)).isEqualTo(null);
    }
}
