package com.rusefi.pcb.nodes;

import com.rusefi.pcb.nodes.LayerNode;
import com.rusefi.pcb.nodes.PcbNode;

import java.util.List;

/**
 * (c) Andrey Belomutskiy
 * 2/11/14.
 */
public class ZoneNode extends PcbNode {
    private final LayerNode layerNode;

    public ZoneNode(String nodeName, int i, List<Object> children) {
        super(nodeName, i, children);
        layerNode = (LayerNode) find("layer");
    }

    public LayerNode getLayerNode() {
        return layerNode;
    }
}
