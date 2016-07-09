package com.xabaohui.modules.minipay.entity;

import java.util.Date;

/**
 * Order entity. @author MyEclipse Persistence Tools
 */

public class Order implements java.io.Serializable {

	// Fields

	private Integer orderId;
	private Integer buyerId;
	private String buyerName;

	private Double totalMoney;
	private String subject;
	private String channel;
	private String status;
	private String receiverName;
	private String receiverPhone;
	private Integer receiverCityId;
	private String receiverDetailAddr;
	private String buyerMess;
	private Date gmtCreate;
	private Date gmtModify;
	private Integer version;

	// Constructors

	/** default constructor */
	public Order() {
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Integer buyerId) {
		this.buyerId = buyerId;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public Integer getReceiverCityId() {
		return receiverCityId;
	}

	public void setReceiverCityId(Integer receiverCityId) {
		this.receiverCityId = receiverCityId;
	}

	public String getReceiverDetailAddr() {
		return receiverDetailAddr;
	}

	public void setReceiverDetailAddr(String receiverDetailAddr) {
		this.receiverDetailAddr = receiverDetailAddr;
	}

	public String getBuyerMess() {
		return buyerMess;
	}

	public void setBuyerMess(String buyerMess) {
		this.buyerMess = buyerMess;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModify() {
		return gmtModify;
	}

	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Order(Integer orderId, Integer buyerId, String buyerName,
			Double totalMoney, String subject, String channel, String status,
			String receiverName, String receiverPhone, Integer receiverCityId,
			String receiverDetailAddr, String buyerMess) {
		super();
		this.orderId = orderId;
		this.buyerId = buyerId;
		this.buyerName = buyerName;
		this.totalMoney = totalMoney;
		this.subject = subject;
		this.channel = channel;
		this.status = status;
		this.receiverName = receiverName;
		this.receiverPhone = receiverPhone;
		this.receiverCityId = receiverCityId;
		this.receiverDetailAddr = receiverDetailAddr;
		this.buyerMess = buyerMess;
	}

}