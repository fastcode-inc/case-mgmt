package com.fastcode.example.domain.core.authorization.usersrole;

import java.io.Serializable;
import java.time.*;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsersroleId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long roleId;
    private String usersUsername;

    public UsersroleId(Long roleId, String usersUsername) {
        this.roleId = roleId;
        this.usersUsername = usersUsername;
    }
}
