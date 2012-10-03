package de.isnow.stypi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import de.isnow.stypi.data.StypiDocument;
import de.isnow.stypi.data.StypiDocumentVersion;

//FIXME change name - this is not a Writer 
public class StypiWriter {
	private File outDir;
	
	public StypiWriter (File outDir) {
		this.outDir = outDir;
	}
	
	public int writeDocument (StypiDocument doc) throws IOException {
		int retval = 0;
		for (StypiDocumentVersion version : doc.getVersions()) {
			File uIdDir = new File (outDir, doc.stypiId);
			File versionFile = new File (uIdDir, version.version+".md");
			if (!versionFile.exists()) {
				retval++;
				FileOutputStream fos = new FileOutputStream(versionFile);
				OutputStreamWriter ow = new OutputStreamWriter(fos, GLOBALS.CHARSETNAME);
				BufferedWriter writer = new BufferedWriter(ow);
				writer.write(version.getText());
				writer.close();
			}
		}
		return retval;
	}
}
