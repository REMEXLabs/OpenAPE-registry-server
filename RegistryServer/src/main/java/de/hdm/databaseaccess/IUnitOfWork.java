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

/**
 * This is the interface for an unit of work. An unit of work can be used for transactional database access if more than
 * one of the data access objects methods have to be called. Calling the method {@link #start()} starts an unit of work.
 * It is like starting a transaction. Note that an unit of work can only be started once. With the method
 * {@link #abort()} and already started unit of work can be aborted. Calling this method will has the same effects like
 * aborting a transaction. All changes, that were already done by the unit of work in the database will be undone.
 * Calling the method {@link #finish()} finishes an unit of work normally. This is comparable to a commit in a
 * transaction. Thus it should be called, if all method calls on the database access objects were done successfully.
 * Note, that only an already started unit of work can be finished.
 * 
 * This class is not thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public interface IUnitOfWork {

	/**
	 * Returns the name of the database for which the unit of work was created.
	 * 
	 * @return database name for which the unit of work was created
	 */
	public String getDatabaseName();

	/**
	 * Returns the unit of work's state. The state describes, whether the unit of work is created
	 * {@link UnitOfWorkStateEnum#CREATED}, started {@link UnitOfWorkStateEnum#STARTED}, aborted
	 * {@link UnitOfWorkStateEnum#ABORTED} or finished {@link UnitOfWorkStateEnum#FINISHED}.
	 * 
	 * @return unit of work's state
	 */
	public UnitOfWorkStateEnum getState();

	/**
	 * Starts the unit of work and set it state to {@link UnitOfWorkStateEnum#STARTED}. If the unit of work's state is
	 * not {@link UnitOfWorkStateEnum#CREATED} a {@link RuntimeException} will be thrown.
	 * 
	 * @throws DataAccessException
	 *             if unit of work could not be started on account of a problem with the database
	 * @throws RuntimeException
	 *             if the unit of work's state is not {@link UnitOfWorkStateEnum#CREATED}
	 */
	public void start() throws DataAccessException;

	/**
	 * Aborts the unit of work and set it state to {@link UnitOfWorkStateEnum#ABORTED}. If the unit of work's state is
	 * not {@link UnitOfWorkStateEnum#STARTED} a {@link RuntimeException} will be thrown.
	 * 
	 * @throws DataAccessException
	 *             if unit of work could not be aborted on account of a problem with the database
	 * @throws RuntimeException
	 *             if the unit of work's state is not {@link UnitOfWorkStateEnum#STARTED}
	 */
	public void abort() throws DataAccessException;

	/**
	 * Finishes the unit of work and set it state to {@link UnitOfWorkStateEnum#FINISHED}. If the unit of work's state
	 * is not {@link UnitOfWorkStateEnum#STARTED} a {@link RuntimeException} will be thrown.
	 * 
	 * @throws DataAccessException
	 *             if unit of work could not be finished on account of a problem with the database
	 * @throws RuntimeException
	 *             if the unit of work's state is not {@link UnitOfWorkStateEnum#STARTED}
	 */
	public void finish() throws DataAccessException;

}
