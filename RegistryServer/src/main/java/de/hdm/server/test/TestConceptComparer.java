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
package de.hdm.server.test;

import de.hdm.datatypes.Concept;
import de.hdm.datatypes.DataTypeEnum;
import de.hdm.datatypes.IConcept;
import de.hdm.helpers.test.TestEntities;
import de.hdm.server.ConceptComparer;

public class TestConceptComparer {

	public static void main(String[] args) {
		IConcept concept1 = TestEntities.createConcepts().get(0);
		IConcept concept2 = TestEntities.createConcepts().get(0);
		ConceptComparer cc = new  ConceptComparer();
		System.out.println(cc.compareConcepts(concept1, concept2));
		
		concept2.setDataType(DataTypeEnum.BOOLEAN);
		
		concept2.getAuthors().remove(2);
		concept2.getAuthors().add("author d");
		
		concept2.getOwners().remove(0);
		concept2.getOwners().add(5);
		
		System.out.println(cc.compareConcepts(concept1, concept2));
	}

}
