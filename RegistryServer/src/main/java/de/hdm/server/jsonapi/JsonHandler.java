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
package de.hdm.server.jsonapi;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.hdm.databaseaccess.DaoFactory;
import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IConceptFilter;
import de.hdm.databaseaccess.ILanguageDao;
import de.hdm.datatypes.Concept;
import de.hdm.datatypes.DataTypeEnum;
import de.hdm.datatypes.Definition;
import de.hdm.datatypes.Example;
import de.hdm.datatypes.GroupAccessRight;
import de.hdm.datatypes.IConcept;
import de.hdm.datatypes.IContent;
import de.hdm.datatypes.IGroupAccessRight;
import de.hdm.datatypes.IValueSpace;
import de.hdm.datatypes.Name;
import de.hdm.datatypes.Note;
import de.hdm.datatypes.SubTypeEnum;
import de.hdm.datatypes.TypeEnum;
import de.hdm.datatypes.ValueSpace;
import de.hdm.helpers.Checker;
import de.hdm.helpers.GregorianCalendarHelper;
import de.hdm.helpers.TemplateFiller;
import de.hdm.multiplelanguages.LanguageHandler;

/**
 * This class converts concepts, requests and responses from and to JSON for the registry server.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class JsonHandler implements IJsonHandler{

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
		
	/**
	 * JSON value for concept type {@link TypeEnum#NEED_AND_PREFERENCE}.
	 */
	private static final String CONCEPT_TYPE_NEED_AND_PREFERENCE_AS_JSON_VALUE = "NeedAndPreference";
	
	/**
	 * JSON value for concept type {@link TypeEnum#CONTEXT_DESCRIPTION}.
	 */
	private static final String CONCEPT_TYPE_CONTEXT_DESCRIPTION_AS_JSON_VALUE = "ContextDescription";
	
	/**
	 * JSON value for concept type {@link TypeEnum#RESOURCE_DESCRIPTION}.
	 */
	private static final String CONCEPT_TYPE_RESOURCE_DESCRIPTION_AS_JSON_VALUE = "ResourceDescription";
	
	/**
	 * JSON value for concept sub type {@link SubTypeEnum#TERM}.
	 */
	private static final String CONCEPT_SUB_TYPE_TERM_AS_JSON_VALUE = "term";
	
	/**
	 * JSON value for concept sub type {@link SubTypeEnum#ALIAS}.
	 */
	private static final String CONCEPT_SUB_TYPE_ALIAS_AS_JSON_VALUE = "alias";
	
	/**
	 * JSON value for concept sub type {@link SubTypeEnum#TRANSFORM}.
	 */
	private static final String CONCEPT_SUB_TYPE_TRANSFORM_AS_JSON_VALUE = "transform";
	
	/**
	 * JSON value for concept sub type {@link SubTypeEnum#TRANSLATION}.
	 */
	private static final String CONCEPT_SUB_TYPE_TRANSLATION_AS_JSON_VALUE = "translation";
	
	/**
	 * JSON value for concept data type {@link DataTypeEnum#BOOLEAN}.
	 */
	private static final String CONCEPT_DATA_TYPE_BOOLEAN_AS_JSON_VALUE = "Boolean";
	
	/**
	 * JSON value for concept data type {@link DataTypeEnum#NUMBER}.
	 */
	private static final String CONCEPT_DATA_TYPE_NUMBER_AS_JSON_VALUE = "Number";
	
	/**
	 * JSON value for concept data type {@link DataTypeEnum#STRING}.
	 */
	private static final String CONCEPT_DATA_TYPE_STRING_AS_JSON_VALUE = "String";
	
	/**
	 * Integer constant for content type name.
	 */
	private static final int CONTENT_TYPE_NAME = 1;
	
	/**
	 * Integer constant for content type definition.
	 */
	private static final int CONTENT_TYPE_DEFINITION = 2;
	
	/**
	 * Integer constant for content type example.
	 */
	private static final int CONTENT_TYPE_EXAMPLE = 3;
	
	/**
	 * Integer constant for content type note.
	 */
	private static final int CONTENT_TYPE_NOTE = 4;
	
	/**
	 * The users preferred language for messages / error messages.
	 */
	private Locale locale;
	
	/**
     * Data access object for the languages.
     */
    private ILanguageDao languageDao;

	


	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Create a new {@link JsonHandler}.
	 * @param locale the users preferred language for messages / error messages. It can be null. If it is null, the default language will be used.
	 */
	public JsonHandler(Locale locale) {
		this.setLocale(locale);
		this.languageDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createLanguageDao();
	}

	
	

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// getters and setters
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	private void setLocale(Locale locale){
		this.locale = locale;
	}
	
	


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
	 * @see de.hdm.json.IJsonHandler#jsonToConcepts(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IConcept> jsonToConcepts(String json) throws JsonHandlerException, DataAccessException {
		Checker.checkNullAndEmptiness(json, "json");
		JSONParser jsonParser = new JSONParser();
		JSONArray conceptsAsJSONArray;
		List<IConcept> concepts = new ArrayList<IConcept>();
		try {
			conceptsAsJSONArray = (JSONArray)jsonParser.parse(json);
			Iterator<JSONObject> iterator = conceptsAsJSONArray.iterator();
            while (iterator.hasNext()) {
                concepts.add(this.jsonObjectToConcept(iterator.next(), true));
            }
		} catch (ParseException e) {
			e.printStackTrace();
			throw new JsonHandlerException("The JSON data could not be converted into a list of concepts!", e);
		}
		return concepts;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.json.IJsonHandler#jsonToConcept(java.lang.String, boolean)
	 */
	@Override
	public IConcept jsonToConcept(String json, boolean conceptIdFieldRequired) throws JsonHandlerException, DataAccessException {
		Checker.checkNullAndEmptiness(json, "json");
		JSONParser jsonParser = new JSONParser();
		IConcept concept = null;
		try {
			concept = this.jsonObjectToConcept((JSONObject)jsonParser.parse(json), conceptIdFieldRequired);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new JsonHandlerException("The JSON data could not be converted into a concept!", e);
		}
		return concept;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.json.IJsonHandler#conceptToJson(de.hdm.datatypes.IConcept)
	 */
	@Override
	public String conceptToJson(IConcept concept) {
		Checker.checkNull(concept, "concept");
		return this.conceptToJsonObject(concept).toJSONString();
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.json.IJsonHandler#conceptsToJson(java.util.List)
	 */
	@Override
	public String conceptsToJson(List<IConcept> concepts) {
		Checker.checkNull(concepts, "concepts");
		return this.fillJsonArrayWithConcepts(concepts).toJSONString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.json.IJsonHandler#createPostAndPutConceptSuccessJsonResponse(de.hdm.datatypes.IConcept, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String createPostAndPutConceptSuccessJsonResponse(IConcept concept, String message){
		Checker.checkNull(concept, "concept");
		Checker.checkNullAndEmptiness(message, "message");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonFieldNames.MESSAGE, message);
		jsonObject.put(JsonFieldNames.RECORD, this.conceptToJsonObject(concept));
		return jsonObject.toJSONString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.json.IJsonHandler#createGetConceptsSuccessJsonResponse(java.util.List, de.hdm.databaseaccess.IConceptFilter, long, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String createGetConceptsSuccessJsonResponse(List<IConcept> concepts, IConceptFilter conceptFilter, long retrievedAt, int totalNumberOfConcepts) {
		Checker.checkNull(concepts, "concepts");
		Checker.checkNull(conceptFilter, "conceptFilter");
		Checker.checkLongGreaterEquals(retrievedAt, "retrievedAt", 0);
		Checker.checkIntegerGreaterEquals(totalNumberOfConcepts, "totalNumberOfConcepts", 0);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonFieldNames.TOTAL_NUMBER_OF_CONCEPTS, totalNumberOfConcepts);
		jsonObject.put(JsonFieldNames.CONCEPT_FILTER, this.conceptFilterToJson(conceptFilter));
		jsonObject.put(JsonFieldNames.RECORDS, this.fillJsonArrayWithConcepts(concepts));
		jsonObject.put(JsonFieldNames.RETRIEVED_AT, this.timeStampToString(retrievedAt));
		return jsonObject.toJSONString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.json.IJsonHandler#createGetConceptSuccessJsonResponse(de.hdm.datatypes.IConcept, long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String createGetConceptSuccessJsonResponse(IConcept concept, long retrievedAt) {
		Checker.checkNull(concept, "concept");
		Checker.checkLongGreaterEquals(retrievedAt, "retrievedAt", 0);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonFieldNames.RECORD, this.conceptToJsonObject(concept));
		jsonObject.put(JsonFieldNames.RETRIEVED_AT, this.timeStampToString(retrievedAt));
		return jsonObject.toJSONString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.json.IJsonHandler#createDeleteConceptSuccessJsonResponse(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String createDeleteConceptSuccessJsonResponse(String message){
		Checker.checkNullAndEmptiness(message, "message");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonFieldNames.MESSAGE, message);
		return jsonObject.toJSONString();
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
	
	@SuppressWarnings("unchecked")
	private IConcept jsonObjectToConcept(JSONObject conceptAsJson, boolean conceptIdFieldRequired) throws JsonHandlerException, DataAccessException{
		String conceptId = this.getString(conceptAsJson, JsonFieldNames.CONCEPT_ID, conceptIdFieldRequired, true);
		IConcept concept = new Concept(conceptId);
		concept.setType(this.convertJsonValueToConceptType(this.getString(conceptAsJson, JsonFieldNames.TYPE, true, true)));
		concept.setSubType(this.convertJsonValueToConceptSubType(this.getString(conceptAsJson, JsonFieldNames.SUBTYPE, true, true)));
		concept.setOrigin(this.getString(conceptAsJson, JsonFieldNames.ORIGIN, false, false));
		concept.setDefinitions(this.convertJsonArrayToListWithContents(this.getJsonArray(conceptAsJson, JsonFieldNames.DEFINITION, true, true), conceptId, CONTENT_TYPE_DEFINITION));
		concept.setNames(this.convertJsonArrayToListWithContents(this.getJsonArray(conceptAsJson, JsonFieldNames.TERM_LABEL, true, true), conceptId, CONTENT_TYPE_NAME));	
		concept.setDataType(this.convertJsonValueToConceptDataType(this.getString(conceptAsJson, JsonFieldNames.DATATYPE, true, true)));
		//concept.setValueSpace(new ValueSpace(this.getString(conceptAsJson, JsonFieldNames.VALUE_SPACE, false, false)));
		concept.setValueSpace(this.convertJsonValueToValueSpace(conceptAsJson));
		concept.setConceptsWhichAreTransformedByThisConcept(this.convertJsonArrayToListWithStrings(this.getJsonArray(conceptAsJson, JsonFieldNames.TRANSFORMATION_OF, false, false)));
		concept.setConceptsWhichAreRefinedByThisConcept(this.convertJsonArrayToListWithStrings(this.getJsonArray(conceptAsJson, JsonFieldNames.REFINES, false, false)));
		concept.setNotes(this.convertJsonArrayToListWithContents(this.getJsonArray(conceptAsJson, JsonFieldNames.NOTES, false, false), conceptId, CONTENT_TYPE_EXAMPLE));
		concept.setExamples(this.convertJsonArrayToListWithContents(this.getJsonArray(conceptAsJson, JsonFieldNames.EXAMPLES, false, false), conceptId, CONTENT_TYPE_NOTE));
		concept.setAuthors(this.convertJsonArrayToListWithStrings(this.getJsonArray(conceptAsJson, JsonFieldNames.AUTHORS, false, false)));
		concept.setOwners(this.convertJsonArrayToListWithIntegers(this.getJsonArray(conceptAsJson, JsonFieldNames.OWNERS, false, false), JsonFieldNames.OWNERS));
		concept.setGroupAccessRights(this.convertJsonArrayToListWithGroupAccessRights(conceptId, this.getJsonArray(conceptAsJson, JsonFieldNames.GROUP_ACCESS_RIGHTS, false, false)));
		return concept;
	}
	
	private JSONObject getJsonObject(JSONObject jsonObject, String jsonKey, boolean isRequired, boolean nullNotAllowed) throws JsonHandlerException{
		Object o = this.getObjectFromJSON(jsonObject, jsonKey, isRequired, nullNotAllowed);
		if(o != null && o instanceof JSONObject == false){
			String message = "The value " + o.toString() + " in the json field " + jsonKey + " is invalid! It must be a JSONObject";
			String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(this.locale, "ERROR_BAD_REQUEST_INVALID_JSON_DATA_WITH_VALID_VALUES"), o.toString(), jsonKey, "JSON-Object");
			throw new JsonHandlerException(message, null, userMessage);
		}
		return (JSONObject)o;
	}
	
	private JSONArray getJsonArray(JSONObject jsonObject, String jsonKey, boolean isRequired, boolean nullNotAllowed) throws JsonHandlerException{
		Object o = this.getObjectFromJSON(jsonObject, jsonKey, isRequired, nullNotAllowed);
		if(o != null && o instanceof JSONArray == false){
			String message = "The value " + o.toString() + " in the json field " + jsonKey + " is invalid! It must be a JSONArray";
			String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(this.locale, "ERROR_BAD_REQUEST_INVALID_JSON_DATA_WITH_VALID_VALUES"), o.toString(), jsonKey, "JSON-Array");
			throw new JsonHandlerException(message, null, userMessage);
		}
		return (JSONArray)o;
	}
	
	private String getString(JSONObject jsonObject, String jsonKey, boolean isRequired, boolean nullNotAllowed) throws JsonHandlerException{
		Object o = this.getObjectFromJSON(jsonObject, jsonKey, isRequired, nullNotAllowed);
		if(o != null && o instanceof String == false){
			String message = "The value " + o.toString() + " in the json field " + jsonKey + " is invalid! It must be a string";
			String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(this.locale, "ERROR_BAD_REQUEST_INVALID_JSON_DATA_WITH_VALID_VALUES"), o.toString(), jsonKey, "String");
			throw new JsonHandlerException(message, null, userMessage);
		}
		return (String)o;
	}
	
	private boolean getBoolean(JSONObject jsonObject, String jsonKey) throws JsonHandlerException{
		Object o = this.getObjectFromJSON(jsonObject, jsonKey, true, true);
		if(o == null || o instanceof Boolean == false){
			String message = "The value " + o.toString() + " in the json field " + jsonKey + " is invalid! It must be true or false!";
			String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(this.locale, "ERROR_BAD_REQUEST_INVALID_JSON_DATA_WITH_VALID_VALUES"), o.toString(), jsonKey, "true or false");
			throw new JsonHandlerException(message, null, userMessage);
		}
		return (Boolean)o;
	}
	
	private int getInteger(JSONObject jsonObject, String jsonKey) throws JsonHandlerException{
		long groupIdAsLong = (long)this.getObjectFromJSON(jsonObject, jsonKey, true, true);
		if (groupIdAsLong < Integer.MIN_VALUE || groupIdAsLong > Integer.MAX_VALUE) { 
	    	String message = "The value " + groupIdAsLong + " in the json field " + jsonKey + " is invalid! It must be an integer!";
			String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(this.locale, "ERROR_BAD_REQUEST_INVALID_JSON_DATA_WITH_VALID_VALUES"), String.valueOf(groupIdAsLong), jsonKey, "One integer");
			throw new JsonHandlerException(message, null, userMessage);
	    }
		return Math.toIntExact(groupIdAsLong);
	}
	
	private Object getObjectFromJSON(JSONObject jsonObject, String jsonKey, boolean isRequired, boolean nullNotAllowed) throws JsonHandlerException{
		Object returnObject = jsonObject.get(jsonKey);
		boolean fieldExists = jsonObject.containsKey(jsonKey);
		if(isRequired && fieldExists == false){
				String message = "Missing json field " + jsonKey + "!";
				String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(this.locale, "ERROR_BAD_REQUEST_MISSING_JSON_DATA"), jsonKey);
				throw new JsonHandlerException(message, null, userMessage);
		}
		if(fieldExists && returnObject == null && nullNotAllowed){
			if(returnObject == null && nullNotAllowed){
				String message = "The value in the json field " + jsonKey + " must not be null!";
				String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(this.locale, "ERROR_BAD_REQUEST_INVALID_JSON_DATA_NOT_NULL"), jsonKey);
				throw new JsonHandlerException(message, null, userMessage);
			}
		}
		return returnObject; 
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JSONObject conceptToJsonObject(IConcept concept){
		JSONObject conceptAsJson = new JSONObject();
		conceptAsJson.put(JsonFieldNames.CONCEPT_ID, concept.getId());
		conceptAsJson.put(JsonFieldNames.TYPE, this.convertConceptTypeToJsonValue(concept.getType()));
		conceptAsJson.put(JsonFieldNames.SUBTYPE, this.convertConceptSubTypeToJsonValue(concept.getSubType()));
		conceptAsJson.put(JsonFieldNames.ORIGIN, concept.getOrigin());
		conceptAsJson.put(JsonFieldNames.DEFINITION, this.convertConceptContentToJSONArray((List)concept.getDefinitions()));
		conceptAsJson.put(JsonFieldNames.TERM_LABEL, this.convertConceptContentToJSONArray((List)concept.getNames()));
		conceptAsJson.put(JsonFieldNames.DATATYPE, this.convertConceptDataTypeToJsonValue(concept.getDataType()));
		if(concept.getValueSpace() != null && concept.getValueSpace().getJsonSchema() != null){
		    conceptAsJson.put(JsonFieldNames.VALUE_SPACE, concept.getValueSpace().getJsonSchema());    
		}
		conceptAsJson.put(JsonFieldNames.TRANSFORMATION_OF, this.fillJsonArrayWithStrings(concept.getConceptsWhichAreTransformedByThisConcept()));
		conceptAsJson.put(JsonFieldNames.REFINES, this.fillJsonArrayWithStrings(concept.getConceptsWhichAreRefinedByThisConcept()));
		conceptAsJson.put(JsonFieldNames.NOTES, this.convertConceptContentToJSONArray((List)concept.getNotes()));
		conceptAsJson.put(JsonFieldNames.EXAMPLES, this.convertConceptContentToJSONArray((List)concept.getExamples()));
		conceptAsJson.put(JsonFieldNames.AUTHORS, this.fillJsonArrayWithStrings(concept.getAuthors()));
		if(concept.getUpdatedInMilliseconds() != -1){
			conceptAsJson.put(JsonFieldNames.UPDATED, this.timeStampToString(concept.getUpdatedInMilliseconds()));	
		}
		conceptAsJson.put(JsonFieldNames.OWNERS, this.fillJsonArrayWithIntegers(concept.getOwners()));
		if(concept.getGroupAccessRights() != null && concept.getGroupAccessRights().isEmpty() == false){
			List<JSONObject> groupAccessRightsAsJsonObjects = this.groupAccessRightsToJsonArray(concept.getGroupAccessRights());
			conceptAsJson.put(JsonFieldNames.GROUP_ACCESS_RIGHTS, this.fillJsonArrayWithJsonObjects(groupAccessRightsAsJsonObjects));
		}
		return conceptAsJson;
	}
	
	private IValueSpace convertJsonValueToValueSpace(JSONObject jsonObject) throws JsonHandlerException{
	    JSONObject valueSpaceAsJsonObject = this.getJsonObject(jsonObject, JsonFieldNames.VALUE_SPACE, false, false);
	    IValueSpace valueSpace = null;
	    if(valueSpaceAsJsonObject != null){
	        if(ValueSpace.isJsonSchemaValid(valueSpaceAsJsonObject.toJSONString())){
	            valueSpace = new ValueSpace(valueSpaceAsJsonObject.toJSONString());
	        }else{
	            String message = "The value space is not a valid JSON schema!";
	            String userMessage = LanguageHandler.getWord(this.locale, "ERROR_BAD_REQUEST_INVALID_JSON_DATA_VALUE_SPACE_INVALID");
	            throw new JsonHandlerException(message, null, userMessage);
	        }
	    }
	    return valueSpace;
	}
	
	private TypeEnum convertJsonValueToConceptType(String typeAsString) throws JsonHandlerException{
		TypeEnum type;
		switch(typeAsString){
		case CONCEPT_TYPE_NEED_AND_PREFERENCE_AS_JSON_VALUE:
			type = TypeEnum.NEED_AND_PREFERENCE;
			break;
		case CONCEPT_TYPE_CONTEXT_DESCRIPTION_AS_JSON_VALUE:
			type = TypeEnum.CONTEXT_DESCRIPTION;
			break;
		case CONCEPT_TYPE_RESOURCE_DESCRIPTION_AS_JSON_VALUE:
			type = TypeEnum.RESOURCE_DESCRIPTION;
			break;
		default:
			String message = "The concept type " + typeAsString + " is not valid!";
			String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(this.locale, "ERROR_BAD_REQUEST_INVALID_JSON_DATA_WITH_VALID_VALUES"), typeAsString, JsonFieldNames.TYPE, "\"NeedAndPreference\", \"ContextDescription\", \"ResourceDescription\"");
			throw new JsonHandlerException(message, null, userMessage);
		}
		return type;
	}
	
	private String convertConceptTypeToJsonValue(TypeEnum type){
		String typeAsString;
		switch(type){
		case NEED_AND_PREFERENCE:
			typeAsString = CONCEPT_TYPE_NEED_AND_PREFERENCE_AS_JSON_VALUE;
			break;
		case CONTEXT_DESCRIPTION:
			typeAsString = CONCEPT_TYPE_CONTEXT_DESCRIPTION_AS_JSON_VALUE;
			break;
		case RESOURCE_DESCRIPTION:
			typeAsString = CONCEPT_TYPE_RESOURCE_DESCRIPTION_AS_JSON_VALUE;
			break;
		default:
			throw new RuntimeException("The type " + type.toString() + " is unknown for this method!");
		}
		return typeAsString;
	}
	
	private SubTypeEnum convertJsonValueToConceptSubType(String subTypeAsString) throws JsonHandlerException{
		SubTypeEnum subType;
		switch(subTypeAsString){
		case CONCEPT_SUB_TYPE_TERM_AS_JSON_VALUE:
			subType = SubTypeEnum.TERM;
			break;
		case CONCEPT_SUB_TYPE_ALIAS_AS_JSON_VALUE:
			subType = SubTypeEnum.ALIAS;
			break;
		case CONCEPT_SUB_TYPE_TRANSFORM_AS_JSON_VALUE:
			subType = SubTypeEnum.TRANSFORM;
			break;
		case CONCEPT_SUB_TYPE_TRANSLATION_AS_JSON_VALUE:
			subType = SubTypeEnum.TRANSLATION;
			break;
		default:
			String message = "The concept subtype " + subTypeAsString + " is not valid!";
			String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(this.locale, "ERROR_BAD_REQUEST_INVALID_JSON_DATA_WITH_VALID_VALUES"), subTypeAsString, JsonFieldNames.SUBTYPE, "\"term\", \"alias\", \"transform\" and \"translation\"");
			throw new JsonHandlerException(message, null, userMessage);
		}
		return subType;
	}
	
	private String convertConceptSubTypeToJsonValue(SubTypeEnum subType){
		String subTypeAsString;
		switch(subType){
		case TERM:
			subTypeAsString = CONCEPT_SUB_TYPE_TERM_AS_JSON_VALUE;
			break;
		case ALIAS:
			subTypeAsString = CONCEPT_SUB_TYPE_ALIAS_AS_JSON_VALUE;
			break;
		case TRANSFORM:
			subTypeAsString = CONCEPT_SUB_TYPE_TRANSFORM_AS_JSON_VALUE;
			break;
		case TRANSLATION:
			subTypeAsString = CONCEPT_SUB_TYPE_TRANSLATION_AS_JSON_VALUE;
			break;
		default:
			throw new RuntimeException("The subtype " + subType.toString() + " is unknown for this method!");
		}
		return subTypeAsString;
	}
	
	private DataTypeEnum convertJsonValueToConceptDataType(String dataTypeAsString) throws JsonHandlerException{
		DataTypeEnum dataType;
		switch(dataTypeAsString){
		case CONCEPT_DATA_TYPE_BOOLEAN_AS_JSON_VALUE:
			dataType = DataTypeEnum.BOOLEAN;
			break;
		case CONCEPT_DATA_TYPE_NUMBER_AS_JSON_VALUE:
			dataType = DataTypeEnum.NUMBER;
			break;
		case CONCEPT_DATA_TYPE_STRING_AS_JSON_VALUE:
			dataType = DataTypeEnum.STRING;
			break;
		default:
			String message = "The concept datatype " + dataTypeAsString + " is not valid!";
			String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(this.locale, "ERROR_BAD_REQUEST_INVALID_JSON_DATA_WITH_VALID_VALUES"), dataTypeAsString, JsonFieldNames.DATATYPE, "\"Boolean\", \"Number\", \"String\"");
			throw new JsonHandlerException(message, null, userMessage);
		}
		return dataType;
	}
	
	private String convertConceptDataTypeToJsonValue(DataTypeEnum dataType){
		String dataTypeAsString;
		switch(dataType){
		case BOOLEAN:
			dataTypeAsString = CONCEPT_DATA_TYPE_BOOLEAN_AS_JSON_VALUE;
			break;
		case NUMBER:
			dataTypeAsString = CONCEPT_DATA_TYPE_NUMBER_AS_JSON_VALUE;
			break;
		case STRING:
			dataTypeAsString = CONCEPT_DATA_TYPE_STRING_AS_JSON_VALUE;
			break;
		default:
			throw new RuntimeException("The datatype " + dataType.toString() + " is unknown for this method!");
		}
		return dataTypeAsString;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List convertJsonArrayToListWithContents(JSONArray jsonArray, String conceptId, int contentType) throws JsonHandlerException, DataAccessException{
		List list = new ArrayList();
		Iterator<JSONObject> iterator = jsonArray.iterator();
		while(iterator.hasNext()){
			JSONObject jsonObject = iterator.next();
			String languageCode = this.getString(jsonObject, JsonFieldNames.CONTENT_LANGUAGE_CODE, false, false);
			if(languageCode != null && languageCode.isEmpty()){
				languageCode = null;
			}
			if(languageCode != null){
			    if(!this.languageDao.isLanguageCodeValid(null, languageCode)){
			        String message = "The language code " + languageCode + " is invalid!";
			        String userMessage = LanguageHandler.getWord(this.locale, "ERROR_BAD_REQUEST_INVALID_JSON_DATA_LANGUAGE_CODE_INVALID");
			        userMessage = TemplateFiller.fillTemplate(userMessage, languageCode);
			        throw new JsonHandlerException(message, null, userMessage);
			    }
			}
			String value = this.getString(jsonObject, JsonFieldNames.CONTENT_VALUE, true, true);
			switch(contentType){
			case CONTENT_TYPE_NAME:
				list.add(new Name(languageCode, conceptId, value));
				break;
			case CONTENT_TYPE_DEFINITION:
				list.add(new Definition(languageCode, conceptId, value));
				break;
			case CONTENT_TYPE_EXAMPLE:
				list.add(new Example(languageCode, conceptId, value));
				break;
			case CONTENT_TYPE_NOTE:
				list.add(new Note(languageCode, conceptId, value));
				break;
			default:
				throw new RuntimeException("The content type " + contentType + " is unknown for this method!");
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray convertConceptContentToJSONArray(List<IContent> contents){
		JSONArray jsonArray = new JSONArray();
		for(IContent n : contents){
			JSONObject jsonObject = new JSONObject();
			if(n.getLanguageCode() == null){
				jsonObject.put(JsonFieldNames.CONTENT_LANGUAGE_CODE, "");
			}else{
				jsonObject.put(JsonFieldNames.CONTENT_LANGUAGE_CODE, n.getLanguageCode());	
			}
			jsonObject.put(JsonFieldNames.CONTENT_VALUE, n.getContent());
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
		
	@SuppressWarnings("unchecked")
	private List<String> convertJsonArrayToListWithStrings(JSONArray jsonArray){
		List<String> list = new ArrayList<String>();
		Iterator<String> iterator = jsonArray.iterator();
		while(iterator.hasNext()){
			list.add(iterator.next());
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private List<Integer> convertJsonArrayToListWithIntegers(JSONArray jsonArray, String jsonKey) throws JsonHandlerException{
		List<Integer> list = new ArrayList<Integer>();
		Iterator<Long> iterator = jsonArray.iterator();
		while(iterator.hasNext()){
			long tmp = iterator.next();
			if (tmp >= Integer.MIN_VALUE && tmp <= Integer.MAX_VALUE) {
				list.add(Math.toIntExact(tmp));    
		    }else{
		    	String message = "The value " + tmp + " it the json array is invalid! It must be an integer!";
				String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(this.locale, "ERROR_BAD_REQUEST_INVALID_JSON_DATA_IN_JSON_ARRAY_WITH_VALID_VALUES"), String.valueOf(tmp), jsonKey, "Integer");
				throw new JsonHandlerException(message, null, userMessage);
		    }
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray fillJsonArrayWithConcepts(List<IConcept> concepts){
		JSONArray conceptsAsJsonArray = new JSONArray();
		for(IConcept concept : concepts){
			conceptsAsJsonArray.add(this.conceptToJsonObject(concept));
		}
		return conceptsAsJsonArray;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray fillJsonArrayWithStrings(List<String> list){
		JSONArray jsonArray = new JSONArray();
		if(list != null){
			for(String n : list){
				jsonArray.add(n);
			}	
		}
		return jsonArray;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray fillJsonArrayWithIntegers(List<Integer> list){
		JSONArray jsonArray = new JSONArray();
		if(list != null){
			for(int n : list){
				jsonArray.add(n);
			}	
		}
		return jsonArray;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray fillJsonArrayWithConceptTypes(List<TypeEnum> conceptTypes){
		JSONArray jsonArray = new JSONArray();
		for(TypeEnum n : conceptTypes){
			jsonArray.add(this.convertConceptTypeToJsonValue(n));
		}
		return jsonArray;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray fillJsonArrayWithJsonObjects(List<JSONObject> jsonObjects){
		JSONArray jsonArray = new JSONArray();
		for(JSONObject n : jsonObjects){
			jsonArray.add(n);
		}
		return jsonArray;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject conceptFilterToJson(IConceptFilter conceptFilter){
		JSONObject jsonObject = new JSONObject();
		if(conceptFilter.getOffset() != -1){
			jsonObject.put(JsonFieldNames.CONCEPT_FILTER_OFFSET, conceptFilter.getOffset());	
		}else{
			jsonObject.put(JsonFieldNames.CONCEPT_FILTER_OFFSET, "");
		}
		if(conceptFilter.getLimit() != -1){
			jsonObject.put(JsonFieldNames.CONCEPT_FILTER_LIMIT, conceptFilter.getLimit());
		}else{
			jsonObject.put(JsonFieldNames.CONCEPT_FILTER_LIMIT, "");
		}
		if(conceptFilter.getUpdated() != null){
			jsonObject.put(JsonFieldNames.CONCEPT_FILTER_UPDATED, GregorianCalendarHelper.convertDateAndTimeToString(conceptFilter.getUpdated(), "-", "T", ":"));
		}else{
			jsonObject.put(JsonFieldNames.CONCEPT_FILTER_UPDATED, "");
		}
		if(conceptFilter.getTypes() != null){
			jsonObject.put(JsonFieldNames.CONCEPT_FILTER_TYPES, this.fillJsonArrayWithConceptTypes(conceptFilter.getTypes()));
		}else{
			jsonObject.put(JsonFieldNames.CONCEPT_FILTER_TYPES, this.fillJsonArrayWithConceptTypes(new ArrayList<TypeEnum>()));
		}
		return jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	private List<JSONObject> groupAccessRightsToJsonArray(List<IGroupAccessRight> groupAccessRights) {
		JSONArray jsonArray = new JSONArray();
		for(IGroupAccessRight n : groupAccessRights){
			jsonArray.add(this.groupAccessRigtToJsonObject(n));
		}
		return jsonArray;
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
	
	private List<IGroupAccessRight> convertJsonArrayToListWithGroupAccessRights(String conceptId, JSONArray jsonArray) throws JsonHandlerException{
		List<IGroupAccessRight> groupAccessRights = new ArrayList<IGroupAccessRight>();
		if(jsonArray != null){
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> iterator = jsonArray.iterator();
			while(iterator.hasNext()){
				groupAccessRights.add(this.jsonObjectToGroupAccessRight(conceptId, iterator.next()));
			}
		}
		return groupAccessRights;
	}
	
	private IGroupAccessRight jsonObjectToGroupAccessRight(String conceptId, JSONObject jsonObject) throws JsonHandlerException{
		int groupId = this.getInteger(jsonObject, JsonFieldNames.GROUP_ACCESS_RIGHT_GROUP_ID);
		boolean readRight = this.getBoolean(jsonObject, JsonFieldNames.GROUP_ACCESS_RIGHT_READ_RIGHT);
		boolean updateRight = this.getBoolean(jsonObject, JsonFieldNames.GROUP_ACCESS_RIGHT_UPDATE_RIGHT);
		boolean deleteRight = this.getBoolean(jsonObject, JsonFieldNames.GROUP_ACCESS_RIGHT_DELETE_RIGHT);
		boolean changeRightsRight = this.getBoolean(jsonObject, JsonFieldNames.GROUP_ACCESS_RIGHT_CHANGE_RIGHTS_RIGHT);
		return new GroupAccessRight(groupId, conceptId, readRight, updateRight, deleteRight, changeRightsRight);
	}
	
	private String timeStampToString(long timeStamp){
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTimeInMillis(timeStamp);
		gregorianCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		String result = GregorianCalendarHelper.convertDateAndTimeToString(gregorianCalendar, "-", "T", ":", true, ".") + "Z";
		return result;
	}
	
	
	
	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
