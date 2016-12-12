package com.bmc.cloud.bsasimulator.response;

import javax.ws.rs.core.Response;

public interface ResponseGenerator {
    public Response generate(String filepath);
}
