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
package de.hdm.helpers;

/**
 * This class provides static methods to check parameter values.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class Checker {
	

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




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

	/**
	 * Checks a string against null and emptiness.
	 * @param stringToCheck the string which should be checked
	 * @param parameterName the name of the parameter which value is checked
	 * @throws IllegalArgumentException if stringToCheck is null or empty
	 */
	public static void checkNullAndEmptiness(String stringToCheck, String parameterName){
		Checker.checkNull(stringToCheck, parameterName);
		Checker.checkEmptiness(stringToCheck, parameterName);
	}
	
	/**
	 * Checks an object against null.
	 * @param objectToCheck the object which should be checked
	 * @param parameterName the name of the parameter which value is checked
	 * @throws IllegalArgumentException if objectToCheck is null
	 */
	public static void checkNull(Object objectToCheck, String parameterName){
		if(objectToCheck == null){
			throw new IllegalArgumentException("The parameter " + parameterName + " must not be null!");
		}
	}
	
	/**
	 * Checks a string against emptiness.
	 * @param stringToCheck the string which should be checked
	 * @param parameterName the name of the parameter which value is checked
	 * @throws IllegalArgumentException if stringToCheck is not null but empty
	 */
	public static void checkEmptiness(String stringToCheck, String parameterName){
		if(stringToCheck != null && stringToCheck.isEmpty()){
			throw new IllegalArgumentException("The parameter " + parameterName + " must not be empty!");
		}
	}
	
	/**
	 * Checks whether an integer is greater equals 0 or not.
	 * @param intToCheck the integer which should be checked
	 * @param parameterName the name of the parameter which value is checked
	 * @throws IllegalArgumentException if intToCheck is smaller 0
	 */
	public static void checkPositiveInteger(int intToCheck, String parameterName){
		if(intToCheck < 0){
			throw new IllegalArgumentException("The parameter " + parameterName + " must be greater euqals 0!");
		}
	}
	
	/**
	 * Checks whether an integer is greater than a minimum or not.
	 * @param intToCheck the integer which should be checked.
	 * @param parameterName the name of the parameter which value is checked
	 * @param minimum the minimum
	 * @throws IllegalArgumentException if intToCheck is smaller equals minimum
	 */
	public static void checkIntegerGreater(int intToCheck, String parameterName, int minimum){
		if(intToCheck <= minimum){
			throw new IllegalArgumentException("The parameter " + parameterName + " must be greater " + minimum + "!");
		}
	}
	
	/**
	 * Checks whether an integer is greater equals than a minimum or not.
	 * @param intToCheck the integer which should be checked.
	 * @param parameterName the name of the parameter which value is checked
	 * @param minimum the minimum
	 * @throws IllegalArgumentException if intToCheck is smaller minimum
	 */
	public static void checkIntegerGreaterEquals(int intToCheck, String parameterName, int minimum){
		if(intToCheck < minimum){
			throw new IllegalArgumentException("The parameter " + parameterName + " must be greater euqals " + minimum + "!");
		}
	}
	
	/**
	 * Checks whether a long is greater equals 0 or not.
	 * @param longToCheck the long which should be checked
	 * @param parameterName the name of the parameter which value is checked
	 * @throws IllegalArgumentException if longToCheck is smaller 0
	 */
	public static void checkPositiveLong(long longToCheck, String parameterName){
		if(longToCheck < 0){
			throw new IllegalArgumentException("The parameter " + parameterName + " must be greater euqals 0!");
		}
	}

	/**
	 * Checks whether a long is greater than a minimum or not.
	 * @param longToCheck the long which should be checked.
	 * @param parameterName the name of the parameter which value is checked
	 * @param minimum the minimum
	 * @throws IllegalArgumentException if longToCheck is smaller equals minimum
	 */
	public static void checkLongGreater(long longToCheck, String parameterName, long minimum){
		if(longToCheck <= minimum){
			throw new IllegalArgumentException("The parameter " + parameterName + " must be greater " + minimum + "!");
		}
	}

	/**
	 * Checks whether a long is greater equals than a minimum or not.
	 * @param longToCheck the long which should be checked.
	 * @param parameterName the name of the parameter which value is checked
	 * @param minimum the minimum
	 * @throws IllegalArgumentException if longToCheck is smaller equals minimum
	 */
	public static void checkLongGreaterEquals(long longToCheck, String parameterName, long minimum){
		if(longToCheck < minimum){
			throw new IllegalArgumentException("The parameter " + parameterName + " must be greater euqals " + minimum + "!");
		}
	}
	
	/**
	 * Checks whether an integer is a valid user id or not. It is valid, if it is greater equals 1.
	 * @param userId the integer which should be checked
	 * @throws IllegalArgumentException if userId is smaller 1
	 */
	public static void checkUserId(int userId){
		if(userId < 1){
			throw new IllegalArgumentException("The parameter userId must be greater 0!");
		}
	}
	
	/**
	 * Checks whether a string is a valid integer.
	 * @param integerAsString the string which should be checked
	 * @return true if the string is a valid integer and false if not
	 */
	public static boolean isStringInteger(String integerAsString){
		return integerAsString.matches("[0]|[1-9][0-9]*");
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




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	
}
