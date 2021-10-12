package com.fastcode.example.domain.core.personcase;

import com.fastcode.example.domain.core.abstractentity.AbstractEntity;
import com.fastcode.example.domain.core.cases.Cases;
import com.fastcode.example.domain.core.person.Person;
import com.querydsl.core.annotations.Config;
import java.time.*;
import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TypeDefs;

@Entity
@Config(defaultVariableName = "personCaseEntity")
@Table(name = "person_case")
@IdClass(PersonCaseId.class)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@TypeDefs({})
public class PersonCase extends AbstractEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "person_id", nullable = false)
    private Long personId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", insertable = false, updatable = false)
    private Cases cases;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", insertable = false, updatable = false)
    private Person person;
}
