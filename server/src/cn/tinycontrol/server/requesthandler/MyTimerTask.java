package cn.tinycontrol.server.requesthandler;

import cn.tinycontrol.common.model.DataPacket;
import cn.tinycontrol.server.common.ServerConstants;

public class MyTimerTask extends java.util.TimerTask implements ServerConstants{
	
    NoFeedbackTimer myParent;
    
	public MyTimerTask(NoFeedbackTimer noFeedbackTimer) {
	    myParent = noFeedbackTimer;
    }

    public void run() {
		float maxRecv = myParent.curState.getMax();
		if (!myParent.curState.isAnyFeedbackPacketSeen()) { // server did not receive any feedback
		    myParent.curState.X = Math.max(myParent.curState.X/2, DataPacket.PAYLOAD_LENGTH/t_mbi);
		} else if (myParent.curState.last_p == 0) {
		    myParent.curState.X = Math.max(myParent.curState.X/2, DataPacket.PAYLOAD_LENGTH/t_mbi);
		} else if (myParent.curState.last_XBps > 2 * maxRecv) {
			updateLimit(maxRecv);
		} else {
			updateLimit(myParent.curState.last_XBps / 2);
		}
		
		float time=Math.max(4*myParent.curState.R, 2*DataPacket.PAYLOAD_LENGTH/myParent.curState.X);
		myParent.getNewTimer().schedule(myParent.getNewTimerTask(), (int)time);
	}
	
	private void updateLimit(float tempxRecv) {
		if (tempxRecv<DataPacket.PAYLOAD_LENGTH/t_mbi){
			tempxRecv=DataPacket.PAYLOAD_LENGTH/t_mbi;
		}
		myParent.curState.addOnlyThis(tempxRecv/2);
		ServerWorkerThread.runAlgorithmOne(myParent.curState, null);
	}
	
}
