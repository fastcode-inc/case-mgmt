package com.fastcode.example.domain.core.authorization.tokenverification;

import java.io.Serializable;
import java.time.*;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenverificationId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tokenType;
    private String usersUsername;

    public TokenverificationId(String tokenType, String usersUsername) {
        this.tokenType = tokenType;
        this.usersUsername = usersUsername;
    }
}
