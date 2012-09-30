package de.isnow.stypi;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class StypeFileFilter {
	private Pattern filter;
	
	public StypeFileFilter (String filterExpression) {
		filter = Pattern.compile(filterExpression);
	}
	
	public boolean include (String path) {
		return filter.matcher(path).matches();
	}
	
	public Map<String, URI> filterURIs (Map<String, URI> inputs) {
		Map<String, URI> outputs = new HashMap<String, URI>();
		for (String key: inputs.keySet()) {
			URI file = inputs.get(key);
			if (include(file.toString())) {
				outputs.put(key, file);
			}
		}
		return outputs;
	} 

}
