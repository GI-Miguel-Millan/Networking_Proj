package networking.project.game.network.packets;

import networking.project.game.utils.NetCodes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by nick on 3/31/17.
 *
 * Sent when a game is starting.
 *
 * Ordering:
 *  -Game Width (short)
 *  -Game Height (short)
 *  -Number of Players (byte)
 */
public class GameStartPacket extends Packet implements NetCodes {

    // Struct-like behavior
    public short gameWidth, gameHeight;
    public byte numPlayers;

    public GameStartPacket()
    {
        gameHeight = gameWidth = numPlayers = 0;
    }

    @Override
    public void decompose(byte[] data)
    {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
            DataInputStream dis = new DataInputStream(bais))
        {
            if (dis.readByte() == GAME_START)
            {
                gameWidth = dis.readShort();
                gameHeight = dis.readShort();
                numPlayers = dis.readByte();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void compose()
    {
    	if(data == null){
    		
    		data = new byte[1500];
    	}
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1460);
             DataOutputStream dos = new DataOutputStream(baos))
        {
            dos.writeByte(GAME_START);
            dos.writeShort(gameWidth);
            dos.writeShort(gameHeight);
            dos.writeByte(numPlayers);
           
            System.arraycopy(baos.toByteArray(), 0, data, 0, baos.size());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
