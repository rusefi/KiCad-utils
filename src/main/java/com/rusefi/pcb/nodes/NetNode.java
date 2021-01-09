package com.rusefi.pcb.nodes;

import java.util.List;

/**
 * @author Andrey Belomutskiy
 * 1/21/14
 */
public class NetNode extends PcbNode {
    public final String id;
    private String name;
    public static int GND_NET_ID;

    public NetNode(int i, List<Object> children) {
        super(TOKEN_NET, i, children);
        id = getChild(0);
        name = children.size() > 1 ? getChild(1) : null;
        if (name != null)
            System.out.println("NetNode(" + name + " network: " + id + ")");

        if ("GND".equalsIgnoreCase(name))
            GND_NET_ID = Integer.parseInt(id);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "NetNode{" +
                "id='" + id + '\'' +
                '}';
    }
}
