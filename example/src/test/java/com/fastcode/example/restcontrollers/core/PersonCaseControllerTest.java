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
import com.fastcode.example.application.core.cases.CasesAppService;
import com.fastcode.example.application.core.person.PersonAppService;
import com.fastcode.example.application.core.personcase.PersonCaseAppService;
import com.fastcode.example.application.core.personcase.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.SearchUtils;
import com.fastcode.example.domain.core.*;
import com.fastcode.example.domain.core.authorization.users.IUsersRepository;
import com.fastcode.example.domain.core.authorization.users.Users;
import com.fastcode.example.domain.core.cases.Cases;
import com.fastcode.example.domain.core.cases.ICasesRepository;
import com.fastcode.example.domain.core.person.IPersonRepository;
import com.fastcode.example.domain.core.person.Person;
import com.fastcode.example.domain.core.personcase.IPersonCaseRepository;
import com.fastcode.example.domain.core.personcase.PersonCase;
import com.fastcode.example.domain.core.personcase.PersonCaseId;
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
public class PersonCaseControllerTest extends DatabaseContainerConfig {

    @Autowired
    protected SortHandlerMethodArgumentResolver sortArgumentResolver;

    @Autowired
    @Qualifier("personCaseRepository")
    protected IPersonCaseRepository personCase_repository;

    @Autowired
    @Qualifier("casesRepository")
    protected ICasesRepository casesRepository;

    @Autowired
    @Qualifier("personRepository")
    protected IPersonRepository personRepository;

    @Autowired
    @Qualifier("usersRepository")
    protected IUsersRepository usersRepository;

    @SpyBean
    @Qualifier("personCaseAppService")
    protected PersonCaseAppService personCaseAppService;

    @SpyBean
    @Qualifier("casesAppService")
    protected CasesAppService casesAppService;

    @SpyBean
    @Qualifier("personAppService")
    protected PersonAppService personAppService;

    @SpyBean
    protected LoggingHelper logHelper;

    @SpyBean
    protected Environment env;

    @Mock
    protected Logger loggerMock;

    protected PersonCase personCase;

    protected MockMvc mvc;

    @Autowired
    EntityManagerFactory emf;

    static EntityManagerFactory emfs;

    static int relationCount = 10;
    static int yearCount = 1971;
    static int dayCount = 10;
    private BigDecimal bigdec = new BigDecimal(1.2);

    int countUsers = 10;

    int countPerson = 10;

    int countCases = 10;

    @PostConstruct
    public void init() {
        emfs = emf;
    }

    @AfterClass
    public static void cleanup() {
        EntityManager em = emfs.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("truncate table public.person_case CASCADE").executeUpdate();
        em.createNativeQuery("truncate table public.cases CASCADE").executeUpdate();
        em.createNativeQuery("truncate table public.person CASCADE").executeUpdate();
        em.createNativeQuery("truncate table public.users CASCADE").executeUpdate();
        em.getTransaction().commit();
    }

    public Users createUsersEntity() {
        if (countUsers > 60) {
            countUsers = 10;
        }

        if (dayCount >= 31) {
            dayCount = 10;
            yearCount = yearCount++;
        }

        Users usersEntity = new Users();

        usersEntity.setDisplayName(String.valueOf(relationCount));
        usersEntity.setEmail(String.valueOf(relationCount));
        usersEntity.setIsActive(false);
        usersEntity.setIsEmailConfirmed(false);
        usersEntity.setPassword(String.valueOf(relationCount));
        usersEntity.setUsername(String.valueOf(relationCount));
        usersEntity.setVersiono(0L);
        relationCount++;
        if (!usersRepository.findAll().contains(usersEntity)) {
            usersEntity = usersRepository.save(usersEntity);
        }
        countUsers++;
        return usersEntity;
    }

