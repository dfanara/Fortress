package me.fanara.fortress.hybrid;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IOUtil {

    public static void writeString(DataOutputStream dos, String string) throws IOException {
        dos.writeInt(string.length());
        dos.writeBytes(string);
        System.out.println("Writing string of " + string.length() + " chars.");
        dos.flush();
    }

    public static String readString(DataInputStream dis) throws IOException {
        int length = dis.readInt();
        System.out.println("Reading string of " + length + " chars.");
        byte[] inBytes = new byte[length];
        dis.read(inBytes, 0, length);
        return new String(inBytes);
    }

    public static void writeBoolean(DataOutputStream dos, boolean bool) throws IOException {
        dos.writeByte(bool ? 0x01 : 0x02);
        dos.flush();
    }

    public static boolean readBoolean(DataInputStream dis) throws IOException {
        return dis.readByte() == 0x01;
    }

    public static void writeStringArray(DataOutputStream dos, String[] array) throws IOException {
        dos.writeInt(array.length);
        for(String s : array) {
            writeString(dos, s);
        }
        dos.flush();
    }

    public static String[] readStringArray(DataInputStream dis) throws IOException {
        int length = dis.readInt();
        String[] inStrings = new String[length];
        for(int i = 0; i < length; i++) {
            inStrings[i] = readString(dis);
        }
        return inStrings;
    }

}
