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

import java.util.List;

import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.mysql.MySqlUserDao;
import de.hdm.datatypes.IUser;
import de.hdm.helpers.test.TestEntities;

public class TestMySqlUserDao {

	public static void main(String[] args) throws DataAccessException{
		MySqlUserDao m = new MySqlUserDao();
		//List<IUser> users = TestEntities.createUsers();
		//m.insertUser(null, users.get(0));
		//m.insertUser(null, users.get(1));
		//m.insertUser(null, users.get(2));
		//System.out.println("userId = " + m.selectUserIdForApiKey(null, "apiKey2"));
		//System.out.println(m.selectUser(null, 2).getHashOfResetPassword());
		//m.deleteUser(null, 2);
		//System.out.println(m.isUserSuperAdmin(null, 5));
		IUser user = m.selectUser(null, "toabl");
		user.setFirstName("test");
		//user.setSessionId("test");
		//user.setId(238219381);
		//user.setExpiryDateOfResetPasswordInMilliseconds(-1);
		//user.setHashOfResetPassword(null);
		m.updateUser(null, user);
		//System.out.println(System.currentTimeMillis());
	}
	
}
