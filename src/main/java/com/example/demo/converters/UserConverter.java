package com.example.demo.converters;

import com.example.demo.DTO.User;
import com.example.demo.Entities.DbUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserConverter {
    User fromDbToDto(DbUser user);
    DbUser fromDtoToDb(User user);
}
