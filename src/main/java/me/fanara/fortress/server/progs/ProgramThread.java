package me.fanara.fortress.server.progs;

import me.fanara.fortress.server.FortressServer;

import java.util.ArrayList;

public class ProgramThread extends Thread {

    private final FortressServer server;

    public ProgramThread(FortressServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        while(server.isRunning()) {
            ArrayList<Program> removing = new ArrayList<>();
            for(Program program : server.programs) {
                boolean b = program.run();
                if(!b) {
                    removing.add(program);
                }
            }
            removing.forEach(server.programs::remove);

            Thread.yield();
        }
    }
}
