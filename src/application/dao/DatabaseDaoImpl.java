package application.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("databaseDao")
public class DatabaseDaoImpl implements DatabaseDao {
	private SessionFactory sessionFactory;

	@Autowired
	public DatabaseDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional(readOnly = true)
	@Override
	public <T> List<T> findAll(Class<T> resultType) {
		return sessionFactory.getCurrentSession()
				.createNativeQuery("SELECT * FROM " + getTableName(resultType) + ";", resultType).list();
	}

	@Transactional(readOnly = true)
	@Override
	public <T> T findById(int id, Class<T> resultType) {
		return sessionFactory.getCurrentSession()
				.createNativeQuery("SELECT * FROM " + getTableName(resultType) + " WHERE id=" + id + ";", resultType)
				.uniqueResult();
	}

	@Override
	public int save(Object entity) {
		System.out.println("To update: "+entity);
//		List<?> alreadyInDB=find(entity, entity.getClass(),true);
//		if(!alreadyInDB.contains(entity))
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
		return getRecordId(entity);
	}

	@Override
	public void delete(Object entity) {
		sessionFactory.getCurrentSession().delete(entity);
	}

	@Transactional(readOnly = true)
	@Override
	public <T> List<T> find(Object entity, Class<T> resultType, boolean onlyUniqueFields) {
		String query = getWHEREQuery(entity,onlyUniqueFields);
		System.out.println(query);
		return sessionFactory.getCurrentSession().createNativeQuery(query, resultType).list();
	}

	private String getWHEREQuery(Object entity, boolean onlyUniqueFields) {
		Field[] fields = entity.getClass().getDeclaredFields();
		List<String> queryParts = new ArrayList<>();
		try {
			for (Field field : fields) {
				if (!field.isAnnotationPresent(Id.class)) {
					field.setAccessible(true);
					if (field.isAnnotationPresent(Column.class)) {
						if(onlyUniqueFields && field.getAnnotation(Column.class).unique())
							queryParts.add(field.getAnnotation(Column.class).name() + "='" + field.get(entity) + "'");
						else if(!onlyUniqueFields)
							queryParts.add(field.getAnnotation(JoinColumn.class).name() + "=" + field.get(entity) + "'");
					} else if (field.isAnnotationPresent(JoinColumn.class)) {
						if(onlyUniqueFields && field.getAnnotation(JoinColumn.class).unique())
							queryParts.add(field.getAnnotation(JoinColumn.class).name() + "=" + field.get(entity) + "'");
						else if(!onlyUniqueFields)
							queryParts.add(field.getAnnotation(JoinColumn.class).name() + "=" + field.get(entity) + "'");
					}
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return "SELECT * FROM " + getTableName(entity.getClass()) + " WHERE "
				+ queryParts.stream().collect(Collectors.joining(" and ")) + ";";
	}

	@Override
	public <T> T constructEntity(Class<?> type, String... params)
			throws InstantiationException, IllegalAccessException {
		@SuppressWarnings("unchecked")
		T obj = (T) type.newInstance();
		Field[] fields = type.getDeclaredFields();
		if (fields.length == params.length) {
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].isAnnotationPresent(Id.class)) {
					fields[i].setAccessible(true);
					fields[i].set(obj, Integer.valueOf(params[i]));
				} else if (fields[i].isAnnotationPresent(Column.class)) {
					fields[i].setAccessible(true);
					fields[i].set(obj, params[i]);
				} else if (fields[i].isAnnotationPresent(JoinColumn.class)) {
					fields[i].setAccessible(true);
					fields[i].set(obj, findById(Integer.valueOf(params[i]), fields[i].getType()));
				}
			}
		}
		return obj;
	}

	/**
	 * Ta metoda zwraca nazwê tabeli w bazie danych, jaka odpowiada encji
	 * przekazanej jako argument.
	 * 
	 * @param resultType - encja, której nazwa tabeli zostanie zwrócona
	 * @return nazwa tabeli
	 */
	private String getTableName(Class<?> resultType) {
		if (resultType.isAnnotationPresent(Table.class))
			return resultType.getAnnotation(Table.class).name();
		return "";
	}
	
	/**
	 * Ta metoda zwraca wartoœæ pola id danej encji.
	 * @param entity - sprawdzana encja
	 * @return wartoœæ kolumny id tego rekordu.
	 */
	private int getRecordId(Object entity) {
		int result=0;
		try {
			Field idField=entity.getClass().getDeclaredField("id");
			idField.setAccessible(true);
			result=idField.getInt(entity);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;
	}
}
