package com.rusefi.pcb;

import com.rusefi.pcb.nodes.NetNode;
import com.rusefi.pcb.nodes.PcbNode;

import java.util.HashMap;
import java.util.Map;

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

    public BoardState registerAdditionalBoard(PcbNode source) {

        BoardState result = new BoardState();
        for (NetNode net : source.<NetNode>iterate(TOKEN_NET)) {
            String netId = net.getChild(0);
            String netName = net.getChild(1); // todo: nicer method?

            String newNameInCombinedBoard = registerNetworkIfPcbSpecific(netName);
            result.netNameInLocalToNetNameInCombined.put(netName, newNameInCombinedBoard);
            result.netIdMapping.put(netId, getId(newNameInCombinedBoard));
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
