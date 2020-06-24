package application.dao;

import java.util.function.Consumer;
import java.util.function.Function;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class TransactionWrapper {	
	@Autowired
	private SessionFactory sessionFactory;
	
	public <T> T getTransactionResult(Function<Session, T> transactionBody, Class<T> resultType) {
		T result = null;
		Transaction tx = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			result = transactionBody.apply(session);
			tx.commit();
		} catch (Exception e) {
			rollBackTransactionIfPossible(tx);
			throw e;
		} finally {
			session.close();
		}
		return result;
	}
	
	public void performTransaction(Consumer<Session> transactionBody) {
		Transaction tx = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			transactionBody.accept(session);
			tx.commit();
		} catch (Exception e) {
			rollBackTransactionIfPossible(tx);
			throw e;
		} finally {
			session.close();
		}
	}
	
	private void rollBackTransactionIfPossible(Transaction tx) {
		if (tx != null && tx.isActive())
			tx.rollback();
	}
}	
