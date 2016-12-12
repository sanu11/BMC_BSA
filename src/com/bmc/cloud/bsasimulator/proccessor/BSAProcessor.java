package com.bmc.cloud.bsasimulator.proccessor;

import javax.ws.rs.core.Response;

public interface BSAProcessor {

	Response process(String username,String password,String path);
	//Response process(String path,String assetParam);
	
}
