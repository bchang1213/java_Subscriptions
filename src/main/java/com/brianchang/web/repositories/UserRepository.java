package com.brianchang.web.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.brianchang.web.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	User findByEmail(String email);
	
	List<User> findAll();
	
	//Find all users who are admins.
	@Query("SELECT u from User u JOIN u.roles r WHERE r.id = 2")
	List<User> findAdmins();
}
