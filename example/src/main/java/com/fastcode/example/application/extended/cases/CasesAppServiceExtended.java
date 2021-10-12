package com.fastcode.example.application.extended.cases;

import com.fastcode.example.application.core.cases.CasesAppService;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.domain.extended.cases.ICasesRepositoryExtended;
import org.springframework.stereotype.Service;

@Service("casesAppServiceExtended")
public class CasesAppServiceExtended extends CasesAppService implements ICasesAppServiceExtended {

    public CasesAppServiceExtended(
        ICasesRepositoryExtended casesRepositoryExtended,
        ICasesMapperExtended mapper,
        LoggingHelper logHelper
    ) {
        super(casesRepositoryExtended, mapper, logHelper);
    }
    //Add your custom code here

}
