package com.irecomecar.kids.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.irecomecar.kids.model.connect.Connect;
import com.irecomecar.kids.util.ServiceException;
import com.irecomecar.kids.util.StringHelper;
import com.mongodb.client.result.DeleteResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class ConnectService {

	@Autowired
	MongoTemplate mongoTemplate;
	
	private final static String COLLECTION = "connect";
	
	public Connect save(Connect connect) {
		try {
			if(validarConnect(connect)) {
				return mongoTemplate.save(connect, COLLECTION);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	public Boolean validarConnect(Connect connect) {
		Boolean retorno = true;
		List<String> mensagens = new ArrayList<>();
		if(!StringHelper.validarString(connect.getName())) {
			mensagens.add("O campo nome é obrigatório.");
		}
		if(!StringHelper.validarString(connect.getBirthDate())) {
			mensagens.add("O campo data de nascimento é obrigatório.");
		}
		if(!StringHelper.validarString(connect.getResponsible())) {
			mensagens.add("O campo responsável é obrigatório.");
		}
		if(!StringHelper.validarString(connect.getPhone())) {
			mensagens.add("O campo telefone é obrigatório.");
		}
		
		if(!StringHelper.validarTelefone(connect.getPhone()) && StringHelper.validarString(connect.getPhone())){
			mensagens.add("O campo telefone está no formato errado.");
		}
//		if(StringHelper.validarString(connect.getBirthDate())) {
//			mensagens.add("O campo data de nascimento está no formato errado.");
//		}
		if(!mensagens.isEmpty()) {
			throw new ServiceException(StringHelper.listToString(mensagens));
		}
		return retorno;
	}
	
	public List<Connect> list(){
		return mongoTemplate.findAll(Connect.class, COLLECTION);
	}
	
	public Connect findById(String id) {
		return mongoTemplate.findById(id, Connect.class, COLLECTION);
	}
	
	public DeleteResult delete(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		return mongoTemplate.remove(query, Connect.class, COLLECTION);
	}
	
	public Connect edit(Connect connect) {
		try {
			if(validarConnect(connect)) {
				Query query  = new Query(Criteria.where("id").is(connect.getId()));
				Connect usuarioAuxiliar = mongoTemplate.findOne(query, Connect.class);
				if(usuarioAuxiliar != null) {
					return mongoTemplate.save(connect, COLLECTION);
				}
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	public Boolean readCsv() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("banco_connect.csv"));
			String line;
			while ((line = br.readLine()) != null) {
				try {
					String[] values = line.split(";");
					Connect connect = new Connect();
					connect.setName(values[0]);
					connect.setBirthDate(values[1]);
					connect.setResponsible(values[2]);
					connect.setPhone(values[3]);
					System.out.println(values[0]);
					mongoTemplate.save(connect, COLLECTION);
				} catch (Exception e) {
					System.out.println("Erro na linha: "+line);
				}
			}
			br.close();
			return true;
		} catch (Exception e) {
			return false;
		} 
	}
	
	public static List<Connect> encontrarDuplicados(List<Connect> lista) {
        return lista.stream()
                .collect(Collectors.groupingBy(e -> e))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
	
	public void deleteDuplicate() {
		List<Connect> connectsDuplicado = encontrarDuplicados(mongoTemplate.findAll(Connect.class, COLLECTION));
		for(Connect connect: connectsDuplicado) {
//			Query queryBusca  = new Query(Criteria.where("name").is(connect.getName()));
//			Connect connectAux = mongoTemplate.findOne(queryBusca, Connect.class);
//			System.out.println(connectAux);
			
//			Query queryDelete = new Query(Criteria.where("id").is(connect.getId()));
//			mongoTemplate.remove(queryDelete, Connect.class, COLLECTION);
		}
	}

	public void deleteWrongAge() {
		List<Connect> conects = mongoTemplate.findAll(Connect.class, COLLECTION);
		Integer i = 1;
		for(Connect connect : conects) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate dataNascimento = LocalDate.parse(connect.getBirthDate(), formatter);
				LocalDate dataAtual = LocalDate.now();
				Period diferenca = Period.between(dataNascimento, dataAtual);
				Integer idade = diferenca.getYears();
				if(idade < 9 || idade > 12) {
					System.out.println("["+i+"] " + "Nome: " + connect.getName() + " | Data de Nascimento: " + connect.getBirthDate() + " | Idade: " + idade);
					
//					Query queryDelete = new Query(Criteria.where("id").is(connect.getId()));
//					mongoTemplate.remove(queryDelete, Connect.class, COLLECTION);
					
					i++;
				}
			} catch (Exception e) {
				try {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
					LocalDate dataNascimento = LocalDate.parse(connect.getBirthDate(), formatter);
					LocalDate dataAtual = LocalDate.now();
					Period diferenca = Period.between(dataNascimento, dataAtual);
					Integer idade = diferenca.getYears();
					if(idade < 9 || idade > 12) {
						System.out.println("["+i+"] " + "Nome: " + connect.getName() + " | Data de Nascimento: " + connect.getBirthDate() + " | Idade: " + idade);
						
//						Query queryDelete = new Query(Criteria.where("id").is(connect.getId()));
//						mongoTemplate.remove(queryDelete, Connect.class, COLLECTION);
						
						i++;
						
					}
				} catch (Exception e2) {
					System.out.println("["+i+"] " + "[E R R O] " + "Nome: " + connect.getName() + " | Data de Nascimento: " + connect.getBirthDate());
//					Query queryDelete = new Query(Criteria.where("id").is(connect.getId()));
//					mongoTemplate.remove(queryDelete, Connect.class, COLLECTION);
					i++;
				}
			}
			
		}
		
	}
	
}
