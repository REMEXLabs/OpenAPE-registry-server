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

import de.hdm.datatypes.ILanguage;

/**
 * This interface provides data access methods for languages.
 * 
 * @author Tobias Ableitner
 *
 */
public interface ILanguageDao extends IDao {

	/**
	 * Adds a new language {@link ILanguage} into a database. This method does not check, whether the language already
	 * exists. This has to be done before calling this method.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param language
	 *            the language which will be added. It must not be null. Otherwise an {@link IllegalArgumentException}
	 *            will be thrown.
	 * @throws DataAccessException
	 *             if the database already contains a language with the same code
	 *             {@link de.hdm.datatypes.ILanguage#getCode()} or some other problem occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter language is null.
	 */
	public void insertLanguage(IUnitOfWork unitOfWork, ILanguage language) throws DataAccessException;

	/**
	 * Updates an existing language {@link ILanguage} in the database. Note, that the language code
	 * {@link de.hdm.datatypes.ILanguage#getCode()} has to be unique. This method does not check, whether the langue,
	 * which should be updated already exists. This has to be done before calling this method.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param language
	 *            the updated language. It must not be null. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @param existingLanguageCode
	 *            language code of the existing entry. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @throws DataAccessException
	 *             if the database already contains a language with the same code
	 *             {@link de.hdm.datatypes.ILanguage#getCode()} or some other problem occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter language is null and / or the parameter existingLanguageCode is null or empty.
	 */
	public void updateLanguage(IUnitOfWork unitOfWork, ILanguage language, String existingLanguageCode)
			throws DataAccessException;

	/**
	 * Deletes a language {@link ILanguage} in the database.
	 * 
	 * @param unitOfWork
	 *            handler if this method is part of an transaction. If this is not required it can be null.
	 * @param language
	 *            the language which should be deleted. It must not be null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter language is null.
	 */
	public void deleteLanguage(IUnitOfWork unitOfWork, ILanguage language) throws DataAccessException;

	/**
	 * Checks whether the languageCode is valid. If the database does not contain it, it is invalid. Note that the
	 * languageCode has to be in lower-case characters.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param languageCode
	 *            which should be validated in lower-case characters. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @return true if it valid and false if not
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter languageKey is null or empty.
	 */
	public boolean isLanguageCodeValid(IUnitOfWork unitOfWork, String languageKey) throws DataAccessException;

}
