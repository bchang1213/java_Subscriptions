package com.brianchang.web.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.brianchang.web.models.Subpackage;
import com.brianchang.web.repositories.SubpackageRepository;
import com.brianchang.web.repositories.UserRepository;

@Service
public class SubscriptionService {
	//Dependency Injection
	private UserRepository userRepo;
	private SubpackageRepository packRepo;
	
	public SubscriptionService(UserRepository userRepo, SubpackageRepository packRepo) {
		this.userRepo = userRepo;
		this.packRepo = packRepo;
	}
	//methods
	
	//create pack
	public void createPackage(Subpackage pack) {
		packRepo.save(pack);
	}
	
	//find all packs
	public List<Subpackage> findAllPackages(){
		return packRepo.findAll();
	}
	
	//find package
	public Subpackage findPackage(Long id) {
		return packRepo.findOne(id);
	}
	
	//Set status for package
	public void setPackageStatus(Long id, Boolean status) {
		Subpackage subpackage = packRepo.findOne(id);
		subpackage.setStatus(status);
		packRepo.save(subpackage);
	}
}
