package com.fastcode.example.restcontrollers.core;

import com.fastcode.example.application.core.cases.ICasesAppService;
import com.fastcode.example.application.core.person.IPersonAppService;
import com.fastcode.example.application.core.personcase.IPersonCaseAppService;
import com.fastcode.example.application.core.personcase.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.OffsetBasedPageRequest;
import com.fastcode.example.commons.search.SearchCriteria;
import com.fastcode.example.commons.search.SearchUtils;
import com.fastcode.example.domain.core.personcase.PersonCaseId;
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
@RequestMapping("/personCase")
@RequiredArgsConstructor
public class PersonCaseController {

    @Qualifier("personCaseAppService")
    @NonNull
    protected final IPersonCaseAppService _personCaseAppService;

    @Qualifier("casesAppService")
    @NonNull
    protected final ICasesAppService _casesAppService;

    @Qualifier("personAppService")
    @NonNull
    protected final IPersonAppService _personAppService;

    @NonNull
    protected final LoggingHelper logHelper;

    @NonNull
    protected final Environment env;

    @PreAuthorize("hasAnyAuthority('PERSONCASEENTITY_CREATE')")
    @RequestMapping(method = RequestMethod.POST, consumes = { "application/json" }, produces = { "application/json" })
    public ResponseEntity<CreatePersonCaseOutput> create(@RequestBody @Valid CreatePersonCaseInput personCase) {
        CreatePersonCaseOutput output = _personCaseAppService.create(personCase);
        return new ResponseEntity(output, HttpStatus.OK);
    }

    // ------------ Delete personCase ------------
    @PreAuthorize("hasAnyAuthority('PERSONCASEENTITY_DELETE')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = { "application/json" })
    public void delete(@PathVariable String id) {
        PersonCaseId personcaseid = _personCaseAppService.parsePersonCaseKey(id);
        Optional
            .ofNullable(personcaseid)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

        FindPersonCaseByIdOutput output = _personCaseAppService.findById(personcaseid);
        Optional
            .ofNullable(output)
            .orElseThrow(
                () -> new EntityNotFoundException(String.format("There does not exist a personCase with a id=%s", id))
            );

        _personCaseAppService.delete(personcaseid);
    }

    // ------------ Update personCase ------------
    @PreAuthorize("hasAnyAuthority('PERSONCASEENTITY_UPDATE')")
    @RequestMapping(
        value = "/{id}",
        method = RequestMethod.PUT,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<UpdatePersonCaseOutput> update(
        @PathVariable String id,
        @RequestBody @Valid UpdatePersonCaseInput personCase
    ) {
        PersonCaseId personcaseid = _personCaseAppService.parsePersonCaseKey(id);

        Optional
            .ofNullable(personcaseid)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

        FindPersonCaseByIdOutput currentPersonCase = _personCaseAppService.findById(personcaseid);
        Optional
            .ofNullable(currentPersonCase)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(String.format("Unable to update. PersonCase with id=%s not found.", id))
            );

        personCase.setVersiono(currentPersonCase.getVersiono());
        UpdatePersonCaseOutput output = _personCaseAppService.update(personcaseid, personCase);
        return new ResponseEntity(output, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('PERSONCASEENTITY_READ')")
    @RequestMapping(
        value = "/{id}",
        method = RequestMethod.GET,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<FindPersonCaseByIdOutput> findById(@PathVariable String id) {
        PersonCaseId personcaseid = _personCaseAppService.parsePersonCaseKey(id);
        Optional
            .ofNullable(personcaseid)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

        FindPersonCaseByIdOutput output = _personCaseAppService.findById(personcaseid);
        Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

        return new ResponseEntity(output, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('PERSONCASEENTITY_READ')")
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

        return ResponseEntity.ok(_personCaseAppService.find(searchCriteria, Pageable));
    }

    @PreAuthorize("hasAnyAuthority('PERSONCASEENTITY_READ')")
    @RequestMapping(
        value = "/{id}/cases",
        method = RequestMethod.GET,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<GetCasesOutput> getCases(@PathVariable String id) {
        PersonCaseId personcaseid = _personCaseAppService.parsePersonCaseKey(id);
        Optional
            .ofNullable(personcaseid)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

        GetCasesOutput output = _personCaseAppService.getCases(personcaseid);
        Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

        return new ResponseEntity(output, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('PERSONCASEENTITY_READ')")
    @RequestMapping(
        value = "/{id}/person",
        method = RequestMethod.GET,
        consumes = { "application/json" },
        produces = { "application/json" }
    )
    public ResponseEntity<GetPersonOutput> getPerson(@PathVariable String id) {
        PersonCaseId personcaseid = _personCaseAppService.parsePersonCaseKey(id);
        Optional
            .ofNullable(personcaseid)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

        GetPersonOutput output = _personCaseAppService.getPerson(personcaseid);
        Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

        return new ResponseEntity(output, HttpStatus.OK);
    }
}
