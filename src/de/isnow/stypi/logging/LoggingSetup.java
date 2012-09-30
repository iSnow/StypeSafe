package de.isnow.stypi.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LoggingSetup {
	private Logger log = null;
	private String fileName;
	private static Level logLevel = Level.ERROR;
	
	public LoggingSetup(String fileName) {
		this.fileName = fileName;
		File logFile = new File (fileName);
		if (logFile.exists()) {
			logFile.delete();
		}
		// Configure Logging
		String logging = "org.apache.commons.logging";
		/*System.setProperty(logging + ".Log", logging + ".impl.SimpleLog");
		System.setProperty(logging + ".logging.simplelog.showdatetime", "true");
		System.setProperty(logging + ".simplelog.log.httpclient.wire", "debug");        
		System.setProperty(logging + ".simplelog.log.org.apache.commons.httpclient", "debug");*/
	}
	
	public Logger getLogger () {
		if (null != log) {
			return log;
		}
		try {
		 	BasicConfigurator.configure();
		 } catch (Exception e) {
		 }
		log = Logger.getLogger("com.stypi");
		log.setLevel(logLevel);
		try {
			Logger.getRootLogger().removeAllAppenders();
			Logger.getRootLogger().addAppender(new FileAppender (new StypiLayout(), fileName));
			log.addAppender(new FileAppender (new StypiLayout(), fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return log;
	}
	
	public static String readAndLog (InputStream stream, String logfileName) throws IOException {
		Writer writer = null;
		if (logLevel == Level.ALL) {
			writer = new FileWriter (logfileName);
		}
		StringBuffer buf = new StringBuffer();
		int curChar = stream.read();
		while (-1 != curChar) {
			if (null != writer) {
				writer.write((char)curChar);
			}
			buf.append((char)curChar);
			curChar = stream.read();
		}
		stream.close();
		if (null != writer) {
			writer.close(); 
		}
		return buf.toString();
	}


	public static String readAndLog (Reader reader, String logfileName) throws IOException {
		Writer writer = null;
		if (logLevel == Level.ALL) {
			writer = new FileWriter (logfileName);
		}
		StringBuffer buf = new StringBuffer();
		int curChar = reader.read();
		while (-1 != curChar) {
			if (null != writer) {
				writer.write((char)curChar);
			}
			buf.append((char)curChar);
			curChar = reader.read();
		}
		reader.close();
		if (null != writer) {
			writer.close(); 
		}
		return buf.toString();
	}

}
