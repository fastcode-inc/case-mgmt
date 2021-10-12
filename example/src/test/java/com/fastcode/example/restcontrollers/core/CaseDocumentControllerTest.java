package com.fastcode.example.restcontrollers.core;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fastcode.example.DatabaseContainerConfig;
import com.fastcode.example.application.core.casedocument.CaseDocumentAppService;
import com.fastcode.example.application.core.casedocument.dto.*;
import com.fastcode.example.application.core.cases.CasesAppService;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.domain.core.*;
import com.fastcode.example.domain.core.casedocument.CaseDocument;
import com.fastcode.example.domain.core.casedocument.CaseDocumentId;
import com.fastcode.example.domain.core.casedocument.ICaseDocumentRepository;
import com.fastcode.example.domain.core.cases.Cases;
import com.fastcode.example.domain.core.cases.ICasesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.env.Environment;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
public class CaseDocumentControllerTest extends DatabaseContainerConfig {

    @Autowired
    protected SortHandlerMethodArgumentResolver sortArgumentResolver;

    @Autowired
    @Qualifier("caseDocumentRepository")
    protected ICaseDocumentRepository caseDocument_repository;

    @Autowired
    @Qualifier("casesRepository")
    protected ICasesRepository casesRepository;

    @SpyBean
    @Qualifier("caseDocumentAppService")
    protected CaseDocumentAppService caseDocumentAppService;

    @SpyBean
    @Qualifier("casesAppService")
    protected CasesAppService casesAppService;

    @SpyBean
    protected LoggingHelper logHelper;

    @SpyBean
    protected Environment env;

    @Mock
    protected Logger loggerMock;

    protected CaseDocument caseDocument;

    protected MockMvc mvc;

    @Autowired
    EntityManagerFactory emf;

    static EntityManagerFactory emfs;

    static int relationCount = 10;
    static int yearCount = 1971;
    static int dayCount = 10;
    private BigDecimal bigdec = new BigDecimal(1.2);

    int countCases = 10;

    @PostConstruct
    public void init() {
        emfs = emf;
    }

    @AfterClass
    public static void cleanup() {
        EntityManager em = emfs.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("truncate table public.case_document CASCADE").executeUpdate();
        em.createNativeQuery("truncate table public.cases CASCADE").executeUpdate();
        em.getTransaction().commit();
    }

    public Cases createCasesEntity() {
        if (countCases > 60) {
            countCases = 10;
        }

        if (dayCount >= 31) {
            dayCount = 10;
            yearCount = yearCount++;
        }

        Cases casesEntity = new Cases();

        casesEntity.setCaseId(Long.valueOf(relationCount));
        casesEntity.setStatus(String.valueOf(relationCount));
        casesEntity.setSummary(String.valueOf(relationCount));
        casesEntity.setType(String.valueOf(relationCount));
        casesEntity.setVersiono(0L);
        relationCount++;
        if (!casesRepository.findAll().contains(casesEntity)) {
            casesEntity = casesRepository.save(casesEntity);
        }
        countCases++;
        return casesEntity;
    }

    public CaseDocument createEntity() {
        Cases cases = createCasesEntity();

        CaseDocument caseDocumentEntity = new CaseDocument();
        caseDocumentEntity.setCaseId(1L);
        caseDocumentEntity.setFileId(1L);
        caseDocumentEntity.setVersiono(0L);
        caseDocumentEntity.setCases(cases);
        caseDocumentEntity.setCaseId(Long.parseLong(cases.getCaseId().toString()));

        return caseDocumentEntity;
    }

    public CreateCaseDocumentInput createCaseDocumentInput() {
        CreateCaseDocumentInput caseDocumentInput = new CreateCaseDocumentInput();
        caseDocumentInput.setCaseId(5L);
        caseDocumentInput.setFileId(5L);

        return caseDocumentInput;
    }

    public CaseDocument createNewEntity() {
        CaseDocument caseDocument = new CaseDocument();
        caseDocument.setCaseId(3L);
        caseDocument.setFileId(3L);

        return caseDocument;
    }

    public CaseDocument createUpdateEntity() {
        CaseDocument caseDocument = new CaseDocument();
        caseDocument.setCaseId(4L);
        caseDocument.setFileId(4L);

        return caseDocument;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        final CaseDocumentController caseDocumentController = new CaseDocumentController(
            caseDocumentAppService,
            casesAppService,
            logHelper,
            env
        );
        when(logHelper.getLogger()).thenReturn(loggerMock);
        doNothing().when(loggerMock).error(anyString());

        this.mvc =
            MockMvcBuilders
                .standaloneSetup(caseDocumentController)
                .setCustomArgumentResolvers(sortArgumentResolver)
                .setControllerAdvice()
                .build();
    }

    @Before
    public void initTest() {
        caseDocument = createEntity();
        List<CaseDocument> list = caseDocument_repository.findAll();
        if (!list.contains(caseDocument)) {
            caseDocument = caseDocument_repository.save(caseDocument);
        }
    }

