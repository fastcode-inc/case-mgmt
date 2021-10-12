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
import com.fastcode.example.application.core.authorization.users.UsersAppService;
import com.fastcode.example.application.core.person.PersonAppService;
import com.fastcode.example.application.core.person.dto.*;
import com.fastcode.example.application.core.personcase.PersonCaseAppService;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.SearchUtils;
import com.fastcode.example.domain.core.*;
import com.fastcode.example.domain.core.authorization.users.IUsersRepository;
import com.fastcode.example.domain.core.authorization.users.Users;
import com.fastcode.example.domain.core.person.IPersonRepository;
import com.fastcode.example.domain.core.person.IPersonRepository;
import com.fastcode.example.domain.core.person.Person;
import com.fastcode.example.domain.core.person.Person;
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
public class PersonControllerTest extends DatabaseContainerConfig {

    @Autowired
    protected SortHandlerMethodArgumentResolver sortArgumentResolver;

    @Autowired
    @Qualifier("personRepository")
    protected IPersonRepository person_repository;

    @Autowired
    @Qualifier("usersRepository")
    protected IUsersRepository usersRepository;

    @Autowired
    @Qualifier("personRepository")
    protected IPersonRepository personRepository;

    @SpyBean
    @Qualifier("personAppService")
    protected PersonAppService personAppService;

    @SpyBean
    @Qualifier("personCaseAppService")
    protected PersonCaseAppService personCaseAppService;

    @SpyBean
    @Qualifier("usersAppService")
    protected UsersAppService usersAppService;

    @SpyBean
    protected LoggingHelper logHelper;

    @SpyBean
    protected Environment env;

    @Mock
    protected Logger loggerMock;

    protected Person person;

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

    @PostConstruct
    public void init() {
        emfs = emf;
    }

