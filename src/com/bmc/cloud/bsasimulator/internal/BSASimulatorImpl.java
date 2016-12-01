package com.bmc.cloud.bsasimulator.internal;

import java.io.IOException;

import com.bmc.cloud.bsasimulator.internal.Constants.AUTHENTICATOR_TYPE;
import com.bmc.cloud.bsasimulator.internal.Constants.DB_TYPE;
import com.bmc.cloud.bsasimulator.internal.Constants.REST_SERVER_TYPE;
import com.bmc.cloud.bsasimulator.proccessor.BSAProcessor;
import com.bmc.cloud.bsasimulator.proccessor.BSAProcessorImpl;
import com.bmc.cloud.bsasimulator.response.ResponseGenerator;
import com.bmc.cloud.bsasimulator.resthandler.Authenticator;
import com.bmc.cloud.bsasimulator.resthandler.RestServer;


//type of all objects given here . 
public class BSASimulatorImpl implements BSASimulator{

	@Override
	public RestServer getRestServer() {
		// TODO Auto-generated method stub
		RestServer server=null;
		try {
			server= BSAFactory.getRestServer(REST_SERVER_TYPE.SIMPLE_HTTTP_SERVER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return server;
	}
	

	@Override
	public BSAProcessor getBSAProcessor() {
		// TODO Auto-generated method stub
		return new BSAProcessorImpl();
	
	}

	@Override
	public ResponseGenerator getResponseGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Authenticator getAuthenticator() {
		// TODO Auto-generated method stub
		Authenticator authenticator = null;
		authenticator = BSAFactory.getAuthenticator(AUTHENTICATOR_TYPE.DB);
		return authenticator;
	}


	@Override
	public DBHelper getDBHelper() {
		// TODO Auto-generated method stub
		DBHelper helper=null;
		helper = BSAFactory.getDBHelper(DB_TYPE.SQLITE);
		return helper;
	}

}
