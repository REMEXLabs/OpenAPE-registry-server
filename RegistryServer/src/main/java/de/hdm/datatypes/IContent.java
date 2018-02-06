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
 * This is the interface for a content.
 * 
 * @author Tobias Ableitner
 *
 */
public interface IContent {

    /**
     * Getter for the content's id in the database.
     * 
     * @return the contents id in the database or -1, if the content was not already stored in the database
     */
    public int getId();

    /**
     * Getter for the content's language code.
     * 
     * @return language code of the content or null, if the content is not language specific
     */
    public String getLanguageCode();

    /**
     * Getter for concept's id, to which the content belongs.
     * 
     * @return id of the concept, to which the content belongs
     */
    public String getConceptId();

    /**
     * Setter for the concept's id, to which this content belongs.
     * 
     * @param conceptId
     *            the concept's id. It must be greater equals 1. Otherwise an {@link IllegalArgumentException} will be
     *            thrown.
     */
    public void setConceptId(String conceptId);

    /**
     * Getter for the content it self or in other words the content's value. This can be a name, definition, example or
     * note of a concept.
     * If you want to display the content's value in the web interface use
     * {@link IContent#getContentForHtmlContentTable()} to get a string with compatible line breaks.
     * 
     * @return value of the content
     */
    public String getContent();

    /**
     * This method returns also the value of the content like {@link IContent#getContent()}, but with line breaks, which
     * are compatible with the web interface.
     * 
     * @return value of the content with web interface compatible line breaks.
     */
    public String getContentForHtmlContentTable();

}
