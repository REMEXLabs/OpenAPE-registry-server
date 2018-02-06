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

import de.hdm.helpers.Checker;

/**
 * This class represents a concept record.
 * 
 * This class is not thread safe.
 * 
 * @author Tobias Ableitner
 * 
 */
public class Concept implements IConcept {
	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	
	/**
	 * Id / URI of the concept. It has to be unique application wide.
	 */
	private String id;

	/**
	 * The names of the concept.
	 */
	private List<IName> names;
	
	/**
	 * The definitions of the concept.
	 */
	private List<IDefinition> definitions;
	
	/**
	 * Notes to the concept.
	 */
	private List<INote> notes;
	
	/**
	 * Examples to the concept.
	 */
	private List<IExample> examples;
	
	/**
	 * The type of the concept.
	 */
	private TypeEnum type;
	
	/**
	 * The sub type of the concept.
	 */
	private SubTypeEnum subType;
	
	/**
	 * The origin of the concept.
	 */
	private String origin;
	
	/**
	 * The data type of the concept.
	 */
	private DataTypeEnum dataType;
	
	/**
	 * The value space of the concept. It defines, which values are valid. For example a range of integer values.
	 */
	private IValueSpace valueSpace;
	
	/**
	 * The authors of the concept.
	 */
	private List<String> authors;
	
	/**
	 * The owners of the concept.
	 */
	private List<Integer> owners;
	
	// TODO java doc
	private List<String> conceptsWhichRefineThisConcept;
	
	// TODO java doc
	private List<String> conceptsWhichAreRefinedByThisConcept;
	
	// TODO java doc
	private List<String> conceptsWhichTransformThisConcept;
	
	// TODO java doc
	private List<String> conceptsWhichAreTransformedByThisConcept;
	
	/**
	 * Date in milliseconds of the concepts last update.
	 */
	private long updatedInMilliseconds;
	
	/**
	 * The group access rights for the concept.
	 */
	private List<IGroupAccessRight> groupAccessRights;
	
	


	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Creates a new concept.
	 * @param id of the concepts
	 */
	public Concept(String id){
		this.setId(id);
	}
	


	
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

