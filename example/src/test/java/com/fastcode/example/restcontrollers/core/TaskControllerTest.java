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
import com.fastcode.example.application.core.task.TaskAppService;
import com.fastcode.example.application.core.task.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.domain.core.*;
import com.fastcode.example.domain.core.cases.Cases;
import com.fastcode.example.domain.core.cases.ICasesRepository;
import com.fastcode.example.domain.core.task.ITaskRepository;
import com.fastcode.example.domain.core.task.Task;
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
public class TaskControllerTest extends DatabaseContainerConfig {

    @Autowired
    protected SortHandlerMethodArgumentResolver sortArgumentResolver;

    @Autowired
    @Qualifier("taskRepository")
    protected ITaskRepository task_repository;

    @Autowired
    @Qualifier("casesRepository")
    protected ICasesRepository casesRepository;

    @SpyBean
    @Qualifier("taskAppService")
    protected TaskAppService taskAppService;

    @SpyBean
    @Qualifier("casesAppService")
    protected CasesAppService casesAppService;

    @SpyBean
    protected LoggingHelper logHelper;

    @SpyBean
    protected Environment env;

    @Mock
    protected Logger loggerMock;

    protected Task task;

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
        em.createNativeQuery("truncate table public.task CASCADE").executeUpdate();
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

    public Task createEntity() {
        Cases cases = createCasesEntity();

        Task taskEntity = new Task();
        taskEntity.setMessage("1");
        taskEntity.setStatus("1");
        taskEntity.setTaskId(1L);
        taskEntity.setType("1");
        taskEntity.setUsername("1");
        taskEntity.setVersiono(0L);
        taskEntity.setCases(cases);

        return taskEntity;
    }

    public CreateTaskInput createTaskInput() {
        CreateTaskInput taskInput = new CreateTaskInput();
        taskInput.setMessage("5");
        taskInput.setStatus("5");
        taskInput.setTaskId(5L);
        taskInput.setType("5");
        taskInput.setUsername("5");

        return taskInput;
    }

    public Task createNewEntity() {
        Task task = new Task();
        task.setMessage("3");
        task.setStatus("3");
        task.setTaskId(3L);
        task.setType("3");
        task.setUsername("3");

        return task;
    }

    public Task createUpdateEntity() {
        Task task = new Task();
        task.setMessage("4");
        task.setStatus("4");
        task.setTaskId(4L);
        task.setType("4");
        task.setUsername("4");

        return task;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        final TaskController taskController = new TaskController(taskAppService, casesAppService, logHelper, env);
        when(logHelper.getLogger()).thenReturn(loggerMock);
        doNothing().when(loggerMock).error(anyString());

        this.mvc =
            MockMvcBuilders
                .standaloneSetup(taskController)
                .setCustomArgumentResolvers(sortArgumentResolver)
                .setControllerAdvice()
                .build();
    }

    @Before
    public void initTest() {
        task = createEntity();
        List<Task> list = task_repository.findAll();
        if (!list.contains(task)) {
            task = task_repository.save(task);
        }
    }

    @Test
    public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(get("/task/" + task.getTaskId() + "/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void FindById_IdIsNotValid_ReturnStatusNotFound() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () -> mvc.perform(get("/task/999").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void CreateTask_TaskDoesNotExist_ReturnStatusOk() throws Exception {
        CreateTaskInput taskInput = createTaskInput();

        Cases cases = createCasesEntity();

        taskInput.setCaseId(Long.parseLong(cases.getCaseId().toString()));

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();

        String json = ow.writeValueAsString(taskInput);

        mvc.perform(post("/task").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
    }

    @Test
    public void DeleteTask_IdIsNotValid_ThrowEntityNotFoundException() {
        doReturn(null).when(taskAppService).findById(999L);
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc.perform(delete("/task/999").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("There does not exist a task with a id=999"));
    }

    @Test
    public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
        Task entity = createNewEntity();
        entity.setVersiono(0L);
        Cases cases = createCasesEntity();
        entity.setCases(cases);
        entity = task_repository.save(entity);

        FindTaskByIdOutput output = new FindTaskByIdOutput();
        output.setTaskId(entity.getTaskId());

        Mockito.doReturn(output).when(taskAppService).findById(entity.getTaskId());

        //    Mockito.when(taskAppService.findById(entity.getTaskId())).thenReturn(output);

        mvc
            .perform(delete("/task/" + entity.getTaskId() + "/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    public void UpdateTask_TaskDoesNotExist_ReturnStatusNotFound() throws Exception {
        doReturn(null).when(taskAppService).findById(999L);

        UpdateTaskInput task = new UpdateTaskInput();
        task.setMessage("999");
        task.setStatus("999");
        task.setTaskId(999L);
        task.setType("999");
        task.setUsername("999");

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(task);

        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(put("/task/999").contentType(MediaType.APPLICATION_JSON).content(json))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Unable to update. Task with id=999 not found."));
    }

    @Test
    public void UpdateTask_TaskExists_ReturnStatusOk() throws Exception {
        Task entity = createUpdateEntity();
        entity.setVersiono(0L);

        Cases cases = createCasesEntity();
        entity.setCases(cases);
        entity = task_repository.save(entity);
        FindTaskByIdOutput output = new FindTaskByIdOutput();
        output.setMessage(entity.getMessage());
        output.setStatus(entity.getStatus());
        output.setTaskId(entity.getTaskId());
        output.setType(entity.getType());
        output.setUsername(entity.getUsername());
        output.setVersiono(entity.getVersiono());

        Mockito.when(taskAppService.findById(entity.getTaskId())).thenReturn(output);

        UpdateTaskInput taskInput = new UpdateTaskInput();
        taskInput.setTaskId(entity.getTaskId());

        taskInput.setCaseId(Long.parseLong(cases.getCaseId().toString()));

        ObjectWriter ow = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .writer()
            .withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(taskInput);

        mvc
            .perform(put("/task/" + entity.getTaskId() + "/").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isOk());

        Task de = createUpdateEntity();
        de.setTaskId(entity.getTaskId());
        task_repository.delete(de);
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {
        mvc
            .perform(get("/task?search=taskId[equals]=1&limit=10&offset=1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(
                            get("/task?search=tasktaskId[equals]=1&limit=10&offset=1")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
            )
            .hasCause(new Exception("Wrong URL Format: Property tasktaskId not found!"));
    }

    @Test
    public void GetCases_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() {
        org.assertj.core.api.Assertions
            .assertThatThrownBy(
                () ->
                    mvc
                        .perform(get("/task/999/cases").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
            )
            .hasCause(new EntityNotFoundException("Not found"));
    }

    @Test
    public void GetCases_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
        mvc
            .perform(get("/task/" + task.getTaskId() + "/cases").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
