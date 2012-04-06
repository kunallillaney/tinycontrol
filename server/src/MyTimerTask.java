import java.util.TimerTask;


public class MyTimerTask extends TimerTask {

    private String strToDisplay;
    
    private MainTimerClass mtc;
    
    public MyTimerTask(String strToDisplay, MainTimerClass mtc) {
        this.mtc = mtc;
        this.strToDisplay = strToDisplay;
    }
    
    @Override
    public void run() {
        System.out.println(System.currentTimeMillis()+": I am in "+strToDisplay+" task!");
        System.out.println(System.currentTimeMillis()+": Trying to reschedule another timer...");
        //mtc.getTimer().cancel();
        //mtc.getNewTimer().schedule(mtc.getNewTimerTask(), 1000);
        mtc.startTimer(1000);
        System.out.println(System.currentTimeMillis()+": Scheduling done.");
    }

}
