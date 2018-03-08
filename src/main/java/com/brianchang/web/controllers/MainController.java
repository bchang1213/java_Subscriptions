package com.brianchang.web.controllers;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.brianchang.web.models.Subpackage;
import com.brianchang.web.models.User;
import com.brianchang.web.services.SubscriptionService;
import com.brianchang.web.services.UserService;
import com.brianchang.web.validator.UserValidator;

@Controller
public class MainController {
	//dependency injection
	private UserService userservice;
	private SubscriptionService subservice;
	private UserValidator validator;
	
	public MainController(UserService userservice, SubscriptionService subservice, UserValidator validator) {
		this.userservice = userservice;
		this.validator = validator;
		this.subservice = subservice;
	}	
	
	
	//PLEASE LOGIN, VIEW #1
	@RequestMapping("/login")
	public String login(@Valid @ModelAttribute("user") User user, @RequestParam(value="error", required=false) String error, @RequestParam(value="logout", required=false) String logout, Model model) {
		if(error != null) {
			model.addAttribute("errorMessage", "Invalid Credentials, Please try again.");
		}
		if(logout != null) {
			model.addAttribute("logoutMessage", "Logout Successfull!");
		}
		return "registrationPage.jsp";
	}
	
	//POST ROUTE UPON REGISTRATION
	@PostMapping("/registration")
	public String registration(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
		//Testing to see if any admins exist
		Boolean adminCheck = userservice.checkforAdmin();
		
		validator.validate(user, result);
		if (result.hasErrors()) {
			return "registrationPage.jsp";
		}
		//If no admins...
		if(adminCheck == false) {
			model.addAttribute("message","Success.");
			userservice.saveUserWithAdminRole(user);
			return "redirect:/login";
		}
		model.addAttribute("message","Success.");
		userservice.saveWithUserRole(user);
		return "redirect:/login";
	}
	
	//DASHBOARD *ACCESS GRANTED
	@RequestMapping(value = "/")
	public String home(Principal principal, Model model, @ModelAttribute("user")User user) {
		// 1	 retrieve info of user who hsa logged in.
		String email = principal.getName();
		User currentUser = userservice.findByEmail(email);
		Boolean isAdmin = userservice.checkifAdmin(currentUser);
		//IF IS ADMIN
		if(isAdmin == true) {
			return "redirect:/admin";
		}
		if(currentUser.getSubpackage() == null) {
			//If is not admin.
			List<Subpackage> subpackages = subservice.findAllPackages();
			model.addAttribute("subpackages", subpackages);
			model.addAttribute("currentUser", currentUser);
			return "dashboard.jsp";			
		}
		return "redirect:/profile";
    }
	
	//User subscribes
	@PostMapping("/subscribe/{userid}")
	public String subscribe(@Valid @ModelAttribute("user")User user, BindingResult result, @PathVariable("userid")Long userid, Principal principal, Model model) {
		if(result.hasErrors()) {
			String email = principal.getName();
			User currentUser = userservice.findByEmail(email);
			Boolean isAdmin = userservice.checkifAdmin(currentUser);
			List<Subpackage> subpackages = subservice.findAllPackages();
			model.addAttribute("subpackages", subpackages);
			model.addAttribute("currentUser", currentUser);
			return "dashboard.jsp";
		}
		userservice.createSubscription(userid, user);
		return "redirect:/profile";
	}
	
	//User Profile
	@RequestMapping("/profile")
	public String showprofile(Principal principal, Model model) {
		String email = principal.getName();
		User currentUser = userservice.findByEmail(email);
		Boolean isAdmin = userservice.checkifAdmin(currentUser);
		model.addAttribute("currentUser", currentUser);
		return "profile.jsp";
	}
	
	
	// ADMIN TERRITORY ////////////////////////////////////////////////////////////////////////
	//ADMIN ROUTE
    @RequestMapping("/admin")
    public String adminPage(@ModelAttribute("pack")Subpackage pack, Principal principal, Model model) {
        String email = principal.getName();
        User currentUser = userservice.findByEmail(email);
        
		List<User> allAdmins = userservice.findAdmins();
		List<User> allUsers = userservice.findAllUsers();
		List<Subpackage> allpacks = subservice.findAllPackages();
		
		model.addAttribute("allpacks", allpacks);
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("allAdmins", allAdmins);
		model.addAttribute("allUsers", allUsers);
        return "adminPage.jsp";
    }
    
    //Admin creates package
    @PostMapping("/admin/packages/new")
    public String createPackage(@Valid @ModelAttribute("pack")Subpackage pack, BindingResult result, Model model, Principal principal) {
    	if(result.hasErrors()) {
    		String email = principal.getName();
    		User currentUser = userservice.findByEmail(email);
            
    		List<User> allAdmins = userservice.findAdmins();
    		List<User> allUsers = userservice.findAllUsers();
    		
    		model.addAttribute("currentUser", currentUser);
    		model.addAttribute("allAdmins", allAdmins);
    		model.addAttribute("allUsers", allUsers);
    		return "adminPage.jsp";
    	}
    	subservice.createPackage(pack);
    	return "redirect:/admin";
    }
    
    //Admin activates
    @RequestMapping("/admin/packages/activate/{packid}")
    public String activate(@PathVariable("packid")Long packid) {
    	subservice.setPackageStatus(packid, true);
    	return "redirect:/admin";
    }
    
    //Admin deactivates
	@RequestMapping("/admin/packages/deactivate/{packid}")
	public String deactivate(@PathVariable("packid")Long packid) {
		subservice.setPackageStatus(packid, false);
		return "redirect:/admin";
	}
    
	//Delete user
	@RequestMapping("/admin/delete/{userid}")
	public String deleteuser(@PathVariable("userid")Long id) {
		userservice.deleteUserByID(id);
		return "redirect:/";
	}
	
	//Make user an Admin
	@RequestMapping("admin/{userid}/allow")
	public String makeAdmin(@PathVariable("userid")Long id) {
		userservice.makeAdmin(id);
		return "redirect:/";
	}
}
