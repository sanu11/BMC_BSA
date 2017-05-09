package com.bmc.cloud.bsasimulator.resources;

import com.bmc.cloud.bsasimulator.internal.BSAFactory;
import com.bmc.cloud.bsasimulator.response.ResponseGenerator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.io.*;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.bmc.cloud.bsasimulator.internal.Constants.*;


/**
 * Created by pratshin on 03-05-2017.
 *
 * Acts as Recording class.
 * Accepts any damn request(rest/soap) from clm ,fires it  to bsa, gets the response and send it back to clm(middle-man).
 * Records the response i.e saves it to a file.
 */
@Path("/")
public class RecordMode {

    //get uri context.
    @Context
    UriInfo uriInfo;


    @GET
    @Path("services/LoginService")
    @Produces("text/xml")
    public Response getLoginServiceWSDL(@QueryParam("wsdl") String wsdl) throws IOException {
        System.out.println("Incoming request is :"+uriInfo.getAbsolutePath());

        //create URL for BSA.
        String bsaUrl= UriBuilder.fromUri(uriInfo.getAbsolutePath()).port(10843).scheme("https").host(BSA_HOST).toString()+"?wsdl";

        System.out.println("BSA url is :"+bsaUrl);

        //Creating a http request.
        HttpRequestBase httpRequestBase=new HttpGet(bsaUrl);
        httpRequestBase.addHeader("Accept","text/xml");
        //create a https client.
        HttpClient client=getClient(new DefaultHttpClient());
        HttpResponse response=client.execute(httpRequestBase);

        //byte data[]=toByteArray(response.getEntity().getContent());
        //Write the response to file.
        //saveResponse(data,uriInfo.getPath());

        //InputStream stream=new ByteArrayInputStream(data);
        //Return the response to CLM.
        Response bsaResponse=Response.ok(response.getEntity().getContent(),MediaType.TEXT_XML).build();

        return bsaResponse;
    }

    @GET
    @Path("services/AssumeRoleService")
    @Produces("text/xml")
    public Response getAssumedRoleWSDL(@QueryParam("wsdl") String wsdl) throws IOException {
        System.out.println("Incoming request is :"+uriInfo.getAbsolutePath());

        //create URL for BSA.
        String bsaUrl= UriBuilder.fromUri(uriInfo.getAbsolutePath()).port(10843).scheme("https").host(BSA_HOST).toString()+"?wsdl";

        System.out.println("BSA url is :"+bsaUrl);

        //Creating a http request.
        HttpRequestBase httpRequestBase=new HttpGet(bsaUrl);
        httpRequestBase.addHeader("Accept","text/xml");
        //create a https client.
        HttpClient client=getClient(new DefaultHttpClient());
        HttpResponse response=client.execute(httpRequestBase);

       // byte data[]=toByteArray(response.getEntity().getContent());
        //Write the response to file.
        //saveResponse(data,uriInfo.getPath());

        //InputStream stream=new ByteArrayInputStream(data);
        //Return the response to CLM.
        Response bsaResponse=Response.ok(response.getEntity().getContent(),MediaType.TEXT_XML).build();

        return bsaResponse;
    }
    @GET
    @Path("services/CLITunnelService")
    @Produces("text/xml")
    public Response getCLITunnelServiceWSDL(@QueryParam("wsdl") String wsdl) throws IOException {
        System.out.println("Incoming request is :"+uriInfo.getAbsolutePath());

        //create URL for BSA.
        String bsaUrl= UriBuilder.fromUri(uriInfo.getAbsolutePath()).port(10843).scheme("https").host(BSA_HOST).toString()+"?wsdl";

        System.out.println("BSA url is :"+bsaUrl);

        //Creating a http request.
        HttpRequestBase httpRequestBase=new HttpGet(bsaUrl);
        httpRequestBase.addHeader("Accept","text/xml");
        //create a https client.
        HttpClient client=getClient(new DefaultHttpClient());
        HttpResponse response=client.execute(httpRequestBase);

        //byte data[]=toByteArray(response.getEntity().getContent());
        //Write the response to file.
        //saveResponse(data,uriInfo.getPath());

        //InputStream stream=new ByteArrayInputStream(data);
        //Return the response to CLM.
        Response bsaResponse=Response.ok(response.getEntity().getContent(),MediaType.TEXT_XML).build();

        return bsaResponse;
    }
    @GET
    @Path("/{default : .*}")
    @Produces("text/xml")
    public Response recordRestCalls() throws IOException {
        System.out.println("Incoming request is :"+uriInfo.getAbsolutePath());

        //create URL for BSA.
        MultivaluedMap <String,String> map1=new MultivaluedHashMap<>();
        map1=uriInfo.getQueryParameters();

        Set<String> keys=map1.keySet();
        Iterator<String> itr=keys.iterator();

        StringBuilder queryparam = new StringBuilder("?");
        while (itr.hasNext()){
            String key=itr.next();
            String val = map1.getFirst(key);
            if (key.equals("bquery")){
                val=encodeQuery(val);
            }
            queryparam.append("&"+key + "="+val);
        }
        queryparam.deleteCharAt(1);

        String bsaUrl= UriBuilder.fromUri(uriInfo.getAbsolutePath()).port(10843).scheme("https").host(BSA_HOST).toString() + queryparam.toString();

        System.out.println("BSA url is :"+bsaUrl);

        //Creating a http request.
        HttpRequestBase httpRequestBase=new HttpGet(bsaUrl);
        httpRequestBase.addHeader("Accept","text/xml");
        //create a https client.
        HttpClient client=getClient(new DefaultHttpClient());
        HttpResponse response=client.execute(httpRequestBase);

        byte data[]=toByteArray(response.getEntity().getContent());
        //Write the response to file.
        saveResponse(data,uriInfo.getPath());

        //Return the response to CLM.

        Response bsaResponse=constructResponse(uriInfo.getPath());
        return bsaResponse;
    }

