package com.fastcode.example.domain.extended.personcase;

import com.fastcode.example.domain.core.personcase.IPersonCaseRepository;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.stereotype.Repository;

@JaversSpringDataAuditable
@Repository("personCaseRepositoryExtended")
public interface IPersonCaseRepositoryExtended extends IPersonCaseRepository {
    //Add your custom code here
}
