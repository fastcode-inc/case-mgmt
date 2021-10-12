package com.fastcode.example.restcontrollers.extended;

import com.fastcode.example.application.extended.casedocument.ICaseDocumentAppServiceExtended;
import com.fastcode.example.application.extended.casehistory.ICaseHistoryAppServiceExtended;
import com.fastcode.example.application.extended.cases.ICasesAppServiceExtended;
import com.fastcode.example.application.extended.personcase.IPersonCaseAppServiceExtended;
import com.fastcode.example.application.extended.task.ITaskAppServiceExtended;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.restcontrollers.core.CasesController;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cases/extended")
public class CasesControllerExtended extends CasesController {

    public CasesControllerExtended(
        ICasesAppServiceExtended casesAppServiceExtended,
        ICaseDocumentAppServiceExtended caseDocumentAppServiceExtended,
        ICaseHistoryAppServiceExtended caseHistoryAppServiceExtended,
        IPersonCaseAppServiceExtended personCaseAppServiceExtended,
        ITaskAppServiceExtended taskAppServiceExtended,
        LoggingHelper helper,
        Environment env
    ) {
        super(
            casesAppServiceExtended,
            caseDocumentAppServiceExtended,
            caseHistoryAppServiceExtended,
            personCaseAppServiceExtended,
            taskAppServiceExtended,
            helper,
            env
        );
    }
    //Add your custom code here

}
