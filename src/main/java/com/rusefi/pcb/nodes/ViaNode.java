package com.rusefi.pcb.nodes;

import com.rusefi.pcb.nodes.PcbNode;
import com.rusefi.pcb.nodes.PointNode;
import com.rusefi.pcb.nodes.SizeNode;

import java.util.List;

/**
 * @author Andrey Belomutskiy
 *         1/21/14
 */
public class ViaNode extends PcbNode {
    public final PointNode location;
    final SizeNode size;
    public final int netId;

    public ViaNode(String nodeName, int i, List<Object> children) {
        super(nodeName, i, children);
        location = (PointNode) find("at");
        size = (SizeNode) find("size");
        netId = Integer.parseInt(find("net").getChild(0));
    }

    @Override
    public String toString() {
        return "ViaNode{" +
                "location=" + location +
                '}';
    }

    @Override
    public boolean isConnected(PointNode point) {
        return point.isConnected(location, size);
    }
}
