package pl.dabrowski.GameShop.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Entity
@Table(name = "tryby_gry", uniqueConstraints = @UniqueConstraint(name = "UK_name",columnNames = "nazwa"))
@Data
public class GameplayMode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "nazwa", unique = true)
	@Size(min = 1, max = 40)
	private String name;
	
	@JsonProperty
	public String print() {
		return name;
	}
}
