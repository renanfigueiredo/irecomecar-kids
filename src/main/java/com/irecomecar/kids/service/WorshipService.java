package com.irecomecar.kids.service;

import java.util.ArrayList;
import java.util.List;

import com.irecomecar.kids.model.connect.Connect;
import com.irecomecar.kids.model.worship.ConnectBracelet;
import com.irecomecar.kids.model.worship.Status;
import com.irecomecar.kids.model.worship.Worship;
import com.irecomecar.kids.util.Criptografia;
import com.mongodb.client.result.DeleteResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class WorshipService {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	Criptografia criptografia;
	
	@Autowired
	ConnectService connectService;
	
	private final static String COLLECTION = "worship";
	
	public Worship save(Worship worship) {
		//usuario.setSenha(criptografia.criptografia(worship.getSenha()));
		return mongoTemplate.save(worship, COLLECTION);
	}
	
	public List<Worship> list(){
		return mongoTemplate.findAll(Worship.class, COLLECTION);
	}
	
	public Worship findById(String id) {
		return mongoTemplate.findById(id, Worship.class, COLLECTION);
	}
	
	public DeleteResult delete(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		return mongoTemplate.remove(query, Worship.class, COLLECTION);
	}
	
	public Worship edit(Worship worship) {
		Query query  = new Query(Criteria.where("id").is(worship.getId()));
		Worship worshipAux = mongoTemplate.findOne(query, Worship.class);
		if(worshipAux != null) {
			//usuario.setSenha(criptografia.criptografia(usuario.getSenha()));
			if(worshipAux.getConnectBracelet() != null && !worshipAux.getConnectBracelet().isEmpty()) {
				worship.setConnectBracelet(worshipAux.getConnectBracelet());
			}
			return mongoTemplate.save(worship, COLLECTION);
		}
		return null;
	}
	
	public Worship addToWorship(Worship worship, Connect connect, Integer bracelet) {
		ConnectBracelet connectBracelet = new ConnectBracelet();
		connectBracelet.setBracelet(bracelet);
		connectBracelet.setConnect(connect);
		List<ConnectBracelet> connectBracelets = worship.getConnectBracelet();
		if(connectBracelets == null || connectBracelets.isEmpty()) {
			List<ConnectBracelet> connectBraceletsEmpty = new ArrayList<>();
			connectBraceletsEmpty.add(connectBracelet);
			worship.setConnectBracelet(connectBraceletsEmpty);
		}else {
			connectBracelets.add(connectBracelet);
		}
		
		Query query  = new Query(Criteria.where("id").is(worship.getId()));
		Worship worshipAux = mongoTemplate.findOne(query, Worship.class);
		if(worshipAux != null) {
			return mongoTemplate.save(worship, COLLECTION);
		}
		return null;
	}

	public Boolean deleteConnect(String idWorship, String idConnect) {
		try {
			Query query = new Query(Criteria.where("id").is(idWorship));
			Worship worship = mongoTemplate.findOne(query, Worship.class);
			List<ConnectBracelet> connectBracelets = worship.getConnectBracelet();
			for(ConnectBracelet connectBracelet: connectBracelets) {
				if(connectBracelet.getConnect().getId().equals(idConnect)) {
					connectBracelets.remove(connectBracelet);
					break;
				}
			}
			worship.setConnectBracelet(connectBracelets);
			mongoTemplate.save(worship, COLLECTION);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	public List<Worship> closeAllWorships(){
		List<Worship> worships =  mongoTemplate.findAll(Worship.class, COLLECTION);
		for(Worship worship : worships) {
			worship.setStatus(Status.ENCERRADO);
			mongoTemplate.save(worship, COLLECTION);
		}
		return worships;
	}
	
	public List<Worship> findOpenWorships(){
		Query query = new Query(Criteria.where("status").is("ABERTO"));
		List<Worship> worships =  mongoTemplate.find(query, Worship.class, COLLECTION);
		return worships;
	}
	
}
