package de.isnow.stypesafe.jooq;

import java.sql.Connection;

import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.util.mysql.MySQLFactory;

@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.4.2"},
                            comments = "This class is generated by jOOQ")
public class StypesafeFactory extends MySQLFactory {

	private static final long serialVersionUID = 429370811;

	/**
	 * Create a factory with a connection
	 *
	 * @param connection The connection to use with objects created from this factory
	 */
	public StypesafeFactory(Connection connection) {
		super(connection);

		initDefaultSchema();
	}

	/**
	 * Create a factory with a connection and some settings
	 *
	 * @param connection The connection to use with objects created from this factory
	 * @param settings The settings to apply to objects created from this factory
	 */
	public StypesafeFactory(Connection connection, Settings settings) {
		super(connection, settings);

		initDefaultSchema();
	}

	/**
	 * Initialise the render mapping's default schema.
	 * <p>
	 * For convenience, this schema-specific factory should override any pre-existing setting
	 */
	private final void initDefaultSchema() {
		SettingsTools.getRenderMapping(getSettings()).setDefaultSchema(Stypesafe.STYPESAFE.getName());
	}
}