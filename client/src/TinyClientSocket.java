//TinyClientSocket
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import cn.tinycontrol.common.model.DataPacket;
import cn.tinycontrol.common.model.FeedbackPacket;

public class TinyClientSocket {

	static public long startTime;
	
	private int tinyPort;
	private InetAddress tinyIPAddress;
	private DatagramSocket clientSocket;
	private byte[] tinyBuffer = new byte[10120];
	TinyClientThread tinyThread;
	 
	public TinyClientSocket(String ip, int port) throws IOException {

		startTime = System.currentTimeMillis();
		tinyPort = port;
		tinyIPAddress = InetAddress.getByName(ip);
		clientSocket = new DatagramSocket();
		this.startConnection();
		this.receiveThread();
	}

	// Receive Data and Push it up to the Application
	public byte[] TinyClientReceive(byte[] clientData, int length) {

		if (tinyBuffer != null) {
			clientData = copyArray(clientData, tinyBuffer, length);
			return clientData;
		} else
			return null;

	}

	// Close the TinyClient
	public void TinyClientClose() throws IOException {

		this.endConnection();
	}

	// Copy byte array
	public static byte[] copyArray(byte[] arrayA, byte[] arrayB, int length) {

		for (int i = 0; i < length; i++)
			arrayA[i] = arrayB[i];
		return arrayA;

	}

	// Starts the UDP Connection
	public void startConnection() throws IOException {
		System.out.println("Connection Started");
		FeedbackPacket feedbackPacket = new FeedbackPacket(0, 0, 0, 0);
		feedbackPacket = setPacket(feedbackPacket, -1);// Setting to SYN
		this.sendPacket(feedbackPacket);//Sending
		//System.out.println("SYN packets sent");
		DataPacket dataPacket = this.receivePacket();
		//Checking for SYN-ACK
		if(dataPacket.getSequenceNumber()==-1){
			//System.out.println("SYN ACK received");
			feedbackPacket = new FeedbackPacket(0, 0, 0, 0);
			feedbackPacket = setPacket(feedbackPacket, -2);// Setting to ACK
			feedbackPacket.setTimeStamp(dataPacket.getTimeStamp());//Inserting TimeStamp
			this.sendPacket(feedbackPacket);//Sending Packet
			//System.out.println("ACK packet sent");
			this.receiveThread();//Start Thread
		}
			
		
	}

	// Ends the UDP Connection
	public void endConnection() throws IOException {
		System.out.println("Connection Ended");
		tinyThread.setKeepAlive(false);
		FeedbackPacket feedbackPacket = new FeedbackPacket(0, 0, 0, 0);
		feedbackPacket = setPacket(feedbackPacket, -3);// Setting to FIN
		this.sendPacket(feedbackPacket);
		//Ends here
	}

	// Send a UDP Packet
	public void sendPacket(FeedbackPacket packet) throws IOException {

		byte[] sendData = packet.constructBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
				tinyIPAddress, tinyPort);
		//clientSocket.send(sendPacket);
		System.out.println("Packet sent");
	}

	// Receive a UDP Packet
	public DataPacket receivePacket() throws IOException {
		byte[] receiveData = new byte[1012];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		receiveData = receivePacket.getData(); // TODO: Kunal check if you still need this line 
		DataPacket dataPacket = new DataPacket(receivePacket);
		System.out.println("Packet received");
		return dataPacket;
	}

	// Setting the Packet to SYN,ACK,FIN
	public static FeedbackPacket setPacket(FeedbackPacket feedbackPacket,
			int value) {

		feedbackPacket.setTimeStamp(0);
		feedbackPacket.setLossEventRate(0);
		feedbackPacket.setReceiveRate(0);
		feedbackPacket.setElapsedTime(value);
		return feedbackPacket;
	}
	
	//Creating Thread
	public void receiveThread(){
		
		System.out.println("Packet received");
		tinyThread = new TinyClientThread(this);
		System.out.println("Thread started");
		tinyThread.start(this);
		
	}
	
	public boolean checkEnd(DataPacket dataPacket){
		
		return false;
	}

}
