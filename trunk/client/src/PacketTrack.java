import java.util.TreeMap;


public class PacketTrack {
	
	TreeMap<Integer, NuPack> packetTrack = new TreeMap<Integer, NuPack>();
	LossIntervalList intervalList = new LossIntervalList();
	
	public void addPacket(Integer seqNo, NuPack nupack){
		packetTrack.put(seqNo, nupack);
	}
	
	public void removePacket(Integer seqNo){
		packetTrack.remove(seqNo);
	}
	
	public boolean isEmpty(){
		return packetTrack.isEmpty();
	}
	
	public boolean isExists(Integer seqNo){
		return packetTrack.containsKey(seqNo);
	}
	
	public TreeMap<Integer, NuPack> incrementPackValue(ClientPacket clientpacket){
		
		//System.out.println("Inside IncrementPackValue");
		TreeMap<Integer, NuPack> lossEventList = new TreeMap<Integer, NuPack>();
		for(int integer : packetTrack.navigableKeySet()){
			if(integer < clientpacket.getDataPacket().getSequenceNumber()){
				int temp = packetTrack.get(integer).getPackValue()+1;
				packetTrack.get(integer).setPackValue(temp);
				
				if(packetTrack.get(integer).getPackValue()>2){
					lossEventList.put(integer, packetTrack.get(integer));
					System.out.println(integer);
				}
			}
		}
		return lossEventList;
	}//End of method
	
	public int Dist(int sA, int sB){
		int sMax = (int)Math.pow(2, 32);
		return ((sA + sMax + sB) % sMax);
	
	} 
	
	public boolean CheckNewLossEvent(TreeMap<Integer, NuPack> list){
		
//		System.out.println("Inside NewCheckLossEvent");
		if(list.isEmpty()){
			return false;
		}
		else{
			for(int integer : packetTrack.navigableKeySet()){
				int sLoss = integer;
				int sBefore = packetTrack.get(integer).getBefore().getDataPacket().getSequenceNumber();
				int sAfter = packetTrack.get(integer).getAfter().getDataPacket().getSequenceNumber();
				long tBefore = packetTrack.get(integer).getBefore().getTime();
				long tAfter = packetTrack.get(integer).getAfter().getTime();
				
				long tLoss = tBefore + (((tAfter - tBefore) * Dist(sLoss, sBefore) / Dist(sAfter, sBefore)));
				long tOld = intervalList.getPreviousInterval().getStartTime();
				//Last Received Packet's RTT
				long RTT = PacketHistory.getInstance().getLastPacket().getDataPacket().getRTT();
				if( tOld + RTT >= tLoss){
					intervalList.addNewInterval(integer, tLoss);
					return true;
				}
				else{
					intervalList.getPreviousInterval().getListLossEvent().add(integer);
					return false;
				}
			}
			//return true;
		}
		return false;//TODO
		
	}//End of method
	
	public float CalculateLossRate(){
		return intervalList.CalculateLossRate();
	}
	
	public void FirstLossEvent(){
		
	}

    @Override
    public String toString() {
        return "PacketTrack [packetTrack=" + packetTrack + "]";
    }
	
}
