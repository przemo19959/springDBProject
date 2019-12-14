package application.dao;

import static org.junit.Assert.assertNotNull;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import application.configs.DBServiceConfig;
import application.entities.Genre;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes= {DBServiceConfig.class})
@Transactional
class DatabaseDaoTest {
//	private static SessionFactory sessionFactory;
////	private DatabaseDao mockDao=new DatabaseDaoImpl(sessionFactory);
//	
//	@Autowired
//	public void setSessionFactory(SessionFactory sessionFactory) {
//		DatabaseDaoTest.sessionFactory = sessionFactory;
//		//ta metoda obchodzi wi¹zanie na polu, bo nie dzia³a dla pola statycznego
//	}
//
//	@Test
//	@DisplayName("Session is not null")
//	void test0() {
//		assertNotNull(sessionFactory);
//	}
//	
//	@Test
//	@DisplayName("updating record works fine")
//	void test1() throws InstantiationException, IllegalAccessException {
////		Genre genre=new Genre();
////		genre.setName("mockGenre");
////		mockDao.save(genre); //dodaj do bazy
////		
////		List<Genre> genres=mockDao.find(genre, Genre.class);
////		genres.get(0).setName("mock2");
////		
//////		Object result=mockDao.constructEntity(Genre.class,"0","mockGenre");
//////		mockDao.save(result);
////		for(Genre g:genres)
////			mockDao.delete(g);
//	}
//	
//	@Test
//	void test2() {
//		Genre genre1=new Genre();
//		genre1.setId(15); genre1.setName("horror");
//		mockDao.save(genre1);
//		for(Genre gen:mockDao.findAll(Genre.class))
//			System.out.println(gen);
//	}
//	
//	@AfterAll
//	static void cleanUp() {
//		sessionFactory.close();
//	}
}
