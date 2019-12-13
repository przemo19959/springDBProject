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

@Entity
@Table(name = "gra_konsolowa")
public class ConsoleGame {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@Column(name = "tytul",unique=true)
	private String title;
	@Temporal(TemporalType.DATE)
	@Column(name = "data_wydania")
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
	private HardwarePlatform platform;
	@OneToOne
	@JoinColumn(name = "fk_gatunek")
	private Genre genre;
	@OneToOne
	@JoinColumn(name = "fk_kategoria_wiekowa")
	private AgeCategory ageCategory;
	
	//@formatter:off
	public int getId() {return id;}
	public void setId(int id) {this.id = id;}
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	public Date getDateOfRelease() {return dateOfRelease;}
	public void setDateOfRelease(Date dateOfRelease) {this.dateOfRelease = dateOfRelease;}
	public Publisher getPublisher() {return publisher;}
	public void setPublisher(Publisher publisher) {this.publisher = publisher;}
	public Producer getProducer() {return producer;}
	public void setProducer(Producer producer) {this.producer = producer;}
	public Language getLanguage() {return language;}
	public void setLanguage(Language language) {this.language = language;}
	public GameplayMode getGameplayMode() {return gameplayMode;}
	public void setGameplayMode(GameplayMode gameplayMode) {this.gameplayMode = gameplayMode;}
	public HardwarePlatform getPlatform() {return platform;}
	public void setPlatform(HardwarePlatform platform) {this.platform = platform;}
	public Genre getGenre() {return genre;}
	public void setGenre(Genre genre) {this.genre = genre;}
	public AgeCategory getAgeCategory() {return ageCategory;}
	public void setAgeCategory(AgeCategory ageCategory) {this.ageCategory = ageCategory;}
	//@formatter:on

	@Override
	public String toString() {
		return "ConsoleGame [id=" + id + ", title=" + title + ", dateOfRelease=" + dateOfRelease + ", publisher="
				+ publisher.getName() + ", producer=" + producer.getName() + ", language=" + language.getName() + "("
				+ language.getShortcut() + "), gameplayMode=" + gameplayMode.getName() + ", platform="
				+ platform.getName() + "(" + platform.getShortcut() + "), genre=" + genre.getName() + ", ageCategory="
				+ ageCategory.getName() + "]";
	}
}
