package com.xabaohui.modules.minipay.dao.impl;

import java.util.List;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.xabaohui.modules.minipay.dao.ClearingDao;
import com.xabaohui.modules.minipay.entity.Clearing;

/**
 * A data access object (DAO) providing persistence and search support for
 * Clearing entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.xabaohui.modules.minipay.entity.Clearing
 * @author MyEclipse Persistence Tools
 */
public class ClearingDaoImpl extends HibernateDaoSupport implements ClearingDao {
	private static final Logger log = LoggerFactory
			.getLogger(ClearingDaoImpl.class);

	protected void initDao() {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xabaohui.modules.minipay.dao.ClearingDaoI#save(com.xabaohui.modules
	 * .minipay.entity.Clearing)
	 */
	@Override
	public void save(Clearing transientInstance) {
		log.debug("saving Clearing instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Clearing persistentInstance) {
		log.debug("deleting Clearing instance");
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
	 * com.xabaohui.modules.minipay.dao.ClearingDaoI#findById(java.lang.Integer)
	 */
	@Override
	public Clearing findById(java.lang.Integer id) {
		log.debug("getting Clearing instance with id: " + id);
		try {
			Clearing instance = (Clearing) getHibernateTemplate().get(
					"com.xabaohui.modules.minipay.entity.Clearing", id);
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
	 * com.xabaohui.modules.minipay.dao.ClearingDaoI#findByExample(com.xabaohui
	 * .modules.minipay.entity.Clearing)
	 */
	@Override
	public List findByExample(Clearing instance) {
		log.debug("finding Clearing instance by example");
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
		log.debug("finding Clearing instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Clearing as model where model."
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
	 * com.xabaohui.modules.minipay.dao.ClearingDaoI#findByOrderId(java.lang
	 * .Object)
	 */
	@Override
	public List findByOrderId(Object orderId) {
		return findByProperty(ORDER_ID, orderId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xabaohui.modules.minipay.dao.ClearingDaoI#findByOutTradeId(java.lang
	 * .Object)
	 */
	@Override
	public List findByOutTradeId(Object outTradeId) {
		return findByProperty(OUT_TRADE_ID, outTradeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xabaohui.modules.minipay.dao.ClearingDaoI#findByOutBuyerAccount(java
	 * .lang.Object)
	 */
	@Override
	public List findByOutBuyerAccount(Object outBuyerAccount) {
		return findByProperty(OUT_BUYER_ACCOUNT, outBuyerAccount);
	}

	public List findByChannel(Object channel) {
		return findByProperty(CHANNEL, channel);
	}

	public List findByTradeMoney(Object tradeMoney) {
		return findByProperty(TRADE_MONEY, tradeMoney);
	}

	public List findByOrigMsg(Object origMsg) {
		return findByProperty(ORIG_MSG, origMsg);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List findAll() {
		log.debug("finding all Clearing instances");
		try {
			String queryString = "from Clearing";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Clearing merge(Clearing detachedInstance) {
		log.debug("merging Clearing instance");
		try {
			Clearing result = (Clearing) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Clearing instance) {
		log.debug("attaching dirty Clearing instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Clearing instance) {
		log.debug("attaching clean Clearing instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ClearingDaoImpl getFromApplicationContext(
			ApplicationContext ctx) {
		return (ClearingDaoImpl) ctx.getBean("ClearingDAO");
	}
}