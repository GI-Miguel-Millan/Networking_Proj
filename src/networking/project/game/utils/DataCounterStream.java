package networking.project.game.utils;

import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by nick on 3/28/17.
 *
 * This class is introduced so that we may read floats (with proper endianness).
 */
public class DataCounterStream {

    private int totalBytesRead;
    private DataInputStream dis;
    private ByteBuffer buf;

    public DataCounterStream(byte[] buffer, DataInputStream dis) {
        totalBytesRead = 0;
        this.dis = dis;
        buf = ByteBuffer.wrap(buffer).asReadOnlyBuffer().order(ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * This method will determine what type of data is to follow it.
     * @return The indicator byte for the packet.
     */
    public byte getIndicator() {
        try
        {
            totalBytesRead += 1;
            return dis.readByte();
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public String getString() {
        try {
            int nameSize = dis.readInt();
            totalBytesRead += 4;
            byte[] stringToRead = new byte[nameSize];
            int read = dis.read(stringToRead, 0, nameSize);
            if (read >= 0) {
                totalBytesRead += read;
                return new String(stringToRead);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public float getFloat() {
        float toRet = buf.getFloat(totalBytesRead);
        totalBytesRead += 4;
        return toRet;
    }

    public int readUnsignedByte() {
        try {
            totalBytesRead += 8;
            return dis.readUnsignedByte();
        } catch (Exception e) {
            return -1;
        }
    }
}