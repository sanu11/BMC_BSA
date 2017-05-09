package com.bmc.cloud.bsasimulator.resthandler;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.UriBuilder;

import com.bmc.cloud.bsasimulator.resources.RecordMode;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import com.bmc.cloud.bsasimulator.resources.PrimaryResource;
import com.bmc.cloud.bsasimulator.resources.Resource;
import static com.bmc.cloud.bsasimulator.internal.Constants.PORT_NUMBER;

public class HttpRestServer implements RestServer{
	 com.sun.net.httpserver.HttpServer server;

	public HttpRestServer() throws IOException, NoSuchAlgorithmException, KeyManagementException {
		URI builder=UriBuilder.fromUri("http://localhost/").port(PORT_NUMBER).build();
		Set<Class<?>> set=new HashSet<>();
		set.add(RecordMode.class);
		set.add(Resource.class);
		ResourceConfig config=new ResourceConfig(set);
		server= JdkHttpServerFactory.createHttpServer(builder,config);
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
