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
package de.hdm.databaseaccess;

import de.hdm.databaseaccess.mysql.MySqlDaoFactory;

/**
 * This class is part of a factory design pattern. It is the factory to create database specific data access object
 * factories. So, in this class, the factory for the data access objects for a MySQL database are created. The supported
 * database by this factory are defined in the inner class {@link DaoFactory.Database}. This factories supports also the
 * use of a default database in terms of the constant {@link DaoFactory.Database#DEFAULT}. If you use this constant to
 * create the factories, you have only to change this constants value to the other database, if you want to change
 * database, which is used by the registry server at later time.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public abstract class DaoFactory {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




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

	/**
	 * Creates database specific data access object for language.
	 * 
	 * @return data access object for language
	 */
	public abstract ILanguageDao createLanguageDao();

	/**
	 * Creates database specific data access object for concept.
	 * 
	 * @return data access object for concept
	 */
	public abstract IConceptDao createConceptDao();

	/**
	 * Creates database specific data access object for user.
	 * 
	 * @return data access object for user
	 */
	public abstract IUserDao createUserDao();

	/**
	 * Creates database specific data access object for ownership.
	 * 
	 * @return data access object for ownership
	 */
	public abstract IOwnershipDao createOwnershipDao();

	/**
	 * Creates database specific data access object for group access right.
	 * 
	 * @return data access object for group access right
	 */
	public abstract IGroupAccessRightDao createGroupAccessRightDao();

	/**
	 * Creates database specific data access object for log.
	 * 
	 * @return data access object for log
	 */
	public abstract ILogDao createLogDao();

	/**
	 * Creates database specific data access object for generated concept id.
	 * 
	 * @return data access object for generated concept id
	 */
	public abstract IGeneratedConceptIdDao createGeneratedConceptIdDao();
	
	/**
	 * Creates database specific data access object for groups.
	 * 
	 * @return data access object for groups
	 */
	public abstract IGroupDao createGroupDao();



	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// override methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// public methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Creates a data access object factory for the database, which is defined by the caller of this method via the
	 * parameter database. The available databases are defined in {@link RegistryDaoFactory.Database}. You should use
	 * one of those constants for this method's parameter database.
	 * 
	 * @param database
	 *            the database for which the data access object factory should be created. It's value must be equal to
	 *            one of the constant's value defined in {@link RegistryDaoFactory.Database}. Otherwise a
	 *            {@link RuntimeException} will be thrown.
	 * @return data access object factory for a database
	 * @throws RuntimeException
	 *             if the value of the parameter database is invalid
	 */
	public static DaoFactory createDaoFactory(int database) {
		switch (database) {
		case Database.MY_SQL:
			return new MySqlDaoFactory();
		default:
			throw new RuntimeException(
					"There is no RegistryDaoFactory for a parameter with the value " + database + "!");
		}
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// protected methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// private methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * This class defines integer constants to define, for which database the data access object factory should be
	 * created in {@link RegistryDaoFactory#createDaoFactory(int)}.
	 * 
	 * @author Tobias
	 *
	 */
	public static class Database {

		/**
		 * Use default database. This constant has the same value with one of the other constants of class
		 * {@link Database}.
		 */
		public static final int DEFAULT = 1;

		/**
		 * MySQL database
		 */
		public static final int MY_SQL = 1;

	}

}
