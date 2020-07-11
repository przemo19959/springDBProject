package application.dao;

import java.util.List;

public interface DatabaseDao {
	public <T> List<T> findAll(Class<T> resultType);
	public <T> T findById(int id, Class<T> resultType);
	public int save(Object entity);
	public void delete(Object entity);
	public <T> List<T> find(Object entity, Class<T> resultType, boolean onlyUniqueFields);
	
	public <T> T constructEntity(Class<?> type, String... params)
			throws InstantiationException, IllegalAccessException;
}