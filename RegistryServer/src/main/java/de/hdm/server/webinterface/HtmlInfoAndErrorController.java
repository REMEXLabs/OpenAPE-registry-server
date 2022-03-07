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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import de.hdm.datatypes.IUser;
import de.hdm.multiplelanguages.LanguageHandler;
import de.hdm.server.AuthenticationController;
import de.hdm.server.AuthenticationException;
import de.hdm.server.Path;
import de.hdm.server.RequestUtil;
import de.hdm.server.SessionUtil;
import spark.Request;
import spark.Response;

public class HtmlInfoAndErrorController extends HtmlController implements IHtmlInfoAndErrorController {
	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Logger.
	 */
	private final static Logger logger = LogManager.getLogger(HtmlInfoAndErrorController.class);

	
	

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




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

	@Override
	public String getNotLoggedInInfoAndErrorPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();	
		
		// insert error message
        String errorMessage = SessionUtil.getErrorMessage(request);
        if(errorMessage != null){
            SessionUtil.removeErrorMessage(request);
            model.put(MODEL_VALUE_KEY_ERROR_MESSAGE, errorMessage);
        }
        
		// insert success message
		String successMessage = SessionUtil.getSuccessMessage(request);
		if(successMessage != null){
			SessionUtil.removeSuccessMessage(request);
			model.put(MODEL_VALUE_KEY_INFO_MESSAGE, successMessage);
		}
		
		return ViewUtil.render(request, model, Path.WebTemplate.NOT_LOGGED_IN_INFO_AND_ERROR, locale);
	}	
	
	@Override
	public String getLoggedInInfoAndErrorPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			// authentication
			AuthenticationController.ensureUserIsLoggedIn(request);
			
			// insert success message
			String successMessage = SessionUtil.getSuccessMessage(request);
			if(successMessage != null){
				SessionUtil.removeSuccessMessage(request);
				model.put(MODEL_VALUE_KEY_INFO_MESSAGE, successMessage);
			}
		} catch (AuthenticationException e) {
			this.handleAuthenticationException(request, response, e, locale);
		}
		return ViewUtil.render(request, model, Path.WebTemplate.LOGGED_IN_INFO_AND_ERROR, locale);
	}

    @Override
    public String getNotFoundPage(Request request, Response response) {
        Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
        Map<String, Object> model = new HashMap<String, Object>();
        IUser user = null;
        
        try{
            user = AuthenticationController.ensureUserIsLoggedIn(request);
        }catch(AuthenticationException e){
            // do nothing
        }
        
        try{
            if(user != null){
                throw new AuthenticationException("User " + user.getId() + " requested / clicked an invalid link within a session!", false);
            }else{
                model.put(MODEL_VALUE_KEY_ERROR_MESSAGE, LanguageHandler.getWord(locale, "WEB_NOT_FOUND_PAGE_MESSAGE"));
            }    
        }catch(AuthenticationException e){
            this.handleAuthenticationException(request, response, e, locale);
        }
        response.status(404);
        return ViewUtil.render(request, model, Path.WebTemplate.NOT_LOGGED_IN_INFO_AND_ERROR, locale);
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

	


	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
