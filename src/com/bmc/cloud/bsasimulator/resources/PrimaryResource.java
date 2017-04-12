package com.bmc.cloud.bsasimulator.resources;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.bmc.cloud.bsasimulator.internal.BSAFactory;
import com.bmc.cloud.bsasimulator.proccessor.BSAProcessor;
import com.bmc.cloud.bsasimulator.response.ResponseGenerator;
import com.bmc.cloud.bsasimulator.response.ResponseGeneratorImpl;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Random;

import static com.bmc.cloud.bsasimulator.internal.Constants.*;

@Path("/")
public class PrimaryResource {
    @QueryParam("username")
    String username;
    @QueryParam("password")
    String password;
    static BSAProcessor processor= BSAFactory.getBSAProccesser();
    static int count=1;
    @Context
    UriInfo uriInfo;
    final static Logger logger=Logger.getLogger(com.bmc.cloud.bsasimulator.resources.Resource.class);

    @GET
    @Path("services/LoginService")
    @Produces("text/xml")
    public Response getLoginServiceWSDL(@QueryParam("wsdl") String wsdl) {
        System.out.println(uriInfo.getAbsolutePath());
        System.out.println("getLoginServiceWSDL");
        String filepath="LoginService.wsdl";
        ResponseGenerator generator=BSAFactory.getResponseGenerator();
        Response response=generator.generate(RESPONSE_TEMPLATE_PATH,filepath,null);
        return response;
    }

    @GET
    @Path("services/CLITunnelService")
    @Produces("text/xml")
    public  Response getCLIWsdl(){
        System.out.println(uriInfo.getAbsolutePath());
        System.out.println("getCLIWsdl");
        String filepath="CLITunnelService.wsdl";
        ResponseGenerator generator=BSAFactory.getResponseGenerator();
        Response response=generator.generate(RESPONSE_TEMPLATE_PATH,filepath,null);
        return response;
    }
    @GET
    @Path("services/AssumeRoleService")
    @Produces("text/xml")
    public  Response getAssumeRoleWsdl(){
        System.out.println(uriInfo.getAbsolutePath());
        System.out.println("getAssumeRoleWsdl");
        String filepath="AssumeRoleService.wsdl";
        ResponseGenerator generator=BSAFactory.getResponseGenerator();
        Response response=generator.generate(RESPONSE_TEMPLATE_PATH,filepath,null);
        return response;
    }
    @POST
    @Path("services/LoginService")
    @Produces("text/xml")
    public Response loginService(InputStream stream){
        System.out.println(uriInfo.getAbsolutePath());
        System.out.println("loginService");
        String fileName="loginService"+(count++)+".xml";
        if (count==15){
            count=1;
        }
        ResponseGenerator generator=BSAFactory.getResponseGenerator();
        Response response=generator.generate(RESPONSE_TEMPLATE_PATH,fileName,null);
        return response;
    }

    @POST
    @Path("services/AssumeRoleService")
    @Produces("text/xml")
    public Response assumeRoleService(InputStream stream){
        System.out.println(uriInfo.getAbsolutePath());
        System.out.println("assumeRoleService");
        String file="assume.xml";
        ResponseGenerator generator=BSAFactory.getResponseGenerator();
        Response response=generator.generate(RESPONSE_TEMPLATE_PATH,file,null);
        return response;
    }

