package com.fastcode.example.application.core.cases;

import com.fastcode.example.application.core.cases.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.domain.core.cases.Cases;
import com.fastcode.example.domain.core.cases.ICasesRepository;
import com.fastcode.example.domain.core.cases.QCases;
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

@Service("casesAppService")
@RequiredArgsConstructor
public class CasesAppService implements ICasesAppService {

    @Qualifier("casesRepository")
    @NonNull
    protected final ICasesRepository _casesRepository;

    @Qualifier("ICasesMapperImpl")
    @NonNull
    protected final ICasesMapper mapper;

    @NonNull
    protected final LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
    public CreateCasesOutput create(CreateCasesInput input) {
        Cases cases = mapper.createCasesInputToCases(input);

        Cases createdCases = _casesRepository.save(cases);
        return mapper.casesToCreateCasesOutput(createdCases);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UpdateCasesOutput update(Long casesId, UpdateCasesInput input) {
        Cases existing = _casesRepository.findById(casesId).get();

        Cases cases = mapper.updateCasesInputToCases(input);
        cases.setCaseDocumentsSet(existing.getCaseDocumentsSet());
        cases.setCaseHistorysSet(existing.getCaseHistorysSet());
        cases.setPersonCasesSet(existing.getPersonCasesSet());
        cases.setTasksSet(existing.getTasksSet());

        Cases updatedCases = _casesRepository.save(cases);
        return mapper.casesToUpdateCasesOutput(updatedCases);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long casesId) {
        Cases existing = _casesRepository.findById(casesId).orElse(null);

        _casesRepository.delete(existing);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FindCasesByIdOutput findById(Long casesId) {
        Cases foundCases = _casesRepository.findById(casesId).orElse(null);
        if (foundCases == null) return null;

        return mapper.casesToFindCasesByIdOutput(foundCases);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<FindCasesByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception {
        Page<Cases> foundCases = _casesRepository.findAll(search(search), pageable);
        List<Cases> casesList = foundCases.getContent();
        Iterator<Cases> casesIterator = casesList.iterator();
        List<FindCasesByIdOutput> output = new ArrayList<>();

        while (casesIterator.hasNext()) {
            Cases cases = casesIterator.next();
            output.add(mapper.casesToFindCasesByIdOutput(cases));
        }
        return output;
    }

    protected BooleanBuilder search(SearchCriteria search) throws Exception {
        QCases cases = QCases.casesEntity;
        if (search != null) {
            Map<String, SearchFields> map = new HashMap<>();
            for (SearchFields fieldDetails : search.getFields()) {
                map.put(fieldDetails.getFieldName(), fieldDetails);
            }
            List<String> keysList = new ArrayList<String>(map.keySet());
            checkProperties(keysList);
            return searchKeyValuePair(cases, map, search.getJoinColumns());
        }
        return null;
    }

    protected void checkProperties(List<String> list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            if (
                !(
                    list.get(i).replace("%20", "").trim().equals("caseId") ||
                    list.get(i).replace("%20", "").trim().equals("status") ||
                    list.get(i).replace("%20", "").trim().equals("summary") ||
                    list.get(i).replace("%20", "").trim().equals("type")
                )
            ) {
                throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!");
            }
        }
    }

    protected BooleanBuilder searchKeyValuePair(
        QCases cases,
        Map<String, SearchFields> map,
        Map<String, String> joinColumns
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if (details.getKey().replace("%20", "").trim().equals("caseId")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(cases.caseId.like(details.getValue().getSearchValue() + "%"));
                } else if (
                    details.getValue().getOperator().equals("equals") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(cases.caseId.eq(Long.valueOf(details.getValue().getSearchValue())));
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(cases.caseId.ne(Long.valueOf(details.getValue().getSearchValue())));
                } else if (details.getValue().getOperator().equals("range")) {
                    if (
                        StringUtils.isNumeric(details.getValue().getStartingValue()) &&
                        StringUtils.isNumeric(details.getValue().getEndingValue())
                    ) {
                        builder.and(
                            cases.caseId.between(
                                Long.valueOf(details.getValue().getStartingValue()),
                                Long.valueOf(details.getValue().getEndingValue())
                            )
                        );
                    } else if (StringUtils.isNumeric(details.getValue().getStartingValue())) {
                        builder.and(cases.caseId.goe(Long.valueOf(details.getValue().getStartingValue())));
                    } else if (StringUtils.isNumeric(details.getValue().getEndingValue())) {
                        builder.and(cases.caseId.loe(Long.valueOf(details.getValue().getEndingValue())));
                    }
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("status")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(cases.status.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
                } else if (details.getValue().getOperator().equals("equals")) {
                    builder.and(cases.status.eq(details.getValue().getSearchValue()));
                } else if (details.getValue().getOperator().equals("notEqual")) {
                    builder.and(cases.status.ne(details.getValue().getSearchValue()));
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("summary")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(cases.summary.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
                } else if (details.getValue().getOperator().equals("equals")) {
                    builder.and(cases.summary.eq(details.getValue().getSearchValue()));
                } else if (details.getValue().getOperator().equals("notEqual")) {
                    builder.and(cases.summary.ne(details.getValue().getSearchValue()));
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("type")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(cases.type.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
                } else if (details.getValue().getOperator().equals("equals")) {
                    builder.and(cases.type.eq(details.getValue().getSearchValue()));
                } else if (details.getValue().getOperator().equals("notEqual")) {
                    builder.and(cases.type.ne(details.getValue().getSearchValue()));
                }
            }
        }

        return builder;
    }

    public Map<String, String> parseCaseDocumentsJoinColumn(String keysString) {
        Map<String, String> joinColumnMap = new HashMap<String, String>();
        joinColumnMap.put("caseId", keysString);

        return joinColumnMap;
    }

    public Map<String, String> parseCaseHistorysJoinColumn(String keysString) {
        Map<String, String> joinColumnMap = new HashMap<String, String>();
        joinColumnMap.put("caseId", keysString);

        return joinColumnMap;
    }

    public Map<String, String> parsePersonCasesJoinColumn(String keysString) {
        Map<String, String> joinColumnMap = new HashMap<String, String>();
        joinColumnMap.put("caseId", keysString);

        return joinColumnMap;
    }

    public Map<String, String> parseTasksJoinColumn(String keysString) {
        Map<String, String> joinColumnMap = new HashMap<String, String>();
        joinColumnMap.put("caseId", keysString);

        return joinColumnMap;
    }
}
