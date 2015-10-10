package me.fanara.fortress.hybrid.packet;

import me.fanara.fortress.server.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class KeepAlivePacket extends Packet {

    public KeepAlivePacket() {
        super((byte) 0x03);
    }

    @Override
    public void handle() {

    }

    @Override
    public void handleServer(Client client) {
        //Disregard ping packets.
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {

    }

    @Override
    public void create(DataInputStream dis) throws IOException {

    }

}
