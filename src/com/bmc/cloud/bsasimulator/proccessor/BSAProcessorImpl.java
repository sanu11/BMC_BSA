package com.bmc.cloud.bsasimulator.proccessor;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.bmc.cloud.bsasimulator.internal.BSASimulator;
import com.bmc.cloud.bsasimulator.internal.BSASimulatorImpl;
import com.bmc.cloud.bsasimulator.internal.DBHelper;
import com.bmc.cloud.bsasimulator.resources.VMDetails;

public class BSAProcessorImpl implements BSAProcessor {

	BSASimulator simulator=null;
	String response=null;
	@Override
	public Response process(String path) {
		//code for db logic.
		
		//Temporarily creating direct POJO.
		VMDetails details=new VMDetails("BMC_VM","LINUX",1,8,320);
		Response response=Response.ok(details,MediaType.APPLICATION_XML).build();
		return response;
	}
}


