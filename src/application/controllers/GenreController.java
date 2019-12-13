package application.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import application.dao.DatabaseDao;
import application.dtos.Record;
import application.dtos.Records;
import application.entities.Genre;
import application.services.MainService;

@Controller
public class GenreController {
	private MainService mainService;
	
	@Autowired
	public GenreController(MainService mainService) {
		this.mainService = mainService;
	}
		
	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public String showAll() throws ClassNotFoundException{
		return "index";
	}
	
	@GetMapping(value = {"/genre"})
	@ResponseBody
	public Genre getValue() {
		System.out.println("Otrzymano");
		Genre genre=new Genre();
		genre.setId(22);
		genre.setName("cos");
		return genre;
	}
	
	@RequestMapping(value = {"/{tableName}"}, method = RequestMethod.GET)
	public Records showAll(Model model, @PathVariable("tableName") String tableName){
//		System.out.println("Otrzymano zapytanie: ");
//		
//		Class<?> queryClass=Class.forName("application.entities."+tableName);
//		List<?> recordsInDB=databaseDao.findAll(queryClass);
//		
//		List<Record> records=new ArrayList<>();
//		if(recordsInDB.size()>0)
//			records.add(new Record(getHeadersNames(recordsInDB.get(0))));
//		for(int i=0;i<recordsInDB.size();i++) {
//			records.add(new Record(getValues(recordsInDB.get(i),i+1)));
//		}
//		return new Records(records);
		return null;
	}
		
	@RequestMapping(value = {"/list/{tableName}"}, method = RequestMethod.POST)
	@ResponseBody
	public String updateForm(@PathVariable("tableName") String tableName,
			@RequestBody String request){
//		System.out.println("Otrzymano POST! parametry: ["+request+"]");
//		String recordNumber=request.substring(0, request.indexOf("&"));
//		String[] params=request.substring(request.indexOf("&")+1).split("&");
//		System.out.println("Po split: "+Arrays.toString(params));
//		int idOfRecord=0;
//		try {
//			Class<?> queryClass=Class.forName("application.entities."+tableName);
////			Object newRecord=databaseDao.constructEntity(queryClass, params);
////			idOfRecord=databaseDao.save(newRecord);
//		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch(DataIntegrityViolationException dive) {
//			if(dive.getCause() instanceof ConstraintViolationException) {
//				ConstraintViolationException cve=(ConstraintViolationException) dive.getCause();
//				return "Error while updating: "+cve.getSQLException().getMessage();
//			}
//			return "Error while updating: "+dive.getMessage();
//		}
//		return "Successful update on record id:"+idOfRecord+",No:"+recordNumber;
		return null;
	}
			
	private List<String> getHeadersNames(Object record){
		List<String> result=new ArrayList<>();
		result.add("Lp.");
		String string=record.toString(); //pobierz string z nazwami kolumn oraz ich wartoœciami
		
		String[] parts=string.split("(\\[)|(, )|\\]");
		int lastIndex=0;
		for(int i=1;i<parts.length;i++) {
			lastIndex=parts[i].indexOf("=");
			result.add(parts[i].substring(0,lastIndex));
		}
		return result;
	}
	
	private List<String> getValues(Object record, int index){
		List<String> result=new ArrayList<>();
		result.add(index+"");
		String string=record.toString(); //pobierz string z nazwami kolumn oraz ich wartoœciami
		
		String[] parts=string.split("(\\[)|(, )|\\]");
		int firstIndex=0;
		for(int i=1;i<parts.length;i++) {
			firstIndex=parts[i].indexOf("=")+1;
			result.add(parts[i].substring(firstIndex));
		}
		return result;
	}	
}
