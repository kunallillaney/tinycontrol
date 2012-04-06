

public class TestTimerClass {
    
    public static void main(String[] args) {
        
        System.out.println(System.currentTimeMillis()+":"+"The fun begins!");
        MainTimerClass mtc = new MainTimerClass("!!A-Timer!!");
        mtc.startTimer(1000);
        System.out.println(System.currentTimeMillis()+":"+"MAIN: Sleeping for 2 seconds");
        try {
            Thread.sleep(2000);
            System.out.println(System.currentTimeMillis()+":"+"MAIN: Woke up!");
            //mtc.restartTimer(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
