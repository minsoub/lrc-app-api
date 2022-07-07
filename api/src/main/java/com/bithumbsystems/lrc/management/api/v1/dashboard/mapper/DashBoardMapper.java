package com.bithumbsystems.lrc.management.api.v1.dashboard.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DashBoardMapper {

    DashBoardMapper INSTANCE = Mappers.getMapper(DashBoardMapper.class);
}
