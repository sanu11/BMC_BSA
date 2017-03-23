package com.bmc.cloud.bsasimulator.proccessor;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.bmc.cloud.bsasimulator.internal.BSAFactory;
import com.bmc.cloud.bsasimulator.internal.BSASimulator;
import com.bmc.cloud.bsasimulator.internal.BSASimulatorImpl;
import com.bmc.cloud.bsasimulator.internal.Constants;
import com.bmc.cloud.bsasimulator.resources.VMDetails;
import com.bmc.cloud.bsasimulator.response.ResponseGenerator;
import com.bmc.cloud.bsasimulator.resthandler.Authenticator;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

public class BSAProcessorImpl implements BSAProcessor {
	@Override
	public Response process(String username,String password,String path) {
        System.out.println(path);
        Authenticator authenticator = BSAFactory.getAuthenticator(Constants.AUTHENTICATOR_TYPE.DB);
        ResponseGenerator generator= BSAFactory.getResponseGenerator();
        if (authenticator.authenticate(username, password)) {
            // no use of REQUEST_TYPE.REST here
            Response response=generator.generate(path,"temp.xml");
            return  response;
        }
        System.out.println("error");
        return Response.serverError().build();
	}
}


