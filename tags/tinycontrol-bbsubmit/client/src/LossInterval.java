import java.util.ArrayList;


public class LossInterval {
	
	private int startSeqNo;
	private long startTime;
	private ArrayList<Integer> listLossEvent;//Not sure
	private int numPackets;
	
	public LossInterval() {
		// TODO Auto-generated constructor stub
		startSeqNo = 0;
		startTime = 0;
		listLossEvent = new ArrayList<Integer>();
		numPackets=0;
	}
	
	public ArrayList<Integer> getListLossEvent() {
		return listLossEvent;
	}

	public void setListLossEvent(ArrayList<Integer> listLossEvent) {
		this.listLossEvent = listLossEvent;
	}

	public int getStartSeqNo() {
		return startSeqNo;
	}

	public void setStartSeqNo(int startSeqNo) {
		this.startSeqNo = startSeqNo;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getNumPackets() {
		return numPackets;
	}

	public void setNumPackets(int numPackets) {
		this.numPackets = numPackets;
	}

	

}
