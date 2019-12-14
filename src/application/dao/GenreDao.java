package application.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import application.entities.Genre;

@Transactional
@Repository
public class GenreDao implements Dao<Genre> {
	private SessionFactory sessionFactory;

	@Autowired
	public GenreDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public int save(Genre entity) {
		sessionFactory.getCurrentSession().save(entity);
		return entity.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Genre> findAll() {
		return sessionFactory.getCurrentSession().getNamedNativeQuery("findAll").list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<Genre> findById(int id) {
		return sessionFactory.getCurrentSession().getNamedNativeQuery("findById").setParameter("id", id)
				.uniqueResultOptional();
	}

	@Override
	public void update(Genre entity) {
		sessionFactory.getCurrentSession().update(entity);
	}

	@Override
	public void delete(Genre entity) {
		sessionFactory.getCurrentSession().delete(entity);
	}
}
