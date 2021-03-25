
package se.liu.ida.tdp024.account.data.api.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;


public class RemoteService {
	String port;
	String host;
	String protocol;
	
	public <T> T fetch(String path, Class<T> classType) throws IOException, URISyntaxException {
		URL url = new URL(protocol+host+port+path);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("accept", "application/json");
		InputStream responseStream = connection.getInputStream();
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(responseStream, classType);
	}


    public RemoteService(int port) 
    {
    	this.port = ":"+port;
    	this.host = "localhost";
    	this.protocol = "http://";
    }
}