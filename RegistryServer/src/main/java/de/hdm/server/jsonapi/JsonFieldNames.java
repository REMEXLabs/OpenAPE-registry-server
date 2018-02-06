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

/**
 * This class contains the field names for the JSON objects which are used by the JSON Rest api of the registry server.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class JsonFieldNames {
	
	/**
	 * JSON field name for one concept.
	 */
	public static final String RECORD = "record";
	
	/**
	 * JSON field name for an array of concepts.
	 */
	public static final String RECORDS = "records";
	
	/**
	 * JSON field name for the date time stamp, when the concept(s) was / were received from the database.
	 */
	public static final String RETRIEVED_AT = "retrievedAt";
	
	/**
	 * JSON field name for a message.
	 */
	public static final String MESSAGE = "message";
	
	/**
	 * JSON field name for the concept id.
	 */
	public static final String CONCEPT_ID = "conceptId";
	
	/**
	 * JSON field name for the concept type.
	 */
	public static final String TYPE = "type";
	
	/**
	 * JSON field name for the concept sub type.
	 */
	public static final String SUBTYPE = "subtype";
	
	/**
	 * JSON field name for the concept origin.
	 */
	public static final String ORIGIN = "origin";
	
	/**
	 * JSON field name for the language of a concept content.
	 */
	public static final String CONTENT_LANGUAGE_CODE = "languageCode";
	
	/**
	 * JSON field name for the value of a concept content.
	 */
	public static final String CONTENT_VALUE = "value";
	
	/**
	 * JSON field name for an array of concept definitions.
	 */
	public static final String DEFINITION = "definition";
	
	/**
	 * JSON field name for an array of concept names.
	 */
	public static final String TERM_LABEL = "termLabel";
	
	/**
	 * JSON field name for the concept data type.
	 */
	public static final String DATATYPE = "datatype";
	
	/**
	 * JSON field name for the concept value space.
	 */
	public static final String VALUE_SPACE = "valueSpace";
	
	/**
	 * JSON field name for an array of concept transformations.
	 */
	public static final String TRANSFORMATION_OF = "transformationOf";
	
	/**
	 * JSON field name for an array of concept refinements.
	 */
	public static final String REFINES = "refines";
	
	/**
	 * JSON field name for an array of concept notes.
	 */
	public static final String NOTES = "notes";
	
	/**
	 * JSON field name for an array of concept examples.
	 */
	public static final String EXAMPLES = "examples";
	
	/**
	 * JSON field name for an array with authors of the concept.
	 */
	public static final String AUTHORS = "authors";
	
	/**
	 * JSON field name for the last update date time stamp of the concept.
	 */
	public static final String UPDATED = "updated";
	
	/**
	 * JSON field name for an array with owners of the concept.
	 */
	public static final String OWNERS = "owners";
	
	/**
	 * JSON field name for the total number of concepts which are stored in the database and match the concept filter.
	 */
	public static final String TOTAL_NUMBER_OF_CONCEPTS = "total_rows";
	
	/**
	 * JSON field name for the concept filter.
	 */
	public static final String CONCEPT_FILTER = "params";
	
	/**
	 * JSON field name for the concept filter's parameter limit.
	 */
	public static final String CONCEPT_FILTER_LIMIT = "limit";
	
	/**
	 * JSON field name for the concept filter's parameter offset.
	 */
	public static final String CONCEPT_FILTER_OFFSET = "offset";
	
	/**
	 * JSON field name for the concept filter's parameter updated.
	 */
	public static final String CONCEPT_FILTER_UPDATED = "updated";
	
	/**
	 * JSON field name for the concept filter's parameter array types.
	 */
	public static final String CONCEPT_FILTER_TYPES = "types";
	
	/**
	 * JSON field name for an array of group access rights of a concept.
	 */
	public static final String GROUP_ACCESS_RIGHTS = "groupAccessRights";
	
	/**
	 * JSON field name for the group id of a concept's group access right.
	 */
	public static final String GROUP_ACCESS_RIGHT_GROUP_ID = "groupId";
	
	/**
	 * JSON field name for the read right of a concept's group access right.
	 */
	public static final String GROUP_ACCESS_RIGHT_READ_RIGHT = "readRight";
	
	/**
	 * JSON field name for the update right of a concept's group access right.
	 */
	public static final String GROUP_ACCESS_RIGHT_UPDATE_RIGHT = "updateRight";
	
	/**
	 * JSON field name for the delete right of a concept's group access right.
	 */
	public static final String GROUP_ACCESS_RIGHT_DELETE_RIGHT = "deleteRight";
	
	/**
	 * JSON field name for the change rights right of a concept's group access right.
	 */
	public static final String GROUP_ACCESS_RIGHT_CHANGE_RIGHTS_RIGHT = "changeRightsRight";
	
}
