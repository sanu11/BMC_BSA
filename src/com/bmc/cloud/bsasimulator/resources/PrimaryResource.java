package com.bmc.cloud.bsasimulator.resources;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import com.bmc.cloud.bsasimulator.internal.BSAFactory;
import com.bmc.cloud.bsasimulator.proccessor.BSAProcessor;
import static com.bmc.cloud.bsasimulator.internal.Constants.COMMON_PREFIX;

@Path("/")
public class PrimaryResource {
    @QueryParam("username")
    String username;
    @QueryParam("password")
    String password;
    static BSAProcessor processor= BSAFactory.getBSAProccesser();

    @Path(COMMON_PREFIX+"{guid}/Assets")
    @Produces("application/xml")
    public AssetResources getResponseFromAssets(@PathParam("guid") String guid){
        return new AssetResources(guid,username,password);
    }
    @GET
    @Path(COMMON_PREFIX+"{guid}")
    @Produces("application/xml")
    public Response getGUIDResponse(@PathParam("guid") String guid) {
        return processor.process(username,password,guid);
    }
    @GET
    @Path("group/Depot/CSM_Virtual_Guest_Packages/{VGP_ID}")
    @Produces("application/xml")
    public Response getVGP(@PathParam("VGP_ID") String VGP_ID) {
        return processor.process(username,password,VGP_ID);
    }

}