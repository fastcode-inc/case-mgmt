package com.fastcode.example.application.extended.person;

import com.fastcode.example.application.core.person.PersonAppService;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.domain.extended.authorization.users.IUsersRepositoryExtended;
import com.fastcode.example.domain.extended.person.IPersonRepositoryExtended;
import org.springframework.stereotype.Service;

@Service("personAppServiceExtended")
public class PersonAppServiceExtended extends PersonAppService implements IPersonAppServiceExtended {

    public PersonAppServiceExtended(
        IPersonRepositoryExtended personRepositoryExtended,
        IUsersRepositoryExtended usersRepositoryExtended,
        IPersonMapperExtended mapper,
        LoggingHelper logHelper
    ) {
        super(personRepositoryExtended, usersRepositoryExtended, mapper, logHelper);
    }
    //Add your custom code here

}
