package com.fastcode.example.restcontrollers.core;

import static org.mockito.ArgumentMatchers.any;
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
import com.fastcode.example.application.core.authorization.users.dto.*;
import com.fastcode.example.application.core.authorization.userspermission.UserspermissionAppService;
import com.fastcode.example.application.core.authorization.userspermission.UserspermissionAppService;
import com.fastcode.example.application.core.authorization.usersrole.UsersroleAppService;
import com.fastcode.example.application.core.authorization.usersrole.UsersroleAppService;
import com.fastcode.example.application.core.person.PersonAppService;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.SearchUtils;
import com.fastcode.example.domain.core.*;
import com.fastcode.example.domain.core.authorization.users.IUsersRepository;
import com.fastcode.example.domain.core.authorization.users.IUsersRepository;
import com.fastcode.example.domain.core.authorization.users.Users;
import com.fastcode.example.domain.core.authorization.users.Users;
import com.fastcode.example.domain.core.authorization.userspreference.IUserspreferenceManager;
import com.fastcode.example.domain.core.authorization.userspreference.Userspreference;
import com.fastcode.example.domain.core.person.IPersonRepository;
import com.fastcode.example.domain.core.person.Person;
import com.fastcode.example.security.JWTAppService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.persistence.EntityExistsException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
public class UsersControllerTest extends DatabaseContainerConfig {

    @Autowired
    protected SortHandlerMethodArgumentResolver sortArgumentResolver;

    @Autowired
    @Qualifier("usersRepository")
    protected IUsersRepository users_repository;

    @Autowired
    @Qualifier("usersRepository")
    protected IUsersRepository usersRepository;

    @Autowired
    @Qualifier("personRepository")
    protected IPersonRepository personRepository;

    @SpyBean
    @Qualifier("usersAppService")
    protected UsersAppService usersAppService;

    @SpyBean
    @Qualifier("personAppService")
    protected PersonAppService personAppService;

    @SpyBean
    @Qualifier("userspermissionAppService")
    protected UserspermissionAppService userspermissionAppService;

    @SpyBean
    @Qualifier("usersroleAppService")
    protected UsersroleAppService usersroleAppService;

    @SpyBean
    protected IUserspreferenceManager userspreferenceManager;

    @SpyBean
    protected JWTAppService jwtAppService;

    @SpyBean
    protected PasswordEncoder pEncoder;

    @SpyBean
    protected LoggingHelper logHelper;

    @SpyBean
    protected Environment env;

    @Mock
    protected Logger loggerMock;

    protected Users users;

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
        em.createNativeQuery("truncate table public.users CASCADE").executeUpdate();
        em.createNativeQuery("truncate table public.users CASCADE").executeUpdate();
        em.createNativeQuery("truncate table public.person CASCADE").executeUpdate();
        em.createNativeQuery("truncate table public.userspreference CASCADE").executeUpdate();
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
        usersEntity.setEmail("bbc" + countUsers + "@d.c");
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

    public Users createEntity() {
        Users usersEntity = new Users();
        usersEntity.setDisplayName("1");
        usersEntity.setEmail("bbc@d.c");
        usersEntity.setIsActive(false);
        usersEntity.setIsEmailConfirmed(false);
        usersEntity.setPassword("1");
        usersEntity.setUsername("1");
        usersEntity.setVersiono(0L);

        return usersEntity;
    }

    public CreateUsersInput createUsersInput() {
        CreateUsersInput usersInput = new CreateUsersInput();
        usersInput.setDisplayName("5");
        usersInput.setEmail("pmk@d.c");
        usersInput.setIsActive(false);
        usersInput.setIsEmailConfirmed(false);
        usersInput.setPassword("5");
        usersInput.setUsername("5");

        return usersInput;
    }

    public Users createNewEntity() {
        Users users = new Users();
        users.setDisplayName("3");
        users.setEmail("bmc@d.c");
        users.setIsActive(false);
        users.setIsEmailConfirmed(false);
        users.setPassword("3");
        users.setUsername("3");

        return users;
    }

