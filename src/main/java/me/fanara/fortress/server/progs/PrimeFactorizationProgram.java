package me.fanara.fortress.server.progs;

import me.fanara.fortress.hybrid.packet.Packet;
import me.fanara.fortress.hybrid.packet.primes.RequestPrimePacket;
import me.fanara.fortress.hybrid.packet.primes.ResponsePrimePacket;
import me.fanara.fortress.server.Client;
import me.fanara.fortress.server.FortressServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PrimeFactorizationProgram extends Program {

    private long originalNumber;
    private ArrayList<Long> notPrime = new ArrayList<>();
    private ArrayList<Long> prime = new ArrayList<>();
    private HashMap<Client, Boolean> busyMap = new HashMap<>(); //Bool set to true if waiting for a response.
    private ArrayList<Client> clients = new ArrayList<>(FortressServer.getInstance().getNetworkHandler().getClients());
    private int index = 0;


    public PrimeFactorizationProgram(long number) {
        super(new byte[] {(byte) 0xA1});
        this.originalNumber = number;
        notPrime.add(number);

        for(Client c : FortressServer.getInstance().getNetworkHandler().getClients()) {
            busyMap.put(c, false);
        }
    }

    @Override
    public void handlePacket(Client client, byte packetId, DataInputStream dis) throws IOException {
        ResponsePrimePacket packet = new ResponsePrimePacket(0, false, 0, false);
        packet.create(dis);
        busyMap.put(client, false);
        System.out.println("Handling prime response " + packet.getLong1() + " " + packet.isLong1Prime() + " " + packet.getLong2() + " " + packet.isLong2Prime());

        if(packet.isLong1Prime()) {
            prime.add(packet.getLong1());
        }else {
            notPrime.add(packet.getLong1());
            notPrime.add(packet.getLong2());
        }
    }

    @Override
    public boolean run() {
        if(notPrime.size() > 0 || prime.size() == 0 || stillRunning()) {
            if(index == clients.size())
                index = 0;

            Client client = clients.get(index);
            if(busyMap.get(client)) {
                //Busy
                index++;
            }else if(notPrime.size() > 0){
                if(client != null && notPrime != null && notPrime.get(0) != null) {
                    client.sendPacket(new RequestPrimePacket(notPrime.get(0)));
                    notPrime.remove(0);
                    busyMap.put(client, true);
                }
                index++;
            }
        }else {
            System.out.println("Prime Factors of " + originalNumber);
            prime.forEach(System.out::println);
            return false;
        }
        return true;
    }

    public boolean stillRunning() {
        for(Client c : busyMap.keySet()) {
            if(busyMap.get(c))
                return true;
        }
        return false;
    }
}
