import java.io.IOException;
import java.net.DatagramPacket;
import java.util.TimerTask;

import cn.tinycontrol.common.model.DataPacket;
import cn.tinycontrol.common.model.FeedbackPacket;

public class FeedbackTask extends TimerTask {

	TinyClientThread myParent;

	public FeedbackTask(TinyClientThread thread) {
		myParent = thread;
	}

	@Override
	public void run() {
		if(myParent.getxRecv()!=0){
			
//			System.out.println("Sending Feedback packet:"+RTT.getRTT());
			long elapsedTime = (System.currentTimeMillis() - myParent.packetHistory
					.getLastPacket().getTime());
			float recvRate = ((float)myParent.getxRecv() * DataPacket.PAYLOAD_LENGTH / RTT.getRTT());
			long tStamp = myParent.packetHistory.getLastPacket().getDataPacket()
					.getTimeStamp();
			FeedbackPacket feedbackPacket = new FeedbackPacket((int) tStamp,
					(int) elapsedTime, recvRate, myParent.lossRate);
			try {
				myParent.tinySocket.sendPacket(feedbackPacket);
//				System.out.println(feedbackPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		RTT.setRTT();
		myParent.setxRecv(0);
		myParent.startTimer((int)RTT.getRTT());
		//System.out.println("End of feedback"+ RTT.getRTT());
		// tinyThread.timer.schedule(this, (long));
	}

}
