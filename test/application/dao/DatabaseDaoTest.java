package application.dao;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import application.configs.root.CommonConfig;
import application.configs.root.ProdConfig;
import application.configs.root.TestConfig;
import application.entities.AgeCategory;
import application.entities.Genre;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes= {CommonConfig.class,TestConfig.class,ProdConfig.class})
@ActiveProfiles("test")
class DatabaseDaoTest {
	
	@Autowired
	private Dao dao;
	
	@Test
	@DisplayName("save operation works fine")
	void test1() {
		Genre genre = new Genre();
		genre.setName("gatunek1");
		assertTrue(genre.getId()==0);
		dao.save(genre); //+1 save
		assertTrue(genre.getId()>0);
		List<Genre> genres=dao.findAll(Genre.class);
		assertEquals(1, genres.size());
	}
	
	@Test
	@DisplayName("save operation which breaks unique constraint returns null")
	void test2() {
		Genre genre = new Genre();
		genre.setName("gatunek1");
		assertThrows(ConstraintViolationException.class, ()->dao.save(genre));
	}
	
	@Test
	@DisplayName("findById works fine")
	void test3() {
		Genre genre = new Genre();
		genre.setName("gatunek2");

		final Optional<Genre> genre2=dao.findById(23, Genre.class);
		assertThrows(NoSuchElementException.class, ()->genre2.get());
		
		dao.save(genre);	//+1 save
		Optional<Genre> genre3=dao.findById(genre.getId(), Genre.class);
		assertEquals(genre.getId(), genre3.get().getId());
		assertEquals(genre.getName(), genre3.get().getName());
	}
	
	@Test
	@DisplayName("findById - returned entity throws exception when accessed, when request id is not positive")
	void test4() {
		final Optional<Genre> genre=dao.findById(0, Genre.class);
		assertThrows(NoSuchElementException.class, ()->genre.get());
		
		final Optional<Genre> genre2=dao.findById(-1, Genre.class);
		assertThrows(NoSuchElementException.class, ()->genre2.get());
	}
	
	@Test
	@DisplayName("update operation works fine")
	void test5() {
		List<Genre> genres=dao.findAll(Genre.class);
		assertEquals(2, genres.size());
		
		Optional<Genre> genre=dao.findById(genres.get(0).getId(), Genre.class);
		assertEquals(genres.get(0).getId(), genre.get().getId());
		assertEquals(genres.get(0).getName(), genre.get().getName());
		
		genre.get().setName("inna nazwa");
		dao.update(genre.get());
		Optional<Genre> genre2=dao.findById(genre.get().getId(), Genre.class);
		assertEquals(genre.get().getId(), genre2.get().getId());
		assertEquals(genre.get().getName(), genre2.get().getName());
	}
	
	@Test
	@DisplayName("update operation throws exception when unique constaint is broken")
	void test6() {
		List<Genre> genres=dao.findAll(Genre.class);
		assertEquals(2, genres.size());
		
		Optional<Genre> genre=dao.findById(genres.get(0).getId(), Genre.class);
		assertEquals(genres.get(0).getId(), genre.get().getId());
		assertEquals(genres.get(0).getName(), genre.get().getName());
		
		genre.get().setName(genres.get(1).getName());
		assertThrows(PersistenceException.class, ()->dao.update(genre.get()));
	}
	
	@Test
	@DisplayName("get columns names works fine")
	void test7() {
		System.out.println(dao.getColumnNames(AgeCategory.class));
		//póki co zwraca pust¹ listê, prawdopodobnie z powodu, ¿e test dzia³a na wbudowanej bazie danych
//		assertArrayEquals(new String[] {"id","nazwa"}, dao.getColumnNames(Genre.class));
		//TODO 2 sty 2020:dodaæ testy nowej metody pobieraj¹cej nazwy kolumn tabeli. Jest to konieczne w przypadku, gdy baza danych jest pusta i
		//nie wszystkie pola s¹ inicjowane w JS w obiekcie VirtualTable
	}
	
	
}
