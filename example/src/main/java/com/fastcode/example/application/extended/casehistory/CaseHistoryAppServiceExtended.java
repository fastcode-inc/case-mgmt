package com.fastcode.example.application.extended.casehistory;

import com.fastcode.example.application.core.casehistory.CaseHistoryAppService;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.domain.extended.casehistory.ICaseHistoryRepositoryExtended;
import com.fastcode.example.domain.extended.cases.ICasesRepositoryExtended;
import org.springframework.stereotype.Service;

@Service("caseHistoryAppServiceExtended")
public class CaseHistoryAppServiceExtended extends CaseHistoryAppService implements ICaseHistoryAppServiceExtended {

    public CaseHistoryAppServiceExtended(
        ICaseHistoryRepositoryExtended caseHistoryRepositoryExtended,
        ICasesRepositoryExtended casesRepositoryExtended,
        ICaseHistoryMapperExtended mapper,
        LoggingHelper logHelper
    ) {
        super(caseHistoryRepositoryExtended, casesRepositoryExtended, mapper, logHelper);
    }
    //Add your custom code here

}