	/* (non-Javadoc)
	 * @see IConcept#getId()
	*/
	@Override
	public String getId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see IConcept#setId(String id)
	*/
	@Override
	public void setId(String id) {
		// TODO is this check needed?
		//Checker.checkNullAndEmptiness(id, "id");
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see IConcept#getNames()
	*/
	@Override
	public List<IName> getNames() {
		return this.names;
	}
	
	/* (non-Javadoc)
	 * @see IConcept#setNames(List<IName> names)
	*/
	@Override
	public void setNames(List<IName> names) {
		Checker.checkNull(names, "names");
		this.names = names;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IConcept#getNameByLocale(java.util.Locale)
	 */
	@Override
	public IName getNameByLocale(Locale locale) {
		String defaultLanguage = "en";
		String language = locale.getLanguage();
		String country = locale.getCountry();
		String languageAndCountry = null;
		if(country != null && country.isEmpty() == false){
			languageAndCountry = language + "-" + country;
		}
		IName matchingName = null;
		
		// check whether language and country matches
		if(languageAndCountry != null){
			for(IName name : this.names){
				if(name.getLanguageCode() != null && name.getLanguageCode().contains(languageAndCountry)){
					matchingName = name;
					break;
				}
			}
		}
		
		// check whether language matches
		if(matchingName == null && language != null){
			for(IName name : this.names){
				if(name.getLanguageCode() != null && name.getLanguageCode().contains(language)){
					matchingName = name;
					break;
				}
			}
		}
		
		// check whether default language matches
		if(matchingName == null){
			for(IName name : this.names){
				if(name.getLanguageCode() != null && name.getLanguageCode().contains(defaultLanguage)){
					matchingName = name;
					break;
				}
			}
		}
		
		// search non language specific name
		if(matchingName == null){
			for(IName name : this.names){
				if(name.getLanguageCode() == null){
					matchingName = name;
					break;
				}
			}
		}
		
		// get one name
		if(matchingName == null){
			matchingName = this.names.get(0);
		}
		
		return matchingName;
	}

	/* (non-Javadoc)
	 * @see IConcept#getDefinitions()
	*/
	@Override
	public List<IDefinition> getDefinitions() {
		return this.definitions;
	}

	/* (non-Javadoc)
	 * @see IConcept#setDefinitions(List<IDefinition> definitions)
	*/
	@Override
	public void setDefinitions(List<IDefinition> definitions) {
		Checker.checkNull(definitions, "definitions");
		this.definitions = definitions;
	}

	/* (non-Javadoc)
	 * @see IConcept#getNotes()
	*/
	@Override
	public List<INote> getNotes() {
		return this.notes;
	}

	/* (non-Javadoc)
	 * @see IConcept#setNotes(List<INote> notes)
	*/
	@Override
	public void setNotes(List<INote> notes) {
		Checker.checkNull(notes, "notes");
		this.notes = notes;
	}

	/* (non-Javadoc)
	 * @see IConcept#getExamples()
	*/
	@Override
	public List<IExample> getExamples() {
		return this.examples;
	}
	
	/* (non-Javadoc)
	 * @see IConcept#setExamples(List<IExample> examples)
	*/
	@Override
	public void setExamples(List<IExample> examples) {
		Checker.checkNull(examples, "examples");
		this.examples = examples;
	}

	/* (non-Javadoc)
	 * @see IConcept#getType()
	*/
	@Override
	public TypeEnum getType() {
		return this.type;
	}

	/* (non-Javadoc)
	 * @see IConcept#setType(TypeEnum type)
	*/
	@Override
	public void setType(TypeEnum type) {
		Checker.checkNull(type, "type");
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see IConcept#getSubType()
	*/
	@Override
	public SubTypeEnum getSubType() {
		return this.subType;
	}

	/* (non-Javadoc)
	 * @see IConcept#setSubType(SubTypeEnum subType)
	*/
	@Override
	public void setSubType(SubTypeEnum subType) {
		Checker.checkNull(subType, "subType");
		this.subType = subType;
	}

	/* (non-Javadoc)
	 * @see IConcept#getOrigin()
	*/
	@Override
	public String getOrigin() {
		return this.origin;
	}

	/* (non-Javadoc)
	 * @see IConcept#setOrigin(String origin)
	*/
	@Override
	public void setOrigin(String origin) {
		Checker.checkNull(origin, "origin");
		this.origin = origin;
	}

	/* (non-Javadoc)
	 * @see IConcept#getDataType()
	*/
	@Override
	public DataTypeEnum getDataType() {
		return this.dataType;
	}

	/* (non-Javadoc)
	 * @see IConcept#setDataType(DataTypeEnum dataType)
	*/
	@Override
	public void setDataType(DataTypeEnum dataType) {
		Checker.checkNull(dataType, "dataType");
		this.dataType = dataType;
	}

	/* (non-Javadoc)
	 * @see IConcept#getValueSpace()
	*/
	@Override
	public IValueSpace getValueSpace() {
		return this.valueSpace;
	}

	/* (non-Javadoc)
	 * @see IConcept#setValueSpace(IValueSpace valueSpace)
	*/
	@Override
	public void setValueSpace(IValueSpace valueSpace) {
		//Checker.checkNull(valueSpace, "valueSpace");
		this.valueSpace = valueSpace;
	}

	/* (non-Javadoc)
	 * @see IConcept#getAuthors()
	*/
	@Override
	public List<String> getAuthors() {
		return this.authors;
	}

	/* (non-Javadoc)
	 * @see IConcept#setAuthors(List<String> authors)
	*/
	@Override
	public void setAuthors(List<String> authors) {
		Checker.checkNull(authors, "authors");
		this.authors = authors;
	}
	
	/* (non-Javadoc)
	 * @see IConcept#getOwners()
	*/
	@Override
	public List<Integer> getOwners(){
		return this.owners;
	}
	
	/* (non-Javadoc)
	 * @see IConcept#setOwners(List<Integer> owners)
	*/
	@Override
	public void setOwners(List<Integer> owners){
		Checker.checkNull(owners, "owners");
		this.owners = owners;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IConcept#getConceptsWhichRefineThisConcept()
	 */
	@Override
	public List<String> getConceptsWhichRefineThisConcept(){
		return this.conceptsWhichRefineThisConcept;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IConcept#setConceptsWhichRefineThisConcept(java.util.List)
	 */
	@Override
	public void setConceptsWhichRefineThisConcept(List<String> conceptsWhichRefineThisConcept){
		Checker.checkNull(conceptsWhichRefineThisConcept, "conceptsWhichRefineThisConcept");
		this.conceptsWhichRefineThisConcept = conceptsWhichRefineThisConcept;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IConcept#getConceptsWhichAreRefinedByThisConcept()
	 */
	@Override
	public List<String> getConceptsWhichAreRefinedByThisConcept(){
		return this.conceptsWhichAreRefinedByThisConcept;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IConcept#setConceptsWhichAreRefinedByThisConcept(java.util.List)
	 */
	@Override
	public void setConceptsWhichAreRefinedByThisConcept(List<String> conceptsWhichAreRefinedByThisConcept){
		Checker.checkNull(conceptsWhichAreRefinedByThisConcept, "conceptsWhichAreRefinedByThisConcept");
		this.conceptsWhichAreRefinedByThisConcept = conceptsWhichAreRefinedByThisConcept;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IConcept#getConceptsWhichTransformThisConcept()
	 */
	@Override
	public List<String> getConceptsWhichTransformThisConcept(){
		return this.conceptsWhichTransformThisConcept;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IConcept#setConceptsWhichTransformThisConcept(java.util.List)
	 */
	@Override
	public void setConceptsWhichTransformThisConcept(List<String> conceptsWhichTransformThisConcept){
		Checker.checkNull(conceptsWhichTransformThisConcept, "conceptsWhichTransformThisConcept");
		this.conceptsWhichTransformThisConcept = conceptsWhichTransformThisConcept;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IConcept#getConceptsWhichAreTransformedByThisConcept()
	 */
	@Override
	public List<String> getConceptsWhichAreTransformedByThisConcept(){
		return this.conceptsWhichAreTransformedByThisConcept;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IConcept#setConceptsWhichAreTransformedByThisConcept(java.util.List)
	 */
	@Override
	public void setConceptsWhichAreTransformedByThisConcept(List<String> conceptsWhichAreTransformedByThisConcept){
		Checker.checkNull(conceptsWhichAreTransformedByThisConcept, "conceptsWhichAreTransformedByThisConcept");
		this.conceptsWhichAreTransformedByThisConcept = conceptsWhichAreTransformedByThisConcept;
	}
	
	/* (non-Javadoc)
	 * @see IConcept#getUpdatedInMilliseconds()
	*/
	@Override
	public long getUpdatedInMilliseconds(){
		return this.updatedInMilliseconds;
	}
	
	/* (non-Javadoc)
	 * @see IConcept#setUpdatedInMilliseconds(long updatedInMilliseconds)
	*/	
	@Override
	public void setUpdatedInMilliseconds(long updatedInMilliseconds){
		Checker.checkLongGreaterEquals(updatedInMilliseconds, "updatedInMilliseconds", -1);
		this.updatedInMilliseconds = updatedInMilliseconds;
	}

	/* (non-Javadoc)
	 * @see IConcept#getGroupAccessRights()
	*/
	@Override
	public List<IGroupAccessRight> getGroupAccessRights() {
		return this.groupAccessRights;
	}

	/* (non-Javadoc)
	 * @see IConcept#setGroupAccessRights(List<IGroupAccessRight> groupAccessRights)
	*/
	@Override
	public void setGroupAccessRights(List<IGroupAccessRight> groupAccessRights) {
		Checker.checkNull(groupAccessRights, "groupAccessRights");
		this.groupAccessRights = groupAccessRights;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IConcept#hasUserReadRight(int, java.util.List)
	 */
	@Override
	public boolean hasUserReadRight(IUser user, List<IGroup> usersGroups) {
	    return this.hasUserRight(user, usersGroups, IGroupAccessRight.READ_RIGHT);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IConcept#hasUserUpdateRight(int, java.util.List)
	 */
	@Override
	public boolean hasUserUpdateRight(IUser user, List<IGroup> usersGroups) {
	    return this.hasUserRight(user, usersGroups, IGroupAccessRight.UPDATE_RIGHT);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IConcept#hasUserDeleteRight(int, java.util.List)
	 */
	@Override
	public boolean hasUserDeleteRight(IUser user, List<IGroup> usersGroups) {
	    return this.hasUserRight(user, usersGroups, IGroupAccessRight.DELETE_RIGHT);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IConcept#hasUserChangeRightsRight(int, java.util.List)
	 */
	@Override
	public boolean hasUserChangeRightsRight(IUser user, List<IGroup> usersGroups) {
	    return this.hasUserRight(user, usersGroups, IGroupAccessRight.CHANGE_RIGHTS_RIGHT);
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

	private boolean hasUserRight(IUser user, List<IGroup> usersGroups, String right){
        Checker.checkNull(user, "user");
        Checker.checkNull(usersGroups, "usersGroups");
        Checker.checkNullAndEmptiness(right, "right");
        boolean hasRight = false;
        if(user.isSuperAdmin() || this.owners.contains(user.getId())){
            hasRight = true;
        }else{
            for(IGroup group : usersGroups){
                for(IGroupAccessRight groupAccessRight : this.groupAccessRights){
                    if(groupAccessRight.getGroupId() == group.getId() && this.hasGroupAccessRight(groupAccessRight, right)){
                        hasRight = true;
                    }
                }
            }    
        }
        return hasRight;
    }
    
    private boolean hasGroupAccessRight(IGroupAccessRight groupAccessRight, String right){
        boolean result = false;
        switch(right){
        case IGroupAccessRight.READ_RIGHT:
            result = groupAccessRight.hasReadRight();
            break;
        case IGroupAccessRight.UPDATE_RIGHT:
            result = groupAccessRight.hasUpdateRight();
            break;
        case IGroupAccessRight.DELETE_RIGHT:
            result = groupAccessRight.hasDeleteRight();
            break;
        case IGroupAccessRight.CHANGE_RIGHTS_RIGHT:
            result = groupAccessRight.hasChangeRightsRight();
            break;
        default:
            throw new RuntimeException("The right " + right + " is unknown!");
        }
        return result;
    }

    


	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
