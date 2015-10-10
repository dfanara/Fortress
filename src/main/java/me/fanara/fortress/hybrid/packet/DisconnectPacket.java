package me.fanara.fortress.hybrid.packet;

import me.fanara.fortress.hybrid.IOUtil;
import me.fanara.fortress.server.Client;
import me.fanara.fortress.server.FortressServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DisconnectPacket extends Packet {

    private String message;

    public DisconnectPacket(String message) {
        super((byte) 0x02);
    }

    @Override
    public void handle() {
        System.out.println("Disconnected by server for reason '" + message + "'");
        System.exit(0);
    }

    @Override
    public void handleServer(Client client) {
        //Only sent to client to disconnect.
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        IOUtil.writeString(dos, message);
    }

    @Override
    public void create(DataInputStream dis) throws IOException {
        message = IOUtil.readString(dis);
    }
}
