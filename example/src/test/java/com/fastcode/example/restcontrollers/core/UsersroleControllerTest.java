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
import com.fastcode.example.application.core.authorization.role.RoleAppService;
import com.fastcode.example.application.core.authorization.users.UsersAppService;
import com.fastcode.example.application.core.authorization.usersrole.UsersroleAppService;
import com.fastcode.example.application.core.authorization.usersrole.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.SearchUtils;
import com.fastcode.example.domain.core.*;
import com.fastcode.example.domain.core.authorization.role.IRoleRepository;
import com.fastcode.example.domain.core.authorization.role.Role;
import com.fastcode.example.domain.core.authorization.users.IUsersRepository;
import com.fastcode.example.domain.core.authorization.users.Users;
import com.fastcode.example.domain.core.authorization.usersrole.IUsersroleRepository;
import com.fastcode.example.domain.core.authorization.usersrole.Usersrole;
import com.fastcode.example.domain.core.authorization.usersrole.UsersroleId;
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
public class UsersroleControllerTest extends DatabaseContainerConfig {

    @Autowired
    protected SortHandlerMethodArgumentResolver sortArgumentResolver;

    @Autowired
    @Qualifier("usersroleRepository")
    protected IUsersroleRepository usersrole_repository;

    @Autowired
    @Qualifier("roleRepository")
    protected IRoleRepository roleRepository;

    @Autowired
    @Qualifier("usersRepository")
    protected IUsersRepository usersRepository;

    @Autowired
    @Qualifier("personRepository")
    protected IPersonRepository personRepository;

    @SpyBean
    @Qualifier("usersroleAppService")
    protected UsersroleAppService usersroleAppService;

    @SpyBean
    @Qualifier("roleAppService")
    protected RoleAppService roleAppService;

    @SpyBean
    @Qualifier("usersAppService")
    protected UsersAppService usersAppService;

    @SpyBean
    protected JWTAppService jwtAppService;

    @SpyBean
    protected LoggingHelper logHelper;

    @SpyBean
    protected Environment env;

    @Mock
    protected Logger loggerMock;

    protected Usersrole usersrole;

    protected MockMvc mvc;

    @Autowired
    EntityManagerFactory emf;

    static EntityManagerFactory emfs;

    static int relationCount = 10;
    static int yearCount = 1971;
    static int dayCount = 10;
    private BigDecimal bigdec = new BigDecimal(1.2);

