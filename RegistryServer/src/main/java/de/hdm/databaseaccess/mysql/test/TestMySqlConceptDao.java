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
import de.hdm.databaseaccess.mysql.MySqlConceptDao;
import de.hdm.datatypes.IConcept;

public class TestMySqlConceptDao {

	public static void main(String[] args) throws DataAccessException {
		MySqlConceptDao m = new MySqlConceptDao();
		//IConcept concept = m.selectConcept(null, "idX18");
		//System.out.println(concept.getValueSpace().getJsonSchema());
		
		//List<IConcept> concepts = m.selectConceptsWhichRefineConcept(null, "id111");
		System.out.println("start");
		List<IConcept> concepts = m.selectAllConcepts(null, 1);
        System.out.println("after database request");
		for(IConcept concept : concepts){
			System.out.println(concept.getId());
		}
		
		//m.insertConcept(null, TestEntities.createConcepts().get(0));
		//m.insertConcept(null, TestEntities.createConcepts().get(1));
		//m.insertConcept(null, TestEntities.createConcepts().get(2));
		//m.selectAllConcepts(null, 1);
		//ArrayList<TypeEnum> types = new ArrayList<TypeEnum>();
		//types.add(TypeEnum.NEED_AND_PREFERENCE);
		//ConceptFilter c = new ConceptFilter(new GregorianCalendar(), types, 100, 0);
		//System.out.println(m.createSqlForSelectConcepts(1, c));
		
		//System.out.println(m.createSqlForSelectConcepts(1, null));
		
		//System.out.println(m.existsConcept(null, "id33"));
		//System.out.println(m.existsConcept(null, "id10"));
		//System.out.println(m.selectTotalNumberOfConcepts(null));

	}

}
