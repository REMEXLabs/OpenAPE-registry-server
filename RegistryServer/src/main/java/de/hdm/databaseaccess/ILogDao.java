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

import java.util.GregorianCalendar;
import java.util.List;

import de.hdm.datatypes.ILog;

/**
 * This interface provides data access methods for logs.
 * 
 * @author Tobias Ableitner
 *
 */
public interface ILogDao {

	/**
	 * Inserts a new log entry.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param log
	 *            the log entry, that will be inserted. It must not be null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter log is null.
	 */
	public void insertLog(IUnitOfWork unitOfWork, ILog log) throws DataAccessException;

	/**
	 * Checks whether a concept was updated since the user has red it for the last time. This method should be called
	 * before updating a concept, because it ensures, that an earlier update, which the updating user did not already
	 * read, will not be overwritten. Note this method will only work correctly if successful concept reads are logged
	 * the user, which is defined by the parameter user id, have already red the concept, which is defined by the
	 * parameter conceptId.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userId
	 *            the id of the user, who wants to read the concept. It must be greater equals 1. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @param conceptId
	 *            the id of the concept, which the user wants to read. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @return true if there was another update since the user has red it for the last time or there exists no
	 *         successful read log entry with the given user and
	 *         concept id, and false if not
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if one or more parameters are invalid.
	 */
	public boolean updatedSinceUserRed(IUnitOfWork unitOfWork, int userId, String conceptId) throws DataAccessException;

	/**
	 * Selects the last update date time stamp of a concept.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param conceptId
	 *            the id of the concept, which the user wants to read. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @return date time stamp of the concept's last update in UTC or null, if there exists no concept with the given
	 *         concept id
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter conceptId is null or empty.
	 */
	public GregorianCalendar selectLastUpdateDateTimeStampOfConcept(IUnitOfWork unitOfWork, String conceptId)
			throws DataAccessException;
	
	// TODO java doc
	public List<ILog> selectAllLogsForConcept(IUnitOfWork unitOfWork, String conceptId, boolean newestEntryFirst) throws DataAccessException;

}
