package com.irecomecar.kids.service;

import com.irecomecar.kids.model.auth.Authentication;
import com.irecomecar.kids.model.auth.Login;
import com.irecomecar.kids.model.auth.User;
import com.irecomecar.kids.util.Criptografia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	Criptografia criptografia;
	
	private final static String COLLECTION_LOGIN = "authentication";
	private final static String COLLECTION_USUARIO = "user";
	
	public Boolean validarLogin(Login login) {
		Query query = new Query(Criteria.where("email").is(login.getEmail())
				.and("senha").is(criptografia.criptografia(login.getSenha())));
		User user  = mongoTemplate.findOne(query, User.class, COLLECTION_USUARIO);
		if(user != null) {
			return true;
		}
		return false;
	}
	
	public Authentication login(Login login) {
		Query query = new Query(Criteria.where("login.email").is(login.getEmail()));
		Authentication authentication = mongoTemplate.findOne(query, Authentication.class, COLLECTION_LOGIN);
		if(authentication != null) {
			return authentication;
		}else {
			Authentication authenticationNew = new Authentication();
			login.setSenha(criptografia.criptografia(login.getSenha()));
			authenticationNew.setLogin(login);
			mongoTemplate
		    .indexOps(Authentication.class)
		    .ensureIndex(new Index().on("createdAt", Sort.Direction.ASC).expire(3600));
			return mongoTemplate.save(authenticationNew);
		}
	}

	public Authentication findById(String id) {
		return mongoTemplate.findById(id, Authentication.class);
	}
	
}
