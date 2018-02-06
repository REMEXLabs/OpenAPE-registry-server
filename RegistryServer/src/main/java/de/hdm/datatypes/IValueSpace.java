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
 * This is the interface for a value space.
 * 
 * @author Tobias Ableitner
 *
 */
public interface IValueSpace {

    /**
     * Getter for the JSON schema of a concept's value. For example a range of possible integer values for a concept
     * with the data type number {@link DataTypeEnum#NUMBER}.
     * 
     * @return JSON schema of a concept's value.
     */
    public String getJsonSchema();

    /**
     * Setter for the JSON schema of a concept's value.
     * 
     * @param jsonSchema
     *            the JSON schema of a concept's value. It must not be null or empty. Otherwise an
     *            {@link IllegalArgumentException} will be thrown!
     */
    public void setJsonSchema(String jsonSchema);

    /**
     * Sets the default description in the JSON schema {@link IValueSpace#getJsonSchema()} as description if there
     * exists no other.
     * 
     * @param conceptId
     *            the id of the concept, to which this value space / JSON schema belongs. It must not be null or empty.
     *            Otherwise an {@link IllegalArgumentException} will be thrown!
     */
    public void setDefaultDesciptionIfThereExistsNoOther(String conceptId);

}
