package networking.project.game.network.packets;

import networking.project.game.utils.NetCodes;

/**
 * Created by nick on 3/28/17.
 */
public class ConnectionPacket extends Packet implements NetCodes {

    public ConnectionPacket(byte[] input)
    {
        super(input);
    }

    public ConnectionPacket(byte indentifier)
    {
        super();
        try {
            dos.writeByte(CONN_DATA);
            dos.writeByte(indentifier);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
