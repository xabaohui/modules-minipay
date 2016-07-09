/**
 * 
 */
package com.xabaohui.modules.minipay.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.xabaohui.modules.minipay.bo.MiniPayBo;
import com.xabaohui.modules.minipay.dto.AddItemDTO;
import com.xabaohui.modules.minipay.dto.CreateClearingDTO;
import com.xabaohui.modules.minipay.dto.CreateOrderDTO;
import com.xabaohui.modules.minipay.entity.Order;
import com.xabaohui.modules.minipay.entity.OrderDetail;
import com.xabaohui.modules.minipay.service.PayService;
import com.xabaohui.modules.minipay.status.OrderStatus;

/**
 * @author YRee
 * 
 */
@Service(value = "payService")
public class PayServiceImpl implements PayService {

	@Resource
	private MiniPayBo miniPayBo;

	@Override
	public Order createOrder(CreateOrderDTO coDTO) {
		return miniPayBo.createOrder(coDTO);
	}

	@Override
	public void addItem(AddItemDTO aiDTO) {
		miniPayBo.addItem(aiDTO);
	}

	@Override
	public void updateCount(Integer orderId, Integer skuId, Integer skuAmmount) {
		miniPayBo.updateCount(orderId, skuId, skuAmmount);
	}

	@Override
	public void deleteItem(Integer orderId, Integer skuId) {
		miniPayBo.deleteItem(skuId, orderId);
	}

	@Override
	public void cancelOrder(Integer orderId) {
		miniPayBo.cancelOrder(orderId);
	}

	@Override
	public OrderDetail findDetailByOrderIdAndSkuId(Integer orderId,
			Integer skuId) {
		return miniPayBo.findDetailByOrderIdAndSkuId(orderId, skuId);
	}

	@Override
	public Order findOrderByOrderId(Integer orderId) {
		return miniPayBo.findOrderByOrderId(orderId);
	}

	@Override
	public List<Order> findOrderByBuyerId(Integer buyerId) {
		return miniPayBo.findOrderByBuyerId(buyerId);
	}

	@Override
	public Double calculateTotalMoney(Integer orderId) {
		return miniPayBo.calculateTotalMoney(orderId);
	}

	@Override
	public List<OrderDetail> findDetailsByOrderId(Integer orderId) {
		return miniPayBo.findDetailsByOrderId(orderId);
	}

	@Override
	public void tradeSuccess(CreateClearingDTO ccDTO) {
		// 生成业务流水
		miniPayBo.createClearing(ccDTO);
		// 回填订单
		miniPayBo.backfillOrder(ccDTO.getOrderId(), OrderStatus.PAID,
				ccDTO.getChannel());
	}

	@Override
	public void tradeFail(CreateClearingDTO ccDTO) {
		// 生成业务流水
		miniPayBo.createClearing(ccDTO);
		// 回填订单
		miniPayBo.backfillOrder(ccDTO.getOrderId(), OrderStatus.FAIL,
				ccDTO.getChannel());
	}

	@Override
	public boolean isOrderCanBePay(Order order) {
		return miniPayBo.isOrderCanBePay(order);
	}

	@Override
	public boolean isOrderCanBePay(Integer orderId) {
		return miniPayBo.isOrderCanBePay(orderId);
	}

	@Override
	public boolean isOrderCanBeCancel(Order order) {
		return miniPayBo.isOrderCanBeCancel(order);
	}

	@Override
	public boolean isOrderCanBeCancel(Integer orderId) {
		return miniPayBo.isOrderCanBeCancel(orderId);
	}

	public void alipayapi(String out_trade_no, String subject,
			String total_fee, String body, HttpServletRequest request) {
		miniPayBo.alipayapi(out_trade_no, subject, total_fee, body, request);
	}
}
