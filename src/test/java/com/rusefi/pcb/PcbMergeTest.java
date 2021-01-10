package com.rusefi.pcb;

import com.rusefi.pcb.nodes.NetNode;
import com.rusefi.pcb.nodes.PcbNode;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static com.rusefi.pcb.nodes.PcbNode.TOKEN_NET;
import static com.rusefi.pcb.nodes.PcbNode.TOKEN_PAD;
import static org.junit.Assert.assertEquals;

public class PcbMergeTest {
    @Test
    public void testMerge() throws IOException {
        PcbNode destNode = PcbNode.readFromFile("pcb/test.kicad_pcb");
        assertEquals(605, destNode.children.size());

        PcbNode pcb = PcbNode.readFromFile("pcb/adc_amp_divider.kicad_pcb");
        assertEquals(1036, pcb.children.size());

        PcbMergeTool.mergePcb(destNode, pcb, new Networks());
        assertEquals(1541, destNode.children.size());
    }

    @Test
    public void testMergeNets() {
        String pcb1 = "(kicad_pcb (version 4) (host pcbnew \"(2014-07-21 BZR 5016)-product\")\n" +
                "\n" +
                "  (general\n" +
                "    (zones 0)\n" +
                "    (modules 42)\n" +
                "    (nets 9)\n" +
                "  )\n" +
                "\n" +
                "  (net 0 \"\")\n" +
                "  (net 1 \"Net1\")\n" +
                "  (net 2 \"CAN_VIO\")\n" +
                "  (net 3 \"CAN_RX\")\n" +
                "   (pad \"S1\" smd oval\n" +
                "   (at 1.1 1.1)\n" +
                "\n" +
                "   (size 0.25 0.5)\n" +
                "\n" +
                "   (layers F.Cu F.Mask F.Paste)\n" +
                "\n" +
                "   (net 2 \"CAN_VIO\")\n" +
                ")\n" +
                ")\n";
        PcbNode node = PcbNode.parse(pcb1);
        assertEquals(4, node.iterate(TOKEN_NET).size());

        String pcb2 = "(kicad_pcb (version 4) (host pcbnew \"(2014-07-21 BZR 5016)-product\")\n" +
                "\n" +
                "  (net 0 \"\")\n" +
                "  (net 1 \"Net1\")\n" +
                "  (net 2 \"EXT_SPI_SCK\")\n" +
                "   (pad \"S2\" smd oval\n" +
                "   (at 2.2 2.2)\n" +
                "\n" +
                "   (size 0.25 0.5)\n" +
                "\n" +
                "   (layers F.Cu F.Mask F.Paste)\n" +
                "\n" +
                "   (net 2 \"EXT_SPI_SCK\")\n" +
                ")\n" +
                ")";
        PcbNode node2 = PcbNode.parse(pcb2);
        assertEquals(3, node2.iterate(TOKEN_NET).size());

        PcbMergeTool.mergePcb(node, node2, new Networks());

        List<PcbNode> pads = node.iterate(TOKEN_PAD);
        assertEquals(2, pads.size());
        NetNode net1 = pads.get(0).find(TOKEN_NET);
        NetNode net2 = pads.get(1).find(TOKEN_NET);

        assertEquals("2", net1.getId());
        assertEquals("3", net2.getId());

        assertEquals(4, node.iterate(TOKEN_NET).size());
    }

    @Test
    public void testMergeHellen() throws IOException {
        PcbNode destNode = PcbNode.readFromFile("pcb/hellen1-72-PcbDoc.kicad_pcb");
        assertEquals(4822, destNode.children.size());

        destNode.write("out_hellen.kicad_pcb");

        PcbNode pcb = PcbNode.readFromFile("pcb/adc_amp_divider.kicad_pcb");
        assertEquals(1036, pcb.children.size());

        PcbMergeTool.mergePcb(destNode, pcb, new Networks());
        assertEquals(5758, destNode.children.size());
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
