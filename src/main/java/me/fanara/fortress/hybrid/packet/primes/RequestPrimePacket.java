package me.fanara.fortress.hybrid.packet.primes;

import me.fanara.fortress.client.FortressClient;
import me.fanara.fortress.hybrid.packet.Packet;
import me.fanara.fortress.server.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RequestPrimePacket extends Packet {

    private long number;

    public RequestPrimePacket(long num) {
        super((byte) 0xA0);
        this.number = num;
    }

    @Override
    public void handle() {
        //Prime factor dat piece o' poo
        for(int i = 2; i < number; i++) {
            if(number % i == 0) {
                //number / i R0;
                FortressClient.getInstance().getNetworkHandler().sendPacket(new ResponsePrimePacket(number/i, false, i, false));
                return;
            }
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FortressClient.getInstance().getNetworkHandler().sendPacket(new ResponsePrimePacket(number, true, 0, false));
    }

    @Override
    public void handleServer(Client client) {
        //Disregard. S->C
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeLong(number);
    }

    @Override
    public void create(DataInputStream dis) throws IOException {
        number = dis.readLong();
    }
}
