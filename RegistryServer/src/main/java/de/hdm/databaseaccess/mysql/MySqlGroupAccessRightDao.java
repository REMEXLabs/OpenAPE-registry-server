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
import de.hdm.databaseaccess.IGroupAccessRightDao;
import de.hdm.databaseaccess.IUnitOfWork;
import de.hdm.databaseaccess.UnitOfWorkStateEnum;
import de.hdm.datatypes.GroupAccessRight;
import de.hdm.datatypes.IGroupAccessRight;
import de.hdm.helpers.Checker;

/**
 * This class implements the database access for group access rights on MySQL databases.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class MySqlGroupAccessRightDao extends MySqlDao implements IGroupAccessRightDao {

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
	public MySqlGroupAccessRightDao() {

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
	 * @see de.hdm.databaseaccess.IGroupAccessRightDao#insertGroupAccessRight(de.hdm.databaseaccess.IUnitOfWork,
	 * de.hdm.datatypes.IGroupAccessRight)
	 */
	@Override
	public void insertGroupAccessRight(IUnitOfWork unitOfWork, IGroupAccessRight groupAccessRight)
			throws DataAccessException {
		Checker.checkNull(groupAccessRight, "groupAccessRight");
		this.executeInsertGroupAccessRight(unitOfWork, groupAccessRight);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.IGroupAccessRightDao#insertGroupAccessRights(de.hdm.databaseaccess.IUnitOfWork,
	 * java.util.List)
	 */
	@Override
	public void insertGroupAccessRights(IUnitOfWork unitOfWork, List<IGroupAccessRight> groupAccessRights)
			throws DataAccessException {
		Checker.checkNull(groupAccessRights, "groupAccessRights");
		
		if(unitOfWork != null){
			// insert them as part of an unit of work
			for (IGroupAccessRight n : groupAccessRights) {
				this.executeInsertGroupAccessRight(unitOfWork, n);
			}	
		}else{
			// create an unit of work to ensure, that all group access rights or no group access rights will be inserted
			unitOfWork = this.createUnitOfWork();
			try{
				this.createUnitOfWork().start();
				for (IGroupAccessRight n : groupAccessRights) {
					this.executeInsertGroupAccessRight(unitOfWork, n);
				}
				unitOfWork.finish();
			}catch(DataAccessException e){
				e.printStackTrace();
				unitOfWork.abort();
				throw e;
			}finally{
				if(unitOfWork != null && unitOfWork.getState() == UnitOfWorkStateEnum.STARTED){
					unitOfWork.finish();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.IGroupAccessRightDao#deleteGroupAccessRight(de.hdm.databaseaccess.IUnitOfWork, int,
	 * java.lang.String)
	 */
	@Override
	public void deleteGroupAccessRight(IUnitOfWork unitOfWork, int groupId, String conceptId)
			throws DataAccessException {
		Checker.checkIntegerGreaterEquals(groupId, "groupId", 1);
		Checker.checkNullAndEmptiness(conceptId, "conceptId");
		String sql = "DELETE FROM GroupAccessRight WHERE groupId = ? AND conceptId = ?";
		Connection connection = null;
		try {
			connection = this.getConnection(unitOfWork);
			PreparedStatement deleteGroupAccessRightStatement = connection.prepareStatement(sql);
			deleteGroupAccessRightStatement.setInt(1, groupId);
			deleteGroupAccessRightStatement.setString(2, conceptId);
			deleteGroupAccessRightStatement.executeUpdate();
			deleteGroupAccessRightStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("The access right of group " + groupId + " for the concept " + conceptId
					+ "could not be deleted on account of an error with the database!", e);
		} finally {
			this.closeConnection(unitOfWork, connection);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hdm.databaseaccess.IGroupAccessRightDao#selectGroupAccessRightsForConcept(de.hdm.databaseaccess.IUnitOfWork,
	 * java.lang.String)
	 */
	public List<IGroupAccessRight> selectGroupAccessRightsForConcept(IUnitOfWork unitOfWork, String conceptId)
			throws DataAccessException {
		Checker.checkNullAndEmptiness(conceptId, "conceptId");
		String sql = "SELECT groupId, conceptId, readRight, updateRight, deleteRight, changeRightsRight FROM GroupAccessRight WHERE conceptId = '"
				+ conceptId + "'";
		List<IGroupAccessRight> groupAccessRights = new ArrayList<IGroupAccessRight>();
		Connection connection = null;
		try {
			connection = this.getConnection(unitOfWork);
			PreparedStatement selectGroupAccessRightsStatement = connection.prepareStatement(sql);

			ResultSet resultSet = selectGroupAccessRightsStatement.executeQuery();
			while (resultSet.next()) {
				groupAccessRights
						.add(new GroupAccessRight(resultSet.getInt(1), resultSet.getString(2), resultSet.getBoolean(3),
								resultSet.getBoolean(4), resultSet.getBoolean(5), resultSet.getBoolean(6)));
			}
			selectGroupAccessRightsStatement.close();
			resultSet.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException(
					"The access rights could not be selected on account of an error with the database!", e);
		} finally {
			this.closeConnection(unitOfWork, connection);
		}
		return groupAccessRights;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.IGroupAccessRightDao#hasUserReadRightForConcept(de.hdm.databaseaccess.IUnitOfWork,
	 * int, java.lang.String)
	 */
	@Override
	public boolean hasUserReadRightForConcept(IUnitOfWork unitOfWork, int userId, String conceptId)
			throws DataAccessException {
		return this.hasUserRightForConcept(unitOfWork, userId, conceptId, "readRight", "read right");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.IGroupAccessRightDao#hasUserUpdateRightForConcept(de.hdm.databaseaccess.IUnitOfWork,
	 * int, java.lang.String)
	 */
	@Override
	public boolean hasUserUpdateRightForConcept(IUnitOfWork unitOfWork, int userId, String conceptId)
			throws DataAccessException {
		return this.hasUserRightForConcept(unitOfWork, userId, conceptId, "updateRight", "update right");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.IGroupAccessRightDao#hasUserDeleteRightForConcept(de.hdm.databaseaccess.IUnitOfWork,
	 * int, java.lang.String)
	 */
	@Override
	public boolean hasUserDeleteRightForConcept(IUnitOfWork unitOfWork, int userId, String conceptId)
			throws DataAccessException {
		return this.hasUserRightForConcept(unitOfWork, userId, conceptId, "deleteRight", "delete right");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hdm.databaseaccess.IGroupAccessRightDao#hasUserChangeRightsRightForConcept(de.hdm.databaseaccess.IUnitOfWork,
	 * int, java.lang.String)
	 */
	@Override
	public boolean hasUserChangeRightsRightForConcept(IUnitOfWork unitOfWork, int userId, String conceptId)
			throws DataAccessException {
		return this.hasUserRightForConcept(unitOfWork, userId, conceptId, "changeRightsRight", "change rights right");
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

	private void executeInsertGroupAccessRight(IUnitOfWork unitOfWork, IGroupAccessRight groupAccessRight)
			throws DataAccessException {
		String sql = "INSERT INTO GroupAccessRight (groupId, conceptId, readRight, updateRight, deleteRight, changeRightsRight) VALUES (?, ?, ?, ?, ?, ?)";
		Connection connection = null;
		try {
			connection = this.getConnection(unitOfWork);
			PreparedStatement insertGroupAccessRightStatement = connection.prepareStatement(sql);
			insertGroupAccessRightStatement.setInt(1, groupAccessRight.getGroupId());
			insertGroupAccessRightStatement.setString(2, groupAccessRight.getConceptId());
			insertGroupAccessRightStatement.setBoolean(3, groupAccessRight.hasReadRight());
			insertGroupAccessRightStatement.setBoolean(4, groupAccessRight.hasUpdateRight());
			insertGroupAccessRightStatement.setBoolean(5, groupAccessRight.hasDeleteRight());
			insertGroupAccessRightStatement.setBoolean(6, groupAccessRight.hasChangeRightsRight());
			insertGroupAccessRightStatement.execute();
			insertGroupAccessRightStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("The access right of group " + groupAccessRight.getGroupId()
					+ " for the concept " + groupAccessRight.getConceptId()
					+ "could not be inserted on account of an error with the database!", e);
		} finally {
			this.closeConnection(unitOfWork, connection);
		}
	}

	private boolean hasUserRightForConcept(IUnitOfWork unitOfWork, int userId, String conceptId, String rightColumnName,
			String rightName) throws DataAccessException {
		Checker.checkIntegerGreaterEquals(userId, "userId", 1);
		Checker.checkNullAndEmptiness(conceptId, "conceptId");
		String sql = "SELECT * FROM GroupAccessRight WHERE GroupAccessRight." + rightColumnName
				+ " = true AND GroupAccessRight.conceptId = ? AND GroupAccessRight.groupId IN (SELECT UserBelongsToGroup.groupId FROM UserBelongsToGroup WHERE UserBelongsToGroup.userId = ?)";
		boolean result = false;
		Connection connection = null;
		try {
			connection = this.getConnection(unitOfWork);
			PreparedStatement selectUserRightStatement = connection.prepareStatement(sql);
			selectUserRightStatement.setString(1, conceptId);
			selectUserRightStatement.setInt(2, userId);
			ResultSet resultSet = selectUserRightStatement.executeQuery();
			while (resultSet.next() && result == false) {
				result = true;
			}
			resultSet.close();
			selectUserRightStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Could not check " + rightName + " of user " + userId + " for concept "
					+ conceptId + " on account of an error with the database!", e);
		} finally {
			this.closeConnection(unitOfWork, connection);
		}
		return result;
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
