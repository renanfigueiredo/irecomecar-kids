package com.irecomecar.kids.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.irecomecar.kids.model.auth.Authentication;
import com.irecomecar.kids.model.auth.Login;
import com.irecomecar.kids.model.auth.Token;
import com.irecomecar.kids.service.LoginService;

@RestController
@RequestMapping("login")
public class LoginController {

	@Autowired
	LoginService loginService;
	
	@PostMapping()
	public ResponseEntity<?> login(@RequestBody Login login){
		Boolean validaLogin = loginService.validarLogin(login);
		if(validaLogin) {
			Authentication authentication = loginService.login(login);
			Token token = new Token();
			token.setToken(authentication.getId());
			return ResponseEntity.ok(token);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso Negado");
	}
	
	@GetMapping()
	public ResponseEntity<?> getToken(@RequestHeader("Authorization") String auth){
		return ResponseEntity.ok(loginService.findById(auth));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> logout(@PathVariable String id){
		return ResponseEntity.ok("ok");
	}
	
}
