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

import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdm.helpers.Checker;
import de.hdm.helpers.GregorianCalendarHelper;

/**
 * This class is a log entry for a single user action. For example the update of a concept. Each log entry consists of
 * the user's id, who did the action, the concept's id, which was affected thereby, date and time as well as the type
 * {@link ActionEnum} of the action. Furthermore it contains whether the action was terminated successfully or failed
 * and an optional comment / note.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class Log implements ILog{

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * The user's id, which activity is logged.
	 */
	private int userId;

	/**
	 * The concept's id, which was part of the user's action.
	 */
	private String conceptId;
	
	/**
	 * Date and time stamp in milliseconds of the user's action.
	 */
	private long dateTimeStampInMilliseconds;
	
	/**
	 * Type of the user's action.
	 */
	private ActionEnum action;
	
	/**
	 * True if the user's action did not fail and false if it failed.
	 */
	private boolean success;
	
	/**
	 * Optional note to the user's action. For example the error message if his action failed.
	 */
	private String note;


	
	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	
	/**
	 * Empty default constructor.
	 */
	public Log(){
		
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
	 * @see de.hdm.datatypes.ILog#getUserId()
	 */
	@Override
	public int getUserId() {
		return this.userId;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.ILog#setUserId(int)
	 */
	@Override
	public void setUserId(int userId) {
		Checker.checkIntegerGreater(userId, "userId", -2);
		this.userId = userId;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.ILog#getConceptId()
	 */
	@Override
	public String getConceptId() {
		return this.conceptId;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.ILog#setConceptId(java.lang.String)
	 */
	@Override
	public void setConceptId(String conceptId) {
		Checker.checkEmptiness(conceptId, "conceptId");
		this.conceptId = conceptId;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.ILog#getDateTimeStampInMilliseconds()
	 */
	@Override
	public long getDateTimeStampInMilliseconds() {
		return this.dateTimeStampInMilliseconds;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.ILog#setDateTimeStampInMilliseconds(long)
	 */
	@Override
	public void setDateTimeStampInMilliseconds(long dateTimeStampInMilliseconds) {
		this.dateTimeStampInMilliseconds = dateTimeStampInMilliseconds;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.ILog#getAction()
	 */
	@Override
	public ActionEnum getAction() {
		return this.action;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.ILog#setAction(de.hdm.datatypes.ActionEnum)
	 */
	@Override
	public void setAction(ActionEnum action) {
		this.action = action;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.ILog#isSuccess()
	 */
	@Override
	public boolean isSuccess() {
		return this.success;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.ILog#setSuccess(boolean)
	 */
	@Override
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.ILog#getNote()
	 */
	@Override
	public String getNote() {
		return this.note;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.ILog#setNote(java.lang.String)
	 */
	@Override
	public void setNote(String note) {
		this.note = note;
	}
	
	@Override
	public String getDateTimeStampAsUTCString() {
		String result;
		if(this.dateTimeStampInMilliseconds == -1){
			result = "-";
		}else{
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTimeInMillis(this.dateTimeStampInMilliseconds);
			gregorianCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
			result = GregorianCalendarHelper.convertDateAndTimeToString(gregorianCalendar, "-", " ", ":", true, ".") + " UTC";	
		}
		return result;
	}
	
	@Override
	public boolean isNoteJson(){
	    boolean isJson = false;
	    if(this.note != null && !this.note.isEmpty()){
	        try{
	            new JSONObject(this.note);
	            isJson = true;
	        }catch(JSONException e){
	            // do nothing
	        }    
	    }
        return isJson;
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
