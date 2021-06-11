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

import de.hdm.helpers.Checker;
import de.hdm.server.webinterface.MenuEnum;
import spark.Request;

public class SessionUtil {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	public static final String USER_ID = "userId";
	
	public static final String USER_ID_FOR_PASSWORD_RESET = "userIdForPasswordReset";
	
	public static final String LOGGED_OUT = "loggedOut";
	
	public static final String REDIRECT = "redirect";
	
	public static final String SUCCESS_MESSAGE = "successMessage";
	
	public static final String OPENED_MENU = "openedMenu";
	
	public static final String INFO_MESSAGE = "infoMessage";
	
	public static final String ERROR_MESSAGE = "errorMessage";

	
	

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

	public static String getSessionId(Request request){
		return request.session().id();
	}
	
	public static void setUserId(Request request, int userId){
		//System.out.println("in setUserId userdId = " + userId);
	    Checker.checkNull(request, "request");
		Checker.checkUserId(userId);
		request.session().attribute(USER_ID, userId);
	}
	
	public static int getUserId(Request request){
		//System.out.println("in getUserId");
	    Checker.checkNull(request, "request");
		Object tmp = request.session().attribute(USER_ID);
		int userId = -1;
		if(tmp != null){
			userId = (int)tmp;
		}
		return userId;
	}
	
	public static void removeUserId(Request request){
		removeSessionAttribute(request, USER_ID);
	}
	
	public static void setUserIdForPasswordReset(Request request, int userIdForPasswordReset){
		Checker.checkNull(request, "request");
		Checker.checkUserId(userIdForPasswordReset);
		request.session().attribute(USER_ID_FOR_PASSWORD_RESET, userIdForPasswordReset);
	}
	
	public static int getUserIdForPasswordReset(Request request){
		Checker.checkNull(request, "request");
		Object tmp = request.session().attribute(USER_ID_FOR_PASSWORD_RESET);
		int userIdForPasswordReset = -1;
		if(tmp != null){
			userIdForPasswordReset = (int)tmp;
		}
		return userIdForPasswordReset;
	}
	
	public static void removeUserIdForPasswordReset(Request request){
		removeSessionAttribute(request, USER_ID_FOR_PASSWORD_RESET);
	}
	
	public static void setSessionAttributeLoggedOut(Request request){
		request.session().attribute(LOGGED_OUT, true);
	}
	
	public static boolean getSessionAttributeLoggedOut(Request request){
		Checker.checkNull(request, "request");
		boolean loggedOut = false;
		Object tmp = request.session().attribute(LOGGED_OUT);
		if(tmp != null && tmp instanceof Boolean) {
			loggedOut = (Boolean)tmp;
		}
		return loggedOut;
	}
	
	public static void removeLoggedOut(Request request){
		removeSessionAttribute(request, LOGGED_OUT);
	}
	
	public static void setRedirect(Request request, String redirect){
		Checker.checkNull(request, "request");
		Checker.checkNullAndEmptiness(redirect, "redirect");
		request.session().attribute(REDIRECT, redirect);
	}
	
	public static String getRedirect(Request request){
		Checker.checkNull(request, "request");
		return request.session().attribute(REDIRECT);
	}
	
	public static void removeRedirect(Request request){
		Checker.checkNull(request, "request");
		removeSessionAttribute(request, REDIRECT);
	}
	
	public static void setSuccessMessage(Request request, String successMessage){
		Checker.checkNull(request, "request");
		Checker.checkNull(successMessage, "successMessage");
		request.session().attribute(SUCCESS_MESSAGE, successMessage);
	}
	
	public static String getSuccessMessage(Request request){
		Checker.checkNull(request, "request");
		return request.session().attribute(SUCCESS_MESSAGE);
	}
	
	public static void removeSuccessMessage(Request request){
		removeSessionAttribute(request, SUCCESS_MESSAGE);
	}
	
	public static void setOpenedMenu(Request request, MenuEnum openedMenu){
		Checker.checkNull(request, "request");
		Checker.checkNull(openedMenu, "openedMenu");
		request.session().attribute(OPENED_MENU, openedMenu);
	}
	
	public static String getOpenedMenu(Request request){
		Checker.checkNull(request, "request");
		return request.session().attribute(OPENED_MENU);
	}
	
	public static void removeOpendedMenu(Request request){
		removeSessionAttribute(request, OPENED_MENU);
	}
	
	public static void setInfoMessage(Request request, String infoMessage){
		Checker.checkNull(request, "request");
		Checker.checkNullAndEmptiness(infoMessage, "infoMessage");
		request.session().attribute(INFO_MESSAGE, infoMessage);
	}
	
	public static String getInfoMessage(Request request){
		Checker.checkNull(request, "request");
		return request.session().attribute(INFO_MESSAGE);
	}
	
	public static void removeInfoMessage(Request request){
		removeSessionAttribute(request, INFO_MESSAGE);
	}

	public static void setErrorMessage(Request request, String errorMessage){
		Checker.checkNull(request, "request");
		Checker.checkNullAndEmptiness(errorMessage, "errorMessage");
		request.session().attribute(ERROR_MESSAGE, errorMessage);
	}
	
	public static String getErrorMessage(Request request){
		Checker.checkNull(request, "request");
		return request.session().attribute(ERROR_MESSAGE);
	}
	
	public static void removeErrorMessage(Request request){
		removeSessionAttribute(request, ERROR_MESSAGE);
	}

	

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

	private static void removeSessionAttribute(Request request, String attributeName){
		Checker.checkNull(request, "request");
		Checker.checkNullAndEmptiness(attributeName, "attributeName");
		request.session().removeAttribute(attributeName);
	}



	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
