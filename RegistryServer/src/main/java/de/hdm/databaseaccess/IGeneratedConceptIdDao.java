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

/**
 * This interface provides data access methods for the generated concept id.
 * 
 * @author Tobias Ableitner
 *
 */
public interface IGeneratedConceptIdDao extends IDao {

	/**
	 * Selects the next counter value which should be usable for a concept id generation and increments the counter.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @return value of the counter before it was incremented
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public int selectNumberAndIncrementIt(IUnitOfWork unitOfWork) throws DataAccessException;

}
