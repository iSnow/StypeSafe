package de.isnow.stypi;

import java.io.StringReader;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import de.isnow.stypi.data.StypiDocument;
import de.isnow.stypi.data.StypiDocumentFragment;
import de.isnow.stypi.data.StypiDocumentVersion;

public class StypiDocumentParser {
	private JsonParser jsonParser = new JsonParser();
	
	private String getVisibleUrl (Document html, String targetId) {
		Elements files = html.select("li.filetree-node a");
		for (Element file : files) {
			String uid = file.attr("data-token");
			if (uid.equals(targetId)) {
				return file.attr("href");
			}
		}
		return null;
	}
	
	private String getTitle (Document html) {
		Elements title = html.select("title");
		return title.text();
	}
	

	public StypiDocument parseDocument(Document html, StypiDocument doc, String uId) {
		doc.humanReadableUrl = getVisibleUrl (html, uId);
		doc.title = getTitle (html);
		Elements scripts = html.select("script");
		for (Element script : scripts) {
			String content = script.html();
			int pos = content.indexOf("var Stypi =");
			if (pos != -1) {
				doc.stypiId = uId;
				content = content.substring(pos + 12);
				content = content.substring(0, content.length() - 8);
				JsonReader rdr = new JsonReader(new StringReader (content));
				rdr.setLenient(true);
				JsonObject obj = jsonParser.parse(rdr).getAsJsonObject();
				JsonObject configs = obj.getAsJsonObject("configs");
				JsonObject d = configs.getAsJsonObject("head");
				JsonArray deltas = d.getAsJsonArray("deltas");
				String version = configs.getAsJsonPrimitive("version").getAsString();
				String username = configs.getAsJsonPrimitive("username").getAsString();
				StypiDocumentVersion versionObj = new StypiDocumentVersion();
				versionObj.version = Integer.parseInt(version);
				versionObj.username = username;
				doc.addVersion (versionObj);
				for (int i = 0; i < deltas.size(); i++) {
					StypiDocumentFragment f = new StypiDocumentFragment();
					JsonObject delta = deltas.get(i).getAsJsonObject();
					f.text = delta.getAsJsonPrimitive("text").getAsString();
					JsonObject attrs = delta.getAsJsonObject("attributes");
					f.authorId = attrs.getAsJsonPrimitive("authorId").getAsString();
					versionObj.addFragment(f);
				}
				versionObj.generateFingerprint();
			}
		}
		return doc;
	}
	
	public StypiDocument parseDocument (Document html, String uId) {
		StypiDocument doc = new StypiDocument();
		return parseDocument (html, doc, uId);
	}

	
	/*public List<StypiDocumentVersion> parseDocument (Document html) {
		List<StypiDocumentVersion> versions = null;
		Elements scripts = html.select("script");
		for (Element script : scripts) {
			String content = script.html();
			int pos = content.indexOf("var Stypi =");
			if (pos != -1) {
				versions = new ArrayList<StypiDocumentVersion>();
				content = content.substring(pos + 12);
				content = content.substring(0, content.length() - 8);
				JsonReader rdr = new JsonReader(new StringReader (content));
				rdr.setLenient(true);
				//JsonObject obj = jsonParser.parse(content).getAsJsonObject();
				JsonObject obj = jsonParser.parse(rdr).getAsJsonObject();
				JsonObject configs = obj.getAsJsonObject("configs");
				//String url = configs.getAsJsonPrimitive("url").getAsString();
				JsonObject d = configs.getAsJsonObject("head");
				JsonArray deltas = d.getAsJsonArray("deltas");
				String version = configs.getAsJsonPrimitive("version").getAsString();
				String username = configs.getAsJsonPrimitive("username").getAsString();
				for (int i = 0; i < deltas.size(); i++) {
					StypiDocumentVersion versionObj = new StypiDocumentVersion();
					//versionObj.url = url;
					versionObj.version = Integer.parseInt(version);
					versionObj.username = username;
					JsonObject delta = deltas.get(i).getAsJsonObject();
					versionObj.text = delta.getAsJsonPrimitive("text").getAsString();
					versions.add(versionObj);
				}
			}
		}
		return versions;
	}*/
}
