package com.bmc.cloud.bsasimulator.resources;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.bmc.cloud.bsasimulator.internal.BSAFactory;
import com.bmc.cloud.bsasimulator.proccessor.BSAProcessor;
import org.apache.log4j.Logger;

import static com.bmc.cloud.bsasimulator.internal.Constants.COMMON_PREFIX;

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

    @Path(COMMON_PREFIX+"{guid}/Assets")
    @Produces("application/xml")
    public AssetResources getResponseFromAssets(@PathParam("guid") String guid){
        logger.debug(uriInfo.getAbsolutePath());
        return new AssetResources(guid,username,password);
    }
    @GET
    @Path(COMMON_PREFIX+"{guid}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getGUIDResponse(@PathParam("guid") String guid) {
        logger.debug(uriInfo.getAbsolutePath());
        Response response=processor.process(username,password,guid);
        return response;
    }
    @GET
    @Path("group/Depot/CSM_Virtual_Guest_Packages/{VGP_ID}")
    @Produces("application/xml")
    public Response getVGP(@PathParam("VGP_ID") String VGP_ID) {
        logger.debug(uriInfo.getAbsolutePath());
        return processor.process(username,password,VGP_ID);
    }
    @GET
    @Path("/{default : .*}")
    @Produces("application/xml")
    public Response defaultPath(){
        logger.debug(uriInfo.getAbsolutePath());
        return processor.process(username,password,"default");
    }

}