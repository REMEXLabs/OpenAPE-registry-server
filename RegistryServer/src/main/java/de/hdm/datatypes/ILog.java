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

public interface ILog {

    /**
     * Getter for the user's id who did the action. If the action belongs to an invalid user id, -1 will be returned.
     * 
     * @return user's id who did the action or -1
     */
    public int getUserId();

    /**
     * Setter for the user's id who did the action. If you want to log an failed action, which belongs to an invalid
     * user id, you can use -1 as value. If the user id is smaller -1 an {@link IllegalArgumentException} will be
     * thrown.
     * 
     * @param userId
     *            user's id who did the action
     */
    public void setUserId(int userId);

    /**
     * Getter for the concept's id, which was part of the user's action.
     * 
     * @return concept's id, which was part of the users action or null, if the concept id was invalid or not existing
     */
    public String getConceptId();

    /**
     * Setter for the concept's id, which was part of the user's action. If you want to log an failed action, which
     * belongs to an invalid or rather not existing concept id, you can use null as value. If the concept id is empty,
     * an {@link IllegalArgumentException} will be thrown.
     * 
     * @param conceptId
     *            id of the concept or null
     */
    public void setConceptId(String conceptId);

    /**
     * Getter for the date and time in milliseconds of the user's action.
     * 
     * @return data and time in milliseconds of the user's action
     */
    public long getDateTimeStampInMilliseconds();

    /**
     * Setter for the date and time stamp in milliseconds of the user's action.
     * 
     * @param dateTimeStampInMilliseconds
     *            date and time stamp in milliseconds of the user's action
     */
    public void setDateTimeStampInMilliseconds(long dateTimeStampInMilliseconds);

    /**
     * Getter for the type of the user's action.
     * 
     * @return type of the user's action.
     */
    public ActionEnum getAction();

    /**
     * Setter for the type of the user's action
     * 
     * @param action
     *            type of the user's action
     */
    public void setAction(ActionEnum action);

    /**
     * Getter for the result (success / failure) of the user's action.
     * 
     * @return true if the user's action terminated successfully and false if not
     */
    public boolean isSuccess();

    /**
     * Setter for the result (success / failure) of the user's action.
     * 
     * @param success
     *            true if the user's action terminated successfully and false if not
     */
    public void setSuccess(boolean success);

    /**
     * Optional comment. For example an error message.
     * 
     * @return comment or null, if there is no comment
     */
    public String getNote();

    /**
     * Setter for an optional note to the user's action.
     * 
     * @param note
     *            to the user's action or null
     */
    public void setNote(String note);

    /**
     * Return the date time stamp {@link ILog#getDateTimeStampInMilliseconds()} of the user's action as string in UTC.
     * 
     * @return date time stamp as string in UTC
     */
    public String getDateTimeStampAsUTCString();

    /**
     * Checks whether the note of this log {@link ILog#getNote()} is formatted as JSON.
     * 
     * @return true if the note is formatted as JSON and false if not and when the note is null or empty
     */
    public boolean isNoteJson();

}
