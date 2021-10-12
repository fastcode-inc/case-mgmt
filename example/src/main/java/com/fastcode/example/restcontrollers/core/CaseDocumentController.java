package com.fastcode.example.restcontrollers.core;

import com.fastcode.example.application.core.casedocument.ICaseDocumentAppService;
import com.fastcode.example.application.core.casedocument.dto.*;
import com.fastcode.example.application.core.cases.ICasesAppService;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.OffsetBasedPageRequest;
import com.fastcode.example.commons.search.SearchCriteria;
import com.fastcode.example.commons.search.SearchUtils;
import com.fastcode.example.domain.core.casedocument.CaseDocumentId;
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
@RequestMapping("/caseDocument")
@RequiredArgsConstructor
public class CaseDocumentController {

    @Qualifier("caseDocumentAppService")
    @NonNull
    protected final ICaseDocumentAppService _caseDocumentAppService;

    @Qualifier("casesAppService")
    @NonNull
    protected final ICasesAppService _casesAppService;

    @NonNull
    protected final LoggingHelper logHelper;

    @NonNull
    protected final Environment env;

    @PreAuthorize("hasAnyAuthority('CASEDOCUMENTENTITY_CREATE')")
    @RequestMapping(method = RequestMethod.POST, consumes = { "application/json" }, produces = { "application/json" })
    public ResponseEntity<CreateCaseDocumentOutput> create(@RequestBody @Valid CreateCaseDocumentInput caseDocument) {
        CreateCaseDocumentOutput output = _caseDocumentAppService.create(caseDocument);
        return new ResponseEntity(output, HttpStatus.OK);
    }

    // ------------ Delete caseDocument ------------
    @PreAuthorize("hasAnyAuthority('CASEDOCUMENTENTITY_DELETE')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = { "application/json" })
    public void delete(@PathVariable String id) {
        CaseDocumentId casedocumentid = _caseDocumentAppService.parseCaseDocumentKey(id);
        Optional
            .ofNullable(casedocumentid)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

        FindCaseDocumentByIdOutput output = _caseDocumentAppService.findById(casedocumentid);
        Optional
            .ofNullable(output)
            .orElseThrow(
                () -> new EntityNotFoundException(String.format("There does not exist a caseDocument with a id=%s", id))
            );

        _caseDocumentAppService.delete(casedocumentid);
    }

    // ------------ Update caseDocument ------------
    @PreAuthorize("hasAnyAuthority('CASEDOCUMENTENTITY_UPDATE')")
    @RequestMapping(
        value = "/{id}",
        method = RequestMethod.PUT,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<UpdateCaseDocumentOutput> update(
        @PathVariable String id,
        @RequestBody @Valid UpdateCaseDocumentInput caseDocument
    ) {
        CaseDocumentId casedocumentid = _caseDocumentAppService.parseCaseDocumentKey(id);

        Optional
            .ofNullable(casedocumentid)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

        FindCaseDocumentByIdOutput currentCaseDocument = _caseDocumentAppService.findById(casedocumentid);
        Optional
            .ofNullable(currentCaseDocument)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        String.format("Unable to update. CaseDocument with id=%s not found.", id)
                    )
            );

        caseDocument.setVersiono(currentCaseDocument.getVersiono());
        UpdateCaseDocumentOutput output = _caseDocumentAppService.update(casedocumentid, caseDocument);
        return new ResponseEntity(output, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('CASEDOCUMENTENTITY_READ')")
    @RequestMapping(
        value = "/{id}",
        method = RequestMethod.GET,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<FindCaseDocumentByIdOutput> findById(@PathVariable String id) {
        CaseDocumentId casedocumentid = _caseDocumentAppService.parseCaseDocumentKey(id);
        Optional
            .ofNullable(casedocumentid)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

        FindCaseDocumentByIdOutput output = _caseDocumentAppService.findById(casedocumentid);
        Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

        return new ResponseEntity(output, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('CASEDOCUMENTENTITY_READ')")
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

        return ResponseEntity.ok(_caseDocumentAppService.find(searchCriteria, Pageable));
    }

    @PreAuthorize("hasAnyAuthority('CASEDOCUMENTENTITY_READ')")
    @RequestMapping(
        value = "/{id}/cases",
        method = RequestMethod.GET,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<GetCasesOutput> getCases(@PathVariable String id) {
        CaseDocumentId casedocumentid = _caseDocumentAppService.parseCaseDocumentKey(id);
        Optional
            .ofNullable(casedocumentid)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

        GetCasesOutput output = _caseDocumentAppService.getCases(casedocumentid);
        Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

        return new ResponseEntity(output, HttpStatus.OK);
    }
}
