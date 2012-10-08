package de.isnow.stypi.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashSet;
import java.util.Set;

import de.isnow.stypi.helpers.UnicodeFormatter;

public class StypiDocumentVersion {
	public Integer version;
	public String username;
	public String fingerprint;
	public Set<StypiDocumentFragment> fragments = new LinkedHashSet<StypiDocumentFragment>();
	
	public void addFragment (StypiDocumentFragment fragment) {
		fragments.add(fragment);
	}
	
	public String getText () {
		StringBuilder b = new StringBuilder();
		for (StypiDocumentFragment f : fragments) {
			b.append(f.text);
		}
		return b.toString();
	}
	
	public String toString () {
		String result = "VERSION: "+version+"\n";
		result += "EDITOR: "+username+"\n";
		result += "CONTENT: "+getText()+"\n";
		return result;
	}

	public void generateFingerprint () {
		MessageDigest digester;
		try {
			digester = MessageDigest.getInstance("MD5");
			digester.update(getText().getBytes()); 
			fingerprint = UnicodeFormatter.bytesToString(digester.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
