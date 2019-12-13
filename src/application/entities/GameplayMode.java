package application.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tryb_gry")
public class GameplayMode {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@Column(name="nazwa", unique=true)
	private String name;
	
	//@formatter:off
	public int getId() {return id;}
	public void setId(int id) {this.id = id;}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	//@formatter:on
	
	@Override
	public String toString() {
		return "GameplayMode [id=" + id + ", name=" + name + "]";
	}
}
