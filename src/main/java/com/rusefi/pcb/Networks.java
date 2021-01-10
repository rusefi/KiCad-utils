package com.rusefi.pcb;

import com.rusefi.pcb.nodes.NetNode;
import com.rusefi.pcb.nodes.PcbNode;

import java.util.*;

import static com.rusefi.pcb.nodes.PcbNode.TOKEN_NET;

public class Networks {
    /**
     * Net name > Net Id
     */
    private final Map<String, Integer> networks = new HashMap<>();

    /**
     * @param name current name
     * @return new name for local network
     */
    public String registerNetworkIfPcbSpecific(String name) {
        if (isLocalNetwork(name)) {
            String newName = "F-0000" + networks.size();
            PcbMergeTool.log("Board-specific net: " + name + " would be " + newName);

            registerNet(newName);
            int newId = networks.get(newName);
            PcbMergeTool.log(newName + " is " + newId);
            return newName;
        } else {
            if (networks.containsKey(name)) {
                PcbMergeTool.log("Existing global net: " + name);
                return name;
            }

            PcbMergeTool.log("New global net: " + name);
            registerNet(name);
            return name;
        }
    }

    public static boolean isLocalNetwork(String name) {
        return name != null && name.startsWith("N-00");
    }

    private void registerNet(String name) {
        networks.put(name, networks.size());
    }

    public int getId(String localName) {
        if (localName == null)
            throw new NullPointerException("localName");
        Integer value = networks.get(localName);
        if (value == null)
            throw new NullPointerException("No id for " + localName);
        return value;
    }

    public BoardState registerAdditionalBoard(PcbNode destNode, PcbNode source) {
        Set<String> names = new HashSet<>();
        for (NetNode known : destNode.<NetNode>iterate(TOKEN_NET)) {
            names.add(known.getName());
        }

        BoardState result = new BoardState();
        for (NetNode net : source.<NetNode>iterate(TOKEN_NET)) {
            String netId = net.getChild(0);
            String netName = net.getChild(1); // todo: nicer method?

            String newNameInCombinedBoard = registerNetworkIfPcbSpecific(netName);
            result.netNameInLocalToNetNameInCombined.put(netName, newNameInCombinedBoard);
            int id = getId(newNameInCombinedBoard);
            result.netIdMapping.put(netId, id);

            if (!names.contains(newNameInCombinedBoard)) {
                System.out.println("Adding new NET declaration into combined PCB");
                destNode.children.add(new NetNode(0, Arrays.asList("" + id, newNameInCombinedBoard)));
            }
        }


        return result;

    }

    public static class BoardState {
        /**
         * original local net ID (as string) > new net ID
         */
        public Map<String, Integer> netIdMapping = new HashMap<>();
        public Map<String, String> netNameInLocalToNetNameInCombined = new HashMap<>();
    }
}
