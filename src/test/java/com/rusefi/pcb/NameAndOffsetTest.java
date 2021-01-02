package com.rusefi.pcb;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NameAndOffsetTest {
    @Test
    public void test() {
        NameAndOffset n = NameAndOffset.parseNameAndOffset("1 2 3");
        assertEquals("1", n.getName());
    }
}
