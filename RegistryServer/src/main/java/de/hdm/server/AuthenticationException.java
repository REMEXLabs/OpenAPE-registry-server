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

import de.hdm.exceptions.RegistryServerException;

/**
 * This exception is thrown, if an authentication error occur. For example if an user tries to delete a concept without
 * having the delete right.
 * 
 * This class is not thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class AuthenticationException extends RegistryServerException {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 1533903886862400046L;
	
	// TODO java doc
	private boolean redirect;




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Creates a new {@link AuthenticationException} without a user message and an original exception.
	 * 
	 * @param message
	 *            the error message.
	 * @param redirect true if the user should be redirected after he logged in successfully and false if not
	 */
	public AuthenticationException(String message, boolean redirect) {
		this(message, null, redirect);
	}

	/**
	 * Creates a new {@link AuthenticationException} without a user message but an original exception. The original
	 * exception is an exception, which raises this exception and was caught before throwing this exception.
	 * 
	 * @param message
	 *            the error message.
	 * @param originalException
	 *            the exception which raised this error message
	 * @param redirect true if the user should be redirected after he logged in successfully and false if not
	 */
	public AuthenticationException(String message, Exception originalException, boolean redirect) {
		this(message, originalException, null, redirect);
	}

	/**
	 * Creates a new {@link AuthenticationException} with user message and original exception. The user message is an
	 * error
	 * message which can be shown to the user. The original exception is an exception, which raises this exception and
	 * was caught before throwing this exception.
	 * 
	 * @param message
	 *            the error message
	 * @param originalException
	 *            the exception which raised this error message
	 * @param userMessage
	 *            an error message which can be shown to the user
	 * @param redirect true if the user should be redirected after he logged in successfully and false if not
	 */
	public AuthenticationException(String message, Exception originalException, String userMessage, boolean redirect) {
		super(message, originalException, userMessage);
		this.setRedirect(redirect);
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// getters and setters
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	public void setRedirect(boolean redirect){
		this.redirect = redirect;
	}

	public boolean isRedirect(){
		return this.redirect;
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
