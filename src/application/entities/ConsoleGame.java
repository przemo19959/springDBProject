package application.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table(name = "gry_konsolowe")
@Data
public class ConsoleGame {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@Column(name = "tytul", unique = true, length = 200)
	private String title;
	@Temporal(TemporalType.DATE)
	@Column(name = "data_wydania")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dateOfRelease;

	@OneToOne
	@JoinColumn(name = "fk_wydawca")
	private Publisher publisher;
	@OneToOne
	@JoinColumn(name = "fk_producent")
	private Producer producer;
	@OneToOne
	@JoinColumn(name = "fk_jezyk")
	private Language language;
	@OneToOne
	@JoinColumn(name = "fk_tryb_gry")
	private GameplayMode gameplayMode;
	@OneToOne
	@JoinColumn(name = "fk_platforma_sprzetowa")
	private HardwarePlatform hardwarePlatform;
	@OneToOne
	@JoinColumn(name = "fk_gatunek")
	private Genre genre;
	@OneToOne
	@JoinColumn(name = "fk_kategoria_wiekowa")
	private AgeCategory ageCategory;
}
