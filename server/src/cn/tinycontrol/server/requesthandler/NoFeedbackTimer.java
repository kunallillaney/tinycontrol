package cn.tinycontrol.server.requesthandler;

import java.util.Timer;

import cn.tinycontrol.server.core.model.CurrentState;

public class NoFeedbackTimer {

	private CurrentState curState;

	public int expireAfter;

	Timer timerExpire = null;

	public NoFeedbackTimer(CurrentState curState) {
		this.curState = curState;
		this.timerExpire = new Timer();
	}
	
	public void setExpireAfter(int expireAfter) {
		this.expireAfter = expireAfter;
	}

	public void startTimer() {
		MyTimerTask timerTask = new MyTimerTask(curState, timerExpire);
		timerExpire.schedule(timerTask ,(System.currentTimeMillis()+expireAfter));		
	}

	public void resetTimer(long time) {
		timerExpire.cancel();
		MyTimerTask timerTask = new MyTimerTask(curState, timerExpire);
		timerExpire.schedule(timerTask , (long) (System.currentTimeMillis()+time));
	}
}
