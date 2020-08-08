package pl.dabrowski.GameShop.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name = "kategorie_wiekowe", uniqueConstraints = @UniqueConstraint(name = "UK_name",columnNames = "nazwa"))
@Data
public class AgeCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "nazwa", unique = true)
	@Size(min = 1, max = 30)
	private String name;
}
