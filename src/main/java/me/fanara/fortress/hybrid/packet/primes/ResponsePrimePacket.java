package me.fanara.fortress.hybrid.packet.primes;

import me.fanara.fortress.hybrid.IOUtil;
import me.fanara.fortress.hybrid.packet.Packet;
import me.fanara.fortress.server.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ResponsePrimePacket extends Packet {

    private long long1;
    private long long2;
    private boolean prime1;
    private boolean prime2;

    public ResponsePrimePacket(long l1, boolean prime1, long l2, boolean prime2) {
        super((byte) 0xA1);
        this.long1 = l1;
        this.long2 = l2;
        this.prime1 = prime1;
        this.prime2 = prime2;
    }

    @Override
    public void handle() {
        //Only received by server. Disregard.
    }

    @Override
    public void handleServer(Client client) {

    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeLong(long1);
        IOUtil.writeBoolean(dos, prime1);
        dos.writeLong(long2);
        IOUtil.writeBoolean(dos, prime2);
        dos.flush();
    }

    @Override
    public void create(DataInputStream dis) throws IOException {
        long1 = dis.readLong();
        prime1 = IOUtil.readBoolean(dis);
        long2 = dis.readLong();
        prime2 = IOUtil.readBoolean(dis);
    }

    public long getLong1() {
        return this.long1;
    }

    public long getLong2() {
        return this.long2;
    }

    public boolean isLong1Prime() {
        return prime1;
    }

    public boolean isLong2Prime() {
        return prime2;
    }

}
