package com.xabaohui.modules.minipay.dao.impl;

import java.util.List;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.xabaohui.modules.minipay.dao.OrderDetailDao;
import com.xabaohui.modules.minipay.entity.OrderDetail;

/**
 * A data access object (DAO) providing persistence and search support for
 * OrderDetail entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.xabaohui.modules.minipay.entity.OrderDetail
 * @author MyEclipse Persistence Tools
 */
public class OrderDetailDaoImpl extends HibernateDaoSupport implements
		OrderDetailDao {
	private static final Logger log = LoggerFactory
			.getLogger(OrderDetailDaoImpl.class);

	protected void initDao() {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xabaohui.modules.minipay.dao.impl.OrderDetailDao#save(com.xabaohui
	 * .modules.minipay.entity.OrderDetail)
	 */
	@Override
	public void save(OrderDetail transientInstance) {
		log.debug("saving OrderDetail instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(OrderDetail persistentInstance) {
		log.debug("deleting OrderDetail instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xabaohui.modules.minipay.dao.impl.OrderDetailDao#findById(java.lang
	 * .Integer)
	 */
	@Override
	public OrderDetail findById(java.lang.Integer id) {
		log.debug("getting OrderDetail instance with id: " + id);
		try {
			OrderDetail instance = (OrderDetail) getHibernateTemplate().get(
					"com.xabaohui.modules.minipay.entity.OrderDetail", id);
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
	 * com.xabaohui.modules.minipay.dao.impl.OrderDetailDao#findByExample(com
	 * .xabaohui.modules.minipay.entity.OrderDetail)
	 */
	@Override
	public List findByExample(OrderDetail instance) {
		log.debug("finding OrderDetail instance by example");
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
		log.debug("finding OrderDetail instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from OrderDetail as model where model."
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
	 * com.xabaohui.modules.minipay.dao.impl.OrderDetailDao#findByOrderId(java
	 * .lang.Object)
	 */
	@Override
	public List findByOrderId(Object orderId) {
		return findByProperty(ORDER_ID, orderId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xabaohui.modules.minipay.dao.impl.OrderDetailDao#findBySkuId(java
	 * .lang.Object)
	 */
	@Override
	public List findBySkuId(Object skuId) {
		return findByProperty(SKU_ID, skuId);
	}

	public List findBySkuAmmount(Object skuAmmount) {
		return findByProperty(SKU_AMMOUNT, skuAmmount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xabaohui.modules.minipay.dao.impl.OrderDetailDao#findBySkuName(java
	 * .lang.Object)
	 */
	@Override
	public List findBySkuName(Object skuName) {
		return findByProperty(PRODUCTION_NAME, skuName);
	}

	public List findBySkuPrice(Object skuPrice) {
		return findByProperty(SKU_PRICE, skuPrice);
	}

	public List findBySkuDesc(Object skuDesc) {
		return findByProperty(SKU_DESC, skuDesc);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List findAll() {
		log.debug("finding all OrderDetail instances");
		try {
			String queryString = "from OrderDetail";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public OrderDetail merge(OrderDetail detachedInstance) {
		log.debug("merging OrderDetail instance");
		try {
			OrderDetail result = (OrderDetail) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(OrderDetail instance) {
		log.debug("attaching dirty OrderDetail instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OrderDetail instance) {
		log.debug("attaching clean OrderDetail instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static OrderDetailDao getFromApplicationContext(
			ApplicationContext ctx) {
		return (OrderDetailDao) ctx.getBean("OrderDetailDAO");
	}

	@Override
	public void update(OrderDetail orderDetail) {
		getHibernateTemplate().update(orderDetail);
	}

	@Override
	public List findByProductionId(Integer productionId) {
		return findByProperty(PRODUCTION_ID, productionId);
	}
}