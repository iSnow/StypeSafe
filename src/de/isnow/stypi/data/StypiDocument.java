package de.isnow.stypi.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

@Entity
public class StypiDocument implements Comparable<StypiDocument> {
	public String stypiId = null;
	public String humanReadableUrl = null;
	public String title = null;
	
	private Set<StypiDocumentVersion> versions = new HashSet<StypiDocumentVersion>();


	public Set<StypiDocumentVersion> getVersions() {
		return versions;
	}

	public void addVersion (StypiDocumentVersion version) {
		versions.add(version);
	}
	
	public StypiDocumentVersion getLatestVersion () {
		int verNum = 0;
		StypiDocumentVersion outVal = null;
		for (StypiDocumentVersion version : versions) {
			if (version.version > verNum) {
				verNum = version.version;
				outVal = version;
			}
		}
		return outVal;
	}

	@Override
	public int compareTo(StypiDocument arg0) {
		return this.title.compareToIgnoreCase(((StypiDocument)arg0).title);
	}
}
