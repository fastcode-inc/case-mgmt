package com.fastcode.example.application.core.authorization.users;

import com.fastcode.example.application.core.authorization.users.dto.*;
import com.fastcode.example.domain.core.authorization.users.Users;
import com.fastcode.example.domain.core.authorization.userspreference.Userspreference;
import java.time.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface IUsersMapper {
    Users createUsersInputToUsers(CreateUsersInput usersDto);

    @Mappings({ @Mapping(source = "entity.username", target = "username") })
    CreateUsersOutput usersToCreateUsersOutput(Users entity, Userspreference userPreference);

    @Mappings(
        {
            @Mapping(source = "usersProfile.displayName", target = "displayName"),
            @Mapping(source = "usersProfile.email", target = "email"),
        }
    )
    UpdateUsersInput findUsersWithAllFieldsByIdOutputAndUsersProfileToUpdateUsersInput(
        FindUsersWithAllFieldsByIdOutput users,
        UsersProfile usersProfile
    );

    Users findUsersWithAllFieldsByIdOutputToUsers(FindUsersWithAllFieldsByIdOutput users);

    UsersProfile updateUsersOutputToUsersProfile(UpdateUsersOutput usersDto);

    UsersProfile findUsersByIdOutputToUsersProfile(FindUsersByIdOutput users);

    Users updateUsersInputToUsers(UpdateUsersInput usersDto);

    UpdateUsersOutput usersToUpdateUsersOutput(Users entity);

    @Mappings(
        {
            @Mapping(source = "entity.versiono", target = "versiono"),
            @Mapping(source = "entity.username", target = "username"),
        }
    )
    FindUsersByIdOutput usersToFindUsersByIdOutput(Users entity, Userspreference userPreference);

    FindUsersWithAllFieldsByIdOutput usersToFindUsersWithAllFieldsByIdOutput(Users entity);
    FindUsersByUsernameOutput usersToFindUsersByUsernameOutput(Users entity);
}
