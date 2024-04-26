package com.example.demo;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
class BirthDateValidator  {}

//public class BirthDateValidator implements ConstraintValidator<BirthDateConstraint, String> {
//
//    @Value("${user.minAge}")
//    int minAge;
//
//    @Override
//    public void initialize(BirthDateConstraint birthDate) {
//    }
//
//    @Override
//    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
//        return false;
//    }
//
//
//    public boolean isValid(Date birthDate,
//                           ConstraintValidatorContext cxt) {
//        LocalDate today = LocalDate.now();
//        LocalDate birthDateLocal = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        Period period = Period.between(birthDateLocal, today);
//
//        return birthDate != null && period.getYears() >= minAge;
//    }

