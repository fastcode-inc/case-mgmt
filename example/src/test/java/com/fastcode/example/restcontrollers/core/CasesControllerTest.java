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
import com.fastcode.example.application.core.casehistory.CaseHistoryAppService;
import com.fastcode.example.application.core.cases.CasesAppService;
import com.fastcode.example.application.core.cases.dto.*;
import com.fastcode.example.application.core.personcase.PersonCaseAppService;
import com.fastcode.example.application.core.task.TaskAppService;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.domain.core.*;
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
public class CasesControllerTest extends DatabaseContainerConfig {

    @Autowired
    protected SortHandlerMethodArgumentResolver sortArgumentResolver;

    @Autowired
    @Qualifier("casesRepository")
    protected ICasesRepository cases_repository;

    @SpyBean
    @Qualifier("casesAppService")
    protected CasesAppService casesAppService;

    @SpyBean
    @Qualifier("caseDocumentAppService")
    protected CaseDocumentAppService caseDocumentAppService;

    @SpyBean
    @Qualifier("caseHistoryAppService")
    protected CaseHistoryAppService caseHistoryAppService;

    @SpyBean
    @Qualifier("personCaseAppService")
    protected PersonCaseAppService personCaseAppService;

    @SpyBean
    @Qualifier("taskAppService")
    protected TaskAppService taskAppService;

    @SpyBean
    protected LoggingHelper logHelper;

    @SpyBean
    protected Environment env;

    @Mock
    protected Logger loggerMock;

    protected Cases cases;

    protected MockMvc mvc;

    @Autowired
    EntityManagerFactory emf;

    static EntityManagerFactory emfs;

    static int relationCount = 10;
    static int yearCount = 1971;
    static int dayCount = 10;
    private BigDecimal bigdec = new BigDecimal(1.2);

    @PostConstruct
    public void init() {
        emfs = emf;
    }

    @AfterClass
    public static void cleanup() {
        EntityManager em = emfs.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("truncate table public.cases CASCADE").executeUpdate();
        em.getTransaction().commit();
    }

    public Cases createEntity() {
        Cases casesEntity = new Cases();
        casesEntity.setCaseId(1L);
        casesEntity.setStatus("1");
        casesEntity.setSummary("1");
        casesEntity.setType("1");
        casesEntity.setVersiono(0L);

        return casesEntity;
    }

    public CreateCasesInput createCasesInput() {
        CreateCasesInput casesInput = new CreateCasesInput();
        casesInput.setCaseId(5L);
        casesInput.setStatus("5");
        casesInput.setSummary("5");
        casesInput.setType("5");

        return casesInput;
    }

    public Cases createNewEntity() {
        Cases cases = new Cases();
        cases.setCaseId(3L);
        cases.setStatus("3");
        cases.setSummary("3");
        cases.setType("3");

        return cases;
    }

    public Cases createUpdateEntity() {
        Cases cases = new Cases();
        cases.setCaseId(4L);
        cases.setStatus("4");
        cases.setSummary("4");
        cases.setType("4");

        return cases;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        final CasesController casesController = new CasesController(
            casesAppService,
            caseDocumentAppService,
            caseHistoryAppService,
            personCaseAppService,
            taskAppService,
            logHelper,
            env
        );
        when(logHelper.getLogger()).thenReturn(loggerMock);
        doNothing().when(loggerMock).error(anyString());

        this.mvc =
            MockMvcBuilders
                .standaloneSetup(casesController)
                .setCustomArgumentResolvers(sortArgumentResolver)
                .setControllerAdvice()
                .build();
    }

    @Before
    public void initTest() {
        cases = createEntity();
        List<Cases> list = cases_repository.findAll();
        if (!list.contains(cases)) {
            cases = cases_repository.save(cases);
        }
    }

