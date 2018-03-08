package com.brianchang.web.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.brianchang.web.models.Subpackage;

@Repository
public interface SubpackageRepository extends CrudRepository<Subpackage, Long> {
	List<Subpackage> findAll();
}
