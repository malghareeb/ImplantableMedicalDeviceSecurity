import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

class Receiver
{
   public static void main(String args[]) throws Exception
   {

       System.out.println("Enter receiving buffersize: ");
       BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
       int bufferSize = Integer.parseInt(buff.readLine());;
       
       //initialize  buffersize, nextbyteexpected, winsize
     
      int winSize = bufferSize;
      int nextByte = 0;
      
      //initialize buffers
      byte[] requestBuffer = new byte[100000];
      byte[] recBuffer = new byte[bufferSize]; 

      DatagramSocket receiverSocket = new DatagramSocket();
      InetAddress IPAddress = InetAddress.getByName("localhost");
      DatagramPacket request = new DatagramPacket(requestBuffer,requestBuffer.length,  IPAddress, 9876);
   
      //send new request with new nextbyteexpected and windowsize    
     String send = new String( nextByte + "," + winSize);
     request.setData(send.getBytes());
     receiverSocket.send(request);
     
     // initialize lastmessege as 0;
     int lastmsgSize = 0;         
      
      while (true) {
       
        DatagramPacket receivePacket = new DatagramPacket(recBuffer, recBuffer.length); 

        // reterival of data into buffer
        receiverSocket.receive(receivePacket);
        System.out.println(" contains:   "+ new String(receivePacket.getData(),"UTF-8"));
             //check to see if the incoming packet is the last packet by comparing it's size to the last packet received
            if ((lastmsgSize > 0 )&& (lastmsgSize > receivePacket.getLength())){                   
                  for (int i = 0;  i < receivePacket.getLength(); i++) {
                     recBuffer[nextByte + i] = receivePacket.getData()[i];                    
                  } 
                  System.out.println(" Incoming packet size:  "+ receivePacket.getLength());
                  System.out.println(" Transmission complete. Received:  "+ new String(recBuffer,"UTF-8"));
            }
            //if it is not last packet
            else {         
              for (int i = 0;  i < receivePacket.getLength(); i++) {
              recBuffer[nextByte + i] = receivePacket.getData()[i];     
             }
            System.out.println(" Incoming packet size:  "+ receivePacket.getLength());   
        //update nextbyte and winsize 
        nextByte = nextByte + receivePacket.getLength();
        winSize = winSize - receivePacket.getLength();

       //send ack with nextbyteexpected and winsize
       send = new String( nextByte + "," + winSize);
       request.setData(send.getBytes());
       receiverSocket.send(request);
       // update lastmessege received
       lastmsgSize = receivePacket.getLength();
            }
     }      
   }
}