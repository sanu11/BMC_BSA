package com.bmc.cloud.bsasimulator.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class Resource {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt(){
		return "Windows x86_64";
	}
}
