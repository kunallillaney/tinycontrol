package cn.tinycontrol.server.core.model;

import java.net.InetAddress;

public class ClientConnectionInfo {
	public InetAddress IPAddress;
	public int port;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((IPAddress == null) ? 0 : IPAddress.hashCode());
		result = prime * result + port;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientConnectionInfo other = (ClientConnectionInfo) obj;
		if (IPAddress == null) {
			if (other.IPAddress != null)
				return false;
		} else if (!IPAddress.equals(other.IPAddress))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
	
}
