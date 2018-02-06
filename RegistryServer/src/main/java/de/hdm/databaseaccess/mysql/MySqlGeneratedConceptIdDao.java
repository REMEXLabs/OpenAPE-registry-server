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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IGeneratedConceptIdDao;
import de.hdm.databaseaccess.IUnitOfWork;
import de.hdm.databaseaccess.UnitOfWorkStateEnum;

/**
 * This class implements the data access for generated concept ids on MySql databases.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class MySqlGeneratedConceptIdDao extends MySqlConceptDao implements IGeneratedConceptIdDao {

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

	/**
	 * Empty default constructor.
	 */
	public MySqlGeneratedConceptIdDao() {
		
	}



	
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
	 * @see de.hdm.databaseaccess.IGeneratedConceptIdDao#selectNumberAndIncrementIt(de.hdm.databaseaccess.IUnitOfWork)
	 */
	@Override
	public int selectNumberAndIncrementIt(IUnitOfWork unitOfWork) throws DataAccessException {
		int number;
		if (unitOfWork == null) {
			number = this.selectNumberAndIncrementItNotAsPartOfAnTransaction();
		} else {
			number = this.executeSelectNumberAndIncrementIt(this.getConnection(unitOfWork));
		}
		return number;
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




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// private methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	private int selectNumberAndIncrementItNotAsPartOfAnTransaction() throws DataAccessException {
		IUnitOfWork unitOfWork = this.createUnitOfWork();
		unitOfWork.start();
		int number = -1;
		try {
			number = this.executeSelectNumberAndIncrementIt(this.getConnection(unitOfWork));
		} catch (DataAccessException e) {
			e.printStackTrace();
			unitOfWork.abort();
			throw e;
		} finally {
		    if (unitOfWork != null && unitOfWork.getState() == UnitOfWorkStateEnum.STARTED) {
                unitOfWork.finish();
            }
		}

		return number;
	}

	private int executeSelectNumberAndIncrementIt(Connection connection) throws DataAccessException {
		int number = -1;
		try {
			String sqlSelect = "SELECT number FROM GeneratedConceptId";
			PreparedStatement selectNumberStatement = connection.prepareStatement(sqlSelect);
			ResultSet resultSet = selectNumberStatement.executeQuery();
			while (resultSet.next()) {
				number = resultSet.getInt(1);
			}
			resultSet.close();
			selectNumberStatement.close();

			String sqlInsert = "UPDATE GeneratedConceptId SET number = ? WHERE id = 1";
			PreparedStatement updateNumberStatement = connection.prepareStatement(sqlInsert);
			updateNumberStatement.setInt(1, (number + 1));
			updateNumberStatement.executeUpdate();
			updateNumberStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException(
					"The number for the concept id generation could not be selected and incremented on account of an error with the database!",
					e);
		}
		return number;
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
