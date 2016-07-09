/**
 * 
 */
package com.xabaohui.modules.minipay.dao;

import java.util.List;

import com.xabaohui.modules.minipay.entity.OrderDetail;

/**
 * @author YRee
 * 
 */
public interface OrderDetailDao {

	// property constants
	public static final String ORDER_ID = "orderId";
	public static final String PRODUCTION_ID = "productionId";
	public static final String SKU_ID = "skuId";
	public static final String SKU_AMMOUNT = "skuAmmount";
	public static final String PRODUCTION_NAME = "productionName";
	public static final String SKU_PRICE = "skuPrice";
	public static final String SKU_DESC = "skuDesc";
	public static final String STATUS = "status";
	public static final String VERSION = "version";

	public abstract void save(OrderDetail transientInstance);

	public abstract List findByProductionId(java.lang.Integer productionId);

	public abstract OrderDetail findById(java.lang.Integer id);

	public abstract List findByExample(OrderDetail instance);

	public abstract List findByOrderId(Object orderId);

	public abstract List findBySkuId(Object skuId);

	public abstract List findBySkuName(Object skuName);

	public abstract void update(OrderDetail orderDetail);
}