package cn.tinycontrol.server.requesthandler;

import java.util.Timer;

import cn.tinycontrol.common.model.DataPacket;
import cn.tinycontrol.server.common.ServerConstants;
import cn.tinycontrol.server.core.model.CurrentState;

public class MyTimerTask extends java.util.TimerTask implements ServerConstants{
	
	CurrentState curState;
	
	Timer myParentTimer;
	
	public MyTimerTask(CurrentState curState, Timer timerExpire) {
		this.curState = curState;
		this.myParentTimer = timerExpire; 
	}
	
	public void run() {
		float maxRecv = curState.getMax();
		if (!curState.isAnyFeedbackPacketSeen()) { // server did not receive any feedback
			curState.X = Math.max(curState.X/2, DataPacket.PAYLOAD_LENGTH/t_mbi);
		} else if (curState.last_p == 0) {
			curState.X = Math.max(curState.X/2, DataPacket.PAYLOAD_LENGTH/t_mbi);
		} else if (curState.last_XBps > 2 * maxRecv) {
			updateLimit(maxRecv);
		} else {
			updateLimit(curState.last_XBps / 2);
		}
		
		float time=Math.max(4*curState.R, 2*DataPacket.PAYLOAD_LENGTH/curState.X);
		myParentTimer.schedule(this,(long)time);
	}
	
	private void updateLimit(float tempxRecv) {
		if (tempxRecv<DataPacket.PAYLOAD_LENGTH/t_mbi){
			tempxRecv=DataPacket.PAYLOAD_LENGTH/t_mbi;
		}
		curState.addOnlyThis(tempxRecv/2);
		ServerWorkerThread.runAlgorithmOne(curState, null);
	}
	
}
