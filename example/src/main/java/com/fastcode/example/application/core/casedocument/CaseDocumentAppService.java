package com.fastcode.example.application.core.casedocument;

import com.fastcode.example.addons.docmgmt.domain.file.FileEntity;
import com.fastcode.example.addons.docmgmt.domain.file.IFileRepository;
import com.fastcode.example.application.core.casedocument.dto.*;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.commons.search.*;
import com.fastcode.example.domain.core.casedocument.CaseDocument;
import com.fastcode.example.domain.core.casedocument.CaseDocumentId;
import com.fastcode.example.domain.core.casedocument.ICaseDocumentRepository;
import com.fastcode.example.domain.core.casedocument.QCaseDocument;
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

@Service("caseDocumentAppService")
@RequiredArgsConstructor
public class CaseDocumentAppService implements ICaseDocumentAppService {

    @Qualifier("caseDocumentRepository")
    @NonNull
    protected final ICaseDocumentRepository _caseDocumentRepository;

    @Qualifier("casesRepository")
    @NonNull
    protected final ICasesRepository _casesRepository;
    
    @Qualifier("fileRepository")
    @NonNull
    protected final IFileRepository _fileRepository;

    @Qualifier("ICaseDocumentMapperImpl")
    @NonNull
    protected final ICaseDocumentMapper mapper;

