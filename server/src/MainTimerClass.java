import java.util.Timer;
import java.util.TimerTask;


public class MainTimerClass {
    
    private Timer _timer;
    
    private TimerTask _timerTask;
    
    private String initMeOnce;
    
    public MainTimerClass(String initMeOnce) {
        super();
        this.initMeOnce = initMeOnce;
    }

    public Timer getTimer() {
        return _timer;
    }
    
    public Timer getNewTimer() {
        _timer = new Timer();
        return _timer;
    }
    
    public TimerTask getNewTimerTask() {
        _timerTask = new MyTimerTask(initMeOnce, this);
        return _timerTask;
    }
    
    public void startTimer(int delay) {
        if (_timer != null) {
            _timer.cancel();
        }
        getNewTimer().schedule(getNewTimerTask(), delay);
        //getNewTimer();
        //getNewTimerTask();
        //_timer.schedule(_timerTask, delay);
    }
    
    public void restartTimer(int newDelay) {
        _timer.cancel();
        getNewTimer();
        getNewTimerTask();
        _timer.schedule(_timerTask, newDelay);
    }
}
