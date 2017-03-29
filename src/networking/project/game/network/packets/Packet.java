package networking.project.game.network.packets;

import networking.project.game.utils.DataCounterStream;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

/**
 * Created by nick on 3/28/17.
 */
public class Packet {

    public byte identifier;

    // Input packet (recv)
    DataCounterStream dcs = null;
    DataInputStream dis = null;
    ByteArrayInputStream bais = null;

    // Output packet (send)
    public DataOutputStream dos = null;
    ByteArrayOutputStream baos = null;

    // Defaults to being a send packet
    public Packet()
    {
        baos = new ByteArrayOutputStream(1460);
        dos = new DataOutputStream(baos);
    }

    // Defaults to being a recv packet
    public Packet(byte[] input)
    {
        bais = new ByteArrayInputStream(input);
        dis = new DataInputStream(bais);
        dcs = new DataCounterStream(input, dis);
        try
        {
            identifier = dcs.getIndicator();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public byte[] getBytes()
    {
        return baos.toByteArray();
    }

    // Used in sending packets, each packet composes differently
    public void send(DatagramSocket serverSocket, DatagramPacket clientDatagram, boolean disposeAfter) throws IOException
    {
        send(serverSocket, clientDatagram.getAddress(), clientDatagram.getPort(), disposeAfter);
    }

    public void send(DatagramSocket serverSocket, InetAddress inet, int port, boolean dispose) throws IOException
    {
        serverSocket.send(new DatagramPacket(baos.toByteArray(), baos.size(), inet, port));
        if (dispose)
            dispose();
    }

    public void dispose()
    {
        try
        {
            // Output
            if (dos != null)
                dos.close();
            if (baos != null)
                baos.close();

            // Input
            if (bais != null)
                bais.close();
            if (dis != null)
                dis.close();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
