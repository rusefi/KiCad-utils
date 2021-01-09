package com.rusefi.pcb;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NetworksTest {

    private static final String GND = "GND";

    @Test(expected = NullPointerException.class)
    public void testNonExistent() {
        Networks n = new Networks();

        n.getId(GND);
    }

    @Test
    public void testRegister() {
        Networks n = new Networks();

        n.registerNetworkIfPcbSpecific(GND);
        assertEquals(0, n.getId(GND));

        n.registerNetworkIfPcbSpecific(GND);
        assertEquals(0, n.getId(GND));
    }

    @Test
    public void testLocalNetwork() {
        Networks n = new Networks();
        n.registerNetworkIfPcbSpecific(GND);

        String newName = n.registerNetworkIfPcbSpecific("N-00239");
        assertEquals("F-00001", newName);
        assertEquals(1, n.getId(newName));
    }

}
