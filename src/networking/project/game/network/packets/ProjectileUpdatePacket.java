package networking.project.game.network.packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by nick on 3/31/17.
 */
public class ProjectileUpdatePacket extends Packet {

    public int ID, parentID;

    public double rotation;

    public float xPos, yPos;

    @Override
    public void decompose(byte[] data)
    {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             DataInputStream dis = new DataInputStream(bais))
        {
            if (dis.readByte() == GAME_PROJ_UPDATE)
            {

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


            System.arraycopy(baos.toByteArray(), 0, data, 0, baos.size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
