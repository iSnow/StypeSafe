package de.isnow.stypi;

import static de.isnow.stypesafe.jooq.Tables.DOCUMENT;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.ini4j.Wini;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jsoup.nodes.Document;
import org.junit.Assert;

import de.isnow.stypesafe.jooq.StypesafeFactory;
import de.isnow.stypesafe.jooq.Tables;
import de.isnow.stypesafe.jooq.tables.Documentversion;
import de.isnow.stypesafe.jooq.tables.records.DocumentRecord;
import de.isnow.stypesafe.jooq.tables.records.DocumentversionRecord;
import de.isnow.stypesafe.jooq.tables.records.ErrorRecord;
import de.isnow.stypi.data.StypiDocument;
import de.isnow.stypi.data.StypiDocumentVersion;
import de.isnow.stypi.data.StypiSiteMap;
import de.isnow.stypi.helpers.NetworkHelper;
import de.isnow.stypi.logging.LoggingSetup;

public class StypeSafe {
	private Logger log;

	public static void main (String[] argv) {
		DefaultHttpClient client = null;
		try {
			StypeSafe stypi = new StypeSafe();
			Map<String, String> config = readConfig(argv);
			
			Connection conn = stypi.connectToDatabase (config);
			StypesafeFactory fac = new StypesafeFactory(conn);
			File outDir = new File (config.get("outDir"));
			Assert.assertTrue("Output dir does not exist: "+outDir.getAbsolutePath(), outDir.exists());
			Assert.assertTrue("Output dir is plain file: "+outDir.getAbsolutePath(), outDir.isDirectory());
			client = NetworkHelper.getHttpClient();
			Statistics stats = new Statistics();
			StypiLoader loader = new StypiLoader(client);
			StypiDocumentParser parser = new StypiDocumentParser();
			stypi.log = new LoggingSetup("log.txt").getLogger();
			StypiWriter wr = new StypiWriter(outDir);
			StypiLogin lg = new StypiLogin(GLOBALS.BASEURL, client, stypi.log);
			StypeFileFilter filter = new StypeFileFilter(".*/kathrin/mecodepretty/.*");
			
			if (!lg.postLogin(config.get("login"), config.get("password"))) {
				System.err.println ("Error logging in");
				System.exit (-1);
			}
			String zipName = "mecodepretty_"+new Date().getTime() +".zip";
			ZipOutputStream out = openZip(zipName, config);
			Map<String, URI> files = filter.filterURIs(lg.parseFileTree());
			StypiSiteMap sitemap = new StypiSiteMap(outDir);
			sitemap.setZipFilename(zipName);
			for (String key: files.keySet()) {
				File uIdDir = new File (outDir, key);
				if (!uIdDir.exists()) {
					Assert.assertTrue("Could not create UID Dir: "+uIdDir.getAbsolutePath(), uIdDir.mkdir());
				}
				Document jSoupDoc = loader.getDocument(new URI (GLOBALS.BASEURL+key), lg.getAuthToken());
				StypiDocument stypiDoc = parser.parseDocument (jSoupDoc, key);
				
				StypiDocumentVersion ver = stypiDoc.getLatestVersion();
				if (null != ver) {
					ZipEntry e = new ZipEntry(stypiDoc.humanReadableUrl);
					out.putNextEntry(e);
		
					byte[] data = ver.getText().getBytes();
					out.write(data, 0, data.length);
					out.closeEntry();
				}
				
				DocumentRecord result = (DocumentRecord)fac.select().from(DOCUMENT).where(DOCUMENT.STYPIUID.equal(key)).fetchOne();
				if (null == result) {
					result = new DocumentRecord(stypiDoc);
				}
				sitemap.addDocument (stypiDoc);
				int numNewVersions = wr.writeDocument(stypiDoc);
				stats.log (key, numNewVersions);
				if (null == result.getId()) {
					InsertSetStep<DocumentRecord> s = fac.insertInto(Tables.DOCUMENT);
					InsertSetMoreStep<DocumentRecord> sm = result.prepareInsert(s);
					sm.execute();
					result = (DocumentRecord)fac.select().from(DOCUMENT).where(DOCUMENT.STYPIUID.equal(key)).fetchOne();
				}
				if (result.getId() != null) {
					List<DocumentversionRecord> versionList = result.fetchVersions();
					Map<Integer, DocumentversionRecord> versions = new TreeMap<Integer, DocumentversionRecord>();
					for (DocumentversionRecord r : versionList) {
						versions.put(r.getValueAsInteger(Documentversion.DOCUMENTVERSION.VERSION), r);
					}
					for (StypiDocumentVersion v : stypiDoc.getVersions()) {
						if (!versions.containsKey(v.version)) {
							DocumentversionRecord drv = new DocumentversionRecord(v, result.getId());
							versions.put(v.version, drv);
						}
					}
				}
				/*for (result.) {
					if (null == entry.getValue().getId()) {
						//
					}
				}*/
			}	
			out.close();
			File sitemapFile = new File (outDir, "sitemap.json");
			Writer writer = new BufferedWriter (new FileWriter (sitemapFile)); 
			writer.write(sitemap.toJson());
			writer.close();
	
			lg.postLogout ();
			client.getConnectionManager().shutdown(); // Close the instance here
		} catch (Throwable ex) {
			ex.printStackTrace();
			ErrorRecord e = new ErrorRecord();
			e.setError(Arrays.toString(ex.getStackTrace()));
			e.into(Tables.ERROR);
		}
		finally {
			client.getConnectionManager().shutdown(); // Close the instance here
		}
	}
	
	
	private static Map<String, String> readConfig(String[] argv) throws Exception {
		Map<String, String> config = new HashMap<String, String>();
		Wini ini = new Wini(new File("config.txt"));
		config.put("login", ini.get("stypi", "login"));
		config.put("password", ini.get("stypi", "password"));
		config.put("outDir", ini.get("stypi", "outDir"));
		config.put("archiveDir", ini.get("stypi", "archiveDir"));
		
		config.put("dbhost", ini.get("database", "dbhost"));
		config.put("dbname", ini.get("database", "dbname"));
		config.put("dbport", ini.get("database", "dbport"));
		config.put("dbuser", ini.get("database", "dbuser"));
		config.put("dbpass", ini.get("database", "dbpass"));
		
		for (String arg : argv) {
			if (arg.startsWith("-login=")) {
				config.put("login", arg.substring(7));
			} else if (arg.startsWith("-pass=")) {
				config.put("password", arg.substring(6));
			} else if (arg.startsWith("-out=")) {
				config.put("outDir", arg.substring(5).replaceAll("\"", ""));
			} else if (arg.startsWith("-arch=")) {
				config.put("archiveDir", arg.substring(6).replaceAll("\"", ""));
			} else if (arg.startsWith("-dbhost=")) {
				config.put("dbhost", arg.substring(8));
			} else if (arg.startsWith("-dbname=")) {
				config.put("dbname", arg.substring(8));
			} else if (arg.startsWith("-dbport=")) {
				config.put("dbport", arg.substring(8));
			} else if (arg.startsWith("-dbuser=")) {
				config.put("dbuser", arg.substring(8));
			} else if (arg.startsWith("-dbpass=")) {
				config.put("dbpass", arg.substring(8));
			}	
		}
		return config;
	}

	private static ZipOutputStream openZip(String zipName, Map<String, String> config) throws FileNotFoundException {
		File zipDir = new File (config.get("archiveDir"));
		if (!zipDir.exists()) {
			zipDir.mkdirs();
		}
		File f = new File(zipDir, zipName);
		return new ZipOutputStream(new FileOutputStream(f));
	}

	private Connection connectToDatabase (Map<String, String> config) {
		Connection conn = null;

		String userName = config.get("dbuser");
		String password = config.get("dbpass");
		String url = "jdbc:mysql://"+config.get("dbhost")+":"+config.get("dbport")+"/"+config.get("dbname");

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, userName, password);
		} catch (Exception e) {
			e.printStackTrace();
			disconnectFromDatabase(conn);
			System.exit(-1);
		} 
		return conn;
	}

	private void disconnectFromDatabase (Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException ignore) {
			}
		}
	}
}
