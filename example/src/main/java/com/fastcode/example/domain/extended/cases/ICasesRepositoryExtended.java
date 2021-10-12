package com.fastcode.example.domain.extended.cases;

import com.fastcode.example.domain.core.cases.ICasesRepository;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.stereotype.Repository;

@JaversSpringDataAuditable
@Repository("casesRepositoryExtended")
public interface ICasesRepositoryExtended extends ICasesRepository {
    //Add your custom code here
}
