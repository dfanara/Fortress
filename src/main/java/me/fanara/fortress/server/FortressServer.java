package me.fanara.fortress.server;

import me.fanara.fortress.server.network.NetworkHandler;
import me.fanara.fortress.server.network.SocketListener;
import me.fanara.fortress.server.progs.FibonacciSequenceProgram;
import me.fanara.fortress.server.progs.PrimeFactorizationProgram;
import me.fanara.fortress.server.progs.Program;
import me.fanara.fortress.server.progs.ProgramThread;

import java.util.ArrayList;
import java.util.Scanner;

public class FortressServer {

    private boolean isRunning;
    private NetworkHandler networkHandler;
    private SocketListener socketListener;
    private ProgramThread programThread;
    private static FortressServer instance;

    public ArrayList<Program> programs = new ArrayList<>();

    public FortressServer() {
        instance = this;
        isRunning = true;

        networkHandler = new NetworkHandler(this);
        networkHandler.start();
        socketListener = new SocketListener(this);
        socketListener.start();
        programThread = new ProgramThread(this);
        programThread.start();

        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            String line = scanner.nextLine();

            if(line.startsWith("prime")) {
                System.out.print("Enter a number: ");
                long input = scanner.nextLong();
                PrimeFactorizationProgram pfp = new PrimeFactorizationProgram(input);
                programs.add(pfp);
            }else if(line.startsWith("fib")) {
                FibonacciSequenceProgram prgm = new FibonacciSequenceProgram(1, scanner.nextInt());
                programs.add(prgm);
            }
        }
    }


    public NetworkHandler getNetworkHandler() {
        return this.networkHandler;
    }

    public boolean isRunning() {
        return isRunning;
    }
    public static FortressServer getInstance() {
        return instance;
    }

}
