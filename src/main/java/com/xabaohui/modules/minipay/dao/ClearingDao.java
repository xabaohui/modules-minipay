/**
 * 
 */
package com.xabaohui.modules.minipay.dao;

import java.util.List;

import com.xabaohui.modules.minipay.entity.Clearing;

/**
 * @author YRee
 *
 */
public interface ClearingDao {

	// property constants
	public static final String ORDER_ID = "orderId";
	public static final String OUT_TRADE_ID = "outTradeId";
	public static final String OUT_BUYER_ACCOUNT = "outBuyerAccount";
	public static final String CHANNEL = "channel";
	public static final String TRADE_MONEY = "tradeMoney";
	public static final String ORIG_MSG = "origMsg";
	public static final String STATUS = "status";
	public static final String VERSION = "version";

	public abstract void save(Clearing transientInstance);

	public abstract Clearing findById(java.lang.Integer id);

	public abstract List findByExample(Clearing instance);

	public abstract List findByOrderId(Object orderId);

	public abstract List findByOutTradeId(Object outTradeId);

	public abstract List findByOutBuyerAccount(Object outBuyerAccount);

}