package com.example.demo.exceptions;

import java.util.Date;

public class DatesAreWrongException extends RuntimeException {


    public DatesAreWrongException(Date from, Date to) {
        super("Date 'to' " + to + "should be after date 'from' " + from);
    }
}
