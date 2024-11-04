package com.erunjrun.icon.dto;

import java.sql.Date;

import com.erunjrun.member.dto.MemberDTO;

public class IconDTO {

	// 아이콘 테이블
	private int icon_idx;
	private String icon_name;
	private int cost;
	private String image; // int -> String으로 바꿈
	private Date create_date;
	private Date update_date;
	private String use_yn;
	
	//아이콘 구매 테이블
	private int buy_idx;
	private String id;
	private Date buy_date;
	
	private String bought;
	private int icon_quantity;
	private int total_count;

	//회원 테이블
	private int point;

	public int getTotal_count() {
		return total_count;
	}
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	public int getIcon_quantity() {
		return icon_quantity;
	}
	public void setIcon_quantity(int icon_quantity) {
		this.icon_quantity = icon_quantity;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getBought() {
		return bought;
	}
	public void setBought(String bought) {
		this.bought = bought;
	}
	public int getIcon_idx() {
		return icon_idx;
	}
	public void setIcon_idx(int icon_idx) {
		this.icon_idx = icon_idx;
	}
	public String getIcon_name() {
		return icon_name;
	}
	public void setIcon_name(String icon_name) {
		this.icon_name = icon_name;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	
	
	
	
	public int getBuy_idx() {
		return buy_idx;
	}
	public void setBuy_idx(int buy_idx) {
		this.buy_idx = buy_idx;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getBuy_date() {
		return buy_date;
	}
	public void setBuy_date(Date buy_date) {
		this.buy_date = buy_date;
	}
	
	
}
