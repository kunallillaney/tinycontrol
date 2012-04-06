
public class NuPack {
	
	private ClientPacket before;
	private ClientPacket after;
	private int packValue;
	
	public NuPack(ClientPacket pack1, ClientPacket pack2) {
		
		before = pack1;
		after = pack2;
		packValue = 0;
	}

	public ClientPacket getBefore() {
		return before;
	}

	public void setBefore(ClientPacket before) {
		this.before = before;
	}

	public ClientPacket getAfter() {
		return after;
	}

	public void setAfter(ClientPacket after) {
		this.after = after;
	}

	public int getPackValue() {
		return packValue;
	}

	public void setPackValue(int packValue) {
		this.packValue = packValue;
	}
	
}
