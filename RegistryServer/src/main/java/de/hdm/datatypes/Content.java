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
 * This abstract class is used to define the names, definitions, examples and notes of a concept in multiple languages.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public abstract class Content implements IContent {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Id of the content in the database.
	 */
	private int id;

	/**
	 * Language code of content's language. If the content should not belong to a specific language, it is null.
	 */
	private String languageCode;

	/**
	 * The concept's id, to which the content belongs.
	 */
	private String conceptId;

	/**
	 * The content itself. A name, definition, note or example.
	 */
	private String content;




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Creates a new Content and set's the value of {@link Content#id} to -1. This constructor should be used, to
	 * instantiate contents, which are not already stored in the database.
	 * 
	 * @param languageCode
	 *            the language code of the content's language. If the content is not language specific, it can be null.
	 *            But if it is empty an {@link IllegalArgumentException} will be thrown.
	 * @param conceptId
	 *            the concept's id to which the content belongs. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @param content
	 *            the content it self or in other words, the content's value. It can be empty. But if it null, an
	 *            {@link IllegalArgumentException} will be thrown.
	 */
	public Content(String languageCode, String conceptId, String content) {
		this.id = -1;
		this.setLanguageCode(languageCode);
		this.setConceptId(conceptId);
		this.setContent(content);
	}

	/**
	 * Creates a new Content. This constructor should be used, to instantiate contents, which are already stored in the
	 * database.
	 * 
	 * @param id
	 *            the id of the content in the database. It must be greater equals 1. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @param languageCode
	 *            the language code of the content's language. If the content is not language specific, it can be null.
	 *            But if it is empty an {@link IllegalArgumentException} will be thrown.
	 * @param conceptId
	 *            the concept's id to which the content belongs. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @param content
	 *            the content it self or in other words, the content's value. It can be empty. But if it null, an
	 *            {@link IllegalArgumentException} will be thrown.
	 */
	public Content(int id, String languageCode, String conceptId, String content) {
		this.setId(id);
		this.setLanguageCode(languageCode);
		this.setConceptId(conceptId);
		this.setContent(content);
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// getters and setters
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
    
    private void setId(int id) {
        Checker.checkIntegerGreater(id, "id", 0);
        this.id = id;
    }
    
	private void setLanguageCode(String languageCode) {
		Checker.checkEmptiness(languageCode, "languageCode");
		this.languageCode = languageCode;
	}

	private void setContent(String content) {
		Checker.checkNull(content, "content");
		this.content = content;
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
	 * @see de.hdm.datatypes.IContent#getId()
	 */
	@Override
	public int getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IContent#getLanguageCode()
	 */
	@Override
	public String getLanguageCode() {
		return this.languageCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IContent#getConceptId()
	 */
	@Override
	public String getConceptId() {
		return this.conceptId;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IContent#setConceptId(java.lang.String)
	 */
	@Override
	public void setConceptId(String conceptId) {
        Checker.checkEmptiness(conceptId, "conceptId");
        this.conceptId = conceptId;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IContent#getContent()
	 */
	@Override
	public String getContent() {
		return this.content;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IContent#getContentForHtmlContentTable()
	 */
	@Override
	public String getContentForHtmlContentTable() {
		return this.content.replace(new String(new char[]{13, 10}), "<br />");
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
