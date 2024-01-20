package com.irecomecar.kids.service;

import java.util.List;

import com.irecomecar.kids.model.auth.User;
import com.irecomecar.kids.util.Criptografia;
import com.mongodb.client.result.DeleteResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	Criptografia criptografia;
	
	private final static String COLLECTION = "user";
	
	public User save(User user) {
		//usuario.setSenha(criptografia.criptografia(user.getSenha()));
		return mongoTemplate.save(user, COLLECTION);
	}
	
	public List<User> list(){
		return mongoTemplate.findAll(User.class, COLLECTION);
	}
	
	public User findById(String id) {
		return mongoTemplate.findById(id, User.class, COLLECTION);
	}
	
	public DeleteResult delete(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		return mongoTemplate.remove(query, User.class, COLLECTION);
	}
	
	public User edit(User usuario) {
		Query query  = new Query(Criteria.where("id").is(usuario.getId()));
		User usuarioAuxiliar = mongoTemplate.findOne(query, User.class);
		if(usuarioAuxiliar != null) {
			//usuario.setSenha(criptografia.criptografia(usuario.getSenha()));
			return mongoTemplate.save(usuario, COLLECTION);
		}
		return null;
	}
	
}
