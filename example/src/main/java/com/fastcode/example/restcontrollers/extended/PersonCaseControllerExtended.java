package com.fastcode.example.restcontrollers.extended;

import com.fastcode.example.application.extended.cases.ICasesAppServiceExtended;
import com.fastcode.example.application.extended.person.IPersonAppServiceExtended;
import com.fastcode.example.application.extended.personcase.IPersonCaseAppServiceExtended;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.restcontrollers.core.PersonCaseController;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/personCase/extended")
public class PersonCaseControllerExtended extends PersonCaseController {

    public PersonCaseControllerExtended(
        IPersonCaseAppServiceExtended personCaseAppServiceExtended,
        ICasesAppServiceExtended casesAppServiceExtended,
        IPersonAppServiceExtended personAppServiceExtended,
        LoggingHelper helper,
        Environment env
    ) {
        super(personCaseAppServiceExtended, casesAppServiceExtended, personAppServiceExtended, helper, env);
    }
    //Add your custom code here

}
