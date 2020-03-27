package com.example.webservices.user;

import java.util.Date;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;


public class User {
	
	private Integer id;
	
	@Size(min =2, message = "name should be atleast 2 character")
	private String name;
	
	
	@Past
	private Date dob;

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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public User(Integer id, String name, Date dob) {
		super();
		this.id = id;
		this.name = name;
		this.dob = dob;
	}

	
	
}
