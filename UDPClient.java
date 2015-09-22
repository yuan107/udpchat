/**
*	UDP Client Program
*	Connects to a UDP Server
*	Receives a line of input from the keyboard and sends it to the server
*	Receives a response from the server and displays it.
*
*	@author: Michael Yuan
@	version: 2.1
*/

import java.io.*;
import java.net.*;

class UDPClient {
  static DatagramSocket clientSocket = null;
  static String clientColor = "";
  static String otherClientColor = "";
  static InetAddress IPAddress = null;

  public static void main(String args[]) throws Exception{
    clientSocket = new DatagramSocket();
    //IPAddress = InetAddress.getByName("10.49.139.107");
    IPAddress = InetAddress.getByName("192.168.0.9");
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Waiting to connect to server...\n");
    sendString("500");//the I want to conncect code
    try{
      Runtime.getRuntime().addShutdownHook(new Thread(){
        public void run(){
      try{
      sendString("300");
    } catch (Exception e){

    }

      clientSocket.close();
        }
      });
      while(true){

        // byte[] sendData = new byte[1024];

        // String sentence = inFromUser.readLine();
        // sendData = sentence.getBytes();
        // DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);

        // clientSocket.send(sendPacket);

        String modifiedSentence = recieveString();
        if(modifiedSentence.substring(0,3).equals("100")){//I am red
          clientColor = "Red";
          otherClientColor = "Blue";
          String blueConnectedMessage = "404";
          System.out.println("\nWaiting for Blue to connect...");
          while (!blueConnectedMessage.substring(0,3).equals("200")){
            blueConnectedMessage = recieveString();
          }
          System.out.println("\n\n---------------------------------------\n");
          modifiedSentence = "200 Blue connected! Type the first message!\n";
        } else if(modifiedSentence.substring(0,3).equals("200")){//I am blue
          clientColor = "Blue";
          otherClientColor = "Red";
          System.out.println("\nConnected! Waiting for Red to send the first message...\n");
          modifiedSentence = recieveString();//wait for message from red
        }

        System.out.println(otherClientColor + ": " + modifiedSentence.substring(3) + "\n" + clientColor + ":");

        sendString("000" + inFromUser.readLine());
      }
    }catch(Exception e){
      clientSocket.close();
      sendString("300");
    }
  }

  public static String recieveString() throws IOException{
    byte[] receiveData = new byte[1024];
    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
    clientSocket.receive(receivePacket);
    return new String(receivePacket.getData()).trim();
  }

  public static void sendString(String str) throws IOException{
    if(clientSocket == null){
      clientSocket = new DatagramSocket();
    }
    byte[] sendData = new byte[1024];
    sendData = str.getBytes();
    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
    clientSocket.send(sendPacket);
  }
}
