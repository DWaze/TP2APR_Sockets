package com.company;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.zip.CRC32;

/**
 * Created by lhadj on 11/12/2016.
 */
public class EmetteurProcess {
    static int i=0;


    ///////////Serialisation

    public static byte[] serialize(Object obj) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }
    public static void ProcedureEmeteur(String m,int numberTry){
        String message=m;

        DatagramSocket aSocket=null;



        //////// Server Synchronisation counter
        numberTry++;

        try {

            /////// Starting server
            System.out.println("Starting The process of sending.......");
            aSocket = new DatagramSocket();
            InetAddress Host = InetAddress.getByName("localhost");

            int serverPort = 9999;

            /////////////  Transforming the message to bytes
            byte[] mb = m.getBytes();

            ////////////// generating CRC from message Bytes mb
            CRC32 crc = new CRC32();
            crc.update(mb);
            long crcValue = crc.getValue();


            /////////////Creating the objet AllPacket that contain all the information about the packet
            Object data = new AllPacket(crcValue,m,i);
            byte[] crcBytes=  serialize(data);

            System.out.println("CRC Generated : "+crcValue);

            /////////////// putting the message into a datagram packet and sending it
            DatagramPacket request = new DatagramPacket(crcBytes,crcBytes.length,Host,serverPort);
            aSocket.setSoTimeout(1500);
            aSocket.send(request);

            byte[] buffer = new byte[1000];

            /////////////// receiving  the response acknowledgment

            DatagramPacket reply = new DatagramPacket(buffer,buffer.length);

            aSocket.receive(reply);

            String datai = new String(reply.getData(),0, reply.getLength());


            //////////////// checking if it's a positive acknowledgment
            if (datai.equals("TRUE")) {
                System.out.println("Message sent successfully tram "+i+" !");
                i++;
                return;
            } else {
                /////////////// checking else and retrying til the number of try reaches 10     0..9
                if (numberTry<=9){
                    System.out.println("CRC Doesn't match Recending...");
                    ProcedureEmeteur(message,numberTry);
                }else{
                    System.out.println("Can't Reach Target try number : "+numberTry);
                    return;
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
        catch(SocketTimeoutException e){
            /////////////// retrying the process if the time out is bypassed
            if(numberTry<=9){
                System.out.println("Time out Recending Message "+i+" try number : "+numberTry);
                ProcedureEmeteur(message,numberTry);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally{
            if(aSocket != null){
                aSocket.close();
            }
        }
    }
}