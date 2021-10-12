package com.fastcode.example.restcontrollers.core;

import com.fastcode.example.application.core.casehistory.ICaseHistoryAppService;
import com.fastcode.example.application.core.casehistory.dto.*;
import com.fastcode.example.application.core.cases.ICasesAppService;
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
@RequestMapping("/caseHistory")
@RequiredArgsConstructor
public class CaseHistoryController {

    @Qualifier("caseHistoryAppService")
    @NonNull
    protected final ICaseHistoryAppService _caseHistoryAppService;

    @Qualifier("casesAppService")
    @NonNull
    protected final ICasesAppService _casesAppService;

    @NonNull
    protected final LoggingHelper logHelper;

    @NonNull
    protected final Environment env;

    @PreAuthorize("hasAnyAuthority('CASEHISTORYENTITY_CREATE')")
    @RequestMapping(method = RequestMethod.POST, consumes = { "application/json" }, produces = { "application/json" })
    public ResponseEntity<CreateCaseHistoryOutput> create(@RequestBody @Valid CreateCaseHistoryInput caseHistory) {
        CreateCaseHistoryOutput output = _caseHistoryAppService.create(caseHistory);
        Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("No record found")));

        return new ResponseEntity(output, HttpStatus.OK);
    }

    // ------------ Delete caseHistory ------------
    @PreAuthorize("hasAnyAuthority('CASEHISTORYENTITY_DELETE')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = { "application/json" })
    public void delete(@PathVariable String id) {
        FindCaseHistoryByIdOutput output = _caseHistoryAppService.findById(Long.valueOf(id));
        Optional
            .ofNullable(output)
            .orElseThrow(
                () -> new EntityNotFoundException(String.format("There does not exist a caseHistory with a id=%s", id))
            );

        _caseHistoryAppService.delete(Long.valueOf(id));
    }

    // ------------ Update caseHistory ------------
    @PreAuthorize("hasAnyAuthority('CASEHISTORYENTITY_UPDATE')")
    @RequestMapping(
        value = "/{id}",
        method = RequestMethod.PUT,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<UpdateCaseHistoryOutput> update(
        @PathVariable String id,
        @RequestBody @Valid UpdateCaseHistoryInput caseHistory
    ) {
        FindCaseHistoryByIdOutput currentCaseHistory = _caseHistoryAppService.findById(Long.valueOf(id));
        Optional
            .ofNullable(currentCaseHistory)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        String.format("Unable to update. CaseHistory with id=%s not found.", id)
                    )
            );

        caseHistory.setVersiono(currentCaseHistory.getVersiono());
        UpdateCaseHistoryOutput output = _caseHistoryAppService.update(Long.valueOf(id), caseHistory);
        Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("No record found")));
        return new ResponseEntity(output, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('CASEHISTORYENTITY_READ')")
    @RequestMapping(
        value = "/{id}",
        method = RequestMethod.GET,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<FindCaseHistoryByIdOutput> findById(@PathVariable String id) {
        FindCaseHistoryByIdOutput output = _caseHistoryAppService.findById(Long.valueOf(id));
        Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

        return new ResponseEntity(output, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('CASEHISTORYENTITY_READ')")
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

        return ResponseEntity.ok(_caseHistoryAppService.find(searchCriteria, Pageable));
    }

    @PreAuthorize("hasAnyAuthority('CASEHISTORYENTITY_READ')")
    @RequestMapping(
        value = "/{id}/cases",
        method = RequestMethod.GET,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<GetCasesOutput> getCases(@PathVariable String id) {
        GetCasesOutput output = _caseHistoryAppService.getCases(Long.valueOf(id));
        Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

        return new ResponseEntity(output, HttpStatus.OK);
    }
}
