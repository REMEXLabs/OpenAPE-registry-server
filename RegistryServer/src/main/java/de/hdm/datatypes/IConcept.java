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

import java.util.List;
import java.util.Locale;

/**
 * This is the interface for a concept.
 * 
 * @author Tobias Ableitner
 *
 */
public interface IConcept {

	/**
	 * Getter for the concept's id.
	 * 
	 * @return id of the concept
	 */
	public String getId();

	/**
	 * Setter for the concept's id. The id must not be null or empty. Otherwise an {@link IllegalArgumentException} will
	 * be thrown.
	 * 
	 * @param id
	 *            of the concept
	 */
	public void setId(String id);

	/**
	 * Getter for the concept's names. If no names are defined, an empty list will be returned.
	 * 
	 * @return list with the concept's names.
	 */
	public List<IName> getNames();

	/**
	 * Setter for the concept's names. The list with the names must not be null. Otherwise an
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param names
	 *            List with the names or an empty one
	 */
	public void setNames(List<IName> names);
	
    /**
     * Get the concept's name by locale. The selection is done by the following order: 1. Check if language and country
     * matches and return if appropriate. 2. Check if language matches and return if appropriate. 3. Check if there is
     * an English version of the name and return if appropriate. 4. Check if there is a name without language code,
     * which means that this name is not language specific and return if appropriate. 5. Return the first element of the
     * concept's name list {@link IConcept#getNames()}.
     * 
     * @param locale
     *            the locale which is used to find the matching name. It can be null. If it is null, the selection
     *            starts with step 3.
     * @return the most matching name. Null will never be returned.
     */
	public IName getNameByLocale(Locale locale);

	/**
	 * Getter for the concept's definitions. If no definitions are defined, an empty list will be returned.
	 * 
	 * @return list with the concept's definitions.
	 */
	public List<IDefinition> getDefinitions();

	/**
	 * Setter for the concept's definitions. The list with the definitions must not be null. Otherwise an
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param definitions
	 *            List with the definitions or an empty one
	 */
	public void setDefinitions(List<IDefinition> definitions);

	/**
	 * Getter for the concept's notes. If no notes are defined, an empty list will be returned.
	 * 
	 * @return list with the concept's notes.
	 */
	public List<INote> getNotes();

	/**
	 * Setter for the concept's notes. The list with the notes must not be null. Otherwise an
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param notes
	 *            List with the notes or an empty one
	 */
	public void setNotes(List<INote> notes);

	/**
	 * Getter for the concept's examples. If no examples are defined, an empty list will be returned.
	 * 
	 * @return list with the concept's examples.
	 */
	public List<IExample> getExamples();

	/**
	 * Setter for the concept's examples. The list with the examples must not be null. Otherwise an
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param examples
	 *            List with the examples or an empty one
	 */
	public void setExamples(List<IExample> examples);

	/**
	 * Getter for the concept's type.
	 * 
	 * @return type of the concept
	 */
	public TypeEnum getType();

	/**
	 * Setter for the concept's type. It must not be null. Otherwise an {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param type
	 *            the concept's type
	 */
	public void setType(TypeEnum type);

	/**
	 * Getter for the concept's sub type.
	 * 
	 * @return sub type of the concept
	 */
	public SubTypeEnum getSubType();

	/**
	 * Setter for the concept's sub type. It must not be null. Otherwise an {@link IllegalArgumentException} will be
	 * thrown.
	 * 
	 * @param subType
	 *            the concept's sub type
	 */
	public void setSubType(SubTypeEnum subType);

	/**
	 * Getter for the concept's sub type.
	 * 
	 * @return sub type of the concept
	 */
	public String getOrigin();

	/**
	 * Setter for the concept's origin. It must not be null. Otherwise an {@link IllegalArgumentException} will be
	 * thrown.
	 * 
	 * @param origin
	 *            the concept's origin
	 */
	public void setOrigin(String origin);

	/**
	 * Getter for the concept's data type.
	 * 
	 * @return data type of the concept
	 */
	public DataTypeEnum getDataType();

	/**
	 * Setter for the concept's data type. It must not be null. Otherwise an {@link IllegalArgumentException} will be
	 * thrown.
	 * 
	 * @param dataType
	 *            the concept's data type
	 */
	public void setDataType(DataTypeEnum dataType);

	/**
	 * Getter for the concept's value space.
	 * 
	 * @return value space of the concept
	 */
	public IValueSpace getValueSpace();

	/**
	 * Setter for the concept's value space. It must not be null. Otherwise an {@link IllegalArgumentException} will be
	 * thrown.
	 * 
	 * @param valueSpace
	 *            the concept's valueSpace
	 */
	public void setValueSpace(IValueSpace valueSpace);

	/**
	 * Getter for the concept's authors. If no authors are defined, an empty list will be returned.
	 * 
	 * @return list with the concept's authors.
	 */
	public List<String> getAuthors();

	/**
	 * Setter for the concept's authors. The list with the authors must not be null. Otherwise an
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param authors
	 *            List with the authors or an empty one
	 */
	public void setAuthors(List<String> authors);

