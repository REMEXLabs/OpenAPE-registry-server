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
package de.hdm.databaseaccess;

import java.util.GregorianCalendar;
import java.util.List;

import de.hdm.datatypes.TypeEnum;
import de.hdm.helpers.Checker;

/**
 * This class defines a concept filter. A concept filter can be used to define criteria for concept selection.
 * 
 * This class is not thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class ConceptFilter implements IConceptFilter {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * This criterion defines, how old the last update or the date and time of creation must maximal be, that a concept
	 * is selected.
	 */
	private GregorianCalendar updated = null;

	/**
	 * This criterion defines, of which types the concepts have to be, that will be returned.
	 */
	private List<TypeEnum> types = null;

	/**
	 * This criterion limits the number of selected concepts.
	 */
	private int limit = -1;

	/**
	 * This criterion defines how many of the firstly selected concepts will be skipped.
	 */
	private int offset = -1;




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Empty Default constructor. It creates an empty concept filter. No one of the criteria will be set.
	 */
	public ConceptFilter() {

	}

	/**
	 * Creates a new criteria.
	 * 
	 * @param updated
	 *            defines, how old the last update or the date and time of creation must maximal be, that a concept is
	 *            selected. It can be null if it should not be set.
	 * @param types
	 *            defines, of which types the concepts have to be, that will be returned. It can be null if it should
	 *            not be set.
	 * @param limit
	 *            limits the number of selected concepts. If it should be set, it must be an integer greater equals 0.
	 *            It can be -1, if it should not be set. If the value is smaller -1 an {@link IllegalArgumentException}
	 *            will be thrown.
	 * @param offset
	 *            defines how many of the firstly selected concepts will be skipped. It can be -1, if it should not be
	 *            set. If the value is smaller -1 an {@link IllegalArgumentException} will be thrown.
	 */
	public ConceptFilter(GregorianCalendar updated, List<TypeEnum> types, int limit, int offset) {
		this.setUpdated(updated);
		this.setTypes(types);
		this.setLimit(limit);
		this.setOffset(offset);
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// getters and setters
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	private void setUpdated(GregorianCalendar updated) {
		this.updated = updated;
	}

	private void setTypes(List<TypeEnum> types) {
		this.types = types;
	}

	private void setLimit(int limit) {
		Checker.checkIntegerGreaterEquals(limit, "limit", -1);
		this.limit = limit;
	}

	private void setOffset(int offset) {
		Checker.checkIntegerGreaterEquals(offset, "offset", -1);
		this.offset = offset;
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
	 * @see de.hdm.databaseaccess.IConceptFilter#getUpdated()
	 */
	@Override
	public GregorianCalendar getUpdated() {
		return this.updated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.IConceptFilter#getTypes()
	 */
	@Override
	public List<TypeEnum> getTypes() {
		return this.types;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.IConceptFilter#getLimit()
	 */
	@Override
	public int getLimit() {
		return this.limit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.IConceptFilter#getOffset()
	 */
	@Override
	public int getOffset() {
		return this.offset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.databaseaccess.IConceptFilter#isConceptFilterEmpty()
	 */
	@Override
	public boolean isConceptFilterEmpty() {
		boolean empty = true;
		if (this.updated != null) {
			empty = false;
		} else if (types != null && types.isEmpty() == false) {
			empty = false;
		} else if (this.limit != -1) {
			empty = false;
		} else if (this.offset != -1) {
			empty = false;
		}
		return empty;
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
