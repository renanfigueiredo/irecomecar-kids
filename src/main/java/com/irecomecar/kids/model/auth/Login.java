package com.irecomecar.kids.model.auth;

import org.springframework.data.annotation.Transient;

public class Login {

	private String email;
	
	@Transient
	private String senha;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
}