    @Test
    public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(get("/cases/" + cases.getCaseId() + "/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void FindById_IdIsNotValid_ReturnStatusNotFound() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () -> mvc.perform(get("/cases/999").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void CreateCases_CasesDoesNotExist_ReturnStatusOk() throws Exception {
        CreateCasesInput casesInput = createCasesInput();

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();

        String json = ow.writeValueAsString(casesInput);

        mvc.perform(post("/cases").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
    }

    @Test
    public void DeleteCases_IdIsNotValid_ThrowEntityNotFoundException() {
        doReturn(null).when(casesAppService).findById(999L);
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc.perform(delete("/cases/999").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("There does not exist a cases with a id=999"));
    }

    @Test
    public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
        Cases entity = createNewEntity();
        entity.setVersiono(0L);
        entity = cases_repository.save(entity);

        FindCasesByIdOutput output = new FindCasesByIdOutput();
        output.setCaseId(entity.getCaseId());

        Mockito.doReturn(output).when(casesAppService).findById(entity.getCaseId());

        //    Mockito.when(casesAppService.findById(entity.getCaseId())).thenReturn(output);

        mvc
            .perform(delete("/cases/" + entity.getCaseId() + "/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    public void UpdateCases_CasesDoesNotExist_ReturnStatusNotFound() throws Exception {
        doReturn(null).when(casesAppService).findById(999L);

        UpdateCasesInput cases = new UpdateCasesInput();
        cases.setCaseId(999L);
        cases.setStatus("999");
        cases.setSummary("999");
        cases.setType("999");

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(cases);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(put("/cases/999").contentType(MediaType.APPLICATION_JSON).content(json))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Unable to update. Cases with id=999 not found."));
    }

    @Test
    public void UpdateCases_CasesExists_ReturnStatusOk() throws Exception {
        Cases entity = createUpdateEntity();
        entity.setVersiono(0L);

        entity = cases_repository.save(entity);
        FindCasesByIdOutput output = new FindCasesByIdOutput();
        output.setCaseId(entity.getCaseId());
        output.setStatus(entity.getStatus());
        output.setSummary(entity.getSummary());
        output.setType(entity.getType());
        output.setVersiono(entity.getVersiono());

        Mockito.when(casesAppService.findById(entity.getCaseId())).thenReturn(output);

        UpdateCasesInput casesInput = new UpdateCasesInput();
        casesInput.setCaseId(entity.getCaseId());

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(casesInput);

        mvc
            .perform(put("/cases/" + entity.getCaseId() + "/").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk());

        Cases de = createUpdateEntity();
        de.setCaseId(entity.getCaseId());
        cases_repository.delete(de);
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(get("/cases?search=caseId[equals]=1&limit=10&offset=1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/cases?search=casescaseId[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property casescaseId not found!"));
    }

    @Test
    public void GetCaseDocuments_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() {
        Map<String, String> joinCol = new HashMap<String, String>();
        joinCol.put("caseId", "1");

        Mockito.when(casesAppService.parseCaseDocumentsJoinColumn("caseid")).thenReturn(joinCol);
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/cases/1/caseDocuments?search=abc[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property abc not found!"));
    }

    @Test
    public void GetCaseDocuments_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
        Map<String, String> joinCol = new HashMap<String, String>();
        joinCol.put("caseId", "1");

        Mockito.when(casesAppService.parseCaseDocumentsJoinColumn("caseId")).thenReturn(joinCol);
        mvc
            .perform(
                get("/cases/1/caseDocuments?search=caseId[equals]=1&limit=10&offset=1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    public void GetCaseDocuments_searchIsNotEmpty() {
        Mockito.when(casesAppService.parseCaseDocumentsJoinColumn(anyString())).thenReturn(null);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/cases/1/caseDocuments?search=caseId[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Invalid join column"));
    }

    @Test
    public void GetCaseHistorys_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() {
        Map<String, String> joinCol = new HashMap<String, String>();
        joinCol.put("caseId", "1");

        Mockito.when(casesAppService.parseCaseHistorysJoinColumn("caseid")).thenReturn(joinCol);
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/cases/1/caseHistorys?search=abc[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property abc not found!"));
    }

    @Test
    public void GetCaseHistorys_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
        Map<String, String> joinCol = new HashMap<String, String>();
        joinCol.put("caseId", "1");

        Mockito.when(casesAppService.parseCaseHistorysJoinColumn("caseId")).thenReturn(joinCol);
        mvc
            .perform(
                get("/cases/1/caseHistorys?search=caseId[equals]=1&limit=10&offset=1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    public void GetCaseHistorys_searchIsNotEmpty() {
        Mockito.when(casesAppService.parseCaseHistorysJoinColumn(anyString())).thenReturn(null);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/cases/1/caseHistorys?search=caseId[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Invalid join column"));
    }

    @Test
    public void GetPersonCases_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() {
        Map<String, String> joinCol = new HashMap<String, String>();
        joinCol.put("caseId", "1");

        Mockito.when(casesAppService.parsePersonCasesJoinColumn("caseid")).thenReturn(joinCol);
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/cases/1/personCases?search=abc[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property abc not found!"));
    }

    @Test
    public void GetPersonCases_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
        Map<String, String> joinCol = new HashMap<String, String>();
        joinCol.put("caseId", "1");

        Mockito.when(casesAppService.parsePersonCasesJoinColumn("caseId")).thenReturn(joinCol);
        mvc
            .perform(
                get("/cases/1/personCases?search=caseId[equals]=1&limit=10&offset=1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    public void GetPersonCases_searchIsNotEmpty() {
        Mockito.when(casesAppService.parsePersonCasesJoinColumn(anyString())).thenReturn(null);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/cases/1/personCases?search=caseId[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Invalid join column"));
    }

    @Test
    public void GetTasks_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() {
        Map<String, String> joinCol = new HashMap<String, String>();
        joinCol.put("caseId", "1");

        Mockito.when(casesAppService.parseTasksJoinColumn("caseid")).thenReturn(joinCol);
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/cases/1/tasks?search=abc[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property abc not found!"));
    }

    @Test
    public void GetTasks_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
        Map<String, String> joinCol = new HashMap<String, String>();
        joinCol.put("caseId", "1");

        Mockito.when(casesAppService.parseTasksJoinColumn("caseId")).thenReturn(joinCol);
        mvc
            .perform(
                get("/cases/1/tasks?search=caseId[equals]=1&limit=10&offset=1").contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    public void GetTasks_searchIsNotEmpty() {
        Mockito.when(casesAppService.parseTasksJoinColumn(anyString())).thenReturn(null);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/cases/1/tasks?search=caseId[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Invalid join column"));
    }
}
