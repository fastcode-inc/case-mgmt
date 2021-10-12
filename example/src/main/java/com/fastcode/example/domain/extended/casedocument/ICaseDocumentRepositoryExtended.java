package com.fastcode.example.domain.extended.casedocument;

import com.fastcode.example.domain.core.casedocument.ICaseDocumentRepository;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.stereotype.Repository;

@JaversSpringDataAuditable
@Repository("caseDocumentRepositoryExtended")
public interface ICaseDocumentRepositoryExtended extends ICaseDocumentRepository {
    //Add your custom code here
}
