package com.fastcode.example.application.extended.personcase;

import com.fastcode.example.application.core.personcase.IPersonCaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IPersonCaseMapperExtended extends IPersonCaseMapper {}
