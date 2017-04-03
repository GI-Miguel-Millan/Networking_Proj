package networking.project.game.network.packets;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by nick on 3/28/17.
 */
public class PlayerUpdatePacket extends Packet {


    public int ID, health;
    public byte input;

    public double rotation;
    public float posX, posY;

    @Override
    public void decompose(byte[] data)
    {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             DataInputStream dis = new DataInputStream(bais))
        {
            if (dis.readByte() == GAME_PLAYER_UPDATE)
            {
                ID = dis.readInt();
                health = dis.readInt();
                input = dis.readByte();
                rotation = dis.readDouble();
                posX = dis.readFloat();
                posY = dis.readFloat();
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
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1500);
             DataOutputStream dos = new DataOutputStream(baos))
        {
            dos.writeByte(GAME_PLAYER_UPDATE);
            dos.writeInt(ID);
            dos.writeInt(health);
            dos.writeByte(input);
            dos.writeDouble(rotation);
            dos.writeFloat(posX);
            dos.writeFloat(posY);

            System.arraycopy(baos.toByteArray(), 0, data, 0, baos.size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}