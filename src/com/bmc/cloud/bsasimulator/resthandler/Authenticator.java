package com.bmc.cloud.bsasimulator.resthandler;

public interface Authenticator {
	boolean authenticate(String user,String password);
	
}
