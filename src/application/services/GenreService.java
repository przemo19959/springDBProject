package application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import application.dao.Dao;
import application.entities.Genre;

@Service
public class GenreService {
	private Dao<Genre> genreDao;
	
	@Autowired
	public GenreService(Dao<Genre> genreDao) {
		this.genreDao = genreDao;
	}
	
	public List<Genre> findAll() {
		return genreDao.findAll();
	}
	
	public ResponseEntity<Genre> findById(int id) {
		Optional<Genre> result=genreDao.findById(id);
		if(result.isPresent())
			return ResponseEntity.ok(result.get());
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
