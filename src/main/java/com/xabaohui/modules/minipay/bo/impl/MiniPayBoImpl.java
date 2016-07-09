/**
 * 
 */
package com.xabaohui.modules.minipay.bo.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipaySubmit;
import com.xabaohui.modules.minipay.bo.MiniPayBo;
import com.xabaohui.modules.minipay.dao.ClearingDao;
import com.xabaohui.modules.minipay.dao.OrderDao;
import com.xabaohui.modules.minipay.dao.OrderDetailDao;
import com.xabaohui.modules.minipay.dto.AddItemDTO;
import com.xabaohui.modules.minipay.dto.CreateClearingDTO;
import com.xabaohui.modules.minipay.dto.CreateOrderDTO;
import com.xabaohui.modules.minipay.entity.Clearing;
import com.xabaohui.modules.minipay.entity.Order;
import com.xabaohui.modules.minipay.entity.OrderDetail;
import com.xabaohui.modules.minipay.status.OrderDetailStatus;
import com.xabaohui.modules.minipay.status.OrderStatus;

/**
 * @author YRee
 * 
 */
@Service(value = "miniPayBo")
public class MiniPayBoImpl implements MiniPayBo {
	protected static Logger logger = LoggerFactory
			.getLogger(MiniPayBoImpl.class);
	@Resource
	private OrderDao orderDao;
	@Resource
	private OrderDetailDao orderDetailDao;
	@Resource
	private ClearingDao clearingDao;

	@Override
	public Order createOrder(CreateOrderDTO coDTO) {
		// coDTO对象验证
		this.validateCoDTO(coDTO);
		Order order = new Order();
		// 复制属性
		BeanUtils.copyProperties(coDTO, order);

		order.setTotalMoney(0.0);
		// 设置订单状态位初始化
		order.setStatus(OrderStatus.INIT);

		Date date = new Date();
		order.setGmtCreate(date);
		order.setGmtModify(date);
		order.setVersion(1);
		orderDao.save(order);
		return order;
	}

	/**
	 * 验证创建订单对象|| 后期可以增加验证，此时用不上
	 * 
	 * @param coDTO
	 */
	private void validateCoDTO(CreateOrderDTO coDTO) {
		if (coDTO.getBuyerId() == null) {
			throw new RuntimeException("买家id不能为空");
		}
	}

	@Override
	public void addItem(AddItemDTO aiDTO) {
		Order order = this.validateAiDTO(aiDTO);
		// 设置订单状态为等待
		order.setStatus(OrderStatus.WAITING);
		// 更新订单
		this.updateOrder(order);
		// 查找一番，查看skuId是否已经在该订单中存在
		OrderDetail detail = this.findDetailByOrderIdAndSkuId(
				aiDTO.getOrderId(), aiDTO.getSkuId());
		// 该商品在order中并不存在
		if (detail == null) {
			detail = new OrderDetail();
			BeanUtils.copyProperties(aiDTO, detail);
			// 设置detail状态为正常
			detail.setStatus(OrderDetailStatus.NORMAL);

			Date date = new Date();
			detail.setGmtCreate(date);
			detail.setGmtModify(date);
			detail.setVersion(1);
			orderDetailDao.save(detail);
		} else { // 有且只有一条商品记录
			// 更新商品数量和单价
			// 商品数量增加
			detail.setSkuAmmount(detail.getSkuAmmount() + aiDTO.getSkuAmmount());
			// 后来的商品价格覆盖了新的商品
			detail.setSkuPrice(aiDTO.getSkuPrice());
			this.updateOrderDetail(detail);
		}
		// 重新计算总价
		this.updateOrderMoneyByCalculate(order.getOrderId());
		this.createSubjectForOrder(order.getOrderId());
	}

	/**
	 * 为订单order生成subject
	 * 
	 * @param orderId
	 */
	private void createSubjectForOrder(Integer orderId) {
		if (orderId == null) {
			throw new RuntimeException("生成subject失败：传来的订单id为空");
		}
		Order order = this.findOrderByOrderId(orderId);
		if (order == null) {
			throw new RuntimeException("生成subject失败：传来的orderId没有找到order");
		}
		List<OrderDetail> details = this.findDetailsByOrderId(orderId);
		if (details == null || details.isEmpty()) {
			throw new RuntimeException("生成subject失败：通过orderId没有找到detail");
		}
		String subject = "";
		if (details.size() == 1) {
			// 获得第一条的商品名称
			subject = details.get(0).getProductionName();
		} else {
			subject = details.get(0).getProductionName() + "等";
		}
		order.setSubject(subject);
		this.updateOrder(order);
	}

