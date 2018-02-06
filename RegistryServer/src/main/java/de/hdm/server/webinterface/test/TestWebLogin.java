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
package de.hdm.server.webinterface.test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import de.hdm.configuration.MyProperties;
import de.hdm.databaseaccess.DaoFactory;
import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IUserDao;
import de.hdm.datatypes.IUser;
import de.hdm.datatypes.User;
import de.hdm.server.PasswordEncoder;

public class TestWebLogin {

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, DataAccessException {
		IUser user = new User();
		user.setUserName("toabl");
		user.setFirstName("Tobias");
		user.setLastName("Ableitner");
		user.seteMailAddress("tobias.ableitner@t-online.de");
		user.setSuperAdmin(true);
		user.setApiKey("apiKeyToAbl");
		user.setInstitute("HDM");
		user.setHashOfPassword(MyProperties.getPrefixForHashOfPassword() + PasswordEncoder.encode("12345" + MyProperties.getSuffixForPassword()));
		
		IUserDao userDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createUserDao();
		userDao.insertUser(null, user);
		
		String password = "12345";
		String encodedPassword = PasswordEncoder.encode(password + MyProperties.getSuffixForPassword());
		System.out.println(encodedPassword);
		System.out.println(PasswordEncoder.matches(password + MyProperties.getSuffixForPassword(), encodedPassword));
	}

}
