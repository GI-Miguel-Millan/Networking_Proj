package networking.project.game.network.packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by nick on 3/28/17.
 */
public class ConnectionPacket extends Packet {

    // This is a bit different for this packet, since
    // all of the connection related ones (ACK/REQ/DISC) all
    // have the same format, so we just store this here.
    public byte type;

    // Used for identifying the player in ACK/REQ/DISC
    public int ID;

    // Used in MSG
    public String message;

    @Override
    public void decompose(byte[] data)
    {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
            DataInputStream dis = new DataInputStream(bais))
        {
            type = dis.readByte();
            if (type == CONN_REQ || type == CONN_DISC || type == CONN_ACK)
                ID = dis.readInt();
            if (type == CONN_MSG)
                message = dis.readUTF();
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
            dos.writeByte(type);
            if (type == CONN_REQ || type == CONN_DISC || type == CONN_ACK)
                dos.writeInt(ID);
            if (type == CONN_MSG)
                dos.writeUTF(message);

            System.arraycopy(baos.toByteArray(), 0, data, 0, baos.size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
