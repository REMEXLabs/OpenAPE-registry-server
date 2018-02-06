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

/**
 * This is the interface for a language.
 * 
 * @author Tobias Ableitner
 *
 */
public interface ILanguage {

	/**
	 * Getter for the language's code. It should be conform to IETF BCP 47, but it is not ensured.
	 * @return the language's code
	 */
	public String getCode();

	/**
	 * Getter for the language's name.
	 * @return the language's name
	 */
	public String getName();

}
