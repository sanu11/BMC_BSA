package com.bmc.cloud.bsasimulator.resources;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bmc.cloud.bsasimulator.internal.BSAFactory;
import com.bmc.cloud.bsasimulator.internal.BSASimulator;
import com.bmc.cloud.bsasimulator.internal.BSASimulatorImpl;
import com.bmc.cloud.bsasimulator.internal.Constants.AUTHENTICATOR_TYPE;
import com.bmc.cloud.bsasimulator.proccessor.BSAProcessor;
import com.bmc.cloud.bsasimulator.resthandler.Authenticator;

@Singleton
@Path("/resources")
public class PrimaryResource {
	@GET
	@Path("/VMdetails")
	@Produces("application/xml")
	public Response getNumberOfVms(@QueryParam("username") String username, @QueryParam("password") String password) {
		Authenticator authenticator = BSAFactory.getAuthenticator(AUTHENTICATOR_TYPE.DB);
		if (authenticator.authenticate(username, password)) {
			BSASimulator simulator = new BSASimulatorImpl();
			BSAProcessor processor = simulator.getBSAProcessor();
			return processor.process("/");
		}
		return Response.serverError().build();
	}

	/*
	 * Example of sub-resource locator.
	 *
	 *
	 *
	 */
	@Path("/osname/Windows")
	public Resource getOSName() {
		return new Resource();
	}

	@GET
	@Path("/NICCount")
	@Produces(MediaType.TEXT_PLAIN)
	public String getNICCount() {
		return "1";
	}

}