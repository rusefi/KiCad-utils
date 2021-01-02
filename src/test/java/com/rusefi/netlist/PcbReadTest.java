package com.rusefi.netlist;

import com.rusefi.pcb.nodes.PcbNode;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PcbReadTest {
    @Test
    public void read80() throws IOException {
        PcbNode pcbNode = PcbNode.readFromFile("pcb_read/Breakout_80pin_1393476-Connector.kicad_pcb");
        assertEquals(575, pcbNode.children.size());
    }

    @Test
    @Ignore
    public void testProteus() throws IOException {
        PcbNode pcbNode = PcbNode.readFromFile("pcb_read/proteus.kicad_pcb");
    }
}
