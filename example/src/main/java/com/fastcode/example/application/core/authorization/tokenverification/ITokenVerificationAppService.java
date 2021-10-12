package com.fastcode.example.application.core.authorization.tokenverification;

import com.fastcode.example.domain.core.authorization.tokenverification.Tokenverification;

public interface ITokenVerificationAppService {
    Tokenverification findByTokenAndType(String token, String type);

    Tokenverification generateToken(String type, String usersId);

    Tokenverification findByUsersIdAndType(String usersId, String type);

    void deleteToken(Tokenverification entity);
}
