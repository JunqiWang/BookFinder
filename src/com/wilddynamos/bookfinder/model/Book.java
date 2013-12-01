package com.wilddynamos.bookfinder.model;

import java.io.Serializable;
import java.util.Date;

public class Book implements Serializable {
	
	private static final long serialVersionUID = -6642891755641628595L;
	
	private int id;
	private String name;
	private int price;
	private Boolean per;
	private int availableTime;
	private int likes;
	private Boolean sOrR;
	private Boolean state;
	private String description;
	private Date postTime;
	private User owner;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public Boolean getPer() {
		return per;
	}
	
	public void setPer(Boolean per) {
		this.per = per;
	}
	
	public int getAvailableTime() {
		return availableTime;
	}
	
	public void setAvailableTime(int availableTime) {
		this.availableTime = availableTime;
	}
	
	public int getLikes() {
		return likes;
	}
	
	public void setLikes(int likes) {
		this.likes = likes;
	}
	
	public Boolean getSOrR() {
		return sOrR;
	}
	
	public void setSOrR(Boolean sOrR) {
		this.sOrR = sOrR;
	}
	
	public Boolean getState() {
		return state;
	}
	
	public void setState(Boolean state) {
		this.state = state;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getPostTime() {
		return postTime;
	}
	
	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}
	
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
}
