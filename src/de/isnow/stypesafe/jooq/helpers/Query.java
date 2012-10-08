package de.isnow.stypesafe.jooq.helpers;

import static de.isnow.stypesafe.jooq.Tables.DOCUMENT;
import static de.isnow.stypesafe.jooq.Tables.DOCUMENTVERSION;

import org.jooq.tools.unsigned.UInteger;

import de.isnow.stypesafe.jooq.StypesafeFactory;
import de.isnow.stypesafe.jooq.tables.records.DocumentRecord;
import de.isnow.stypesafe.jooq.tables.records.DocumentversionRecord;

public class Query {
	public static DocumentRecord fetchDocumentByStypiId (StypesafeFactory fac, String stypiId) {
		return (DocumentRecord)fac
			.select()
			.from(DOCUMENT)
			.where(DOCUMENT.STYPIUID.equal(stypiId))
			.fetchOne();
	}
	
	public static DocumentversionRecord fetchVersionByVersionNumber (StypesafeFactory fac, UInteger docId, UInteger versionNum) {
		return (DocumentversionRecord)fac.select()
			.from(DOCUMENTVERSION)
			.where(DOCUMENTVERSION.FK_DOCUMENT.equal(docId))
			.and(DOCUMENTVERSION.VERSION.equal(versionNum))
			.fetchOne();
	}
	
	public static DocumentversionRecord fetchVersionByFingerprint (StypesafeFactory fac, UInteger docId, String fingerPrint) {
		return (DocumentversionRecord)fac.select()
			.from(DOCUMENTVERSION)
			.where(DOCUMENTVERSION.FK_DOCUMENT.equal(docId))
			.and(DOCUMENTVERSION.FINGERPRINT.equal(fingerPrint))
			.fetchOne();
	}

}
