package cn.tinycontrol.server.core;

import java.net.DatagramSocket;
import java.net.SocketException;

public class TinyControlServerSocket {
	
	private DatagramSocket udpSocket;
	
	private UDPReceiverThread recvThread;
	
	//implement accept
	public TinyControlServerSocket(int port) throws SocketException {
		udpSocket = new DatagramSocket(port);
		recvThread = new UDPReceiverThread(this);
		recvThread.start();
	}
	
	public TinyControlSocket accept() {
		return recvThread.getSocketFromQueue();
	}
	
	public DatagramSocket getUdpSocket() {
        return udpSocket;
    }
	
}
