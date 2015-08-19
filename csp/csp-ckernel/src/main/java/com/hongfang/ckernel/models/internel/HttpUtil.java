package com.hongfang.ckernel.models.internel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.codec.binary.Base64;

import com.google.gdata.util.common.base.CharEscapers;
import com.hongfang.ckernel.APIException;
import com.hongfang.ckernel.Result;
import com.hongfang.ckernel.http.Environment;
import com.hongfang.ckernel.models.User;
import com.hongfang.ckernel.util.JsonUtil;

public class HttpUtil {
	public enum Method {
		GET, POST, PUT, DELETE;
	}
	
	private static class Resp {
		int httpCode;
		String jsonString;
		
		private Resp(int httpCode, String jsonString) {
			this.httpCode = httpCode;
			this.jsonString = jsonString;
		}
		
		private Result toResult() {
			User user = JsonUtil.toObject(jsonString, User.class);
			System.out.println("JsonUtil.toObject:" + JsonUtil.toJson(user));
			return new Result(httpCode, JsonUtil.toJson(user));
		}
		//@todo
		private Result toResultList() {
			//return new ListResult(httpCode, JsonUtil.);
			return null;
		}
	}

	public static Result get(String url, Params params, String jsonString, Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Result post(String url, Params params, String jsonString, Environment env) throws IOException {
		return doFormSubmit(url, Method.POST, toQueryStr(params), jsonString, env);
	}

	public static Result put(String url, Params params, String jsonString, Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static String toQueryStr(Params map) {
		StringJoiner buf = new StringJoiner("&");
		for (Map.Entry<String, Object>entry : map.entries()) {
			Object value = entry.getValue();
			if (value instanceof List) {
				List<String> l = (List<String>)value;
				for (int i=0; i<l.size(); i++) {
					String val = l.get(i);
					String keyValPair = enc(entry.getKey() + "[" + i + "]" + "=" + enc(val != null?val:""));
					buf.add(keyValPair);
				}
			} else {
				String keyValPair = enc(entry.getKey() + "=" + enc((String)value));
				buf.add(keyValPair);
			}
		}
		return buf.toString();
	}
	
	private static String enc(String val) {
		try {
			return CharEscapers.uriEscaper().escape(val);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}
	
	private static Result doFormSubmit(String url, Method m, String queryStr, String jsonString, Environment env) throws IOException {
		HttpURLConnection conn = createConnection(url, m, env);
		writeContent(conn, queryStr, jsonString);
		Resp resp = sendRequest(conn);
		return resp.toResult();
	}
	
    private static void writeContent(HttpURLConnection conn, String queryStr, String jsonStr) throws IOException {
        if (queryStr == null) {
            return;
        }
        OutputStream os = conn.getOutputStream();
        try {
        	//@deprecated post for form post
        	//os.write(queryStr.getBytes(Environment.CHARSET));
        	//@todo jsonwriter
        	os.write((jsonStr).getBytes(Environment.CHARSET));
        } finally {
            os.close();
        }
    }
	
    private static HttpURLConnection createConnection(String url, Method m, Environment config)
    	throws IOException {
    	HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
    	conn.setRequestMethod(m.name());
    	setTimeouts(conn, config);
    	addHeaders(conn, config);
    	setContentType(conn, m);
    	if ((m == Method.POST) || (m == Method.PUT)) {
    		conn.setDoOutput(true);
    	}
    	conn.setUseCaches(false);
    	return conn;
    }
    
    private static Resp sendRequest(HttpURLConnection conn) throws IOException {
    	int httpRespCode = conn.getResponseCode();
    	if (httpRespCode == HttpURLConnection.HTTP_NO_CONTENT) {
            throw new RuntimeException("Got http_no_content response");
        }
        boolean error = httpRespCode < 200 || httpRespCode > 299;
        String jsonString = getContentAsJSON(conn, error);
        if (error) {
        	throw new APIException(jsonString);
        }
        System.out.println("return json:" + jsonString);
        return new Resp(httpRespCode, jsonString);
    }
    
    private static void setTimeouts(URLConnection conn, Environment config) {
        conn.setConnectTimeout(config.connectTimeout);
        conn.setReadTimeout(config.readTimeout);
    }
    
    private static void setContentType(HttpURLConnection conn, Method m) {
        if ((m == Method.POST) || (m == Method.PUT)) {
        	//@deprecated by M 2014-08-03
            //addHeader(conn, "Content-Type", "application/x-www-form-urlencoded;charset=" + Environment.CHARSET);
        	addHeader(conn, "Content-Type", "application/json;charset=" + Environment.CHARSET);
        }
    }
        
    private static void addHeaders(HttpURLConnection conn, Environment config) {
         addHeader(conn, "Accept-Charset", Environment.CHARSET);
         addHeader(conn, "User-Agent", String.format("CSP-Java-Client v%s", Environment.LIBRARY_VERSION));
         addHeader(conn, "Authorization", getAuthValue(config));
         addHeader(conn, "Accept", "application/json");
    }
    
    private static void addHeader(HttpURLConnection conn, String headerName, String value) {
        conn.setRequestProperty(headerName, value);
    }
    
    private static String getAuthValue(Environment config) {
        //return "Basic " + Base64.encodeBase64String((config.apiKey + ":").getBytes())
    	return "Basic " + new String(Base64.encodeBase64((config.apiKey + ":").getBytes()))
                .replaceAll("\r?", "").replaceAll("\n?", "");
    }
    
    private static String getContentAsJSON(HttpURLConnection conn, boolean error) throws IOException {
        String content = getContentAsString(conn, error);
        checkRequiredJSONResp(content);
        return content;
    }
    
    private static String getContentAsString(HttpURLConnection conn, boolean error) throws IOException {

        InputStream resp = (error) ? conn.getErrorStream() : conn.getInputStream();
        if (resp == null) {
            throw new RuntimeException("Got Empty Response ");
        }
        try {
            if ("gzip".equalsIgnoreCase(conn.getContentEncoding())) {
                resp = new GZIPInputStream(resp);
            }
            InputStreamReader inp = new InputStreamReader(resp, Environment.CHARSET);
            StringBuilder buf = new StringBuilder();
            char[] buffer = new char[1024];//Should use content length.
            int bytesRead;
            while ((bytesRead = inp.read(buffer, 0, buffer.length)) >= 0) {
                buf.append(buffer, 0, bytesRead);
            }
            String content = buf.toString();
            return content;
        } finally {
            resp.close();
        }
    }
    
    private static void checkRequiredJSONResp(String respJson) {
    	if (!JsonUtil.isJSONValid(respJson)) {
            throw new RuntimeException("Expected json formatted content in response:" + respJson);
        }
    }


}
