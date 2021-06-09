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
import de.hdm.databaseaccess.IGroupAccessRightDao;
import de.hdm.databaseaccess.mysql.MySqlGroupAccessRightDao;
import de.hdm.helpers.test.TestEntities;

public class TestMySqlGroupAccessRight {

	public static void main(String[] args) throws DataAccessException {
		IGroupAccessRightDao gard = new MySqlGroupAccessRightDao();
		
		/*gard.deleteGroupAccessRight(null, 1, "id1");
		
		gard.insertGroupAccessRight(null, TestEntities.createGroupAccessRights().get(0));
		gard.insertGroupAccessRight(null, TestEntities.createGroupAccessRights().get(1));
		gard.insertGroupAccessRight(null, TestEntities.createGroupAccessRights().get(2));
		
		
		System.out.println(gard.hasUserReadRightForConcept(null, 1, "id1"));
		System.out.println(gard.hasUserUpdateRightForConcept(null, 1, "id1"));
		System.out.println(gard.hasUserDeleteRightForConcept(null, 1, "id1"));
		System.out.println(gard.hasUserChangeRightsRightForConcept(null, 1, "id1"));*/
		
		System.out.println(gard.isConceptPublic(null, "common_pitch"));
		System.out.println(gard.isConceptPublic(null, "zinser_fontSize"));

	}

}
