import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;


class Sender
{
   public static void main(String args[]) throws Exception
      {
      
       
       //retrieve input from the user.
       System.out.println("Starting Server. Enter name of the datafile: ");
       BufferedReader filedir = new BufferedReader(new InputStreamReader(System.in));
       String dataFile = filedir.readLine();
       Path path = Paths.get(dataFile);
           
       byte[] data = Files.readAllBytes(path); 
       System.out.println("The file contains:  " + new String(data, "UTF-8"));
        
       System.out.println("Enter the maxSegSize: ");
       BufferedReader sizeInput = new BufferedReader(new InputStreamReader(System.in));
       int maxSegSize = Integer.valueOf(sizeInput.readLine());
        
       System.out.println("Enter timeout in m/s (Pick a large number!!): ");
       BufferedReader timeInput = new BufferedReader(new InputStreamReader(System.in));
       int timeout = Integer.valueOf(timeInput.readLine());
       
        byte[] requestData = new byte[10000];   //buffer for receiving request
       byte[] dataByte = new byte[maxSegSize];//buffer for sending data
       
      // initialize socket and receive initial nextbyteexpected + IP address and port
      DatagramSocket serverSocket = new DatagramSocket(9876);
      DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length);
      serverSocket.receive(requestPacket);
      requestData = requestPacket.getData();

       // retrieve IP address and port from incoming request
       InetAddress IPAddress = requestPacket.getAddress();
       int port = requestPacket.getPort();       

       //set the nextbyteexpected and windows size using a array
       // the format = (nextbyteexpected),(windowsize)
       String request = new String(requestData, "UTF-8");
       String[] strArray = request.split(",");
       
      
       
       DatagramPacket dataPacket = new DatagramPacket(dataByte, dataByte.length, IPAddress, port);
       int index;          
       int recBufferSize;
       int bufferLength = data.length;      //length of the data buffer
       serverSocket.setSoTimeout(timeout);      //set timeout for the socket
           boolean exit = false;
            while(!exit)
               {

                
                 // Set the index and buffer size buffer from client
                 index= Integer.parseInt(strArray[0].trim()); 
                 recBufferSize = Integer.parseInt(strArray[1].trim());
                
                 //check to see if receiver has enough buffer
                 if ( recBufferSize < maxSegSize) {
                    System.out.println("WARNING: Client buffer is not large enough to receive the data");
                    break;
                }
               
                   if (index < bufferLength) {
                       
                        // check if its full packet size (last)
                       if (bufferLength - index >= maxSegSize) {
                            for (int i = 0; i < maxSegSize; i++) {
                                dataByte[i] = data[index + i]; 
                            }  
                        dataPacket.setData(dataByte);  
                        serverSocket.send(dataPacket);
                        System.out.println("The sending packet size is :" + dataPacket.getLength());
                        System.out.println("The nextByteExpected is :"  + index);
                         System.out.println("Content of packet: "  + new String(dataPacket.getData(),"UTF-8"));
                       }
                       
                    // if it's not full segment, then it must be the last packet to be sent
                         else {
                            byte[] finalPacket = new byte[bufferLength - index];
                            for (int j = 0; j < (bufferLength - index); j++) {
                                 finalPacket[j] = data[index+j];    
                            }
                            
                            dataPacket.setData(finalPacket); 
                            System.out.println("The sending packet size is :" + dataPacket.getLength());
                            System.out.println("The nextByteExpected is :"  + index);
                            System.out.println("Content of packet: "  + new String(dataPacket.getData(),"UTF-8"));
                            serverSocket.send(dataPacket);
                            System.out.println("Last packet sent."); 
                            exit = true;
                            
                       }               
                   }                   
                       // wait for ack from the client
                   int count =0;
                  while(true) {
                      
                     try {
                        System.out.println("trying again "+count);   
                       serverSocket.receive(requestPacket);
                       count = 0;
                       break;
                         } catch (SocketTimeoutException e) {  //if timeout is triggered, the packet is resent
                             serverSocket.send(dataPacket);
                             System.out.println("Timeout!");
                             count++;
                             
                             
                         }
                  }
                     
                        // The ack contains the next byte expected and window size
                       request = new String(requestData, "UTF-8");
                       strArray = request.split(","); //splitting data into a array
                        //check clients buffersize to the data being sent.
                        

               }
      }
}