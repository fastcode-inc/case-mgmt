package com.fastcode.example.application.core.task;

import com.fastcode.example.application.core.task.dto.*;
import com.fastcode.example.domain.core.cases.Cases;
import com.fastcode.example.domain.core.task.Task;
import java.time.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ITaskMapper {
    Task createTaskInputToTask(CreateTaskInput taskDto);

    @Mappings(
        {
            @Mapping(source = "entity.cases.caseId", target = "caseId"),
            @Mapping(source = "entity.cases.caseId", target = "casesDescriptiveField"),
        }
    )
    CreateTaskOutput taskToCreateTaskOutput(Task entity);

    Task updateTaskInputToTask(UpdateTaskInput taskDto);

    @Mappings(
        {
            @Mapping(source = "entity.cases.caseId", target = "caseId"),
            @Mapping(source = "entity.cases.caseId", target = "casesDescriptiveField"),
        }
    )
    UpdateTaskOutput taskToUpdateTaskOutput(Task entity);

    @Mappings(
        {
            @Mapping(source = "entity.cases.caseId", target = "caseId"),
            @Mapping(source = "entity.cases.caseId", target = "casesDescriptiveField"),
        }
    )
    FindTaskByIdOutput taskToFindTaskByIdOutput(Task entity);

    @Mappings(
        {
            @Mapping(source = "cases.status", target = "status"),
            @Mapping(source = "cases.type", target = "type"),
            @Mapping(source = "foundTask.taskId", target = "taskTaskId"),
        }
    )
    GetCasesOutput casesToGetCasesOutput(Cases cases, Task foundTask);
}
