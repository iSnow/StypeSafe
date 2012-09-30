/**
 * This class is generated by jOOQ
 */
package de.isnow.stypesafe.jooq.tables;

import java.util.Arrays;
import java.util.List;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.UpdatableTableImpl;
import org.jooq.tools.unsigned.UInteger;

import de.isnow.stypesafe.jooq.Keys;
import de.isnow.stypesafe.jooq.Stypesafe;
import de.isnow.stypesafe.jooq.tables.records.DocumentversionRecord;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.4.2"},
                            comments = "This class is generated by jOOQ")
public class Documentversion extends UpdatableTableImpl<DocumentversionRecord> {

	private static final long serialVersionUID = 1323653638;

	/**
	 * The singleton instance of stypesafe.documentversion
	 */
	public static final Documentversion DOCUMENTVERSION = new Documentversion();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<DocumentversionRecord> getRecordType() {
		return DocumentversionRecord.class;
	}

	/**
	 * The table column <code>stypesafe.documentversion.id</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final TableField<DocumentversionRecord, UInteger> ID = createField("id", SQLDataType.INTEGERUNSIGNED, this);

	/**
	 * The table column <code>stypesafe.documentversion.fk_document</code>
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT document_version
	 * FOREIGN KEY (fk_document)
	 * REFERENCES stypesafe.document (id)
	 * </pre></code>
	 */
	public final TableField<DocumentversionRecord, UInteger> FK_DOCUMENT = createField("fk_document", SQLDataType.INTEGERUNSIGNED, this);

	/**
	 * The table column <code>stypesafe.documentversion.version</code>
	 */
	public final TableField<DocumentversionRecord, UInteger> VERSION = createField("version", SQLDataType.INTEGERUNSIGNED, this);

	/**
	 * The table column <code>stypesafe.documentversion.timestamp</code>
	 */
	public final TableField<DocumentversionRecord, java.sql.Timestamp> TIMESTAMP = createField("timestamp", SQLDataType.TIMESTAMP, this);

	public Documentversion() {
		super("documentversion", Stypesafe.STYPESAFE);
	}

	public Documentversion(java.lang.String alias) {
		super(alias, Stypesafe.STYPESAFE, Documentversion.DOCUMENTVERSION);
	}

	@Override
	public Identity<DocumentversionRecord, UInteger> getIdentity() {
		return Keys.IDENTITY_DOCUMENTVERSION;
	}

	@Override
	public UniqueKey<DocumentversionRecord> getMainKey() {
		return Keys.KEY_DOCUMENTVERSION_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UniqueKey<DocumentversionRecord>> getKeys() {
		return Arrays.<UniqueKey<DocumentversionRecord>>asList(Keys.KEY_DOCUMENTVERSION_PRIMARY);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ForeignKey<DocumentversionRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<DocumentversionRecord, ?>>asList(Keys.DOCUMENT_VERSION);
	}

	@Override
	public Documentversion as(java.lang.String alias) {
		return new Documentversion(alias);
	}
}
