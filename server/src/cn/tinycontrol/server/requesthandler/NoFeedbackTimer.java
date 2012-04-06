package cn.tinycontrol.server.requesthandler;

import java.util.Timer;
import java.util.TimerTask;

import cn.tinycontrol.server.core.model.CurrentState;

public class NoFeedbackTimer {

	protected CurrentState curState;

	public int expireAfter;

	private Timer _timer;
	
	private TimerTask _timerTask;
	
	public NoFeedbackTimer(CurrentState curState) {
		this.curState = curState;
	}
	
    public Timer getNewTimer() {
        _timer = new Timer();
        return _timer;
    }
    
    public TimerTask getNewTimerTask() {
        _timerTask = new MyTimerTask(this);
        return _timerTask;
    }
    
    public void setToExpireAfter(int expireAfter) {
        this.expireAfter = expireAfter;
    }   
    
    public void startTimer() {
        if (_timer != null) {
            _timer.cancel();
        }
        getNewTimer();
        getNewTimerTask();
        _timer.schedule(_timerTask, expireAfter);
    }
    
    public void startTimer(int delay) {
        if (_timer != null) {
            _timer.cancel();
        }
        getNewTimer();
        getNewTimerTask();
        _timer.schedule(_timerTask, delay);
    }
    
    public void stopTimer() {
        _timer.cancel();
        _timer.purge();
    }
    
    public void restartTimer(int newDelay) {
        _timer.cancel();
        getNewTimer();
        getNewTimerTask();
        _timer.schedule(_timerTask, newDelay);
    }

}
