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

public class HttpRestServer implements RestServer{
    final HttpServer server;
	
	public HttpRestServer() throws IOException {
		URI builder=UriBuilder.fromUri("http://localhost/").port(9000).build();
		Set<Class<?>> set=new HashSet<>();
		set.add(PrimaryResource.class);
		set.add(Resource.class);
		 ResourceConfig config=new ResourceConfig(set);
		server=JdkHttpServerFactory.createHttpServer(builder,config);
		//server.stop(5);
		// TODO Auto-generated constructor stub
	}
	
	public boolean startServer() {
		System.out.println("in server");
		//server.start();
		return true;
	}
	
	public boolean stopServer() {
		server.stop(5);
		return true;
	}
}
