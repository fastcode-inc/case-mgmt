package com.fastcode.example.domain.core.person;

import com.fastcode.example.domain.core.abstractentity.AbstractEntity;
import com.fastcode.example.domain.core.authorization.users.Users;
import com.fastcode.example.domain.core.personcase.PersonCase;
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
@Config(defaultVariableName = "personEntity")
@Table(name = "person")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@TypeDefs({})
public class Person extends AbstractEntity {

    @Basic
    @Column(name = "birth_date", nullable = true)
    private LocalDate birthDate;

    @Basic
    @Column(name = "comments", nullable = true)
    private String comments;

    @Basic
    @Column(name = "given_name", nullable = true)
    private String givenName;

    @Basic
    @Column(name = "home_phone", nullable = true)
    private String homePhone;

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "person_id", nullable = false)
    private Long personId;

    @Basic
    @Column(name = "surname", nullable = true)
    private String surname;

    @ShallowReference
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonCase> personCasesSet = new HashSet<PersonCase>();

    public void addPersonCases(PersonCase personCases) {
        personCasesSet.add(personCases);

        personCases.setPerson(this);
    }

    public void removePersonCases(PersonCase personCases) {
        personCasesSet.remove(personCases);

        personCases.setPerson(null);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private Users users;
}
