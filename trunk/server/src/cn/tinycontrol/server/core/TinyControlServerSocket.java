package cn.tinycontrol.server.core;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Date;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class TinyControlServerSocket {
	
	private DatagramSocket udpSocket;
	
	private UDPReceiverThread recvThread;
	
	//implement accept
	public TinyControlServerSocket(int port) throws SocketException {
		udpSocket = new DatagramSocket(port);
		recvThread = new UDPReceiverThread(udpSocket);
		recvThread.start();
	}
	
	public TinyControlSocket accept() {
		return recvThread.getSocketFromQueue();
	}
}
