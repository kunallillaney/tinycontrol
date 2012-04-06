import java.util.ArrayList;

public class PacketHistory {
	
	private static PacketHistory packetHistory = null;
	
	private ArrayList<ClientPacket> packetList;
	private int maxPacket;
	private int prevMaxPacket;
	
	protected PacketHistory(){
		
	}
	
	public static PacketHistory getInstance(){
		if(packetHistory == null){
			packetHistory = new PacketHistory();
			packetHistory.packetList = new ArrayList<ClientPacket>();
			packetHistory.maxPacket=0;
			packetHistory.prevMaxPacket = 0;
		}
		return packetHistory;
	}
	
	
	
	public void addPacket(ClientPacket clientPacket){
		packetList.add(clientPacket);
		//System.out.println("Packet successfully added to History");
		if(clientPacket.getDataPacket().getSequenceNumber()>maxPacket) {
		    prevMaxPacket = maxPacket;
			maxPacket = clientPacket.getDataPacket().getSequenceNumber();
		}
	}
	
	public ClientPacket getLastPacket(){
		
		return packetList.get(packetList.size()-1);//Returns second last packet
	}

	public int getMaxPacket() {
		return maxPacket;
	}

	public void setMaxPacket(int maxPacket) {
		this.maxPacket = maxPacket;
	}

    public int getPrevMaxPacket() {
        return prevMaxPacket;
    }

    public void setPrevMaxPacket(int prevMaxPacket) {
        this.prevMaxPacket = prevMaxPacket;
    }
	
}
