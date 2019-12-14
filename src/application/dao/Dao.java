package application.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
	// CRUD operations
	// C-Create
	public int save(T entity);

	// R-Retrieve
	public List<T> findAll();
	public Optional<T> findById(int id);

	// U-Update
	public void update(T entity);

	// D-Delete
	public void delete(T entity);

//	public <T> List<T> find(Object entity, Class<T> resultType, boolean onlyUniqueFields);

//	public <T> T constructEntity(Class<?> type, String... params)
//			throws InstantiationException, IllegalAccessException;
}
