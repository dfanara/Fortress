package me.fanara.fortress.server;

import me.fanara.fortress.hybrid.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    private static final long TIMEOUT = 1000 * 60 * 30; //30 second timeout

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private String name = "unknown";
    private Long lastRead = System.currentTimeMillis();
    private ArrayList<Packet> outboundPackets = new ArrayList<>();
    private double lastTemp = 0.0;

    public Client(Socket socket) {
        this.socket = socket;
        try {
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateLastRead() {
        this.lastRead = System.currentTimeMillis();
    }

    public boolean hasTimedout() {
        return System.currentTimeMillis() - lastRead > TIMEOUT;
    }

    public DataOutputStream getDataOutputStream() {
        return dos;
    }

    public DataInputStream getDataInputStream() {
        return dis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void sendPacket(Packet packet) {
        outboundPackets.add(packet);
    }

    public ArrayList<Packet> getOutboundPackets() {
        return this.outboundPackets;
    }

    public boolean pendingOutboundPackets() {
        return this.outboundPackets.size() > 0;
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getLastTemp() {
        return lastTemp;
    }

    public void setLastTemp(double lastTemp) {
        this.lastTemp = lastTemp;
    }
}