    private Response constructResponse(String filename) throws IOException {
        Document document=null;
        filename=filename.replace('/','-');
        System.out.println(filename);
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder=factory.newDocumentBuilder();
            document=builder.parse(RECORD_MODE_PATH+filename+".xml");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Response response=Response.ok(document,MediaType.TEXT_XML).build();
        return response;
    }
    private String encodeQuery(String bql) throws UnsupportedEncodingException {
        String str=bql.substring(bql.indexOf("\"")+1,bql.lastIndexOf("\""));
        str="\""+str+"\"";
        str= URLEncoder.encode(str,"UTF-8");
        System.out.println(str);
        return str;
    }
    @POST
    @Path("services/LoginService")
    @Produces("text/xml")
    public Response loginService(InputStream stream) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        System.out.println("Incoming soap request :"+uriInfo.getAbsolutePath());
        HttpClient client=getClient(new DefaultHttpClient());
        String url=UriBuilder.fromUri(uriInfo.getAbsolutePath()).port(10843).host("10.1.32.49").scheme("https").toString();
        System.out.println("BSA url is :"+url);
        HttpPost post=new HttpPost(url);
        post.setEntity(new InputStreamEntity(stream));
        HttpResponse response=client.execute(post);

       // byte data[]=toByteArray(response.getEntity().getContent());

        //InputStream responseStream=new ByteArrayInputStream(data);
        //Return the response to CLM.
        Response bsaResponse=Response.ok(response.getEntity().getContent(),MediaType.TEXT_XML).build();
        return bsaResponse;
    }

    @POST
    @Path("services/AssumeRoleService")
    @Produces("text/xml")
    public Response assumeRoleService(InputStream stream) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        System.out.println("Incoming soap request :"+uriInfo.getAbsolutePath());
        HttpClient client=getClient(new DefaultHttpClient());
        String url=UriBuilder.fromUri(uriInfo.getAbsolutePath()).port(10843).host("10.1.32.49").scheme("https").toString();
        System.out.println("BSA url is :"+url);
        HttpPost post=new HttpPost(url);
        post.setEntity(new InputStreamEntity(stream));
        HttpResponse response=client.execute(post);

        //byte data[]=toByteArray(response.getEntity().getContent());

        //InputStream responseStream=new ByteArrayInputStream(data);
        //Return the response to CLM.
        Response bsaResponse=Response.ok(response.getEntity().getContent(),MediaType.TEXT_XML).build();

        return bsaResponse;
    }

    @POST
    @Path("services/CLITunnelService")
    @Produces("text/xml")
    public Response recordSoapCalls(InputStream stream) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        System.out.println("Incoming soap request :"+uriInfo.getAbsolutePath());
        HttpClient client=getClient(new DefaultHttpClient());
        String url=UriBuilder.fromUri(uriInfo.getAbsolutePath()).port(10843).host("10.1.32.49").scheme("https").toString();
        System.out.println("BSA url is :"+url);
        byte[] temp=toByteArray(stream);

        InputStream stream1=new ByteArrayInputStream(temp);
        String CommandName=getCommandName(stream1);

        InputStream stream2=new ByteArrayInputStream(temp);
        HttpPost post=new HttpPost(url);
        post.setEntity(new InputStreamEntity(stream2));
        HttpResponse response=client.execute(post);

        //byte data[]=toByteArray(response.getEntity().getContent());
        //Write the response to file.
        //saveResponse(data,CommandName);

        //InputStream Responsestream=new ByteArrayInputStream(data);
        //Return the response to CLM.
        Response bsaResponse=Response.ok(response.getEntity().getContent(),MediaType.TEXT_XML).build();

        return bsaResponse;
    }

    private  String getCommandName(InputStream stream) throws ParserConfigurationException, IOException, SAXException {
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
        return node.getTextContent();
    }
    /*
         Saves the http-response to filename(basically url-path)
         Please don't delete this function roshan bhor.
     */
    private void saveResponse(byte[] data,String path) throws IOException {
        String filename=path.replace('/','-');
        System.out.println("Creating file with name :"+filename);
        File file=new File(RECORD_MODE_PATH+filename+".xml");

        ByteArrayInputStream inputStream=new ByteArrayInputStream(data);
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        BufferedWriter writer=new BufferedWriter(new FileWriter(file));
        String str=" ";
        while((str=reader.readLine())!=null){
            writer.write(str);
        }
        writer.close();
        reader.close();
        System.out.println("Done writing to file.");
    }


    /*

     */
    public byte[] toByteArray(InputStream stream) throws IOException {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

        int pos;
        byte temp[]=new byte[1024];

        while(true){
            pos=stream.read(temp);
            if(pos==-1){
                break;
            }
            outputStream.write(temp,0,pos);
        }
        byte[] data=outputStream.toByteArray();
        return data;
    }

    /*
        get https client.(For firing https request.)
        taken directly from CLM code.(RESTfulRequestHelper)
     */
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