    public Person createPersonEntity() {
        if (countPerson > 60) {
            countPerson = 10;
        }

        if (dayCount >= 31) {
            dayCount = 10;
            yearCount = yearCount++;
        }

        Person personEntity = new Person();

        personEntity.setBirthDate(SearchUtils.stringToLocalDate(yearCount + "-09-" + dayCount));
        personEntity.setComments(String.valueOf(relationCount));
        personEntity.setGivenName(String.valueOf(relationCount));
        personEntity.setHomePhone(String.valueOf(relationCount));
        personEntity.setPersonId(Long.valueOf(relationCount));
        personEntity.setSurname(String.valueOf(relationCount));
        personEntity.setVersiono(0L);
        relationCount++;
        Users users = createUsersEntity();
        personEntity.setUsers(users);
        if (!personRepository.findAll().contains(personEntity)) {
            personEntity = personRepository.save(personEntity);
        }
        countPerson++;
        return personEntity;
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

    public PersonCase createEntity() {
        Cases cases = createCasesEntity();
        Person person = createPersonEntity();

        PersonCase personCaseEntity = new PersonCase();
        personCaseEntity.setCaseId(1L);
        personCaseEntity.setPersonId(1L);
        personCaseEntity.setVersiono(0L);
        personCaseEntity.setCases(cases);
        personCaseEntity.setCaseId(Long.parseLong(cases.getCaseId().toString()));
        personCaseEntity.setPerson(person);
        personCaseEntity.setPersonId(Long.parseLong(person.getPersonId().toString()));

        return personCaseEntity;
    }

    public CreatePersonCaseInput createPersonCaseInput() {
        CreatePersonCaseInput personCaseInput = new CreatePersonCaseInput();
        personCaseInput.setCaseId(5L);
        personCaseInput.setPersonId(5L);

        return personCaseInput;
    }

    public PersonCase createNewEntity() {
        PersonCase personCase = new PersonCase();
        personCase.setCaseId(3L);
        personCase.setPersonId(3L);

        return personCase;
    }

    public PersonCase createUpdateEntity() {
        PersonCase personCase = new PersonCase();
        personCase.setCaseId(4L);
        personCase.setPersonId(4L);

        return personCase;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        final PersonCaseController personCaseController = new PersonCaseController(
            personCaseAppService,
            casesAppService,
            personAppService,
            logHelper,
            env
        );
        when(logHelper.getLogger()).thenReturn(loggerMock);
        doNothing().when(loggerMock).error(anyString());

        this.mvc =
            MockMvcBuilders
                .standaloneSetup(personCaseController)
                .setCustomArgumentResolvers(sortArgumentResolver)
                .setControllerAdvice()
                .build();
    }

    @Before
    public void initTest() {
        personCase = createEntity();
        List<PersonCase> list = personCase_repository.findAll();
        if (!list.contains(personCase)) {
            personCase = personCase_repository.save(personCase);
        }
    }

    @Test
    public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(
                get("/personCase/caseId=" + personCase.getCaseId() + ",personId=" + personCase.getPersonId() + "/")
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
                        .perform(get("/personCase/caseId=999,personId=999").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void CreatePersonCase_PersonCaseDoesNotExist_ReturnStatusOk() throws Exception {
        CreatePersonCaseInput personCaseInput = createPersonCaseInput();

        Cases cases = createCasesEntity();

        personCaseInput.setCaseId(Long.parseLong(cases.getCaseId().toString()));

        Person person = createPersonEntity();

        personCaseInput.setPersonId(Long.parseLong(person.getPersonId().toString()));

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();

        String json = ow.writeValueAsString(personCaseInput);

        mvc
            .perform(post("/personCase").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk());
    }

    @Test
    public void DeletePersonCase_IdIsNotValid_ThrowEntityNotFoundException() {
        doReturn(null).when(personCaseAppService).findById(new PersonCaseId(999L, 999L));
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(delete("/personCase/caseId=999,personId=999").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(
                new EntityNotFoundException("There does not exist a personCase with a id=caseId=999,personId=999")
            );
    }

    @Test
    public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
        PersonCase entity = createNewEntity();
        entity.setVersiono(0L);
        Cases cases = createCasesEntity();
        entity.setCaseId(Long.parseLong(cases.getCaseId().toString()));
        entity.setCases(cases);
        Person person = createPersonEntity();
        entity.setPersonId(Long.parseLong(person.getPersonId().toString()));
        entity.setPerson(person);
        entity = personCase_repository.save(entity);

        FindPersonCaseByIdOutput output = new FindPersonCaseByIdOutput();
        output.setCaseId(entity.getCaseId());
        output.setPersonId(entity.getPersonId());

        //    Mockito.when(personCaseAppService.findById(new PersonCaseId(entity.getCaseId(), entity.getPersonId()))).thenReturn(output);
        Mockito
            .doReturn(output)
            .when(personCaseAppService)
            .findById(new PersonCaseId(entity.getCaseId(), entity.getPersonId()));

        mvc
            .perform(
                delete("/personCase/caseId=" + entity.getCaseId() + ",personId=" + entity.getPersonId() + "/")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());
    }

    @Test
    public void UpdatePersonCase_PersonCaseDoesNotExist_ReturnStatusNotFound() throws Exception {
        doReturn(null).when(personCaseAppService).findById(new PersonCaseId(999L, 999L));

        UpdatePersonCaseInput personCase = new UpdatePersonCaseInput();
        personCase.setCaseId(999L);
        personCase.setPersonId(999L);

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(personCase);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            put("/personCase/caseId=999,personId=999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(
                new EntityNotFoundException("Unable to update. PersonCase with id=caseId=999,personId=999 not found.")
            );
    }

    @Test
    public void UpdatePersonCase_PersonCaseExists_ReturnStatusOk() throws Exception {
        PersonCase entity = createUpdateEntity();
        entity.setVersiono(0L);

        Cases cases = createCasesEntity();
        entity.setCaseId(Long.parseLong(cases.getCaseId().toString()));
        entity.setCases(cases);
        Person person = createPersonEntity();
        entity.setPersonId(Long.parseLong(person.getPersonId().toString()));
        entity.setPerson(person);
        entity = personCase_repository.save(entity);
        FindPersonCaseByIdOutput output = new FindPersonCaseByIdOutput();
        output.setCaseId(entity.getCaseId());
        output.setPersonId(entity.getPersonId());
        output.setVersiono(entity.getVersiono());

        Mockito
            .when(personCaseAppService.findById(new PersonCaseId(entity.getCaseId(), entity.getPersonId())))
            .thenReturn(output);

        UpdatePersonCaseInput personCaseInput = new UpdatePersonCaseInput();
        personCaseInput.setCaseId(entity.getCaseId());
        personCaseInput.setPersonId(entity.getPersonId());

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(personCaseInput);

        mvc
            .perform(
                put("/personCase/caseId=" + entity.getCaseId() + ",personId=" + entity.getPersonId() + "/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isOk());

        PersonCase de = createUpdateEntity();
        de.setCaseId(entity.getCaseId());
        de.setPersonId(entity.getPersonId());
        personCase_repository.delete(de);
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(
                get("/personCase?search=caseId[equals]=1&limit=10&offset=1").contentType(MediaType.APPLICATION_JSON)
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
                            get("/personCase?search=personCasecaseId[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property personCasecaseId not found!"));
    }

    @Test
    public void GetCases_IdIsNotEmptyAndIdIsNotValid_ThrowException() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(get("/personCase/caseId999/cases").contentType(MediaType.APPLICATION_JSON))
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
                            get("/personCase/caseId=999,personId=999/cases").contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void GetCases_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
        mvc
            .perform(
                get("/personCase/caseId=" + personCase.getCaseId() + ",personId=" + personCase.getPersonId() + "/cases")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    public void GetPerson_IdIsNotEmptyAndIdIsNotValid_ThrowException() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(get("/personCase/caseId999/person").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Invalid id=caseId999"));
    }

    @Test
    public void GetPerson_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/personCase/caseId=999,personId=999/person").contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void GetPerson_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
        mvc
            .perform(
                get(
                    "/personCase/caseId=" + personCase.getCaseId() + ",personId=" + personCase.getPersonId() + "/person"
                )
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }
}
