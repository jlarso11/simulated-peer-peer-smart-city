/**
* The SampleMessage class shows the outline for how
* to create new message types.
*
* @author  Kevin Bruhwiler
* @version 1.0
* @since   2019-06-11 
*/

package cs555.chiba.wireformats;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.UUID;


public class GossipData implements Event{

    private final int type = Protocol.GOSSIP_DATA.ordinal();
    private final int SIZE_OF_INT = 4;
    private UUID senderID;
    private String[] devices;
    private Socket socket;

    /**
     * This constructor should be used when creating a message
     * that will be sent
     * @param ID An number representing something
     * @param currentHop The current number of hops the message has made
     * @param hopLimit The maximum number of hops this message can make
     */
    public GossipData(UUID senderID, String[] devices){
        this.senderID = senderID;
        this.devices = devices;
    }

    /**
     * This constructor should be used when deserializing a received message
     * @param message The serialized message
     * @param socket  The socket this message was received from
     */
    public GossipData(byte[] message, Socket socket){ 
    	ByteBuffer b = ByteBuffer.allocate(message.length).put(message);
    	b.rewind();
        b.get();
        int senderIDlen = b.getInt();
        byte[] senderIDbytes = new byte[senderIDlen]; 
        b.get(senderIDbytes);
        senderID = UUID.fromString(new String(senderIDbytes));
        int numDevices = b.getInt();
        devices = new String[numDevices];
        for(int i = 0; i < numDevices; i++) {
        	int deviceLength = b.getInt();
        	byte[] deviceBytes = new byte[deviceLength]; 
        	b.get(deviceBytes);
        	devices[i] = new String(deviceBytes);
        }
        this.socket = socket;
    }

    /**
     * Serializes the message
     * @return byte[] The serialized message
     */
    public byte[] getBytes(){
    	byte[] senderIDbytes = senderID.toString().getBytes();
    	int size = senderIDbytes.length + 2*SIZE_OF_INT + 1;
    	for(String device : devices) {
    		byte[] deviceBytes = device.getBytes();
    		size += deviceBytes.length + SIZE_OF_INT;
    	}
    	ByteBuffer b = ByteBuffer.allocate(size);
    	b.put((byte)type);
    	b.putInt(senderIDbytes.length);
    	b.put(senderIDbytes);
    	b.putInt(devices.length);
    	for(String device : devices) {
    		b.putInt(device.getBytes().length);
    		b.put(device.getBytes());
    	}
        return b.array();
    }

    public int getType(){
        return type;
    }
    
    public UUID getSenderID() {
    	return senderID;
    }
    
    public String[] getDevices() {
    	return devices;
    }

    public Socket getSocket() {
    	return socket;
    }
}