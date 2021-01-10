package com.rusefi.pcb.nodes;

import java.util.List;

/**
 * @author Andrey Belomutskiy
 * 1/21/14
 */
public class NetNode extends PcbNode {
    private int id;
    private String name;
    public static int GND_NET_ID;

    public NetNode(int i, List<Object> children) {
        super(TOKEN_NET, i, children);
        id = Integer.parseInt(getChild(0));
        name = children.size() > 1 ? getChild(1) : null;
// todo: better handling of the whole 'with id no name' drama
        //        if (name == null && id != 1)
//            throw new IllegalArgumentException("Only net '1' has no name: " + id);
        if (name != null)
            System.out.println("NetNode(" + name + " network: " + id + ")");

        if ("GND".equalsIgnoreCase(name))
            GND_NET_ID = id;
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

    public void setId(int id) {
        this.id = id;
        setInt(0, id);
    }

    public void setName(String newName) {
        this.name = newName;
        setString(1, newName);
    }

    public String getId() {
        return "" + id;
    }
}
