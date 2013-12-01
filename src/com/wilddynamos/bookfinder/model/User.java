package com.wilddynamos.bookfinder.model;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 6390633462260174009L;

	private int id;
	private String email;
	private String password;
	private String name;
	private Boolean gender;
	private String campus;
	private String contact;
	private String address;
	private String photoPath;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhotoAddr() {
		return photoPath;
	}

	public void setPhotoAddr(String photoPath) {
		this.photoPath = photoPath;
	}
}
