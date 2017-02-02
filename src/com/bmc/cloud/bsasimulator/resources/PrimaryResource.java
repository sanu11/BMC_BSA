package com.bmc.cloud.bsasimulator.resources;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import com.bmc.cloud.bsasimulator.internal.BSAFactory;
import com.bmc.cloud.bsasimulator.proccessor.BSAProcessor;
import com.bmc.cloud.bsasimulator.response.ResponseGenerator;
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
import java.io.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import static com.bmc.cloud.bsasimulator.internal.Constants.QUERY_PARAM;
import static com.bmc.cloud.bsasimulator.internal.Constants.RESPONSE_PATH;

@Path("/")
public class PrimaryResource {
    @QueryParam("username")
    String username;
    @QueryParam("password")
    String password;
    static BSAProcessor processor= BSAFactory.getBSAProccesser();
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
        Response response=generator.generate(filepath);
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
        Response response=generator.generate(filepath);
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
        Response response=generator.generate(filepath);
        return response;
    }
    @POST
    @Path("services/LoginService")
    @Produces("text/xml")
    public Response loginService(InputStream stream){
        System.out.println(uriInfo.getAbsolutePath());
        System.out.println("loginService");
        String filepath="soap_response.xml";
        ResponseGenerator generator=BSAFactory.getResponseGenerator();
        Response response=generator.generate(filepath);
        return response;
    }

    @POST
    @Path("services/AssumeRoleService")
    @Produces("text/xml")
    public Response assumeRoleService(InputStream stream){
        System.out.println(uriInfo.getAbsolutePath());
        System.out.println("assumeRoleService");
        String filepath="assume.xml";
        ResponseGenerator generator=BSAFactory.getResponseGenerator();
        Response response=generator.generate(filepath);
        return response;
    }

    @POST
    @Path("services/CLITunnelService")
    @Produces("text/xml")
    public Response CLITunnelService(InputStream stream){
        System.out.println(uriInfo.getAbsolutePath());
        System.out.println("CLITunnelService");
        String filepath="getappserverdetails.xml";
        ResponseGenerator generator=BSAFactory.getResponseGenerator();
        Response response=generator.generate(filepath);
        return response;
    }


    @POST
    @Path("/{default : .*}")
    @Produces("text/xml")
    public Response soapDefaultPath(InputStream stream){
        System.out.println(uriInfo.getAbsolutePath());
        System.out.println("soapDefaultPath");
        String filepath="soap_response.xml";
        ResponseGenerator generator=BSAFactory.getResponseGenerator();
        Response response=generator.generate(filepath);
        return response;

    }
    @GET
    @Path("/{default : .*}")
    @Produces("text/xml")
    public Response restDefaultPath() throws IOException {
        logger.debug(uriInfo.getAbsolutePath());
        System.out.println(uriInfo.getAbsolutePath());
        System.out.println("defaultPath()");
        String temp=uriInfo.getPath().toString();
        File currfile=new File("C:\\Users\\pratshin\\workspace\\Simulator\\Responses\\" + temp.replace('/','-')+".xml");
        String url=uriInfo.getAbsolutePath().toString()
                   +"?username=BLAdmin&password=bladelogic&authType=SRP&role=BLAdmins&version=8.2";
        System.out.println(url);
        String bsaURL= UriBuilder.fromUri(uriInfo.getAbsolutePath()).port(10843).scheme("https").host("10.1.32.49").toString()+QUERY_PARAM;
        System.out.println(bsaURL);
        HttpRequestBase base =new HttpGet(bsaURL);
        HttpClient client=getClient(new DefaultHttpClient());
        base.addHeader("Accept","text/xml");
        HttpResponse response=client.execute(base);
        System.out.println(temp);
        File responsefile=new File("C:\\Users\\pratshin\\workspace\\Simulator\\Responses\\" + temp.replace('/','-')+".xml");
        System.out.println(RESPONSE_PATH + temp.replace('/','-')+".xml");
        BufferedWriter writer=new BufferedWriter(new FileWriter(responsefile));
        //writer.write(response,0,response.length());
        writer.close();

        Response browser_response=Response.ok(response.getEntity().getContent(),MediaType.TEXT_XML).build();


        return  browser_response;
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