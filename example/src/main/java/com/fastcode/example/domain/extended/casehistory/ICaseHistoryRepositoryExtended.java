package com.fastcode.example.domain.extended.casehistory;

import com.fastcode.example.domain.core.casehistory.ICaseHistoryRepository;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.stereotype.Repository;

@JaversSpringDataAuditable
@Repository("caseHistoryRepositoryExtended")
public interface ICaseHistoryRepositoryExtended extends ICaseHistoryRepository {
    //Add your custom code here
}
