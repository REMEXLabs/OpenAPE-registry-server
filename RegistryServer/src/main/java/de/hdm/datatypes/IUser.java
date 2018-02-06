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

/**
 * This is the interface for an user.
 * 
 * @author Tobias Ableitner
 *
 */
public interface IUser {

    /**
     * Getter for the user's id.
     * 
     * @return user's id
     */
    public int getId();

    /**
     * Getter for the user's user name.
     * 
     * @return user's user name
     */
    public String getUserName();

    /**
     * Setter for the user's user name. The user name must not be null. Otherwise an {@link IllegalArgumentException}
     * will be thrown.
     * 
     * @param userName
     *            user's user name
     */
    public void setUserName(String userName);

    /**
     * Getter for the user's first name. Not all users have a first name, because institutes or companies can also get
     * an account.
     * 
     * @return user's first name or null
     */
    public String getFirstName();

    /**
     * Setter for the user's first name. It can be null.
     * 
     * @param firstName
     *            user's first name or null
     */
    public void setFirstName(String firstName);

    /**
     * Getter for the user's last name. Not all users have a last name, because institutes or companies can also get an
     * account.
     * 
     * @return user's last name or null
     */
    public String getLastName();

    /**
     * Setter for the user's last name. It can be null.
     * 
     * @param lastName
     *            user's last name or null
     */
    public void setLastName(String lastName);

    /**
     * Getter for the user's institute. Not all users have an institute, because institutes or companies can also get an
     * account. Such users will not have a first name {@link #getFirstName()} and last name {@link #getLastName()}.
     * Instead they will use this attribute.
     * 
     * @return user's institute or null
     */
    public String getInstitute();

    /**
     * Setter for the user's institute. It can be null.
     * 
     * @param institute
     *            user's institute or null
     */
    public void setInstitute(String institute);

    /**
     * Getter for the user's e-mail address.
     * 
     * @return user's e-mail address
     */
    public String geteMailAddress();

    /**
     * Setter for the user's e-mail address. It must not be null or empty. Otherwise an {@link IllegalArgumentException}
     * will be thrown. This method will not check whether the e-mail address is valid or not. If you set an invalid one,
     * this can raise errors / problems at a later time.
     * 
     * @param eMailAddress
     *            user's e-mail address
     */
    public void seteMailAddress(String eMailAddress);

    /**
     * Getter for the user's hashed password.
     * 
     * @return user's hashed password
     */
    public String getHashOfPassword();

    /**
     * Setter for the user's hashed password. It must not be null or empty. Otherwise an
     * {@link IllegalArgumentException} will be thrown.
     * 
     * @param hashOfPassword
     *            user's hashed password
     */
    public void setHashOfPassword(String hashOfPassword);

    /**
     * Getter for the user's hashed reset password. Normally this is null. It is only not null, if the user has
     * forgotten his normal password {@link IUser#getHashOfPassword()} and want's to reset it.
     * 
     * @return user's hashed reset password or null
     */
    public String getHashOfResetPassword();

    /**
     * Setter for the user's hashed reset password. It can be set to null, but must not be empty. Otherwise an
     * {@link IllegalArgumentException} will be thrown.
     * 
     * @param hashOfResetPassword
     *            user's hashed reset password or null
     */
    public void setHashOfResetPassword(String hashOfResetPassword);

    /**
     * Getter for the expiry date of the reset password {@link IUser#getHashOfResetPassword()}.
     * 
     * @return expiry date of the reset password in milliseconds or -1 if there exists no reset password.
     */
    public long getExpiryDateOfResetPasswordInMilliseconds();

    /**
     * Setter for the expiry date of the reset password {@link IUser#getHashOfResetPassword()}. If there exist no reset
     * password the expiry date should be set to -1.
     * 
     * @param expiryDateOfResetPasswordInMilliseconds
     *            it must be greater equals -1. Otherwise an {@link IllegalArgumentException} will be thrown!
     */
    public void setExpiryDateOfResetPasswordInMilliseconds(long expiryDateOfResetPasswordInMilliseconds);

    /**
     * Getter for the user's api key. The api key is required to use the registry server's JSON Rest API. Not all users
     * want to access the JSON Rest api, thus not all of them have an api key.
     * 
     * @return user's api key or null
     */
    public String getApiKey();

    /**
     * Setter for the user's api key. It can be set to null, but must not be empty. Otherwise an
     * {@link IllegalArgumentException} will be thrown.
     * 
     * @param apiKey
     *            user's api key or null
     */
    public void setApiKey(String apiKey);

    /**
     * Getter whether the user is super admin or not.
     * 
     * @return true if the user is super admin and false if not
     */
    public boolean isSuperAdmin();

    /**
     * Setter whether the user is super admin or not.
     * 
     * @param superAdmin
     *            true if the user is super admin and false if not
     */
    public void setSuperAdmin(boolean superAdmin);

    /**
     * Getter whether the user is admin of at least one group or not.
     * 
     * @return true if the user is admin of at least one group and false if not
     */
    public boolean isAdminOfAtLeastOneGroup();

    /**
     * Setter whether the user is admin of at least one group {@link IGroup}.
     * 
     * @param adminOfAtLeastOneGroup
     *            true if the user is admin of at least one group and false if not
     */
    public void setAdminOfAtLeastOneGroup(boolean adminOfAtLeastOneGroup);

    /**
     * Getter for the user's session id. Returns null, if the user is not logged in.
     * 
     * @return user's session id or null
     */
    public String getSessionId();

    /**
     * Setter for the user's session id. It can be null but must not be empty. Otherwise an
     * {@link IllegalArgumentException} will be thrown.
     * 
     * @param sessionId
     *            user's session id or null
     */
    public void setSessionId(String sessionId);

    /**
     * Getter for the number of the user's failed logins since last successful login.
     * 
     * @return number of the user's failed logins
     */
    public int getNumberOfFailedLogins();

    /**
     * Setter for the number of the user's failed logins since last successful login.
     * 
     * @param numberOfFailedLogins
     *            number of the user's failed logins. It must be greater equals 0. Otherwise an
     *            {@link IllegalArgumentException} will be thrown!
     */
    public void setNumberOfFailedLogins(int numberOfFailedLogins);

    /**
     * Getter whether the user is locked or not. If an user is locked, he / she can no more login and use his / her api
     * key.
     * 
     * @return true if the user is locked and false if not.
     */
    public boolean isLocked();

    /**
     * Setter whether the user is locked or not. If an user is locked, he / she can no more login and use his / her api
     * key.
     * 
     * @param locked
     *            true to lock the user and false to unlock
     */
    public void setLocked(boolean locked);

}
