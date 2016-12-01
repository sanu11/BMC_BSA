package com.bmc.cloud.bsasimulator.internal;

import com.bmc.cloud.bsasimulator.proccessor.BSAProcessor;
import com.bmc.cloud.bsasimulator.response.ResponseGenerator;
import com.bmc.cloud.bsasimulator.resthandler.Authenticator;
import com.bmc.cloud.bsasimulator.resthandler.RestServer;

public interface BSASimulator {
	
	RestServer getRestServer(); //returns instance of RestServer for listening.
	Authenticator getAuthenticator(); //authenticate credentials.
	BSAProcessor getBSAProcessor();  //for processing queries.
	ResponseGenerator getResponseGenerator();	
	DBHelper getDBHelper();
}
