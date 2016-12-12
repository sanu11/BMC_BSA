package com.bmc.cloud.bsasimulator.resthandler;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import com.bmc.cloud.bsasimulator.resources.PrimaryResource;
import com.bmc.cloud.bsasimulator.resources.Resource;
import com.sun.net.httpserver.HttpServer;

import static com.bmc.cloud.bsasimulator.internal.Constants.PORT_NUMBER;

public class HttpRestServer implements RestServer{
	final HttpServer server;

	public HttpRestServer() throws IOException {
		URI builder=UriBuilder.fromUri("http://localhost/").port(PORT_NUMBER).build();
		Set<Class<?>> set=new HashSet<>();
		set.add(PrimaryResource.class);
		set.add(Resource.class);
		ResourceConfig config=new ResourceConfig(set);
		server=JdkHttpServerFactory.createHttpServer(builder,config);
		System.out.println("Started Http Server...");
	}

	public boolean startServer() {
		return true;
	}

	public boolean stopServer() {
		server.stop(5);
		return true;
	}
}
