package com.fastcode.example.domain.core.cases;

import com.fastcode.example.domain.core.abstractentity.AbstractEntity;
import com.fastcode.example.domain.core.casedocument.CaseDocument;
import com.fastcode.example.domain.core.casehistory.CaseHistory;
import com.fastcode.example.domain.core.personcase.PersonCase;
import com.fastcode.example.domain.core.task.Task;
import com.querydsl.core.annotations.Config;
import java.time.*;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TypeDefs;
import org.javers.core.metamodel.annotation.ShallowReference;

@Entity
@Config(defaultVariableName = "casesEntity")
@Table(name = "cases")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@TypeDefs({})
public class Cases extends AbstractEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Basic
    @Column(name = "status", nullable = true)
    private String status;

    @Basic
    @Column(name = "summary", nullable = true)
    private String summary;

    @Basic
    @Column(name = "type", nullable = true)
    private String type;

    @ShallowReference
    @OneToMany(mappedBy = "cases", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CaseDocument> caseDocumentsSet = new HashSet<CaseDocument>();

    public void addCaseDocuments(CaseDocument caseDocuments) {
        caseDocumentsSet.add(caseDocuments);

        caseDocuments.setCases(this);
    }

    public void removeCaseDocuments(CaseDocument caseDocuments) {
        caseDocumentsSet.remove(caseDocuments);

        caseDocuments.setCases(null);
    }

    @ShallowReference
    @OneToMany(mappedBy = "cases", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CaseHistory> caseHistorysSet = new HashSet<CaseHistory>();

    public void addCaseHistorys(CaseHistory caseHistorys) {
        caseHistorysSet.add(caseHistorys);

        caseHistorys.setCases(this);
    }

    public void removeCaseHistorys(CaseHistory caseHistorys) {
        caseHistorysSet.remove(caseHistorys);

        caseHistorys.setCases(null);
    }

    @ShallowReference
    @OneToMany(mappedBy = "cases", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonCase> personCasesSet = new HashSet<PersonCase>();

    public void addPersonCases(PersonCase personCases) {
        personCasesSet.add(personCases);

        personCases.setCases(this);
    }

    public void removePersonCases(PersonCase personCases) {
        personCasesSet.remove(personCases);

        personCases.setCases(null);
    }

    @ShallowReference
    @OneToMany(mappedBy = "cases", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasksSet = new HashSet<Task>();

    public void addTasks(Task tasks) {
        tasksSet.add(tasks);

        tasks.setCases(this);
    }

    public void removeTasks(Task tasks) {
        tasksSet.remove(tasks);

        tasks.setCases(null);
    }
}
