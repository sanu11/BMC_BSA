package com.bmc.cloud.bsasimulator.resthandler;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.bmc.cloud.bsasimulator.resources.PrimaryResource;
import com.bmc.cloud.bsasimulator.resources.Resource;
import com.sun.net.httpserver.HttpServer;

public class JerseyHttpRestServer {
	public static void main(String[] args) {
		URI builder=UriBuilder.fromUri("http://localhost/").port(10844).build();
		Set<Class<?>> set=new HashSet<>();
		set.add(PrimaryResource.class);
		set.add(Resource.class);
		 ResourceConfig config=new ResourceConfig(set);
	    HttpServer server=JdkHttpServerFactory.createHttpServer(builder, config);
	    
	}
}
