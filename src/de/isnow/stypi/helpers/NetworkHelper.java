package de.isnow.stypi.helpers;

import java.nio.charset.Charset;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import de.isnow.stypi.GLOBALS;

public class NetworkHelper {


	public static DefaultHttpClient getHttpClient () {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		int timeoutConnection = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 20000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		httpParameters.setParameter( "http.useragent", GLOBALS.USERAGENT);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		httpclient.getParams().setParameter("http.protocol.handle-redirects",false);
		httpclient.getParams().setParameter("http.protocol.content-charset", GLOBALS.CHARSETNAME); 
		HttpConnectionParams.setSoTimeout(httpclient.getParams(), 30000);
		HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 30000); 
		
		CookieStore cookieStore = new BasicCookieStore(); 
		httpclient.setCookieStore(cookieStore); 
		return httpclient;
	}
}
