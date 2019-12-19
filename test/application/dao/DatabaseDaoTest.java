package application.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
		dao.save(genre);
		assertTrue(genre.getId()>0);
		List<Genre> genres=dao.findAll(Genre.class);
		assertTrue(genres.size()==1);
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
		
		dao.save(genre);
		Optional<Genre> genre3=dao.findById(genre.getId(), Genre.class);
		assertEquals(genre.getId(), genre3.get().getId());
		assertEquals(genre.getName(), genre3.get().getName());
	}	
}
