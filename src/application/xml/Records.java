package application.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="records")
public class Records {
	@XmlElement
	private List<Record> record;
	
	public Records() {
	}
	
	public Records(List<Record> record) {
		this.record = record;
	}

	public void setRecord(List<Record> record) {
		this.record = record;
	}
}
