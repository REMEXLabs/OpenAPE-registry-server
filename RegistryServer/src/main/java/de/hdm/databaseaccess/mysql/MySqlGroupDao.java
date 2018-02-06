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
import de.hdm.databaseaccess.IGroupDao;
import de.hdm.databaseaccess.IUnitOfWork;
import de.hdm.databaseaccess.UnitOfWorkStateEnum;
import de.hdm.datatypes.Group;
import de.hdm.datatypes.GroupMember;
import de.hdm.datatypes.IGroup;
import de.hdm.datatypes.IGroupMember;
import de.hdm.helpers.Checker;

/**
 * This class implements the database access for groups on MySql databases.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class MySqlGroupDao extends MySqlDao implements IGroupDao {
	
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
	public MySqlGroupDao() {
	
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
	 * @see de.hdm.databaseaccess.IGroupDao#insertGroup(de.hdm.databaseaccess.IUnitOfWork, de.hdm.datatypes.IGroup)
	 */
	@Override
	public void insertGroup(IUnitOfWork unitOfWork, IGroup group) throws DataAccessException{
		Checker.checkNull(group, "group");
		if(unitOfWork == null){
			this.insertGroupNotAsPartOfATransaction(group);
		}else{
			try {
				this.executeInsertGroup(unitOfWork, group);
			} catch (Exception e) {
				e.printStackTrace();
				throw new DataAccessException("The group " + group.getId() + " could not be inserted on account of an error!", e);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IGroupDao#selectGroupById(de.hdm.databaseaccess.IUnitOfWork, int)
	 */
	@Override
	public IGroup selectGroupById(IUnitOfWork unitOfWork, int groupId) throws DataAccessException {
		Checker.checkIntegerGreaterEquals(groupId, "groupId", 1);
		String sqlCondition = "WHERE id = " + groupId;
		List<IGroup> groups = this.selectGroups(unitOfWork, sqlCondition);
		if(groups.isEmpty()){
			// TODO ???
		}
		return groups.get(0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IGroupDao#selectGroupsWhereUserIsAdmin(de.hdm.databaseaccess.IUnitOfWork, int)
	 */
	@Override
	public List<IGroup> selectGroupsWhereUserIsAdmin(IUnitOfWork unitOfWork, int userId) throws DataAccessException {
		Checker.checkUserId(userId);
		String sqlCondition = "WHERE id IN (SELECT groupId FROM UserBelongsToGroup WHERE userId = " + userId + ")";
		return this.selectGroups(unitOfWork, sqlCondition);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IGroupDao#selectAllGroups(de.hdm.databaseaccess.IUnitOfWork)
	 */
	@Override
	public List<IGroup> selectAllGroups(IUnitOfWork unitOfWork) throws DataAccessException {
		return this.selectGroups(unitOfWork, null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IGroupDao#selectAllGroupsWhereUserIsMember(de.hdm.databaseaccess.IUnitOfWork, int)
	 */
	@Override
	public List<IGroup> selectAllGroupsWhereUserIsMember(IUnitOfWork unitOfWork, int userId) throws DataAccessException {
		Checker.checkUserId(userId);
		String sqlCondition = "WHERE id IN (SELECT groupId FROM UserBelongsToGroup WHERE userId = " + userId + ")";
		return this.selectGroups(unitOfWork, sqlCondition);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IGroupDao#updateGroup(de.hdm.databaseaccess.IUnitOfWork, de.hdm.datatypes.IGroup)
	 */
	@Override
	public void updateGroup(IUnitOfWork unitOfWork, IGroup group) throws DataAccessException {
		Checker.checkNull(group, "group");
		if(unitOfWork == null){
			this.updateGroupNotAsPartOfATransaction(group);
		}else{
			try {
				this.executeUpdateGroup(unitOfWork, group);
			} catch (Exception e) {
				e.printStackTrace();
				throw new DataAccessException("The group " + group.getId() + " could not be updated on account of an error!", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IGroupDao#deleteGroup(de.hdm.databaseaccess.IUnitOfWork, int)
	 */
	@Override
	public void deleteGroup(IUnitOfWork unitOfWork, int groupId) throws DataAccessException {
		Checker.checkIntegerGreaterEquals(groupId, "groupId", 1);
		Connection connection = this.getConnection(unitOfWork);
		try {
			PreparedStatement deleteGroupStatement = connection.prepareStatement("DELETE FROM `Group` WHERE id = ?");
			deleteGroupStatement.setInt(1, groupId);
			deleteGroupStatement.executeUpdate();
			deleteGroupStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Could not delete the group " + groupId + " on account of an error with the database!", e);
		} finally {
			this.closeConnection(unitOfWork, connection);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IGroupDao#isUserAdminOfAtLeastOneGroup(de.hdm.databaseaccess.IUnitOfWork, int)
	 */
	@Override
	public boolean isUserAdminOfAtLeastOneGroup(IUnitOfWork unitOfWork, int userId) throws DataAccessException {
		Checker.checkUserId(userId);
		boolean isAdminOfAtLeastOneGroup = false;
		Connection connection = this.getConnection(unitOfWork);
		try {
			PreparedStatement selectIsUserAdminOfAtLeastOneGroupStatement = connection.prepareStatement("SELECT Count(*) FROM UserBelongsToGroup WHERE userId = ? AND groupAdmin = true");
			selectIsUserAdminOfAtLeastOneGroupStatement.setInt(1, userId);
			ResultSet resultSet = selectIsUserAdminOfAtLeastOneGroupStatement.executeQuery();
			while (resultSet.next()) {
				if(resultSet.getInt(1) > 0){
					isAdminOfAtLeastOneGroup = true;	
				}
			}
			resultSet.close();
			selectIsUserAdminOfAtLeastOneGroupStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Could not select whether user " + userId + " is admin of at least one group or not on account of an error with the database!", e);
		} finally {
			this.closeConnection(unitOfWork, connection);
		}
		return isAdminOfAtLeastOneGroup;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IGroupDao#containsGroupName(de.hdm.databaseaccess.IUnitOfWork, java.lang.String)
	 */
	@Override
	public boolean containsGroupName(IUnitOfWork unitOfWork, String groupName) throws DataAccessException {
		Checker.checkNullAndEmptiness(groupName, "groupName");
		boolean contains = false;
		Connection connection = this.getConnection(unitOfWork);
		try {
			PreparedStatement containsGroupNameStatement = connection.prepareStatement("SELECT Count(*) FROM `Group` WHERE name = ?");
			containsGroupNameStatement.setString(1, groupName);
			ResultSet resultSet = containsGroupNameStatement.executeQuery();
			while (resultSet.next()) {
				if(resultSet.getInt(1) > 0){
					contains = true;	
				}
			}
			resultSet.close();
			containsGroupNameStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Could not check whether there exists already a group with the name " + groupName + " on account of an error with the database!", e);
		} finally {
			this.closeConnection(unitOfWork, connection);
		}
		return contains;
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

	private List<IGroup> selectGroups(IUnitOfWork unitOfWork, String sqlCondition) throws DataAccessException {
		List<IGroup> groups = new ArrayList<IGroup>();
		Connection connection = this.getConnection(unitOfWork);
		try {
			String sql = "SELECT id, name FROM `Group`";
			if(sqlCondition != null){
				sql += " " + sqlCondition;
			}
			PreparedStatement selectAllGroupsStatement = connection.prepareStatement(sql);
			ResultSet resultSet = selectAllGroupsStatement.executeQuery();
			while (resultSet.next()) {
				groups.add(this.createGroup(unitOfWork, resultSet));
			}
			resultSet.close();
			selectAllGroupsStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Could not select groups on account of an error with the database!", e);
		} finally {
			this.closeConnection(unitOfWork, connection);
		}
		return groups;
	}

	private IGroup createGroup(IUnitOfWork unitOfWork, ResultSet resultSetGroup) throws SQLException {
		int id = resultSetGroup.getInt(1);
		String name = resultSetGroup.getString(2);
		List<IGroupMember> members = new ArrayList<IGroupMember>();
		Connection connection = this.getConnection(unitOfWork);
		PreparedStatement selectAllGroupMembersStatement = connection.prepareStatement("SELECT userId, groupAdmin FROM UserBelongsToGroup WHERE groupId = ?");
		selectAllGroupMembersStatement.setInt(1, id);
		ResultSet resultSetGroupMembers = selectAllGroupMembersStatement.executeQuery();
		while(resultSetGroupMembers.next()){
			members.add(new GroupMember(resultSetGroupMembers.getInt(1), resultSetGroupMembers.getBoolean(2)));
		}
		resultSetGroupMembers.close();
		selectAllGroupMembersStatement.close();
		this.closeConnection(unitOfWork, connection);
		return new Group(id, name, members);
	}
	
	private void insertGroupNotAsPartOfATransaction(IGroup group) throws DataAccessException {
		IUnitOfWork unitOfWork = this.createUnitOfWork();
		try {
			unitOfWork.start();
			this.executeInsertGroup(unitOfWork, group);
		} catch (DataAccessException e) {
			e.printStackTrace();
			unitOfWork.abort();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			unitOfWork.abort();
			throw new DataAccessException(
					"The group " + group.getId() + " could not be created on account of an error!", e);
		} finally {
			if (unitOfWork != null && unitOfWork.getState() == UnitOfWorkStateEnum.STARTED) {
				unitOfWork.finish();
			}
		}
	}
	
	private void executeInsertGroup(IUnitOfWork unitOfWork, IGroup group) throws SQLException, DataAccessException{
		Connection connection = this.getConnection(unitOfWork);
		PreparedStatement insertGrouptStatement = connection.prepareStatement("INSERT INTO `Group` (name) VALUES(?)");
		insertGrouptStatement.setString(1, group.getName());
		insertGrouptStatement.executeUpdate();
		insertGrouptStatement.close();
		this.closeConnection(unitOfWork, connection);
		
		group.setId(this.selectGroupIdByName(unitOfWork, group.getName()));
		
		this.insertGroupMembers(unitOfWork, group);
	}
	
	private int selectGroupIdByName(IUnitOfWork unitOfWork, String groupName) throws SQLException {
		Checker.checkNullAndEmptiness(groupName, "groupName");
		int groudId = -1;
		Connection connection = this.getConnection(unitOfWork);
		PreparedStatement selectGroupIdByNameStatement = connection.prepareStatement("SELECT id FROM `Group` WHERE name = ?");
		selectGroupIdByNameStatement.setString(1, groupName);
		ResultSet resultSet = selectGroupIdByNameStatement.executeQuery();
		while (resultSet.next()) {
			groudId = resultSet.getInt(1);
		}
		resultSet.close();
		selectGroupIdByNameStatement.close();
		this.closeConnection(unitOfWork, connection);
		return groudId;
	}
	
	private void updateGroupNotAsPartOfATransaction(IGroup group) throws DataAccessException {
		IUnitOfWork unitOfWork = this.createUnitOfWork();
		try {
			unitOfWork.start();
			this.executeUpdateGroup(unitOfWork, group);
		} catch (DataAccessException e) {
			e.printStackTrace();
			unitOfWork.abort();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			unitOfWork.abort();
			throw new DataAccessException(
					"The group " + group.getId() + " could not be updated on account of an error!", e);
		} finally {
			if (unitOfWork != null && unitOfWork.getState() == UnitOfWorkStateEnum.STARTED) {
				unitOfWork.finish();
			}
		}
	}
	
	private void executeUpdateGroup(IUnitOfWork unitOfWork, IGroup group) throws DataAccessException, SQLException{
		Connection connection = this.getConnection(unitOfWork);
		PreparedStatement updateGroupStatement = connection.prepareStatement("UPDATE `Group`SET name = ? WHERE id = ?");
		updateGroupStatement.setString(1, group.getName());
		updateGroupStatement.setInt(2, group.getId());
		updateGroupStatement.executeUpdate();
		updateGroupStatement.close();
		this.closeConnection(unitOfWork, connection);
		
		this.deleteGroupMembers(unitOfWork, group);
		this.insertGroupMembers(unitOfWork, group);
	}
	
	private void insertGroupMembers(IUnitOfWork unitOfWork, IGroup group) throws SQLException{
		for(IGroupMember groupMember : group.getMembers()){
			Connection connection = this.getConnection(unitOfWork);
			PreparedStatement insertGroupMembersStatement = connection.prepareStatement("INSERT INTO UserBelongsToGroup (userId, groupId, groupAdmin) VALUES (?, ?, ?)");
			insertGroupMembersStatement.setInt(1, groupMember.getUserId());
			insertGroupMembersStatement.setInt(2, group.getId());
			insertGroupMembersStatement.setBoolean(3, groupMember.isGroupAdmin());
			insertGroupMembersStatement.executeUpdate();
			insertGroupMembersStatement.close();
			this.closeConnection(unitOfWork, connection);
		}
	}
	
	private void deleteGroupMembers(IUnitOfWork unitOfWork, IGroup group) throws SQLException{
		Connection connection = this.getConnection(unitOfWork);
		PreparedStatement deleteGroupMembersStatement = connection.prepareStatement("DELETE FROM UserBelongsToGroup WHERE groupId = ?");
		deleteGroupMembersStatement.setInt(1, group.getId());
		deleteGroupMembersStatement.executeUpdate();
		deleteGroupMembersStatement.close();
		this.closeConnection(unitOfWork, connection);
	}

	
	

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
