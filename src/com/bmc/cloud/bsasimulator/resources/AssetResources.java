package com.bmc.cloud.bsasimulator.resources;
import com.bmc.cloud.bsasimulator.internal.BSAFactory;
import com.bmc.cloud.bsasimulator.proccessor.BSAProcessor;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static com.bmc.cloud.bsasimulator.internal.Constants.BMC_VMware_VirtualInfrastructureManager;

/**
 * Created by pratshin on 09-12-2016.
 */

public class AssetResources {
    String guid;
    String username;
    String password;
    BSAProcessor processor= BSAFactory.getBSAProccesser();
    AssetResources(String guid,String username,String password){
        this.guid=guid;
        this.username=username;
        this.password=password;
    }

    @GET
    @Produces("application/xml")
    public Response getAssetResponse(){
        String filepath=guid+"-"+"Assets";
        return processor.process(username,password,filepath);
    }

    @GET
    @Path(BMC_VMware_VirtualInfrastructureManager)
    @Produces("application/xml")
    public Response getAssetResource(){
        String filepath=guid+"-"+"Assets"+"-"+BMC_VMware_VirtualInfrastructureManager.replace('/','-');
        return processor.process(username,password,filepath);
    }

    @GET
    @Path(BMC_VMware_VirtualInfrastructureManager+"{Resource}")
    @Produces("application/xml")
    public Response getAssetResource(@PathParam("Resource") String resource){
        String filepath=guid+"-"+"Assets"+"-"+BMC_VMware_VirtualInfrastructureManager.replace('/','-')+resource;
        return processor.process(username,password,filepath);

    }
    @GET
    @Path(BMC_VMware_VirtualInfrastructureManager+"Hardware/"+"{Resource}")
    @Produces("application/xml")
    public Response getHardWareAssetResource(@PathParam("Resource") String resource){
        String filepath=guid+"-"+"Assets"+"-"+BMC_VMware_VirtualInfrastructureManager.replace('/','-')+"Hardware-"+resource;
        return processor.process(username,password,filepath);
    }
}
