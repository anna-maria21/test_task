package com.example.demo.Entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class DbUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String firstName;

    private String lastName;

    private Date birthDate;

    private String address;

    private String phoneNumber;


    public String capitalize(String line) {
        return line.substring(0, 1).toUpperCase() + line.substring(1);
    }

}
