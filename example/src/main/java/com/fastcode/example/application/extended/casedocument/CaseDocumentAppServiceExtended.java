package com.fastcode.example.application.extended.casedocument;

import com.fastcode.example.application.core.casedocument.CaseDocumentAppService;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.domain.extended.casedocument.ICaseDocumentRepositoryExtended;
import com.fastcode.example.domain.extended.cases.ICasesRepositoryExtended;
import org.springframework.stereotype.Service;

@Service("caseDocumentAppServiceExtended")
public class CaseDocumentAppServiceExtended extends CaseDocumentAppService implements ICaseDocumentAppServiceExtended {

    public CaseDocumentAppServiceExtended(
        ICaseDocumentRepositoryExtended caseDocumentRepositoryExtended,
        ICasesRepositoryExtended casesRepositoryExtended,
        ICaseDocumentMapperExtended mapper,
        LoggingHelper logHelper
    ) {
        super(caseDocumentRepositoryExtended, casesRepositoryExtended, mapper, logHelper);
    }
    //Add your custom code here

}
