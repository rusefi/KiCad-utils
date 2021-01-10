package com.rusefi.pcb;

import com.rusefi.pcb.nodes.*;

import java.io.IOException;
import java.util.List;

import static com.rusefi.pcb.nodes.PcbNode.*;

/**
 * (c) Andrey Belomutskiy
 * 12/16/13.
 */
public class PcbMergeTool {
    public static final String TOKEN_GR_ARC = "gr_arc";
    public static final String TOKEN_GR_LINE = "gr_line";
    public static final String TOKEN_GR_TEXT = "gr_text";
    public static final String TOKEN_MODULE = "module";

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Three parameters expected: SOURCE_PCB_FILENAME DESTINATION_PCB_FILENAME CHANGES_LIST_FILENAME");
            return;
        }
        String sourcePcb = args[0];
        String destination = args[1];
        String changes = args[2];

        ChangesModel.readConfiguration(changes);
        Networks networks = new Networks();

        log("Running COPY commands");
        for (TwoFileRequest or : ChangesModel.getInstance().COPY_REQUESTS)
            PcbNode.copy(or.input, or.output);

        log("Running OPTIMIZE commands");
        for (TwoFileRequest or : ChangesModel.getInstance().OPTIMIZE_REQUESTS)
            RemoveUnneededTraces.optimize(or.input, or.output);

        PcbNode destNode = PcbNode.readFromFile(sourcePcb);


        for (PcbNode net : destNode.iterate(TOKEN_NET)) {
            String netName = net.getChild(1); // todo: nicer method?
            if (!Networks.isLocalNetwork(netName))
                networks.registerNetworkIfPcbSpecific(netName);
        }

        log("Running ADD commands");
        for (NameAndOffset addRequest : ChangesModel.getInstance().ADD_REQUESTS) {
            PcbNode node = PcbMoveTool.readAndMove(addRequest.getName(), addRequest.x, addRequest.y);

            mergePcb(destNode, node, networks);
        }

        log("Running MOVE commands");
        for (NameAndOffset moveRequest : ChangesModel.getInstance().MOVE_REQUESTS) {
            String moduleName = moveRequest.getName();
            ModuleNode module = findModuleByName(destNode, moduleName);
            if (module == null) {
                log("Module not found: " + moduleName);
                continue;
            }

            PointNode at = module.at;
            at.setLocation(at.x + moveRequest.x, at.y + moveRequest.y);
        }

        removeNodes(destNode);

        destNode.write(destination);

        RemoveUnneededTraces.optimize(destination, destination);
    }

    private static ModuleNode findModuleByName(PcbNode destNode, String moduleName) {
        for (PcbNode node : destNode.iterate("module")) {
            ModuleNode mn = (ModuleNode) node;
            if (moduleName.toLowerCase().equals(mn.getReference().toLowerCase()))
                return mn;
        }
        return null;
    }

    static void mergePcb(PcbNode destNode, PcbNode source, Networks networks) {

        Networks.BoardState state = networks.registerAdditionalBoard(source);

        List<PcbNode> zones = source.iterate(TOKEN_ZONE);
        log("Processing  " + zones.size() + " zone(s)");
        for (PcbNode z : zones) {
            ZoneNode zone = (ZoneNode) z;
            if (zone.getLayerNode().isSilkcreenLayer())
                destNode.addChild(zone);
        }

        List<PcbNode> arcs = source.iterate(TOKEN_GR_ARC);
        log("Processing  " + arcs.size() + " arc(s)");
        for (PcbNode arc : arcs)
            destNode.addChild(arc);


        List<PcbNode> lines = source.iterate(TOKEN_GR_LINE);
        log("Processing  " + lines.size() + " line(s)");
        for (PcbNode l : lines) {
            GrLineNode line = (GrLineNode) l;
            if (line.layerNode.isSilkcreenLayer())
                destNode.addChild(line);
        }


        List<PcbNode> labels = source.iterate(TOKEN_GR_TEXT);
        log("Processing  " + labels.size() + " label(s)");
        for (PcbNode label : labels) {
            destNode.addChild(label);
        }

        List<PcbNode> modules = source.iterate(TOKEN_MODULE);
        log("Processing  " + modules.size() + " module(s)");
        for (PcbNode module : modules) {
            for (PcbNode pad : module.iterate(TOKEN_PAD)) {
                if (!pad.hasChild(TOKEN_NET))
                    continue;
                fixNetId(networks, pad, state);
//                PcbNode net = pad.find("net");
//                String localName = netNameMapping.get(net.getChild(1));
//                net.setString(1, localName);
//                net.setInt(0, networks.getId(localName));
            }
            destNode.addChild(module);
        }

        List<PcbNode> segments = source.iterate(TOKEN_SEGMENT);
        log("Processing " + segments.size() + " segments");
        for (PcbNode segment : segments) {
//            if (!segment.hasChild(TOKEN_NET))
//                continue;
            fixNetId(networks, segment, state);

            destNode.addChild(segment);
        }

        List<PcbNode> pads = source.iterate(TOKEN_PAD);
        log("Processing " + pads.size() + " pads");
        for (PcbNode pad : pads) {
            fixNetId(networks, pad, state);

            destNode.addChild(pad);
        }

        List<PcbNode> vias = source.iterate(TOKEN_VIA);
        log("Processing " + vias.size() + " vias");
        for (PcbNode via : vias) {
            fixNetId(networks, via, state);

            destNode.addChild(via);
        }

//        for (PcbNode zone : source.iterate("zone")) {
//            fixNetId(netIdMapping, zone);
//            destNode.addChild(zone);
//        }
    }

    public static void removeNodes(PcbNode source) {
        for (PcbNode module : source.iterate("module")) {
            if (shouldRemove((ModuleNode) module))
                source.removeChild(module);
        }
    }

    private static boolean shouldRemove(ModuleNode module) {
        for (PcbNode fp_text : module.iterate("fp_text")) {
            if ("reference".equals(fp_text.getChild(0))) {
                String name = fp_text.getChild(1);
                if (ChangesModel.getInstance().DEL_REQUESTS.contains(name))
                    return true;
            }
        }
        return false;
    }

    private static void fixNetId(Networks networks, PcbNode node, Networks.BoardState state) {
        NetNode net = node.find(TOKEN_NET);
        String globalName;
        if (state.netNameInLocalToNetNameInCombined.containsKey(net.nodeName)) {
            globalName = state.netNameInLocalToNetNameInCombined.get(net.nodeName);
        } else {
            globalName = networks.registerNetworkIfPcbSpecific(net.nodeName);
        }


        if (ChangesModel.getInstance().NET_MERGE_REQUESTS.containsKey(globalName)) {
            String newName = ChangesModel.getInstance().NET_MERGE_REQUESTS.get(globalName);
            log("Will merge " + globalName + " into " + newName);
            globalName = newName;
            net.setName(newName);
        }
        net.setId(networks.getId(globalName));
        if (net.getName() != null)
            net.setName(globalName);
    }

    public static void log(String s) {
        System.out.println(s);
    }
}
