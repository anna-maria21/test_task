package com.example.demo.converters;

import com.example.demo.DTO.User;
import com.example.demo.Entities.DbUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User fromDbToDto(DbUser dbUser) {
        return User.builder()
                .id(dbUser.getId())
                .email(dbUser.getEmail())
                .firstName(dbUser.getFirstName())
                .lastName(dbUser.getLastName())
                .birthDate(dbUser.getBirthDate())
                .address(dbUser.getAddress())
                .phoneNumber(dbUser.getPhoneNumber())
                .build();
    }

    public DbUser fromDtoToDb(User user) {
        return DbUser.builder()
                .id(user.id())
                .email(user.email())
                .firstName(user.firstName())
                .lastName(user.lastName())
                .birthDate(user.birthDate())
                .address(user.address())
                .phoneNumber(user.phoneNumber())
                .build();
    }
}
