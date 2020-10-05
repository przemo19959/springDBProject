package pl.dabrowski.GameShop.entities;

import java.text.MessageFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import pl.dabrowski.GameShop.entities.converters.ShortcutConverter;

@Entity
@Table(name = "jezyki", uniqueConstraints = @UniqueConstraint(name = "UK_name", columnNames = "nazwa"))
@Data
public class Language {
	private static final String PRINT_PATTERN = "{0}({1})";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "nazwa", unique = true)
	@Size(min = 1, max = 50)
	@NotNull
	private String name;

	@Column(name = "skrot")
	@Size(min = 1, max = 10)
	@NotNull
	@Convert(converter = ShortcutConverter.class)
	private String shortcut;

	@JsonProperty
	public String print() {
		return MessageFormat.format(PRINT_PATTERN, name, shortcut);
	}
}
