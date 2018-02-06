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
package de.hdm.databaseaccess.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import de.hdm.databaseaccess.DaoFactory;
import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IConceptDao;
import de.hdm.databaseaccess.IConceptFilter;
import de.hdm.databaseaccess.IGroupAccessRightDao;
import de.hdm.databaseaccess.ILogDao;
import de.hdm.databaseaccess.IOwnershipDao;
import de.hdm.databaseaccess.IUnitOfWork;
import de.hdm.databaseaccess.UnitOfWorkStateEnum;
import de.hdm.datatypes.Concept;
import de.hdm.datatypes.DataTypeEnum;
import de.hdm.datatypes.Definition;
import de.hdm.datatypes.Example;
import de.hdm.datatypes.IConcept;
import de.hdm.datatypes.IContent;
import de.hdm.datatypes.Name;
import de.hdm.datatypes.Note;
import de.hdm.datatypes.SubTypeEnum;
import de.hdm.datatypes.TypeEnum;
import de.hdm.datatypes.ValueSpace;
import de.hdm.helpers.Checker;

/**
 * This class implements the database access for concepts on MySql databases.
 * 
 * Note: It is important to use transaction for inserting, updating and deleting concepts, because a concept is
 * distributed about multiple tables in the database. Thus, if a method call is not part of an unit of work, we have to
 * use a unit of work or transaction inside those methods. Concepts are only deleted from the concept table by this
 * class, because we use on delete cascade for the entities in the other tables, which belong to a concept and should be
 * deleted, if a concept is deleted.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class MySqlConceptDao extends MySqlDao implements IConceptDao {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Select part of SQL queries which select concepts.
	 */
	private static final String SELECT_CONCEPT = "SELECT id, type, subType, origin, dataType, valueSpace, authors FROM Concept";

	/**
	 * Data access object for the ownerships.
	 */
	private IOwnershipDao ownershipDao;

	
	
	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Empty default constructor.
	 */
	public MySqlConceptDao(){
		this.ownershipDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createOwnershipDao();
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

	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IConceptDao#insertConcept(de.hdm.databaseaccess.IUnitOfWork, de.hdm.datatypes.IConcept)
	 */
	@Override
	public void insertConcept(IUnitOfWork unitOfWork, IConcept concept) throws DataAccessException {
		Checker.checkNull(concept, "concept");
		if(unitOfWork == null){
			this.insertConceptNotAsPartOfATransaction(concept);
		}else{
			try {
				this.executeInsertConcept(unitOfWork, concept);
			} catch (Exception e) {
				e.printStackTrace();
				throw new DataAccessException("The concept " + concept.getId() + " could not be created on account of an error!", e);
			}
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IConceptDao#updateConcept(de.hdm.databaseaccess.IUnitOfWork, de.hdm.datatypes.IConcept)
	 */
	@Override
	public void updateConcept(IUnitOfWork unitOfWork, IConcept concept) throws DataAccessException {
		Checker.checkNull(concept, "concept");
		if(unitOfWork == null){
			this.updateConceptNotAsPartOfAnTransaction(concept);
		}else{
			try {
				this.executeConceptUpdate(unitOfWork, concept);
			} catch (Exception e) {
				e.printStackTrace();
				throw new DataAccessException("The concept " + concept.getId() + " could not be updated on account of an error!", e);
			}	
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IConceptDao#deleteConcept(de.hdm.databaseaccess.IUnitOfWork, java.lang.String)
	 */
	@Override
	public void deleteConcept(IUnitOfWork unitOfWork, String conceptId) throws DataAccessException {
		Checker.checkNullAndEmptiness(conceptId, "conceptId");
		Connection connection = this.getConnection(unitOfWork);
		try {
			this.executeConceptDeletion(connection, conceptId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("The concept " + conceptId + " could not be deleted on account of an error!", e);
		}	
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IConceptDao#selectConcept(de.hdm.databaseaccess.IUnitOfWork, java.lang.String)
	 */
	@Override
	public IConcept selectConcept(IUnitOfWork unitOfWork, String conceptId) throws DataAccessException {
		Checker.checkNullAndEmptiness(conceptId, "conceptId");
		String sql = SELECT_CONCEPT + " WHERE id = '" + conceptId + "'";
		List<IConcept> concepts = this.executeConceptsSelection(unitOfWork, sql);
		if(concepts.size() == 0){
			throw new DataAccessException("No concept was found with the conceptId " + conceptId + "!", null);
		}else if(concepts.size() > 1){
			throw new DataAccessException("More than one concept was found with the conceptId " + conceptId + "!", null);
		}
		return concepts.get(0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IConceptDao#selectAllConcepts(de.hdm.databaseaccess.IUnitOfWork, int)
	 */
	@Override
	public List<IConcept> selectAllConcepts(IUnitOfWork unitOfWork, int userId) throws DataAccessException {
		Checker.checkIntegerGreaterEquals(userId, "userId", 1);
		String sql = this.createSqlForSelectConcepts(userId, null);
		return this.executeConceptsSelection(unitOfWork, sql);	
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IConceptDao#selectAllConceptsOfUser(de.hdm.databaseaccess.IUnitOfWork, int)
	 */
	@Override
	public List<IConcept> selectAllConceptsOfUser(IUnitOfWork unitOfWork, int userId) throws DataAccessException {
		Checker.checkIntegerGreaterEquals(userId, "userId", 1);
		String sql = SELECT_CONCEPT;
		sql += " WHERE id IN (SELECT conceptId FROM Ownership WHERE userId = " + userId + ")";
		return this.executeConceptsSelection(unitOfWork, sql);	
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IConceptDao#selectAllSingleOwnerConceptsOfUser(de.hdm.databaseaccess.IUnitOfWork, int)
	 */
	@Override
	public List<IConcept> selectAllSingleOwnerConceptsOfUser(IUnitOfWork unitOfWork, int userId) throws DataAccessException {
		Checker.checkIntegerGreaterEquals(userId, "userId", 1);
		String sql = SELECT_CONCEPT;
		sql += " WHERE id IN (SELECT conceptId FROM Ownership WHERE userId = " + userId + ") AND ((SELECT Count(conceptId) FROM Ownership WHERE conceptId = id) <= 1)";
		return this.executeConceptsSelection(unitOfWork, sql);	
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IConceptDao#selectConcepts(de.hdm.databaseaccess.IUnitOfWork, int, de.hdm.databaseaccess.IConceptFilter)
	 */
	@Override
	public List<IConcept> selectConcepts(IUnitOfWork unitOfWork, int userId,  IConceptFilter conceptFilter) throws DataAccessException {
		Checker.checkIntegerGreaterEquals(userId, "userId", 1);
		Checker.checkNull(conceptFilter, "conceptFilter");
		String sql = createSqlForSelectConcepts(userId, conceptFilter);
		return this.executeConceptsSelection(unitOfWork, sql);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IConceptDao#existsConcept(de.hdm.databaseaccess.IUnitOfWork, java.lang.String)
	 */
	@Override
	public boolean existsConcept(IUnitOfWork unitOfWork, String conceptId) throws DataAccessException {
		Checker.checkNullAndEmptiness(conceptId, "conceptId");
		boolean exists = false;
		Connection connection = null;
		try {
			connection = this.getConnection(unitOfWork);
			PreparedStatement existsConceptStatement = connection.prepareStatement("SELECT id FROM Concept WHERE id = ?");
			existsConceptStatement.setString(1, conceptId);
			ResultSet resultSet = existsConceptStatement.executeQuery();
			while(resultSet.next()){
				exists = true;
			}
			resultSet.close();
			existsConceptStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("It could not be checked, whether the concept id " + conceptId + "already exists or not on account of an error with the database!", e);
		}finally{
			this.closeConnection(unitOfWork, connection);
		}
		
		return exists;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IConceptDao#selectTotalNumberOfConcepts(de.hdm.databaseaccess.IUnitOfWork)
	 */
	@Override
	public int selectTotalNumberOfConcepts(IUnitOfWork unitOfWork) throws DataAccessException {
		String sql = "SELECT COUNT(*) FROM Concept";
		return this.executeSelectTotalNumberOfConcepts(unitOfWork, sql);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IConceptDao#selectTotalNumberOfConceptsForWhichUserHasReadRight(de.hdm.databaseaccess.IUnitOfWork, int)
	 */
	@Override
	public int selectTotalNumberOfConceptsForWhichUserHasReadRight(IUnitOfWork unitOfWork, int userId) throws DataAccessException{
		Checker.checkUserId(userId);
		String sql = "SELECT COUNT(*) FROM Concept WHERE " + this.createConditionReadAccessRightsForConcepts(userId);
		return this.executeSelectTotalNumberOfConcepts(unitOfWork, sql);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IConceptDao#searchConcepts(de.hdm.databaseaccess.IUnitOfWork, int, java.lang.String, boolean, boolean)
	 */
	@Override
	public List<IConcept> searchConcepts(IUnitOfWork unitOfWork, int userId, String searchTerm, boolean searchInIds, boolean searchInNames) throws DataAccessException {
		Checker.checkUserId(userId);
		Checker.checkNullAndEmptiness(searchTerm, "searchTerm");
		if(searchInIds == false && searchInNames == false){
			throw new IllegalArgumentException("At least one of the two parameters searchInIds and seachInNames must be true!");
		}
		String sql = SELECT_CONCEPT;
		sql += " WHERE (";
		sql += this.createConditionReadAccessRightsForConcepts(userId);
		sql += ") AND ";
		if(searchInIds){
			sql += "id LIKE '%" + searchTerm + "%'";
		}
		if(searchInNames){
			if(searchInIds){
				sql += " OR";
			}
			sql += " id IN (Select conceptId FROM ConceptName WHERE `name` LIKE '%" + searchTerm + "%')";
		}
		return this.executeConceptsSelection(unitOfWork, sql);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IConceptDao#selectConceptsWhichRefineConcept(de.hdm.databaseaccess.IUnitOfWork, java.lang.String)
	 */
	@Override
	public List<IConcept> selectConceptsWhichRefineConcept(IUnitOfWork unitOfWork, String conceptId) throws DataAccessException {
		Checker.checkNullAndEmptiness(conceptId, "conceptId");
		String sql = SELECT_CONCEPT;
		sql += " WHERE id IN (SELECT conceptId FROM Refinement WHERE refinesConceptId = '" + conceptId + "')";
		return this.executeConceptsSelection(unitOfWork, sql);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.IConceptDao#selectConceptsWhichTransformConcept(de.hdm.databaseaccess.IUnitOfWork, java.lang.String)
	 */
	@Override
	public List<IConcept> selectConceptsWhichTransformConcept(IUnitOfWork unitOfWork, String conceptId) throws DataAccessException {
		Checker.checkNullAndEmptiness(conceptId, "conceptId");
		String sql = SELECT_CONCEPT;
		sql += " WHERE id IN (SELECT conceptId FROM Transformation WHERE transformsConceptId = '" + conceptId + "')";
		return this.executeConceptsSelection(unitOfWork, sql);
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
	
	// for insertions
	
	private void insertConceptNotAsPartOfATransaction(IConcept concept) throws DataAccessException {
		IUnitOfWork unitOfWork = this.createUnitOfWork();
		try {
			unitOfWork.start();
			this.executeInsertConcept(unitOfWork, concept);
		} catch (DataAccessException e) {
			e.printStackTrace();
			unitOfWork.abort();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			unitOfWork.abort();
			throw new DataAccessException(
					"The concept " + concept.getId() + " could not be created on account of an error!", e);
		} finally {
			if (unitOfWork != null && unitOfWork.getState() == UnitOfWorkStateEnum.STARTED) {
				unitOfWork.finish();
			}
		}
	}
	
	private void executeInsertConcept(IUnitOfWork unitOfWork, IConcept concept) throws SQLException, DataAccessException{
		Connection connection = this.getConnection(unitOfWork);
		PreparedStatement insertConceptStatement = connection.prepareStatement("INSERT INTO Concept (id, type, subtype, origin, dataType, valueSpace, authors) VALUES(?, ?, ?, ?, ?, ?, ?)");
		insertConceptStatement.setString(1, concept.getId());
		insertConceptStatement.setString(2, concept.getType().toString());
		insertConceptStatement.setString(3, concept.getSubType().toString());
		insertConceptStatement.setString(4, concept.getOrigin());
		insertConceptStatement.setString(5, concept.getDataType().toString());
		if(concept.getValueSpace() != null){
			insertConceptStatement.setString(6, concept.getValueSpace().getJsonSchema());
		}else{
			insertConceptStatement.setString(6, null);
		}
		insertConceptStatement.setString(7, this.getAuthorsAsString(concept.getAuthors()));
		insertConceptStatement.executeUpdate();
		insertConceptStatement.close();
		
		this.insertConceptNames(connection, concept);
		this.insertConceptDefinitions(connection, concept);
		this.insertNotes(connection, concept);
		this.insertExamples(connection, concept);
		this.insertTransformations(connection, concept);
		this.insertRefinements(connection, concept);
		this.insertOwnerships(unitOfWork, concept);
		this.insertGroupAccessRights(unitOfWork, concept);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void insertConceptNames(Connection connection, IConcept concept) throws SQLException{
		if(concept.getNames() != null){
			String sql = "INSERT INTO ConceptName (conceptId, languageCode, name) VALUES(?, ?, ?)";
			this.insertContents(connection, concept.getId(), (List)concept.getNames(), sql);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void insertConceptDefinitions(Connection connection, IConcept concept) throws SQLException{
		if(concept.getDefinitions() != null){
			String sql = "INSERT INTO ConceptDefinition (conceptId, languageCode, description) VALUES(?, ?, ?)";
			this.insertContents(connection, concept.getId(), (List)concept.getDefinitions(), sql);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void insertNotes(Connection connection, IConcept concept) throws SQLException{
		if(concept.getNotes() != null){
			String sql = "INSERT INTO Note (conceptId, languageCode, content) VALUES (?, ?, ?)";
			this.insertContents(connection, concept.getId(), (List)concept.getNotes(), sql);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void insertExamples(Connection connection, IConcept concept) throws SQLException{
		if(concept.getExamples() != null){
			String sql = "INSERT INTO Example (conceptId, languageCode, content) VALUES (?, ?, ?)";
			this.insertContents(connection, concept.getId(), (List)concept.getExamples(), sql);
		}
	}
	
	private void insertContents(Connection connection, String conceptId, List<IContent> contents, String sql) throws SQLException{
		for(IContent content : contents){
			PreparedStatement insertNoteOrExampleStatement = connection.prepareStatement(sql);
			insertNoteOrExampleStatement.setString(1, conceptId);
			insertNoteOrExampleStatement.setString(2, content.getLanguageCode());
			insertNoteOrExampleStatement.setString(3, content.getContent());
			insertNoteOrExampleStatement.executeUpdate();
		}
	}
	
	private void insertTransformations(Connection connection, IConcept concept) throws SQLException{
		if(concept.getConceptsWhichAreTransformedByThisConcept() != null){
			String sql = "INSERT INTO Transformation (conceptId, transformsConceptId) VALUES (?, ?)";
			this.insertTransformationsOrRefinements(connection, concept.getId(), concept.getConceptsWhichAreTransformedByThisConcept(), sql);
		}
	}
	
	private void insertRefinements(Connection connection, IConcept concept) throws SQLException{
		if(concept.getConceptsWhichAreRefinedByThisConcept() != null){
			String sql = "INSERT INTO Refinement (conceptId, refinesConceptId) VALUES (?, ?)";
			this.insertTransformationsOrRefinements(connection, concept.getId(), concept.getConceptsWhichAreRefinedByThisConcept(), sql);
		}
	}
	
	private void insertTransformationsOrRefinements(Connection connection, String conceptId, List<String> idsOfOtherConcepts, String sql) throws SQLException{
		for(String n : idsOfOtherConcepts){
			PreparedStatement insertTransformationOrRefinementStatement = connection.prepareStatement(sql);
			insertTransformationOrRefinementStatement.setString(1, conceptId);
			insertTransformationOrRefinementStatement.setString(2, n);
			insertTransformationOrRefinementStatement.executeUpdate();
			insertTransformationOrRefinementStatement.close();
		}
	}
	
	private void insertOwnerships(IUnitOfWork unitOfWork, IConcept concept) throws DataAccessException{
		if(concept.getOwners() != null){
			for(int userId : concept.getOwners()){
				this.ownershipDao.insertOwnership(unitOfWork, userId, concept.getId());
			}
		}
	}
	
	private void insertGroupAccessRights(IUnitOfWork unitOfWork, IConcept concept) throws DataAccessException{
		IGroupAccessRightDao groupAccessRightDao = new MySqlGroupAccessRightDao();
		if(concept.getGroupAccessRights() != null && concept.getGroupAccessRights().isEmpty() == false){
			groupAccessRightDao.insertGroupAccessRights(unitOfWork, concept.getGroupAccessRights());
		}
	}
	
	private String getAuthorsAsString(List<String> authors){
		String authorsAsString = "";
		for(int i = 0; i < authors.size(); i++){
			authorsAsString += authors.get(i);
			if(i < authors.size() -1){
				authorsAsString += "; ";
			}
		}
		return authorsAsString;
	}
	
	// for updates
	
	private void updateConceptNotAsPartOfAnTransaction(IConcept concept) throws DataAccessException{
		IUnitOfWork unitOfWork = this.createUnitOfWork();
		try {
			this.executeConceptUpdate(unitOfWork, concept);
		} catch (Exception e) {
			e.printStackTrace();
			unitOfWork.abort();
			throw new DataAccessException("The concept " + concept.getId() + " could not be updated on account of an error!", e);
		}finally{
			if(unitOfWork != null && unitOfWork.getState() == UnitOfWorkStateEnum.STARTED){
				unitOfWork.finish();
			}
		}
	}
	
	private void executeConceptUpdate(IUnitOfWork unitOfWork, IConcept concept) throws SQLException, DataAccessException{
		Connection connection = this.getConnection(unitOfWork);
		this.executeConceptDeletion(connection, concept.getId());
		this.executeInsertConcept(unitOfWork, concept);
	}
	
	// for deletions
	
	private void executeConceptDeletion(Connection connection, String conceptId) throws SQLException{
		PreparedStatement deleteConceptStatement = connection.prepareStatement("DELETE FROM Concept WHERE id = ?");
		deleteConceptStatement.setString(1, conceptId);
		deleteConceptStatement.executeUpdate();
		deleteConceptStatement.close();
	}
	
	// for selections
	
	private String createSqlForSelectConcepts(int userId, IConceptFilter conceptFilter){
		String sql = SELECT_CONCEPT;
		sql += " WHERE ";
		sql += this.createConditionReadAccessRightsForConcepts(userId);
		if(conceptFilter != null){
			if(conceptFilter.getTypes() != null && conceptFilter.getTypes().isEmpty() == false){
				sql += " AND Concept.type IN (";
				for(int i = 0; i < conceptFilter.getTypes().size(); i++){
					sql += "'" + conceptFilter.getTypes().get(i).toString() + "'";
					if(i < conceptFilter.getTypes().size() - 1){
						sql += ", ";
					}
				}
				sql += ")";
			}
			if(conceptFilter.getUpdated() != null){
				sql += " AND Concept.id IN (";
				sql += createLogSubQueryForSelectConcepts(conceptFilter);
				sql += ")";
			}
			if(conceptFilter.getOffset() != -1 || conceptFilter.getLimit() != -1){
				sql += " ORDER BY Concept.id";
				if(conceptFilter.getLimit() != -1){
					sql += " LIMIT ";
					if(conceptFilter.getOffset() != -1){
						sql += conceptFilter.getOffset() + ", ";
					}
					sql += conceptFilter.getLimit();
				}
			}
		}
		return sql;
	}
	
	private String createLogSubQueryForSelectConcepts(IConceptFilter conceptFilter){
		String sql = "SELECT ConceptLog.conceptId FROM ConceptLog WHERE success = true AND `action` IN ('CREATE', 'UPDATE') AND `timestamp` >= '";
		sql += this.convertGregorianCalendarToMySqlDateTime(conceptFilter.getUpdated());
		sql += "'";
		return sql;
	}
	
	private String createConditionReadAccessRightsForConcepts(int userId){
		String sql = "(Concept.id IN (SELECT Ownership.conceptId FROM Ownership WHERE userId = ";
		sql += String.valueOf(userId) + ") OR Concept.id IN ";
		sql += "(SELECT GroupAccessRight.conceptId FROM GroupAccessRight WHERE GroupAccessRight.readRight = true AND GroupAccessRight.groupId IN (SELECT UserBelongsToGroup.groupId FROM UserBelongsToGroup WHERE UserBelongsToGroup.userId = ";
		sql += String.valueOf(userId) + ")))";
		sql += "OR (SELECT superAdmin FROM User WHERE id = " + userId + ")";
		return sql;
	}
	
	private List<IConcept> executeConceptsSelection(IUnitOfWork unitOfWork, String sql) throws DataAccessException{
		List<IConcept> concepts;
		if(unitOfWork != null){
			concepts = this.selectConceptsAsPartOfATransaction(unitOfWork, sql);
		}else{
			concepts = this.selectConceptsNotAsPartOfATransaction(sql);
		}
		return concepts;
	}
	
	private List<IConcept> selectConceptsAsPartOfATransaction(IUnitOfWork unitOfWork, String sql) throws DataAccessException{
		List<IConcept> concepts = new ArrayList<IConcept>();
		try {
			PreparedStatement selectConceptStatement = this.getConnection(unitOfWork).prepareStatement(sql);
			ResultSet resultSet = selectConceptStatement.executeQuery();
			while(resultSet.next()){
				concepts.add(this.createConcept(unitOfWork, resultSet));
			}
			resultSet.close();
			selectConceptStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("The concept(s) could not be selected on account of an error with the database!", e);
		}
		return concepts;
	}
	
	private List<IConcept> selectConceptsNotAsPartOfATransaction(String sql) throws DataAccessException {
		List<IConcept> concepts = new ArrayList<IConcept>();
		IUnitOfWork unitOfWork = this.createUnitOfWork();
		unitOfWork.start();
		try {
			PreparedStatement selectConceptStatement = this.getConnection(unitOfWork).prepareStatement(sql);
			ResultSet resultSet = selectConceptStatement.executeQuery();
			while (resultSet.next()) {
				concepts.add(this.createConcept(unitOfWork, resultSet));
			}
			resultSet.close();
			selectConceptStatement.close();
		} catch (DataAccessException e) {
			unitOfWork.abort();
			throw e;
		} catch (Exception e) {
			unitOfWork.abort();
			e.printStackTrace();
			throw new DataAccessException(
					"The concept(s) could not be selected on account of an error with the database!", e);
		} finally {
			if (unitOfWork != null && unitOfWork.getState() == UnitOfWorkStateEnum.STARTED) {
				unitOfWork.finish();
			}
		}
		return concepts;
	}
	
	private IConcept createConcept(IUnitOfWork unitOfWork, ResultSet resultSet) throws SQLException, DataAccessException{
		IConcept concept = new Concept(resultSet.getString(1));
		concept.setType(TypeEnum.valueOf(resultSet.getString(2)));
		concept.setSubType(SubTypeEnum.valueOf(resultSet.getString(3)));
		concept.setOrigin(resultSet.getString(4));
		concept.setDataType(DataTypeEnum.valueOf(resultSet.getString(5)));
		if(resultSet.getString(6) == null){
			concept.setValueSpace(null);
		}else{
			concept.setValueSpace(new ValueSpace(resultSet.getString(6)));	
		}
		concept.setAuthors(this.getAuthorsFromString(resultSet.getString(7)));
		
		Connection connection = this.getConnection(unitOfWork);
		this.selectConceptNames(connection, concept);
		this.selectConceptDefinitions(connection, concept);
		this.selectNotes(connection, concept);
		this.selectExamples(connection, concept);
		this.selectConceptsWhichAreRefinedByThisConcept(connection, concept);
		this.selectConceptsWhichRefineThisConcept(connection, concept);
		this.selectConceptsWhichAreTransformedByThisConcept(connection, concept);
		this.selectConceptsWhichTransformThisConcept(connection, concept);
		concept.setOwners(this.ownershipDao.selectOwnershipsOfConcept(unitOfWork, concept.getId()));
		this.selectUpdated(unitOfWork, concept);
		this.selectGroupAccessRights(unitOfWork, concept);
		return concept;
	}
	
	@SuppressWarnings("unchecked")
	private void selectConceptNames(Connection connection, IConcept concept) throws SQLException{
		String sql = "SELECT * FROM ConceptName WHERE conceptId = ?";
		concept.setNames(this.selectContents(connection, concept.getId(), sql));
	}
	
	@SuppressWarnings("unchecked")
	private void selectConceptDefinitions(Connection connection, IConcept concept) throws SQLException{
		String sql = "SELECT * FROM ConceptDefinition WHERE conceptId = ?";
		concept.setDefinitions(this.selectContents(connection, concept.getId(), sql));
	}
	
	@SuppressWarnings("unchecked")
	private void selectNotes(Connection connection, IConcept concept) throws SQLException{
		String sql = "SELECT * FROM Note WHERE conceptId = ?";
		concept.setNotes(this.selectContents(connection, concept.getId(), sql));
	}
	
	@SuppressWarnings("unchecked")
	private void selectExamples(Connection connection, IConcept concept) throws SQLException{
		String sql = "SELECT * FROM Example WHERE conceptId = ?";
		concept.setExamples(this.selectContents(connection, concept.getId(), sql));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List selectContents(Connection connection, String conceptId, String sql) throws SQLException{
		List values = new ArrayList<IContent>();
		PreparedStatement selectNotesOrExamplesStatement = connection.prepareStatement(sql);
		selectNotesOrExamplesStatement.setString(1, conceptId);
		ResultSet resultSet = selectNotesOrExamplesStatement.executeQuery();
		while(resultSet.next()){
			if(sql.contains("Name")){
				values.add(new Name(resultSet.getInt(1), resultSet.getString(3), resultSet.getString(2), resultSet.getString(4)));
			}else if(sql.contains("Definition")){
				values.add(new Definition(resultSet.getInt(1), resultSet.getString(3), resultSet.getString(2), resultSet.getString(4)));
			}else if(sql.contains("Note")){
				values.add(new Note(resultSet.getInt(1), resultSet.getString(3), conceptId, resultSet.getString(4)));
			}else if(sql.contains("Example")){
				values.add(new Example(resultSet.getInt(1), resultSet.getString(3), conceptId, resultSet.getString(4)));
			}else{
				throw new RuntimeException("Method getNotesOrExamples can not process the query result of sql=" + sql + "!");
			}
		}
		resultSet.close();
		selectNotesOrExamplesStatement.close();
		return values;
	}
	
	private void selectConceptsWhichAreTransformedByThisConcept(Connection connection, IConcept concept) throws SQLException{
		String sql = "SELECT transformsConceptId FROM Transformation WHERE conceptId = ?";
		concept.setConceptsWhichAreTransformedByThisConcept(this.selectTransformationsOrRefinements(connection, concept.getId(), sql));
	}
	
	private void selectConceptsWhichTransformThisConcept(Connection connection, IConcept concept) throws SQLException{
		String sql = "SELECT conceptId FROM Transformation WHERE transformsConceptId = ?";
		concept.setConceptsWhichTransformThisConcept(this.selectTransformationsOrRefinements(connection, concept.getId(), sql));
	}
	
	private void selectConceptsWhichAreRefinedByThisConcept(Connection connection, IConcept concept) throws SQLException{
		String sql = "SELECT refinesConceptId FROM Refinement WHERE conceptId = ?";
		concept.setConceptsWhichAreRefinedByThisConcept(this.selectTransformationsOrRefinements(connection, concept.getId(), sql));
	}
	
	private void selectConceptsWhichRefineThisConcept(Connection connection, IConcept concept) throws SQLException{
		String sql = "SELECT conceptId FROM Refinement WHERE refinesConceptId = ?";
		concept.setConceptsWhichRefineThisConcept(this.selectTransformationsOrRefinements(connection, concept.getId(), sql));
	}
	
	private List<String> selectTransformationsOrRefinements(Connection connection, String conceptId, String sql) throws SQLException{
		List<String> values = new ArrayList<String>();
		PreparedStatement selectTransformationsOrRefinementsStatement = connection.prepareStatement(sql);
		selectTransformationsOrRefinementsStatement.setString(1, conceptId);
		ResultSet resultSet = selectTransformationsOrRefinementsStatement.executeQuery();
		while(resultSet.next()){
			values.add(resultSet.getString(1));
		}
		resultSet.close();
		selectTransformationsOrRefinementsStatement.close();
		return values;
	}
	
	private void selectUpdated(IUnitOfWork unitOfWork, IConcept concept) throws DataAccessException{
		ILogDao logDao = new MySqlLogDao();
		long timeStampAsLong = -1;
		GregorianCalendar timeStampAsGregorianCalendar = logDao.selectLastUpdateDateTimeStampOfConcept(unitOfWork, concept.getId());
		if(timeStampAsGregorianCalendar != null){
			timeStampAsLong = timeStampAsGregorianCalendar.getTimeInMillis();
		}
		concept.setUpdatedInMilliseconds(timeStampAsLong);
	}
	
	private void selectGroupAccessRights(IUnitOfWork unitOfWork, IConcept concept) throws DataAccessException{
		IGroupAccessRightDao groupAccessRightDao = new MySqlGroupAccessRightDao();
		concept.setGroupAccessRights(groupAccessRightDao.selectGroupAccessRightsForConcept(unitOfWork, concept.getId()));
	}
	 
	private List<String> getAuthorsFromString(String authorsAsString){
		List<String> authorsAsList = new ArrayList<String>();
		String[] authorsAsArray = authorsAsString.split("; ");
		for(String n : authorsAsArray){
			authorsAsList.add(n);
		}
		return authorsAsList;
	}
	
	private int executeSelectTotalNumberOfConcepts(IUnitOfWork unitOfWork, String sql) throws DataAccessException{
		int totalNumberOfConcepts = -1;
		Connection connection = null;
		try {
			connection = this.getConnection(unitOfWork);
			PreparedStatement selectTotalNumberOfConceptsStatement = connection.prepareStatement(sql);
			ResultSet resultSet = selectTotalNumberOfConceptsStatement.executeQuery();
			while(resultSet.next()){
				totalNumberOfConcepts = resultSet.getInt(1);
			}
			resultSet.close();
			selectTotalNumberOfConceptsStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("The total number of concepts could not be counted on account of an error with the database!", e);
		}finally{
			this.closeConnection(unitOfWork, connection);
		}
		return totalNumberOfConcepts;
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
