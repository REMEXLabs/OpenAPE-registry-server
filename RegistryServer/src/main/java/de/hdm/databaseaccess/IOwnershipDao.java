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

import java.util.List;

/**
 * This interface provides data access methods for ownerships.
 * 
 * @author Tobias Ableitner
 *
 */
public interface IOwnershipDao extends IDao {

	/**
	 * Selects the user ids of the owners of a concept. The concept is defined by the parameter conceptId. If there
	 * exist no concept with the given id or the concept has no owners (what should normally not be the case), an empty
	 * list will be returned.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param conceptId
	 *            id of concept, which owners should be selected. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @return list with the user ids of the concept's owners. If the concept has no owners or an concept with the given
	 *         id does not exist an empty list will be returned.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter conceptId is null or empty.
	 */
	public List<Integer> selectOwnershipsOfConcept(IUnitOfWork unitOfWork, String conceptId) throws DataAccessException;

	/**
	 * Inserts an ownership. This method does not check, whether the user and concept, which are defined by the
	 * parameters userId and conceptId really exist or this ownership already exists. This has to be done before
	 * calling this method.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userId
	 *            id of the user. It must be greater equals 1. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @param conceptId
	 *            the id of the concept which should be deleted. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter userId is not greater equals 1 and / or the parameter conceptId is null or empty.
	 */
	public void insertOwnership(IUnitOfWork unitOfWork, int userId, String conceptId) throws DataAccessException;

	/**
	 * Deletes an ownership.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userId
	 *            id of the user. It must be greater equals 1. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @param conceptId
	 *            the id of the concept which should be deleted. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter userId is not greater equals 1 and / or the parameter conceptId is null or empty.
	 */
	public void deleteOwnership(IUnitOfWork unitOfWork, int userId, String conceptId) throws DataAccessException;

	/**
	 * Checks whether an user, defined by the parameter userId, is owner of the concept, which is defined by the
	 * parameter conceptId.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userId
	 *            id of the user. It must be greater equals 1. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @param conceptId
	 *            the id of the concept which should be deleted. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @return true if the user is owner of the concept or false if not or the user and / or concept does not exist
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter userId is not greater equals 1 and / or the parameter conceptId is null or empty.
	 */
	public boolean isUserOwnerOfConcept(IUnitOfWork unitOfWork, int userId, String conceptId)
			throws DataAccessException;

}
