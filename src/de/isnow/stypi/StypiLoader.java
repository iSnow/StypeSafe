package de.isnow.stypi;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import de.isnow.stypi.logging.LoggingSetup;

public class StypiLoader {
	private DefaultHttpClient client;

	public StypiLoader(DefaultHttpClient client) {
		this.client = client;
	}

	public Document getDocument (URI uri, String token) throws Exception {
		HttpGet get = new HttpGet (uri);
		get.setHeader("Accept", "*/*");
		get.setHeader("X-CSRF-Token", token);
		get.setHeader("X-Requested-With", "XMLHttpRequest");
		HttpResponse response = client.execute(get);
		HttpEntity responseEntity = response.getEntity();
		InputStream st = responseEntity.getContent();
		String content =  LoggingSetup.readAndLog (st, uri.getPath().replaceAll("/", "_")+"_log.txt");
		return Jsoup.parse(content);
	}
}
