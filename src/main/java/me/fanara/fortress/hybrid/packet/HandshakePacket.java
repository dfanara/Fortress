package me.fanara.fortress.hybrid.packet;

import me.fanara.fortress.hybrid.IOUtil;
import me.fanara.fortress.server.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class HandshakePacket extends Packet {

    private String name;

    public HandshakePacket(String name) {
        super((byte) 0x00);
        this.name = name;
    }

    /**
     * Do not handle this packet on the client end.
     * One way packet to server.
     */
    @Override
    public void handle() {

    }

    @Override
    public void handleServer(Client client) {
        System.out.println("Adding client named: " + name);
        //TODO: Get client information and set display name. Respond with an accept / deny packet
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        IOUtil.writeString(dos, this.name);
    }

    @Override
    public void create(DataInputStream dis) throws IOException {
        name = IOUtil.readString(dis);
    }
}
