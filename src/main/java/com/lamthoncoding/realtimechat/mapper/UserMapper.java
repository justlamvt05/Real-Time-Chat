package com.lamthoncoding.realtimechat.mapper;


import com.lamthoncoding.realtimechat.dto.UserDto;
import com.lamthoncoding.realtimechat.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}