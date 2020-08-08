package pl.dabrowski.GameShop.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "gry_konsolowe", uniqueConstraints = @UniqueConstraint(name = "UK_title", columnNames = "tytul"))
@Data
public class ConsoleGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "tytul", unique = true)
    @Size(min = 1, max = 200)
    private String title;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_wydania")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfRelease;

    @OneToOne
    @JoinColumn(name = "fk_wydawca", foreignKey = @ForeignKey(name = "FK_publisher"))
    private Publisher publisher;

    @OneToOne
    @JoinColumn(name = "fk_producent", foreignKey = @ForeignKey(name = "FK_producer"))
    private Producer producer;

    @OneToOne
    @JoinColumn(name = "fk_jezyk", foreignKey = @ForeignKey(name = "FK_language"))
    private Language language;

    @OneToOne
    @JoinColumn(name = "fk_tryb_gry", foreignKey = @ForeignKey(name = "FK_gameplayMode"))
    private GameplayMode gameplayMode;

    @OneToOne
    @JoinColumn(name = "fk_platforma_sprzetowa", foreignKey = @ForeignKey(name = "FK_hardwarePlatoform"))
    private HardwarePlatform hardwarePlatform;

    @OneToOne
    @JoinColumn(name = "fk_gatunek", foreignKey = @ForeignKey(name = "FK_genre"))
    private Genre genre;

    @OneToOne
    @JoinColumn(name = "fk_kategoria_wiekowa", foreignKey = @ForeignKey(name = "FK_ageCategory"))
    private AgeCategory ageCategory;
}
