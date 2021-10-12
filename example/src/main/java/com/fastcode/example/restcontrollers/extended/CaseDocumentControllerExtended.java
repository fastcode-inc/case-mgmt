package com.fastcode.example.restcontrollers.extended;

import com.fastcode.example.application.extended.casedocument.ICaseDocumentAppServiceExtended;
import com.fastcode.example.application.extended.cases.ICasesAppServiceExtended;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.restcontrollers.core.CaseDocumentController;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/caseDocument/extended")
public class CaseDocumentControllerExtended extends CaseDocumentController {

    public CaseDocumentControllerExtended(
        ICaseDocumentAppServiceExtended caseDocumentAppServiceExtended,
        ICasesAppServiceExtended casesAppServiceExtended,
        LoggingHelper helper,
        Environment env
    ) {
        super(caseDocumentAppServiceExtended, casesAppServiceExtended, helper, env);
    }
    //Add your custom code here

}
