package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.user.mapper;

import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.user.model.response.UserResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface User mapper.
 */
@Mapper
public interface UserMapper {
  /**
   * The constant INSTANCE.
   */
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  /**
   * User info touser response user response.
   *
   * @param userInfo the user info
   * @return the user response
   */
  UserResponse userInfoTouserResponse(UserInfo userInfo);
}