    public Users createUpdateEntity() {
        Users users = new Users();
        users.setDisplayName("4");
        users.setEmail("pmlp@d.c");
        users.setIsActive(false);
        users.setIsEmailConfirmed(false);
        users.setPassword("4");
        users.setUsername("4");

        return users;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        final UsersController usersController = new UsersController(
            usersAppService,
            personAppService,
            userspermissionAppService,
            usersroleAppService,
            pEncoder,
            jwtAppService,
            logHelper,
            env
        );
        when(logHelper.getLogger()).thenReturn(loggerMock);
        doNothing().when(loggerMock).error(anyString());

        this.mvc =
            MockMvcBuilders
                .standaloneSetup(usersController)
                .setCustomArgumentResolvers(sortArgumentResolver)
                .setControllerAdvice()
                .build();
    }

    @Before
    public void initTest() {
        users = createEntity();
        List<Users> list = users_repository.findAll();
        if (!list.contains(users)) {
            users = users_repository.save(users);
        }
    }

    @Test
    public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(get("/users/" + users.getUsername() + "/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void FindById_IdIsNotValid_ReturnStatusNotFound() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () -> mvc.perform(get("/users/999").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void CreateUsers_UsersDoesNotExist_ReturnStatusOk() throws Exception {
        Mockito.doReturn(null).when(usersAppService).findByUsername(anyString());

        CreateUsersInput users = createUsersInput();
        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(users);

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());

        users_repository.delete(createNewEntity());
    }

