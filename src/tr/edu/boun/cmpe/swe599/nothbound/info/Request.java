package tr.edu.boun.cmpe.swe599.nothbound.info;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Request {

	@XmlElement
	protected String queryString;
	@XmlElement
	protected String switchId;
	@XmlElement
	protected String clientMAC;
	@XmlElement
	protected String ingressPort;

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getSwitchId() {
		return switchId;
	}

	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}

	public String getClientMAC() {
		return clientMAC;
	}

	public void setClientMAC(String clientMAC) {
		this.clientMAC = clientMAC;
	}

	public String getIngressPort() {
		return ingressPort;
	}

	public void setIngressPort(String ingressPort) {
		this.ingressPort = ingressPort;
	}

	@Override
	public String toString() {
		return "Request [queryString=" + queryString + ", switchId=" + switchId
				+ ", clientMAC=" + clientMAC + ", ingressPort=" + ingressPort
				+ "]";
	}

}