    @Test
    public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(
                get("/caseDocument/caseId=" + caseDocument.getCaseId() + ",fileId=" + caseDocument.getFileId() + "/")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    public void FindById_IdIsNotValid_ReturnStatusNotFound() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(get("/caseDocument/caseId=999,fileId=999").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void CreateCaseDocument_CaseDocumentDoesNotExist_ReturnStatusOk() throws Exception {
        CreateCaseDocumentInput caseDocumentInput = createCaseDocumentInput();

        Cases cases = createCasesEntity();

        caseDocumentInput.setCaseId(Long.parseLong(cases.getCaseId().toString()));

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();

        String json = ow.writeValueAsString(caseDocumentInput);

        mvc
            .perform(post("/caseDocument").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk());
    }

    @Test
    public void DeleteCaseDocument_IdIsNotValid_ThrowEntityNotFoundException() {
        doReturn(null).when(caseDocumentAppService).findById(new CaseDocumentId(999L, 999L));
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(delete("/caseDocument/caseId=999,fileId=999").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(
                new EntityNotFoundException("There does not exist a caseDocument with a id=caseId=999,fileId=999")
            );
    }

    @Test
    public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
        CaseDocument entity = createNewEntity();
        entity.setVersiono(0L);
        Cases cases = createCasesEntity();
        entity.setCaseId(Long.parseLong(cases.getCaseId().toString()));
        entity.setCases(cases);
        entity = caseDocument_repository.save(entity);

        FindCaseDocumentByIdOutput output = new FindCaseDocumentByIdOutput();
        output.setCaseId(entity.getCaseId());
        output.setFileId(entity.getFileId());

        //    Mockito.when(caseDocumentAppService.findById(new CaseDocumentId(entity.getCaseId(), entity.getFileId()))).thenReturn(output);
        Mockito
            .doReturn(output)
            .when(caseDocumentAppService)
            .findById(new CaseDocumentId(entity.getCaseId(), entity.getFileId()));

        mvc
            .perform(
                delete("/caseDocument/caseId=" + entity.getCaseId() + ",fileId=" + entity.getFileId() + "/")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());
    }

    @Test
    public void UpdateCaseDocument_CaseDocumentDoesNotExist_ReturnStatusNotFound() throws Exception {
        doReturn(null).when(caseDocumentAppService).findById(new CaseDocumentId(999L, 999L));

        UpdateCaseDocumentInput caseDocument = new UpdateCaseDocumentInput();
        caseDocument.setCaseId(999L);
        caseDocument.setFileId(999L);

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(caseDocument);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            put("/caseDocument/caseId=999,fileId=999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(
                new EntityNotFoundException("Unable to update. CaseDocument with id=caseId=999,fileId=999 not found.")
            );
    }

    @Test
    public void UpdateCaseDocument_CaseDocumentExists_ReturnStatusOk() throws Exception {
        CaseDocument entity = createUpdateEntity();
        entity.setVersiono(0L);

        Cases cases = createCasesEntity();
        entity.setCaseId(Long.parseLong(cases.getCaseId().toString()));
        entity.setCases(cases);
        entity = caseDocument_repository.save(entity);
        FindCaseDocumentByIdOutput output = new FindCaseDocumentByIdOutput();
        output.setCaseId(entity.getCaseId());
        output.setFileId(entity.getFileId());
        output.setVersiono(entity.getVersiono());

        Mockito
            .when(caseDocumentAppService.findById(new CaseDocumentId(entity.getCaseId(), entity.getFileId())))
            .thenReturn(output);

        UpdateCaseDocumentInput caseDocumentInput = new UpdateCaseDocumentInput();
        caseDocumentInput.setCaseId(entity.getCaseId());
        caseDocumentInput.setFileId(entity.getFileId());

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(caseDocumentInput);

        mvc
            .perform(
                put("/caseDocument/caseId=" + entity.getCaseId() + ",fileId=" + entity.getFileId() + "/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isOk());

        CaseDocument de = createUpdateEntity();
        de.setCaseId(entity.getCaseId());
        de.setFileId(entity.getFileId());
        caseDocument_repository.delete(de);
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(
                get("/caseDocument?search=caseId[equals]=1&limit=10&offset=1").contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/caseDocument?search=caseDocumentcaseId[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property caseDocumentcaseId not found!"));
    }

    @Test
    public void GetCases_IdIsNotEmptyAndIdIsNotValid_ThrowException() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(get("/caseDocument/caseId999/cases").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Invalid id=caseId999"));
    }

    @Test
    public void GetCases_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/caseDocument/caseId=999,fileId=999/cases").contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void GetCases_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
        mvc
            .perform(
                get(
                    "/caseDocument/caseId=" +
                    caseDocument.getCaseId() +
                    ",fileId=" +
                    caseDocument.getFileId() +
                    "/cases"
                )
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }
}
