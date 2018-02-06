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

/**
 * This is the interface for a concept filter. A concept filter can be used to define criteria for concept selection.
 * 
 * @author Tobias Ableiter
 *
 */
public interface IConceptFilter {

	/**
	 * Getter for the select criterion updated. If this criterion is not set, null will be returned. This criterion
	 * defines, how old the last update or the date and time of creation must maximal be, that a concept is selected.
	 * 
	 * @return date time stamp in UTC or null
	 */
	public GregorianCalendar getUpdated();

	/**
	 * Getter for the select criterion types. If it is this criterion is not set, null will be returned. This criterion
	 * defines, of which types the concepts have to be, that will be returned.
	 * 
	 * @return list with {@link TypeEnum} or null
	 */
	public List<TypeEnum> getTypes();

	/**
	 * Getter for the select criterion limit. If this criterion is not set, -1 will be returned. This criterion limits
	 * the number of selected concepts.
	 * 
	 * @return limit as value greater equals 0 or -1 if it is not set
	 */
	public int getLimit();

	/**
	 * Getter for the select criterion offset. If this criterion is not set, -1 will be returned. This criterion defines
	 * how many of the firstly selected concepts will be skipped.
	 * 
	 * @return offset as value greater equals 0 or -1 if it is not set
	 */
	public int getOffset();

	/**
	 * Check whether at least one of the criteria is set. When no criterion is set, the concept filter is empty.
	 * 
	 * @return true if no criterion is set and false, if at least one of the criteria is set
	 */
	public boolean isConceptFilterEmpty();

}
