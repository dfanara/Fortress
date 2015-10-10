package me.fanara.fortress.server.network;

import me.fanara.fortress.server.Client;
import me.fanara.fortress.server.FortressServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketListener extends Thread {

    private final FortressServer server;

    public SocketListener(FortressServer server) {
        this.server = server;
    }

    public void run() {
        try {
            ServerSocket socket = new ServerSocket(1337);
            while(server.isRunning()) {
                Socket s = socket.accept();
                System.out.println("Adding new Client " + s.getInetAddress().getHostAddress());
                server.getNetworkHandler().addClient(new Client(s));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
