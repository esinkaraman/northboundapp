package tr.edu.boun.cmpe.swe599.nothbound.info;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.SerializedName;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Rule {
	
	@XmlElement()
	@SerializedName("switch")
	private String switchStr;
	@XmlElement
	private String name;
	@XmlElement
	private String cookie = "0";
	@XmlElement
	private String priority = "32768";
	@XmlElement
	private String ipv4_dst;
	@XmlElement
	private String eth_type;
	@XmlElement
	private String active = "true";
	@XmlElement
	private String in_port;
	@XmlElement
	private String eth_dst;
	@XmlElement
	private String actions;


	public String getSwitchStr() {
		return switchStr;
	}

	public void setSwitchStr(String switchStr) {
		this.switchStr = switchStr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getIpv4_dst() {
		return ipv4_dst;
	}

	public void setIpv4_dst(String ipv4_dst) {
		this.ipv4_dst = ipv4_dst;
	}

	public String getEth_type() {
		return eth_type;
	}

	public void setEth_type(String eth_type) {
		this.eth_type = eth_type;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	public String getIn_port() {
		return in_port;
	}

	public void setIn_port(String in_port) {
		this.in_port = in_port;
	}

	// public static void main(String[] args) {
	// Request r = new Request();
	// r.setClientMAC("000000mac");
	// r.setQueryString("yahoo.com");
	// r.setSwitchId("1222000");
	//
	// Gson gson = new GsonBuilder().create();
	// String json = gson.toJson(r);
	// System.out.println(json);
	// }

	
	public String getEth_dst() {
		return eth_dst;
	}

	public void setEth_dst(String eth_dst) {
		this.eth_dst = eth_dst;
	}

}
