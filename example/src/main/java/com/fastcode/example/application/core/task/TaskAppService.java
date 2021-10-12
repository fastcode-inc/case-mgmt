package com.fastcode.example.application.core.task;

import com.fastcode.example.application.core.task.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.domain.core.cases.Cases;
import com.fastcode.example.domain.core.cases.ICasesRepository;
import com.fastcode.example.domain.core.task.ITaskRepository;
import com.fastcode.example.domain.core.task.QTask;
import com.fastcode.example.domain.core.task.Task;
import com.querydsl.core.BooleanBuilder;
import java.time.*;
import java.util.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("taskAppService")
@RequiredArgsConstructor
public class TaskAppService implements ITaskAppService {

    @Qualifier("taskRepository")
    @NonNull
    protected final ITaskRepository _taskRepository;

    @Qualifier("casesRepository")
    @NonNull
    protected final ICasesRepository _casesRepository;

    @Qualifier("ITaskMapperImpl")
    @NonNull
    protected final ITaskMapper mapper;

    @NonNull
    protected final LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
    public CreateTaskOutput create(CreateTaskInput input) {
        Task task = mapper.createTaskInputToTask(input);
        Cases foundCases = null;
        if (input.getCaseId() != null) {
            foundCases = _casesRepository.findById(input.getCaseId()).orElse(null);

            if (foundCases != null) {
                foundCases.addTasks(task);
                //task.setCases(foundCases);
            }
        }

        Task createdTask = _taskRepository.save(task);
        return mapper.taskToCreateTaskOutput(createdTask);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UpdateTaskOutput update(Long taskId, UpdateTaskInput input) {
        Task existing = _taskRepository.findById(taskId).get();

        Task task = mapper.updateTaskInputToTask(input);
        Cases foundCases = null;

        if (input.getCaseId() != null) {
            foundCases = _casesRepository.findById(input.getCaseId()).orElse(null);

            if (foundCases != null) {
                foundCases.addTasks(task);
                //	task.setCases(foundCases);
            }
        }

        Task updatedTask = _taskRepository.save(task);
        return mapper.taskToUpdateTaskOutput(updatedTask);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long taskId) {
        Task existing = _taskRepository.findById(taskId).orElse(null);

        if (existing.getCases() != null) {
            existing.getCases().removeTasks(existing);
        }
        _taskRepository.delete(existing);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FindTaskByIdOutput findById(Long taskId) {
        Task foundTask = _taskRepository.findById(taskId).orElse(null);
        if (foundTask == null) return null;

        return mapper.taskToFindTaskByIdOutput(foundTask);
    }

    //Cases
    // ReST API Call - GET /task/1/cases
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public GetCasesOutput getCases(Long taskId) {
        Task foundTask = _taskRepository.findById(taskId).orElse(null);
        if (foundTask == null) {
            logHelper.getLogger().error("There does not exist a task wth a id=%s", taskId);
            return null;
        }
        Cases re = foundTask.getCases();
        return mapper.casesToGetCasesOutput(re, foundTask);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<FindTaskByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception {
        Page<Task> foundTask = _taskRepository.findAll(search(search), pageable);
        List<Task> taskList = foundTask.getContent();
        Iterator<Task> taskIterator = taskList.iterator();
        List<FindTaskByIdOutput> output = new ArrayList<>();

        while (taskIterator.hasNext()) {
            Task task = taskIterator.next();
            output.add(mapper.taskToFindTaskByIdOutput(task));
        }
        return output;
    }

    protected BooleanBuilder search(SearchCriteria search) throws Exception {
        QTask task = QTask.taskEntity;
        if (search != null) {
            Map<String, SearchFields> map = new HashMap<>();
            for (SearchFields fieldDetails : search.getFields()) {
                map.put(fieldDetails.getFieldName(), fieldDetails);
            }
            List<String> keysList = new ArrayList<String>(map.keySet());
            checkProperties(keysList);
            return searchKeyValuePair(task, map, search.getJoinColumns());
        }
        return null;
    }

    protected void checkProperties(List<String> list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            if (
                !(
                    list.get(i).replace("%20", "").trim().equals("cases") ||
                    list.get(i).replace("%20", "").trim().equals("caseId") ||
                    list.get(i).replace("%20", "").trim().equals("message") ||
                    list.get(i).replace("%20", "").trim().equals("status") ||
                    list.get(i).replace("%20", "").trim().equals("taskId") ||
                    list.get(i).replace("%20", "").trim().equals("type") ||
                    list.get(i).replace("%20", "").trim().equals("username")
                )
            ) {
                throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!");
            }
        }
    }

    protected BooleanBuilder searchKeyValuePair(
        QTask task,
        Map<String, SearchFields> map,
        Map<String, String> joinColumns
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if (details.getKey().replace("%20", "").trim().equals("message")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(task.message.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
                } else if (details.getValue().getOperator().equals("equals")) {
                    builder.and(task.message.eq(details.getValue().getSearchValue()));
                } else if (details.getValue().getOperator().equals("notEqual")) {
                    builder.and(task.message.ne(details.getValue().getSearchValue()));
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("status")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(task.status.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
                } else if (details.getValue().getOperator().equals("equals")) {
                    builder.and(task.status.eq(details.getValue().getSearchValue()));
                } else if (details.getValue().getOperator().equals("notEqual")) {
                    builder.and(task.status.ne(details.getValue().getSearchValue()));
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("taskId")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(task.taskId.like(details.getValue().getSearchValue() + "%"));
                } else if (
                    details.getValue().getOperator().equals("equals") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(task.taskId.eq(Long.valueOf(details.getValue().getSearchValue())));
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(task.taskId.ne(Long.valueOf(details.getValue().getSearchValue())));
                } else if (details.getValue().getOperator().equals("range")) {
                    if (
                        StringUtils.isNumeric(details.getValue().getStartingValue()) &&
                        StringUtils.isNumeric(details.getValue().getEndingValue())
                    ) {
                        builder.and(
                            task.taskId.between(
                                Long.valueOf(details.getValue().getStartingValue()),
                                Long.valueOf(details.getValue().getEndingValue())
                            )
                        );
                    } else if (StringUtils.isNumeric(details.getValue().getStartingValue())) {
                        builder.and(task.taskId.goe(Long.valueOf(details.getValue().getStartingValue())));
                    } else if (StringUtils.isNumeric(details.getValue().getEndingValue())) {
                        builder.and(task.taskId.loe(Long.valueOf(details.getValue().getEndingValue())));
                    }
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("type")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(task.type.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
                } else if (details.getValue().getOperator().equals("equals")) {
                    builder.and(task.type.eq(details.getValue().getSearchValue()));
                } else if (details.getValue().getOperator().equals("notEqual")) {
                    builder.and(task.type.ne(details.getValue().getSearchValue()));
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("username")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(task.username.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
                } else if (details.getValue().getOperator().equals("equals")) {
                    builder.and(task.username.eq(details.getValue().getSearchValue()));
                } else if (details.getValue().getOperator().equals("notEqual")) {
                    builder.and(task.username.ne(details.getValue().getSearchValue()));
                }
            }

            if (details.getKey().replace("%20", "").trim().equals("cases")) {
                if (
                    details.getValue().getOperator().equals("contains") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(task.cases.caseId.like(details.getValue().getSearchValue() + "%"));
                } else if (
                    details.getValue().getOperator().equals("equals") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(task.cases.caseId.eq(Long.valueOf(details.getValue().getSearchValue())));
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(task.cases.caseId.ne(Long.valueOf(details.getValue().getSearchValue())));
                } else if (details.getValue().getOperator().equals("range")) {
                    if (
                        StringUtils.isNumeric(details.getValue().getStartingValue()) &&
                        StringUtils.isNumeric(details.getValue().getEndingValue())
                    ) {
                        builder.and(
                            task.cases.caseId.between(
                                Long.valueOf(details.getValue().getStartingValue()),
                                Long.valueOf(details.getValue().getEndingValue())
                            )
                        );
                    } else if (StringUtils.isNumeric(details.getValue().getStartingValue())) {
                        builder.and(task.cases.caseId.goe(Long.valueOf(details.getValue().getStartingValue())));
                    } else if (StringUtils.isNumeric(details.getValue().getEndingValue())) {
                        builder.and(task.cases.caseId.loe(Long.valueOf(details.getValue().getEndingValue())));
                    }
                }
            }
        }

        for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
            if (joinCol != null && joinCol.getKey().equals("caseId")) {
                builder.and(task.cases.caseId.eq(Long.parseLong(joinCol.getValue())));
            }
        }
        return builder;
    }
}
