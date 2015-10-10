package me.fanara.fortress;

import me.fanara.fortress.client.FortressClient;
import me.fanara.fortress.server.FortressServer;

public class Main {

    public static void main(String[] args) {
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("server")) {
                System.out.println("Starting Fortress Server");
                FortressServer fs = new FortressServer();
                while(fs.isRunning()) {
                    Thread.yield();
                }
            }else if(args[0].equalsIgnoreCase("client")) {
                FortressClient fc = new FortressClient(args[1], Integer.parseInt(args[2]), args[3]);
                while(fc.isRunning()) {
                    Thread.yield();
                }
            }
        }else {
            System.out.println("fortress (server|client) [ip,port,name]");
        }
    }

}
