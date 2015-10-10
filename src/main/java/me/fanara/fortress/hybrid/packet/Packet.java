package me.fanara.fortress.hybrid.packet;

import me.fanara.fortress.server.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Packet {

    protected byte id;

    public Packet(byte id) {
        this.id = id;
    }

    public abstract void handle();
    public abstract void handleServer(Client client);

    /**
     * Write packet to DOS. **Write packet identifier before calling .write.
     * @param dos DataOutputStream of socket
     * @throws IOException RIP.
     */
    public abstract void write(DataOutputStream dos) throws IOException;
    public abstract void create(DataInputStream dis) throws IOException;

    public byte getId() {
        return this.id;
    }
}
