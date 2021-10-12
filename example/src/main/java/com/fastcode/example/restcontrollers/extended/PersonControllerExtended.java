package com.fastcode.example.restcontrollers.extended;

import com.fastcode.example.application.extended.authorization.users.IUsersAppServiceExtended;
import com.fastcode.example.application.extended.person.IPersonAppServiceExtended;
import com.fastcode.example.application.extended.personcase.IPersonCaseAppServiceExtended;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.restcontrollers.core.PersonController;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person/extended")
public class PersonControllerExtended extends PersonController {

    public PersonControllerExtended(
        IPersonAppServiceExtended personAppServiceExtended,
        IPersonCaseAppServiceExtended personCaseAppServiceExtended,
        IUsersAppServiceExtended usersAppServiceExtended,
        LoggingHelper helper,
        Environment env
    ) {
        super(personAppServiceExtended, personCaseAppServiceExtended, usersAppServiceExtended, helper, env);
    }
    //Add your custom code here

}
