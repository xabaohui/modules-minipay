/**
 * 
 */
package com.xabaohui.modules.minipay.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xabaohui.modules.minipay.dto.AddItemDTO;
import com.xabaohui.modules.minipay.dto.CreateClearingDTO;
import com.xabaohui.modules.minipay.dto.CreateOrderDTO;
import com.xabaohui.modules.minipay.entity.Order;
import com.xabaohui.modules.minipay.entity.OrderDetail;
import com.xabaohui.modules.minipay.service.PayService;
import com.xabaohui.modules.minipay.status.ClearingStatus;

/**
 * @author YRee
 * 
 */
@Controller
@RequestMapping(value = "miniPay")
public class PayAction {
	protected static Logger logger = LoggerFactory.getLogger(PayAction.class);
	@Resource
	private PayService payService;

	@RequestMapping(value = "createOrder")
	public ModelAndView createOrder(Integer buyerId, Integer productionId,
			Integer skuId, Double skuPrice, Integer skuAmmount,
			String productionName) {
		// 先创建订单，目前关键字段只有buyerId
		CreateOrderDTO coDTO = new CreateOrderDTO();
		coDTO.setBuyerId(buyerId);
		Order order = payService.createOrder(coDTO);

		// 添加商品、
		AddItemDTO aiDTO = new AddItemDTO();
		aiDTO.setOrderId(order.getOrderId());
		aiDTO.setProductionId(productionId);
		aiDTO.setSkuPrice(skuPrice);
		aiDTO.setSkuAmmount(skuAmmount);
		aiDTO.setSkuId(skuId);
		aiDTO.setProductionName(productionName);
		payService.addItem(aiDTO);

		// 拿到order下的所有的detail
		List<OrderDetail> details = payService.findDetailsByOrderId(order
				.getOrderId());

		// 刷新得到order中的总价
		Order order2 = payService.findOrderByOrderId(order.getOrderId());

		ModelAndView mv = new ModelAndView();
		mv.addObject("order", order2);
		mv.addObject("details", details);
		mv.setViewName("list");
		return mv;
	}

	@RequestMapping(value = "addItem")
	public ModelAndView addItem(Integer orderId, Integer productionId,
			Integer skuId, Double skuPrice, Integer skuAmmount,
			String productionName) {
		// 添加商品
		AddItemDTO aiDTO = new AddItemDTO();
		aiDTO.setOrderId(orderId);
		aiDTO.setProductionId(productionId);
		aiDTO.setSkuPrice(skuPrice);
		aiDTO.setSkuAmmount(skuAmmount);
		aiDTO.setSkuId(skuId);
		aiDTO.setProductionName(productionName);
		payService.addItem(aiDTO);

		Order order = payService.findOrderByOrderId(orderId);
		List<OrderDetail> details = payService.findDetailsByOrderId(orderId);

		ModelAndView mv = new ModelAndView();
		mv.addObject("order", order);
		mv.addObject("details", details);
		mv.setViewName("list");
		return mv;
	}

	// /**
	// * 跳转支付宝前检查操作
	// *
	// * @return
	// */
	// @RequestMapping(value = "payCheck")
	// public String payCheck(Integer orderId, Double totalMoney,
	// HttpServletRequest request) {
	// Order order = payService.findOrderByOrderId(orderId);
	// // 检查是否能够支付，盘点状态是否为waitting
	// if (!payService.isOrderCanBePay(order)) {
	// logger.info("订单的状态错误是：{}", order.getStatus());
	// return "checkError";
	// }
	// // 检查上个页面价格和数据库价格
	// if (Math.abs(totalMoney - order.getTotalMoney()) >= 0.001) {
	// logger.info("上个页面传来的总价是：{}，计算出的总价是：{}", totalMoney,
	// order.getTotalMoney());
	// return "checkError";
	// }
	// payService.alipayapi(orderId + "", order.getSubject(), totalMoney + "",
	// " ", request);
	//
	// // request.setAttribute("WIDout_trade_no", orderId + "");
	// // request.setAttribute("WIDsubject", order.getSubject());
	// // request.setAttribute("WIDtotal_fee", totalMoney + "");
	// // request.setAttribute("WIDbody", " ");
	// return "alipayapi";
	// }

