package com.fastcode.example.domain.core.casehistory;

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
@Config(defaultVariableName = "caseHistoryEntity")
@Table(name = "case_history")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@TypeDefs({})
public class CaseHistory extends AbstractEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "case_history_id", nullable = false)
    private Long caseHistoryId;

    @Basic
    @Column(name = "message", nullable = true)
    private String message;

    @Basic
    @Column(name = "timestamp", nullable = false)
    private OffsetTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private Cases cases;
}