    @POST
    @Path("services/CLITunnelService")
    @Produces("text/xml")
    public Response CLITunnelService(InputStream stream) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        System.out.println("Into ClitTunnel Service");
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder=factory.newDocumentBuilder();
        Document document=builder.parse(stream);
        document.getDocumentElement().normalize();
        System.out.println("hello");
        System.out.println(document.getNodeName());
        OutputStream outputStream=new PrintStream(System.out);
       //printDocument(document,outputStream);
        NodeList list=document.getElementsByTagName("ns4:commandName");
        Node node=list.item(0);
        System.out.println("Command name is "+node.getTextContent());
       /* NodeList body=(document.getElementsByTagName("S:Body")).item(0).getChildNodes();
        String CommandType=null;
        for(int i=0;i<body.getLength();i++)
        {
            if(body.item(i).getNodeType()==Node.ELEMENT_NODE)
            {
                CommandType= body.item(i).getNodeName();
                break;
            }
        }*/
       // Node command=document.getElementById("ns4:executeCommandByParamList");
        //System.out.println("Command type is "+CommandType);
        ResponseGenerator generator = BSAFactory.getResponseGenerator();
        //CommandType=CommandType.substring(CommandType.indexOf(':')+1);
        String CommandType="executeCommandByParamList";
        if(node.getTextContent().equals("createVirtualGuest") || node.getTextContent().equals("exportNSHScriptRun") ){
            CommandType="executeCommandByParamListAndAttachment";
        }
        Response response = generator.generate(RESPONSE_TEMPLATE_PATH,node.getTextContent(),CommandType);
        return response;
    }

    public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(new DOMSource(doc),
                new StreamResult(new OutputStreamWriter(out, "UTF-8")));
    }
    @POST
    @Path("/{default : .*}")
    @Produces("text/xml")
    public Response soapDefaultPath(InputStream stream){
        System.out.println("Default fun \n"+uriInfo.getAbsolutePath());
        System.out.println("soapDefaultPath");
        String filepath="loginService.xml";
        ResponseGenerator generator=BSAFactory.getResponseGenerator();
        Response response=generator.generate(RESPONSE_TEMPLATE_PATH,filepath,null);
        return response;

    }
    @GET
    @Path("group/Depot/CSM_Virtual_Guest_Packages/{VGP_Name}")
    @Produces("text/xml")
    public Response getVGP(@PathParam("VGP_Name") String VGP_Name) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("group/Depot/CSM_Virtual_Guest_Packages/{VGP_Name}");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH+ "-group-Depot-CSM_Virtual_Guest_Packages-VGP_Name.vm");

        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH +"-group-Depot-CSM_Virtual_Guest_Packages-VGP_Name.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-group-Depot-CSM_Virtual_Guest_Packages-VGP_Name");
    }


    @GET
    @Path(COMMON_PREFIX+"{guid}/Assets")
    @Produces("text/xml")
    public Response getGUIDAssetsResponse(@PathParam("guid") String guid) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println(COMMON_PREFIX+"{guid}/Assets\nWithout slash");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets.vm");

        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Server-guid-Assets");
    }
    @GET
    @Path(COMMON_PREFIX+"{guid}")
    @Produces("text/xml")
    public Response getGUIDResponse(@PathParam("guid") String guid) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println(COMMON_PREFIX+"{guid}");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid.vm");

        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Server-guid");
    }

    @GET
    @Path("/type/PropertySetClasses/SystemObject/Server/")
    @Produces("text/xml")
    public Response getTypePropertySetClassSysObjServer() throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("/type/PropertySetClasses/SystemObject/Server/");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-type-PropertySetClasses-SystemObject-Server-.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-type-PropertySetClasses-SystemObject-Server-.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-type-PropertySetClasses-SystemObject-Server-");
    }
    @GET
    @Path("/type/AssetClasses/")
    @Produces("text/xml")
    public Response getAssetClasses() throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("/type/Assets/");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-type-AssetClasses-.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-type-AssetClasses-.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-type-AssetClasses-");
    }

    @GET
    @Path("/type/PropertySetClasses/SystemObject/Job/Virtual+Guest+Job/")
    @Produces("text/xml")
    public Response getVirtualGuestJobPolling(@PathParam("guid") String guid) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("/type/PropertySetClasses/SystemObject/Job/Virtual+Guest+Job/");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-type-PropertySetClasses-SystemObject-Job-Virtual+Guest+Job-.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-type-PropertySetClasses-SystemObject-Job-Virtual+Guest+Job-.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-type-PropertySetClasses-SystemObject-Job-Virtual+Guest+Job-");
    }


    @GET
    @Path("/id/SystemObject/Job/Virtual+Guest+Job/{ObjId}/Statuses")
    @Produces("text/xml")
    public Response getVirtualGuestJobObjIdStatuses(@PathParam("ObjId") String ObjId) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("/id/SystemObject/Job/Virtual Guest Job/{ObjId}/Statuses");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Job-Virtual+Guest+Job-ObjId-Statuses.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Job-Virtual+Guest+Job-ObjId-Statuses.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Job-Virtual+Guest+Job-ObjId-Statuses");
    }
    @GET
    @Path("/id/SystemObject/Job/Virtual+Guest+Job/{ObjId}/Statuses/{StatusName}")
    @Produces("text/xml")
    public Response getVirtualGuestJobObjIdStatuses(@PathParam("ObjId") String ObjId,@PathParam("StatusName") String StatusName) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("/id/SystemObject/Job/Virtual Guest Job/{ObjId}/Statuses/{StatusName}");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Job-Virtual+Guest+Job-ObjId-Statuses-StatusName.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Job-Virtual+Guest+Job-ObjId-Statuses-StatusName.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Job-Virtual+Guest+Job-ObjId-Statuses-StatusName");
    }
    @GET
    @Path("type/PropertySetClasses/SystemObject/Job/Update+Server+Properties+Job/")
    @Produces("text/xml")
    public Response getSysObjUpdateServerPropertiesJob() throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("type/PropertySetClasses/SystemObject/Job/Update+Server+Properties+Job/");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-type-PropertySetClasses-SystemObject-Job-Update+Server+Properties+Job-.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-type-PropertySetClasses-SystemObject-Job-Update+Server+Properties+Job-.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-type-PropertySetClasses-SystemObject-Job-Update+Server+Properties+Job-");
    }

    @GET
    @Path("id/SystemObject/Job/Update+Server+Properties+Job/{ObjId}/Statuses")
    @Produces("text/xml")
    public Response getGUIDUpdateServerPropertiesJobStatuses(@PathParam("ObjId") String ObjId) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("id/SystemObject/Job/Update+Server+Properties+Job/ObjId/Statuses");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Job-Update+Server+Properties+Job-ObjId-Statuses.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Job-Update+Server+Properties+Job-ObjId-Statuses.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Job-Update+Server+Properties+Job-ObjId-Statuses");
    }
    @GET
    @Path("/id/SystemObject/Server/{guid}/Assets/{Asset_URI}/{Version}/{Machine}/{ServerName}/Server+Properties/")
    @Produces("text/xml")
    public Response getguidUuid(@PathParam("guid") String guid,@PathParam("Asset_URI") String Asset_URI,@PathParam("Version") String Version,
                            @PathParam("Machine") String Machine,@PathParam("ServerName") String ServerName) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("-id-SystemObject-Server-guid-Assets-Asset_URI-Version-Machine-ServerName-Server+Properties-");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-Asset_URI-Version-Machine-ServerName-Server+Properties-.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-Asset_URI-Version-Machine-ServerName-Server+Properties-.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Server-guid-Assets-Asset_URI-Version-Machine-ServerName-Server+Properties-");
    }

    @GET
    @Path("/type/PropertySetClasses/SystemObject/Job/Compliance+Job/")
    @Produces("text/xml")
    public Response getSysObjComplianceJob() throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("-type-PropertySetClasses-SystemObject-Job-Compliance+Job-");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-type-PropertySetClasses-SystemObject-Job-Compliance+Job-.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-type-PropertySetClasses-SystemObject-Job-Compliance+Job-.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-type-PropertySetClasses-SystemObject-Job-Compliance+Job-");
    }
    @GET
    @Path("/id/SystemObject/Server/{guid}/Assets/{Asset_URI}/{Version}/{Machine}/{ServerName}")
    @Produces("text/xml")
    public Response getguidInternalName(@PathParam("guid") String guid,@PathParam("Asset_URI") String Asset_URI,@PathParam("Version") String Version,
                            @PathParam("Machine") String Machine,@PathParam("ServerName") String ServerName) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("-id-SystemObject-Server-guid-Assets-Asset_URI-Version-Machine-ServerName");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-Asset_URI-Version-Machine-ServerName.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-Asset_URI-Version-Machine-ServerName.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Server-guid-Assets-Asset_URI-Version-Machine-ServerName");
    }

    @GET
    @Path("/id/SystemObject/Server/{guid}/Assets/SystemInfo/{Version}/System/AssetAttributeValues/Serial+Number")
    @Produces("text/xml")
    public Response getSerialNumber(@PathParam("guid") String guid,@PathParam("Version") String Version) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("/id/SystemObject/Server/{guid}/Assets/SystemInfo/{Version}/System/AssetAttributeValues/Serial+Number");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-SystemInfo-Version-System-AssetAttributeValues-Serial+Number.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-SystemInfo-Version-System-AssetAttributeValues-Serial+Number.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Server-guid-Assets-SystemInfo-Version-System-AssetAttributeValues-Serial+Number");
    }
    @GET
    @Path("/type/PropertySetClasses/SystemObject/Job+Run/")
    @Produces("text/xml")
    public Response getSysObjJobRun() throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("/type/PropertySetClasses/SystemObject/Job+Run/");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-type-PropertySetClasses-SystemObject-Job+Run-.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-type-PropertySetClasses-SystemObject-Job+Run-.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-type-PropertySetClasses-SystemObject-Job+Run-");
    }



    @GET
    @Path("/id/SystemObject/Server/{guid}/Assets/{Asset_URI}/{Version}/{Machine}/{ServerName}/Hardware/Network+Adapters/")
    @Produces("text/xml")
    public Response getNetworkAdapter(@PathParam("guid") String guid,@PathParam("Asset_URI") String Asset_URI,@PathParam("Version") String Version,
                                      @PathParam("Machine") String Machine,@PathParam("ServerName") String ServerName) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("/id/SystemObject/Server/{guid}/Assets/{Asset_URI}/{Version}/{Machine}/{ServerName}/Hardware/Network+Adapters/");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-Asset_URI-Version-Machines-ServerName-Hardware-Network+Adapters-.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-Asset_URI-Version-Machines-ServerName-Hardware-Network+Adapters-.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Server-guid-Assets-Asset_URI-Version-Machines-ServerName-Hardware-Network+Adapters-");
    }

    @GET
    @Path("/id/SystemObject/Server/{guid}/Assets/{Asset_URI}/{Machines}/{ServerName}/Hardware/Disks/")
    @Produces("text/xml")
    public Response getDisks(@PathParam("guid") String guid,@PathParam("Asset_URI") String Asset_URI,
                                      @PathParam("Machines") String Machines,@PathParam("ServerName") String ServerName) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("/id/SystemObject/Server/{guid}/Assets/{Asset_URI}/{Machines}/{ServerName}/Hardware/Disks/");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-Asset_URI-Machines-ServerName-Hardware-Disks-");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-Asset_URI-Machines-ServerName-Hardware-Disks-.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Server-guid-Assets-Asset_URI-Machines-ServerName-Hardware-Disks-");
    }


    @GET
    @Path(COMMON_PREFIX+"{guid}/Assets/System+Info/NetworkAdapter")
    @Produces("text/xml")
    public Response getGUIDNetworkAdapterResponse(@PathParam("guid") String guid) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println(COMMON_PREFIX+"{guid}/Assets/System+Info/NetworkAdapter");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-System+Info-NetworkAdapter.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-System+Info-NetworkAdapter.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Server-guid-Assets-System+Info-NetworkAdapter");
    }

    @GET
    @Path(COMMON_PREFIX+"{guid}/Assets/System+Info/NetworkAdapter/0/")
    @Produces("text/xml")
    public Response getGUIDNetworkAdapterResponse0(@PathParam("guid") String guid) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println(COMMON_PREFIX+"{guid}/Assets/System+Info/NetworkAdapter/0/");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-System+Info-NetworkAdapter-0-.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-System+Info-NetworkAdapter-0-.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Server-guid-Assets-System+Info-NetworkAdapter-0-");
    }
    @GET
    @Path(COMMON_PREFIX+"{guid}/Assets/System+Info/NetworkAdapter/1/")
    @Produces("text/xml")
    public Response getGUIDNetworkAdapterResponse1(@PathParam("guid") String guid) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println(COMMON_PREFIX+"{guid}/Assets/System+Info/NetworkAdapter/1/");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-System+Info-NetworkAdapter-1-.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-System+Info-NetworkAdapter-1-.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Server-guid-Assets-System+Info-NetworkAdapter-1-");
    }
    @GET
    @Path("/id/SystemObject/Server/{guid}/Assets/System+Info/OpSys/")
    @Produces("text/xml")
    public Response getSystemInfoOpSys(@PathParam("guid") String guid) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("/id/SystemObject/Server/{guid}/Assets/System+Info/OpSys/");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-System+Info-OpSys-.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-System+Info-OpSys-.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Server-guid-Assets-System+Info-OpSys-");
    }
    @GET
    @Path("/type/PropertySetClasses/SystemObject/Job/NSH+Script+Job/")
    @Produces("text/xml")
    public Response getNSHScriptJob(@QueryParam("Name") String  Name) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("/type/PropertySetClasses/SystemObject/Job/NSH+Script+Job/ "+Name);

        String temp=uriInfo.getPath().toString();
        File UpdateFile = new File(RESPONSE_VALUES_PATH + "-type-PropertySetClasses-SystemObject-Job-NSH+Script+Job-.txt");

        if(UpdateFile.exists())
        {
            System.out.println("File to be updated exist");
            try {
                Files.write(Paths.get(RESPONSE_VALUES_PATH + "-type-PropertySetClasses-SystemObject-Job-NSH+Script+Job-.txt"), ("\n"+Name).getBytes(), StandardOpenOption.APPEND);
            }catch (IOException e) {
                System.out.println(e.toString());
                //exception handling left as an exercise for the reader
            }
        }
        else
        {
            System.out.println("File to be updated does not exist");
        }
        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-type-PropertySetClasses-SystemObject-Job-NSH+Script+Job-.vm");

        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-type-PropertySetClasses-SystemObject-Job-NSH+Script+Job-.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-type-PropertySetClasses-SystemObject-Job-NSH+Script+Job-");
    }
    @GET
    @Path("id/SystemObject/Job/NSH+Script+Job/{ObjId}/Statuses")
    @Produces("text/xml")
    public Response getGUIDNSHScriptJobStatus(@PathParam("ObjId") String ObjId) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("id/SystemObject/Job/NSH+Script+Job/{ObjId}/Statuses");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Job-NSH+Script+Job-ObjId-Statuses.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Job-NSH+Script+Job-ObjId-Statuses.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Job-NSH+Script+Job-ObjId-Statuses");
    }
    @GET
    @Path("id/SystemObject/Job/NSH+Script+Job/{ObjId}/Statuses/{StatusName}")
    @Produces("text/xml")
    public Response getGUIDNSHScriptJobStatusStatus3257(@PathParam("ObjId") String ObjId,@PathParam("StatusName") String StatusName) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("id/SystemObject/Job/NSH+Script+Job/{ObjId}/Statuses/{StatusName}");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Job-NSH+Script+Job-ObjId-Statuses-StatusName.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Job-NSH+Script+Job-ObjId-Statuses-StatusName.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Job-NSH+Script+Job-ObjId-Statuses-StatusName");
    }
    @GET
    @Path("id/SystemObject/Server/{guid}/Assets/Windows+User+List/{UserName}")
    @Produces("text/xml")
    public Response getWindowsUserListUsername(@PathParam("guid") String guid,@PathParam("UserName") String UserName) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("id/SystemObject/Server/{guid}/Assets/Windows+User+List/{UserName}");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-Windows+User+List-UserName.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-Windows+User+List-UserName.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Server-guid-Assets-Windows+User+List-UserName");
    }

    @GET
    @Path("id/SystemObject/Server/{guid}/Assets/{Asset_URI}/{Version}/{Machines}/{ServerName}/{GuestInfoType}")
    @Produces("text/xml")
    public Response getWindowsUserListUsername(@PathParam("guid") String guid,@PathParam("Asset_URI") String Asset_URI,@PathParam("Version") String Version,
                                               @PathParam("Machine") String Machine,@PathParam("ServerName") String ServerName,@PathParam("GuestInfoType") String GuestInfoType) throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println("id/SystemObject/Server/{guid}/Assets/{Asset_URI}/{Version}/{Machines}/{ServerName}/{GuestInfoType}");

        String temp=uriInfo.getPath().toString();

        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-Asset_URI-Version-Machines-ServerName-GuestInfoType.vm");


        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Server-guid-Assets-Asset_URI-Version-Machines-ServerName-GuestInfoType.vm");

        if(!currfile.exists()) {
            System.out.println("Template File Does Not Exist");
        }
        else
        {
            System.out.println("Template File Exist");
        }
        ResponseGenerator generator = new ResponseGeneratorImpl();
        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Server-guid-Assets-Asset_URI-Version-Machines-ServerName-GuestInfoType");
    }
    //    @GET
