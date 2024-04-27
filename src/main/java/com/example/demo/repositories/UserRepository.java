package com.example.demo.repositories;

import com.example.demo.Entities.DbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<DbUser, Long> {
    List<DbUser> findByBirthDateBetween(Date from, Date to);

    Optional<DbUser> findByEmail(String email);
}
