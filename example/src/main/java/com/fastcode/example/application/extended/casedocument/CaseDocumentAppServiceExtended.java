package com.fastcode.example.application.extended.casedocument;

import com.fastcode.example.addons.docmgmt.domain.file.IFileRepository;
import com.fastcode.example.application.core.casedocument.CaseDocumentAppService;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.domain.extended.casedocument.ICaseDocumentRepositoryExtended;
import com.fastcode.example.domain.extended.cases.ICasesRepositoryExtended;

import lombok.NonNull;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("caseDocumentAppServiceExtended")
public class CaseDocumentAppServiceExtended extends CaseDocumentAppService implements ICaseDocumentAppServiceExtended {

    public CaseDocumentAppServiceExtended(
        ICaseDocumentRepositoryExtended caseDocumentRepositoryExtended,
        ICasesRepositoryExtended casesRepositoryExtended,
        IFileRepository fileRepository,
        ICaseDocumentMapperExtended mapper,
        LoggingHelper logHelper
    ) {
        super(caseDocumentRepositoryExtended, casesRepositoryExtended, fileRepository, mapper, logHelper);
    }
    //Add your custom code here

}
