import java.util.ArrayList;


public class LossIntervalList {
	
	private ArrayList<LossInterval> intervalList = new ArrayList<LossInterval>();
	private double[] weights = {1.0,1.0,1.0,1.0,0.8,0.6,0.4};
	
	public LossInterval getPreviousInterval(){
		return intervalList.get(intervalList.size()-1);
	}
	
	public void addFirstInterval(Integer seqNo, long seqTime){
	    LossInterval curInterval = new LossInterval();
	    curInterval.setStartSeqNo(seqNo);
	    curInterval.setStartTime(seqTime);
	    intervalList.add(curInterval);
	}
	
	public void addNewInterval(Integer seqNo, long seqTime){
		LossInterval curInterval = new LossInterval();
		curInterval.setStartSeqNo(seqNo);
		curInterval.setStartTime(seqTime);
		LossInterval prevInterval = this.getPreviousInterval();
		//Setting Previous Interval number of Packets
		prevInterval.setNumPackets(prevInterval.getStartSeqNo()-curInterval.getStartSeqNo());
		curInterval.setNumPackets(PacketHistory.getInstance().getMaxPacket() -curInterval.getStartSeqNo());
		intervalList.add(curInterval);
	}
	
	public float CalculateLossRate(){
		float iTot=0, wTot=0, iMean=0, p=0;
		for(int i=0;i<8 && i<intervalList.size();i++){
			wTot = (float) (wTot + weights[i]);
		}
		for(int i=0;i<8 && i<intervalList.size();i++){
			iTot = (float) (iTot + (intervalList.get(intervalList.size()-1-i).getStartTime() - TinyClientSocket.startTime)*weights[i]);
		}
		iMean = iTot/wTot;
		p=1/iMean;
		
		return p;
	}

	public ArrayList<LossInterval> getIntervalList() {
		return intervalList;
	}

	public void setIntervalList(ArrayList<LossInterval> intervalList) {
		this.intervalList = intervalList;
	}
	
}
