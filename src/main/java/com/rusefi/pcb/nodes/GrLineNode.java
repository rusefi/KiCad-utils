package com.rusefi.pcb.nodes;

import java.util.List;

/**
 * @author Andrey Belomutskiy
 * 2/7/14.
 */
public class GrLineNode extends PcbNode {
    public final LayerNode layerNode;

    public GrLineNode(int i, List<Object> children) {
        super(TOKEN_GR_LINE, i, children);
        layerNode = find("layer");
    }
}
