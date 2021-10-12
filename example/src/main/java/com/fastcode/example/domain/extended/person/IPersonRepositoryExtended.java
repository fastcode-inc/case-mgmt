package com.fastcode.example.domain.extended.person;

import com.fastcode.example.domain.core.person.IPersonRepository;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.stereotype.Repository;

@JaversSpringDataAuditable
@Repository("personRepositoryExtended")
public interface IPersonRepositoryExtended extends IPersonRepository {
    //Add your custom code here
}