	/**
	 * 验证aiDTO||这只是初期验证，后期可以增进其他验证
	 * 
	 * @param aiDTO
	 */
	private Order validateAiDTO(AddItemDTO aiDTO) {
		if (aiDTO.getOrderId() == null || aiDTO.getSkuId() == null
				|| aiDTO.getSkuPrice() == null || aiDTO.getSkuPrice() <= 0.0
				|| aiDTO.getSkuAmmount() == null || aiDTO.getSkuAmmount() <= 0) {
			throw new RuntimeException("请检查aiDTO中的数据是否正确");
		}
		Order order = orderDao.findById(aiDTO.getOrderId());
		if (order == null) {
			throw new RuntimeException("传入的orderId并没有找到订单");
		}
		return order;
	}

	/**
	 * 更新订单
	 * 
	 * @param order
	 */
	private void updateOrder(Order order) {
		order.setGmtModify(new Date());
		order.setVersion(order.getVersion() + 1);
		orderDao.update(order);
	}

	/**
	 * 更新明细
	 * 
	 * @param orderDetail
	 */
	private void updateOrderDetail(OrderDetail orderDetail) {
		orderDetail.setGmtModify(new Date());
		orderDetail.setVersion(orderDetail.getVersion() + 1);
		orderDetailDao.update(orderDetail);
	}

	/**
	 * 通过orderId
	 * 
	 * @param orderId
	 * @return
	 */
	public Order findOrderByOrderId(Integer orderId) {
		if (orderId == null) {
			throw new RuntimeException("查找orderId失败：orderId是空");
		}
		return orderDao.findById(orderId);
	}

	/**
	 * 通过orderId和skuId来查找没有被删除的明细
	 * 
	 * @param orderId
	 * @param skuId
	 * @return
	 */
	public OrderDetail findDetailByOrderIdAndSkuId(Integer orderId,
			Integer skuId) {
		if (orderId == null || skuId == null) {
			throw new RuntimeException("查找订单明细失败：orderId为空或者skuId为空");
		}
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setOrderId(orderId);
		orderDetail.setSkuId(skuId);
		orderDetail.setStatus(OrderDetailStatus.NORMAL);

		List<OrderDetail> details = orderDetailDao.findByExample(orderDetail);
		if (details == null || details.isEmpty()) {
			return null;
		} else if (details.size() > 1) {
			logger.info("在订单{}中查出的orderDetail为{}条", orderId, details.size());
			throw new RuntimeException("在订单中查出的orderDetail大于1条");
		}
		return details.get(0);
	}

	@Override
	public void updateCount(Integer orderId, Integer skuId, Integer skuAmmount) {
		if (orderId == null || skuId == null || skuAmmount == null
				|| skuAmmount <= 0) {
			throw new RuntimeException("修改商品数量时传来的数据有误，请检查");
		}
		// 先查出orderDetail
		OrderDetail orderDetail = this.findDetailByOrderIdAndSkuId(orderId,
				skuId);
		if (orderDetail == null) {
			throw new RuntimeException("没有在该订单中找到这个商品，请检查");
		}

		// 修改商品数量
		orderDetail.setSkuAmmount(skuAmmount);
		this.updateOrderDetail(orderDetail);
		// 跳整总价
		this.updateOrderMoneyByCalculate(orderId);
	}

	/**
	 * 在订单明细detail修改后计算并修改订单order总价
	 * 
	 * @param orderId
	 */
	private void updateOrderMoneyByCalculate(Integer orderId) {
		// 价格修改
		Order order = this.findOrderByOrderId(orderId);
		Double totalMoney = this.calculateTotalMoney(orderId);
		order.setTotalMoney(totalMoney);
		this.updateOrder(order);
	}

	@Override
	public void deleteItem(Integer skuId, Integer orderId) {
		OrderDetail detail = this.findDetailByOrderIdAndSkuId(orderId, skuId);
		if (detail == null) {
			throw new RuntimeException("没有通过orderId和skuI查询出detail");
		}
		detail.setStatus(OrderDetailStatus.DELETED);
		this.updateOrderDetail(detail);
		// 调整总价
		this.updateOrderMoneyByCalculate(orderId);
		this.createSubjectForOrder(orderId);
	}

	@Override
	public void cancelOrder(Integer orderId) {
		if (orderId == null) {
			throw new RuntimeException("取消订单失败：订单id为空");
		}
		Order order = orderDao.findById(orderId);

		if (order == null) {
			throw new RuntimeException("取消订单失败：没有通过orderId查找出订单");
		}
		// 不为waiting状态不能取消订单
		if (!this.isOrderCanBeCancel(order)) {
			logger.info("取消订单失败：订单{}的状态为{}", orderId, order.getStatus());
			throw new RuntimeException("取消订单失败：订单状态不为waiting");
		}
		// 取消订单操做
		order.setStatus(OrderStatus.CANCEL);
		this.updateOrder(order);
	}

	/**
	 * 是否能够取消
	 * 
	 * @param order
	 * @return
	 */
	public boolean isOrderCanBeCancel(Order order) {
		// 只有在waitting状态或者是init状态可以取消
		return OrderStatus.WAITING.equals(order.getStatus())
				|| OrderStatus.INIT.equals(order.getStatus());
	}

