package me.fanara.fortress.hybrid.packet.fibonnaci;

import me.fanara.fortress.client.FortressClient;
import me.fanara.fortress.hybrid.packet.Packet;
import me.fanara.fortress.server.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FibonacciRequestPacket extends Packet {

    private long n;

    public FibonacciRequestPacket(long n) {
        super((byte) 0xA2);
        this.n = n;
    }

    @Override
    public void handle() {
        long fib = fib(n);
        FortressClient.getInstance().getNetworkHandler().sendPacket(new FibonacciResponsePacket(n, fib));
    }

    @Override
    public void handleServer(Client client) {

    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeLong(n);
    }

    @Override
    public void create(DataInputStream dis) throws IOException {
        n = dis.readLong();
    }

    public long fib(long n) {
        if (n <= 1) return n;
        else return fib(n-1) + fib(n-2);
    }
}
