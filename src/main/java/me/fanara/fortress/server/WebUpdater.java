package me.fanara.fortress.server;

public class WebUpdater extends Thread {

    private final FortressServer server;

    public WebUpdater(FortressServer server) {
        this.server = server;
    }

    public void run() {
        while(server.isRunning()) {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