	@Override
	public boolean isOrderCanBeCancel(Integer orderId) {
		if (orderId == null) {
			throw new RuntimeException("检查订单是否能取消失败：orderId为空");
		}
		Order order = orderDao.findById(orderId);
		return this.isOrderCanBeCancel(order);
	}

	/**
	 * 是否能够支付
	 * 
	 * @param orderId
	 * @return
	 */
	public boolean isOrderCanBePay(Order order) {
		// 只有在waitting状态可以支付
		return OrderStatus.WAITING.equals(order.getStatus());
	}

	@Override
	public boolean isOrderCanBePay(Integer orderId) {
		if (orderId == null) {
			throw new RuntimeException("检查订单是否能支付失败：orderId为空");
		}
		Order order = orderDao.findById(orderId);
		return this.isOrderCanBePay(order);
	}

	@Override
	public List<Order> findOrderByBuyerId(Integer buyerId) {
		if (buyerId == null) {
			throw new RuntimeException("查找订单失败：买家id是空");
		}
		return orderDao.findByBuyerId(buyerId);
	}

	@Override
	public Double calculateTotalMoney(Integer orderId) {
		if (orderId == null) {
			throw new RuntimeException("计算订单总价失败：orderId是空");
		}
		// 拿到所有的明细
		List<OrderDetail> details = this.findDetailsByOrderId(orderId);
		if (details == null || details.isEmpty()) {
			throw new RuntimeException("计算订单总价失败：订单中不存在detail");
		}
		Double totalMoney = 0.0;
		for (OrderDetail detail : details) {
			totalMoney += detail.getSkuPrice() * detail.getSkuAmmount();
		}
		logger.info("order{}的detail中的总价是{}", orderId, totalMoney);
		return totalMoney;
	}

	public List<OrderDetail> findDetailsByOrderId(Integer orderId) {
		if (orderId == null) {
			throw new RuntimeException("查询orderDetail失败：orderId为空");
		}
		OrderDetail detail = new OrderDetail();
		detail.setOrderId(orderId);
		detail.setStatus(OrderDetailStatus.NORMAL);
		List<OrderDetail> details = orderDetailDao.findByExample(detail);
		return details;
	}

	@Override
	public Clearing createClearing(CreateClearingDTO ccDTO) {
		this.validateCCDTO(ccDTO);

		Clearing clearing = new Clearing();
		// 复制属性
		BeanUtils.copyProperties(ccDTO, clearing);

		Date now = new Date();
		clearing.setGmtCreate(now);
		clearing.setGmtModify(now);
		clearing.setVersion(1);
		clearingDao.save(clearing);

		return clearing;
	}

	/**
	 * 验证ccDTO
	 * 
	 * @param ccDTO
	 */
	private void validateCCDTO(CreateClearingDTO ccDTO) {
		if (ccDTO.getOrderId() == null || ccDTO.getTradeMoney() == null
				|| StringUtils.isBlank(ccDTO.getStatus())) {
			throw new RuntimeException("生成流水失败：请检查此次DTO对象");
		}
	}

	@Override
	public void backfillOrder(Integer orderId, String status, String channel) {
		if (orderId == null || StringUtils.isBlank(channel)
				|| StringUtils.isBlank(status)) {
			throw new RuntimeException("回填渠道失败：请检查参数是否有效");
		}
		Order order = this.findOrderByOrderId(orderId);
		if (order == null) {
			throw new RuntimeException("回填渠道失败：orderId没有查找到order");
		}
		order.setChannel(channel);
		order.setStatus(status);
		this.updateOrder(order);
	}

	public void alipayapi(String out_trade_no, String subject,
			String total_fee, String body, HttpServletRequest request) {
		// String out_trade_no = (String)
		// request.getAttribute("WIDout_trade_no");
		//
		// // 订单名称，必填
		// String subject = (String) request.getAttribute("WIDsubject");
		//
		// // 付款金额，必填
		// String total_fee = (String) request.getAttribute("WIDtotal_fee");
		//
		// // 商品描述，可空
		// String body = (String) request.getAttribute("WIDbody");

		// ////////////////////////////////////////////////////////////////////////////////

		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", AlipayConfig.service);
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("seller_id", AlipayConfig.seller_id);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", AlipayConfig.payment_type);
		sParaTemp.put("notify_url", AlipayConfig.notify_url);
		sParaTemp.put("return_url", AlipayConfig.return_url);
		sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("body", body);
		// 其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.O9yorI&treeId=62&articleId=103740&docType=1
		// 如sParaTemp.put("参数名","参数值");

		System.out.println("!!!!!!!!!" + out_trade_no);
		System.out.println("!!!!!!!!!" + subject);
		System.out.println("!!!!!!!!!" + total_fee);
		System.out.println("!!!!!!!!!" + body);
		// 建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
	};

}