    int countRole = 10;

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
        em.createNativeQuery("truncate table public.usersrole CASCADE").executeUpdate();
        em.createNativeQuery("truncate table public.role CASCADE").executeUpdate();
        em.createNativeQuery("truncate table public.users CASCADE").executeUpdate();
        em.createNativeQuery("truncate table public.person CASCADE").executeUpdate();
        em.getTransaction().commit();
    }

    public Role createRoleEntity() {
        if (countRole > 60) {
            countRole = 10;
        }

        if (dayCount >= 31) {
            dayCount = 10;
            yearCount = yearCount++;
        }

        Role roleEntity = new Role();

        roleEntity.setDisplayName(String.valueOf(relationCount));
        roleEntity.setId(Long.valueOf(relationCount));
        roleEntity.setName(String.valueOf(relationCount));
        roleEntity.setVersiono(0L);
        relationCount++;
        if (!roleRepository.findAll().contains(roleEntity)) {
            roleEntity = roleRepository.save(roleEntity);
        }
        countRole++;
        return roleEntity;
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

    public Usersrole createEntity() {
        Role role = createRoleEntity();
        Users users = createUsersEntity();

        Usersrole usersroleEntity = new Usersrole();
        usersroleEntity.setRoleId(1L);
        usersroleEntity.setUsersUsername("1");
        usersroleEntity.setVersiono(0L);
        usersroleEntity.setRole(role);
        usersroleEntity.setRoleId(Long.parseLong(role.getId().toString()));
        usersroleEntity.setUsers(users);
        usersroleEntity.setUsersUsername(users.getUsername());

        return usersroleEntity;
    }

    public CreateUsersroleInput createUsersroleInput() {
        CreateUsersroleInput usersroleInput = new CreateUsersroleInput();
        usersroleInput.setRoleId(5L);
        usersroleInput.setUsersUsername("5");

        return usersroleInput;
    }

    public Usersrole createNewEntity() {
        Usersrole usersrole = new Usersrole();
        usersrole.setRoleId(3L);
        usersrole.setUsersUsername("3");

        return usersrole;
    }

    public Usersrole createUpdateEntity() {
        Usersrole usersrole = new Usersrole();
        usersrole.setRoleId(4L);
        usersrole.setUsersUsername("4");

        return usersrole;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        final UsersroleController usersroleController = new UsersroleController(
            usersroleAppService,
            roleAppService,
            usersAppService,
            jwtAppService,
            logHelper,
            env
        );
        when(logHelper.getLogger()).thenReturn(loggerMock);
        doNothing().when(loggerMock).error(anyString());

        this.mvc =
            MockMvcBuilders
                .standaloneSetup(usersroleController)
                .setCustomArgumentResolvers(sortArgumentResolver)
                .setControllerAdvice()
                .build();
    }

    @Before
    public void initTest() {
        usersrole = createEntity();
        List<Usersrole> list = usersrole_repository.findAll();
        if (!list.contains(usersrole)) {
            usersrole = usersrole_repository.save(usersrole);
        }
    }

    @Test
    public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(
                get(
                    "/usersrole/roleId=" +
                    usersrole.getRoleId() +
                    ",usersUsername=" +
                    usersrole.getUsersUsername() +
                    "/"
                )
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
                        .perform(get("/usersrole/roleId=999,usersUsername=999").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void CreateUsersrole_UsersroleDoesNotExist_ReturnStatusOk() throws Exception {
        CreateUsersroleInput usersroleInput = createUsersroleInput();

        Role role = createRoleEntity();

        usersroleInput.setRoleId(Long.parseLong(role.getId().toString()));

        Users users = createUsersEntity();

        usersroleInput.setUsersUsername(users.getUsername());
        doNothing().when(jwtAppService).deleteAllUserTokens(any(String.class));

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();

        String json = ow.writeValueAsString(usersroleInput);

        mvc
            .perform(post("/usersrole").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk());
    }

    @Test
    public void CreateUsersrole_roleDoesNotExists_ThrowEntityNotFoundException() throws Exception {
        CreateUsersroleInput usersrole = createUsersroleInput();
        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();

        String json = ow.writeValueAsString(usersrole);

        org.assertj.core.api.Assertions.assertThatThrownBy(
            () ->
                mvc
                    .perform(post("/usersrole").contentType(MediaType.APPLICATION_JSON).content(json))
                    .andExpect(status().isNotFound())
        );
    }

    @Test
    public void CreateUsersrole_usersDoesNotExists_ThrowEntityNotFoundException() throws Exception {
        CreateUsersroleInput usersrole = createUsersroleInput();
        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();

        String json = ow.writeValueAsString(usersrole);

        org.assertj.core.api.Assertions.assertThatThrownBy(
            () ->
                mvc
                    .perform(post("/usersrole").contentType(MediaType.APPLICATION_JSON).content(json))
                    .andExpect(status().isNotFound())
        );
    }

    @Test
    public void DeleteUsersrole_IdIsNotValid_ThrowEntityNotFoundException() {
        doReturn(null).when(usersroleAppService).findById(new UsersroleId(999L, "999"));
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            delete("/usersrole/roleId=999,usersUsername=999").contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(
                new EntityNotFoundException("There does not exist a usersrole with a id=roleId=999,usersUsername=999")
            );
    }

    @Test
    public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
        Usersrole entity = createNewEntity();
        entity.setVersiono(0L);
        Role role = createRoleEntity();
        entity.setRoleId(Long.parseLong(role.getId().toString()));
        entity.setRole(role);
        Users users = createUsersEntity();
        entity.setUsersUsername(users.getUsername());
        entity.setUsers(users);
        entity = usersrole_repository.save(entity);

        FindUsersroleByIdOutput output = new FindUsersroleByIdOutput();
        output.setRoleId(entity.getRoleId());
        output.setUsersUsername(entity.getUsersUsername());

        //    Mockito.when(usersroleAppService.findById(new UsersroleId(entity.getRoleId(), entity.getUsersUsername()))).thenReturn(output);
        Mockito
            .doReturn(output)
            .when(usersroleAppService)
            .findById(new UsersroleId(entity.getRoleId(), entity.getUsersUsername()));

        doNothing().when(jwtAppService).deleteAllUserTokens(any(String.class));
        mvc
            .perform(
                delete("/usersrole/roleId=" + entity.getRoleId() + ",usersUsername=" + entity.getUsersUsername() + "/")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());
    }

    @Test
    public void UpdateUsersrole_UsersroleDoesNotExist_ReturnStatusNotFound() throws Exception {
        doReturn(null).when(usersroleAppService).findById(new UsersroleId(999L, "999"));

        UpdateUsersroleInput usersrole = new UpdateUsersroleInput();
        usersrole.setRoleId(999L);
        usersrole.setUsersUsername("999");

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(usersrole);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            put("/usersrole/roleId=999,usersUsername=999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(
                new EntityNotFoundException(
                    "Unable to update. Usersrole with id=roleId=999,usersUsername=999 not found."
                )
            );
    }

    @Test
    public void UpdateUsersrole_UsersroleExists_ReturnStatusOk() throws Exception {
        Usersrole entity = createUpdateEntity();
        entity.setVersiono(0L);

        Role role = createRoleEntity();
        entity.setRoleId(Long.parseLong(role.getId().toString()));
        entity.setRole(role);
        Users users = createUsersEntity();
        entity.setUsersUsername(users.getUsername());
        entity.setUsers(users);
        entity = usersrole_repository.save(entity);
        FindUsersroleByIdOutput output = new FindUsersroleByIdOutput();
        output.setRoleId(entity.getRoleId());
        output.setUsersUsername(entity.getUsersUsername());
        output.setVersiono(entity.getVersiono());

        Mockito
            .when(usersroleAppService.findById(new UsersroleId(entity.getRoleId(), entity.getUsersUsername())))
            .thenReturn(output);

        doNothing().when(jwtAppService).deleteAllUserTokens(any(String.class));

        UpdateUsersroleInput usersroleInput = new UpdateUsersroleInput();
        usersroleInput.setRoleId(entity.getRoleId());
        usersroleInput.setUsersUsername(entity.getUsersUsername());

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(usersroleInput);

        mvc
            .perform(
                put("/usersrole/roleId=" + entity.getRoleId() + ",usersUsername=" + entity.getUsersUsername() + "/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isOk());

        Usersrole de = createUpdateEntity();
        de.setRoleId(entity.getRoleId());
        de.setUsersUsername(entity.getUsersUsername());
        usersrole_repository.delete(de);
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(
                get("/usersrole?search=roleId[equals]=1&limit=10&offset=1").contentType(MediaType.APPLICATION_JSON)
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
                            get("/usersrole?search=usersroleroleId[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property usersroleroleId not found!"));
    }

    @Test
    public void GetRole_IdIsNotEmptyAndIdIsNotValid_ThrowException() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(get("/usersrole/roleId999/role").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Invalid id=roleId999"));
    }

    @Test
    public void GetRole_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/usersrole/roleId=999,usersUsername=999/role").contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void GetRole_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
        mvc
            .perform(
                get(
                    "/usersrole/roleId=" +
                    usersrole.getRoleId() +
                    ",usersUsername=" +
                    usersrole.getUsersUsername() +
                    "/role"
                )
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    public void GetUsers_IdIsNotEmptyAndIdIsNotValid_ThrowException() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(get("/usersrole/roleId999/users").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Invalid id=roleId999"));
    }

    @Test
    public void GetUsers_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/usersrole/roleId=999,usersUsername=999/users").contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void GetUsers_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
        mvc
            .perform(
                get(
                    "/usersrole/roleId=" +
                    usersrole.getRoleId() +
                    ",usersUsername=" +
                    usersrole.getUsersUsername() +
                    "/users"
                )
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }
}
