package com.bmc.cloud.bsasimulator.resthandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import com.bmc.cloud.bsasimulator.internal.BSAFactory;
import com.bmc.cloud.bsasimulator.internal.BSASimulator;
import com.bmc.cloud.bsasimulator.internal.BSASimulatorImpl;
import com.bmc.cloud.bsasimulator.proccessor.BSAProcessor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RequestHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange arg0) throws IOException {
		// TODO Auto-generated method stub
		
		BSASimulator simulator=new BSASimulatorImpl();
		Authenticator authenticator =  simulator.getAuthenticator();
		BSAProcessor processor = simulator.getBSAProcessor();

		URI uri = arg0.getRequestURI();
		String path = uri.getPath();
		String credentials = uri.getQuery();

		String auth[] = credentials.split("&");
		String user[] = auth[0].split("=");
		String pass[] = auth[1].split("=");
		
		boolean valid = authenticator.authenticate(user[1], pass[1]);
		String response=null;
		if(valid){
			System.out.println("Authentication Successful");
		   // response = processor.process(path);
		}
		else{
			System.out.println("Authentication Unsuccessful");
			response = "Authentication Failed";
	}
		arg0.sendResponseHeaders(200, response.length());
		OutputStream out = arg0.getResponseBody();
		out.write(response.getBytes());
		
	}
}