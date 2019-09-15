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
package de.hdm.helpers.test;

import java.util.ArrayList;
import java.util.List;

import de.hdm.datatypes.Concept;
import de.hdm.datatypes.DataTypeEnum;
import de.hdm.datatypes.Definition;
import de.hdm.datatypes.GroupAccessRight;
import de.hdm.datatypes.IConcept;
import de.hdm.datatypes.IDefinition;
import de.hdm.datatypes.IExample;
import de.hdm.datatypes.IGroupAccessRight;
import de.hdm.datatypes.IName;
import de.hdm.datatypes.INote;
import de.hdm.datatypes.IUser;
import de.hdm.datatypes.Name;
import de.hdm.datatypes.SubTypeEnum;
import de.hdm.datatypes.TypeEnum;
import de.hdm.datatypes.User;
import de.hdm.datatypes.ValueSpace;

public class TestEntities {

	public static List<IConcept> createConcepts(){
		String concept1Id = "id1";
		IConcept concept1 = new Concept(concept1Id);
		concept1.setType(TypeEnum.NEED_AND_PREFERENCE);
		concept1.setOrigin("common");
		List<IDefinition> definitions1 = new ArrayList<IDefinition>();
		definitions1.add(new Definition("DE", concept1Id, "definition1"));
		definitions1.add(new Definition("AUT", concept1Id, "definition1"));
		definitions1.add(new Definition("FR", concept1Id, "definition1"));
		concept1.setDefinitions(definitions1);
		List<IName> names1 = new ArrayList<IName>();
		names1.add(new Name("DE", concept1Id, "name1"));
		names1.add(new Name("AUT", concept1Id, "name1"));
		names1.add(new Name("FR", concept1Id, "name1"));
		concept1.setNames(names1);
		concept1.setDataType(DataTypeEnum.STRING);
		concept1.setValueSpace(new ValueSpace("schema1"));
		concept1.setConceptsWhichAreTransformedByThisConcept(new ArrayList<String>());
		concept1.setConceptsWhichAreRefinedByThisConcept(new ArrayList<String>());
		concept1.setNotes(new ArrayList<INote>());
		concept1.setExamples(new ArrayList<IExample>());
		List<String> authors1 = new ArrayList<String>();
		authors1.add("author a");
		authors1.add("author b");
		authors1.add("author c");
		concept1.setAuthors(authors1);
		List<Integer> owners1 = new ArrayList<Integer>();
		owners1.add(1);
		owners1.add(2);
		owners1.add(3);
		concept1.setOwners(owners1);

		IConcept concept2 = new Concept("id2");
		concept2.setType(TypeEnum.NEED_AND_PREFERENCE);
		concept2.setOrigin("common");
		List<IDefinition> definitions2 = new ArrayList<IDefinition>();
		definitions2.add(new Definition("DE", concept1Id, "definition2"));
		definitions2.add(new Definition("AUT", concept1Id, "definition2"));
		definitions2.add(new Definition("FR", concept1Id, "definition2"));
		concept2.setDefinitions(definitions2);
		List<IName> names2 = new ArrayList<IName>();
		names2.add(new Name("DE", concept1Id, "name2"));
		names2.add(new Name("AUT", concept1Id, "name2"));
		names2.add(new Name("FR", concept1Id, "name2"));
		concept2.setNames(names2);
		concept2.setDataType(DataTypeEnum.STRING);
		concept2.setValueSpace(new ValueSpace("schema2"));
		concept2.setConceptsWhichAreTransformedByThisConcept(new ArrayList<String>());
		concept2.setConceptsWhichAreRefinedByThisConcept(new ArrayList<String>());
		concept2.setNotes(new ArrayList<INote>());
		concept2.setExamples(new ArrayList<IExample>());
		List<String> authors2 = new ArrayList<String>();
		authors2.add("author a");
		authors2.add("author b");
		authors2.add("author c");
		concept2.setAuthors(authors2);
		List<Integer> owners2 = new ArrayList<Integer>();
		owners2.add(1);
		owners2.add(2);
		owners2.add(3);
		concept2.setOwners(owners2);

		IConcept concept3 = new Concept("id3");
		concept3.setType(TypeEnum.NEED_AND_PREFERENCE);
		concept3.setOrigin("common");
		List<IDefinition> definitions3 = new ArrayList<IDefinition>();
		definitions3.add(new Definition(null, concept1Id, "definition3"));
		definitions3.add(new Definition(null, concept1Id, "definition3"));
		definitions3.add(new Definition("FR", concept1Id, "definition3"));
		concept3.setDefinitions(definitions3);
		List<IName> names3 = new ArrayList<IName>();
		names3.add(new Name("DE", concept1Id, "name3"));
		names3.add(new Name("AUT", concept1Id, "name3"));
		names3.add(new Name("FR", concept1Id, "name3"));
		concept3.setNames(names3);
		concept3.setDataType(DataTypeEnum.STRING);
		concept3.setValueSpace(new ValueSpace("schema3"));
		concept3.setConceptsWhichAreTransformedByThisConcept(new ArrayList<String>());
		concept3.setConceptsWhichAreRefinedByThisConcept(new ArrayList<String>());
		concept3.setNotes(new ArrayList<INote>());
		concept3.setExamples(new ArrayList<IExample>());
		List<String> authors3 = new ArrayList<String>();
		authors3.add("author a");
		authors3.add("author b");
		authors3.add("author c");
		concept3.setAuthors(authors3);
		List<Integer> owners3 = new ArrayList<Integer>();
		owners3.add(1);
		owners3.add(2);
		owners3.add(3);
		concept3.setOwners(owners3);

		List<IConcept> concepts = new ArrayList<IConcept>();
		concepts.add(concept1);
		concepts.add(concept2);
		concepts.add(concept3);
		return concepts;
	}

