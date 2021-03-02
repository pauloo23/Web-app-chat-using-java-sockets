package server;

import java.net.*;
import java.io.*;
import java.util.*;

public class AudienciasRequestHandler extends Thread {
	Socket ligacao;
	Audiencias audiencia;
	BufferedReader in;
	

	public AudienciasRequestHandler(Socket ligacao, Audiencias audiencia) throws IOException {
		this.ligacao = ligacao;
		this.audiencia = audiencia;
		try{	
			this.in = new BufferedReader (new InputStreamReader(ligacao.getInputStream()));
		} 
		catch (IOException e) {
			System.out.println("Erro na execucao do servidor: " + e);
			System.exit(1);
		}
	}
	
	public void run() {                
		try {
			System.out.println("Aceitou ligacao de cliente no endereco " + ligacao.getInetAddress() + " na porta " + ligacao.getPort());
			
			String httpResponse="";
			String msg = in.readLine();
			System.out.println("Request=" + msg);
			
			StringTokenizer tokens = new StringTokenizer(msg);
			String metodo = tokens.nextToken();
			
			if (metodo.equals("GET")) {
				//executa um get e faz as operações necessarias
				String ansGet = AnswerGet(tokens.nextToken());
				//consuante falhe ou nao, envia uma mensagem para o browser
				if(ansGet.equals("Failed"))
					httpResponse = FailAnswer();
				else
					httpResponse = SucessAnswer(ansGet);

				ligacao.getOutputStream().write(httpResponse.getBytes("UTF-8"));
				System.out.println(httpResponse);
			}
			else if(metodo.equals("POST")) {
				//executa um post e faz as operaçoes necessarias
				String ansPost = AnswerPost(tokens.nextToken());
				//consuante falhe ou nao, envia uma mensagem para o browser
				if(ansPost.equals("Failed"))
					httpResponse = FailAnswer();
				else
					httpResponse = SucessAnswer(ansPost);

				ligacao.getOutputStream().write(httpResponse.getBytes("UTF-8"));
				System.out.println(httpResponse);
			}
			else 
				System.out.println("201;method not found");
				
			in.close();
			ligacao.close();
		} catch (IOException e) {
			System.out.println("Erro na execucao do servidor: " + e);
			System.exit(1);
		}
	}
	
	
	private String AnswerPost(String token) {
			//divide o pedido pelos caracters "_field", ate um maximo de 5 partes
			String[] stringParts = token.split("_field:",5);
			if(stringParts[0].equals("/newUser")) //registar um utilizador
				return this.audiencia.AddPresence(stringParts[1], stringParts[2]);
			else if (stringParts[0].equals("/newMessage")) //adicionar uma mensagem
				return this.audiencia.AddMessage(stringParts[1], stringParts[2]);
			else if(stringParts[0].equals("/logOut"))
				return this.audiencia.RemoveUser(stringParts[1]);
			else 
				return "Failed";
	}
	
	private String AnswerGet(String token) {
		if(token.contains("getUsers")) //consulta todos os utilizadores
			return this.audiencia.GetAllUsers();
		else if(token.contains("getMessages")) //consulta todas as mensagem
			return this.audiencia.GetAllMessages();
		else
			return "Failed";
	}
	
	private String SucessAnswer(String ans) {
		//mensagem de sucesso
		String response ="HTTP/1.1 200 OK\r\n";
		response += "Content-Length: " + ans.length() + "\r\n";
		response += "Content-type: text/html";
		response += "\r\nAccess-Control-Allow-Origin: *\r\n\r\n";
		response += ans;
		
		return response;
	}
	
	private String FailAnswer() {
		//mensagem em caso de falha
		String response ="HTTP/1.1 400 ERROR\r\n";
		response += "Content-type: application/json \\r\\n";
		response += "Access-Control-Allow-Origin: *\\r\\n\\r\\n";
		
		return response;
	}
}