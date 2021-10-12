package com.fastcode.example.application.extended.person;

import com.fastcode.example.application.core.person.IPersonMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IPersonMapperExtended extends IPersonMapper {}
