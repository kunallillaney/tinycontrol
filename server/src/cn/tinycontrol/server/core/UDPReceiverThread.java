package cn.tinycontrol.server.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

import cn.tinycontrol.common.model.DataPacket;
import cn.tinycontrol.common.model.FeedbackPacket;
import cn.tinycontrol.server.requesthandler.MapHandler;
import cn.tinycontrol.server.requesthandler.ServerWorkerThread;

public class UDPReceiverThread extends Thread {
	
	private TinyControlServerSocket tcServerSocket;	// This is the TinyControlServerSocket that created this thread!

	LinkedBlockingQueue<TinyControlSocket> ackQueue = new LinkedBlockingQueue<TinyControlSocket>();
	
	public UDPReceiverThread(TinyControlServerSocket tcServerSocket) {
		this.tcServerSocket = tcServerSocket;
	}
	
	public TinyControlSocket getSocketFromQueue() {
		// TODO Removes a TinyControlSocket from ackQueue and returns that socket to the caller
		TinyControlSocket retVal = null;
		try {
			retVal = ackQueue.take();
			System.out.println("Successfully accepted a connection...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				byte[] receiveData = new byte[FeedbackPacket.PACKET_LENGTH];
				DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
				tcServerSocket.getUdpSocket().receive(receivePacket);
				String clientDetails = receivePacket.getAddress()+ " " + receivePacket.getPort();
				FeedbackPacket feedbackPacket = new FeedbackPacket(receivePacket);
				//System.out.println("Recieved connection from " + clientDetails + " " + feedbackPacket);
				
				switch (feedbackPacket.getElapsedTime()) {
				case -1: // SYN
					System.out.println("Recieved SYN from " + clientDetails);
					DataPacket synAckDataPacket = new DataPacket(-1, tcServerSocket.getCurTime(), 0, new byte[1000]);
					byte[] synDataPacketBytes = synAckDataPacket.constructBytes();
					System.out.println("Sending SYN ACK to " + clientDetails);
					tcServerSocket.getUdpSocket().send(new DatagramPacket(synDataPacketBytes,
							synDataPacketBytes.length, feedbackPacket
									.getSourceAddr(), feedbackPacket.getPort()));
					break;
				case -2: // ACK
					System.out.println("Recieved ACK from " + clientDetails);
					//long currentTime = new Date().getTime();
					int initialRTT = tcServerSocket.getCurTime() - feedbackPacket.getTimeStamp();
					System.out.println(initialRTT);
					ServerWorkerThread workerThread = new ServerWorkerThread(
							initialRTT, feedbackPacket.getSourceAddr(),
							feedbackPacket.getPort(), tcServerSocket);
					TinyControlSocket tcs = new TinyControlSocket(workerThread);
					ackQueue.add(tcs); // add to the queue
					break;
				case -3: // close
				    System.out.println("Recieved CLOSE from " + clientDetails + ". Packet: " + feedbackPacket);
				    MapHandler.getInstance().get(feedbackPacket.getSourceAddr(),feedbackPacket.getPort()).shutDown();
					break;
				default: // feedback
					//System.out.println("Recieved Feedback Packet from " + clientDetails + ". Packet: " + feedbackPacket);
					MapHandler.getInstance().get(feedbackPacket.getSourceAddr(),feedbackPacket.getPort()).handleFeedBackPacket(feedbackPacket);
				}
			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}