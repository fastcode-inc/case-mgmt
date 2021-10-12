package com.fastcode.example.application.extended.casehistory;

import com.fastcode.example.application.core.casehistory.ICaseHistoryMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICaseHistoryMapperExtended extends ICaseHistoryMapper {}