	/**
	 * 跳转支付宝前检查操作
	 * 
	 * @return
	 */
	@RequestMapping(value = "payCheck")
	public String payCheck(Integer orderId, Double totalMoney,
			HttpServletRequest request) {
		Order order = payService.findOrderByOrderId(orderId);
		// 检查是否能够支付，盘点状态是否为waitting
		if (!payService.isOrderCanBePay(order)) {
			logger.info("订单的状态错误是：{}", order.getStatus());
			return "checkError";
		}
		// 检查上个页面价格和数据库价格
		if (Math.abs(totalMoney - order.getTotalMoney()) >= 0.001) {
			logger.info("上个页面传来的总价是：{}，计算出的总价是：{}", totalMoney,
					order.getTotalMoney());
			return "checkError";
		}
		request.setAttribute("WIDout_trade_no", orderId + "");
		request.setAttribute("WIDsubject", order.getSubject());
		request.setAttribute("WIDtotal_fee", totalMoney + "");
		request.setAttribute("WIDbody", " ");
		return "alipayapi";
	}

	@RequestMapping(value = "tradeSuccess")
	/**
	 * 支付成功跳转
	 * @param request
	 * @return
	 */
	public ModelAndView tradeSuccess(HttpServletRequest request) {
		String buyer_email = (String) request.getAttribute("buyer_email");
		String total_feeStr = (String) request.getAttribute("total_fee");
		Double total_fee = Double.parseDouble(total_feeStr);
		String origMsg = (String) request.getAttribute("origMsg");
		// 内部交易号
		String out_trade_no = (String) request.getAttribute("out_trade_no");
		Integer orderId = Integer.parseInt(out_trade_no);
		// 支付宝交易流水号
		String trade_no = (String) request.getAttribute("trade_no");
		String trade_status = (String) request.getAttribute("trade_status");
		String channel = (String) request.getAttribute("chanel");

		CreateClearingDTO ccDTO = new CreateClearingDTO();
		ccDTO.setChannel(channel);
		ccDTO.setOrderId(orderId);
		ccDTO.setOrigMsg(origMsg);
		// TODO checker
		ccDTO.setOutBuyerAccount(buyer_email);
		ccDTO.setOutTradeId(trade_no);
		ccDTO.setStatus(ClearingStatus.SUCCESS);
		ccDTO.setTradeMoney(total_fee);
		// 设置交易成功（生成交易流水，修改订单状态）
		payService.tradeSuccess(ccDTO);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("finish");
		mv.addObject("status", ClearingStatus.SUCCESS);
		return mv;
	}

	@RequestMapping(value = "tradeFail")
	/**
	 * 支付失败跳转
	 * @param request
	 * @return
	 */
	public ModelAndView tradeFail(HttpServletRequest request) {
		String buyer_email = (String) request.getAttribute("buyer_email");
		String total_feeStr = (String) request.getAttribute("total_fee");
		Double total_fee = Double.parseDouble(total_feeStr);
		String origMsg = (String) request.getAttribute("origMsg");
		// 内部交易号
		String out_trade_no = (String) request.getAttribute("out_trade_no");
		Integer orderId = Integer.parseInt(out_trade_no);
		// 支付宝交易流水号
		String trade_no = (String) request.getAttribute("trade_no");
		String trade_status = (String) request.getAttribute("trade_status");
		String channel = (String) request.getAttribute("chanel");

		CreateClearingDTO ccDTO = new CreateClearingDTO();
		ccDTO.setChannel(channel);
		ccDTO.setOrderId(orderId);
		ccDTO.setOrigMsg(origMsg);
		// TODO checker
		ccDTO.setOutBuyerAccount(buyer_email);
		ccDTO.setOutTradeId(trade_no);
		ccDTO.setStatus(ClearingStatus.FAIL);
		ccDTO.setTradeMoney(total_fee);
		// 设置交易失败（生成交易流水，修改订单状态）
		payService.tradeFail(ccDTO);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("finish");
		mv.addObject("status", ClearingStatus.FAIL);
		return mv;
	}

	public ModelAndView processAlipayResult() {
		// 参数预处理 -> DTO
		// result = payService.processAlipayResult(DTO);
		// if(result == success) {
		// todo
		// } else {
		//
		// }

		return null;
	}

	@RequestMapping(value = "showDetails")
	public ModelAndView showDetails(Integer orderId) {
		List<OrderDetail> details = payService.findDetailsByOrderId(orderId);
		Order order = payService.findOrderByOrderId(orderId);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("list");
		mv.addObject("details", details);
		mv.addObject("order", order);
		return mv;
	}

}
