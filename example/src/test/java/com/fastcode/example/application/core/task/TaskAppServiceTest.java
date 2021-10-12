package com.fastcode.example.application.core.task;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fastcode.example.application.core.task.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.domain.core.cases.Cases;
import com.fastcode.example.domain.core.cases.ICasesRepository;
import com.fastcode.example.domain.core.task.*;
import com.fastcode.example.domain.core.task.QTask;
import com.fastcode.example.domain.core.task.Task;
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
public class TaskAppServiceTest {

    @InjectMocks
    @Spy
    protected TaskAppService _appService;

    @Mock
    protected ITaskRepository _taskRepository;

    @Mock
    protected ICasesRepository _casesRepository;

    @Mock
    protected ITaskMapper _mapper;

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
    public void findTaskById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
        Optional<Task> nullOptional = Optional.ofNullable(null);
        Mockito.when(_taskRepository.findById(anyLong())).thenReturn(nullOptional);
        Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
    }

    @Test
    public void findTaskById_IdIsNotNullAndIdExists_ReturnTask() {
        Task task = mock(Task.class);
        Optional<Task> taskOptional = Optional.of((Task) task);
        Mockito.when(_taskRepository.findById(anyLong())).thenReturn(taskOptional);

        Assertions.assertThat(_appService.findById(ID)).isEqualTo(_mapper.taskToFindTaskByIdOutput(task));
    }

    @Test
    public void createTask_TaskIsNotNullAndTaskDoesNotExist_StoreTask() {
        Task taskEntity = mock(Task.class);
        CreateTaskInput taskInput = new CreateTaskInput();

        Cases cases = mock(Cases.class);
        Optional<Cases> casesOptional = Optional.of((Cases) cases);
        taskInput.setCaseId(15L);

        Mockito.when(_casesRepository.findById(any(Long.class))).thenReturn(casesOptional);

        Mockito.when(_mapper.createTaskInputToTask(any(CreateTaskInput.class))).thenReturn(taskEntity);
        Mockito.when(_taskRepository.save(any(Task.class))).thenReturn(taskEntity);

        Assertions.assertThat(_appService.create(taskInput)).isEqualTo(_mapper.taskToCreateTaskOutput(taskEntity));
    }

    @Test
    public void createTask_TaskIsNotNullAndTaskDoesNotExistAndChildIsNullAndChildIsNotMandatory_StoreTask() {
        Task task = mock(Task.class);
        CreateTaskInput taskInput = mock(CreateTaskInput.class);

        Mockito.when(_mapper.createTaskInputToTask(any(CreateTaskInput.class))).thenReturn(task);
        Mockito.when(_taskRepository.save(any(Task.class))).thenReturn(task);
        Assertions.assertThat(_appService.create(taskInput)).isEqualTo(_mapper.taskToCreateTaskOutput(task));
    }

    @Test
    public void updateTask_TaskIsNotNullAndTaskDoesNotExistAndChildIsNullAndChildIsNotMandatory_ReturnUpdatedTask() {
        Task task = mock(Task.class);
        UpdateTaskInput taskInput = mock(UpdateTaskInput.class);

        Optional<Task> taskOptional = Optional.of((Task) task);
        Mockito.when(_taskRepository.findById(anyLong())).thenReturn(taskOptional);

        Mockito.when(_mapper.updateTaskInputToTask(any(UpdateTaskInput.class))).thenReturn(task);
        Mockito.when(_taskRepository.save(any(Task.class))).thenReturn(task);
        Assertions.assertThat(_appService.update(ID, taskInput)).isEqualTo(_mapper.taskToUpdateTaskOutput(task));
    }

    @Test
    public void updateTask_TaskIdIsNotNullAndIdExists_ReturnUpdatedTask() {
        Task taskEntity = mock(Task.class);
        UpdateTaskInput task = mock(UpdateTaskInput.class);

        Optional<Task> taskOptional = Optional.of((Task) taskEntity);
        Mockito.when(_taskRepository.findById(anyLong())).thenReturn(taskOptional);

        Mockito.when(_mapper.updateTaskInputToTask(any(UpdateTaskInput.class))).thenReturn(taskEntity);
        Mockito.when(_taskRepository.save(any(Task.class))).thenReturn(taskEntity);
        Assertions.assertThat(_appService.update(ID, task)).isEqualTo(_mapper.taskToUpdateTaskOutput(taskEntity));
    }

    @Test
    public void deleteTask_TaskIsNotNullAndTaskExists_TaskRemoved() {
        Task task = mock(Task.class);
        Optional<Task> taskOptional = Optional.of((Task) task);
        Mockito.when(_taskRepository.findById(anyLong())).thenReturn(taskOptional);

        _appService.delete(ID);
        verify(_taskRepository).delete(task);
    }

    @Test
    public void find_ListIsEmpty_ReturnList() throws Exception {
        List<Task> list = new ArrayList<>();
        Page<Task> foundPage = new PageImpl(list);
        Pageable pageable = mock(Pageable.class);
        List<FindTaskByIdOutput> output = new ArrayList<>();
        SearchCriteria search = new SearchCriteria();

        Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
        Mockito.when(_taskRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(foundPage);
        Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
    }

    @Test
    public void find_ListIsNotEmpty_ReturnList() throws Exception {
        List<Task> list = new ArrayList<>();
        Task task = mock(Task.class);
        list.add(task);
        Page<Task> foundPage = new PageImpl(list);
        Pageable pageable = mock(Pageable.class);
        List<FindTaskByIdOutput> output = new ArrayList<>();
        SearchCriteria search = new SearchCriteria();

        output.add(_mapper.taskToFindTaskByIdOutput(task));

        Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
        Mockito.when(_taskRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(foundPage);
        Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
    }

    @Test
    public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
        QTask task = QTask.taskEntity;
        SearchFields searchFields = new SearchFields();
        searchFields.setOperator("equals");
        searchFields.setSearchValue("xyz");
        Map<String, SearchFields> map = new HashMap<>();
        map.put("message", searchFields);
        Map<String, String> searchMap = new HashMap<>();
        searchMap.put("xyz", String.valueOf(ID));
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(task.message.eq("xyz"));
        Assertions.assertThat(_appService.searchKeyValuePair(task, map, searchMap)).isEqualTo(builder);
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
        list.add("status");
        list.add("type");
        list.add("username");
        _appService.checkProperties(list);
    }

    @Test
    public void search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
        Map<String, SearchFields> map = new HashMap<>();
        QTask task = QTask.taskEntity;
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
        builder.or(task.message.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
        Mockito
            .doReturn(builder)
            .when(_appService)
            .searchKeyValuePair(any(QTask.class), any(HashMap.class), any(HashMap.class));

        Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
    }

    @Test
    public void search_StringIsNull_ReturnNull() throws Exception {
        Assertions.assertThat(_appService.search(null)).isEqualTo(null);
    }

    //Cases
    @Test
    public void GetCases_IfTaskIdAndCasesIdIsNotNullAndTaskExists_ReturnCases() {
        Task task = mock(Task.class);
        Optional<Task> taskOptional = Optional.of((Task) task);
        Cases casesEntity = mock(Cases.class);

        Mockito.when(_taskRepository.findById(any(Long.class))).thenReturn(taskOptional);

        Mockito.when(task.getCases()).thenReturn(casesEntity);
        Assertions.assertThat(_appService.getCases(ID)).isEqualTo(_mapper.casesToGetCasesOutput(casesEntity, task));
    }

    @Test
    public void GetCases_IfTaskIdAndCasesIdIsNotNullAndTaskDoesNotExist_ReturnNull() {
        Optional<Task> nullOptional = Optional.ofNullable(null);
        Mockito.when(_taskRepository.findById(anyLong())).thenReturn(nullOptional);
        Assertions.assertThat(_appService.getCases(ID)).isEqualTo(null);
    }
}
