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

import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IUnitOfWork;
import de.hdm.databaseaccess.UnitOfWorkStateEnum;
import de.hdm.exceptions.RegistryServerException;
import de.hdm.helpers.TemplateFiller;
import de.hdm.multiplelanguages.LanguageHandler;
import de.hdm.server.AuthenticationException;
import de.hdm.server.BadRequestException;
import de.hdm.server.Path;
import de.hdm.server.SessionUtil;
import spark.Request;
import spark.Response;

public abstract class HtmlController {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Logger.
	 */
	private final static Logger logger = LogManager.getLogger(HtmlController.class);

	protected static final String MODEL_VALUE_KEY_INFO_MESSAGE = "infoMessage";

	protected static final String MODEL_VALUE_KEY_ERROR_MESSAGE = "errorMessage";

	protected static final String MODEL_VALUE_KEY_OPENED_MENU = "openedMenu";




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

	protected void handleSuccessMessage(Request request, Map<String, Object> model){
		String successMessage = SessionUtil.getSuccessMessage(request);
		if(successMessage != null){
			SessionUtil.removeSuccessMessage(request);
			model.put(MODEL_VALUE_KEY_INFO_MESSAGE, successMessage);
		}
	}

	protected void handleAuthenticationException(Request request, Response response, AuthenticationException e, Locale locale){
	    request.session().invalidate();
	    if(e != null && e.isRedirect()){
		    String redirect = request.pathInfo();
		    String queryString = request.queryString();
		    if(queryString != null && !queryString.isEmpty()){
		        redirect += "?" + queryString;
		    }
		    SessionUtil.setRedirect(request, redirect);
		    response.redirect(Path.Web.LOGIN);
		}else{
		    SessionUtil.setErrorMessage(request, LanguageHandler.getWord(locale, "WEB_DISALLOWED_REQUEST_ERROR_MESSAGE"));
		    response.redirect(Path.Web.NOT_LOGGED_IN_INFO_AND_ERROR);
		}

	}

	/*protected static void handleUserIsNotLoggedInStatic(Request request, Response response){
		SessionUtil.setRedirect(request, request.pathInfo());
		response.redirect(Path.Web.LOGIN);
	}*/

	protected void abortUnitOfWork(IUnitOfWork unitOfWork){
		if(unitOfWork != null && unitOfWork.getState() == UnitOfWorkStateEnum.STARTED){
			try {
				unitOfWork.abort();
			} catch (DataAccessException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
	}

	protected void handleSuccess(Locale locale, Request request, Response response, String templateKey, String value, String redirect){
		String successMessage = LanguageHandler.getWord(locale, templateKey);
		if(value != null){
			successMessage = TemplateFiller.fillTemplate(successMessage, value);
		}
		SessionUtil.setSuccessMessage(request, successMessage);
		response.redirect(redirect);
	}

	protected void setOpenedMenu(Request request, Map<String, Object> model, MenuEnum openedMenu){
		SessionUtil.setOpenedMenu(request, openedMenu);
		model.put(MODEL_VALUE_KEY_OPENED_MENU, openedMenu.toString());
	}

	protected void handleExpception(Locale locale, IUnitOfWork unitOfWork, Exception exception, Map<String, Object> model, Request request, Response response, String userMessageTemplateKey){
        exception.printStackTrace();
        logger.error(exception.getMessage());

        this.abortUnitOfWork(unitOfWork);

        this.setResponseStatusCodeInErrorCase(response, exception);

        if(exception instanceof AuthenticationException){
            this.handleAuthenticationException(request, response, (AuthenticationException)exception, locale);
        }else if(exception instanceof RegistryServerException == false || ((RegistryServerException)exception).isShowUserMessage()){
            // create user message if not existing
            String userMessage = null;
            if(exception instanceof RegistryServerException){
                userMessage = ((RegistryServerException)exception).getUserMessage();
            }
            if((userMessage == null || userMessage.isEmpty()) && userMessageTemplateKey != null && !userMessageTemplateKey.isEmpty()){
                userMessage = LanguageHandler.getWord(locale, userMessageTemplateKey);
            }

            if (userMessage != null && !userMessage.isEmpty()) {
                // insert user message into model
                model.put(MODEL_VALUE_KEY_ERROR_MESSAGE, userMessage);
            }
        }
    }

   protected void setResponseStatusCodeInErrorCase(Response response, Exception exception){
        if(exception instanceof AuthenticationException){
            response.status(403); // status code Forbidden
        }else if(exception instanceof BadRequestException){
            response.status(400); // status code Bad Request
        }
    }




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
