package com.bmc.cloud.bsasimulator.internal;

import java.io.IOException;

import com.bmc.cloud.bsasimulator.internal.Constants.AUTHENTICATOR_TYPE;
import com.bmc.cloud.bsasimulator.internal.Constants.DB_TYPE;
import com.bmc.cloud.bsasimulator.internal.Constants.REST_SERVER_TYPE;
import com.bmc.cloud.bsasimulator.resthandler.AuthenticateFromDb;
import com.bmc.cloud.bsasimulator.resthandler.Authenticator;
import com.bmc.cloud.bsasimulator.resthandler.HttpRestServer;
import com.bmc.cloud.bsasimulator.resthandler.RestServer;

public class BSAFactory {
	
	public static RestServer getRestServer(REST_SERVER_TYPE type) throws IOException{
		RestServer server=null;
		if(type.equals(REST_SERVER_TYPE.SIMPLE_HTTTP_SERVER)){
			server=new HttpRestServer();
		}

		return server;
	}
	public static Authenticator getAuthenticator(AUTHENTICATOR_TYPE type){
		Authenticator authenticate=null;
		if(type.equals(AUTHENTICATOR_TYPE.DB)){
			authenticate= new AuthenticateFromDb();
		}

		return authenticate;
	}
	public static DBHelper getDBHelper(DB_TYPE type){
		DBHelper helper = null;
		if(type.equals(DB_TYPE.SQLITE)){
			helper = new SqliteDBHelper();
		}
		return helper;
	}
}
