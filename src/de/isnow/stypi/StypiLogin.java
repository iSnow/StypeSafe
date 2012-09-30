package de.isnow.stypi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.isnow.stypi.logging.LoggingSetup;


public class StypiLogin {
	private DefaultHttpClient client;
	//private Gson gson = new Gson();
	private JsonParser jsonParser = new JsonParser();
	private Logger log = null;
	private String token = null;
	private String site;
	
	public StypiLogin (String site, DefaultHttpClient client, Logger log) {
		this.client = client;
		this.log = log;
		this.site = site;
	}
	
	public String getAuthToken () {
		if (null != token) {
			return token;
		}
		HttpGet getReq = new HttpGet (site);
		try {
			HttpResponse response = client.execute(getReq);
			BufferedReader reader = new BufferedReader (new InputStreamReader (response.getEntity().getContent()));
			String content = LoggingSetup.readAndLog(reader, "authtokenlog.txt");
			reader.close();
			if (response.getEntity() != null ) {
				response.getEntity().consumeContent();
			}
			Document doc = Jsoup.parse(content);
			Element input = doc.select("input[name=authenticity_token]").first();
			if (null != input) {
				token = input.val();
				return token;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	//authenticity_token=Nu7n3y8qvafW8XLRSn0ISOD4AxfdwjnU09siUynLPig%3D&email=iSnow&password=atx1buy2&referer=https%3A%2F%2Fwww.stypi.com%2F

	public boolean postLogin (String email, String password) throws IOException {	
		if (null == token) {
			getAuthToken();
		}
		Assert.assertNotNull ("Auth token must not be null", token);
		boolean loggedIn = false;
	    HttpContext localContext = new BasicHttpContext();
		HttpPost post = new HttpPost(site+"users/login");
		
	    BasicNameValuePair[] params = {
	            new BasicNameValuePair("authenticity_token", token),
	            new BasicNameValuePair("email", email),
	            new BasicNameValuePair("password", password),
	            new BasicNameValuePair("referer", site)      
	    };
	    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(Arrays.asList(params));
	    entity.setContentEncoding(GLOBALS.CHARSETNAME);
		post.setEntity(entity);
		post.setHeader("Accept", "*/*");
		post.setHeader("X-CSRF-Token", token);
		post.setHeader("X-Requested-With", "XMLHttpRequest");

		HttpResponse response = client.execute(post, localContext);
		String answer = LoggingSetup.readAndLog(new BufferedReader (new InputStreamReader (response.getEntity().getContent())), "login.txt");

		JsonObject obj = jsonParser.parse(answer).getAsJsonObject();
		loggedIn = obj.getAsJsonPrimitive("success").getAsBoolean();
		
		if (response.getEntity() != null ) {
			response.getEntity().consumeContent();
		}
		return loggedIn;
	}
	
	public Document getInitalDoc () throws Exception {
		Assert.assertNotNull ("Auth token must not be null", token);
		HttpGet get = new HttpGet (site);
		get.setHeader("Accept", "*/*");
		get.setHeader("X-CSRF-Token", token);
		get.setHeader("X-Requested-With", "XMLHttpRequest");
		HttpResponse response = client.execute(get);
		HttpEntity responseEntity = response.getEntity();
		InputStream st = responseEntity.getContent();
		String content =  LoggingSetup.readAndLog (st, "initialdoc.txt");
		return Jsoup.parse(content);
	}
	
	public Map<String, URI> parseFileTree (Document html) throws Exception {
		Elements files = html.select("li.filetree-node a");
		Map<String, URI> docs = new HashMap<String, URI>();
		for (Element file : files) {
			String path = file.attr("href");
			String uid = file.attr("data-token");
			URI uri = new URI (site+path);
			docs.put(uid, uri);
		}
		return docs;
	}
	
	public Map<String, URI> parseFileTree () throws Exception {
		return parseFileTree(getInitalDoc ());
	}
	
	public void postLogout () throws Exception {
		Assert.assertNotNull ("Auth token must not be null", token);
	    HttpContext localContext = new BasicHttpContext();
		HttpPost post = new HttpPost(site+"users/logout");
	    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, GLOBALS.CHARSET);
	    entity.addPart("authenticity_token", new StringBody(token, GLOBALS.CHARSET)); 
	    entity.addPart("referrer", new StringBody(site, GLOBALS.CHARSET));  
		post.setEntity(entity);
		HttpResponse response = client.execute(post, localContext);
		BufferedReader reader = new BufferedReader (new InputStreamReader (response.getEntity().getContent()));
		LoggingSetup.readAndLog(reader, "logout.txt");
		reader.close();
		
		if (response.getEntity() != null ) {
			response.getEntity().consumeContent();
		}
	}
}
