package application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.DatabaseDao;

@Service
public class MainService {
	private DatabaseDao databaseDao;
	
	@Autowired
	public MainService(DatabaseDao databaseDao) {
		this.databaseDao = databaseDao;
	}
	
	
	
	
}
