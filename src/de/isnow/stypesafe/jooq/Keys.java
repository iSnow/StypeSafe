/**
 * This class is generated by jOOQ
 */
package de.isnow.stypesafe.jooq;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;
import org.jooq.tools.unsigned.UInteger;

import de.isnow.stypesafe.jooq.tables.Author;
import de.isnow.stypesafe.jooq.tables.Document;
import de.isnow.stypesafe.jooq.tables.Documentfragment;
import de.isnow.stypesafe.jooq.tables.Documentversion;
import de.isnow.stypesafe.jooq.tables.Error;
import de.isnow.stypesafe.jooq.tables.records.AuthorRecord;
import de.isnow.stypesafe.jooq.tables.records.DocumentRecord;
import de.isnow.stypesafe.jooq.tables.records.DocumentfragmentRecord;
import de.isnow.stypesafe.jooq.tables.records.DocumentversionRecord;
import de.isnow.stypesafe.jooq.tables.records.ErrorRecord;

/**
 * This class is generated by jOOQ.
 *
 * A class modelling foreign key relationships between tables of the <code>stypesafe</code> 
 * schema
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.4.2"},
                            comments = "This class is generated by jOOQ")
public class Keys {

	// IDENTITY definitions
	public static final Identity<AuthorRecord, UInteger> IDENTITY_AUTHOR = Identities0.IDENTITY_AUTHOR;
	public static final Identity<DocumentRecord, UInteger> IDENTITY_DOCUMENT = Identities0.IDENTITY_DOCUMENT;
	public static final Identity<DocumentfragmentRecord, UInteger> IDENTITY_DOCUMENTFRAGMENT = Identities0.IDENTITY_DOCUMENTFRAGMENT;
	public static final Identity<DocumentversionRecord, UInteger> IDENTITY_DOCUMENTVERSION = Identities0.IDENTITY_DOCUMENTVERSION;
	public static final Identity<ErrorRecord, UInteger> IDENTITY_ERROR = Identities0.IDENTITY_ERROR;

	// UNIQUE and PRIMARY KEY definitions
	public static final UniqueKey<AuthorRecord> KEY_AUTHOR_PRIMARY = UniqueKeys0.KEY_AUTHOR_PRIMARY;
	public static final UniqueKey<DocumentRecord> KEY_DOCUMENT_PRIMARY = UniqueKeys0.KEY_DOCUMENT_PRIMARY;
	public static final UniqueKey<DocumentfragmentRecord> KEY_DOCUMENTFRAGMENT_PRIMARY = UniqueKeys0.KEY_DOCUMENTFRAGMENT_PRIMARY;
	public static final UniqueKey<DocumentversionRecord> KEY_DOCUMENTVERSION_PRIMARY = UniqueKeys0.KEY_DOCUMENTVERSION_PRIMARY;
	public static final UniqueKey<ErrorRecord> KEY_ERROR_PRIMARY = UniqueKeys0.KEY_ERROR_PRIMARY;

	// FOREIGN KEY definitions
	public static final ForeignKey<DocumentRecord, DocumentRecord> DOCUMENT_HISTORY = ForeignKeys0.DOCUMENT_HISTORY;
	public static final ForeignKey<DocumentfragmentRecord, DocumentversionRecord> FRAGMENT_VERSION = ForeignKeys0.FRAGMENT_VERSION;
	public static final ForeignKey<DocumentfragmentRecord, AuthorRecord> FRAGMENT_AUTHOR = ForeignKeys0.FRAGMENT_AUTHOR;
	public static final ForeignKey<DocumentversionRecord, DocumentRecord> DOCUMENT_VERSION = ForeignKeys0.DOCUMENT_VERSION;

	/**
	 * No instances
	 */
	private Keys() {}

	private static class Identities0 extends AbstractKeys {
		public static Identity<AuthorRecord, UInteger> IDENTITY_AUTHOR = createIdentity(Author.AUTHOR, Author.AUTHOR.ID);
		public static Identity<DocumentRecord, UInteger> IDENTITY_DOCUMENT = createIdentity(Document.DOCUMENT, Document.DOCUMENT.ID);
		public static Identity<DocumentfragmentRecord, UInteger> IDENTITY_DOCUMENTFRAGMENT = createIdentity(Documentfragment.DOCUMENTFRAGMENT, Documentfragment.DOCUMENTFRAGMENT.ID);
		public static Identity<DocumentversionRecord, UInteger> IDENTITY_DOCUMENTVERSION = createIdentity(Documentversion.DOCUMENTVERSION, Documentversion.DOCUMENTVERSION.ID);
		public static Identity<ErrorRecord, UInteger> IDENTITY_ERROR = createIdentity(Error.ERROR, Error.ERROR.ID);
	}

	@SuppressWarnings({"unchecked"})
	private static class UniqueKeys0 extends AbstractKeys {
		public static final UniqueKey<AuthorRecord> KEY_AUTHOR_PRIMARY = createUniqueKey(Author.AUTHOR, Author.AUTHOR.ID);
		public static final UniqueKey<DocumentRecord> KEY_DOCUMENT_PRIMARY = createUniqueKey(Document.DOCUMENT, Document.DOCUMENT.ID);
		public static final UniqueKey<DocumentfragmentRecord> KEY_DOCUMENTFRAGMENT_PRIMARY = createUniqueKey(Documentfragment.DOCUMENTFRAGMENT, Documentfragment.DOCUMENTFRAGMENT.ID);
		public static final UniqueKey<DocumentversionRecord> KEY_DOCUMENTVERSION_PRIMARY = createUniqueKey(Documentversion.DOCUMENTVERSION, Documentversion.DOCUMENTVERSION.ID);
		public static final UniqueKey<ErrorRecord> KEY_ERROR_PRIMARY = createUniqueKey(Error.ERROR, Error.ERROR.ID);
	}

	@SuppressWarnings({"unchecked"})
	private static class ForeignKeys0 extends AbstractKeys {
		public static final ForeignKey<DocumentRecord, DocumentRecord> DOCUMENT_HISTORY = createForeignKey(Keys.KEY_DOCUMENT_PRIMARY, Document.DOCUMENT, Document.DOCUMENT.BEFORE_RENAME);
		public static final ForeignKey<DocumentfragmentRecord, DocumentversionRecord> FRAGMENT_VERSION = createForeignKey(Keys.KEY_DOCUMENTVERSION_PRIMARY, Documentfragment.DOCUMENTFRAGMENT, Documentfragment.DOCUMENTFRAGMENT.FK_VERSION);
		public static final ForeignKey<DocumentfragmentRecord, AuthorRecord> FRAGMENT_AUTHOR = createForeignKey(Keys.KEY_AUTHOR_PRIMARY, Documentfragment.DOCUMENTFRAGMENT, Documentfragment.DOCUMENTFRAGMENT.FK_AUTHOR);
		public static final ForeignKey<DocumentversionRecord, DocumentRecord> DOCUMENT_VERSION = createForeignKey(Keys.KEY_DOCUMENT_PRIMARY, Documentversion.DOCUMENTVERSION, Documentversion.DOCUMENTVERSION.FK_DOCUMENT);
	}
}
