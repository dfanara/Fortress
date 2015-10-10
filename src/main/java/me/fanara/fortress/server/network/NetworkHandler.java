package me.fanara.fortress.server.network;

import me.fanara.fortress.hybrid.packet.HandshakePacket;
import me.fanara.fortress.hybrid.packet.Packet;
import me.fanara.fortress.server.Client;
import me.fanara.fortress.server.FortressServer;
import me.fanara.fortress.server.progs.Program;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NetworkHandler extends Thread {

    private final FortressServer server;
    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<Client> toAdd = new ArrayList<>();
    private ArrayList<Client> toRemove = new ArrayList<>();

    private ArrayList<Packet> toBroadcast = new ArrayList<>();
    private ArrayList<Packet> toAddBroadcast = new ArrayList<>();

    public NetworkHandler(FortressServer server) {
        this.server = server;
    }

    public void run() {
        while(server.isRunning()) {
            for(Client client : clients) {
                try {
                    DataInputStream dis = client.getDataInputStream();
                    DataOutputStream dos = client.getDataOutputStream();
                    if(dis.available() > 0) {
                        readPacket(client, dis);
                        client.updateLastRead();
                    }

                    if(client.pendingOutboundPackets()) {
                        for(Packet packet : client.getOutboundPackets()) {
                            System.out.println("Sending packet " + packet.getClass().getSimpleName());
                            dos.writeByte(packet.getId());
                            packet.write(dos);
                        }
                        client.getOutboundPackets().clear();
                    }

                    if(client.hasTimedout()) {
                        client.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            toBroadcast.addAll(toAddBroadcast);
            toAddBroadcast.clear();

            for(Packet packet : toBroadcast) {
                for(Client client : clients) {
                    client.sendPacket(packet);
                }
            }

            clients.addAll(toAdd);

            for(Client c : toRemove) {
                clients.remove(c);
            }

            toAdd.clear();
            toRemove.clear();
            toBroadcast.clear();
            Thread.yield();
        }
    }

    public void addClient(Client client) {
        toAdd.add(client);
    }

    public void broadcastPacket(Packet packet) {
        toAddBroadcast.add(packet);
    }

    public void readPacket(Client client, DataInputStream dis) throws IOException {
        byte packetId = dis.readByte();
        System.out.println("Received packet ID " + packetId);
        switch (packetId) {
            case 0x00:
                HandshakePacket handshakePacket = new HandshakePacket("");
                handshakePacket.create(dis);
                handshakePacket.handleServer(client);
                break;
            default:
                for(Program program : server.programs) {
                    if(program.handlesPacket(packetId)) {
                        program.handlePacket(client, packetId, dis);
                    }
                }

                break;
        }
    }

    public ArrayList<Client> getClients() {
        return new ArrayList<>(clients);
    }
}
