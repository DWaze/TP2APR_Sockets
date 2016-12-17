package com.company;

import java.io.Serializable;

/**
 * Created by lhadj on 11/21/2016.
 */
public class AllPacket implements Serializable {

    int sequence ;
    long crc;
    String message;

    public AllPacket(long crc, String message,int sequence) {
        this.sequence=sequence;
        this.crc = crc;
        this.message = message;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public long getCrc() {
        return crc;
    }

    public void setCrc(long crc) {
        this.crc = crc;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
