package com.rusefi.pcb;

import com.rusefi.pcb.nodes.PcbNode;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PcbMergeTest {
    @Test
    public void testMerge() throws IOException {
        PcbNode destNode = PcbNode.readFromFile("pcb/test.kicad_pcb");
        assertEquals(605, destNode.children.size());

        PcbNode pcb = PcbNode.readFromFile("pcb/adc_amp_divider.kicad_pcb");
        assertEquals(1036, pcb.children.size());

        PcbMergeTool.mergePcb(destNode, pcb);
        assertEquals(1541, destNode.children.size());
    }

    @Test
    public void test() throws IOException {
        PcbMergeTool.main(new String[]{"pcb/test.kicad_pcb",
                "out.kicad_pcb",
                "pcb/changes.txt"});
    }

    @Test
    public void testAdc() throws IOException {
        PcbMergeTool.main(new String[]{"pcb/adc_amp_divider.kicad_pcb",
                "out_adc.kicad_pcb",
                "pcb/empty.txt"});
    }

    @Test
    public void testBreakout() throws IOException {
        PcbMergeTool.main(new String[]{"pcb_read/Breakout_80pin_1393476-Connector.kicad_pcb",
                "out_breakout.kicad_pcb",
                "pcb/empty.txt"});
    }

    @Test
    @Ignore
    public void testProteus() throws IOException {
        PcbMergeTool.main(new String[]{"pcb_read/proteus.kicad_pcb",
                "out_proteus.kicad_pcb",
                "pcb/empty.txt"});
    }
}
