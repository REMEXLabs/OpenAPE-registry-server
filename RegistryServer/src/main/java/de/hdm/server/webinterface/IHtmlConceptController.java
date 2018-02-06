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
package de.hdm.server.webinterface;

import spark.Request;
import spark.Response;

// TODO java doc
public interface IHtmlConceptController {
	
	public String getAllConceptsPage(Request request, Response response);
	
	public String getMyConceptsPage(Request request, Response response);
	
	public String getNewConceptPage(Request request, Response response);
	
	public String handleNewConceptPost(Request request, Response response);
	
	public String getEditConceptPage(Request request, Response response);
	
	public String handleEditConceptPost(Request request, Response response);
	
	public String getShowConceptPage(Request request, Response response);
	
	public String getSearchConceptPage(Request request, Response response);
	
	public String handleSearchConceptPage(Request request, Response response);
	
	public String getConceptLogPage(Request request, Response response);
	
	public String handleDeleteConcept(Request request, Response response);
	
}
