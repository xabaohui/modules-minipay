package com.xabaohui.modules.minipay.entity;

import java.util.Date;

/**
 * Clearing entity. @author MyEclipse Persistence Tools
 */

public class Clearing implements java.io.Serializable {

	// Fields

	private Integer clearingId;
	private Integer orderId;
	private String outTradeId;
	private String outBuyerAccount;
	private String channel;
	private Double tradeMoney;
	private String origMsg;
	private String status;
	private Date gmtCreate;
	private Date gmtModify;
	private Integer version;

	// Constructors

	/** default constructor */
	public Clearing() {
	}

	/** full constructor */
	public Clearing(Integer orderId, String outTradeId, String outBuyerAccount,
			String channel, Double tradeMoney, String origMsg, String status,
			Date gmtCreate, Date gmtModify, Integer version) {
		this.orderId = orderId;
		this.outTradeId = outTradeId;
		this.outBuyerAccount = outBuyerAccount;
		this.channel = channel;
		this.tradeMoney = tradeMoney;
		this.origMsg = origMsg;
		this.status = status;
		this.gmtCreate = gmtCreate;
		this.gmtModify = gmtModify;
		this.version = version;
	}

	// Property accessors

	public Integer getClearingId() {
		return this.clearingId;
	}

	public void setClearingId(Integer clearingId) {
		this.clearingId = clearingId;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOutTradeId() {
		return this.outTradeId;
	}

	public void setOutTradeId(String outTradeId) {
		this.outTradeId = outTradeId;
	}

	public String getOutBuyerAccount() {
		return this.outBuyerAccount;
	}

	public void setOutBuyerAccount(String outBuyerAccount) {
		this.outBuyerAccount = outBuyerAccount;
	}

	public String getChannel() {
		return this.channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Double getTradeMoney() {
		return this.tradeMoney;
	}

	public void setTradeMoney(Double tradeMoney) {
		this.tradeMoney = tradeMoney;
	}

	public String getOrigMsg() {
		return this.origMsg;
	}

	public void setOrigMsg(String origMsg) {
		this.origMsg = origMsg;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getGmtCreate() {
		return this.gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModify() {
		return this.gmtModify;
	}

	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}