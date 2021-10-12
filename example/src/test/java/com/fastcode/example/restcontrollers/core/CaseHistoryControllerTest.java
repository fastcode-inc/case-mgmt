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
import com.fastcode.example.application.core.casehistory.CaseHistoryAppService;
import com.fastcode.example.application.core.casehistory.dto.*;
import com.fastcode.example.application.core.cases.CasesAppService;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.SearchUtils;
import com.fastcode.example.domain.core.*;
import com.fastcode.example.domain.core.casehistory.CaseHistory;
import com.fastcode.example.domain.core.casehistory.ICaseHistoryRepository;
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
public class CaseHistoryControllerTest extends DatabaseContainerConfig {

    @Autowired
    protected SortHandlerMethodArgumentResolver sortArgumentResolver;

    @Autowired
    @Qualifier("caseHistoryRepository")
    protected ICaseHistoryRepository caseHistory_repository;

    @Autowired
    @Qualifier("casesRepository")
    protected ICasesRepository casesRepository;

    @SpyBean
    @Qualifier("caseHistoryAppService")
    protected CaseHistoryAppService caseHistoryAppService;

    @SpyBean
    @Qualifier("casesAppService")
    protected CasesAppService casesAppService;

    @SpyBean
    protected LoggingHelper logHelper;

    @SpyBean
    protected Environment env;

    @Mock
    protected Logger loggerMock;

    protected CaseHistory caseHistory;

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
        em.createNativeQuery("truncate table public.case_history CASCADE").executeUpdate();
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

    public CaseHistory createEntity() {
        Cases cases = createCasesEntity();

        CaseHistory caseHistoryEntity = new CaseHistory();
        caseHistoryEntity.setCaseHistoryId(1L);
        caseHistoryEntity.setMessage("1");
        caseHistoryEntity.setTimestamp(SearchUtils.stringToOffsetTime("05:26:22+07:00"));
        caseHistoryEntity.setVersiono(0L);
        caseHistoryEntity.setCases(cases);

        return caseHistoryEntity;
    }

    public CreateCaseHistoryInput createCaseHistoryInput() {
        CreateCaseHistoryInput caseHistoryInput = new CreateCaseHistoryInput();
        caseHistoryInput.setCaseHistoryId(5L);
        caseHistoryInput.setMessage("5");
        caseHistoryInput.setTimestamp(SearchUtils.stringToOffsetTime("05:26:23+07:00"));

        return caseHistoryInput;
    }

    public CaseHistory createNewEntity() {
        CaseHistory caseHistory = new CaseHistory();
        caseHistory.setCaseHistoryId(3L);
        caseHistory.setMessage("3");
        caseHistory.setTimestamp(SearchUtils.stringToOffsetTime("07:28:23+07:00"));

        return caseHistory;
    }

    public CaseHistory createUpdateEntity() {
        CaseHistory caseHistory = new CaseHistory();
        caseHistory.setCaseHistoryId(4L);
        caseHistory.setMessage("4");
        caseHistory.setTimestamp(SearchUtils.stringToOffsetTime("09:52:21+07:00"));

        return caseHistory;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        final CaseHistoryController caseHistoryController = new CaseHistoryController(
            caseHistoryAppService,
            casesAppService,
            logHelper,
            env
        );
        when(logHelper.getLogger()).thenReturn(loggerMock);
        doNothing().when(loggerMock).error(anyString());

        this.mvc =
            MockMvcBuilders
                .standaloneSetup(caseHistoryController)
                .setCustomArgumentResolvers(sortArgumentResolver)
                .setControllerAdvice()
                .build();
    }

    @Before
    public void initTest() {
        caseHistory = createEntity();
        List<CaseHistory> list = caseHistory_repository.findAll();
        if (!list.contains(caseHistory)) {
            caseHistory = caseHistory_repository.save(caseHistory);
        }
    }

