import java.io.IOException;
import java.util.TimerTask;

import cn.tinycontrol.common.model.FeedbackPacket;


public class FeedbackTask extends TimerTask{

	TinyClientSocket tinySocket;
	TinyClientThread tinyThread;
	public FeedbackTask(TinyClientSocket socket, TinyClientThread thread) {
		tinySocket = socket;
		tinyThread = thread;
	}
	
	@Override
	public void run() {
		
		long elapsedTime = (tinySocket.startTime-tinyThread.packetHistory.getLastPacket().getTime()) - System.currentTimeMillis();
		long recvRate = tinyThread.getxRecvRate();
		long tStamp = tinyThread.packetHistory.getLastPacket().getDataPacket().getTimeStamp();
		FeedbackPacket feedbackPacket = new FeedbackPacket((int)tStamp, (int)elapsedTime, recvRate, tinyThread.lossRate);
		try {
			tinySocket.sendPacket(feedbackPacket);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Sending Feedback packet");
		tinyThread.setxRecv(0);
		RTT.setRTT();
	}

}
