package tr.edu.boun.cmpe.swe599.nothbound;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import tr.edu.boun.cmpe.swe599.nothbound.config.PropertyReader;
import tr.edu.boun.cmpe.swe599.nothbound.info.Actions;
import tr.edu.boun.cmpe.swe599.nothbound.info.ControllerResponse;
import tr.edu.boun.cmpe.swe599.nothbound.info.Request;
import tr.edu.boun.cmpe.swe599.nothbound.info.Rule;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Run {

	private static final String DATE_PATTERN = "dd-M-yyyy hh:mm:ss";

	private static final String OK = "ok";
	private static final String NOK = "nok";
	private static final String OK_FROM_CONTROLLER = "Entry pushed";
	
	protected static final Set<String> toCloudletHost = new ConcurrentSkipListSet<String>();
	protected static final Set<String> toClientHost = new ConcurrentSkipListSet<String>();

	private static final AtomicInteger flowId = new AtomicInteger(1);
	
	private static final AtomicInteger sessionId = new AtomicInteger(1);
	private static final String SESSIONSTR = "<{0}>";
	

	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
		server.createContext("/nb", new MyHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
		System.out.println("<NoAp> HTTP server of the Northbound Application started.");
	}

	static class MyHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {

			SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
			String date = sdf.format(new Date());
			
			String session = getSessionStr();

			System.out.println(session + "Request received on " + date);

			String response = processRequest(t, session);
			
			System.out.println(session + "Returning response");
			t.sendResponseHeaders(200, response.length());
			OutputStream responseBody = t.getResponseBody();
			responseBody.write(response.getBytes());
			responseBody.close();
		}
	}
	
	private static String getSessionStr(){
		MessageFormat format = new MessageFormat(SESSIONSTR);
		return format.format(new Object[] {sessionId.getAndIncrement()}) + " ";
	}

	private static String processRequest(HttpExchange t, String session) {
		InputStreamReader isr;
		try {
			isr = new InputStreamReader(t.getRequestBody(), "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String query = br.readLine();

			System.out.println(session + "Request received: " + query);

			Request content = getContent(query);

			if (content == null) {
				System.out
						.println("session + Received content can not be deserialize into a valid object");
				return NOK;
			}
			
			System.out.println(session + "Received and parsed:" + content.toString());

			if (!validateInpuParameters(content)) {
				System.out.println(session + "Missing parameters");
				return NOK;
			}

			// validate query string
			String cloudname = getMatchingCloud(content.getQueryString());
			if (cloudname == null) {
				System.out.println(session + "Cloudlet does not exist.");
				return NOK;
			}

			// send DNS query
			String ipAddress = InetAddress.getByName(cloudname)
					.getCanonicalHostName();

			System.out.println(session + "IP address of " + content.getQueryString()
					+ " is " + ipAddress);

			addFlow(content, ipAddress, cloudname, session);
			
			return OK;

		} catch (IOException e) {
			e.printStackTrace();
			return NOK;
		} finally {

		}
	}

	private static Request getContent(String query) {
		if (query != null) {
			Gson gson = new GsonBuilder().create();
			Request fromJson = gson.fromJson(query, Request.class);
			return fromJson;
		}
		return null;
	}

	private static boolean validateInpuParameters(Request content) {
		return !(content.getClientMAC() == null
				|| content.getIngressPort() == null
				|| content.getQueryString() == null || content.getSwitchId() == null);
	}

	private static void addFlow(Request r, String ipv4_dst, String cloudname, String session) {
		Gson gson = new GsonBuilder().create();
		String toCloudletKey = getToCloudletKey(ipv4_dst, r.getSwitchId());
		
		System.out.println(session + "================================================");
		
		if(!toCloudletHost.contains(toCloudletKey)) {
			// add for client
			Rule r1 = new Rule();
			r1.setSwitchStr(r.getSwitchId());
			r1.setName(String.valueOf(flowId.getAndIncrement()));
			r1.setIpv4_dst(ipv4_dst);
			r1.setEth_type("0x0800");

			Actions a1 = new Actions();
			a1.setSet_eth_dst(PropertyReader.getInstance().getCloudMac(cloudname));
			a1.setSet_ipv4_dst(PropertyReader.getInstance().getCloudIp(cloudname));
			a1.setOutput(PropertyReader.getInstance().getCloudSwitchPort(cloudname));
			r1.setActions(a1.toString());
			
			ControllerResponse response = postXml(PropertyReader.getInstance().getControllerURL(),
					gson.toJson(r1));
			
			if(response.getStatus().equalsIgnoreCase(OK_FROM_CONTROLLER)){
				System.out.println(session + "Addet to toCloudletHost: "+toCloudletKey);
				toCloudletHost.add(toCloudletKey);
			}
		} else {
			System.out.println(session + "Rule for switch "+ r.getSwitchId() +" and ipv4_dst "+ toCloudletKey + " already added.");
		}

		String cloudSwitchPort = PropertyReader.getInstance().getCloudSwitchPort(cloudname);
		String toClientKey = getToClientKey(r.getSwitchId(), cloudSwitchPort, r.getClientMAC());
		
		if(!toClientHost.contains(toClientKey)) {
			// reverse rule
			Rule r2 = new Rule();
			r2.setSwitchStr(r.getSwitchId());
			r2.setName(String.valueOf(flowId.getAndIncrement()));
			r2.setIn_port(cloudSwitchPort);
			r2.setEth_dst(r.getClientMAC());

			Actions a2 = new Actions();
			a2.setSet_eth_src(PropertyReader.getInstance().getNatMac());
			a2.setSet_ipv4_src(ipv4_dst);
			a2.setOutput(r.getIngressPort());

			r2.setActions(a2.toString());

			ControllerResponse response = postXml(PropertyReader.getInstance().getControllerURL(),
					gson.toJson(r2));
			
			if(response.getStatus().equalsIgnoreCase(OK_FROM_CONTROLLER)){
				System.out.println(session + "Addet to toClientHost: "+toClientKey);
				toClientHost.add(toClientKey);
			} 
		} else {
			System.out.println(session + "Rule for switch "+ r.getSwitchId() +", ingressport "+cloudSwitchPort+" and eth_dst "+ toCloudletKey + " already added.");
		}
		
		System.out.println(session + "================================================");
	}
	
	private static String getToCloudletKey(String ipv4_dst, String switchId) {
		return new StringBuffer().append(ipv4_dst).append(switchId).toString();
	}
	
	private static String getToClientKey(String switchId, String inPort, String eth_dst) {
		return new StringBuffer().append(switchId).append(inPort).append(eth_dst).toString();
	}

	private static String getMatchingCloud(String query) {
		List<String> clouds = PropertyReader.getInstance().getClouds();
		String cloudEndpoint = query.trim();
		for (String cloud : clouds) {
			if (cloudEndpoint.equalsIgnoreCase(cloud)) {
				return cloud;
			}
		}
		System.out.println(query + " does not exist in local network.");
		return null;
	}

	private static ControllerResponse postXml(String URL, String json) {
		try {
			System.out.println("POSTING " + json + " to " + URL);
			
			URL url = new URL(URL);
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream(),"utf-8");

			out.write(json);
			out.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			ControllerResponse cresp=null;
			String response = null;
			while ((response = in.readLine()) != null) {
				System.out.println(response);
				Gson gson = new GsonBuilder().create();
				cresp = gson.fromJson(response, ControllerResponse.class);
			}
			
			
			in.close();
			
			return cresp;
			
		} catch (Throwable e) {
			e.printStackTrace();
			
		}
		return null;
	}
}