	public static List<IUser> createUsers(){
		User user1 = new User();
		user1.setUserName("userName1");
		user1.setFirstName("firstName1");
		user1.setLastName("lastName1");
		user1.setInstitute("institute1");
		user1.seteMailAddress("mail1");
		user1.setHashOfPassword("password1");
		user1.setHashOfResetPassword(null);
		user1.setApiKey("apiKey1");
		user1.setSuperAdmin(true);

		User user2 = new User();
		user2.setUserName("userName2");
		user2.setFirstName("firstName2");
		user2.setLastName("lastName2");
		user2.setInstitute("institute2");
		user2.seteMailAddress("mail2");
		user2.setHashOfPassword("password2");
		user2.setHashOfResetPassword(null);
		user2.setApiKey("apiKey2");
		user2.setSuperAdmin(false);

		User user3 = new User();
		user3.setUserName("userName3");
		user3.setFirstName("firstName3");
		user3.setLastName("lastName3");
		user3.setInstitute(null);
		user3.seteMailAddress("mail3");
		user3.setHashOfPassword("password3");
		user3.setHashOfResetPassword(null);
		user3.setApiKey(null);
		user3.setSuperAdmin(false);

		List<IUser> users = new ArrayList<IUser>();
		users.add(user1);
		users.add(user2);
		users.add(user3);

		return users;
	}

	public static List<IGroupAccessRight> createGroupAccessRights(){
		GroupAccessRight g1 = new GroupAccessRight(1, "id1", true, true, false, false);
		GroupAccessRight g2 = new GroupAccessRight(1, "id2", true, true, false, false);
		GroupAccessRight g3 = new GroupAccessRight(1, "id3", false, false, false, false);


		List<IGroupAccessRight> groupAccessRights = new ArrayList<IGroupAccessRight>();
		groupAccessRights.add(g1);
		groupAccessRights.add(g2);
		groupAccessRights.add(g3);
		return groupAccessRights;
	}
}
