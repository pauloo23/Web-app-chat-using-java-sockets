package server;

import java.util.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class Audiencias {
	//utilizadores registados
	private static Hashtable<String, PInfo> presentIPs = new Hashtable<String, PInfo>();
	//mensagens registadas
	private static ArrayList<Message> messages = new ArrayList<Message>();
	private String curAns;

	public String AddPresence(String name, String type) {
		PInfo tmp = new PInfo(name, type);
		String id =UUID.randomUUID().toString();
		//adiciona um novo utilizador e devolve o seu id
		presentIPs.put(id, tmp);
		return id;
	}
	
	public String AddMessage(String message, String id) {
		//se o utilizador existir, regista a mensagem
		if(this.presentIPs.containsKey(id)){
			Message msg = new Message(message, this.presentIPs.get(id).getName());
			this.messages.add(msg);
			
			return "message writen";
		}
		
		return "user not logged";
	}
	
	public String RemoveUser(String userId) {
		if(this.presentIPs.containsKey(userId)) {
			this.presentIPs.remove(userId);
			return "user removed";
		}
		
		return "user not found";
	}
	
	public String GetAllUsers() {
		this.curAns = "";
		//constroi a lista com todos os utilizadores para ser apresentada no browser
		presentIPs.forEach((k,v) ->{
			this.curAns += presentIPs.get(k).getTime() + ") " +  v.getType() + ": " +  presentIPs.get(k).getName() + "\r\n";
		});
		
		return this.curAns;
	}
	
	public String GetAllMessages() {
		this.curAns = "";
		//constroi a lista com todas as mensagens para ser apresentada no browser
		for(int i = 0; i!=this.messages.size(); i++)
			this.curAns += messages.get(i).GetString() + "\r\n";
		
		return this.curAns;
	}
}

class PInfo {
	
	private String name; //nome do utilizador
	private LocalTime time; //o momento do seu registo
	private String type; //se é aluno, professor, ou outro
	
	public PInfo(String name, String type) {
		this.name = name;
		this.time = LocalTime.now();
		this.type = type;
	}
	

	public String getName () {
		return this.name;
	}

	public String getType() {
		return this.type;
	}
	
	public String getTime(){
		//formatar o tempo para ficar no formato Horas:Minutos:Segundos
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		return time.format(formatter).toString();
	}
}



class Message {
	
	private String content; //conteudo da mensagem
	private LocalTime time; //hora da mensagem
	private String user; //utilizador que enviou a mensagem
	
	public Message(String content, String user) {
		this.content = content;
		this.time = LocalTime.now();
		this.user = user;
	}
	
	public String GetTime() {
		//formatar o tempo para ficar no formato Horas:Minutos:Segundos
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		return time.format(formatter).toString();
	}
	
	public String GetUser() {
		return user;
	}
	
	public String GetMessage() {
		return content;
	}
	
	public String GetString() {
		return GetTime() + ") " + user + ": " + content;
	}
}


