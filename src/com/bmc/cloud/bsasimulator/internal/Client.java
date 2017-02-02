package com.bmc.cloud.bsasimulator.internal;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by pratshin on 07-01-2017.
 */
public class Client {
    public static void main(String args[]) throws IOException {
        String url="https://docs.bmc.com/docs/ServerAutomation/86/developing/blcli-reference/using-the-cli/executing-commands";
        HttpRequestBase base =new HttpGet(url);
        System.out.println(base.getURI());
        System.out.println(base.getURI().getPath());
        HttpClient client=new DefaultHttpClient();
        base.addHeader("Accept","text/xml");
        HttpResponse response=client.execute(base);
        InputStream stream=response.getEntity().getContent();
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
