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

import de.hdm.helpers.Checker;

/**
 * This class defines a user. Users can read, create, update and delete concepts {@link Concept}. Users are also the
 * owners of the
 * concepts. Furthermore users can be group members. The ownerships and the group memberships decide, for what concept
 * an user has which access rights and which groups he is allowed to administer.
 * A user can also be a super admin. A super admin has all rights. He can administer the groups and can do every
 * possible action in relation to the concepts.
 * 
 * There are two ways, how a user can authenticate against the server. If the user uses the JSON Rest API, he
 * authenticates via his api key {@link #apiKey}. If he uses the web interface, he has to authenticate with his user
 * name {@link #userName} and password {@link #hashOfPassword}. If the user forgets his password
 * {@link User#hashOfPassword} he can request a server generated password {@link #hashOfResetPassword} which will be
 * sent to him via an e-mail. Afterwards the user can use the reset password to set a new password
 * {@link #hashOfPassword}.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class User implements IUser {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * User's id.
	 */
	private int id;

	/**
	 * User's user name.
	 */
	private String userName;

	/**
	 * User's first name. It can be null, because institutes or companies can also get an user account.
	 */
	private String firstName;

	/**
	 * User's last name. It can be null, because institutes or companies can also get an user account.
	 */
	private String lastName;

	/**
	 * User's institute. It can be null, because not all users belong to an institute or company.
	 */
	private String institute;

	/**
	 * User's e-mail address.
	 */
	private String eMailAddress;

	/**
	 * User's hashed password.
	 */
	private String hashOfPassword;

	/**
	 * User's hashed reset password. It can be null, because it is only required, if the user has forgotten his password
	 * {@link #hashOfPassword} and want's to reset it.
	 */
	private String hashOfResetPassword;
	
	/**
	 * The expiry date in milliseconds of the reset password ({@link #hashOfResetPassword}). If there exists no reset
	 * password for this user, it's value is -1.
	 */
	private long expiryDateOfResetPasswordInMilliseconds;

	/**
	 * User's api key. The api key is required to use the JSON rest api. It can be null, because not all users access
	 * the registry server about the JSON rest api.
	 */
	private String apiKey;

	/**
	 * True if the user is super admin and false if not. If a user is super admin he has all rights on the registry
	 * server. So he can read, update and delete all concepts, change their group access rights and administer groups.
	 */
	private boolean superAdmin;
	
	/**
	 * True if the user is admin of at least one group and false if not. If he is an admin of a group he can edit it.
	 */
	private boolean adminOfAtLeastOneGroup;

	/**
	 * The user's session id, during he is logged in.
	 */
	private String sessionId;
	
	/**
	 * Counter for failed login tries due to a wrong password. Default value is 0.
	 */
	private int numberOfFailedLogins = 0;
	
	/**
	 * True if the user's account is locked als false if not.
	 */
	private boolean locked = false;



	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Creates a new user. This constructor should be used for users, who are not already stored in the database and
	 * have no user id {@link #id}.
	 */
	public User() {

	}

	/**
	 * Creates a new user. This constructor should be used for users, who are already stored in the database and have an
	 * user id {@link #id}.
	 * 
	 * @param id
	 *            the user's id. The id must be greater equals 1. Otherwise an {@link IllegalArgumentException} will be
	 *            thrown.
	 */
	public User(int id) {
		this.setId(id);
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// getters and setters
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	private void setId(int id) {
		Checker.checkIntegerGreaterEquals(id, "id", 1);
		this.id = id;
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
	 * @see de.hdm.datatypes.IUser#getId()
	 */
	@Override
	public int getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#getUserName()
	 */
	@Override
	public String getUserName() {
		return this.userName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#setUserName(java.lang.String)
	 */
	@Override
	public void setUserName(String userName) {
		Checker.checkNullAndEmptiness(userName, "userName");
		this.userName = userName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#getFirstName()
	 */
	@Override
	public String getFirstName() {
		return this.firstName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#setFirstName(java.lang.String)
	 */
	@Override
	public void setFirstName(String firstName) {
		Checker.checkEmptiness(firstName, "firstName");
		this.firstName = firstName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#getLastName()
	 */
	@Override
	public String getLastName() {
		return this.lastName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#setLastName(java.lang.String)
	 */
	@Override
	public void setLastName(String lastName) {
		Checker.checkEmptiness(lastName, "lastName");
		this.lastName = lastName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#getInstitute()
	 */
	@Override
	public String getInstitute() {
		return this.institute;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#setInstitute(java.lang.String)
	 */
	@Override
	public void setInstitute(String institute) {
		Checker.checkEmptiness(institute, "institute");
		this.institute = institute;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#geteMailAddress()
	 */
	@Override
	public String geteMailAddress() {
		return this.eMailAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#seteMailAddress(java.lang.String)
	 */
	@Override
	public void seteMailAddress(String eMailAddress) {
		Checker.checkNullAndEmptiness(eMailAddress, "eMailAddress");
		this.eMailAddress = eMailAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#getHashOfPassword()
	 */
	@Override
	public String getHashOfPassword() {
		return this.hashOfPassword;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#setHashOfPassword(java.lang.String)
	 */
	@Override
	public void setHashOfPassword(String hashOfPassword) {
		Checker.checkNullAndEmptiness(hashOfPassword, "hashOfPassword");
		this.hashOfPassword = hashOfPassword;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#getHashOfResetPassword()
	 */
	@Override
	public String getHashOfResetPassword() {
		return this.hashOfResetPassword;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#setHashOfResetPassword(java.lang.String)
	 */
	@Override
	public void setHashOfResetPassword(String hashOfResetPassword) {
		Checker.checkEmptiness(hashOfResetPassword, "hashOfResetPassword");
		this.hashOfResetPassword = hashOfResetPassword;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IUser#getExpiryDateOfResetPasswordInMilliseconds()
	 */
	@Override
	public long getExpiryDateOfResetPasswordInMilliseconds() {
		return this.expiryDateOfResetPasswordInMilliseconds;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IUser#setExpiryDateOfResetPasswordInMilliseconds(long)
	 */
	@Override
	public void setExpiryDateOfResetPasswordInMilliseconds(long expiryDateOfResetPasswordInMilliseconds) {
		Checker.checkLongGreaterEquals(expiryDateOfResetPasswordInMilliseconds, "expiryDateOfResetPasswordInMilliseconds", -1);
		this.expiryDateOfResetPasswordInMilliseconds = expiryDateOfResetPasswordInMilliseconds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#getApiKey()
	 */
	@Override
	public String getApiKey() {
		return this.apiKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#setApiKey(java.lang.String)
	 */
	@Override
	public void setApiKey(String apiKey) {
		Checker.checkEmptiness(apiKey, "apiKey");
		this.apiKey = apiKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#isSuperAdmin()
	 */
	@Override
	public boolean isSuperAdmin() {
		return this.superAdmin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#setSuperAdmin(boolean)
	 */
	@Override
	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#isAdminOfAtLeastOneGroup()
	 */
	@Override
	public boolean isAdminOfAtLeastOneGroup() {
		return this.adminOfAtLeastOneGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IUser#setAdminOfAtLeastOneGroup(boolean)
	 */
	@Override
	public void setAdminOfAtLeastOneGroup(boolean adminOfAtLeastOneGroup) {
		this.adminOfAtLeastOneGroup = adminOfAtLeastOneGroup;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IUser#getSessionId()
	 */
	@Override
	public String getSessionId(){
		return this.sessionId;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IUser#setSessionId(java.lang.String)
	 */
	@Override
	public void setSessionId(String sessionId){
		Checker.checkEmptiness(sessionId, "sessionId");
		this.sessionId = sessionId;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IUser#getNumberOfFailedLogins()
	 */
	@Override
    public int getNumberOfFailedLogins() {
        return this.numberOfFailedLogins;
    }

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IUser#setNumberOfFailedLogins(int)
	 */
	@Override
    public void setNumberOfFailedLogins(int numberOfFailedLogins) {
        Checker.checkIntegerGreaterEquals(numberOfFailedLogins, "numberOfFailedLogins", 0);
        this.numberOfFailedLogins = numberOfFailedLogins;
    }

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IUser#isLocked()
	 */
	@Override
    public boolean isLocked() {
        return this.locked;
    }

	/*
	 * (non-Javadoc)
	 * @see de.hdm.datatypes.IUser#setLocked(boolean)
	 */
	@Override
    public void setLocked(boolean locked) {
        this.locked = locked;
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
