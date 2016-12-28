package com.company;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.zip.CRC32;

/**
 * Created by lhadj on 11/12/2016.
 */
public class RecepteurProcess {
    static int i = 0;


    //////////////// deserialisation
    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return o.readObject();
            }
        }
    }

    public void recepteur(){

        DatagramSocket aSocket = null;
        AllPacket pack=null;
        byte[] b=null;

        try{
            boolean var=true;
            System.out.println("Start receiving....");
            ////////// th same as the process ou sending

            while(var){
                aSocket =  new DatagramSocket(9999);

                byte[] buffer = new byte[1000];
                CRC32 crc = new CRC32();

                DatagramPacket crcPacket = new DatagramPacket(buffer,buffer.length);
                aSocket.receive(crcPacket);
                String crcEm="";
                b= crcPacket.getData();
                Object p;
                try {
                    p=deserialize(b);
                    pack=(AllPacket)p;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                long crcvalue = pack.getCrc();


                byte[] crcDataRec = pack.getMessage().getBytes();
                crc.update(crcDataRec);
                long crcRec =crc.getValue();
                DatagramPacket reply =null;


                ///////// checking if the the serial number of the packet that we attempt to receive
                // ////// is the same as the serial number of the packet that we have got



                if(crcvalue == crcRec && i==pack.getSequence()){
                    b= (new String("TRUE")).getBytes();
                    System.out.println("CRC Check and trame "+i+" complite with success !");
                    reply = new DatagramPacket(b,b.length,crcPacket.getAddress(),crcPacket.getPort());
                    aSocket.send(reply);
                    System.out.println("Recepteur reply : "+pack.getMessage());
                    i++;
                }else{
                    aSocket.close();
                    recepteur();
                }

            }
        }catch(SocketException e){
            if(aSocket!=null){
                aSocket.close();
            }
        }catch(SocketTimeoutException e){
            System.out.println("timed out");
        }catch(IOException e){
            e.printStackTrace();
        }

    }

}
