package com.example.demo;

import com.example.demo.annotations.BirthDateConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class BirthDateValidator implements ConstraintValidator<BirthDateConstraint, Date> {

    @Value("${user.minAge}")
    int minAge;

    @Override
    public void initialize(BirthDateConstraint birthDate) {
    }


    @Override
    public boolean isValid(Date birthDate,
                           ConstraintValidatorContext cxt) {
        LocalDate today = LocalDate.now();
        LocalDate birthDateLocal = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(birthDateLocal, today);
        return period.getYears() >= minAge;
    }
}

