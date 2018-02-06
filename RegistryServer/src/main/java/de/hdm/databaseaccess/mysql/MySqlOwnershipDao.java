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
import java.util.ArrayList;
import java.util.List;

import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IOwnershipDao;
import de.hdm.databaseaccess.IUnitOfWork;
import de.hdm.helpers.Checker;

/**
 * This class implements the database access for ownerships on MySql databases.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class MySqlOwnershipDao extends MySqlDao implements IOwnershipDao {

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
	public MySqlOwnershipDao(){
		
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
	 * @see de.hdm.databaseaccess.IOwnershipDao#selectOwnershipsOfConcept(de.hdm.databaseaccess.IUnitOfWork, java.lang.String)
	 */
	public List<Integer> selectOwnershipsOfConcept(IUnitOfWork unitOfWork, String conceptId) throws DataAccessException{
		Checker.checkNullAndEmptiness(conceptId, "conceptId");
		List<Integer> ownerIds = new ArrayList<Integer>();
		Connection connection = this.getConnection(unitOfWork);
		try{
			PreparedStatement selectOwnershipsStatement = connection.prepareStatement("SELECT userId FROM Ownership WHERE conceptId = ?");
			selectOwnershipsStatement.setString(1, conceptId);
			ResultSet resultSet = selectOwnershipsStatement.executeQuery();
			while(resultSet.next()){
				ownerIds.add(resultSet.getInt(1));
			}
			resultSet.close();
			selectOwnershipsStatement.close();
		}catch(Exception e){
			throw new DataAccessException("Could not select ownerships for concept " + conceptId + " on account of an error with the database!");
		}finally{
			this.closeConnection(unitOfWork, connection);
		}
		return ownerIds;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IOwnershipDao#insertOwnership(de.hdm.databaseaccess.IUnitOfWork, int, java.lang.String)
	 */
	@Override
	public void insertOwnership(IUnitOfWork unitOfWork, int userId, String conceptId) throws DataAccessException {
		Checker.checkIntegerGreaterEquals(userId, "userId", 1);
		Checker.checkNullAndEmptiness(conceptId, "conceptId");
		String sql = "INSERT INTO Ownership (conceptId, userId) VALUES (?, ?)";
		Connection connection = null;
		try {
			connection = this.getConnection(unitOfWork);
			PreparedStatement insertOwnershipStatement = connection.prepareStatement(sql);
			insertOwnershipStatement.setString(1, conceptId);
			insertOwnershipStatement.setInt(2, userId);
			insertOwnershipStatement.executeUpdate();
			insertOwnershipStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Could not insert the ownership of user " + userId + " for the concept " + conceptId + " on account of an error with the database!", e);
		}finally{
			this.closeConnection(unitOfWork, connection);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IOwnershipDao#deleteOwnership(de.hdm.databaseaccess.IUnitOfWork, int, java.lang.String)
	 */
	@Override
	public void deleteOwnership(IUnitOfWork unitOfWork, int userId, String conceptId) throws DataAccessException {
		Checker.checkIntegerGreaterEquals(userId, "userId", 1);
		Checker.checkNullAndEmptiness(conceptId, "conceptId");
		String sql = "DELETE FROM Ownership WHERE conceptId = ? AND userId = ?";
		Connection connection = null;
		try {
			connection = this.getConnection(unitOfWork);
			PreparedStatement deleteOwnershipStatement = connection.prepareStatement(sql);
			deleteOwnershipStatement.setString(1, conceptId);
			deleteOwnershipStatement.setInt(2, userId);
			deleteOwnershipStatement.executeUpdate();
			deleteOwnershipStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Could not delete the ownership of user " + userId + " for the concept " + conceptId + " on account of an error with the database!", e);
		}finally {
			this.closeConnection(unitOfWork, connection);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IOwnershipDao#isUserOwnerOfConcept(de.hdm.databaseaccess.IUnitOfWork, int, java.lang.String)
	 */
	@Override
	public boolean isUserOwnerOfConcept(IUnitOfWork unitOfWork, int userId, String conceptId) throws DataAccessException {
		Checker.checkIntegerGreaterEquals(userId, "userId", 1);
		Checker.checkNullAndEmptiness(conceptId, "conceptId");
		String sql = "SELECT * FROM Ownership WHERE conceptId = ? AND userId = ?";
		boolean isOwner = false;
		Connection connection = null;
		try {
			connection = this.getConnection(unitOfWork);
			PreparedStatement selectOwnershipStatement = connection.prepareStatement(sql);
			selectOwnershipStatement.setString(1, conceptId);
			selectOwnershipStatement.setInt(2, userId);
			ResultSet resultSet = selectOwnershipStatement.executeQuery();
			while(resultSet.next() && isOwner == false){
				isOwner = true;
			}
			resultSet.close();
			selectOwnershipStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Could not check whether user " + userId + " is owner of concept " + conceptId + " or not of an error with the database!", e);
		}finally {
			this.closeConnection(unitOfWork, connection);
		}
		return isOwner;
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
