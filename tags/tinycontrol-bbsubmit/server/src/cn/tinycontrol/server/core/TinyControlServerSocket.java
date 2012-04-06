package cn.tinycontrol.server.core;

import java.net.DatagramSocket;
import java.net.SocketException;

public class TinyControlServerSocket {
	
	private DatagramSocket udpSocket;
	
	private UDPReceiverThread recvThread;
	
	private long socketCreateTime;
	
	//implement accept
	public TinyControlServerSocket(int port) throws SocketException {
	    socketCreateTime = System.currentTimeMillis();
		udpSocket = new DatagramSocket(port);
		System.out.println("Server started at port " + port +" ...");
		recvThread = new UDPReceiverThread(this);
		recvThread.start();
	}
	
	public TinyControlSocket accept() {
		return recvThread.getSocketFromQueue();
	}
	
	public DatagramSocket getUdpSocket() {
        return udpSocket;
    }
	
	public int getCurTime() {
	    return (int)(System.currentTimeMillis() - socketCreateTime);
	}
	
}
