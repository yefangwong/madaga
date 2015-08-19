package com.hongfang.ckernel.models.internel;

import java.io.IOException;

import com.hongfang.ckernel.Result;
import com.hongfang.ckernel.http.Environment;
import com.hongfang.ckernel.models.internel.HttpUtil.Method;

public class Request<U extends Request> {

	private HttpUtil.Method httpMeth;
	
    private String uri;

    private String jsonString;
    
    protected Params params = new Params();
    
    public Request(Method httpMeth, String uri, String jsonString) {
    	this.uri = uri;
    	this.httpMeth = httpMeth;
    	this.jsonString = jsonString;
    }
    
    protected Params params() {
        return params;
    }
    
    public final Result request() throws IOException {
        return request(Environment.defaultConfig());
    }
    
    public final Result request(Environment env) throws IOException {
	   if(env == null) {
           throw new RuntimeException("Environment cannot be null");
       }
       String url = new StringBuilder(env.apiBaseUrl()).append(uri).toString();
       switch(httpMeth) {
           case GET:
               return HttpUtil.get(url, params(), jsonString, env);
           case POST:
               return HttpUtil.post(url, params(), jsonString, env);
           case PUT:
               return HttpUtil.put(url, params(), jsonString, env);
           default:
               throw new RuntimeException("Not handled type [" + httpMeth + "]");
       }    	
    }
}
