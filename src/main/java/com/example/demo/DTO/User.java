package com.example.demo.DTO;

import com.example.demo.annotations.BirthDateConstraint;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.Date;

@Builder
public record User(
        Long id,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,


        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date must be in the past")
        @BirthDateConstraint
        Date birthDate,

        String address,

        @Pattern(regexp = "^(?:\\+?380|\\(?0)[\\d]{9}$||^$", message = "Phone number start with \"+380\", \"380\" or \"0\" and contain 9 digits after 0")
        String phoneNumber
) {
}
