package cn.tinycontrol.server.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import cn.tinycontrol.common.model.DataPacket;
import cn.tinycontrol.common.model.FeedbackPacket;
import cn.tinycontrol.server.requesthandler.MapHandler;
import cn.tinycontrol.server.requesthandler.ServerWorkerThread;

public class UDPReceiverThread extends Thread {
	
	private TinyControlServerSocket tcServerSocket;	// This is the TinyControlServerSocket that created this thread!

	Queue<TinyControlSocket> ackQueue = new LinkedBlockingQueue<TinyControlSocket>();
	
	public UDPReceiverThread(TinyControlServerSocket tcServerSocket) {
		this.tcServerSocket = tcServerSocket;
	}
	
	public TinyControlSocket getSocketFromQueue() {
		// TODO Removes a TinyControlSocket from ackQueue and returns that socket to the caller
		return ackQueue.remove();
	}
	
	@Override
	public void run() {
		try {
			long threadStartTime = new Date().getTime();
			
			while (true) {
				byte[] receiveData = new byte[FeedbackPacket.PACKET_LENGTH];
				DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
				tcServerSocket.getUdpSocket().receive(receivePacket);
				
				FeedbackPacket feedbackPacket = new FeedbackPacket(receivePacket);
				
				switch (feedbackPacket.getElapsedTime()) {
				case -1: // SYN
					DataPacket synAckDataPacket = new DataPacket(-1, 0, 0, new byte[1000]);
					byte[] synDataPacketBytes = synAckDataPacket.constructBytes();
					tcServerSocket.getUdpSocket().send(new DatagramPacket(synDataPacketBytes,
							synDataPacketBytes.length, feedbackPacket
									.getSourceAddr(), feedbackPacket.getPort()));
					break;
				case -2: // ACK
					long currentTime = new Date().getTime();
					int initialRTT = (int) (currentTime - threadStartTime);
					ServerWorkerThread workerThread = new ServerWorkerThread(
							initialRTT, feedbackPacket.getSourceAddr(),
							feedbackPacket.getPort(), tcServerSocket);
					TinyControlSocket tcs = new TinyControlSocket(workerThread);
					ackQueue.add(tcs); // add to the queue
					break;
				case -3: // close
					MapHandler
							.getInstance()
							.get(feedbackPacket.getSourceAddr(),
									feedbackPacket.getPort()).shutDown();
				default: // feedback
					MapHandler
					.getInstance()
					.get(feedbackPacket.getSourceAddr(),
							feedbackPacket.getPort())
					.handleFeedBackPacket(feedbackPacket);
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