package pl.dabrowski.GameShop.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name = "jezyki",uniqueConstraints = @UniqueConstraint(name = "UK_name",columnNames = "nazwa"))
@Data
public class Language {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "nazwa", unique = true)
	@Size(min = 1, max = 50)
	private String name;

	@Column(name = "skrot")
	@Size(min = 1, max = 10)
	private String shortcut;
}