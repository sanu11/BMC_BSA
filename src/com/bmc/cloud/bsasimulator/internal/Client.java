package com.bmc.cloud.bsasimulator.internal;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by pratshin on 07-01-2017.
 */
public class Client {
    public static void main(String args[]) throws IOException {
        String url="http://localhost:10844/type/AssetClasses/?username=BLAdmin&password=bladelogic&authType=SRP&role=BLAdmins&version=8.2";
        HttpRequestBase base =new HttpGet(url);
        HttpClient client=new DefaultHttpClient();
        base.addHeader("Accept","text/xml");
        HttpResponse response=client.execute(base);
        if(response.getEntity()==null){
            System.out.println("Error");
        }else{
            System.out.println("yes");
            if (response.getEntity().getContentType()==null){
                System.out.println("wrong");
            }
        }
    }
}