    @Test
    public void CreateUsers_UsersAlreadyExists_ThrowEntityExistsException() throws Exception {
        FindUsersByUsernameOutput output = new FindUsersByUsernameOutput();
        output.setEmail("bpc@g.c");
        output.setIsActive(false);
        output.setIsEmailConfirmed(false);
        output.setUsername("1");

        Mockito.doReturn(output).when(usersAppService).findByUsername(anyString());
        CreateUsersInput users = createUsersInput();
        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();

        String json = ow.writeValueAsString(users);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityExistsException("There already exists a users with Username =" + users.getUsername()));
    }

    @Test
    public void DeleteUsers_IdIsNotValid_ThrowEntityNotFoundException() {
        doReturn(null).when(usersAppService).findById("999");
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc.perform(delete("/users/999").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("There does not exist a users with a id=999"));
    }

    @Test
    public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
        Users entity = createNewEntity();
        entity.setVersiono(0L);
        entity = users_repository.save(entity);

        Userspreference userspreference = new Userspreference();
        userspreference.setUsername(entity.getUsername());
        userspreference.setUsers(entity);
        userspreference.setTheme("Abc");
        userspreference.setLanguage("abc");
        userspreference = userspreferenceManager.create(userspreference);

        FindUsersByIdOutput output = new FindUsersByIdOutput();
        output.setEmail(entity.getEmail());
        output.setIsActive(entity.getIsActive());
        output.setIsEmailConfirmed(entity.getIsEmailConfirmed());
        output.setUsername(entity.getUsername());

        Mockito.doReturn(output).when(usersAppService).findById(any(String.class));

        //   Mockito.when(usersAppService.findById(any(String.class))).thenReturn(output);

        mvc
            .perform(delete("/users/" + entity.getUsername() + "/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    public void UpdateUsers_UsersDoesNotExist_ReturnStatusNotFound() throws Exception {
        doReturn(null).when(usersAppService).findById("999");

        UpdateUsersInput users = new UpdateUsersInput();
        users.setDisplayName("999");
        users.setEmail("bmc@g.c");
        users.setIsActive(false);
        users.setIsEmailConfirmed(false);
        users.setUsername("999");

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(users);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(put("/users/999").contentType(MediaType.APPLICATION_JSON).content(json))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Unable to update. Users with id=999 not found."));
    }

    @Test
    public void UpdateUsers_UsersExists_ReturnStatusOk() throws Exception {
        Users entity = createUpdateEntity();
        entity.setVersiono(0L);

        entity = users_repository.save(entity);
        FindUsersWithAllFieldsByIdOutput output = new FindUsersWithAllFieldsByIdOutput();
        output.setDisplayName(entity.getDisplayName());
        output.setEmail(entity.getEmail());
        output.setIsActive(entity.getIsActive());
        output.setIsEmailConfirmed(entity.getIsEmailConfirmed());
        output.setPassword(entity.getPassword());
        output.setUsername(entity.getUsername());
        output.setVersiono(entity.getVersiono());

        Mockito.when(usersAppService.findWithAllFieldsById(entity.getUsername())).thenReturn(output);

        UpdateUsersInput usersInput = new UpdateUsersInput();
        usersInput.setEmail(entity.getEmail());
        usersInput.setIsActive(entity.getIsActive());
        usersInput.setIsEmailConfirmed(entity.getIsEmailConfirmed());
        usersInput.setPassword(entity.getPassword());
        usersInput.setUsername(entity.getUsername());

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(usersInput);

        mvc
            .perform(put("/users/" + entity.getUsername() + "/").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk());

        Users de = createUpdateEntity();
        de.setUsername(entity.getUsername());
        users_repository.delete(de);
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(get("/users?search=username[equals]=1&limit=10&offset=1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/users?search=usersusername[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property usersusername not found!"));
    }

    @Test
    public void GetPersons_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() {
        Map<String, String> joinCol = new HashMap<String, String>();
        joinCol.put("username", "1");

        Mockito.when(usersAppService.parsePersonsJoinColumn("username")).thenReturn(joinCol);
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/users/1/persons?search=abc[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property abc not found!"));
    }

    @Test
    public void GetPersons_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
        Map<String, String> joinCol = new HashMap<String, String>();
        joinCol.put("username", "1");

        Mockito.when(usersAppService.parsePersonsJoinColumn("username")).thenReturn(joinCol);
        mvc
            .perform(
                get("/users/1/persons?search=username[equals]=1&limit=10&offset=1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    public void GetPersons_searchIsNotEmpty() {
        Mockito.when(usersAppService.parsePersonsJoinColumn(anyString())).thenReturn(null);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/users/1/persons?search=username[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Invalid join column"));
    }

    @Test
    public void GetUserspermissions_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() {
        Map<String, String> joinCol = new HashMap<String, String>();
        joinCol.put("username", "1");

        Mockito.when(usersAppService.parseUserspermissionsJoinColumn("usersusername")).thenReturn(joinCol);
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/users/1/userspermissions?search=abc[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property abc not found!"));
    }

    @Test
    public void GetUserspermissions_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
        Map<String, String> joinCol = new HashMap<String, String>();
        joinCol.put("username", "1");

        Mockito.when(usersAppService.parseUserspermissionsJoinColumn("usersUsername")).thenReturn(joinCol);
        mvc
            .perform(
                get("/users/1/userspermissions?search=usersUsername[equals]=1&limit=10&offset=1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    public void GetUserspermissions_searchIsNotEmpty() {
        Mockito.when(usersAppService.parseUserspermissionsJoinColumn(anyString())).thenReturn(null);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/users/1/userspermissions?search=usersUsername[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Invalid join column"));
    }

    @Test
    public void GetUsersroles_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() {
        Map<String, String> joinCol = new HashMap<String, String>();
        joinCol.put("username", "1");

        Mockito.when(usersAppService.parseUsersrolesJoinColumn("usersusername")).thenReturn(joinCol);
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/users/1/usersroles?search=abc[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property abc not found!"));
    }

    @Test
    public void GetUsersroles_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
        Map<String, String> joinCol = new HashMap<String, String>();
        joinCol.put("username", "1");

        Mockito.when(usersAppService.parseUsersrolesJoinColumn("usersUsername")).thenReturn(joinCol);
        mvc
            .perform(
                get("/users/1/usersroles?search=usersUsername[equals]=1&limit=10&offset=1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    public void GetUsersroles_searchIsNotEmpty() {
        Mockito.when(usersAppService.parseUsersrolesJoinColumn(anyString())).thenReturn(null);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/users/1/usersroles?search=usersUsername[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Invalid join column"));
    }
}
