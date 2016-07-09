/**
 * 
 */
package com.xabaohui.modules.minipay.dao;

import java.util.List;

import com.xabaohui.modules.minipay.entity.Order;

/**
 * @author YRee
 * 
 */
public interface OrderDao {

	// property constants
	public static final String BUYER_ID = "buyerId";
	public static final String BUYER_NAME = "buyerName";
	public static final String TOTAL_MONEY = "totalMoney";
	public static final String SUBJECT = "subject";
	public static final String CHANNEL = "channel";
	public static final String STATUS = "status";
	public static final String RECEVIER_NAME = "recevierName";
	public static final String RECEVIER_PHONE = "recevierPhone";
	public static final String RECEVIER_CITY_ID = "recevierCityId";
	public static final String RECEIVER_DETAIL_ADDR = "receiverDetailAddr";
	public static final String BUYER_MESS = "buyerMess";
	public static final String VERSION = "version";

	public abstract Order findById(Integer id);

	public abstract void save(Order transientInstance);

	public abstract List findByExample(Order instance);

	public abstract List findByBuyerId(Object buyerId);

	public abstract List findByBuyerName(Object buyerName);

	public abstract List findByRecevierName(Object recevierName);

	public abstract List findByRecevierPhone(Object recevierPhone);

	public abstract void update(Order order);
}