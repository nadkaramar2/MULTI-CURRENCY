package ams.cms.dao.generic;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;

import ams.cms.dao.GenericDao;


@SuppressWarnings("unchecked")
public abstract class AbstractGenericDao<E> implements GenericDao<E> 
{
	@Autowired
	private SessionFactory sessionFactory;

	private final Class<E> entityClass;
	
	public AbstractGenericDao() {
		this.entityClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	protected Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public E findById(final Serializable id) {
		return (E) getSession().get(this.entityClass, id);
	}

	@Override
	public Serializable save(E entity) {
		return getSession().save(entity);
	}

	@Override
	public void saveOrUpdate(E entity) {
		getSession().saveOrUpdate(entity);
	}
	
	@Override
	public void update(E entity) 
	{
		Session session = getSession(); 
		Transaction tx = session.beginTransaction();		
		session.update(entity);
		tx.commit();
	}

	@Override
	public void delete(E entity) {
		getSession().delete(entity);
	}

	@Override
	public void deleteAll() {
		List<E> entities = findAll();
		for (E entity : entities) {
			getSession().delete(entity);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<E> findAll() {
		return getSession().createCriteria(this.entityClass).list();
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<E> findAllByExample(E entity) {
		Example example = Example.create(entity).ignoreCase().enableLike().excludeZeroes();
		return getSession().createCriteria(this.entityClass).add(example).list();
	}
	
	@SuppressWarnings("deprecation")
	protected Criteria createEntityCriteria() {
		return getSession().createCriteria(this.entityClass);
	}

	@Override
	public void clear() {
		getSession().clear();
	}

	@Override
	public void flush() {
		getSession().flush();
	}
	
	@Override
    public void saveAll(List<E> entities) {
        for (E entity : entities) {
            getSession().save(entity);
        }
    }
}
