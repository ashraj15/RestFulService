package com.example.webservices.user;


import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

//import org.springframework.hateoas.Resource;

//import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
public class userController {
	
	@Autowired
	private UserDao userdao;
	
	
	
	
	
	@GetMapping(path = "/findUsers")
	public MappingJacksonValue findUsers(){
		
		List<User> users = userdao.findAll();
		
		SimpleBeanPropertyFilter filter =  SimpleBeanPropertyFilter.filterOutAllExcept("id","name"); //dynamic filtering
		FilterProvider filters = new SimpleFilterProvider().addFilter("User Filter", filter);
		
		MappingJacksonValue map = new MappingJacksonValue(users);
		map.setFilters(filters);
			
		
		return map;
	}
	
	@GetMapping(path = "/findUser/{id}")
	public MappingJacksonValue findUser(@PathVariable Integer id ){
		
		SimpleBeanPropertyFilter filter =  SimpleBeanPropertyFilter.filterOutAllExcept("dob","name"); //dynamic filtering
		FilterProvider filters = new SimpleFilterProvider().addFilter("User Filter", filter);
		
		
		
		User uid = userdao.findOne(id);
		
		Resource<User> res = new Resource<User>(uid);
		ControllerLinkBuilder link = linkTo(methodOn(this.getClass()).findUsers());
		res.add(link.withRel("all_users"));
		
		MappingJacksonValue map = new MappingJacksonValue(uid);
		map.setFilters(filters);
		map.setValue(res);
		if(uid == null)
			throw new UserNotFoundException("id- "+id);
		return map;
	}

	
	// ResponseEntity ---> Extension of HttpEntity that adds a HttpStatus status code.
	//Used in RestTemplate as well @Controller methods.
	
	@PostMapping(path = "/saveUser")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		
		User newuser = userdao.save(user);
		
		
	//	Resource<User> res = new Resource<User>(newuser);
		//fromPath is used to build uri from the passed argument to fromPath
		// path ---> Append the given path to the existing path of this builder.
		// Build a UriComponents instance and replaces URI template variables with passed parameter
		
		
	//	URI location = ServletUriComponentsBuilder.fromPath("/findUser").path("/{id}").buildAndExpand(newuser.getId()).toUri();
		
		// returns location as /findUser/4
		
		//fromPath cannot use query parameter in argument to fromPath
		
		String url = "http://localhost:8080/findUser/{id}";
		
		//returns http://localhost:8080/findUser/4
		URI location = ServletUriComponentsBuilder.fromHttpUrl(url).buildAndExpand(newuser.getId()).toUri();	
		
		
		//returns /findUser/4 but can use query parameter {}
	//	URI location = ServletUriComponentsBuilder.fromUriString("/findUser/{value}").buildAndExpand(newuser.getId()).toUri();
		
		// return 201 created http status along with new uri (location)created in Header part
		
	//	ControllerLinkBuilder link = linkTo(methodOn(this.getClass()).findUser(newuser.getId()));
	//	System.out.println("Link is: "+link);
	//	res.add(link.withRel("user with id"));
		
	//	return res;
		return ResponseEntity.created(location).build();
		
		
		
	}
	
	@DeleteMapping(path = "/user/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable Integer id ){
		
		User uid = userdao.deleteById(id);
		if(uid == null)
			throw new UserNotFoundException("id- "+id);
		
		return ResponseEntity.noContent().build();
		
	}
}
