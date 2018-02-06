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

import java.util.List;

import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IConceptFilter;
import de.hdm.datatypes.IConcept;

/**
 * This interface can be used to convert concepts from type {@link IConcept} to and from the JSON format and create
 * responses in the JSON format.
 * 
 * @author Tobias Ableitner
 *
 */
public interface IJsonHandler {

	/**
	 * Converts a JSON array of concepts into a list of concepts from type {@link IConcept}.
	 * 
	 * @param json
	 *            the JSON array with concepts as string. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @return list with the converted concepts. If the JSON array was empty, this list will it be, too.
	 * @throws JsonHandlerException
	 *             if the JSON array in the string json is not well formed or invalid.
	 * @throws DataAccessException if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter json is null or empty.
	 */
	public List<IConcept> jsonToConcepts(String json) throws JsonHandlerException, DataAccessException;

	/**
	 * Converts a concept as JSON object into a concept from type {@link IConcept}.
	 * 
	 * @param json
	 *            the JSON object including the concept as string. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @param conceptIdFieldRequired
	 *            true if the concept in the JSON object has to contain a concept id {@link JsonFieldNames#CONCEPT_ID}
	 *            and false if not
	 * @return converted concept
	 * @throws JsonHandlerException
	 *             if the JSON object in the string json is not well formed or invalid.
	 * @throws DataAccessException if a problem with the database occurs.
	 * @throws IllegalArgumentException
	 *             if the parameter json is null or empty.
	 */
	public IConcept jsonToConcept(String json, boolean conceptIdFieldRequired) throws JsonHandlerException, DataAccessException;

	/**
	 * Converts a concept into a string containing it as JSON object.
	 * 
	 * @param concept
	 *            the concept which should be converted. It must not be null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @return string containing the concept as JSON object
	 * @throws IllegalArgumentException
	 *             if the parameter concept is null.
	 */
	public String conceptToJson(IConcept concept);

	/**
	 * Converts a list of concepts into a string containing it as JSON array.
	 * 
	 * @param concepts
	 *            the list with the concepts which should be converted. The list can by empty but not null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @return string containing the concepts as JSON array
	 * @throws IllegalArgumentException
	 *             if the parameter concepts is null.
	 */
	public String conceptsToJson(List<IConcept> concepts);

	/**
	 * Creates a string containing a JSON object for the response body of a successful post or put concept request.
	 * 
	 * @param concept
	 *            the concept which was created or updated. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @param message
	 *            the message. It must not be null or empty. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @return JSON object as String
	 * @throws IllegalArgumentException
	 *             if one or more parameters are invalid.
	 */
	public String createPostAndPutConceptSuccessJsonResponse(IConcept concept, String message);

	/**
	 * Creates a string containing a JSON object for the response body of a successful get concept request.
	 * 
	 * @param concept
	 *            the concept. It must not be null. Otherwise an {@link IllegalArgumentException} will be thrown.
	 * @param retrievedAt
	 *            date and time stamp as long, when the concepts were retrieved from the database. It must be greater
	 *            equals 0. Otherwise an {@link IllegalArgumentException} will be thrown.
	 * @return JSON object as string
	 * @throws IllegalArgumentException
	 *             if one or more parameters are invalid.
	 */
	public String createGetConceptSuccessJsonResponse(IConcept concept, long retrievedAt);

	/**
	 * Creates a string containing a JSON object for the response body of a successful get concepts request.
	 * 
	 * @param concepts
	 *            the concepts. The list can be empty but not null. Otherwise an {@link IllegalArgumentException} will
	 *            be thrown.
	 * @param conceptFilter
	 *            the concept filter which was used to get the concepts. It must not be null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @param retrievedAt
	 *            date and time stamp as long, when the concepts were retrieved from the database. It must be greater
	 *            equals 0. Otherwise an {@link IllegalArgumentException} will be thrown.
	 * @param totalNumberOfConcepts
	 *            total number of concepts in the database which fits into the concept filter. It must be greater equals
	 *            0. Otherwise an {@link IllegalArgumentException} will be thrown.
	 * @return JSON object as string
	 * @throws IllegalArgumentException
	 *             if one or more parameters are invalid.
	 */
	public String createGetConceptsSuccessJsonResponse(List<IConcept> concepts, IConceptFilter conceptFilter,
			long retrievedAt, int totalNumberOfConcepts);

	/**
	 * Creates a string containing a JSON object for the response body of a successful delete concept request.
	 * 
	 * @param message
	 *            the message. It must not be null or empty. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @return JSON object as string
	 * @throws IllegalArgumentException
	 *             if the parameter message is null.
	 */
	public String createDeleteConceptSuccessJsonResponse(String message);

}
