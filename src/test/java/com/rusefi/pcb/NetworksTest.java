package com.rusefi.pcb;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NetworksTest {

    private static final String GND = "GND";

    @Test(expected = NullPointerException.class)
    public void testNonExistent() {
        PcbMergeTool.Networks n = new PcbMergeTool.Networks();

        n.getId(GND);
    }

    @Test
    public void testRegister() {
        PcbMergeTool.Networks n = new PcbMergeTool.Networks();

        n.registerNetworkIfPcbSpecific(GND);
        assertEquals(0, n.getId(GND));

        n.registerNetworkIfPcbSpecific(GND);
        assertEquals(0, n.getId(GND));
    }

    @Test
    public void testLocalNetwork() {
        PcbMergeTool.Networks n = new PcbMergeTool.Networks();
        n.registerNetworkIfPcbSpecific(GND);

        String newName = n.registerNetworkIfPcbSpecific("N-00239");
        assertEquals("F-00001", newName);
        assertEquals(1, n.getId(newName));
    }

}
