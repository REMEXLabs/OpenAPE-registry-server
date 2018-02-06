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
import java.sql.SQLException;

import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.ILanguageDao;
import de.hdm.databaseaccess.IUnitOfWork;
import de.hdm.datatypes.ILanguage;
import de.hdm.helpers.Checker;

/**
 * This class implements the database access for languages on MySQL databases.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class MySqlLanguageDao extends MySqlDao implements ILanguageDao {

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
	public MySqlLanguageDao() {

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
	 * @see de.hdm.databaseaccess.ILanguageDao#insertLanguage(de.hdm.databaseaccess.IUnitOfWork,
	 * de.hdm.datatypes.ILanguage)
	 */
	@Override
	public void insertLanguage(IUnitOfWork unitOfWork, ILanguage language) throws DataAccessException {
		Checker.checkNull(language, "language");
		Connection connection = this.getConnection(unitOfWork);
		try {
			PreparedStatement insertLanguageStatement = connection
					.prepareStatement("INSERT INTO Language (code, name) VALUES(?, ?)");
			insertLanguageStatement.setString(1, language.getCode());
			insertLanguageStatement.setString(2, language.getName());
			insertLanguageStatement.executeUpdate();
			insertLanguageStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("The language " + language.getName() + "(" + language.getCode()
					+ ") could not be created on account of an error!", e);
		} finally {
			this.closeConnection(unitOfWork, connection);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.ILanguageDao#updateLanguage(de.hdm.databaseaccess.IUnitOfWork,
	 * de.hdm.datatypes.ILanguage, java.lang.String)
	 */
	@Override
	public void updateLanguage(IUnitOfWork unitOfWork, ILanguage language, String existingLanguageCode)
			throws DataAccessException {
		Checker.checkNull(language, "language");
		Checker.checkNullAndEmptiness(existingLanguageCode, "existingLanguageCode");
		Connection connection = this.getConnection(unitOfWork);
		try {
			PreparedStatement updateLanguageStatement = connection
					.prepareStatement("UPDATE language SET code = ?, name = ? WHERE code = ?");
			updateLanguageStatement.setString(1, language.getCode());
			updateLanguageStatement.setString(2, language.getName());
			updateLanguageStatement.setString(3, existingLanguageCode);
			updateLanguageStatement.executeUpdate();
			updateLanguageStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException(
					"The language with the code" + language.getCode() + " could not be updated on account of an error!",
					e);
		} finally {
			this.closeConnection(unitOfWork, connection);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.ILanguageDao#deleteLanguage(de.hdm.databaseaccess.IUnitOfWork,
	 * de.hdm.datatypes.ILanguage)
	 */
	@Override
	public void deleteLanguage(IUnitOfWork unitOfWork, ILanguage language) throws DataAccessException {
		Checker.checkNull(language, "language");
		Connection connection = this.getConnection(unitOfWork);
		try {
			PreparedStatement deleteLanguageStatement = connection
					.prepareStatement("Delete From Language Where code = ?");
			deleteLanguageStatement.setString(1, language.getCode());
			deleteLanguageStatement.executeUpdate();
			deleteLanguageStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("The language " + language.getName() + "(" + language.getCode()
					+ ") could not be deleted on account of an error!", e);
		} finally {
			this.closeConnection(unitOfWork, connection);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.ILanguageDao#isLanguageCodeValid(de.hdm.databaseaccess.IUnitOfWork, java.lang.String)
	 */
	@Override
	public boolean isLanguageCodeValid(IUnitOfWork unitOfWork, String languageCode) throws DataAccessException {
		Checker.checkNullAndEmptiness(languageCode, "languageCode");
		boolean result = false;
		Connection connection = this.getConnection(unitOfWork);
		try {
			PreparedStatement isLanguageKeyValidStatement = connection
					.prepareStatement("Select code From Language Where code = ?");
			isLanguageKeyValidStatement.setString(1, languageCode);
			ResultSet resultSet = isLanguageKeyValidStatement.executeQuery();
			if (resultSet.next() && resultSet.getString(1).equals(languageCode)) {
				result = true;
			}
			resultSet.close();
			isLanguageKeyValidStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Error occurred while validating the language code " + languageCode + "!", e);
		} finally {
			this.closeConnection(unitOfWork, connection);
		}
		return result;
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




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
