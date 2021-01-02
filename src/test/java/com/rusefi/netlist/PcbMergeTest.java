package com.rusefi.netlist;

import com.rusefi.pcb.PcbMergeTool;
import org.junit.Test;

import java.io.IOException;

public class PcbMergeTest {
    @Test
    public void test() throws IOException {
        PcbMergeTool.main(new String[]{"pcb/test.kicad_pcb",
                "pcb/out.kicad_pcb",
                "pcb/changes.txt"});
    }
}
