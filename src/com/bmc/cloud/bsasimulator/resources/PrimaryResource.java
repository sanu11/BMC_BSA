package com.bmc.cloud.bsasimulator.resources;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.bmc.cloud.bsasimulator.internal.BSAFactory;
import com.bmc.cloud.bsasimulator.proccessor.BSAProcessor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static com.bmc.cloud.bsasimulator.internal.Constants.COMMON_PREFIX;
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
    @Path("type/AssetClasses")
    @Produces("text/xml")
    public Response fun1(){
        logger.debug(uriInfo.getAbsolutePath());
        Response response=processor.process(username,password,"type-AssetClasses");
        return response;
    }

    @Path(COMMON_PREFIX+"{guid}/Assets")
    @Produces("text/xml")
    public AssetResources getResponseFromAssets(@PathParam("guid") String guid){
        logger.debug(uriInfo.getAbsolutePath());
        return new AssetResources(guid,username,password);
    }
    @GET
    @Path(COMMON_PREFIX+"{guid}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getGUIDResponse(@PathParam("guid") String guid) {
        System.out.println("hi");
        logger.debug(uriInfo.getAbsolutePath());
        Response response=processor.process(username,password,guid);
        return response;
    }
    @GET
    @Path("group/Depot/CSM_Virtual_Guest_Packages/{VGP_ID}")
    @Produces("text/xml")
    public Response getVGP(@PathParam("VGP_ID") String VGP_ID) {
        logger.debug(uriInfo.getAbsolutePath());
        return processor.process(username,password,VGP_ID);
    }
    @GET
    @Path("/{default : .*}")
    @Produces("text/xml")
    public Response defaultPath() throws IOException {
        logger.debug(uriInfo.getAbsolutePath());

        // fire to bbsa
        // get response
        // store using uri
        // return response

        // http://bmcstrykerclmonbmc:10844/type/PropertySetClasses/SystemObject/Device/
        // ?username=BLAdmin&password=bladelogic&authType=SRP&role=BLAdmins&version=8.2

        String temp=uriInfo.getPath().toString();

        // search file
        // if found
        File currfile=new File("C:\\Users\\pratshin\\workspace\\Simulator\\Responses\\" + temp.replace('/','-')+".xml");
        if(currfile.exists())
        {
            System.out.println("Response exist");
            return null;
        }


        // if not found
        // construct uri and fire
        String url=uriInfo.getAbsolutePath().toString()
                   +"?username=BLAdmin&password=bladelogic&authType=SRP&role=BLAdmins&version=8.2";
        System.out.println(url);
        String bsaURL= UriBuilder.fromUri(uriInfo.getAbsolutePath()).port(10843).scheme("https").host("10.1.32.49").toString()+QUERY_PARAM;
        System.out.println(bsaURL);
        HttpRequestBase base =new HttpGet(bsaURL);
        HttpClient client=getClient(new DefaultHttpClient());
        base.addHeader("Accept","text/xml");
        ResponseHandler <String> newStringResponse = new BasicResponseHandler();

        String response=client.execute(base,newStringResponse);
        System.out.println(response);


        // create file
        System.out.println(temp);
        File responsefile=new File("C:\\Users\\pratshin\\workspace\\Simulator\\Responses\\" + temp.replace('/','-')+".xml");
        System.out.println(RESPONSE_PATH + temp.replace('/','-')+".xml");
        BufferedWriter writer=new BufferedWriter(new FileWriter(responsefile));
        writer.write(response,0,response.length());
        writer.close();

        //Response browser_response=Response.ok(response.getEntity().getContent(),MediaType.TEXT_XML).build();


        return  null;
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