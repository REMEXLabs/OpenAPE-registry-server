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
package de.hdm.server.jsonapi.test;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.hdm.databaseaccess.ConceptFilter;
import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IConceptFilter;
import de.hdm.datatypes.IConcept;
import de.hdm.datatypes.TypeEnum;
import de.hdm.helpers.test.TestEntities;
import de.hdm.server.jsonapi.IJsonHandler;
import de.hdm.server.jsonapi.JsonHandler;
import de.hdm.server.jsonapi.JsonHandlerException;

public class TestJsonHandler {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws JsonHandlerException, ParseException, DataAccessException {
		IJsonHandler jsonHandler = new JsonHandler(null);
		
		/*List<IConcept> concepts = TestEntities.createConcepts();
		
		//System.out.println(jsonHandler.conceptToJson(concepts.get(0)));
		
		//System.out.println(jsonHandler.conceptsToJson(concepts));
		
			
		//String conceptsAsJson = "[{\"notes\":[],\"origin\":\"common\",\"refines\":[],\"valueSpace\":\"schema1\",\"type\":\"NeedAndPreference\",\"transformationOf\":[],\"termLabel\":[{\"languageCode\":\"DE\",\"value\":\"name1\"},{\"languageCode\":\"AUT\",\"value\":\"name1\"},{\"languageCode\":\"FR\",\"value\":\"name1\"}],\"examples\":[],\"subtype\":\"alias\",\"datatype\":\"String\",\"conceptId\":\"id1\",\"definition\":[{\"languageCode\":\"DE\",\"value\":\"definition1\"},{\"languageCode\":\"AUT\",\"value\":\"definition1\"},{\"languageCode\":\"FR\",\"value\":\"definition1\"}],\"authors\":[\"author a\",\"author b\",\"author c\"]},{\"notes\":[],\"origin\":\"common\",\"refines\":[],\"valueSpace\":\"schema2\",\"type\":\"NeedAndPreference\",\"transformationOf\":[],\"termLabel\":[{\"languageCode\":\"DE\",\"value\":\"name2\"},{\"languageCode\":\"AUT\",\"value\":\"name2\"},{\"languageCode\":\"FR\",\"value\":\"name2\"}],\"examples\":[],\"subtype\":\"alias\",\"datatype\":\"String\",\"conceptId\":\"id2\",\"definition\":[{\"languageCode\":\"DE\",\"value\":\"definition2\"},{\"languageCode\":\"AUT\",\"value\":\"definition2\"},{\"languageCode\":\"FR\",\"value\":\"definition2\"}],\"authors\":[\"author a\",\"author b\",\"author c\"]},{\"notes\":[],\"origin\":\"common\",\"refines\":[],\"valueSpace\":\"schema3\",\"type\":\"NeedAndPreference\",\"transformationOf\":[],\"termLabel\":[{\"languageCode\":\"DE\",\"value\":\"name3\"},{\"languageCode\":\"AUT\",\"value\":\"name3\"},{\"languageCode\":\"FR\",\"value\":\"name3\"}],\"examples\":[],\"subtype\":\"alias\",\"datatype\":\"String\",\"conceptId\":\"id3\",\"definition\":[{\"languageCode\":\"\",\"value\":\"definition3\"},{\"languageCode\":null,\"value\":\"definition3\"},{\"languageCode\":\"FR\",\"value\":\"definition3\"}],\"authors\":[\"author a\",\"author b\",\"author c\"]}]";
		//List<IConcept> concepts2 = jsonHandler.jsonToConcepts(conceptsAsJson);
		/*for(IConcept n : concepts2){
			if(n.getId() == null){
				System.out.println("conceptId is null");
			}else{
				
			}
			System.out.println(n.getId());
		}
		
		System.out.println(concepts2.get(0).getNames().get(0).getLanguageCode().isEmpty());
		
		System.out.println(jsonHandler.conceptsToJson(concepts2));*/
		
		//System.out.println(jsonHandler.createGetConceptSuccessJsonResponse(concepts.get(0), System.currentTimeMillis()));
		
		/*List<TypeEnum> types = new ArrayList<TypeEnum>();
		types.add(TypeEnum.CONTEXT_DESCRIPTION);
		types.add(TypeEnum.NEED_AND_PREFERENCE);
		IConceptFilter conceptFilter = new ConceptFilter(new GregorianCalendar(), types, 100, 0);
		System.out.println(jsonHandler.createGetConceptsSuccessJsonResponse(concepts, conceptFilter, System.currentTimeMillis(), 10));
	*/
	
		/*JSONObject object = new JSONObject();
		object.put("nullAsString", "null");
		object.put("null", null);
		System.out.println(object.toJSONString());
		*/
		/*
		JSONParser parser = new JSONParser();
		JSONObject object = (JSONObject)parser.parse("{\"nullAsString\":\"null\",\"null\":null}");
		
		if(object.get("nullAsString") == null){
			System.out.println("really null");
		}else{
			System.out.println("not really null");
		}
		
		if(object.get("null") == null){
			System.out.println("really null");
		}else{
			System.out.println("not really null");
		}
		
		if(object.get("plapla") == null){
			System.out.println("really null");
		}else{
			System.out.println("not really null");
		}
		
		System.out.println(object.get("nullAsString"));
		System.out.println(object.get("null"));
		System.out.println(object.get("plapla"));*/
		
		jsonHandler.jsonToConcept(null, false);
	}

}
