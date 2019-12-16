package application.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.services.DaoService;

@RestController
@RequestMapping(value="/mainPage")
public class MainRESTController {
	private DaoService daoService;
	
	@Autowired
	public MainRESTController(DaoService daoService) {
		this.daoService = daoService;
	}
			
	@GetMapping(value = {"/{tableName}"})
	public <T> List<T> findAll(@PathVariable String tableName) {
		return daoService.findAll(tableName);
	}
	
	@GetMapping(value = {"/{tableName}/{id}"})
	public <T> ResponseEntity<T> findAll(@PathVariable String tableName, @PathVariable int id) {
		return daoService.findById(tableName,id);
	}
	
	@PutMapping(value= {"/{tableName}/{id}"}, consumes="application/json")
	public void update(@PathVariable String tableName, @PathVariable int id, @RequestBody Object entity) {
		daoService.update(tableName,entity);
	}
	
	@PostMapping(value= {"/{tableName}"})
	public <T> void save(@PathVariable String tableName, @RequestBody T entity) {
		daoService.save(entity);
	}
}
