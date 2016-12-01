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
		
		//Used JAX-RS annotations instead.
		//Moved db handling to SQLiteDBHelper.
		
	}
}