package com.fastcode.example.application.core.casedocument;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fastcode.example.application.core.casedocument.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.domain.core.casedocument.*;
import com.fastcode.example.domain.core.casedocument.CaseDocument;
import com.fastcode.example.domain.core.casedocument.CaseDocumentId;
import com.fastcode.example.domain.core.casedocument.QCaseDocument;
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
public class CaseDocumentAppServiceTest {

    @InjectMocks
    @Spy
    protected CaseDocumentAppService _appService;

    @Mock
    protected ICaseDocumentRepository _caseDocumentRepository;

    @Mock
    protected ICasesRepository _casesRepository;

    @Mock
    protected ICaseDocumentMapper _mapper;

    @Mock
    protected Logger loggerMock;

    @Mock
    protected LoggingHelper logHelper;

    @Mock
    protected CaseDocumentId caseDocumentId;

    private static final Long ID = 15L;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(_appService);
        when(logHelper.getLogger()).thenReturn(loggerMock);
        doNothing().when(loggerMock).error(anyString());
    }

    @Test
    public void findCaseDocumentById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
        Optional<CaseDocument> nullOptional = Optional.ofNullable(null);
        Mockito.when(_caseDocumentRepository.findById(any(CaseDocumentId.class))).thenReturn(nullOptional);
        Assertions.assertThat(_appService.findById(caseDocumentId)).isEqualTo(null);
    }

    @Test
    public void findCaseDocumentById_IdIsNotNullAndIdExists_ReturnCaseDocument() {
        CaseDocument caseDocument = mock(CaseDocument.class);
        Optional<CaseDocument> caseDocumentOptional = Optional.of((CaseDocument) caseDocument);
        Mockito.when(_caseDocumentRepository.findById(any(CaseDocumentId.class))).thenReturn(caseDocumentOptional);

        Assertions
            .assertThat(_appService.findById(caseDocumentId))
            .isEqualTo(_mapper.caseDocumentToFindCaseDocumentByIdOutput(caseDocument));
    }

    @Test
    public void createCaseDocument_CaseDocumentIsNotNullAndCaseDocumentDoesNotExist_StoreCaseDocument() {
        CaseDocument caseDocumentEntity = mock(CaseDocument.class);
        CreateCaseDocumentInput caseDocumentInput = new CreateCaseDocumentInput();

        Cases cases = mock(Cases.class);
        Optional<Cases> casesOptional = Optional.of((Cases) cases);
        caseDocumentInput.setCaseId(15L);

        Mockito.when(_casesRepository.findById(any(Long.class))).thenReturn(casesOptional);

        Mockito
            .when(_mapper.createCaseDocumentInputToCaseDocument(any(CreateCaseDocumentInput.class)))
            .thenReturn(caseDocumentEntity);
        Mockito.when(_caseDocumentRepository.save(any(CaseDocument.class))).thenReturn(caseDocumentEntity);

        Assertions
            .assertThat(_appService.create(caseDocumentInput))
            .isEqualTo(_mapper.caseDocumentToCreateCaseDocumentOutput(caseDocumentEntity));
    }

    @Test
    public void createCaseDocument_CaseDocumentIsNotNullAndCaseDocumentDoesNotExistAndChildIsNullAndChildIsNotMandatory_StoreCaseDocument() {
        CaseDocument caseDocument = mock(CaseDocument.class);
        CreateCaseDocumentInput caseDocumentInput = mock(CreateCaseDocumentInput.class);

        Mockito
            .when(_mapper.createCaseDocumentInputToCaseDocument(any(CreateCaseDocumentInput.class)))
            .thenReturn(caseDocument);
        Mockito.when(_caseDocumentRepository.save(any(CaseDocument.class))).thenReturn(caseDocument);
        Assertions
            .assertThat(_appService.create(caseDocumentInput))
            .isEqualTo(_mapper.caseDocumentToCreateCaseDocumentOutput(caseDocument));
    }

    @Test
    public void updateCaseDocument_CaseDocumentIsNotNullAndCaseDocumentDoesNotExistAndChildIsNullAndChildIsNotMandatory_ReturnUpdatedCaseDocument() {
        CaseDocument caseDocument = mock(CaseDocument.class);
        UpdateCaseDocumentInput caseDocumentInput = mock(UpdateCaseDocumentInput.class);

        Optional<CaseDocument> caseDocumentOptional = Optional.of((CaseDocument) caseDocument);
        Mockito.when(_caseDocumentRepository.findById(any(CaseDocumentId.class))).thenReturn(caseDocumentOptional);

        Mockito
            .when(_mapper.updateCaseDocumentInputToCaseDocument(any(UpdateCaseDocumentInput.class)))
            .thenReturn(caseDocument);
        Mockito.when(_caseDocumentRepository.save(any(CaseDocument.class))).thenReturn(caseDocument);
        Assertions
            .assertThat(_appService.update(caseDocumentId, caseDocumentInput))
            .isEqualTo(_mapper.caseDocumentToUpdateCaseDocumentOutput(caseDocument));
    }

    @Test
    public void updateCaseDocument_CaseDocumentIdIsNotNullAndIdExists_ReturnUpdatedCaseDocument() {
        CaseDocument caseDocumentEntity = mock(CaseDocument.class);
        UpdateCaseDocumentInput caseDocument = mock(UpdateCaseDocumentInput.class);

        Optional<CaseDocument> caseDocumentOptional = Optional.of((CaseDocument) caseDocumentEntity);
        Mockito.when(_caseDocumentRepository.findById(any(CaseDocumentId.class))).thenReturn(caseDocumentOptional);

        Mockito
            .when(_mapper.updateCaseDocumentInputToCaseDocument(any(UpdateCaseDocumentInput.class)))
            .thenReturn(caseDocumentEntity);
        Mockito.when(_caseDocumentRepository.save(any(CaseDocument.class))).thenReturn(caseDocumentEntity);
        Assertions
            .assertThat(_appService.update(caseDocumentId, caseDocument))
            .isEqualTo(_mapper.caseDocumentToUpdateCaseDocumentOutput(caseDocumentEntity));
    }

    @Test
    public void deleteCaseDocument_CaseDocumentIsNotNullAndCaseDocumentExists_CaseDocumentRemoved() {
        CaseDocument caseDocument = mock(CaseDocument.class);
        Optional<CaseDocument> caseDocumentOptional = Optional.of((CaseDocument) caseDocument);
        Mockito.when(_caseDocumentRepository.findById(any(CaseDocumentId.class))).thenReturn(caseDocumentOptional);

        _appService.delete(caseDocumentId);
        verify(_caseDocumentRepository).delete(caseDocument);
    }

    @Test
    public void find_ListIsEmpty_ReturnList() throws Exception {
        List<CaseDocument> list = new ArrayList<>();
        Page<CaseDocument> foundPage = new PageImpl(list);
        Pageable pageable = mock(Pageable.class);
        List<FindCaseDocumentByIdOutput> output = new ArrayList<>();
        SearchCriteria search = new SearchCriteria();

        Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
        Mockito.when(_caseDocumentRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(foundPage);
        Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
    }

    @Test
    public void find_ListIsNotEmpty_ReturnList() throws Exception {
        List<CaseDocument> list = new ArrayList<>();
        CaseDocument caseDocument = mock(CaseDocument.class);
        list.add(caseDocument);
        Page<CaseDocument> foundPage = new PageImpl(list);
        Pageable pageable = mock(Pageable.class);
        List<FindCaseDocumentByIdOutput> output = new ArrayList<>();
        SearchCriteria search = new SearchCriteria();

        output.add(_mapper.caseDocumentToFindCaseDocumentByIdOutput(caseDocument));

        Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
        Mockito.when(_caseDocumentRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(foundPage);
        Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
    }

    @Test
    public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
        QCaseDocument caseDocument = QCaseDocument.caseDocumentEntity;
        SearchFields searchFields = new SearchFields();
        searchFields.setOperator("equals");
        searchFields.setSearchValue("xyz");
        Map<String, SearchFields> map = new HashMap<>();
        Map<String, String> searchMap = new HashMap<>();
        searchMap.put("xyz", String.valueOf(ID));
        BooleanBuilder builder = new BooleanBuilder();
        Assertions.assertThat(_appService.searchKeyValuePair(caseDocument, map, searchMap)).isEqualTo(builder);
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
        QCaseDocument caseDocument = QCaseDocument.caseDocumentEntity;
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
            .searchKeyValuePair(any(QCaseDocument.class), any(HashMap.class), any(HashMap.class));

        Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
    }

    @Test
    public void search_StringIsNull_ReturnNull() throws Exception {
        Assertions.assertThat(_appService.search(null)).isEqualTo(null);
    }

    //Cases
    @Test
    public void GetCases_IfCaseDocumentIdAndCasesIdIsNotNullAndCaseDocumentExists_ReturnCases() {
        CaseDocument caseDocument = mock(CaseDocument.class);
        Optional<CaseDocument> caseDocumentOptional = Optional.of((CaseDocument) caseDocument);
        Cases casesEntity = mock(Cases.class);

        Mockito.when(_caseDocumentRepository.findById(any(CaseDocumentId.class))).thenReturn(caseDocumentOptional);

        Mockito.when(caseDocument.getCases()).thenReturn(casesEntity);
        Assertions
            .assertThat(_appService.getCases(caseDocumentId))
            .isEqualTo(_mapper.casesToGetCasesOutput(casesEntity, caseDocument));
    }

    @Test
    public void GetCases_IfCaseDocumentIdAndCasesIdIsNotNullAndCaseDocumentDoesNotExist_ReturnNull() {
        Optional<CaseDocument> nullOptional = Optional.ofNullable(null);
        Mockito.when(_caseDocumentRepository.findById(any(CaseDocumentId.class))).thenReturn(nullOptional);
        Assertions.assertThat(_appService.getCases(caseDocumentId)).isEqualTo(null);
    }

    @Test
    public void ParseCaseDocumentKey_KeysStringIsNotEmptyAndKeyValuePairExists_ReturnCaseDocumentId() {
        String keyString = "caseId=15,fileId=15";

        CaseDocumentId caseDocumentId = new CaseDocumentId();
        caseDocumentId.setCaseId(15L);
        caseDocumentId.setFileId(15L);

        Assertions
            .assertThat(_appService.parseCaseDocumentKey(keyString))
            .isEqualToComparingFieldByField(caseDocumentId);
    }

    @Test
    public void ParseCaseDocumentKey_KeysStringIsEmpty_ReturnNull() {
        String keyString = "";
        Assertions.assertThat(_appService.parseCaseDocumentKey(keyString)).isEqualTo(null);
    }

    @Test
    public void ParseCaseDocumentKey_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull() {
        String keyString = "caseId";

        Assertions.assertThat(_appService.parseCaseDocumentKey(keyString)).isEqualTo(null);
    }
}
