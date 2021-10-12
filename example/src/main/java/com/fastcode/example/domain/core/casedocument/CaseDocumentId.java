package com.fastcode.example.domain.core.casedocument;

import java.io.Serializable;
import java.time.*;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CaseDocumentId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long caseId;
    private Long fileId;

    public CaseDocumentId(Long caseId, Long fileId) {
        this.caseId = caseId;
        this.fileId = fileId;
    }
}
