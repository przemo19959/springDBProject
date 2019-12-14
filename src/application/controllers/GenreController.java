package application.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.entities.Genre;
import application.services.GenreService;

@RestController
@RequestMapping(value="/mainPage")
public class GenreController {
	private GenreService genreService;
	
	@Autowired
	public GenreController(GenreService genreService) {
		this.genreService = genreService;
	}
			
	@GetMapping(value = {"/genre"})
	public List<Genre> findAll() {
		return genreService.findAll();
	}
	
	@GetMapping(value = {"/genre/{id}"})
	public ResponseEntity<Genre> findAll(@PathVariable int id) {
		return genreService.findById(id);
	}
	
	
}
