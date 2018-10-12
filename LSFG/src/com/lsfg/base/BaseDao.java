package com.lsfg.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Scope("prototype")
public class BaseDao<T> {

	private Class<T> currClass;
	

	@Autowired
    @Qualifier("sessionFactory")
	private SessionFactory sessionFactory;


	public final void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	

	@Transactional
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}


	@SuppressWarnings("unchecked")
	public BaseDao(){
		Type type = this.getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] pTypes = ((ParameterizedType) type).getActualTypeArguments();
			this.currClass = (Class<T>) pTypes[0];
		} else {
			this.currClass = (Class<T>) Object.class;
		}
	}
	

	protected void initDao() {
		// do nothing
	}

	public final void save(T transientInstance) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			session.save(transientInstance);
			session.flush();
			tx.commit();
		} catch (Exception re) {
			if (tx != null) {
				tx.rollback();
			}
			re.printStackTrace();
		} finally {
			session.close();
		}
	}


	public final void update(T persistentInstance) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			session.update(persistentInstance);
			session.flush();
			tx.commit();
		} catch (Exception re) {
			re.printStackTrace();
		} finally {
			session.close();
		}
	}
	

	public final void update(List<T> list) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++ ) {
					session.update(list.get(i));
				}
				session.flush();
			}
			tx.commit();
		} catch (Exception re) {
			re.printStackTrace();
		} finally {
			session.close();
		}
	}
	

	public final void delete(T persistentInstance) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			session.delete(persistentInstance);
			session.flush();
			tx.commit();
		} catch (Exception re) {
			re.printStackTrace();
		} finally {
			session.close();
		}
	}

	public final void addFileToDateBase(List<T> list) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					session.save(list.get(i));
				}
				session.flush();
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	

	@SuppressWarnings("unchecked")
    public final T findById(Serializable id) {
		Session session = sessionFactory.openSession();
		try {
			T instance = (T) session.get(this.currClass, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		} finally{
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
    public final List<T> findByProperty(String propertyName, Object value) {
		Session session = sessionFactory.openSession();
		try {
			String queryString = " from "+ this.currClass.getSimpleName() +" as model where model." + propertyName + "= ?";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		} finally{
			session.close();
		}
	}
	

	@SuppressWarnings("unchecked")
    public final List<T> findByHql(String hql){
		Session session = sessionFactory.openSession();
		try {
			Query queryObject = session.createQuery(hql);
			if (queryObject != null) {
				return queryObject.list();
			} else {
				return null;
			}
		} catch (RuntimeException re) {
			throw re;
		}
		finally{
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
    public final List<T> findBySql(String sql){
		Session session = sessionFactory.openSession();
		try {
			SQLQuery queryObject = session.createSQLQuery(sql);
			if (queryObject != null) {
				return queryObject.list();
			} else {
				return null;
			}
		} catch (RuntimeException re) {
			throw re;
		}
		finally{
			session.close();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List<?> findBySQL( final String sql, final Map<String,Object> params) {
    	List ls = null;
		Session session = sessionFactory.openSession();
		try {
			SQLQuery queryObject = session.createSQLQuery(sql);
			if (params != null && params.size() > 0) {
				Iterator<String> iter = params.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					queryObject.setParameter(key, params.get(key));
				}
			}
			ls = queryObject.list();
		} catch (Exception ex) {
			throw new RuntimeException("findBySQL 查询失败",ex);
		}finally{
			session.close();
		}
		return ls;
    }
	
    public final Integer findMaxIdByHql(String hql){
    	Session session = sessionFactory.openSession();
		try {
			Integer maxId = (Integer) session.createQuery(hql).uniqueResult();
			if (maxId != null) {
				return maxId;
			} else {
				return 0;
			}
		} catch (RuntimeException re) {
			throw re;
		}finally{
			session.close();
		}
	}
	

	public final int getAllRowsCount() {
		Session session = sessionFactory.openSession();
		try {
			String queryString = "from " + this.currClass.getSimpleName();
			Query queryObject = session.createQuery(queryString);
			return queryObject.list().size();
		} catch (RuntimeException e) {
			throw e;
		} finally{
			session.close();
		}
	}
	

	public final int getAllRowsCount(String hql) {
		Session session = sessionFactory.openSession();
		try {
			Query queryObject = session.createQuery(hql);
			return queryObject.list().size();
		} catch (RuntimeException e) {
			throw e;
		} finally{
			session.close();
		}
	}


	@SuppressWarnings("unchecked")
    public final T merge(T detachedInstance) {
		Session session = sessionFactory.openSession();
		try {
			T result = (T) session.merge(detachedInstance);
			return result;
		} catch (RuntimeException re) {
			throw re;
		} finally{
			session.close();
		}
	}
}
