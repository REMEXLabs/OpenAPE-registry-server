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

import de.hdm.datatypes.IGroupAccessRight;

/**
 * This interface provide data access methods for group access rights.
 * 
 * @author Tobias Ableitner
 *
 */
public interface IGroupAccessRightDao extends IDao {

	/**
	 * Inserts a group access right. This method does not check, whether there exists already a group access right or
	 * not for the same group and concept. It does also not check, whether the concept and group exist. This has to be
	 * done before calling this method.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param groupAccessRight
	 *            the group access right which will be inserted. It must not be null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if one or more parameters are invalid.
	 */
	public void insertGroupAccessRight(IUnitOfWork unitOfWork, IGroupAccessRight groupAccessRight)
			throws DataAccessException;

	/**
	 * Inserts a group access rights. This method does not check before storing the group access rights, whether there
	 * exists already a group access right or not same group and concept. It does also not check, whether the concepts
	 * and groups exist. This has to be done before calling this method.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param groupAccessRights
	 *            the group access rights which will be inserted. It must not be null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if one or more parameters are invalid.
	 */
	public void insertGroupAccessRights(IUnitOfWork unitOfWork, List<IGroupAccessRight> groupAccessRights)
			throws DataAccessException;

	/**
	 * Deletes a group access right.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param groupId
	 *            id of the group which group access right should be deleted. Together with the parameter conceptId it
	 *            uniquely identifies the group access right. It must be greater equals 1. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @param conceptId
	 *            id of the concept, which group access right should be deleted. Together with the parameter groupId it
	 *            uniquely identifies the group access right. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if one or more parameters are invalid.
	 */
	public void deleteGroupAccessRight(IUnitOfWork unitOfWork, int groupId, String conceptId)
			throws DataAccessException;

	
	// public void updateGroupAccessRight(IUnitOfWork unitOfWork, IGroupAccessRight groupAccessRight);

	// public void updateGroupAccessRights(IUnitOfWork unitOfWork, List<IGroupAccessRight> groupAccessRights);

	/**
	 * Selects all group access rights related to one concept.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param conceptId
	 *            the id of the concept whose group access rights will be selected. It must not be null or empty.
	 *            Otherwise an {@link IllegalArgumentException} will be thrown.
	 * @return list of group access rights related to the concept id. If there are no group access rights for the given
	 *         concept id, an empty list will be returned.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if one or more parameters are invalid.
	 */
	public List<IGroupAccessRight> selectGroupAccessRightsForConcept(IUnitOfWork unitOfWork, String conceptId)
			throws DataAccessException;

	/**
	 * Checks whether an user has read right for a concept. This method only checks the group access right and not,
	 * whether an user is owner of a concept or super admin. Thus the result of this method is only based on the group
	 * access right.
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
	 * @return true if user has read right for concept and false if not
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if one or more parameters are invalid.
	 */
	public boolean hasUserReadRightForConcept(IUnitOfWork unitOfWork, int userId, String conceptId)
			throws DataAccessException;

	/**
	 * Checks whether an user has update right for a concept. This method only checks the group access right and not,
	 * whether an user is owner of a concept or super admin. Thus the result of this method is only based on the group
	 * access right.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userId
	 *            the id of the user, who wants to update the concept. It must be greater equals 1. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @param conceptId
	 *            the id of the concept, which the user wants to update. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @return true if user has update right for concept and false if not
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if one or more parameters are invalid.
	 */
	public boolean hasUserUpdateRightForConcept(IUnitOfWork unitOfWork, int userId, String conceptId)
			throws DataAccessException;

	/**
	 * Checks whether an user has delete right for a concept. This method only checks the group access right and not,
	 * whether an user is owner of a concept or super admin. Thus the result of this method is only based on the group
	 * access right.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userId
	 *            the id of the user, who wants to delete the concept. It must be greater equals 1. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @param conceptId
	 *            the id of the concept, which the user wants to delete. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @return true if user has delete right for concept and false if not
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if one or more parameters are invalid.
	 */
	public boolean hasUserDeleteRightForConcept(IUnitOfWork unitOfWork, int userId, String conceptId)
			throws DataAccessException;

	/**
	 * Checks whether an user has change rights right for a concept. This method only checks the group access right and
	 * not, whether an user is owner of a concept or super admin. Thus the result of this method is only based on the
	 * group access right.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userId
	 *            the id of the user, who wants to change the group access rights for the concept. It must be greater
	 *            equals 1. Otherwise an {@link IllegalArgumentException} will be thrown.
	 * @param conceptId
	 *            the id of the concept, which group access rights the user wants to change. It must not be null or
	 *            empty. Otherwise an {@link IllegalArgumentException} will be thrown.
	 * @return true if user has change rights right for concept and false if not
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if one or more parameters are invalid.
	 */
	public boolean hasUserChangeRightsRightForConcept(IUnitOfWork unitOfWork, int userId, String conceptId)
			throws DataAccessException;

}
