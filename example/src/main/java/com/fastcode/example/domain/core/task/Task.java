package com.fastcode.example.domain.core.task;

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
@Config(defaultVariableName = "taskEntity")
@Table(name = "task")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@TypeDefs({})
public class Task extends AbstractEntity {

    @Basic
    @Column(name = "message", nullable = true)
    private String message;

    @Basic
    @Column(name = "status", nullable = true)
    private String status;

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Basic
    @Column(name = "type", nullable = true)
    private String type;

    @Basic
    @Column(name = "username", nullable = true)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private Cases cases;
}
