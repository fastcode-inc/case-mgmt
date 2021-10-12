package com.fastcode.example.domain.core.casedocument;

import com.fastcode.example.domain.core.abstractentity.AbstractEntity;
import com.fastcode.example.domain.core.cases.Cases;
import com.querydsl.core.annotations.Config;
import java.time.*;
import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TypeDefs;

@Entity
@Config(defaultVariableName = "caseDocumentEntity")
@Table(name = "case_document")
@IdClass(CaseDocumentId.class)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@TypeDefs({})
public class CaseDocument extends AbstractEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "file_id", nullable = false)
    private Long fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", insertable = false, updatable = false)
    private Cases cases;
}
