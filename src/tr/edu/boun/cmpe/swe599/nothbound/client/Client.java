package tr.edu.boun.cmpe.swe599.nothbound.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import tr.edu.boun.cmpe.swe599.nothbound.info.Request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Client {

	public static void main(String[] args) {
		
		postXml("http://localhost:8000/nb");
	}
	
	private static void postXml(String URL){
		try{
			Gson gson = new GsonBuilder().create();
			String json = gson.toJson(getRequest());
			
			URL url = new URL( URL);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(),"utf-8");
            
            System.out.println("POSTING... " + json);
            out.write(json);
            
            out.close();
    
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }
            in.close();
        }catch (Throwable e) {
            e.printStackTrace();
        }
	}
	
	private static Request getRequest() {
		Request r = new Request();
		r.setClientMAC("00:00:00:00:00:01");
		r.setQueryString("cmpe.boun.edu.tr");
		r.setSwitchId("00:00:00:00:00:00:00:01");
		r.setIngressPort("1");
		return r;
	}

}
