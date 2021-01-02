package com.rusefi.pcb.nodes;

import java.util.List;

/**
 * @author Andrey Belomutskiy
 *         2/7/14.
 */
public class GrLineNode extends PcbNode {
    public final LayerNode layerNode;
    public GrLineNode(String nodeName, int i, List<Object> children) {
        super(nodeName, i, children);
        layerNode = (LayerNode) find("layer");
    }
}
