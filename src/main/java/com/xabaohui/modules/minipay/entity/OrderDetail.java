package com.xabaohui.modules.minipay.entity;

import java.util.Date;

/**
 * OrderDetail entity. @author MyEclipse Persistence Tools
 */

public class OrderDetail implements java.io.Serializable {

	// Fields

	private Integer detailId;
	private Integer orderId;
	private Integer productionId;
	private Integer skuId;
	private Integer skuAmmount;
	private String productionName;
	private Double skuPrice;
	private String skuDesc;
	private String status;
	private Date gmtCreate;
	private Date gmtModify;
	private Integer version;

	// Constructors

	/** default constructor */
	public OrderDetail() {
	}

	public Integer getDetailId() {
		return detailId;
	}

	public void setDetailId(Integer detailId) {
		this.detailId = detailId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getProductionId() {
		return productionId;
	}

	public void setProductionId(Integer productionId) {
		this.productionId = productionId;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Integer getSkuAmmount() {
		return skuAmmount;
	}

	public void setSkuAmmount(Integer skuAmmount) {
		this.skuAmmount = skuAmmount;
	}

	public String getProductionName() {
		return productionName;
	}

	public void setProductionName(String productionName) {
		this.productionName = productionName;
	}

	public Double getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(Double skuPrice) {
		this.skuPrice = skuPrice;
	}

	public String getSkuDesc() {
		return skuDesc;
	}

	public void setSkuDesc(String skuDesc) {
		this.skuDesc = skuDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public OrderDetail(Integer detailId, Integer orderId, Integer productionId,
			Integer skuId, Integer skuAmmount, String productionName,
			Double skuPrice, String skuDesc, String status) {
		super();
		this.detailId = detailId;
		this.orderId = orderId;
		this.productionId = productionId;
		this.skuId = skuId;
		this.skuAmmount = skuAmmount;
		this.productionName = productionName;
		this.skuPrice = skuPrice;
		this.skuDesc = skuDesc;
		this.status = status;
	}

}