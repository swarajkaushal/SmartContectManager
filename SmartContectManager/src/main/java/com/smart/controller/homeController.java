package com.smart.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class homeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
   @Autowired
    private UserRepository userRepository;

	@GetMapping("/")
	public String hone(Model model) {
		model.addAttribute("title","Home-Smart Contect Manager");
		return "home";
		
	}
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","about-Smart Contect Manager");
		return "about";
		
	}
	@GetMapping("/sinup")
	public String sinup(Model model) {
		model.addAttribute("title","Register-Smart Contect Manager");
		model.addAttribute("user",new User());
		return "sinup";

}
	//handler for registering user
	@PostMapping("/do_register")
	public String registeruser(@Valid @ModelAttribute("user") User user,BindingResult result1,@RequestParam(value ="agreement",defaultValue = "false")boolean agreement,Model model,HttpSession session)
	{
		try {
			if (!agreement) {
				System.out.println("you have not agreed the terms and condition");
				throw new Exception("you have not agreed the terms and condition");
				}
			if (result1 .hasErrors()) {
				System.out.println("ERROR in binding"+result1.toString());
				model.addAttribute("user",user);
				return "sinup";
			}
				user.setRole("ROLE_USER");
				user.setEnabled(true);
				user.setImageurl("default.png");
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				
				System.out.println("Agreement" + agreement);
				System.out.println("USER"+ user);
				User result = this.userRepository.save(user);
				model.addAttribute("user" ,new User());
				session.setAttribute("message", new Message("successfully registered", "alert-error"));
				return "sinup";
		} catch (Exception e) {
			// TODO: handle exception
			
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("somethings wents wrong!!"+e.getMessage(), "alert-error"));
			return "sinup";

		}
		
		
	}
	//handler for custom login
		@GetMapping("/signin")
		public String customLogin(Model model)
		{
			model.addAttribute("title","Login Page");
			return "login";
		}
		
}