    @AfterClass
    public static void cleanup() {
        EntityManager em = emfs.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("truncate table public.person CASCADE").executeUpdate();
        em.createNativeQuery("truncate table public.users CASCADE").executeUpdate();
        em.createNativeQuery("truncate table public.person CASCADE").executeUpdate();
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

    public Person createEntity() {
        Users users = createUsersEntity();

        Person personEntity = new Person();
        personEntity.setBirthDate(SearchUtils.stringToLocalDate("1996-09-01"));
        personEntity.setComments("1");
        personEntity.setGivenName("1");
        personEntity.setHomePhone("1");
        personEntity.setPersonId(1L);
        personEntity.setSurname("1");
        personEntity.setVersiono(0L);
        personEntity.setUsers(users);

        return personEntity;
    }

    public CreatePersonInput createPersonInput() {
        CreatePersonInput personInput = new CreatePersonInput();
        personInput.setBirthDate(SearchUtils.stringToLocalDate("1996-08-10"));
        personInput.setComments("5");
        personInput.setGivenName("5");
        personInput.setHomePhone("5");
        personInput.setPersonId(5L);
        personInput.setSurname("5");

        return personInput;
    }

    public Person createNewEntity() {
        Person person = new Person();
        person.setBirthDate(SearchUtils.stringToLocalDate("1996-08-11"));
        person.setComments("3");
        person.setGivenName("3");
        person.setHomePhone("3");
        person.setPersonId(3L);
        person.setSurname("3");

        return person;
    }

    public Person createUpdateEntity() {
        Person person = new Person();
        person.setBirthDate(SearchUtils.stringToLocalDate("1996-09-09"));
        person.setComments("4");
        person.setGivenName("4");
        person.setHomePhone("4");
        person.setPersonId(4L);
        person.setSurname("4");

        return person;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        final PersonController personController = new PersonController(
            personAppService,
            personCaseAppService,
            usersAppService,
            logHelper,
            env
        );
        when(logHelper.getLogger()).thenReturn(loggerMock);
        doNothing().when(loggerMock).error(anyString());

        this.mvc =
            MockMvcBuilders
                .standaloneSetup(personController)
                .setCustomArgumentResolvers(sortArgumentResolver)
                .setControllerAdvice()
                .build();
    }

    @Before
    public void initTest() {
        person = createEntity();
        List<Person> list = person_repository.findAll();
        if (!list.contains(person)) {
            person = person_repository.save(person);
        }
    }

    @Test
    public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(get("/person/" + person.getPersonId() + "/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void FindById_IdIsNotValid_ReturnStatusNotFound() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () -> mvc.perform(get("/person/999").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void CreatePerson_PersonDoesNotExist_ReturnStatusOk() throws Exception {
        CreatePersonInput personInput = createPersonInput();

        Users users = createUsersEntity();

        personInput.setUsername(users.getUsername());

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();

        String json = ow.writeValueAsString(personInput);

        mvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
    }

    @Test
    public void DeletePerson_IdIsNotValid_ThrowEntityNotFoundException() {
        doReturn(null).when(personAppService).findById(999L);
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(delete("/person/999").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("There does not exist a person with a id=999"));
    }

    @Test
    public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
        Person entity = createNewEntity();
        entity.setVersiono(0L);
        Users users = createUsersEntity();
        entity.setUsers(users);
        entity = person_repository.save(entity);

        FindPersonByIdOutput output = new FindPersonByIdOutput();
        output.setPersonId(entity.getPersonId());

        Mockito.doReturn(output).when(personAppService).findById(entity.getPersonId());

        //    Mockito.when(personAppService.findById(entity.getPersonId())).thenReturn(output);

        mvc
            .perform(delete("/person/" + entity.getPersonId() + "/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    public void UpdatePerson_PersonDoesNotExist_ReturnStatusNotFound() throws Exception {
        doReturn(null).when(personAppService).findById(999L);

        UpdatePersonInput person = new UpdatePersonInput();
        person.setBirthDate(SearchUtils.stringToLocalDate("1996-09-28"));
        person.setComments("999");
        person.setGivenName("999");
        person.setHomePhone("999");
        person.setPersonId(999L);
        person.setSurname("999");

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(person);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(put("/person/999").contentType(MediaType.APPLICATION_JSON).content(json))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Unable to update. Person with id=999 not found."));
    }

    @Test
    public void UpdatePerson_PersonExists_ReturnStatusOk() throws Exception {
        Person entity = createUpdateEntity();
        entity.setVersiono(0L);

        Users users = createUsersEntity();
        entity.setUsers(users);
        entity = person_repository.save(entity);
        FindPersonByIdOutput output = new FindPersonByIdOutput();
        output.setBirthDate(entity.getBirthDate());
        output.setComments(entity.getComments());
        output.setGivenName(entity.getGivenName());
        output.setHomePhone(entity.getHomePhone());
        output.setPersonId(entity.getPersonId());
        output.setSurname(entity.getSurname());
        output.setVersiono(entity.getVersiono());

        Mockito.when(personAppService.findById(entity.getPersonId())).thenReturn(output);

        UpdatePersonInput personInput = new UpdatePersonInput();
        personInput.setPersonId(entity.getPersonId());

        personInput.setUsername(users.getUsername());

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(personInput);

        mvc
            .perform(put("/person/" + entity.getPersonId() + "/").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk());

        Person de = createUpdateEntity();
        de.setPersonId(entity.getPersonId());
        person_repository.delete(de);
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(get("/person?search=personId[equals]=1&limit=10&offset=1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/person?search=personpersonId[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property personpersonId not found!"));
    }

    @Test
    public void GetPersonCases_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() {
        Map<String, String> joinCol = new HashMap<String, String>();
        joinCol.put("personId", "1");

        Mockito.when(personAppService.parsePersonCasesJoinColumn("personid")).thenReturn(joinCol);
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/person/1/personCases?search=abc[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property abc not found!"));
    }

    @Test
    public void GetPersonCases_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
        Map<String, String> joinCol = new HashMap<String, String>();
        joinCol.put("personId", "1");

        Mockito.when(personAppService.parsePersonCasesJoinColumn("personId")).thenReturn(joinCol);
        mvc
            .perform(
                get("/person/1/personCases?search=personId[equals]=1&limit=10&offset=1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    public void GetPersonCases_searchIsNotEmpty() {
        Mockito.when(personAppService.parsePersonCasesJoinColumn(anyString())).thenReturn(null);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/person/1/personCases?search=personId[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Invalid join column"));
    }

    @Test
    public void GetUsers_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(get("/person/999/users").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void GetUsers_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
        mvc
            .perform(get("/person/" + person.getPersonId() + "/users").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
