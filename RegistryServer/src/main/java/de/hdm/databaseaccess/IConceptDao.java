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

import java.util.List;

import de.hdm.datatypes.IConcept;

/**
 * This interface provides data access methods for concepts.
 * 
 * @author Tobias Ableitner
 *
 */
public interface IConceptDao extends IDao {

	/**
	 * Inserts a new concept into the database. This method does not check, whether a concept with the concept id of the
	 * concept, which should be inserted, already exists. This has to be done before calling this method via
	 * {@link #existsConcept(IUnitOfWork, String)}.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param concept
	 *            the concept which should be inserted. It must not be null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public void insertConcept(IUnitOfWork unitOfWork, IConcept concept) throws DataAccessException;

	/**
	 * Updates a concept, which is already stored in the database. This method does not check, whether an user has
	 * update right for the concept, which should be updated. This has to be done before calling this method. It also
	 * does not check, whether the concept, which should be updated, already exists in the database. This has to be done
	 * before calling this method via {@link #existsConcept(IUnitOfWork, String)}.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param concept
	 *            the concept which should be updated. It must not be null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public void updateConcept(IUnitOfWork unitOfWork, IConcept concept) throws DataAccessException;

	/**
	 * Deletes a concept from database. This method does not check, whether an user has delete right for the concept,
	 * which should be deleted. This has to be done before calling this method. It also does not check, whether a
	 * concept with the concept id of the concept, which should be deleted, exists. This has to be done before calling
	 * this method via {@link #existsConcept(IUnitOfWork, String)}.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param conceptId
	 *            the id of the concept which should be deleted. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public void deleteConcept(IUnitOfWork unitOfWork, String conceptId) throws DataAccessException;

	/**
	 * Selects a concept from database. This method does not check, whether an user has read right for the requested
	 * concept. This has to be done before calling this method.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param conceptId
	 *            the id of the concept which should be selected. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @return the concept or null, if there is no concept with the concept id, which is defined by the parameter
	 *         conceptId
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public IConcept selectConcept(IUnitOfWork unitOfWork, String conceptId) throws DataAccessException;

	/**
	 * Selects all concepts from database, for which the user, defined by the parameter userId, has read rights.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userId
	 *            id of the user. It must be greater equals 1. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @return list of concepts. If there are no concepts or the user has read right to no concept, an empty list will
	 *         be returned.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public List<IConcept> selectAllConcepts(IUnitOfWork unitOfWork, int userId) throws DataAccessException;
	
	/**
     * Selects all public concepts from database. Public means, that the anonymous users group has read right for the concept.
     * 
     * @param unitOfWork
     *            the unit of work to which the call of this method should belong or null, if this method call is not
     *            part of an unit of work.
     * @return list of concepts. If there are no public concepts, an empty list will
     *         be returned.
     * @throws DataAccessException
     *             if a problem with the database occurs.
     */
    public List<IConcept> selectAllPublicConcepts(IUnitOfWork unitOfWork) throws DataAccessException;
	
	/**
	 * Selects all concepts from database, where the user, defined by the parameter userId, is owner.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userId
	 *            id of the user. It must be greater equals 1. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @return list of concepts. If there exist no concepts where the user is owner an empty list will
	 *         be returned.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public List<IConcept> selectAllConceptsOfUser(IUnitOfWork unitOfWork, int userId) throws DataAccessException;
	
	/**
	 * Selects all concepts from database, where the user, defined by the parameter userId, is the single owner.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userId
	 *            id of the user. It must be greater equals 1. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @return list of concepts. If there are no concepts where the user is the single owner an empty list will
	 *         be returned.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public List<IConcept> selectAllSingleOwnerConceptsOfUser(IUnitOfWork unitOfWork, int userId) throws DataAccessException;

	/**
	 * Selects all concepts from database, for which the user, defined by the parameter userId, has read rights and
	 * which matches the concept filter.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userId
	 *            id of the user. It must be greater equals 1. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @param conceptFilter
	 *            the concept filter which defines the criteria
	 * @return list of concepts. If there are no concepts, the user has read right to no concept or no of the concepts,
	 *         for which the user has read right, matches the concept filter, an empty list will be returned.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public List<IConcept> selectConcepts(IUnitOfWork unitOfWork, int userId, IConceptFilter conceptFilter)
			throws DataAccessException;
	
	/**
	 * Checks, whether there exists already a concept in the database with the same concept id as defined by the
	 * parameter conceptId.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param conceptId
	 *            the id of the concept which should be deleted. It must not be null or empty. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @return true if a concept with the id, which is defined by the parameter conceptId, exists and false if not
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public boolean existsConcept(IUnitOfWork unitOfWork, String conceptId) throws DataAccessException;

	/**
	 * Selects the total number of stored concepts in the database.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @return total number of concepts
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public int selectTotalNumberOfConcepts(IUnitOfWork unitOfWork) throws DataAccessException;

	/**
	 * Selects the total number of stored concepts in the database for which the user, defined by the parameter userId,
	 * has read rights.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userId
	 *            id of the user. It must be greater equals 0. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @return total number of concepts for the user has read right
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public int selectTotalNumberOfConceptsForWhichUserHasReadRight(IUnitOfWork unitOfWork, int userId)
			throws DataAccessException;
	
	/**
	 * Searches concepts, which match the search term defined by the parameter searchTerm. A matching concept will only
	 * be returned, if the user, who is defined by the parameter used id, has a read permission for it. The caller of
	 * this method can decide, whether the concept ids, names or both one should be compared with the search term for
	 * matches. If he decides for both this means, that either a concept's id or name and not both has to match for a
	 * match. But the latter is also allowed.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param userId
	 *            id of the user. It must be greater equals 0. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @param searchTerm
	 *            the search term. It must not be null or empty. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @param searchInIds
	 *            true if matches should be searched in the concepts IDs and false if not. Note, that at least the
	 *            parameter searchInIds or searchInNames has to be true. Otherwise an {@link IllegalArgumentException}
	 *            will be thrown.
	 * @param searchInNames
	 *            true if matches should be searched in the concepts names and false if not.
	 * @return list of matching concepts. If no concepts match or the user has no read right for the matching concepts,
	 *         an empty list will be returned.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public List<IConcept> searchConcepts(IUnitOfWork unitOfWork, int userId, String searchTerm, boolean searchInIds, boolean searchInNames) throws DataAccessException;

	/**
	 * Selects all concepts from database, which refine the concept, defined by the parameter conceptId.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param conceptId
	 *            the id of the concept, for which the refining concepts should be selected. It must not be null or empty. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @return list of refining concepts. If there are no refining concepts an empty list will
	 *         be returned.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public List<IConcept> selectConceptsWhichRefineConcept(IUnitOfWork unitOfWork, String conceptId) throws DataAccessException;
	
	/**
	 * Selects all concepts from database, which transform the concept, defined by the parameter conceptId.
	 * 
	 * @param unitOfWork
	 *            the unit of work to which the call of this method should belong or null, if this method call is not
	 *            part of an unit of work.
	 * @param conceptId
	 *            the id of the concept, for which the transforming concepts should be selected. It must not be null or empty. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 * @return list of transforming concepts. If there are no transforming concepts an empty list will
	 *         be returned.
	 * @throws DataAccessException
	 *             if a problem with the database occurs.
	 */
	public List<IConcept> selectConceptsWhichTransformConcept(IUnitOfWork unitOfWork, String conceptId) throws DataAccessException;
	
}
