package application.dtos;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="record")
public class Record {
	@XmlElement
	private List<String> column;
	
	public Record() {
	}
	
	public Record(List<String> column) {
		this.column = column;
	}
	
	public void setColumn(List<String> column) {
		this.column = column;
	}

	@Override
	public String toString() {
		return "Record [column=" + column + "]";
	}
}
