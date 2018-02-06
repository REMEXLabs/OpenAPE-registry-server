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
public interface IHtmlInfoAndErrorController {

	public String getNotLoggedInInfoAndErrorPage(Request request, Response response);
	
	public String getLoggedInInfoAndErrorPage(Request request, Response response);
	
	public String getNotFoundPage(Request request, Response response);
	
	public String getLegalNoticePage(Request request, Response response);
	
	public String getPrivacyPolicyPage(Request request, Response response);

}
