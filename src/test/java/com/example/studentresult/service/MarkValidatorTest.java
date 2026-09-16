package com.example.studentresult.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarkValidatorTest {

    private final MarkValidator markValidator = new MarkValidator();

    @ParameterizedTest
    @CsvSource({
            "-1,false",
            "0,true",
            "1,true",
            "100,true",
            "101,false"
    })
    void isValidUsesEquivalencePartitionsAndBoundaries(Integer mark, boolean expected) {
        assertEquals(expected, markValidator.isValid(mark));
    }
}
