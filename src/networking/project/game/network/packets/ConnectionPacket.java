package networking.project.game.network.packets;

/**
 * Created by nick on 3/28/17.
 */
public class ConnectionPacket extends Packet {

    // This is a bit different for this packet, since
    // all of the connection related ones (ACK/REQ/DISC) all
    // have the same format, so we just store this here.
    byte type;

    int ID;

    @Override
    public void decompose(byte[] data)
    {

    }

    @Override
    public void compose()
    {

    }
}
