package com.example.webservices.user;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFilter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Validation for users")
@JsonFilter("User Filter")
@Entity
public class User {
	
	//Add @JsonIgnore to the filed to ignore that filed in output -- example of static filter
	
	

	@Id
	@GeneratedValue
	private  Integer id;
	
	@Size(min =2, message = "name should be atleast 2 character")
	@ApiModelProperty(notes = "should have atleast 2 character")
	private String name;
	
	@OneToMany(mappedBy = "user")
	private List<Post> post;
	
	
	public List<Post> getPost() {
		return post;
	}

	public void setPost(List<Post> post) {
		this.post = post;
	}

	@Past
	@ApiModelProperty(notes = "should be in past")
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
	

	public User() {
		super();
	}

	public User(Integer id, String name, Date dob) {
		super();
		this.id = id;
		this.name = name;
		this.dob = dob;
	}

	
	
}
