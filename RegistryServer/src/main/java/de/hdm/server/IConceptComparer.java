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
package de.hdm.server;

import java.util.List;

import de.hdm.datatypes.GroupAccessRight;
import de.hdm.datatypes.IConcept;
import de.hdm.datatypes.IGroupAccessRight;

/**
 * This is the interface to compare concepts.
 * 
 * @author Tobias Ableitner
 *
 */
public interface IConceptComparer {

	/**
	 * Compares two concepts and returns the differences between those two as JSON string. The registry server uses this
	 * method to store changes of a concept on account of updates in the log.
	 * 
	 * @param oldConcept
	 *            the old concept. It must not be null. Otherwise an {@link IllegalArgumentException} will be thrown.
	 * @param newConcept
	 *            the new concept. It must not be null. Otherwise an {@link IllegalArgumentException} will be thrown.
	 * @return JSON string containing the differences between the old and the new concept.
	 * @throws {@link
	 *             IllegalArgumentException} if one or both concepts are null.
	 */
	public String compareConcepts(IConcept oldConcept, IConcept newConcept);

	/**
	 * Compares to list of group access rights whether they are equal or not. The order of the group access rights in
	 * the lists is ignored. When two group access rights are equal is defined in
	 * {@link GroupAccessRight#equals(Object)}.
	 * 
	 * @param oldValues
	 *            list with group access rights. The list can be null or empty.
	 * @param newValues
	 *            list with group access rights. The list can be null or empty.
	 * @return true if the group access rights in the lists oldValues and newValues are not equal and false if equal
	 */
	public boolean hasGroupAccessRightsChanged(List<IGroupAccessRight> oldValues, List<IGroupAccessRight> newValues);

}
