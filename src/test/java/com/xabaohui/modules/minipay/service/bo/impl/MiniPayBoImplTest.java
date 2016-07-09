/**
 * 
 */
package com.xabaohui.modules.minipay.service.bo.impl;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.xabaohui.modules.minipay.bo.MiniPayBo;
import com.xabaohui.modules.minipay.dao.OrderDao;
import com.xabaohui.modules.minipay.dao.OrderDetailDao;
import com.xabaohui.modules.minipay.dto.AddItemDTO;
import com.xabaohui.modules.minipay.dto.CreateClearingDTO;
import com.xabaohui.modules.minipay.dto.CreateOrderDTO;
import com.xabaohui.modules.minipay.entity.Clearing;
import com.xabaohui.modules.minipay.entity.Order;
import com.xabaohui.modules.minipay.entity.OrderDetail;
import com.xabaohui.modules.minipay.service.PayService;
import com.xabaohui.modules.minipay.status.ClearingStatus;
import com.xabaohui.modules.minipay.status.OrderStatus;

/**
 * @author YRee
 * 
 */
@TransactionConfiguration(transactionManager = "transactionManagerH", defaultRollback = false)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:mini-pay-servlet.xml")
public class MiniPayBoImplTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	@Resource
	private MiniPayBo miniPayBo;
	@Resource
	private OrderDetailDao orderDetailDao;
	@Resource
	private OrderDao orderDao;
	@Resource
	private PayService payService;

	// @Test
	// /**
	// * 下单测试buyId为空错误
	// */
	// public void createOrderTestErrorForBuyerIdIsNull() {
	// CreateOrderDTO coDTO = new CreateOrderDTO();
	// coDTO.setBuyerId(this.getRandomId());
	// Order order = miniPayBo.createOrder(coDTO);
	// Assert.assertNotNull(order);
	// }

	@Test
	/**
	 * 下单测试
	 */
	public void createOrderTest() {
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);
		Assert.assertNotNull(order);
	}

	@Test
	/**
	 * 新增一个项目测试(原来没有这个detail)
	 * 
	 */
	public void addItemTestForNotHadDetail() {
		// 新建一个order对象
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		AddItemDTO aiDTO = new AddItemDTO();
		aiDTO.setSkuId(1);
		aiDTO.setSkuPrice(20.0);
		aiDTO.setSkuAmmount(1);
		aiDTO.setOrderId(order.getOrderId());
		aiDTO.setProductionName("测试商品");
		miniPayBo.addItem(aiDTO);

		Order order2 = orderDao.findById(order.getOrderId());
		Assert.assertEquals(order2.getTotalMoney(), (Double) (1.0 * 20.0));
		Assert.assertEquals("测试商品", order2.getSubject());
	}

	@Test
	/**
	 * 新增一个项目测试(原来order中有这个detail)
	 * 
	 */
	public void addItemTestForHadDetail() {
		// 新建一个order对象
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		AddItemDTO aiDTO = new AddItemDTO();
		aiDTO.setSkuId(1);
		aiDTO.setSkuPrice(20.0);
		aiDTO.setSkuAmmount(1);
		aiDTO.setOrderId(order.getOrderId());
		aiDTO.setProductionName("测试商品");
		// 增加了一次
		miniPayBo.addItem(aiDTO);

		// 再加一次
		miniPayBo.addItem(aiDTO);

		Order order2 = orderDao.findById(order.getOrderId());
		Assert.assertEquals(order2.getTotalMoney(), (Double) (2.0 * 20.0));
	}

	@Test
	/**
	 * 新增一个项目测试，原来的detail中不存在，但是原来的detail中有其他的商品
	 * 
	 */
	public void addItemTestForManyDetail() {
		// 新建一个order对象
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		AddItemDTO aiDTO = new AddItemDTO();
		aiDTO.setSkuId(1);
		aiDTO.setSkuPrice(20.0);
		aiDTO.setSkuAmmount(1);
		aiDTO.setOrderId(order.getOrderId());
		aiDTO.setProductionName("测试商品");
		// 增加了一次
		miniPayBo.addItem(aiDTO);

		AddItemDTO aiDTO2 = new AddItemDTO();
		aiDTO2 = aiDTO;
		aiDTO2.setProductionName("测试商品2");
		aiDTO2.setSkuId(2);
		// 再加一次其他的商品
		miniPayBo.addItem(aiDTO2);

		Order order2 = orderDao.findById(order.getOrderId());
		Assert.assertEquals("测试商品" + "等", order2.getSubject());
	}

	/**
	 * 通過訂單id 去查詢order
	 */
	@Test
	public void findOrderByOrderIdTest() {
		// 新建一个order对象
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		AddItemDTO aiDTO = new AddItemDTO();
		aiDTO.setSkuId(1);
		aiDTO.setSkuPrice(20.0);
		aiDTO.setSkuAmmount(1);
		aiDTO.setOrderId(order.getOrderId());
		aiDTO.setProductionName("测试商品");
		// 增加了一次
		miniPayBo.addItem(aiDTO);

		Order order2 = miniPayBo.findOrderByOrderId(order.getOrderId());
		Assert.assertNotNull(order2);

	}

	@Test
	/**
	 * 通过orderId和skuId 查询detail  orderId不为空且 有该id
	 */
	public void findDetailByOrderIdAndSkuIdTest() {
		// 新建一个order对象
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		AddItemDTO aiDTO = new AddItemDTO();
		aiDTO.setSkuId(1);
		aiDTO.setSkuPrice(20.0);
		aiDTO.setSkuAmmount(1);
		aiDTO.setOrderId(order.getOrderId());
		aiDTO.setProductionName("测试商品");
		// 增加了一次
		miniPayBo.addItem(aiDTO);

		OrderDetail detail = miniPayBo.findDetailByOrderIdAndSkuId(
				order.getOrderId(), 1);
		Assert.assertNotNull(detail);

	}

	@Test
	/**
	 * 通过orderId和skuId 查询detail orderId查出的details为空
	 */
	public void findDetailByOrderIdAndSkuIdTestForNullOrderId() {
		// 新建一个order对象
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);
		OrderDetail detail = miniPayBo.findDetailByOrderIdAndSkuId(
				order.getOrderId(), 1);

		Assert.assertNull(detail);

	}

	@Test
	/**
	 * 更新商品数量测试
	 */
	public void updateCount() {
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		AddItemDTO aiDTO = new AddItemDTO();
		aiDTO.setSkuId(1);
		aiDTO.setSkuPrice(20.0);
		aiDTO.setSkuAmmount(1);
		aiDTO.setOrderId(order.getOrderId());
		// 添加商品
		miniPayBo.addItem(aiDTO);
		// 修改商品数量
		miniPayBo.updateCount(order.getOrderId(), 1, 5);
		OrderDetail detail = miniPayBo.findDetailByOrderIdAndSkuId(
				order.getOrderId(), 1);
		Assert.assertEquals((Integer) 5, detail.getSkuAmmount());
	}

	@Test
	/**
	 * 删除商品
	 */
	public void deleteItemTest() {
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		AddItemDTO aiDTO = new AddItemDTO();
		aiDTO.setSkuId(1);
		aiDTO.setSkuPrice(20.0);
		aiDTO.setSkuAmmount(1);
		aiDTO.setOrderId(order.getOrderId());
		// 添加商品
		miniPayBo.addItem(aiDTO);
		// 删除商品
		miniPayBo.deleteItem(1, order.getOrderId());
		OrderDetail detail = miniPayBo.findDetailByOrderIdAndSkuId(
				order.getOrderId(), 1);
		Assert.assertNull(detail);
	}

	@Test
	/**
	 * 取消订单测试
	 */
	public void cancelOrderTest() {
		// 新建订单
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		// 添加商品
		AddItemDTO aiDTO = new AddItemDTO();
		aiDTO.setSkuId(1);
		aiDTO.setSkuPrice(20.0);
		aiDTO.setSkuAmmount(1);
		aiDTO.setOrderId(order.getOrderId());
		miniPayBo.addItem(aiDTO);

		// 取消订单
		miniPayBo.cancelOrder(order.getOrderId());

		Order order2 = orderDao.findById(order.getOrderId());
		Assert.assertEquals(OrderStatus.CANCEL, order2.getStatus());
	}

	@Test
	/**
	 * 取消订单测试 状态不为waitting
	 */
	public void cancelOrderTestForStatusNotWaitting() {
		// 新建订单
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		// 添加商品
		AddItemDTO aiDTO = new AddItemDTO();
		aiDTO.setSkuId(1);
		aiDTO.setSkuPrice(20.0);
		aiDTO.setSkuAmmount(1);
		aiDTO.setOrderId(order.getOrderId());
		miniPayBo.addItem(aiDTO);
		// 设置支付成功
		CreateClearingDTO ccDTO = new CreateClearingDTO();
		ccDTO.setChannel("alipay");
		ccDTO.setOrderId(order.getOrderId());
		ccDTO.setOrigMsg("test");

		ccDTO.setOutBuyerAccount("testEmail");
		ccDTO.setOutTradeId("test");
		ccDTO.setStatus(ClearingStatus.SUCCESS);
		ccDTO.setTradeMoney(100.0);
		// 设置交易成功（生成交易流水，修改订单状态）
		payService.tradeSuccess(ccDTO);

		try {
			// 取消订单
			miniPayBo.cancelOrder(order.getOrderId());
		} catch (RuntimeException e) {
			if (!"取消订单失败：订单状态不为waiting".equals(e.getMessage())) {
				Assert.fail();
			}
		}

	}

	/**
	 * 判断订单是否能取消
	 */
	@Test
	public void isOrderCanBeCancelTest() {
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		// 添加商品
		AddItemDTO aiDTO = new AddItemDTO();
		aiDTO.setSkuId(1);
		aiDTO.setSkuPrice(20.0);
		aiDTO.setSkuAmmount(1);
		aiDTO.setOrderId(order.getOrderId());
		miniPayBo.addItem(aiDTO);
		// 此时未支付，可退款
		Assert.assertTrue(miniPayBo.isOrderCanBeCancel(order.getOrderId()));

		// 添加商品
		AddItemDTO aiDTO2 = new AddItemDTO();
		aiDTO2.setSkuId(1);
		aiDTO2.setSkuPrice(20.0);
		aiDTO2.setSkuAmmount(1);
		aiDTO2.setOrderId(order.getOrderId());
		miniPayBo.addItem(aiDTO);
		// 设置支付成功
		CreateClearingDTO ccDTO = new CreateClearingDTO();
		ccDTO.setChannel("alipay");
		ccDTO.setOrderId(order.getOrderId());
		ccDTO.setOrigMsg("test");

		ccDTO.setOutBuyerAccount("testEmail");
		ccDTO.setOutTradeId("test");
		ccDTO.setStatus(ClearingStatus.SUCCESS);
		ccDTO.setTradeMoney(100.0);
		// 设置交易成功（生成交易流水，修改订单状态）
		payService.tradeSuccess(ccDTO);
		// 此时已经支付，不能退款
		Assert.assertFalse(miniPayBo.isOrderCanBeCancel(order.getOrderId()));
	}

	/**
	 * 是否能够支付测试
	 */
	@Test
	public void isOrderCanBePayTest() {
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		// 设置支付成功
		CreateClearingDTO ccDTO = new CreateClearingDTO();
		ccDTO.setChannel("alipay");
		ccDTO.setOrderId(order.getOrderId());
		ccDTO.setOrigMsg("test");

		ccDTO.setOutBuyerAccount("testEmail");
		ccDTO.setOutTradeId("test");
		ccDTO.setStatus(ClearingStatus.SUCCESS);
		ccDTO.setTradeMoney(100.0);

		// 设置交易成功（生成交易流水，修改订单状态）
		payService.tradeSuccess(ccDTO);
		// 此时已经状态为paid，不能再支付
		Assert.assertFalse(miniPayBo.isOrderCanBePay(order.getOrderId()));
	}

	/**
	 * 通过buyerId查找订单测试
	 */
	@Test
	public void findOrderByBuyerIdTest() {
		// 新建订单
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(2);
		// 创建订单
		miniPayBo.createOrder(coDTO);
		Assert.assertNotEquals(0, miniPayBo.findOrderByBuyerId(1).size());
	}

	@Test
	/**
	 * 计算订单总价测试
	 */
	public void calculateTotalMoneyTest() {
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		AddItemDTO aiDTO = new AddItemDTO();
		aiDTO.setSkuId(1);
		aiDTO.setSkuPrice(20.0);
		aiDTO.setSkuAmmount(2);
		aiDTO.setOrderId(order.getOrderId());
		// 添加商品
		miniPayBo.addItem(aiDTO);

		AddItemDTO aiDTO2 = new AddItemDTO();
		aiDTO2.setSkuId(2);
		aiDTO2.setSkuPrice(30.0);
		aiDTO2.setSkuAmmount(1);
		aiDTO2.setOrderId(order.getOrderId());
		// 添加商品
		miniPayBo.addItem(aiDTO2);

		Assert.assertEquals((Double) (20.0 * 2 + 30.0 * 1),
				miniPayBo.calculateTotalMoney(order.getOrderId()));

	}

	/**
	 * 计算没有detail的订单总价测试
	 */
	@Test
	public void calculateTotalMoneyTestForNoDetail() {
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		try {
			miniPayBo.calculateTotalMoney(order.getOrderId());
		} catch (RuntimeException e) {
			if (!"计算订单总价失败：订单中不存在detail".equals(e.getMessage())) {
				Assert.fail();
			}
		}

	}

	/**
	 * 通过订单id查询details
	 */
	@Test
	public void findDetailsByOrderIdTest() {
		// 新建一个order对象
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		AddItemDTO aiDTO = new AddItemDTO();
		aiDTO.setSkuId(1);
		aiDTO.setSkuPrice(20.0);
		aiDTO.setSkuAmmount(1);
		aiDTO.setOrderId(order.getOrderId());
		aiDTO.setProductionName("测试商品");
		// 增加了一次
		miniPayBo.addItem(aiDTO);

		AddItemDTO aiDTO2 = new AddItemDTO();
		aiDTO2 = aiDTO;
		aiDTO2.setProductionName("测试商品2");
		aiDTO2.setSkuId(2);
		// 再加一次其他的商品
		miniPayBo.addItem(aiDTO2);

		Assert.assertEquals(2,
				miniPayBo.findDetailsByOrderId(order.getOrderId()).size());
	}

	@Test
	public void createClearingTest() {
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		AddItemDTO aiDTO = new AddItemDTO();
		aiDTO.setSkuId(1);
		aiDTO.setSkuPrice(20.0);
		aiDTO.setSkuAmmount(2);
		aiDTO.setOrderId(order.getOrderId());
		// 添加商品
		miniPayBo.addItem(aiDTO);

		AddItemDTO aiDTO2 = new AddItemDTO();
		aiDTO2.setSkuId(2);
		aiDTO2.setSkuPrice(30.0);
		aiDTO2.setSkuAmmount(1);
		aiDTO2.setOrderId(order.getOrderId());
		// 添加商品
		miniPayBo.addItem(aiDTO2);

		Order order2 = miniPayBo.findOrderByOrderId(order.getOrderId());
		CreateClearingDTO ccDTO = new CreateClearingDTO();
		ccDTO.setOrderId(order.getOrderId());
		ccDTO.setTradeMoney(order2.getTotalMoney());
		ccDTO.setStatus(ClearingStatus.SUCCESS);
		Clearing clearing = miniPayBo.createClearing(ccDTO);

		Assert.assertNotNull(clearing);
	}

	@Test
	/**
	 * 回填测试
	 */
	public void backfillOrderTest() {
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		AddItemDTO aiDTO = new AddItemDTO();
		aiDTO.setSkuId(1);
		aiDTO.setSkuPrice(20.0);
		aiDTO.setSkuAmmount(2);
		aiDTO.setOrderId(order.getOrderId());
		// 添加商品
		miniPayBo.addItem(aiDTO);

		miniPayBo.backfillOrder(order.getOrderId(), ClearingStatus.SUCCESS,
				"alipay");

		Order order2 = miniPayBo.findOrderByOrderId(order.getOrderId());

		Assert.assertEquals("alipay", order.getChannel());
	}

	@Test
	/**
	 * 回填测试状态不为waiting
	 */
	public void backfillOrderTestForNotWaiting() {
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(1);
		Order order = miniPayBo.createOrder(coDTO);

		try {
			miniPayBo.backfillOrder(order.getOrderId(), ClearingStatus.SUCCESS,
					"alipay");
		} catch (Exception e) {
			if (!"回填失败：订单状态不为waiting".equals(e.getMessage())) {
				Assert.fail();
			}
		}

	}
}
