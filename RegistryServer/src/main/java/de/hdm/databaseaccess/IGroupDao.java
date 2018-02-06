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

import de.hdm.datatypes.IGroup;

// TODO java doc
public interface IGroupDao extends IDao {

	public void insertGroup(IUnitOfWork unitOfWork, IGroup group) throws DataAccessException;
	
	public IGroup selectGroupById(IUnitOfWork unitOfWork, int groupId) throws DataAccessException;
	
	public List<IGroup> selectGroupsWhereUserIsAdmin(IUnitOfWork unitOfWork, int userId) throws DataAccessException;
	
	public List<IGroup> selectAllGroups(IUnitOfWork unitOfWork) throws DataAccessException;
	
	public List<IGroup> selectAllGroupsWhereUserIsMember(IUnitOfWork unitOfWork, int userId) throws DataAccessException;
	
	public void updateGroup(IUnitOfWork unitOfWork, IGroup group) throws DataAccessException;
	
	public void deleteGroup(IUnitOfWork unitOfWork, int groupId) throws DataAccessException;
	
	public boolean isUserAdminOfAtLeastOneGroup(IUnitOfWork unitOfWork, int userId) throws DataAccessException;
	
	public boolean containsGroupName(IUnitOfWork unitOfWork, String groupName) throws DataAccessException;
	
}
