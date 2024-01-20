package com.irecomecar.kids.controller;

import java.util.List;

import com.irecomecar.kids.model.connect.Connect;
import com.irecomecar.kids.service.ConnectService;
import com.irecomecar.kids.util.StringHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("connect")
public class ConnectController {

	@Autowired
	ConnectService connectService;


	@GetMapping("index")
	public String showConnectList(Model model) {
		model.addAttribute("connects", connectService.list());
		return "connect/index";
	}

	@GetMapping("signup")
	public String showSignUpForm(Connect connect, Model model) {
		model.addAttribute("errors", null);
		return "connect/add-connect";
	}

	@PostMapping("addconnect")
	public String save(Connect connect, BindingResult result, Model model) {
		List<String> err = null;
		try {
			connectService.save(connect);
			return "redirect:/connect/index";
		} catch (Exception e) {
			err = StringHelper.stringAsList(e.getMessage());
			model.addAttribute("errors", err);
			return "connect/add-connect";
		}
		
	}

	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable("id") String id, Model model) {
		Connect connect = connectService.findById(id);
		model.addAttribute("connect", connect);
		model.addAttribute("errors", null);
		return "connect/update-connect";
	}

	@PostMapping("update/{id}")
	public String edit(@PathVariable("id") String id, Connect connect, BindingResult result, Model model) {
		List<String> err = null;
		try {
			connectService.edit(connect);
			return "redirect:/connect/index";
		} catch (Exception e) {
			connect.setId(connect.getId());
			err = StringHelper.stringAsList(e.getMessage());
			model.addAttribute("errors", err);
			return "connect/update-connect";
		}
	}

	@GetMapping("delete/{id}")
	public String delete(@PathVariable String id, Model model) {
		connectService.delete(id);
		return "redirect:/connect/index";
	}
	
	@GetMapping("deleteWrongAge")
	public String deleteWrongAge() {
		connectService.deleteWrongAge();
		return "redirect:/connect/index";
	}
	
	@GetMapping("deleteDuplicate")
	public String deleteDuplicate() {
		connectService.deleteDuplicate();
		return "redirect:/connect/index";
	}

}
