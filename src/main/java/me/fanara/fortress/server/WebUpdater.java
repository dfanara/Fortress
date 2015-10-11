package me.fanara.fortress.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebUpdater extends Thread {

    private final FortressServer server;

    public WebUpdater(FortressServer server) {
        this.server = server;
    }

    public void run() {
        while (server.isRunning()) {
            try {
                URL obj = new URL("http://hek.fanara.me/update.php");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                String urlParameters = "data={\"devices\":[";

                String devices = "";
                for(Client client : server.getNetworkHandler().getClients()) {
                    devices = devices + ",{\"name\":\"" + client.getName() + "\",\"temp\":"+client.getLastTemp()+"}";
                }
                if(devices.length() > 0) {
                    urlParameters = urlParameters + devices.substring(1);
                }
                urlParameters = urlParameters + "]}";

                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
