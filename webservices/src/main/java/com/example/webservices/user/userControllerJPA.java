package com.example.webservices.user;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

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

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
public class userControllerJPA {
	
	@Autowired
	private UserDao userdao;
	
	private Post post;
	
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PostRepository postRepo;
	
	
	@GetMapping(path = "/jpa/findUsers")
	public MappingJacksonValue findUsers(){
		
		List<User> users = userRepo.findAll();
		
		SimpleBeanPropertyFilter filter =  SimpleBeanPropertyFilter.filterOutAllExcept("id","name"); //dynamic filtering
		FilterProvider filters = new SimpleFilterProvider().addFilter("User Filter", filter);
		
		MappingJacksonValue map = new MappingJacksonValue(users);
		map.setFilters(filters);
			
		
		return map;
	}
	
	@GetMapping(path = "/jpa/findUser/{id}")
	public MappingJacksonValue findUser(@PathVariable Integer id){
		
		SimpleBeanPropertyFilter filter =  SimpleBeanPropertyFilter.filterOutAllExcept("dob","name"); //dynamic filtering
		FilterProvider filters = new SimpleFilterProvider().addFilter("User Filter", filter);
				
		Optional<User> uid = userRepo.findById(id);
		
		if(!uid.isPresent())
			throw new UserNotFoundException("id- "+id);
		
		Resource<User> res = new Resource<User>(uid.get());
		ControllerLinkBuilder link = linkTo(methodOn(this.getClass()).findUsers());
		res.add(link.withSelfRel());
		MappingJacksonValue map = new MappingJacksonValue(uid);
		map.setFilters(filters);
		map.setValue(res);
				
		return map;
	}

	
	// ResponseEntity ---> Extension of HttpEntity that adds a HttpStatus status code.
	//Used in RestTemplate as well @Controller methods.
	
	@PostMapping(path = "/jpa/saveUser")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		
		User newuser = userRepo.save(user);
		
	//	Resource<User> res = new Resource<User>(newuser);
		ControllerLinkBuilder link = linkTo(methodOn(this.getClass()).findUser(newuser.getId()));
		
		
	//	res.add(link.withRel("all_users"));
		
		
		MappingJacksonValue map = new MappingJacksonValue(newuser);
		ResponseEntity<User> value = ResponseEntity.created(link.toUri()).build();
	//	map.setValue(res);
		map.setValue(value);
	
		return value;
		
		
		
	}
	
	@DeleteMapping(path = "/jpa/user/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable Integer id ){
		
		userRepo.deleteById(id);
		
		return ResponseEntity.noContent().build();
		
	}
	
	
	@GetMapping(path = "/jpa/findUser/{id}/posts")
	public MappingJacksonValue findUserPosts(@PathVariable Integer id){
		
		SimpleBeanPropertyFilter filter =  SimpleBeanPropertyFilter.filterOutAllExcept("name","post"); //dynamic filtering	
		FilterProvider filters = new SimpleFilterProvider().addFilter("User Filter", filter);
//		
		
		
		Optional<User> uid = userRepo.findById(id);
		
		if(!uid.isPresent())
			throw new UserNotFoundException("id- "+id);
		
		
		
//		Resource<User> res = new Resource<User>(uid.get());
//		ControllerLinkBuilder link = linkTo(methodOn(this.getClass()).findUsers());
//		res.add(link.withSelfRel());
		MappingJacksonValue map = new MappingJacksonValue(uid.get());
		map.setFilters(filters);
	//	map.setValue(res);
				
		return map;
	}
	
	@PostMapping(path = "/jpa/user/{id}/saveUser")
	public ResponseEntity<User> createPost(@Valid @RequestBody Post post, @PathVariable Integer id) {
		
		Optional<User> uid = userRepo.findById(id);
		
		if(!uid.isPresent())
			throw new UserNotFoundException("id- "+id);
		
		post.setUser(uid.get());
		postRepo.save(post);
		
		
		
	//	Resource<User> res = new Resource<User>(newuser);
		ControllerLinkBuilder link = linkTo(methodOn(this.getClass()).findUserPosts(id));
		
		
	//	res.add(link.withRel("all_users"));
		
		
		MappingJacksonValue map = new MappingJacksonValue(uid.get());
		ResponseEntity<User> value = ResponseEntity.created(link.toUri()).build();
	//	map.setValue(res);
		map.setValue(value);
	
		return value;
		
		
		
	}
}
