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
 * This is the interface for a group access right
 * 
 * @author Tobias Ableitner
 *
 */
public interface IGroupAccessRight {

    // TODO java doc
    public static final String READ_RIGHT = "readRight";
    
    // TODO java doc
    public static final String UPDATE_RIGHT = "updateRight";
    
    // TODO java doc
    public static final String DELETE_RIGHT = "deleteRight";
    
    // TODO java doc
    public static final String CHANGE_RIGHTS_RIGHT = "changeRightsRight";
    
	/**
	 * The id of the group, for which the group access rights are defined.
	 * @return id of the group
	 */
	public int getGroupId();
	
	/**
	 * The id of the concept, for which the group access rights are defined.
	 * @return id of the concept
	 */
	public String getConceptId();
	
	/**
	 * Setter for the concept id. The concept id must not be null or empty. Otherwise an {@link IllegalArgumentException} will be thrown.
	 * @param conceptId id of the concept, for which the group access rights are defined
	 */
	public void setConceptId(String conceptId);
	
	/**
	 * Getter for the read right.
	 * @return true if the group members are allowed to read the concept and false if not.
	 */
	public boolean hasReadRight();
	
	/**
	 * Getter for the update right.
	 * @return true if the group members are allowed to update the concept and false if not.
	 */
	public boolean hasUpdateRight();
	
	/**
	 * Getter for the delete right.
	 * @return true if the group members are allowed to delete the concept and false if not.
	 */
	public boolean hasDeleteRight();
	
	/**
	 * Getter for the change rights right.
	 * @return true if the group members are allowed to change the access rights for the concept and false if not.
	 */
	public boolean hasChangeRightsRight();
	
}