//    @Path("id/SystemObject/Job/Virtual+Guest+Job/{guid}/Statuses/Status3251")
//    @Produces("text/xml")
//    public Response getGUIDVirtualGuestJobStatusStatusesStatus3251(@PathParam("guid") String guid) throws IOException {
//        logger.debug(uriInfo.getAbsolutePath());
//
//        System.out.println("id/SystemObject/Job/Virtual+Guest+Job/{guid}/Statuses/Status3251");
//
//        String temp=uriInfo.getPath().toString();
//
//        File responsefile;
//        HttpResponse response;
//
//        File currfile=new File(RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Job-Virtual+Guest+Job-guid-Statuses-Status3251.vm");
//
//
//        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH + "-id-SystemObject-Job-Virtual+Guest+Job-guid-Statuses-Status3251.vm");
//
//        if(!currfile.exists()) {
//            System.out.println("Template File Does Not Exist");
//        }
//        else
//        {
//            System.out.println("Template File Exist");
//        }
//        ResponseGenerator generator = new ResponseGeneratorImpl();
//        return generator.generate(RESPONSE_TEMPLATE_PATH,"-id-SystemObject-Job-Virtual+Guest+Job-guid-Statuses-Status3251");
//    }
    @GET
    @Path("/{default : .*}")
    @Produces("text/xml")
    public Response restDefaultPath() throws IOException
    {
        logger.debug(uriInfo.getAbsolutePath());

        System.out.println(uriInfo.getAbsolutePath());
        System.out.println("defaultPath()");
        String temp=uriInfo.getPath().toString();
        File responsefile;
        HttpResponse response;

        File currfile=new File(RESPONSE_TEMPLATE_PATH+"error.vm");
        if(currfile.exists())
        {
            System.out.println("Error Response file exist");
            ResponseGenerator generator = new ResponseGeneratorImpl();
            return generator.generate(REST_RESPONSE_PATH,"error");
        }
        else
        {
            System.out.println("Error Response file Does not exist");
            return null;
        }

//
//        System.out.println("file name is "+ RESPONSE_TEMPLATE_PATH +"-"+ temp.replace('/','-')+".vm");
//        if(!currfile.exists()) {
//            System.out.println("File does not exist");
//            String url = uriInfo.getAbsolutePath().toString()
//                    + "?username=BLAdmin&password=bladelogic&authType=SRP&role=BLAdmins&version=8.2";
//            System.out.println(url);
//            String bsaURL = UriBuilder.fromUri(uriInfo.getAbsolutePath()).port(10843).scheme("https").host("10.1.32.49").toString() + QUERY_PARAM;
//            System.out.println(bsaURL);
//            HttpRequestBase base = new HttpGet(bsaURL);
//            HttpClient client = getClient(new DefaultHttpClient());
//            base.addHeader("Accept", "text/xml");
//            response = client.execute(base);
//            System.out.println(temp);
//
//            BufferedWriter writer = new BufferedWriter(new FileWriter(currfile));
//        //    writer.write(response,0,response.length());
//            writer.close();
//
//            Response browser_response=Response.ok(response.getEntity().getContent(),MediaType.TEXT_XML).build();
//            return  browser_response;
//        }
//        else
//        {
//        //    responsefile=new File("C:\\Users\\Public\\Response\\Ideal-responses\\" + temp.replace('/','-')+".xml");
//            System.out.println("------------File Exist ------------------\n"+REST_RESPONSE_PATH +"-"+ temp.replace('/','-')+".vm");
//            ResponseGenerator generator = new ResponseGeneratorImpl();
//            return generator.generate(REST_RESPONSE_PATH,"-"+temp.replace('/','-'));
//        }

    }

    private HttpClient getClient(HttpClient base) {
        HttpClient client = null;

        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory sf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("https", 443, sf));
            ThreadSafeClientConnManager ccm = new ThreadSafeClientConnManager(schemeRegistry);
            ccm.setDefaultMaxPerRoute(15);
            ccm.setMaxTotal(50);
            client = new DefaultHttpClient(ccm, base.getParams());

            /**
             * Set the Keep-Alive timeout
             */
            ((DefaultHttpClient)client).setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {

                public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                    return 60000;
                }

            });

        } catch (Exception e) {
            //Logger.log(Logger.Category.BBSA, "RESTfulRequestHelper", "getClient", Logger.Level.ERROR, "Could not create HttpClient", e);
        }

        return client;
    }

}