	/**
	 * Getter for the concept's owners. If no owners are defined, an empty list will be returned.
	 * 
	 * @return list with the concept's owners.
	 */
	public List<Integer> getOwners();

	/**
	 * Setter for the concept's owners. The list with the owners must not be null. Otherwise an
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param owners
	 *            List with the owners or an empty one
	 */
	public void setOwners(List<Integer> owners);

	
	/**
	 * Getter for the concept's refinements. If no refinements are defined, an empty list will be returned.
	 * 
	 * @return list with the concept's refinements.
	 */
	//public List<String> getRefinements();

	/**
	 * Setter for the concept's refinements. The list with the refinements must not be null. Otherwise an
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param refinements
	 *            List with the refinements or an empty one
	 */
	//public void setRefinements(List<String> refinements);
	
	// TODO java doc
	public List<String> getConceptsWhichRefineThisConcept();
	
	// TODO java doc
	public void setConceptsWhichRefineThisConcept(List<String> conceptsWhichRefineThisConcept);
	
	// TODO java doc
	public List<String> getConceptsWhichAreRefinedByThisConcept();
	
	// TODO java doc
	public void setConceptsWhichAreRefinedByThisConcept(List<String> conceptsWhichAreRefinedByThisConcept);

	/**
	 * Getter for the concept's transformations. If no transformations are defined, an empty list will be returned.
	 * 
	 * @return list with the concept's transformations.
	 */
	//public List<String> getTransformations();
	
	/**
	 * Setter for the concept's transformations. The list with the transformations must not be null. Otherwise an
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param transformations
	 *            List with the transformations or an empty one
	 */
	//public void setTransformations(List<String> transformations);
	
	// TODO java doc
	public List<String> getConceptsWhichTransformThisConcept();
	
	// TODO java doc
	public void setConceptsWhichTransformThisConcept(List<String> conceptsWhichTransformThisConcept);
	
	// TODO java doc
	public List<String> getConceptsWhichAreTransformedByThisConcept();
	
	// TODO java doc
	public void setConceptsWhichAreTransformedByThisConcept(List<String> conceptsWhichAreTransformedByThisConcept);

	/**
	 * Getter for the date time stamp of the concept's last update. The date time stamp will be returned in
	 * milliseconds.
	 * 
	 * @return concept's last update date time stamp in milliseconds
	 */
	public long getUpdatedInMilliseconds();

	/**
	 * Setter for the concept's date time stamp in milliseconds of the last update. It must not be greater equals 0.
	 * Otherwise an {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param updatedInMilliseconds
	 *            date time stamp in milliseconds of the concept's last update
	 */
	public void setUpdatedInMilliseconds(long updatedInMilliseconds);

	/**
	 * Getter for the concept's group access rights. If no group access rights are defined, an empty list will be
	 * returned.
	 * 
	 * @return list with the concept's group access rights.
	 */
	public List<IGroupAccessRight> getGroupAccessRights();

	/**
	 * Setter for the concept's group access rights. The list with the group access rights must not be null. Otherwise
	 * an {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param groupAccessRights
	 *            List with the groupAccessRights or an empty one
	 */
	public void setGroupAccessRights(List<IGroupAccessRight> groupAccessRights);
	
    /**
     * Checks whether the user defined by the parameter user has read access for this concept.
     * 
     * @param user
     *            the user. It must not be null! Otherwise an {@link IllegalArgumentException} will be thrown.
     * @param usersGroups
     *            list of groups in which the user is a member. It must not be null! Otherwise an
     *            {@link IllegalArgumentException} will be thrown.
     * @return true if the user has read access for this concept and false if not.
     */
    public boolean hasUserReadRight(IUser user, List<IGroup> usersGroups);

    /**
     * Checks whether the user defined by the parameter user has update access for this concept.
     * 
     * @param user
     *            the user. It must not be null! Otherwise an {@link IllegalArgumentException} will be thrown.
     * @param usersGroups
     *            list of groups in which the user is a member. It must not be null! Otherwise an
     *            {@link IllegalArgumentException} will be thrown.
     * @return true if the user has update access for this concept and false if not.
     */
    public boolean hasUserUpdateRight(IUser user, List<IGroup> usersGroups);

    /**
     * Checks whether the user defined by the parameter user has delete access for this concept.
     * 
     * @param user
     *            the user. It must not be null! Otherwise an {@link IllegalArgumentException} will be thrown.
     * @param usersGroups
     *            list of groups in which the user is a member. It must not be null! Otherwise an
     *            {@link IllegalArgumentException} will be thrown.
     * @return true if the user has delete access for this concept and false if not.
     */
    public boolean hasUserDeleteRight(IUser user, List<IGroup> usersGroups);

    /**
     * Checks whether the user defined by the parameter user has change rights access for this concept.
     * 
     * @param user
     *            the user. It must not be null! Otherwise an {@link IllegalArgumentException} will be thrown.
     * @param usersGroups
     *            list of groups in which the user is a member. It must not be null! Otherwise an
     *            {@link IllegalArgumentException} will be thrown.
     * @return true if the user has change rights access for this concept and false if not.
     */
    public boolean hasUserChangeRightsRight(IUser user, List<IGroup> usersGroups);
    
    public boolean isPublic();

}
