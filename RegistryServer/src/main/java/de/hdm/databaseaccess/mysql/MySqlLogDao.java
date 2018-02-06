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
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.ILogDao;
import de.hdm.databaseaccess.IUnitOfWork;
import de.hdm.datatypes.ActionEnum;
import de.hdm.datatypes.ILog;
import de.hdm.datatypes.Log;
import de.hdm.helpers.Checker;

/**
 * This class implements the database access for logs on MySql databases.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class MySqlLogDao extends MySqlDao implements ILogDao {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Empty default constructor.
	 */
	public MySqlLogDao(){
		
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
	 * @see de.hdm.databaseaccess.ILogDao#insertLog(de.hdm.databaseaccess.IUnitOfWork, de.hdm.datatypes.ILog)
	 */
	@Override
	public void insertLog(IUnitOfWork unitOfWork, ILog log) throws DataAccessException {
		Checker.checkNull(log, "log");
		String sql = "INSERT INTO ConceptLog (conceptId, userId, timeStamp, action, success, note) VALUES (?, ?, ?, ?, ?, ?)";
		Connection connection = null;
		try {
			connection = this.getConnection(unitOfWork);
			PreparedStatement insertConceptLogStatement = connection.prepareStatement(sql);
			if(log.getConceptId() == null){
				insertConceptLogStatement.setNull(1, Types.VARCHAR);
			}else{
				insertConceptLogStatement.setString(1, log.getConceptId());	
			}
			if(log.getUserId() == -1){
				insertConceptLogStatement.setNull(2, Types.INTEGER);
			}else{
				insertConceptLogStatement.setInt(2, log.getUserId());
			}
			
			GregorianCalendar timeStamp = new GregorianCalendar();
			timeStamp.setTimeInMillis(log.getDateTimeStampInMilliseconds());
			insertConceptLogStatement.setString(3, this.convertGregorianCalendarToMySqlDateTime(timeStamp));
			
			insertConceptLogStatement.setString(4, log.getAction().toString());
			insertConceptLogStatement.setBoolean(5, log.isSuccess());
			insertConceptLogStatement.setString(6, log.getNote());
			insertConceptLogStatement.executeUpdate();
			insertConceptLogStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Could not insert log entry on account of an error with the database!", e);
		}finally{
			this.closeConnection(unitOfWork, connection);
		}
	}
		
	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.ILogDao#updatedSinceUserRed(de.hdm.databaseaccess.IUnitOfWork, int, java.lang.String)
	 */
	@Override
	public boolean updatedSinceUserRed(IUnitOfWork unitOfWork, int userId, String conceptId) throws DataAccessException {
		Checker.checkUserId(userId);
		Checker.checkNullAndEmptiness(conceptId, "conceptId");
		String sql = "SELECT case when (SELECT MAX(timestamp) FROM ConceptLog WHERE conceptId = ? AND userId = ? AND action = 'READ' AND success = true) > (SELECT COALESCE(MAX(timestamp), 0) FROM ConceptLog WHERE conceptId = ? AND action in ('CREATE', 'UPDATE') AND success = true) then 'false' else 'true' end";
		boolean updatedSinceUserRed = false;
		Connection connection = null;
		try {
			connection = this.getConnection(unitOfWork);
			PreparedStatement selectCheckUpdatedStatement = connection.prepareStatement(sql);
			selectCheckUpdatedStatement.setString(1, conceptId);
			selectCheckUpdatedStatement.setInt(2, userId);
			selectCheckUpdatedStatement.setString(3, conceptId);
			ResultSet resultSet = selectCheckUpdatedStatement.executeQuery();
			while(resultSet.next() && updatedSinceUserRed == false){
				updatedSinceUserRed = resultSet.getBoolean(1);
			}
			resultSet.close();
			selectCheckUpdatedStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("It could not be checked whether the concept " + conceptId + " was changed, since user " + userId + " has red it!", e);
		}finally{
			this.closeConnection(unitOfWork, connection);
		}
		return updatedSinceUserRed;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.ILogDao#selectLastUpdateTimeStampOfConcept(de.hdm.databaseaccess.IUnitOfWork, java.lang.String)
	 */
	@Override
	public GregorianCalendar selectLastUpdateDateTimeStampOfConcept(IUnitOfWork unitOfWork, String conceptId) throws DataAccessException {
		//System.out.println("in selectLastUpdateTimeStampOfConcept");
		GregorianCalendar updated = null;
		Checker.checkNullAndEmptiness(conceptId, "conceptId");
		String sql = "SELECT MAX(timestamp) FROM ConceptLog WHERE conceptId = ? AND action IN ('CREATE', 'UPDATED') AND success = true";
		Connection connection = null;
		try {
			connection = this.getConnection(unitOfWork);
			PreparedStatement selectLastUpdateTimeStampStatement = connection.prepareStatement(sql);
			selectLastUpdateTimeStampStatement.setString(1, conceptId);
			ResultSet resultSet = selectLastUpdateTimeStampStatement.executeQuery();
			while(resultSet.next()){
				String updatedAsString = resultSet.getString(1);
				if(updatedAsString != null){
					updated = this.convertMySqlDateTimeToGregorianCalendar(resultSet.getString(1));
				}
			}
			resultSet.close();
			selectLastUpdateTimeStampStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("The last update time stamp for the concept " + conceptId + " could not be selected on account of an error with the database!", e);
		}finally{
			this.closeConnection(unitOfWork, connection);
		}
		return updated;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.databaseaccess.ILogDao#selectAllLogsForConcept(de.hdm.databaseaccess.IUnitOfWork, java.lang.String, boolean)
	 */
	@Override
	public List<ILog> selectAllLogsForConcept(IUnitOfWork unitOfWork, String conceptId, boolean newestEntryFirst) throws DataAccessException {
		Checker.checkNullAndEmptiness(conceptId, "conceptId");
		List<ILog> logs = new ArrayList<ILog>();
		String sql = "SELECT conceptId, userId, timestamp, action, success, note FROM ConceptLog WHERE conceptId = ? ORDER BY timestamp ";
		if(newestEntryFirst){
			sql += "DESC";
		}else{
			sql += "ASC";
		}
		Connection connection = null;
		try {
			connection = this.getConnection(unitOfWork);
			PreparedStatement selectAllLogsStatement = connection.prepareStatement(sql);
			selectAllLogsStatement.setString(1, conceptId);
			ResultSet resultSet = selectAllLogsStatement.executeQuery();
			while(resultSet.next()){
				logs.add(this.createLog(resultSet));
			}
			resultSet.close();
			selectAllLogsStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("The logs for the concept " + conceptId + " could not be selected on account of an error with the database!", e);
		}finally{
			this.closeConnection(unitOfWork, connection);
		}
		return logs;
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

	private ILog createLog(ResultSet resultSet) throws SQLException{
		ILog log = new Log();
		log.setConceptId(resultSet.getString(1));
		if(resultSet.getString(2) == null){
			log.setUserId(-1);
		}else{
			log.setUserId(resultSet.getInt(2));	
		}
		log.setDateTimeStampInMilliseconds(this.convertMySqlDateTimeToGregorianCalendar(resultSet.getString(3)).getTimeInMillis());
		log.setAction(ActionEnum.valueOf(resultSet.getString(4)));
		log.setSuccess(resultSet.getBoolean(5));
		log.setNote(resultSet.getString(6));
		return log;
	}



	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
