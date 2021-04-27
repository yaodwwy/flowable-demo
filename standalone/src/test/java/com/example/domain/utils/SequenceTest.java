package com.example.domain.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;

class SequenceTest {

    SequenceUtil sequence;

    @BeforeEach
    void setUp() {
        sequence = new SequenceUtil(0, 0);
    }

    @Test
    void getId() {
        long l = sequence.nextId();
        System.out.println(l);
        String s = Base64Utils.encodeToString(String.valueOf(l).getBytes());
        System.out.println(s);
    }
}