package com.rusefi.pcb;

import java.util.HashMap;
import java.util.Map;

public class Networks {
    /**
     * Net name > Net Id
     */
    private final Map<String, Integer> networks = new HashMap<>();
    private final Map<Integer, String> nameById = new HashMap<>();

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
        return name.startsWith("N-00");
    }

    private void registerNet(String name) {
        networks.put(name, networks.size());
        nameById.put(networks.get(name), name);
    }

    public int getId(String localName) {
        Integer value = networks.get(localName);
        if (value == null)
            throw new NullPointerException("No id for " + localName);
        return value;
    }

    public String getNameById(int networkId) {
        return nameById.get(networkId);
    }
}
