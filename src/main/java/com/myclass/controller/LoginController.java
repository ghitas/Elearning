package com.myclass.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("admin/login")
public class LoginController {
	
	@GetMapping("")
	public String getAuth(@RequestParam(defaultValue = "") String error, Model model) {
		if(!error.equals("")) {
			model.addAttribute("message", "Sai ten dang nhap hoac mat khau");
		}
		return "auth/login";
	}
}
