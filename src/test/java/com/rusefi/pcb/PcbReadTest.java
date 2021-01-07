package com.rusefi.pcb;

import com.rusefi.pcb.nodes.PcbNode;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PcbReadTest {
    @Test
    public void read80() throws IOException {
        PcbNode pcbNode = PcbNode.readFromFile("pcb_read/Breakout_80pin_1393476-Connector.kicad_pcb");
        assertEquals(575, pcbNode.children.size());
    }

    @Test
    public void testParseKicad4() {
        String string = "(kicad_pcb " +
                "(version 4) (host pcbnew \"(2014-07-21 BZR 5016)-product\")\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "  (net 0 \"\")\n" +
                "  (net 205 \"GNDA\")\n" +
                "\n" +
                "  \n" +
                "  (net_class Default \"This is the default net class.\"\n" +
                "\t(add_net Net1)\n" +
                "  )\n" +
                "  \n" +
                "(gr_line (start 17.24988 -105.76185) (end 15.64980 -105.76185) (angle 90) (layer F.CrtYd) (width 0.2))\n" +
                "(gr_line (start 15.64980 -105.76185) (end 15.64980 -104.93783) (angle 90) (layer F.CrtYd) (width 0.2))\n" +
                "(gr_line (start 15.64980 -104.93783) (end 17.24988 -104.93783) (angle 90) (layer F.CrtYd) (width 0.2))\n" +
                "(gr_line (start 17.24988 -104.93783) (end 17.24988 -105.76185) (angle 90) (layer F.CrtYd) (width 0.2))\n" +
                "(gr_line (start 32.50003 3.80002) (end 111.90002 3.80002) (angle 90) (layer F.CrtYd) (width 0.2))\n" +
                "(gr_line (start 111.90002 3.80002) (end 111.90002 -30.05001) (angle 90) (layer F.CrtYd) (width 0.2))\n" +
                ")";
        PcbNode node = PcbNode.parse(string);
        List<PcbNode> lines = node.iterate("gr_line");
        assertEquals(0, lines.size());
    }

    @Test
    @Ignore
    public void testProteus() throws IOException {
        PcbNode pcbNode = PcbNode.readFromFile("pcb_read/proteus.kicad_pcb");
    }
}
