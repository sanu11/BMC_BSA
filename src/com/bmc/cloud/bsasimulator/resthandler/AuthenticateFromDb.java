package com.bmc.cloud.bsasimulator.resthandler;
import com.bmc.cloud.bsasimulator.internal.BSASimulator;
import com.bmc.cloud.bsasimulator.internal.BSASimulatorImpl;
import com.bmc.cloud.bsasimulator.internal.DBHelper;

public class AuthenticateFromDb  implements Authenticator {

	@Override
	public boolean authenticate(String user, String password) {
		// TODO Auto-generated method stub
		
		BSASimulator simulator=new BSASimulatorImpl();
		DBHelper helper = simulator.getDBHelper();
		boolean res=false;
		try {
			res = helper.checkCredentials(user, password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
}
