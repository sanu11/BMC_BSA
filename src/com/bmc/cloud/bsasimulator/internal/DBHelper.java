package com.bmc.cloud.bsasimulator.internal;

public interface DBHelper {

	boolean checkCredentials(String user,String password) throws ClassNotFoundException;

	void addTodb(String str);

	String getVmDetails();
	
}
