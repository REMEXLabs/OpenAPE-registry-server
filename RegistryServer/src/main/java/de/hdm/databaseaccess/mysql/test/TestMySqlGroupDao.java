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
package de.hdm.databaseaccess.mysql.test;

import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IGroupDao;
import de.hdm.databaseaccess.mysql.MySqlGroupDao;
import de.hdm.datatypes.IGroup;

public class TestMySqlGroupDao {

	public static void main(String[] args) throws DataAccessException {
		IGroupDao groupDao = new MySqlGroupDao();
		IGroup group = groupDao.selectGroupById(null, 2);
		group.setName("group2");
		groupDao.updateGroup(null, group);
		
		System.out.println(groupDao.selectAllGroups(null).size());
		System.out.println(groupDao.selectGroupsWhereUserIsAdmin(null, 1).size());
	}

}
