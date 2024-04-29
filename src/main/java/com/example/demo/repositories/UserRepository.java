package com.example.demo.repositories;

import com.example.demo.Entities.DbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<DbUser, Long> {
    @Query(value = "SELECT * FROM users WHERE birth_date BETWEEN ?1 AND ?2", nativeQuery = true)
    List<DbUser> findByBirthDateBetween(Date from, Date to);

    Optional<DbUser> findByEmail(String email);
}
