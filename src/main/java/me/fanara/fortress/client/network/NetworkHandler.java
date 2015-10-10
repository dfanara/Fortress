package me.fanara.fortress.client.network;

import me.fanara.fortress.client.FortressClient;
import me.fanara.fortress.hybrid.packet.HandshakeResponsePacket;
import me.fanara.fortress.hybrid.packet.Packet;
import me.fanara.fortress.hybrid.packet.fibonnaci.FibonacciRequestPacket;
import me.fanara.fortress.hybrid.packet.primes.RequestPrimePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NetworkHandler extends Thread {

    private DataInputStream dis;
    private DataOutputStream dos;
    private FortressClient client;

    private ArrayList<Packet> toSend = new ArrayList<>();

    public NetworkHandler(FortressClient client) {
        this.client = client;
        try {
            dis = new DataInputStream(client.getSocket().getInputStream());
            dos = new DataOutputStream(client.getSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(client.isRunning()) {
            try {
                if(dis.available() > 0) {
                    readPacket();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(toSend.size() > 0) {
                Packet packet = toSend.get(0);
                System.out.println("Sending packet " + packet.getClass().getSimpleName());
                try {
                    dos.writeByte(packet.getId());
                    packet.write(dos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                toSend.remove(0);
            }

            Thread.yield();
        }
    }

    private void readPacket() throws IOException {
        byte packetId = dis.readByte();
        switch (packetId) {
            case 0x01:
                Packet packet = new HandshakeResponsePacket(false, null);
                packet.create(dis);
                packet.handle();
                break;
            case (byte) 0xA0:
                packet = new RequestPrimePacket(0);
                packet.create(dis);
                packet.handle();
                break;
            case (byte) 0xA2:
                packet = new FibonacciRequestPacket(0);
                packet.create(dis);
                packet.handle();
        }
    }

    public void sendPacket(Packet packet) {
        toSend.add(packet);
    }

}
