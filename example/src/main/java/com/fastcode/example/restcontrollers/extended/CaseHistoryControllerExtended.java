package com.fastcode.example.restcontrollers.extended;

import com.fastcode.example.application.extended.casehistory.ICaseHistoryAppServiceExtended;
import com.fastcode.example.application.extended.cases.ICasesAppServiceExtended;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.restcontrollers.core.CaseHistoryController;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/caseHistory/extended")
public class CaseHistoryControllerExtended extends CaseHistoryController {

    public CaseHistoryControllerExtended(
        ICaseHistoryAppServiceExtended caseHistoryAppServiceExtended,
        ICasesAppServiceExtended casesAppServiceExtended,
        LoggingHelper helper,
        Environment env
    ) {
        super(caseHistoryAppServiceExtended, casesAppServiceExtended, helper, env);
    }
    //Add your custom code here

}
