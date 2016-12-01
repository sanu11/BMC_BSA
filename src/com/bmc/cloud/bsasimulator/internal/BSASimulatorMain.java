package com.bmc.cloud.bsasimulator.internal;
import com.bmc.cloud.bsasimulator.resthandler.RestServer;

public class BSASimulatorMain {
	public static void main(String[] args) {
		new BSASimulatorMain().initialize();
	}

	private void initialize() {

		//interface for getting all objects
		BSASimulator simulator=new BSASimulatorImpl();

		RestServer server=simulator.getRestServer();
		//		BSAProccessor processor = simulator.getBSAProcesser();
		//ResponseGenerator generator = simulator.getResponseGenerator();

		//server.startServer();
		//server.stopServer();

	}
}
