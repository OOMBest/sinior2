package com.zhangdake.senior2.week2;

import java.math.BigDecimal;

/**
 * Author: ZhangDaKe
 * Date: 2019年10月14日
 * Describe: 
 * Version: 1.0
 */
public class Goods {

	private Integer id;
	private String name;
	private BigDecimal price;
	private int soldOut;
	
	public Goods() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getSoldOut() {
		return soldOut;
	}

	public void setSoldOut(int soldOut) {
		this.soldOut = soldOut;
	}

	@Override
	public String toString() {
		return "Goods [id=" + id + ", name=" + name + ", price=" + price + ", soldOut="
				+ soldOut + "]";
	}
	
}


