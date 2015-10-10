package me.fanara.fortress.hybrid.packet.fibonnaci;

import me.fanara.fortress.hybrid.packet.Packet;
import me.fanara.fortress.server.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FibonacciResponsePacket extends Packet {

    private long n;
    private long fib;

    public FibonacciResponsePacket(long n, long fib) {
        super((byte) 0xA3);
        this.n = n;
        this.fib = fib;
    }

    @Override
    public void handle() {
        //C->S.
    }

    @Override
    public void handleServer(Client client) {
        //Disregard, captrued by a program.
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeLong(n);
        dos.writeLong(fib);
        dos.flush();
    }

    @Override
    public void create(DataInputStream dis) throws IOException {
        n = dis.readLong();
        fib = dis.readLong();
    }

    public long getN() {
        return this.n;
    }

    public long getFibonacci() {
        return this.fib;
    }
}
