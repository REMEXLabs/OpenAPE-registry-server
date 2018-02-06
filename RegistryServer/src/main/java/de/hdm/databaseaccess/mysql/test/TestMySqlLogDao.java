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
import de.hdm.databaseaccess.ILogDao;
import de.hdm.databaseaccess.mysql.MySqlLogDao;
import de.hdm.datatypes.ActionEnum;
import de.hdm.datatypes.ILog;
import de.hdm.datatypes.Log;

public class TestMySqlLogDao {

	public static void main(String[] args) throws DataAccessException {		
		ILogDao ld = new MySqlLogDao();
		
		ILog log1 = new Log();
		log1.setConceptId("id1");
		log1.setUserId(1);
		log1.setAction(ActionEnum.READ);
		log1.setDateTimeStampInMilliseconds(System.currentTimeMillis());
		log1.setSuccess(true);
		log1.setNote("");
		
		ILog log2 = new Log();
		log2.setConceptId("id2");
		log2.setUserId(1);
		log2.setAction(ActionEnum.READ);
		log2.setDateTimeStampInMilliseconds(System.currentTimeMillis());
		log2.setSuccess(true);
		log2.setNote("note");
		
		ILog log3 = new Log();
		log3.setConceptId("id3");
		log3.setUserId(1);
		log3.setAction(ActionEnum.READ);
		log3.setDateTimeStampInMilliseconds(System.currentTimeMillis());
		log3.setSuccess(true);
		log3.setNote(null);
		
		ILog log4 = new Log();
		log4.setConceptId("id1");
		log4.setUserId(1);
		log4.setAction(ActionEnum.UPDATE);
		log4.setDateTimeStampInMilliseconds(System.currentTimeMillis());
		log4.setSuccess(true);
		log4.setNote(null);
		
		//ld.insertLog(null, log1);
		//ld.insertLog(null, log2);
		//ld.insertLog(null, log3);
		//ld.insertLog(null, log4);
		
		System.out.println(ld.updatedSinceUserRed(null, 8, "C24"));

	}

}
