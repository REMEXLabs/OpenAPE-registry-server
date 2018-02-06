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
package de.hdm.exceptions;

import de.hdm.helpers.Checker;

/**
 * This exception is thrown, if an error on the registry server occurs, which can not be referred to a more specific
 * error message.
 * 
 * This class is not thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class RegistryServerException extends Exception {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -8849596835316794929L;

	/**
	 * The exception which raised this error message.
	 */
	private Exception originalException;

	/**
	 * An error message which can be shown to the user.
	 */
	private String userMessage;
	
    /**
     * If it is true, a user message should be shown for this exception. If it is false, than not. True is the default
     * value.
     */
	private boolean showUserMessage = true;




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Creates a new {@link RegistryServerException} without a user message and an original exception.
	 * 
	 * @param message
	 *            the error message.
	 */
	public RegistryServerException(String message) {
		this(message, null);
	}

	/**
	 * Creates a new {@link RegistryServerException} without a user message and an original exception.
	 * 
	 * @param message
	 *            the error message.
	 */
	public RegistryServerException(String message, Exception originalException) {
		this(message, originalException, null);
	}

	/**
	 * Creates a new {@link RegistryServerException} with user message and original exception. The user message is an
	 * error message which can be shown to the user. The original exception is an exception, which raises this exception
	 * and
	 * was caught before throwing this exception.
	 * 
	 * @param message
	 *            the error message
	 * @param originalException
	 *            the exception which raised this error message
	 * @param userMessage
	 *            an error message which can be shown to the user
	 */
	public RegistryServerException(String message, Exception originalException, String userMessage) {
		super(message);
		this.setOriginalException(originalException);
		this.setUserMessage(userMessage);
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// getters and setters
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Getter for the original exception. The original exception is the exception, which raised this exception. But this
	 * is not always the case. Thus it can also be null.
	 * 
	 * @return original exception or null
	 */
	public Exception getOriginalException() {
		return this.originalException;
	}

	private void setOriginalException(Exception originalException) {
		this.originalException = originalException;
	}

	/**
	 * Getter for the user error message. The user error message can be shown to the user. It is not always set. Thus it
	 * can also be null.
	 * 
	 * @return error message for the user or null
	 */
	public String getUserMessage() {
		return this.userMessage;
	}

	private void setUserMessage(String userMessage) {
		Checker.checkEmptiness(userMessage, "userMessage");
		this.userMessage = userMessage;
	}

    /**
     * Getter for the flag, whether a user message should be shown or not. Returns true if a user message should be
     * shown and false if not. The default value is true.
     * 
     * @return true is a user message should be shown and false if not.
     */
    public boolean isShowUserMessage() {
        return this.showUserMessage;
    }

    /**
     * Setter for the flag, whether a user message for this exception should be shown or not. True if a user message
     * should be shown and false if not. The default value is true.
     * 
     * @param showUserMessage
     *            true if a user message should be shown and false if not.
     */
    public void setShowUserMessage(boolean showUserMessage) {
        this.showUserMessage = showUserMessage;
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
