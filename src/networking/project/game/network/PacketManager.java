package networking.project.game.network;

import networking.project.game.network.packets.ConnectionPacket;
import networking.project.game.network.packets.Packet;
import networking.project.game.utils.NetCodes;

/**
 * Created by nick on 3/28/17.
 */
public class PacketManager implements NetCodes {



    public static Packet getPacket(byte[] input)
    {
        Packet toReturn;
        switch (input[0]) // Depending on the identifier byte
        {
            case CONN_DATA:
                toReturn = new ConnectionPacket(input);
                break;
            case GAME_DATA:
                toReturn = null;
                break;
            default:
                toReturn = null;
                break;
        }


        return toReturn;
    }
}
