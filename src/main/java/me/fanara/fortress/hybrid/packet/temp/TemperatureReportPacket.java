package me.fanara.fortress.hybrid.packet.temp;

import me.fanara.fortress.client.FortressClient;
import me.fanara.fortress.hybrid.packet.Packet;
import me.fanara.fortress.server.Client;

import java.io.*;

public class TemperatureReportPacket extends Packet {

    private double temperature;

    public TemperatureReportPacket(double temperature) {
        super((byte) 0xA4);
        this.temperature = temperature;
    }

    @Override
    public void handle() {
        //Run temperature command and report status
        try {
            Process p = Runtime.getRuntime().exec("/opt/vc/bin/vcgencmd measure_temp");
            BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String tmp = is.readLine();
            if(tmp.contains("=")) {
                String[] t = tmp.split("=");
                temperature = Double.parseDouble(t[1].split("'")[0]);
                FortressClient.getInstance().getNetworkHandler().sendPacket(new TemperatureReportPacket(temperature));
            }
        } catch (IOException e) {

        }
        temperature = 0;
    }

    @Override
    public void handleServer(Client client) {
        client.setLastTemp(temperature);
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeDouble(temperature);
    }

    @Override
    public void create(DataInputStream dis) throws IOException {
        temperature = dis.readDouble();
    }

    public double getTemperature() {
        return temperature;
    }
}
