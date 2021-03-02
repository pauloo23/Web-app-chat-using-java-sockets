package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server {

	static int DEFAULT_PORT=8081;
	
    public static void main(String[] args) throws Exception {
    	int port;
    	if(args.length>0)
    		port=Integer.parseInt(args[0]);
    	else 
    		port = DEFAULT_PORT;
    	
    	Audiencias audiencia = new Audiencias(); 
        ServerSocket server  = new ServerSocket(port);
        
        System.out.println("Listening for connection on port :" + port + " ....");
        //init server
        while (true) {
            Socket sck=null;
            try {
            	sck = server.accept();
            	//Thread
            	AudienciasRequestHandler t = new AudienciasRequestHandler(sck, audiencia);
				t.start();
            }catch(IOException E){
            	System.out.println("Não foi possível criar socket Erro: " + E);
            	System.exit(1);
            }
        }
    }
}
