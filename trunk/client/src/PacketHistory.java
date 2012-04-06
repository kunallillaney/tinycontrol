import java.util.ArrayList;

public class PacketHistory {
	
	private static PacketHistory packetHistory = null;
	
	private ArrayList<ClientPacket> packetList;
	private int maxPacket;
	
	protected PacketHistory(){
		
	}
	
	public static PacketHistory getInstance(){
		if(packetHistory == null){
			packetHistory = new PacketHistory();
			packetHistory.packetList = new ArrayList<ClientPacket>();
			packetHistory.maxPacket=0;
		}
		return packetHistory;
	}
	
	
	
	public void addPacket(ClientPacket clientPacket){
		packetList.add(clientPacket);
		System.out.println("Packet successfully added to History");
		if(clientPacket.getDataPacket().getSequenceNumber()>maxPacket)
			maxPacket = clientPacket.getDataPacket().getSequenceNumber();
	}
	
	public ClientPacket getLastPacket(){
		
		return packetList.get(packetList.size()-2);//Returns second last packet
	}

	public int getMaxPacket() {
		return maxPacket;
	}

	public void setMaxPacket(int maxPacket) {
		this.maxPacket = maxPacket;
	}
	
	

}
