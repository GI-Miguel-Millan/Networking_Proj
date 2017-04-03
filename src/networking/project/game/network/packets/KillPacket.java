package networking.project.game.network.packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

/**
 * Created by nick on 3/31/17.
 */
public class KillPacket extends Packet
{

    public int size;
    public ArrayList<Integer> killIDs;

    public KillPacket()
    {
        killIDs = new ArrayList<>();
    }

    @Override
    public void decompose(byte[] data)
    {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             DataInputStream dis = new DataInputStream(bais))
        {
            if (dis.readByte() == GAME_KILL_UPDATE)
            {
                size = dis.readInt();
                for (int i = 0; i < size; i++)
                    killIDs.add(dis.readInt());
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
            dos.writeByte(GAME_KILL_UPDATE);
            dos.writeInt(size);
            for (int i : killIDs)
                dos.writeInt(i);
            System.arraycopy(baos.toByteArray(), 0, data, 0, baos.size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
