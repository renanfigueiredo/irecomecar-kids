package com.irecomecar.kids.controller;

import java.util.ArrayList;
import java.util.List;

import com.irecomecar.kids.model.connect.Connect;
import com.irecomecar.kids.model.worship.ConnectBracelet;
import com.irecomecar.kids.model.worship.ConnectVisitor;
import com.irecomecar.kids.model.worship.Worship;
import com.irecomecar.kids.model.worship.WorshipConnect;
import com.irecomecar.kids.service.ConnectService;
import com.irecomecar.kids.service.WorshipService;
import com.irecomecar.kids.util.StringHelper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("worship")
public class WorshipController {

	@Autowired
	WorshipService worshipService;
	
	@Autowired
	ConnectService connectService;
	
	@GetMapping("signup")
    public String showSignUpForm(Worship worship, Model model) {
		return "worship/add-worship";
    }
	
	@GetMapping("details/{id}")
    public String showDetails(@PathVariable("id") String id, Model model) {
	    Worship worship = worshipService.findById(id);
	    model.addAttribute("worship", worship);
	    return "worship/details-worship";
    }
	
	@GetMapping("{id}/list-connect")
    public String listConnect(@PathVariable("id") String id, Model model) {
		List<Connect> connects = connectService.list();
	    Worship worship = worshipService.findById(id);
	    model.addAttribute("connects", connects);
	    model.addAttribute("worship", worship);
	    return "worship/list-connect";
    }
	
	@GetMapping("{idWorship}/add-connect/{idConnect}")
    public String insertConnect(@PathVariable("idWorship") String idWorship, 
    		@PathVariable("idConnect") String idConnect, Model model) {
		Connect connect = connectService.findById(idConnect);
	    Worship worship = worshipService.findById(idWorship);
	    model.addAttribute("worship", worship);
	    model.addAttribute("connect", connect);
	    model.addAttribute("worshipConnect", new WorshipConnect());
	    return "worship/add-connect";
    }
	
	@GetMapping("index")
	public String showWorshipList(Model model) {
	    model.addAttribute("worships", worshipService.list());
	    return "worship/index";
	}
	
	@PostMapping("addworship")
	public String save(Worship worship, BindingResult result, Model model){
		
		 if (result.hasErrors()) {
	            return "/worship/add-worship";
	        }
		
		worshipService.save(worship);
		return "redirect:/worship/index";
	}
	
	@PostMapping("add-connect-worship")
	public String addConnectWorhip(@ModelAttribute WorshipConnect worshipConnect, BindingResult result, Model model){
		Worship worship = worshipService.findById(worshipConnect.getWorshipId());
		Connect connect = connectService.findById(worshipConnect.getConnectId());
		Integer bracelet = worshipConnect.getBraceletNumber();
		worshipService.addToWorship(worship, connect, bracelet);
		return "redirect:/worship/details/" + worshipConnect.getWorshipId();
	}
	
	@GetMapping("{idWorship}/add-connect-visitor")
    public String insertConnectVisitor(@PathVariable("idWorship") String idWorship, Model model) {
	    Worship worship = worshipService.findById(idWorship);
	    model.addAttribute("worship", worship);
	    model.addAttribute("connectVisitor", new ConnectVisitor());
	    model.addAttribute("errors", null);
	    return "worship/add-connect-visitor";
    }
	
	@PostMapping("add-connect-visitor-worship")
	public String save(@ModelAttribute ConnectVisitor connectVisitor, BindingResult result, Model model){
		
		List<String> err = null;
		
		//Transformar o form no Connect para salvar no banco de dados;
		
		try {
			Connect connect = new Connect();
			connect.setName(connectVisitor.getName());
			connect.setBirthDate(connectVisitor.getBirthDate());
			connect.setPhone(connectVisitor.getPhone());
			connect.setResponsible(connectVisitor.getResponsible());
			
			if(connectService.validarConnect(connect)) {
				if(!StringHelper.validateBracelet(connectVisitor.getBraceletNumber())){
					Worship worship = worshipService.findById(connectVisitor.getIdWorship());
				    model.addAttribute("worship", worship);
				    model.addAttribute("connectVisitor", connectVisitor);
					err = StringHelper.stringAsList("O número da pulseira é obrigatório.");
					model.addAttribute("errors", err);
					return "worship/add-connect-visitor";
				}
			}
			
			Connect connectSaved = connectService.save(connect);
			
			
			Worship worship = worshipService.findById(connectVisitor.getIdWorship());
			worshipService.addToWorship(worship, connectSaved, connectVisitor.getBraceletNumber());
			
			return "redirect:/worship/details/" + connectVisitor.getIdWorship();
		} catch (Exception e) {
			Worship worship = worshipService.findById(connectVisitor.getIdWorship());
		    model.addAttribute("worship", worship);
		    model.addAttribute("connectVisitor", connectVisitor);
			err = StringHelper.stringAsList(e.getMessage());
			if(!StringHelper.validateBracelet(connectVisitor.getBraceletNumber())){
				err.add("O número da pulseira é obrigatório.");
			}
			model.addAttribute("errors", err);
			return "worship/add-connect-visitor";
		}
		
	}
	
	@GetMapping("connect/delete/{idWorship}/{idConnect}")
	public String deleteConnect(@PathVariable String idWorship, @PathVariable String idConnect, Model model){
		worshipService.deleteConnect(idWorship,idConnect);
		return "redirect:/worship/details/" + idWorship;
	}
	
	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable("id") String id, Model model) {
	    Worship worship = worshipService.findById(id);
	    model.addAttribute("worship", worship);
	    model.addAttribute("statusList", worship.getStatus());
	    return "worship/update-worship";
	}
	
	@PostMapping("update/{id}")
	public String edit(@PathVariable("id") String id, Worship worship, BindingResult result, Model model){
		if (result.hasErrors()) {
			worship.setId(worship.getId());
	        return "worship/update-worship";
	    }
		worshipService.edit(worship);
		return "redirect:/worship/index";
	}
	
	@GetMapping("delete/{id}")
	public String delete(@PathVariable String id, Model model){
		worshipService.delete(id);
		return "redirect:/worship/index";
	}
	
	@GetMapping("closeAll")
    public String closeAllWorships(Model model) {
		List<Worship> worships = worshipService.closeAllWorships();
		model.addAttribute("worships", worships);
		return "worship/index";
    }
	
}
