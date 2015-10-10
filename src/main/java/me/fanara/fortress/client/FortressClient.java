package me.fanara.fortress.client;

import me.fanara.fortress.client.network.NetworkHandler;
import me.fanara.fortress.hybrid.packet.HandshakePacket;
import me.fanara.fortress.hybrid.packet.KeepAlivePacket;

import java.io.IOException;
import java.net.Socket;

public class FortressClient {

    private Socket socket;
    private boolean connected = false;
    private boolean running = true;

    public static FortressClient instance;
    public boolean handshaked = false;

    private long lastPing = System.currentTimeMillis();
    private NetworkHandler networkHandler;

    /**
     * Create a FortressClient
     * @param ip
     * @param port
     */
    public FortressClient(String ip, int port, String name) {
        instance = this;
        try {
            connect(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        NetworkHandler nh = new NetworkHandler(this);
        this.networkHandler = nh;
        nh.start();

        nh.sendPacket(new HandshakePacket(name));

        (new Thread(() -> {
            while(isRunning()) {
                if(System.currentTimeMillis() - lastPing > 5000) {
                    nh.sendPacket(new KeepAlivePacket());
                    lastPing = System.currentTimeMillis();
                }
            }
        })).start();


        while(isRunning()) {
            Thread.yield();
        }
    }

    public void connect(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        connected = true;
    }

    public boolean isRunning() {
        return running;
    }

    public Socket getSocket() {
        return socket;
    }

    public static FortressClient getInstance() {
        return instance;
    }

    public NetworkHandler getNetworkHandler() {
        return networkHandler;
    }
}