    @Test
    public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(
                get("/caseHistory/" + caseHistory.getCaseHistoryId() + "/").contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    public void FindById_IdIsNotValid_ReturnStatusNotFound() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(get("/caseHistory/999").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void CreateCaseHistory_CaseHistoryDoesNotExist_ReturnStatusOk() throws Exception {
        CreateCaseHistoryInput caseHistoryInput = createCaseHistoryInput();

        Cases cases = createCasesEntity();

        caseHistoryInput.setCaseId(Long.parseLong(cases.getCaseId().toString()));

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();

        String json = ow.writeValueAsString(caseHistoryInput);

        mvc
            .perform(post("/caseHistory").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk());
    }

    @Test
    public void CreateCaseHistory_casesDoesNotExists_ThrowEntityNotFoundException() throws Exception {
        CreateCaseHistoryInput caseHistory = createCaseHistoryInput();
        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();

        String json = ow.writeValueAsString(caseHistory);

        org.assertj.core.api.Assertions.assertThatThrownBy(
            () ->
                mvc
                    .perform(post("/caseHistory").contentType(MediaType.APPLICATION_JSON).content(json))
                    .andExpect(status().isNotFound())
        );
    }

    @Test
    public void DeleteCaseHistory_IdIsNotValid_ThrowEntityNotFoundException() {
        doReturn(null).when(caseHistoryAppService).findById(999L);
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(delete("/caseHistory/999").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("There does not exist a caseHistory with a id=999"));
    }

    @Test
    public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
        CaseHistory entity = createNewEntity();
        entity.setVersiono(0L);
        Cases cases = createCasesEntity();
        entity.setCases(cases);
        entity = caseHistory_repository.save(entity);

        FindCaseHistoryByIdOutput output = new FindCaseHistoryByIdOutput();
        output.setCaseHistoryId(entity.getCaseHistoryId());
        output.setTimestamp(entity.getTimestamp());

        Mockito.doReturn(output).when(caseHistoryAppService).findById(entity.getCaseHistoryId());

        //    Mockito.when(caseHistoryAppService.findById(entity.getCaseHistoryId())).thenReturn(output);

        mvc
            .perform(delete("/caseHistory/" + entity.getCaseHistoryId() + "/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    public void UpdateCaseHistory_CaseHistoryDoesNotExist_ReturnStatusNotFound() throws Exception {
        doReturn(null).when(caseHistoryAppService).findById(999L);

        UpdateCaseHistoryInput caseHistory = new UpdateCaseHistoryInput();
        caseHistory.setCaseHistoryId(999L);
        caseHistory.setMessage("999");
        caseHistory.setTimestamp(SearchUtils.stringToOffsetTime("01:15:23+07:00"));

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(caseHistory);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(put("/caseHistory/999").contentType(MediaType.APPLICATION_JSON).content(json))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Unable to update. CaseHistory with id=999 not found."));
    }

    @Test
    public void UpdateCaseHistory_CaseHistoryExists_ReturnStatusOk() throws Exception {
        CaseHistory entity = createUpdateEntity();
        entity.setVersiono(0L);

        Cases cases = createCasesEntity();
        entity.setCases(cases);
        entity = caseHistory_repository.save(entity);
        FindCaseHistoryByIdOutput output = new FindCaseHistoryByIdOutput();
        output.setCaseHistoryId(entity.getCaseHistoryId());
        output.setMessage(entity.getMessage());
        output.setTimestamp(entity.getTimestamp());
        output.setVersiono(entity.getVersiono());

        Mockito.when(caseHistoryAppService.findById(entity.getCaseHistoryId())).thenReturn(output);

        UpdateCaseHistoryInput caseHistoryInput = new UpdateCaseHistoryInput();
        caseHistoryInput.setCaseHistoryId(entity.getCaseHistoryId());
        caseHistoryInput.setTimestamp(entity.getTimestamp());

        caseHistoryInput.setCaseId(Long.parseLong(cases.getCaseId().toString()));

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(caseHistoryInput);

        mvc
            .perform(
                put("/caseHistory/" + entity.getCaseHistoryId() + "/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isOk());

        CaseHistory de = createUpdateEntity();
        de.setCaseHistoryId(entity.getCaseHistoryId());
        caseHistory_repository.delete(de);
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(
                get("/caseHistory?search=caseHistoryId[equals]=1&limit=10&offset=1")
                    .contentType(MediaType.APPLICATION_JSON)
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
                            get("/caseHistory?search=caseHistorycaseHistoryId[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property caseHistorycaseHistoryId not found!"));
    }

    @Test
    public void GetCases_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(get("/caseHistory/999/cases").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void GetCases_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
        mvc
            .perform(
                get("/caseHistory/" + caseHistory.getCaseHistoryId() + "/cases").contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }
}
