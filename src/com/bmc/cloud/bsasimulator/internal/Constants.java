package com.bmc.cloud.bsasimulator.internal;

public class Constants {
	
	//Simulator constants.
	public enum REST_SERVER_TYPE{SIMPLE_HTTTP_SERVER,JETTY_SERVER};
	public enum AUTHENTICATOR_TYPE{DB,AD_PROD};
	public static final int PORT_NUMBER=10844;
	//User credentials
	public static final String USERNAME="BLAdmin";
	public static final String PASSWORD="bladelogic";
	
	//Database constants.
	public enum DB_TYPE {SQLITE,SQL};
	public static final  String USER_DB_NAME="users";
	public static final String USER_DB_PATH="jdbc:sqlite:users.db";
	public static final String JDBC_CONNECTOR_NAME="org.sqlite.JDBC";

	//Path constants.
	public static final String RESPONSE_PATH="\\Responses\\";
	public static final String VGP_URL="group/Depot/CSM_Virtual_Guest_Packages/";
	public static final String COMMON_PREFIX="/id/SystemObject/Server/";
	public static final String BMC_VMware_VirtualInfrastructureManager="BMC_VMware_VirtualInfrastructureManager/87000000/Virtual+Machines/pratik-1/";
	public static final String STD_ERROR_FILE="stderror.xml";
	public static final String QUERY_PARAM="?username=BLAdmin&password=bladelogic&authType=SRP&role=BLAdmins&version=8.2";
}