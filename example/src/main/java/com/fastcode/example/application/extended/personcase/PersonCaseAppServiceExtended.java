package com.fastcode.example.application.extended.personcase;

import com.fastcode.example.application.core.personcase.PersonCaseAppService;
import com.fastcode.example.commons.logging.LoggingHelper;
import com.fastcode.example.domain.extended.cases.ICasesRepositoryExtended;
import com.fastcode.example.domain.extended.person.IPersonRepositoryExtended;
import com.fastcode.example.domain.extended.personcase.IPersonCaseRepositoryExtended;
import org.springframework.stereotype.Service;

@Service("personCaseAppServiceExtended")
public class PersonCaseAppServiceExtended extends PersonCaseAppService implements IPersonCaseAppServiceExtended {

    public PersonCaseAppServiceExtended(
        IPersonCaseRepositoryExtended personCaseRepositoryExtended,
        ICasesRepositoryExtended casesRepositoryExtended,
        IPersonRepositoryExtended personRepositoryExtended,
        IPersonCaseMapperExtended mapper,
        LoggingHelper logHelper
    ) {
        super(personCaseRepositoryExtended, casesRepositoryExtended, personRepositoryExtended, mapper, logHelper);
    }
    //Add your custom code here

}
