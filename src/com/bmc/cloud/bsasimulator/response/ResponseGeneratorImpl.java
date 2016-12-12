package com.bmc.cloud.bsasimulator.response;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static com.bmc.cloud.bsasimulator.internal.Constants.STD_ERROR_FILE;

/**
 * Created by pratshin on 09-12-2016.
 */
public class ResponseGeneratorImpl implements ResponseGenerator {
    @Override
    public Response generate(String filepath) {

        File file=new File(filepath).exists()?new File(filepath):new File(STD_ERROR_FILE);
        Document document=null;
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder=factory.newDocumentBuilder();
            document=builder.parse(file);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Response response=Response.ok(document, MediaType.APPLICATION_XML).build();
        return response;
    }
}
