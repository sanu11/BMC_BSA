package com.bmc.cloud.bsasimulator.response;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.bmc.cloud.bsasimulator.internal.Constants.*;
import static com.bmc.cloud.bsasimulator.internal.Constants.STD_ERROR_FILE;

/**
 * Created by pratshin on 09-12-2016.
 */
public class ResponseGeneratorImpl implements ResponseGenerator {
    @Override
    public Response generate(String DirectoryPath , String fileName) {

        System.out.println("Rest Generator ");
        System.out.println("File name is "+fileName);

        File ParametersToBeReplacedFile = new File(RESPONSE_PARAMETERS_TO_BE_REPLACED_PATH + fileName +".txt");

        if(ParametersToBeReplacedFile.exists())
            System.out.println("ParametersToBeReplacedFile Exist");
        else
            System.out.println("ParametersToBeReplacedFile Does Not Exist");


        File ValuesFile = new File(RESPONSE_VALUES_PATH + fileName +".txt");
        if(ValuesFile.exists())
            System.out.println("ValuesFile Exist");
        else
            System.out.println("ValuesFile Does Not Exist");

        // create list
        List <String> ParametersToBeReplacedList = new ArrayList<String>();
        List <String> ValuesList = new ArrayList<String>();
        String word;

        // Read ParametersToBeReplacedList
        try (
                InputStream fis = new FileInputStream(ParametersToBeReplacedFile);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr);
        ) {
            while ((word = br.readLine()) != null) {
                // Do your thing with line
                ParametersToBeReplacedList.add(word);
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read ValuesList
        try (
                InputStream fis = new FileInputStream(ValuesFile);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr);
        ) {
            while ((word = br.readLine()) != null) {
                ValuesList.add("\""+word+"\"");
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create iterator to iterate on lists
        Iterator <String> ParametersToBeReplacedList_Iterator = ParametersToBeReplacedList.iterator();
        Iterator <String> ValuesList_Iterator = ValuesList.iterator();

        // create a velocityEngine and start
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();

        // create a template
        Template template = velocityEngine.getTemplate(RESPONSE_TEMPLATE_PATH + fileName + ".vm");

        // create a context
        VelocityContext context = new VelocityContext();

        // replace the variables
        while(ParametersToBeReplacedList_Iterator.hasNext() && ValuesList_Iterator.hasNext())
        {
            context.put(ParametersToBeReplacedList_Iterator.next(),ValuesList_Iterator.next());
        }

        // writing updated context to a file
        PrintWriter out=null;
        File file=new File(RESPONSE_TEMPLATE_PATH +"response.xml");
        try {
            out=new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        template.merge(context,out);
        out.close();


    //  Create a XML Response
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
        //Entity<Document> entity=new Entity<Document>(document);
        Response response=Response.ok(document,MediaType.TEXT_XML).build();
        return response;
    }
    public Response generate(String DirectoryPath , String fileName,String SoapCommandType) {

        if(SoapCommandType==null||fileName.equals("getVirtualGuestPackage"))
        {
            Document document=null;
            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder=factory.newDocumentBuilder();

                if(fileName.equals("getVirtualGuestPackage"))
                    document=builder.parse(DirectoryPath+fileName+".txt");
                else
                    document=builder.parse(DirectoryPath+fileName);


            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("returning response file ");
            //Entity<Document> entity=new Entity<Document>(document);
            Response response=Response.ok(document,MediaType.TEXT_XML).encoding("UTF-8").build();
            return response;
        }

        System.out.println("Soap Generator");
        System.out.println("File name is "+fileName);

        System.out.println("Soap command type is "+SoapCommandType+".txt");
        File ParametersToBeReplacedFile = new File(RESPONSE_PARAMETERS_TO_BE_REPLACED_PATH + SoapCommandType + ".txt");


        if(ParametersToBeReplacedFile.exists())
            System.out.println("ParametersToBeReplacedFile Exist");
        else
            System.out.println("ParametersToBeReplacedFile Does Not Exist");


        File ValuesFile = new File(RESPONSE_VALUES_PATH + fileName +".txt");
        if(ValuesFile.exists())
            System.out.println("ValuesFile Exist");
        else
            System.out.println("ValuesFile Does Not Exist");

        // create list
        List <String> ParametersToBeReplacedList = new ArrayList<String>();
        List <String> ValuesList = new ArrayList<String>();
        String word;

        // Read ParametersToBeReplacedList
        try (
                InputStream fis = new FileInputStream(ParametersToBeReplacedFile);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr);
        ) {
            while ((word = br.readLine()) != null) {
                // Do your thing with line

                ParametersToBeReplacedList.add(word);
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read ValuesList
        try (
                InputStream fis = new FileInputStream(ValuesFile);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr);
        ) {
            while ((word = br.readLine()) != null) {
                ValuesList.add(word);
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create iterator to iterate on lists
        Iterator <String> ParametersToBeReplacedList_Iterator = ParametersToBeReplacedList.iterator();
        Iterator <String> ValuesList_Iterator = ValuesList.iterator();

        // create a velocityEngine and start
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();

        // create a template
        Template template = null;
        template = velocityEngine.getTemplate(RESPONSE_TEMPLATE_PATH +SoapCommandType + ".vm");

        // create a context
        VelocityContext context = new VelocityContext();

        // replace the variables
        while(ParametersToBeReplacedList_Iterator.hasNext() && ValuesList_Iterator.hasNext())
        {
            context.put(ParametersToBeReplacedList_Iterator.next(),ValuesList_Iterator.next());
        }

        // writing updated context to a file
        PrintWriter out=null;
        File file=new File(RESPONSE_TEMPLATE_PATH +"response.xml");
        try {
            out=new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        template.merge(context,out);
        out.close();


        //  Create a XML Response
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
        //Entity<Document> entity=new Entity<Document>(document);
        Response response=Response.ok(document,MediaType.TEXT_XML).build();
        return response;
    }
}
