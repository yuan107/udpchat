/**
*	UDP Server Program
*	Listens on a UDP port
*	Receives a line of input from a UDP client
*	Returns an upper case version of the line to the client
*
*	@author: Michael Yuan
@	version: 2.0
*/

import java.io.*;
import java.net.*;

class UDPServer {
	public static DatagramSocket serverSocket = null;
	public static int port = 0;
	public static InetAddress RedIP = null;
	public static InetAddress BlueIP = null;
	
	public static void main(String args[]) throws Exception
	{
    //DatagramSocket serverSocket = null;
	//int port = 0;

		try
		{
			serverSocket = new DatagramSocket(9876);
		}

		catch(Exception e)
		{
			System.out.println("Failed to open UDP socket");
			System.exit(0);
		}

		System.out.println("\n\nServer Open. Listening for Clients...");

		while(true)
		{
			byte[] receiveData = new byte[1024];
			byte[] sendData  = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			serverSocket.receive(receivePacket);

			String sentence = new String(receivePacket.getData());

			InetAddress IPAddress = receivePacket.getAddress();

			port = receivePacket.getPort();

			sendData = sentence.getBytes();

			System.out.println("MSG RECIEVED: " + sentence + "|END OF MESSAGE|MessageCode:" + sentence.substring(0,3) + "\n");

			if(sentence.substring(0,3).equals("300") && IPAddress.equals(RedIP)){//Disconnect Message
				RedIP = null;
				System.out.println("Red Disconnected");
			}
			if(sentence.substring(0,3).equals("300") && IPAddress.equals(BlueIP)){//Disconnect Message
				BlueIP = null;
				System.out.println("Blue Disconnected");
			}

			InetAddress sendAddress = null;
			if(IPAddress.equals(RedIP) && BlueIP != null){
				sendAddress = BlueIP;
				System.out.println("Sending to Blue");
				sendData = sentence.getBytes();
			} else if(IPAddress.equals(BlueIP) && RedIP != null) {
				sendAddress = RedIP;
				System.out.println("Sending to Red");
				sendData = sentence.getBytes();
			} else if(RedIP == null){
				RedIP = IPAddress;
				sendAddress = RedIP;
				sendData = new String("100 Red Client Connected").getBytes();
			} else if(BlueIP == null && !RedIP.equals(IPAddress)){
				BlueIP = IPAddress;
				sendAddress = BlueIP;
				sendData = new String("200 Blue Client Connected").getBytes();

			    //send to both IP's
				DatagramPacket sendPacket2 = new DatagramPacket(sendData, sendData.length, RedIP, port);
				serverSocket.send(sendPacket2);
			} else{
				BlueIP = IPAddress;
				sendAddress = BlueIP;
				sendData = new String("404 Client Not Found").getBytes();
			    //send to both IP's
				DatagramPacket sendPacket2 = new DatagramPacket(sendData, sendData.length, RedIP, port);
				serverSocket.send(sendPacket2);
			}
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sendAddress, port);
			serverSocket.send(sendPacket);
		}
	}
}
