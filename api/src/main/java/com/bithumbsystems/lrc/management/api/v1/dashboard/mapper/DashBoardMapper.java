package com.bithumbsystems.lrc.management.api.v1.dashboard.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface Dash board mapper.
 */
@Mapper
public interface DashBoardMapper {

  /**
   * The constant INSTANCE.
   */
  DashBoardMapper INSTANCE = Mappers.getMapper(DashBoardMapper.class);
}
