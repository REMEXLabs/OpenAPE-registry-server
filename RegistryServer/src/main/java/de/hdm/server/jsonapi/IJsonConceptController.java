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

import de.hdm.RegistryServerMain;
import spark.Request;
import spark.Response;

/**
 * This interface is used by the main class {@link RegistryServerMain} of the registry server. Therefore it routes the
 * JSON requests to this interface.
 * 
 * @author Tobias Ableitner
 *
 */
public interface IJsonConceptController {

	/**
	 * Handles a create new concept request via the JSON api. Error handling is done in this method, if needed. In case
	 * of an error, the returned string will contain an error message which can be shown to the user.
	 * 
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return content for the response body
	 */
	public String postConceptAsJson(Request request, Response response);

	/**
	 * Handles an update concept request via the JSON api. Error handling is done in this method, if needed. In case of
	 * an error, the returned string will contain an error message which can be shown to the user.
	 * 
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return content for the response body
	 */
	public String putConceptAsJson(Request request, Response response);

	/**
	 * Handles a delete concept request via the JSON api. Error handling is done in this method, if needed. In case of
	 * an error, the returned string will contain an error message which can be shown to the user.
	 * 
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return content for the response body
	 */
	public String deleteConceptAsJson(Request request, Response response);

	/**
	 * Handles a get concept request via the JSON api. Error handling is done in this method, if needed. In case of an
	 * error, the returned string will contain an error message which can be shown to the user.
	 * 
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return content for the response body
	 */
	public String getConceptAsJson(Request request, Response response);

	/**
	 * Handles a get concepts request via the JSON api. Error handling is done in this method, if needed. In case of an
	 * error, the returned string will contain an error message which can be shown to the user.
	 * 
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return content for the response body
	 */
	public Object getConceptsAsJson(Request request, Response response);
	
    /**
     * Handles requests with an unknown REST end point and the accept header field value application/json.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @return content for the response body
     */
    public Object getNotFoundAsJson(Request request, Response response);

}
