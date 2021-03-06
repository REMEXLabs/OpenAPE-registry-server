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
package de.hdm.datatypes;

import de.hdm.helpers.Checker;

/**
 * This class defines a language. A language is defined by its name and a code. The code is conform to IETF BCP 47.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class Language implements ILanguage {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * The language's code conform to IETF BCP 47.
	 */
	private String code;

	/**
	 * The language's name.
	 */
	private String name;




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Create a new Language.
	 * 
	 * @param code
	 *            the language's code. It must not be null or empty. Otherwise an {@link IllegalArgumentException} will
	 *            be thrown. It is not check, whether it is conform to IETF BCP 47. When it is not, it can raise
	 *            problems and / or exception at a later time.
	 * @param name
	 *            the language's name. It must not be null or empty. Otherwise an {@link IllegalArgumentException} will
	 *            be thrown.
	 */
	public Language(String code, String name) {
		this.setCode(code);
		this.setName(name);
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// getters and setters
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	private void setCode(String code) {
		Checker.checkEmptiness(code, "code");
		this.code = code;
	}

	private void setName(String name) {
		Checker.checkEmptiness(name, "name");
		this.name = name;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.ILanguage#getCode()
	 */
	@Override
	public String getCode() {
		return this.code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.ILanguage#getName()
	 */
	@Override
	public String getName() {
		return this.name;
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
