package me.fanara.fortress.hybrid.packet;

import me.fanara.fortress.client.FortressClient;
import me.fanara.fortress.hybrid.IOUtil;
import me.fanara.fortress.server.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class HandshakeResponsePacket extends Packet {

    private boolean accepted;
    private String message;

    public HandshakeResponsePacket(boolean accepted, String message) {
        super((byte) 0x01);
    }

    @Override
    public void handle() {
        //TODO Check if accepted, else disconnect.
        if(accepted) {
            FortressClient.getInstance().handshaked = true;
        }else {
            System.out.println("Handshake rejected... ('" + message + "')");
            System.exit(0);
        }
    }

    @Override
    public void handleServer(Client client) {
        //No server handling necessary
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        IOUtil.writeBoolean(dos, accepted);
        IOUtil.writeString(dos, message == null ? "" : message);
    }

    @Override
    public void create(DataInputStream dis) throws IOException {
        accepted = IOUtil.readBoolean(dis);
        message = IOUtil.readString(dis);
    }
}
