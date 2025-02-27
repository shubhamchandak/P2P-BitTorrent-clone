package Models;

import Utils.Enums;
import org.apache.commons.lang.SerializationUtils;

import java.io.Serializable;
import java.util.BitSet;

public class RequestModel {
    public int messageLength;
    public byte type;
    public byte[] payload;

    public RequestModel(Enums.MessageTypes e, Object o) {
        // Type:
        this.type = getType(e);
        this.payload = getPayload(e, 0);
        this.messageLength = getMessageLength();
    }

    private byte getType(Enums.MessageTypes e) {
        BitSet bitSet = new BitSet(8);
        bitSet.set(e.getValue(), true);
        return  bitSet.toByteArray()[0];
    }

    private byte[] getPayload(Enums.MessageTypes e, Object o) {
        if(e == Enums.MessageTypes.BITFIELD) {
            boolean bitfield[] = (boolean[]) o;
            BitSet bitset = new BitSet(bitfield.length);
            for(int i = 0; i < bitset.size(); i++) {
                if(bitfield[i]) bitset.set(i, true);
            }
            return bitset.toByteArray();
        }
        //  ---------------------- Handle piece content !!!! --------------------------------
        return SerializationUtils.serialize((Serializable) o);
    }

    private int getMessageLength() {
        // this.payload.length will give number of bytes and 1 byte for type and 4 bytes for messageLength itself
        return this.payload.length + 5;
    }
}