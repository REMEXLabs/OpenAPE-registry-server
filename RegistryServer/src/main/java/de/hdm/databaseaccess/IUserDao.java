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

import de.hdm.datatypes.IUser;

/**
 * This interface provides data access methods for users.
 * 
 * @author Tobias Ableitner
 *
 */
public interface IUserDao extends IDao {

	/**
	 * Inserts a new user. The user's user id should be -1 or not set, because the database will assign the id. This
	 * method does not check, whether the user or another user with same user name, name or e-mail address already
	 * exist. This has to be done before calling this method.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param user
	 *            the user which will be inserted. It must not be null. Otherwise an {@link IllegalArgumentException}
	 *            will be thrown.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter user is null.
	 */
	public void insertUser(IUnitOfWork unitOfWork, IUser user) throws DataAccessException;

	/**
	 * Selects an user by api key.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param apiKey
	 *            the api key. It must not be null or empty. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @return the user, to which the api key is assigned or null, if there exists no user with this api key
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter api key is null or empty.
	 */
	public int selectUserIdForApiKey(IUnitOfWork unitOfWork, String apiKey) throws DataAccessException;

	/**
	 * Select all users.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @return a list containing all stored users or an empty one, if no user is stored
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public List<IUser> selectUsers(IUnitOfWork unitOfWork) throws DataAccessException;

	/**
	 * Selects an user by user id.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userId
	 *            the id of the user. It must be greater equals 1. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @return the user, to which the user id is assigned or null if there exists no user with this user id
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter userId is not greater equals 1.
	 */
	public IUser selectUser(IUnitOfWork unitOfWork, int userId) throws DataAccessException;

	/**
	 * Deletes an user.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userId
	 *            the id of the user which should be deleted. It must be greater equals 1. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter userId not greater equals 1.
	 */
	public void deleteUser(IUnitOfWork unitOfWork, int userId) throws DataAccessException;

	/**
	 * Checks whether an user is super admin or not.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userId
	 *            the id of the user. It must be greater equals 1. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @return true if the user is super admin or false if not or there exists no user with this id
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter userId is not greater equals 1.
	 */
	public boolean isUserSuperAdmin(IUnitOfWork unitOfWork, int userId) throws DataAccessException;
	
	/**
	 * Updates an user. If an user with the id {@link IUser#getId()} does not exist in the database, nothing will
	 * happen. There will be no exception thrown or some other user entity affected. Thus before calling this method
	 * ensure that the user already exists in the database. Changes of an user's id {@link IUser#getId()} or user name
	 * {@link IUser#getUserName()} will not be stored in the database. This is a design decision, because it should not
	 * be possible to change an user id or user name.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param user
	 *            the user which will be updated. It must not be null. Otherwise an {@link IllegalArgumentException}
	 *            will be thrown.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public void updateUser(IUnitOfWork unitOfWork, IUser user) throws DataAccessException;

	// TODO java doc
	//public int selectUserIdAndAuthenticate(IUnitOfWork unitOfWork, String userName, String hashOfPassword) throws DataAccessException;

	/**
	 * Selects an user by his user name {@link IUser#getUserName()}. If there exists no user with the given user name,
	 * null will be returned.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userName
	 *            the user's user name. It must not be null or empty. Otherwise an {@link IllegalArgumentException} will
	 *            be thrown.
	 * @return user with the given user name or null if no one exists with the given user name
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public IUser selectUser(IUnitOfWork unitOfWork, String userName) throws DataAccessException;
	
}
