package com.bmc.cloud.bsasimulator.response;

import com.bmc.cloud.bsasimulator.internal.Constants;

import javax.ws.rs.core.Response;

public interface ResponseGenerator {
    public Response generate(String DirectoryPath ,String filepath);
    public Response generate(String DirectoryPath ,String filepath,String SoapCommandType);

}
