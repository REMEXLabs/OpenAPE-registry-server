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

import de.hdm.helpers.Checker;

/**
 * Abstract class for an unit of work. It generalizes those parts of the database specific unit of works, which they
 * have in common. For example the state handling.
 * 
 * This class is not thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public abstract class UnitOfWork implements IUnitOfWork {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Unit of work's state.
	 */
	private UnitOfWorkStateEnum state;

	/**
	 * Name of the database, for which this unit of work is created. The database name ensures, that one unit of work
	 * can not be used for calling data access object's methods of more than one database.
	 */
	private String databaseName;




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Creates a new unit of work.
	 * 
	 * @param databaseName
	 *            the name of the database, for which this unit of work is created. It must not be null or empty.
	 *            Otherwise an {@link IllegalArgumentException} will be thrown.
	 */
	public UnitOfWork(String databaseName) {
		this.setState(UnitOfWorkStateEnum.CREATED);
		this.setDatabaseName(databaseName);
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// getters and setters
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	private void setState(UnitOfWorkStateEnum state) {
		Checker.checkNull(state, "state");
		this.state = state;
	}

	private void setDatabaseName(String databaseName) {
		Checker.checkNullAndEmptiness(databaseName, "databaseName");
		this.databaseName = databaseName;
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// abstract methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Database specific implementation of the {@link #start()} method.
	 * @throws DataAccessException if unit of work could not be started on account of a problem with the database
	 */
	protected abstract void startDatabaseSpecificImplementation() throws DataAccessException;

	/**
	 * Database specific implementation of the {@link #abort()} method.
	 * @throws DataAccessException if unit of work could not be aborted on account of a problem with the database
	 */
	protected abstract void abortDatabaseSpecificImplementation() throws DataAccessException;

	/**
	 * Database specific implementation of the {@link #finish()} method.
	 * @throws DataAccessException if unit of work could not be finished on account of a problem with the database
	 */
	protected abstract void finishDatabaseSpecificImplementation() throws DataAccessException;




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// override methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.IUnitOfWork#getDatabaseName()
	 */
	@Override
	public String getDatabaseName() {
		return this.databaseName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.IUnitOfWork#getState()
	 */
	@Override
	public UnitOfWorkStateEnum getState() {
		return this.state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.IUnitOfWork#start()
	 */
	@Override
	public void start() throws DataAccessException {
		if (this.state != UnitOfWorkStateEnum.CREATED) {
			throw new RuntimeException(
					"Calling the method start() is not allowed in the state " + this.state.toString() + "!");
		}
		this.setState(UnitOfWorkStateEnum.STARTED);
		this.startDatabaseSpecificImplementation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.IUnitOfWork#abort()
	 */
	@Override
	public void abort() throws DataAccessException {
		if (this.state != UnitOfWorkStateEnum.STARTED) {
			throw new RuntimeException(
					"Calling the method abort() is not allowed in the state " + this.state.toString() + "!");
		}
		this.setState(UnitOfWorkStateEnum.ABORTED);
		this.abortDatabaseSpecificImplementation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.IUnitOfWork#finish()
	 */
	@Override
	public void finish() throws DataAccessException {
		if (this.state != UnitOfWorkStateEnum.STARTED) {
			throw new RuntimeException(
					"Calling the method finish() is not allowed in the state " + this.state.toString() + "!");
		}
		this.setState(UnitOfWorkStateEnum.FINISHED);
		this.finishDatabaseSpecificImplementation();
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
