package networking.project.game.network.packets;


import networking.project.game.entities.creatures.Player;
import networking.project.game.utils.NetCodes;

import java.util.ArrayList;

/**
 * Created by nick on 3/28/17.
 */
public class EntityPacket extends Packet implements NetCodes {

    private ArrayList<Player> players = null;

    public EntityPacket(byte identifier)
    {
        super();
        try {
            dos.writeByte(GAME_DATA);
            dos.writeByte(identifier);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
