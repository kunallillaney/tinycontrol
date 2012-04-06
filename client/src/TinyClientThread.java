import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import cn.tinycontrol.common.model.DataPacket;


public class TinyClientThread implements Runnable{

	TinyClientSocket tinySocket;
	PacketHistory packetHistory = PacketHistory.getInstance();
	PacketTrack packetTrack = new PacketTrack();
	TreeMap<Integer, NuPack> lossList;
	static float xRecv = 0;
	float xRecvRate = 0;
	float lossRate=0, prevLossRate=0;
	boolean KeepAlive = true;
	static boolean timerOFF = true;
	
	Timer _timer;
	TimerTask _timerTask;
	
	public Timer getNewTimer() {
		_timer = new Timer();
		return _timer;
	}
	
	public TimerTask getNewTimerTask() {
		_timerTask = new FeedbackTask(this);
		return _timerTask;
	}
	
	public void startTimer(int delay) {
        if (_timer != null) {
            _timer.cancel();
        }
		getNewTimer().schedule(getNewTimerTask(), delay);
	}
	
	public void stopTimer() {
		_timer.cancel();
	}
	
	public void restartTimer(int delay) {
		_timer.cancel();
		_timer.purge();
		getNewTimer().schedule(getNewTimerTask(), delay);
	}
	
	public TinyClientThread(TinyClientSocket socket) {
		// TODO Auto-generated constructor stub
		tinySocket = socket;
		lossList = new TreeMap<Integer, NuPack>();
	}
	
	public float getxRecv() {
		return xRecv;
	}

	public void setxRecv(long xRecv) {
		this.xRecv = xRecv;
	}

	public void setKeepAlive(boolean alive){
		this.KeepAlive = alive;
	}
	@Override
	public void run() {
		
		try {
			while(this.KeepAlive){
				DataPacket receivePacket = tinySocket.receivePacket();
				//System.out.println("Data Received");
				xRecv++;
				//Get data from the packet and send it to the application above
				ClientPacket clientPacket = new ClientPacket(receivePacket);
				
				//Start Timer thread
				if(timerOFF){
					//RTT.setRTT();
				    RTT.RTT = clientPacket.getDataPacket().getRTT();
					startTimer(receivePacket.getRTT());
//					System.out.println("Timer thread started");
					timerOFF=false;
				}
				
				//System.out.println("Packet added to History");
				
				//Checking for Loss Events
				if(CheckLossEvent(clientPacket)){
//					System.out.println("Packet Loss Detected");
					if(packetTrack.intervalList.getIntervalList().isEmpty()) {
//                        System.out.println("First Loss Detected");
                        xRecvRate = (float)xRecv / RTT.getRTT();
                        lossRate = (float) ((3 * Math.pow(1000, 2)) / (2 * Math.pow(RTT.getRTT(), 2) * Math.pow(0.05 * xRecvRate, 2)));
                        packetTrack.intervalList.addFirstInterval(clientPacket.getDataPacket().getSequenceNumber(), clientPacket.getTime());
                    }
					/*if(lossList.isEmpty()){
						//First Loss Event
						
					}*/
					//Sending TreeMap to check for NewLossEvent
					else if(packetTrack.CheckNewLossEvent(packetTrack.incrementPackValue(clientPacket))){
					    
						lossRate = packetTrack.CalculateLossRate();
//						System.out.println("Loss Rate: "+lossRate);
						if(lossRate>prevLossRate){
							xRecvRate = xRecv/RTT.getRTT();
							//Expire Feedback Timer
							//timer.schedule(feedbackTask, 0);
							restartTimer(0);
						}
						prevLossRate = lossRate;
					}//End of inner if
				}//End of outer if
                packetHistory.addPacket(clientPacket);//Adding to History
			}//End of while
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	public void start(TinyClientSocket socket) {

		tinySocket = socket;
		this.run();
	}
	
	public void FeedbackExpire(){
		
	}
	
	public boolean CheckLossEvent(ClientPacket clientPacket){
		//System.out.println("Inside CheckLossEvent");
		if(clientPacket.getDataPacket().getSequenceNumber()==(packetHistory.getMaxPacket()+1) && packetTrack.isEmpty()){
			
			return false;
		}
		else if(packetTrack.isExists(clientPacket.getDataPacket().getSequenceNumber())){
			packetTrack.removePacket(clientPacket.getDataPacket().getSequenceNumber());
			return false;
		}
		else{
			makeNduPack(clientPacket);
//			System.out.println(packetTrack);
			lossList = packetTrack.incrementPackValue(clientPacket);
			if(lossList.isEmpty())
				return false;
			else
				return true;
		}
		
	}
	
	public void makeNduPack(ClientPacket clientPacket){
		//System.out.println("Inside MakeNDUPACK");
		for(int i=packetHistory.getMaxPacket()+1;i<clientPacket.getDataPacket().getSequenceNumber();i++){
			NuPack nuPack = new NuPack(packetHistory.getLastPacket(), clientPacket);
			packetTrack.addPacket(i, nuPack);
		}
	}
	
}
