package de.isnow.stypi.data;

import java.util.LinkedHashSet;
import java.util.Set;

public class StypiDocumentVersion {
	public Integer version;
	public String username;
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
}
