package com.rusefi.pcb;

import com.rusefi.pcb.nodes.PcbNode;

import java.io.IOException;

/**
 * @author Andrey Belomutskiy
 *         1/24/14
 */
public class PcbCopyTool {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Two parameters expected: SOURCE DESTINATION");
            return;
        }

        ChangesModel.readConfiguration("pcb_merge_changes.txt");

        String input = args[0];
        String output = args[1];

        PcbNode.copy(input, output);
    }

}
