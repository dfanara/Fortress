package me.fanara.fortress.server.progs;

import me.fanara.fortress.hybrid.packet.Packet;
import me.fanara.fortress.server.Client;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class Program {

    private byte[] packetIds;

    public Program(byte[] packetIds) {
        this.packetIds = packetIds;
    }

    public abstract void handlePacket(Client client, byte packetId, DataInputStream dis) throws IOException;
    public abstract boolean run();

    public boolean handlesPacket(Packet packet) {
        for(byte b : packetIds)
            if(packet.getId() == b)
                return true;
        return false;
    }

    public boolean handlesPacket(byte by) {
        for(byte b : packetIds)
            if(by == b)
                return true;
        return false;
    }

}
