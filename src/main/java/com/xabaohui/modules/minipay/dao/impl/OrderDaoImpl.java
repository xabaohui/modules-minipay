package com.xabaohui.modules.minipay.dao.impl;

import java.util.List;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.xabaohui.modules.minipay.dao.OrderDao;
import com.xabaohui.modules.minipay.entity.Order;

/**
 * A data access object (DAO) providing persistence and search support for Order
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see com.xabaohui.modules.minipay.entity.Order
 * @author MyEclipse Persistence Tools
 */
public class OrderDaoImpl extends HibernateDaoSupport implements OrderDao {
	private static final Logger log = LoggerFactory
			.getLogger(OrderDaoImpl.class);

	protected void initDao() {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xabaohui.modules.minipay.dao.impl.OrderDao#save(com.xabaohui.modules
	 * .minipay.entity.Order)
	 */
	@Override
	public void save(Order transientInstance) {
		log.debug("saving Order instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Order persistentInstance) {
		log.debug("deleting Order instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Order findById(java.lang.Integer id) {
		log.debug("getting Order instance with id: " + id);
		try {
			Order instance = (Order) getHibernateTemplate().get(
					"com.xabaohui.modules.minipay.entity.Order", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xabaohui.modules.minipay.dao.impl.OrderDao#findByExample(com.xabaohui
	 * .modules.minipay.entity.Order)
	 */
	@Override
	public List findByExample(Order instance) {
		log.debug("finding Order instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Order instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Order as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xabaohui.modules.minipay.dao.impl.OrderDao#findByBuyerId(java.lang
	 * .Object)
	 */
	@Override
	public List findByBuyerId(Object buyerId) {
		return findByProperty(BUYER_ID, buyerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xabaohui.modules.minipay.dao.impl.OrderDao#findByBuyerName(java.lang
	 * .Object)
	 */
	@Override
	public List findByBuyerName(Object buyerName) {
		return findByProperty(BUYER_NAME, buyerName);
	}

	public List findByTotalMoney(Object totalMoney) {
		return findByProperty(TOTAL_MONEY, totalMoney);
	}

	public List findBySubject(Object subject) {
		return findByProperty(SUBJECT, subject);
	}

	public List findByChannel(Object channel) {
		return findByProperty(CHANNEL, channel);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xabaohui.modules.minipay.dao.impl.OrderDao#findByRecevierName(java
	 * .lang.Object)
	 */
	@Override
	public List findByRecevierName(Object recevierName) {
		return findByProperty(RECEVIER_NAME, recevierName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xabaohui.modules.minipay.dao.impl.OrderDao#findByRecevierPhone(java
	 * .lang.Object)
	 */
	@Override
	public List findByRecevierPhone(Object recevierPhone) {
		return findByProperty(RECEVIER_PHONE, recevierPhone);
	}

	public List findByRecevierCityId(Object recevierCityId) {
		return findByProperty(RECEVIER_CITY_ID, recevierCityId);
	}

	public List findByReceiverDetailAddr(Object receiverDetailAddr) {
		return findByProperty(RECEIVER_DETAIL_ADDR, receiverDetailAddr);
	}

	public List findByBuyerMess(Object buyerMess) {
		return findByProperty(BUYER_MESS, buyerMess);
	}

	public List findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List findAll() {
		log.debug("finding all Order instances");
		try {
			String queryString = "from Order";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Order merge(Order detachedInstance) {
		log.debug("merging Order instance");
		try {
			Order result = (Order) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Order instance) {
		log.debug("attaching dirty Order instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Order instance) {
		log.debug("attaching clean Order instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static OrderDao getFromApplicationContext(ApplicationContext ctx) {
		return (OrderDao) ctx.getBean("OrderDAO");
	}

	public void update(Order order) {
		getHibernateTemplate().update(order);
	}

}