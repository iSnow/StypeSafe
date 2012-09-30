package de.isnow.stypi.data;

import java.io.File;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import de.isnow.stypi.helpers.FileHelpers;
import de.isnow.stypi.helpers.FileHelpers.FileTuple;

public class StypiSiteMap {
	private File outDir = null;
	private String zipFilename = null;
	private JsonObject sm = null;
	private Set<StypiDocument> docs = new TreeSet<StypiDocument>();
	private FileHelpers fHelpers = new FileHelpers();

	public StypiSiteMap(File outDir) {
		this.outDir = outDir;
	}
	
	public void setZipFilename (String name) {
		zipFilename = name;
	} 

	public void addDocument(StypiDocument doc) {
		docs.add(doc);
	}

	public String toJson() {
		if (null == sm) {
			sm = new JsonObject();
			JsonArray files = new JsonArray();
			sm.addProperty("lastScan", new Date().getTime());
			if (null != zipFilename) {
				sm.addProperty ("zipFilename", zipFilename);
			}
			sm.add("files", files);
			for (StypiDocument doc: docs) {
				JsonObject file = new JsonObject();
				file.addProperty("uid", doc.stypiId);
				file.addProperty("visibleUrl", doc.humanReadableUrl);
				file.addProperty("title", doc.title);
				JsonObject history = new JsonObject();
				File uIdDir = new File (outDir, doc.stypiId);
				Set<FileTuple> children = fHelpers.listVersions(doc.stypiId, uIdDir);
				long ts = 0;
				int id = 0;
				JsonArray docVersions = new JsonArray();
				for (FileTuple child: children) {
					docVersions.add(new JsonPrimitive(child.version));
					if (child.file.lastModified() > ts) {
						id = child.version;
						ts = child.file.lastModified();
					}
				}
				history.addProperty("number", children.size());
				history.addProperty("latest", id);
				history.addProperty("time", ts);
				history.add("versions", docVersions);
				file.add("history", history);
				files.add(file);
			}
		}
		return sm.toString();
	}
}
