package com.fastcode.example.restcontrollers.core;

import com.fastcode.example.application.core.cases.ICasesAppService;
import com.fastcode.example.application.core.task.ITaskAppService;
import com.fastcode.example.application.core.task.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.OffsetBasedPageRequest;
import com.fastcode.example.commons.search.SearchCriteria;
import com.fastcode.example.commons.search.SearchUtils;
import java.time.*;
import java.util.*;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    @Qualifier("taskAppService")
    @NonNull
    protected final ITaskAppService _taskAppService;

    @Qualifier("casesAppService")
    @NonNull
    protected final ICasesAppService _casesAppService;

    @NonNull
    protected final LoggingHelper logHelper;

    @NonNull
    protected final Environment env;

    @PreAuthorize("hasAnyAuthority('TASKENTITY_CREATE')")
    @RequestMapping(method = RequestMethod.POST, consumes = { "application/json" }, produces = { "application/json" })
    public ResponseEntity<CreateTaskOutput> create(@RequestBody @Valid CreateTaskInput task) {
        CreateTaskOutput output = _taskAppService.create(task);
        return new ResponseEntity(output, HttpStatus.OK);
    }

    // ------------ Delete task ------------
    @PreAuthorize("hasAnyAuthority('TASKENTITY_DELETE')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = { "application/json" })
    public void delete(@PathVariable String id) {
        FindTaskByIdOutput output = _taskAppService.findById(Long.valueOf(id));
        Optional
            .ofNullable(output)
            .orElseThrow(
                () -> new EntityNotFoundException(String.format("There does not exist a task with a id=%s", id))
            );

        _taskAppService.delete(Long.valueOf(id));
    }

    // ------------ Update task ------------
    @PreAuthorize("hasAnyAuthority('TASKENTITY_UPDATE')")
    @RequestMapping(
        value = "/{id}",
        method = RequestMethod.PUT,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<UpdateTaskOutput> update(@PathVariable String id, @RequestBody @Valid UpdateTaskInput task) {
        FindTaskByIdOutput currentTask = _taskAppService.findById(Long.valueOf(id));
        Optional
            .ofNullable(currentTask)
            .orElseThrow(
                () -> new EntityNotFoundException(String.format("Unable to update. Task with id=%s not found.", id))
            );

        task.setVersiono(currentTask.getVersiono());
        UpdateTaskOutput output = _taskAppService.update(Long.valueOf(id), task);
        return new ResponseEntity(output, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('TASKENTITY_READ')")
    @RequestMapping(
        value = "/{id}",
        method = RequestMethod.GET,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<FindTaskByIdOutput> findById(@PathVariable String id) {
        FindTaskByIdOutput output = _taskAppService.findById(Long.valueOf(id));
        Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

        return new ResponseEntity(output, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('TASKENTITY_READ')")
    @RequestMapping(method = RequestMethod.GET, consumes = { "application/json" }, produces = { "application/json" })
    public ResponseEntity find(
        @RequestParam(value = "search", required = false) String search,
        @RequestParam(value = "offset", required = false) String offset,
        @RequestParam(value = "limit", required = false) String limit,
        Sort sort
    )
        throws Exception {
        if (offset == null) {
            offset = env.getProperty("fastCode.offset.default");
        }
        if (limit == null) {
            limit = env.getProperty("fastCode.limit.default");
        }

        Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
        SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);

        return ResponseEntity.ok(_taskAppService.find(searchCriteria, Pageable));
    }

    @PreAuthorize("hasAnyAuthority('TASKENTITY_READ')")
    @RequestMapping(
        value = "/{id}/cases",
        method = RequestMethod.GET,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<GetCasesOutput> getCases(@PathVariable String id) {
        GetCasesOutput output = _taskAppService.getCases(Long.valueOf(id));
        Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

        return new ResponseEntity(output, HttpStatus.OK);
    }
}
