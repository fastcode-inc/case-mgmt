package com.fastcode.example.domain.core.personcase;

import java.io.Serializable;
import java.time.*;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonCaseId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long caseId;
    private Long personId;

    public PersonCaseId(Long caseId, Long personId) {
        this.caseId = caseId;
        this.personId = personId;
    }
}
