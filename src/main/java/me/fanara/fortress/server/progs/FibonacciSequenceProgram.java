package me.fanara.fortress.server.progs;

import me.fanara.fortress.hybrid.packet.fibonnaci.FibonacciRequestPacket;
import me.fanara.fortress.hybrid.packet.fibonnaci.FibonacciResponsePacket;
import me.fanara.fortress.server.Client;
import me.fanara.fortress.server.FortressServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FibonacciSequenceProgram extends Program {

    private int min, max;
    private ArrayList<Client> clients = FortressServer.getInstance().getNetworkHandler().getClients();
    private HashMap<Client, Boolean> busyMap = new HashMap<>(); //True if busy
    private HashMap<Long, Long> fibs = new HashMap<>();
    private int index = 0;
    private int fibIndex;

    public FibonacciSequenceProgram(int min, int max) {
        super(new byte[] {(byte) 0xA3});
        this.min = min;
        this.max = max;
        this.fibIndex = min;

        for(Client c : clients)
            busyMap.put(c, false);
    }

    @Override
    public void handlePacket(Client client, byte packetId, DataInputStream dis) throws IOException {
        busyMap.put(client, false);
        FibonacciResponsePacket packet = new FibonacciResponsePacket(0, 0);
        packet.create(dis);

        fibs.put(packet.getN(), packet.getFibonacci());
    }

    @Override
    public boolean run() {
        if(fibs.size() < max - min + 1 && fibIndex != max + 1) {
            if(index == clients.size())
                index = 0;

            Client client = clients.get(index);
            if(busyMap.get(client)) {
                //Busy
                index++;
            }else {
                if(client != null) {
                    client.sendPacket(new FibonacciRequestPacket(fibIndex));
                    fibIndex++;
                    busyMap.put(client, true);
                }
                index++;
            }
        }else if(fibs.size() == max - min + 1){
            System.out.println("Fibonacci sequence calculated for n " + min + " - " + max);
            for(int i = min; i <= max; i++) {
                System.out.println(i + " - " + fibs.get((long) i));
            }
            return false;
        }
        return true;
    }
}
