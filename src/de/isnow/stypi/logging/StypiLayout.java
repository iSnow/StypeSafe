package de.isnow.stypi.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.helpers.DateLayout;
import org.apache.log4j.spi.LoggingEvent;

public class StypiLayout extends DateLayout {
	private static final SimpleDateFormat df = new SimpleDateFormat ("HH:mm:ss");

	@Override
	public String format(LoggingEvent arg0) {
		return df.format(arg0.getTimeStamp())+" - "+arg0.getRenderedMessage().replace("[\r][\n]","")+"\n";
	}

	@Override
	public boolean ignoresThrowable() {
		// TODO Auto-generated method stub
		return false;
	}

}
