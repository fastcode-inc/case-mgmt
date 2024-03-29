package com.fastcode.example.domain.core.authorization.userspreference;

import com.fastcode.example.domain.core.abstractentity.AbstractEntity;
import com.fastcode.example.domain.core.authorization.users.Users;
import com.querydsl.core.annotations.Config;
import java.time.*;
import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TypeDefs;

@Entity
@Config(defaultVariableName = "userspreferenceEntity")
@Table(name = "userspreference")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@TypeDefs({})
public class Userspreference extends AbstractEntity {

    @Basic
    @Column(name = "language", nullable = false, length = 256)
    private String language;

    @Basic
    @Column(name = "theme", nullable = false, length = 256)
    private String theme;

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "username", nullable = false, length = 255)
    private String username;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private Users users;
}
