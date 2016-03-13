package tr.edu.boun.cmpe.swe599.nothbound.info;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ControllerResponse {
	//{"status" : "Entry pushed"}
	@XmlElement
	protected String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ControllerResponse [status=" + status + "]";
	}
	
	
}
