package de.isnow.stypi.helpers;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;
import java.util.TreeSet;

public class FileHelpers {
	
	public class FileTuple implements Comparable {
		public File file;
		public Integer version;
		
		@Override
		public int compareTo(Object arg0) {
			if (!(arg0 instanceof FileTuple)) {
				return 0;
			}
			return ((FileTuple)arg0).version - version;
		}
	}
	
	public Set<FileTuple> listVersions (String stypiUid, File outDir) {
		Set<FileTuple> result = new TreeSet<FileTuple>();
		File[] versionFiles = outDir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File arg0) {
				return (!(arg0.getName().startsWith(".")));
			}}
		);
		
		for (File v : versionFiles) {
			FileTuple t = new FileTuple();
			t.file = v;
			String name = v.getName();
			int pos = name.lastIndexOf(".");
			if (-1 != pos) {
				name = name.substring(0, pos);
			}
			t.version = Integer.parseInt (name);
			result.add(t);
		}
		return result;
	}

	
}
