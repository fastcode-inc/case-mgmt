package com.fastcode.example.application.core.casehistory;

import com.fastcode.example.application.core.casehistory.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.domain.core.casehistory.CaseHistory;
import com.fastcode.example.domain.core.casehistory.ICaseHistoryRepository;
import com.fastcode.example.domain.core.casehistory.QCaseHistory;
import com.fastcode.example.domain.core.cases.Cases;
import com.fastcode.example.domain.core.cases.ICasesRepository;
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

@Service("caseHistoryAppService")
@RequiredArgsConstructor
public class CaseHistoryAppService implements ICaseHistoryAppService {

    @Qualifier("caseHistoryRepository")
    @NonNull
    protected final ICaseHistoryRepository _caseHistoryRepository;

    @Qualifier("casesRepository")
    @NonNull
    protected final ICasesRepository _casesRepository;

    @Qualifier("ICaseHistoryMapperImpl")
    @NonNull
    protected final ICaseHistoryMapper mapper;

    @NonNull
    protected final LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
    public CreateCaseHistoryOutput create(CreateCaseHistoryInput input) {
        CaseHistory caseHistory = mapper.createCaseHistoryInputToCaseHistory(input);
        Cases foundCases = null;
        if (input.getCaseId() != null) {
            foundCases = _casesRepository.findById(input.getCaseId()).orElse(null);

            if (foundCases != null) {
                foundCases.addCaseHistorys(caseHistory);
                //caseHistory.setCases(foundCases);
            } else {
                return null;
            }
        } else {
            return null;
        }

        CaseHistory createdCaseHistory = _caseHistoryRepository.save(caseHistory);
        return mapper.caseHistoryToCreateCaseHistoryOutput(createdCaseHistory);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UpdateCaseHistoryOutput update(Long caseHistoryId, UpdateCaseHistoryInput input) {
        CaseHistory existing = _caseHistoryRepository.findById(caseHistoryId).get();

        CaseHistory caseHistory = mapper.updateCaseHistoryInputToCaseHistory(input);
        Cases foundCases = null;

        if (input.getCaseId() != null) {
            foundCases = _casesRepository.findById(input.getCaseId()).orElse(null);

            if (foundCases != null) {
                foundCases.addCaseHistorys(caseHistory);
                //	caseHistory.setCases(foundCases);
            } else {
                return null;
            }
        } else {
            return null;
        }

        CaseHistory updatedCaseHistory = _caseHistoryRepository.save(caseHistory);
        return mapper.caseHistoryToUpdateCaseHistoryOutput(updatedCaseHistory);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long caseHistoryId) {
        CaseHistory existing = _caseHistoryRepository.findById(caseHistoryId).orElse(null);

        if (existing.getCases() != null) {
            existing.getCases().removeCaseHistorys(existing);
        }
        _caseHistoryRepository.delete(existing);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FindCaseHistoryByIdOutput findById(Long caseHistoryId) {
        CaseHistory foundCaseHistory = _caseHistoryRepository.findById(caseHistoryId).orElse(null);
        if (foundCaseHistory == null) return null;

        return mapper.caseHistoryToFindCaseHistoryByIdOutput(foundCaseHistory);
    }

    //Cases
    // ReST API Call - GET /caseHistory/1/cases
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public GetCasesOutput getCases(Long caseHistoryId) {
        CaseHistory foundCaseHistory = _caseHistoryRepository.findById(caseHistoryId).orElse(null);
        if (foundCaseHistory == null) {
            logHelper.getLogger().error("There does not exist a caseHistory wth a id=%s", caseHistoryId);
            return null;
        }
        Cases re = foundCaseHistory.getCases();
        return mapper.casesToGetCasesOutput(re, foundCaseHistory);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<FindCaseHistoryByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception {
        Page<CaseHistory> foundCaseHistory = _caseHistoryRepository.findAll(search(search), pageable);
        List<CaseHistory> caseHistoryList = foundCaseHistory.getContent();
        Iterator<CaseHistory> caseHistoryIterator = caseHistoryList.iterator();
        List<FindCaseHistoryByIdOutput> output = new ArrayList<>();

        while (caseHistoryIterator.hasNext()) {
            CaseHistory caseHistory = caseHistoryIterator.next();
            output.add(mapper.caseHistoryToFindCaseHistoryByIdOutput(caseHistory));
        }
        return output;
    }

    protected BooleanBuilder search(SearchCriteria search) throws Exception {
        QCaseHistory caseHistory = QCaseHistory.caseHistoryEntity;
        if (search != null) {
            Map<String, SearchFields> map = new HashMap<>();
            for (SearchFields fieldDetails : search.getFields()) {
                map.put(fieldDetails.getFieldName(), fieldDetails);
            }
            List<String> keysList = new ArrayList<String>(map.keySet());
            checkProperties(keysList);
            return searchKeyValuePair(caseHistory, map, search.getJoinColumns());
        }
        return null;
    }

    protected void checkProperties(List<String> list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            if (
                !(
                    list.get(i).replace("%20", "").trim().equals("cases") ||
                    list.get(i).replace("%20", "").trim().equals("caseId") ||
                    list.get(i).replace("%20", "").trim().equals("caseHistoryId") ||
                    list.get(i).replace("%20", "").trim().equals("message") ||
                    list.get(i).replace("%20", "").trim().equals("timestamp")
                )
            ) {
                throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!");
            }
        }
    }

    protected BooleanBuilder searchKeyValuePair(
        QCaseHistory caseHistory,
        Map<String, SearchFields> map,
        Map<String, String> joinColumns
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if (details.getKey().replace("%20", "").trim().equals("caseHistoryId")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(caseHistory.caseHistoryId.like(details.getValue().getSearchValue() + "%"));
                } else if (
                    details.getValue().getOperator().equals("equals") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(caseHistory.caseHistoryId.eq(Long.valueOf(details.getValue().getSearchValue())));
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(caseHistory.caseHistoryId.ne(Long.valueOf(details.getValue().getSearchValue())));
                } else if (details.getValue().getOperator().equals("range")) {
                    if (
                        StringUtils.isNumeric(details.getValue().getStartingValue()) &&
                        StringUtils.isNumeric(details.getValue().getEndingValue())
                    ) {
                        builder.and(
                            caseHistory.caseHistoryId.between(
                                Long.valueOf(details.getValue().getStartingValue()),
                                Long.valueOf(details.getValue().getEndingValue())
                            )
                        );
                    } else if (StringUtils.isNumeric(details.getValue().getStartingValue())) {
                        builder.and(caseHistory.caseHistoryId.goe(Long.valueOf(details.getValue().getStartingValue())));
                    } else if (StringUtils.isNumeric(details.getValue().getEndingValue())) {
                        builder.and(caseHistory.caseHistoryId.loe(Long.valueOf(details.getValue().getEndingValue())));
                    }
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("message")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(caseHistory.message.likeIgnoreCase("%" + details.getValue().getSearchValue() + "%"));
                } else if (details.getValue().getOperator().equals("equals")) {
                    builder.and(caseHistory.message.eq(details.getValue().getSearchValue()));
                } else if (details.getValue().getOperator().equals("notEqual")) {
                    builder.and(caseHistory.message.ne(details.getValue().getSearchValue()));
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("timestamp")) {
                if (
                    details.getValue().getOperator().equals("equals") &&
                    SearchUtils.stringToOffsetTime(details.getValue().getSearchValue()) != null
                ) {
                    builder.and(
                        caseHistory.timestamp.eq(SearchUtils.stringToOffsetTime(details.getValue().getSearchValue()))
                    );
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    SearchUtils.stringToOffsetTime(details.getValue().getSearchValue()) != null
                ) {
                    builder.and(
                        caseHistory.timestamp.ne(SearchUtils.stringToOffsetTime(details.getValue().getSearchValue()))
                    );
                } else if (details.getValue().getOperator().equals("range")) {
                    OffsetTime startOffsetTime = SearchUtils.stringToOffsetTime(details.getValue().getStartingValue());
                    OffsetTime endOffsetTime = SearchUtils.stringToOffsetTime(details.getValue().getEndingValue());
                    if (startOffsetTime != null && endOffsetTime != null) {
                        builder.and(caseHistory.timestamp.between(startOffsetTime, endOffsetTime));
                    } else if (endOffsetTime != null) {
                        builder.and(caseHistory.timestamp.loe(endOffsetTime));
                    } else if (startOffsetTime != null) {
                        builder.and(caseHistory.timestamp.goe(startOffsetTime));
                    }
                }
            }

            if (details.getKey().replace("%20", "").trim().equals("cases")) {
                if (
                    details.getValue().getOperator().equals("contains") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(caseHistory.cases.caseId.like(details.getValue().getSearchValue() + "%"));
                } else if (
                    details.getValue().getOperator().equals("equals") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(caseHistory.cases.caseId.eq(Long.valueOf(details.getValue().getSearchValue())));
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(caseHistory.cases.caseId.ne(Long.valueOf(details.getValue().getSearchValue())));
                } else if (details.getValue().getOperator().equals("range")) {
                    if (
                        StringUtils.isNumeric(details.getValue().getStartingValue()) &&
                        StringUtils.isNumeric(details.getValue().getEndingValue())
                    ) {
                        builder.and(
                            caseHistory.cases.caseId.between(
                                Long.valueOf(details.getValue().getStartingValue()),
                                Long.valueOf(details.getValue().getEndingValue())
                            )
                        );
                    } else if (StringUtils.isNumeric(details.getValue().getStartingValue())) {
                        builder.and(caseHistory.cases.caseId.goe(Long.valueOf(details.getValue().getStartingValue())));
                    } else if (StringUtils.isNumeric(details.getValue().getEndingValue())) {
                        builder.and(caseHistory.cases.caseId.loe(Long.valueOf(details.getValue().getEndingValue())));
                    }
                }
            }
        }

        for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
            if (joinCol != null && joinCol.getKey().equals("caseId")) {
                builder.and(caseHistory.cases.caseId.eq(Long.parseLong(joinCol.getValue())));
            }
        }
        return builder;
    }
}
