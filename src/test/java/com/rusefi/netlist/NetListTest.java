package com.rusefi.netlist;

import org.junit.Test;

import java.io.IOException;

public class NetListTest {
    @Test
    public void test() throws IOException {
        NetListMerge.main(new String[]{
                "netlist/Breakout_80pin_1393476-Connector.net",
                "netlist/Common_Rail_MC33816.net"
        });
    }
}