    @NonNull
    protected final LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
    public CreateCaseDocumentOutput create(CreateCaseDocumentInput input) {
        CaseDocument caseDocument = mapper.createCaseDocumentInputToCaseDocument(input);
        Cases foundCases = null;
        if (input.getCaseId() != null) {
            foundCases = _casesRepository.findById(input.getCaseId()).orElse(null);

            if (foundCases != null) {
                foundCases.addCaseDocuments(caseDocument);
                //caseDocument.setCases(foundCases);
            }
        }

        FileEntity foundFile = null;
        if (input.getCaseId() != null) {
        	foundFile = _fileRepository.findById(input.getFileId()).orElse(null);

            if (foundFile != null) {
                caseDocument.setFile(foundFile);
            }
        }

        CaseDocument createdCaseDocument = _caseDocumentRepository.save(caseDocument);
        return mapper.caseDocumentToCreateCaseDocumentOutput(createdCaseDocument);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UpdateCaseDocumentOutput update(CaseDocumentId caseDocumentId, UpdateCaseDocumentInput input) {
        CaseDocument existing = _caseDocumentRepository.findById(caseDocumentId).get();

        CaseDocument caseDocument = mapper.updateCaseDocumentInputToCaseDocument(input);
        Cases foundCases = null;

        if (input.getCaseId() != null) {
            foundCases = _casesRepository.findById(input.getCaseId()).orElse(null);

            if (foundCases != null) {
                foundCases.addCaseDocuments(caseDocument);
                //	caseDocument.setCases(foundCases);
            }
        }

        CaseDocument updatedCaseDocument = _caseDocumentRepository.save(caseDocument);
        return mapper.caseDocumentToUpdateCaseDocumentOutput(updatedCaseDocument);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(CaseDocumentId caseDocumentId) {
        CaseDocument existing = _caseDocumentRepository.findById(caseDocumentId).orElse(null);

        if (existing.getCases() != null) {
            existing.getCases().removeCaseDocuments(existing);
        }
        _caseDocumentRepository.delete(existing);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FindCaseDocumentByIdOutput findById(CaseDocumentId caseDocumentId) {
        CaseDocument foundCaseDocument = _caseDocumentRepository.findById(caseDocumentId).orElse(null);
        if (foundCaseDocument == null) return null;

        return mapper.caseDocumentToFindCaseDocumentByIdOutput(foundCaseDocument);
    }

    //Cases
    // ReST API Call - GET /caseDocument/1/cases
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public GetCasesOutput getCases(CaseDocumentId caseDocumentId) {
        CaseDocument foundCaseDocument = _caseDocumentRepository.findById(caseDocumentId).orElse(null);
        if (foundCaseDocument == null) {
            logHelper.getLogger().error("There does not exist a caseDocument wth a id=%s", caseDocumentId);
            return null;
        }
        Cases re = foundCaseDocument.getCases();
        return mapper.casesToGetCasesOutput(re, foundCaseDocument);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<FindCaseDocumentByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception {
        Page<CaseDocument> foundCaseDocument = _caseDocumentRepository.findAll(search(search), pageable);
        List<CaseDocument> caseDocumentList = foundCaseDocument.getContent();
        Iterator<CaseDocument> caseDocumentIterator = caseDocumentList.iterator();
        List<FindCaseDocumentByIdOutput> output = new ArrayList<>();

        while (caseDocumentIterator.hasNext()) {
            CaseDocument caseDocument = caseDocumentIterator.next();
            output.add(mapper.caseDocumentToFindCaseDocumentByIdOutput(caseDocument));
        }
        return output;
    }

    protected BooleanBuilder search(SearchCriteria search) throws Exception {
        QCaseDocument caseDocument = QCaseDocument.caseDocumentEntity;
        if (search != null) {
            Map<String, SearchFields> map = new HashMap<>();
            for (SearchFields fieldDetails : search.getFields()) {
                map.put(fieldDetails.getFieldName(), fieldDetails);
            }
            List<String> keysList = new ArrayList<String>(map.keySet());
            checkProperties(keysList);
            return searchKeyValuePair(caseDocument, map, search.getJoinColumns());
        }
        return null;
    }

    protected void checkProperties(List<String> list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            if (
                !(
                    list.get(i).replace("%20", "").trim().equals("cases") ||
                    list.get(i).replace("%20", "").trim().equals("caseId") ||
                    list.get(i).replace("%20", "").trim().equals("fileId")
                )
            ) {
                throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!");
            }
        }
    }

    protected BooleanBuilder searchKeyValuePair(
        QCaseDocument caseDocument,
        Map<String, SearchFields> map,
        Map<String, String> joinColumns
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if (details.getKey().replace("%20", "").trim().equals("caseId")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(caseDocument.caseId.like(details.getValue().getSearchValue() + "%"));
                } else if (
                    details.getValue().getOperator().equals("equals") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(caseDocument.caseId.eq(Long.valueOf(details.getValue().getSearchValue())));
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(caseDocument.caseId.ne(Long.valueOf(details.getValue().getSearchValue())));
                } else if (details.getValue().getOperator().equals("range")) {
                    if (
                        StringUtils.isNumeric(details.getValue().getStartingValue()) &&
                        StringUtils.isNumeric(details.getValue().getEndingValue())
                    ) {
                        builder.and(
                            caseDocument.caseId.between(
                                Long.valueOf(details.getValue().getStartingValue()),
                                Long.valueOf(details.getValue().getEndingValue())
                            )
                        );
                    } else if (StringUtils.isNumeric(details.getValue().getStartingValue())) {
                        builder.and(caseDocument.caseId.goe(Long.valueOf(details.getValue().getStartingValue())));
                    } else if (StringUtils.isNumeric(details.getValue().getEndingValue())) {
                        builder.and(caseDocument.caseId.loe(Long.valueOf(details.getValue().getEndingValue())));
                    }
                }
            }
            if (details.getKey().replace("%20", "").trim().equals("fileId")) {
                if (details.getValue().getOperator().equals("contains")) {
                    builder.and(caseDocument.fileId.like(details.getValue().getSearchValue() + "%"));
                } else if (
                    details.getValue().getOperator().equals("equals") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(caseDocument.fileId.eq(Long.valueOf(details.getValue().getSearchValue())));
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(caseDocument.fileId.ne(Long.valueOf(details.getValue().getSearchValue())));
                } else if (details.getValue().getOperator().equals("range")) {
                    if (
                        StringUtils.isNumeric(details.getValue().getStartingValue()) &&
                        StringUtils.isNumeric(details.getValue().getEndingValue())
                    ) {
                        builder.and(
                            caseDocument.fileId.between(
                                Long.valueOf(details.getValue().getStartingValue()),
                                Long.valueOf(details.getValue().getEndingValue())
                            )
                        );
                    } else if (StringUtils.isNumeric(details.getValue().getStartingValue())) {
                        builder.and(caseDocument.fileId.goe(Long.valueOf(details.getValue().getStartingValue())));
                    } else if (StringUtils.isNumeric(details.getValue().getEndingValue())) {
                        builder.and(caseDocument.fileId.loe(Long.valueOf(details.getValue().getEndingValue())));
                    }
                }
            }

            if (details.getKey().replace("%20", "").trim().equals("cases")) {
                if (
                    details.getValue().getOperator().equals("contains") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(caseDocument.cases.caseId.like(details.getValue().getSearchValue() + "%"));
                } else if (
                    details.getValue().getOperator().equals("equals") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(caseDocument.cases.caseId.eq(Long.valueOf(details.getValue().getSearchValue())));
                } else if (
                    details.getValue().getOperator().equals("notEqual") &&
                    StringUtils.isNumeric(details.getValue().getSearchValue())
                ) {
                    builder.and(caseDocument.cases.caseId.ne(Long.valueOf(details.getValue().getSearchValue())));
                } else if (details.getValue().getOperator().equals("range")) {
                    if (
                        StringUtils.isNumeric(details.getValue().getStartingValue()) &&
                        StringUtils.isNumeric(details.getValue().getEndingValue())
                    ) {
                        builder.and(
                            caseDocument.cases.caseId.between(
                                Long.valueOf(details.getValue().getStartingValue()),
                                Long.valueOf(details.getValue().getEndingValue())
                            )
                        );
                    } else if (StringUtils.isNumeric(details.getValue().getStartingValue())) {
                        builder.and(caseDocument.cases.caseId.goe(Long.valueOf(details.getValue().getStartingValue())));
                    } else if (StringUtils.isNumeric(details.getValue().getEndingValue())) {
                        builder.and(caseDocument.cases.caseId.loe(Long.valueOf(details.getValue().getEndingValue())));
                    }
                }
            }
        }

        for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
            if (joinCol != null && joinCol.getKey().equals("caseId")) {
                builder.and(caseDocument.cases.caseId.eq(Long.parseLong(joinCol.getValue())));
            }
        }
        return builder;
    }

    public CaseDocumentId parseCaseDocumentKey(String keysString) {
        String[] keyEntries = keysString.split(",");
        CaseDocumentId caseDocumentId = new CaseDocumentId();

        Map<String, String> keyMap = new HashMap<String, String>();
        if (keyEntries.length > 1) {
            for (String keyEntry : keyEntries) {
                String[] keyEntryArr = keyEntry.split("=");
                if (keyEntryArr.length > 1) {
                    keyMap.put(keyEntryArr[0], keyEntryArr[1]);
                } else {
                    return null;
                }
            }
        } else {
            String[] keyEntryArr = keysString.split("=");
            if (keyEntryArr.length > 1) {
                keyMap.put(keyEntryArr[0], keyEntryArr[1]);
            } else return null;
        }

        caseDocumentId.setCaseId(Long.valueOf(keyMap.get("caseId")));
        caseDocumentId.setFileId(Long.valueOf(keyMap.get("fileId")));
        return caseDocumentId;
    }
}
