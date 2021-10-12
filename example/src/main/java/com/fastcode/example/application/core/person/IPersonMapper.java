package com.fastcode.example.application.core.person;

import com.fastcode.example.application.core.person.dto.*;
import com.fastcode.example.domain.core.authorization.users.Users;
import com.fastcode.example.domain.core.person.Person;
import java.time.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface IPersonMapper {
    Person createPersonInputToPerson(CreatePersonInput personDto);

    @Mappings(
        {
            @Mapping(source = "entity.users.username", target = "username"),
            @Mapping(source = "entity.users.username", target = "usersDescriptiveField"),
        }
    )
    CreatePersonOutput personToCreatePersonOutput(Person entity);

    Person updatePersonInputToPerson(UpdatePersonInput personDto);

    @Mappings(
        {
            @Mapping(source = "entity.users.username", target = "username"),
            @Mapping(source = "entity.users.username", target = "usersDescriptiveField"),
        }
    )
    UpdatePersonOutput personToUpdatePersonOutput(Person entity);

    @Mappings(
        {
            @Mapping(source = "entity.users.username", target = "username"),
            @Mapping(source = "entity.users.username", target = "usersDescriptiveField"),
        }
    )
    FindPersonByIdOutput personToFindPersonByIdOutput(Person entity);

    @Mappings({ @Mapping(source = "foundPerson.personId", target = "personPersonId") })
    GetUsersOutput usersToGetUsersOutput(Users users, Person foundPerson);
}
