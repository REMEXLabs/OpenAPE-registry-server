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
package de.hdm.server;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.hdm.datatypes.IConcept;
import de.hdm.datatypes.IContent;
import de.hdm.datatypes.IGroupAccessRight;
import de.hdm.helpers.Checker;
import de.hdm.server.jsonapi.JsonFieldNames;

/**
 * This class provides methods to compare instances of the type {@link IConcept}.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class ConceptComparer implements IConceptComparer {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * JSON field name changes. The JSON object with this name contains all the difference between the two compared concepts.
	 */
	private static final String JSON_FIELD_CHANGES = "changes";
	
	/**
	 * JSON field name for the old value.
	 */
	private static final String JSON_FIELD_OLD_VALUE = "old";
	
	/**
	 * JSON field name for the new value.
	 */
	private static final String JSON_FIELD_NEW_VALUE = "new";
	
	/**
	 * JSON field name for removed values from a list.
	 */
	private static final String JSON_FIELD_REMOVED_VALUES = "removed";
	
	/**
	 * JSON field name for added values to a list.
	 */
	private static final String JSON_FIELD_ADDED_VALUES = "added";
	



	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Empty default constructor.
	 */
	public ConceptComparer() {

	}



	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// getters and setters
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	


	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// abstract methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	
	

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// override methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.server.IConceptComparer#compareConcepts(de.hdm.datatypes.IConcept, de.hdm.datatypes.IConcept)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String compareConcepts(IConcept oldConcept, IConcept newConcept){
		Checker.checkNull(oldConcept, "oldConcept");
		Checker.checkNull(newConcept, "newConcept");
		JSONObject jsonObject = new JSONObject();
		this.compareStringValues(jsonObject, JsonFieldNames.CONCEPT_ID, oldConcept.getId(), newConcept.getId());
		this.compareListWithContents(jsonObject, JsonFieldNames.TERM_LABEL, (List)oldConcept.getNames(), (List)newConcept.getNames());
		this.compareListWithContents(jsonObject, JsonFieldNames.DEFINITION, (List)oldConcept.getDefinitions(), (List)newConcept.getDefinitions());
		this.compareListWithContents(jsonObject, JsonFieldNames.NOTES, (List)oldConcept.getNotes(), (List)newConcept.getNotes());
		this.compareListWithContents(jsonObject, JsonFieldNames.EXAMPLES, (List)oldConcept.getExamples(), (List)newConcept.getExamples());
		this.compareStringValues(jsonObject, JsonFieldNames.TYPE, oldConcept.getType().toString(), newConcept.getType().toString());
		this.compareStringValues(jsonObject, JsonFieldNames.SUBTYPE, oldConcept.getSubType().toString(), newConcept.getSubType().toString());
		this.compareStringValues(jsonObject, JsonFieldNames.ORIGIN, oldConcept.getOrigin(), newConcept.getOrigin());
		this.compareStringValues(jsonObject, JsonFieldNames.DATATYPE, oldConcept.getDataType().toString(), newConcept.getDataType().toString());
		
		String oldJsonSchema = null;
		if(oldConcept.getValueSpace() != null){
			oldJsonSchema = oldConcept.getValueSpace().getJsonSchema();
		}
		String newJsonSchema = null;
		if(newConcept.getValueSpace() != null){
			newJsonSchema = newConcept.getValueSpace().getJsonSchema();
		}
		this.compareStringValues(jsonObject, JsonFieldNames.VALUE_SPACE, oldJsonSchema, newJsonSchema);
		
		this.compareListsWithStringsValues(jsonObject, JsonFieldNames.AUTHORS, oldConcept.getAuthors(), newConcept.getAuthors());
		this.compareListsWithIntegerValues(jsonObject, JsonFieldNames.OWNERS, oldConcept.getOwners(), newConcept.getOwners());
		this.compareListsWithStringsValues(jsonObject, JsonFieldNames.REFINES, oldConcept.getConceptsWhichAreRefinedByThisConcept(), newConcept.getConceptsWhichAreRefinedByThisConcept());
		this.compareListsWithStringsValues(jsonObject, JsonFieldNames.TRANSFORMATION_OF, oldConcept.getConceptsWhichAreTransformedByThisConcept(), newConcept.getConceptsWhichAreTransformedByThisConcept());
		this.compareListWithGroupAccessRights(jsonObject, JsonFieldNames.GROUP_ACCESS_RIGHTS, oldConcept.getGroupAccessRights(), newConcept.getGroupAccessRights());
		
		JSONObject jsonObjectRoot = new JSONObject();
		jsonObjectRoot.put(JSON_FIELD_CHANGES, jsonObject);
		return jsonObjectRoot.toJSONString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.server.IConceptComparer#hasGroupAccessRightsChanged(java.util.List, java.util.List)
	 */
	@Override
	public boolean hasGroupAccessRightsChanged(List<IGroupAccessRight> oldValues, List<IGroupAccessRight> newValues){
		boolean changed = false;
		if(oldValues == null){
			oldValues = new ArrayList<IGroupAccessRight>();
		}
		if(newValues == null){
			newValues = new ArrayList<IGroupAccessRight>();
		}		
		for(IGroupAccessRight n : oldValues){
			if(newValues.contains(n) == false){
				changed = true;
				break;
			}
		}
		if(changed == false){
			for(IGroupAccessRight m : newValues){
				if(oldValues.contains(m) == false){
					changed = true;
					break;
				}
			}	
		}
		return changed;
	}



	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// public methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// protected methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// private methods
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	private List<String> convertListWithIntegerToListWithStrings(List<Integer> listWithIntegers){
		List<String> listWithStrings = new ArrayList<String>();
		if(listWithIntegers != null){
			for(int n : listWithIntegers){
				listWithStrings.add(String.valueOf(n));
			}	
		}
		return listWithStrings;
	}
	
	@SuppressWarnings("unchecked")
	private void compareStringValues(JSONObject jsonObject, String valueName, String oldValue, String newValue){
		if(this.compareStringValues(oldValue, newValue) == false){
			JSONObject valueJsonObject = new JSONObject();
			valueJsonObject.put(JSON_FIELD_OLD_VALUE, oldValue);
			valueJsonObject.put(JSON_FIELD_NEW_VALUE, newValue);
			jsonObject.put(valueName, valueJsonObject);
		}
	}
	
	private boolean compareStringValues(String oldValue, String newValue){
		boolean result = true;
		if(oldValue != null && newValue != null){
			if((oldValue == null || newValue == null) || (oldValue.equals(newValue) == false)){
				result = false;
			}
		}
		return result;
	}
	
	private void compareListsWithIntegerValues(JSONObject jsonObject, String valueName, List<Integer> oldValues, List<Integer> newValues){
		this.compareListsWithStringsValues(jsonObject, valueName, this.convertListWithIntegerToListWithStrings(oldValues), this.convertListWithIntegerToListWithStrings(newValues));
	}
	
	@SuppressWarnings("unchecked")
	private void compareListsWithStringsValues(JSONObject jsonObject, String valueName, List<String> oldValues, List<String> newValues){
		JSONArray removedValues = new JSONArray();
		JSONArray addedValues = new JSONArray();
		
		if(oldValues == null){
			oldValues = new ArrayList<String>();
		}
		if(newValues == null){
			newValues = new ArrayList<String>();
		}
		
		for(String n : oldValues){
			if(newValues.contains(n) == false){
				removedValues.add(n);
			}
		}
		
		for(String m : newValues){
			if(oldValues.contains(m) == false){
				addedValues.add(m);
			}
		}
		
		this.changesInListsToJson(valueName, jsonObject, removedValues, addedValues);
	}
	
	@SuppressWarnings("unchecked")
	private void compareListWithContents(JSONObject jsonObject, String valueName, List<IContent> oldValues, List<IContent> newValues){
		JSONArray removedValues = new JSONArray();
		JSONArray addedValues = new JSONArray();
		
		if(oldValues == null){
			oldValues = new ArrayList<IContent>();
		}
		if(newValues == null){
			newValues = new ArrayList<IContent>();
		}
		
		for(IContent o : oldValues){
			boolean removed = true;
			for(IContent n : newValues){
				if(this.compareContents(o, n)){
					removed = false;
					break;
				}
			}
			if(removed){
				removedValues.add(this.contentToJsonObject(o));
			}
		}
		
		for(IContent n : newValues){
			boolean added = true;
			for(IContent o : oldValues){
				if(this.compareContents(n, o)){
					added = false;
					break;
				}
			}
			if(added){
				addedValues.add(this.contentToJsonObject(n));
			}
		}
		
		this.changesInListsToJson(valueName, jsonObject, removedValues, addedValues);
	}
	
	private boolean compareContents(IContent oldContent, IContent newContent){
		boolean result = true;
		if(this.compareStringValues(oldContent.getLanguageCode(), newContent.getLanguageCode()) == false){
			result = false;
		}
		if(this.compareStringValues(oldContent.getConceptId(), newContent.getConceptId()) == false){
			result = false;
		}
		if(this.compareStringValues(oldContent.getContent(), newContent.getContent()) == false){
			result = false;
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private void compareListWithGroupAccessRights(JSONObject jsonObject, String valueName, List<IGroupAccessRight> oldValues, List<IGroupAccessRight> newValues){
		JSONArray removedValues = new JSONArray();
		JSONArray addedValues = new JSONArray();
		
		if(oldValues == null){
			oldValues = new ArrayList<IGroupAccessRight>();
		}
		if(newValues == null){
			newValues = new ArrayList<IGroupAccessRight>();
		}
		
		for(IGroupAccessRight n : oldValues){
			if(newValues.contains(n) == false){
				removedValues.add(this.groupAccessRigtToJsonObject(n));
			}
		}
		
		for(IGroupAccessRight m : newValues){
			if(oldValues.contains(m) == false){
				addedValues.add(this.groupAccessRigtToJsonObject(m));
			}
		}
		
		this.changesInListsToJson(valueName, jsonObject, removedValues, addedValues);
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject groupAccessRigtToJsonObject(IGroupAccessRight groupAccessRight){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonFieldNames.GROUP_ACCESS_RIGHT_GROUP_ID, groupAccessRight.getGroupId());
		jsonObject.put(JsonFieldNames.GROUP_ACCESS_RIGHT_READ_RIGHT, groupAccessRight.hasReadRight());
		jsonObject.put(JsonFieldNames.GROUP_ACCESS_RIGHT_UPDATE_RIGHT, groupAccessRight.hasUpdateRight());
		jsonObject.put(JsonFieldNames.GROUP_ACCESS_RIGHT_DELETE_RIGHT, groupAccessRight.hasDeleteRight());
		jsonObject.put(JsonFieldNames.GROUP_ACCESS_RIGHT_CHANGE_RIGHTS_RIGHT, groupAccessRight.hasChangeRightsRight());
		return jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject contentToJsonObject(IContent content){
		JSONObject jsonObject = new JSONObject();
		if(content.getLanguageCode() == null){
			jsonObject.put(JsonFieldNames.CONTENT_LANGUAGE_CODE, "");
		}else{
			jsonObject.put(JsonFieldNames.CONTENT_LANGUAGE_CODE, content.getLanguageCode());	
		}
		jsonObject.put(JsonFieldNames.CONTENT_VALUE, content.getContent());
		return jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	private void changesInListsToJson(String valueName, JSONObject jsonObject, JSONArray removedValues, JSONArray addedValues){
		if(removedValues.isEmpty() == false || addedValues.isEmpty() == false){
			JSONObject valueJsonObject = new JSONObject();
			if(removedValues.isEmpty() == false){
				valueJsonObject.put(JSON_FIELD_REMOVED_VALUES, removedValues);
			}
			if(addedValues.isEmpty() == false){
				valueJsonObject.put(JSON_FIELD_ADDED_VALUES, addedValues);	
			}
			jsonObject.put(valueName, valueJsonObject);	
		}
	}
	


	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
