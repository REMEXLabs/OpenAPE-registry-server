/*******************************************************************************
 * Copyright 2016-2018 Research group REMEX, Hochschule der Medien (Stuttgart, Germany)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.hdm.databaseaccess.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import de.hdm.databaseaccess.IDao;
import de.hdm.databaseaccess.IUnitOfWork;
import de.hdm.databaseaccess.MySqlUnitOfWork;
import de.hdm.databaseaccess.connections.RegistryServerDatabaseConnectionManager;
import de.hdm.helpers.GregorianCalendarHelper;

/**
 * This abstract class generalizes specific stuff for the MySQL database, which is needed in all data access object
 * classes for the MySQL database.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public abstract class MySqlDao implements IDao {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Name of the database for which this data access object is created.
	 */
	protected static final String REGISTRY_DATABASE = "registryDatabase";




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// getters and setters
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// abstract methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// override methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.IDao#createUnitOfWork()
	 */
	@Override
	public IUnitOfWork createUnitOfWork() {
		return new MySqlUnitOfWork(REGISTRY_DATABASE, RegistryServerDatabaseConnectionManager.getInstance().getConnection());
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// public methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// protected methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Returns a connection to the MySQL database. If the parameter unitOfWork is null, it creates a new one and if it
	 * is not null it will return it's connection.
	 * 
	 * @param unitOfWork
	 *            the unit of work, which connection should be used. If the database connection is not needed for an
	 *            unit of work, it can be null.
	 * @return connection to the MySQL database.
	 */
	protected Connection getConnection(IUnitOfWork unitOfWork) {
		Connection connection;
		if (unitOfWork == null) {
			connection = RegistryServerDatabaseConnectionManager.getInstance().getConnection();
		} else {
			this.checkUnitOfWork(unitOfWork);
			MySqlUnitOfWork mySqlUnitOfWork = (MySqlUnitOfWork) unitOfWork;
			connection = mySqlUnitOfWork.getConnection();
		}
		return connection;
	}

	/**
	 * Closes a connection to thy MySQL database if it is not null and the parameter unitOfWork is not null, too. If the
	 * connection belongs to an unit of work the latter should be committed via the parameter unitOfWork.
	 * 
	 * @param unitOfWork
	 *            the unit of work, to which the connection belongs, which should be closed. It can be null.
	 * @param connection
	 *            the connection which should be closed. It can be null.
	 */
	protected void closeConnection(IUnitOfWork unitOfWork, Connection connection) {
		if (unitOfWork == null && connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected String convertGregorianCalendarToMySqlDateTime(GregorianCalendar gregorianCalendar) {
		gregorianCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		String dateTime = GregorianCalendarHelper.convertDateAndTimeToString(gregorianCalendar, "-", " ", ":", true,
				".");
		return dateTime;
	}

	protected GregorianCalendar convertMySqlDateTimeToGregorianCalendar(String mySqlDateTime) {
		System.out.println("mySqlDateTime=" + mySqlDateTime);
		GregorianCalendar gregorianCalendar = GregorianCalendarHelper
				.convertDateTimeStringFromDatabaseToGregorianCalendar(mySqlDateTime);
		System.out.println("gregorianCalendar.getTimeInMillis()=" + gregorianCalendar.getTimeInMillis());
		gregorianCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		System.out.println("gregorianCalendar.getTimeInMillis()=" + gregorianCalendar.getTimeInMillis());
		return gregorianCalendar;
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// private methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Checks whether the unit of work belongs to the same database as the data access object. This is not needed while
	 * the registry server use's only one database. But if there is more than one database, that an unit of work can
	 * contain only data accesses to one database.
	 * 
	 * @param unitOfWork
	 *            the unit of work
	 */
	private void checkUnitOfWork(IUnitOfWork unitOfWork) {
		if (unitOfWork.getDatabaseName().equals(REGISTRY_DATABASE) == false) {
			throw new RuntimeException("This method cannot be called with a connection for the database "
					+ unitOfWork.getDatabaseName() + "!");
		}
		if (unitOfWork instanceof MySqlUnitOfWork == false) {
			throw new RuntimeException("This method can only be called with an MySqlUnitOfWork instance and not"
					+ unitOfWork.getClass().getCanonicalName() + "!");
		}
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
