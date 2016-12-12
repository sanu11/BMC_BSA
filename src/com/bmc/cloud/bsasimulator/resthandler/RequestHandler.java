package com.bmc.cloud.bsasimulator.resthandler;

import java.io.IOException;
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