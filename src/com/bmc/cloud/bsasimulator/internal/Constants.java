package com.bmc.cloud.bsasimulator.internal;

public class Constants {
	
	//Simulator constants.
	public enum REST_SERVER_TYPE{SIMPLE_HTTTP_SERVER,JETTY_SERVER};
	public enum AUTHENTICATOR_TYPE{DB,AD_PROD};
	
	//User credentials
	public static final String USERNAME="bmc";
	public static final String PASSWORD="bladelogic";
	
	//Database constants.
	public enum DB_TYPE {SQLITE,SQL};
	public static final  String USER_DB_NAME="users";
	public static final String USER_DB_PATH="jdbc:sqlite:C:\\Users\\pratshin\\workspace\\Simulator\\users.db";
	public static final String JDBC_CONNECTOR_NAME="org.sqlite.JDBC";
}