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

/**
 * This class contains string constants and inner classes with string constants. Those constants contain paths, which are used by the registry server.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class Path {
	
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
	
	/**
	 * This class contains the paths for the JSON rest api.
	 * 
	 * This class is thread safe.
	 * 
	 * @author Tobias Ableitner
	 *
	 */
	public static class Json{
		
		/**
		 * Path to create a new concept via the JSON Rest api.
		 */
		public static final String CREATE_CONCEPT_RECORD = "/api/record";
		
		/**
		 * Path to update a concept via the JSON Rest api.
		 */
		public static final String UPDATE_CONCEPT_RECORD = "/api/record/:conceptId";
		
		/**
		 * Path to delete concept via the JSON Rest api.
		 */
		public static final String DELETE_CONCEPT_RECORD = "/api/record/:conceptId";
		
		/**
		 * Path to get a concept via the JSON Rest api.
		 */
		public static final String GET_CONCEPT_RECORD = "/api/record/:conceptId";
		
		/**
		 * Path to get concepts via the JSON Rest api.
		 */
		public static final String GET_LIST_OF_CONCEPT_RECORDS = "/api/records";
		
	}
	
	/**
	 * This class contains the paths for the web user interface.
	 * 
	 * This class is thread safe.
	 * 
	 * @author Tobias Ableitner
	 *
	 */
	public static class Web{
		
		public static final String LOGGED_IN_INFO_AND_ERROR = "/web/loggedIn/infoAndError/";
		
		public static final String NOT_LOGGED_IN_INFO_AND_ERROR = "/web/infoAndError/";
		
		//public static final String LEGAL_NOTICE = "/web/legalNotice/";
		
		//public static final String PRIVACY_POLICY = "/web/privacyPolicy/";
		
		public static final String ROOT = "/";
		
		
		// login and logout
		public static final String LOGIN = "/web/login/";
		
		public static final String FORGOT_PASSWORD = "/web/forgotPassword/";
		
		public static final String RESET_PASSWORD = "/web/resetPassword";
		
		public static final String LOGOUT = "/web/loggedIn/logout/";
		
		public static final String LOGOUT_SUCCESS = "/web/login/";
		
		public static final String WELCOME_USER = "/web/loggedIn/welcomeUser/";
		
		
		// concepts
		public static final String CONCEPTS_NEW_CONCEPT = "/web/loggedIn/concepts/newConcept/";
		
		public static final String CONCEPTS_EDIT_CONCEPT = "/web/loggedIn/concepts/editConcept";
		
		public static final String CONCEPTS_SHOW_CONCEPT = "/web/loggedIn/concepts/showConcept";
		
		public static final String CONCEPTS_SHOW_CONCEPT_PUBLIC = "/web/concepts/showConceptPublic";
		
		public static final String CONCEPTS_LIST_ALL = "/web/loggedIn/concepts/allConcepts/";
		
		public static final String CONCEPTS_LIST_ALL_PUBLIC = "/web/concepts/allConceptsPublic/";
		
		public static final String CONCEPTS_LIST_MY = "/web/loggedIn/concepts/myConcepts/";
		
		public static final String CONCEPTS_SEARCH = "/web/loggedIn/concepts/searchConcept/";
		
		public static final String CONCEPTS_CONCEPT_LOG = "/web/loggedIn/concepts/conceptLog";
		
		public static final String CONCEPTS_DELETE_CONCEPT = "/web/loggedIn/concepts/deleteConcept";
		
		
		// groups
		public static final String GROUPS_LIST_GROUPS = "/web/loggedIn/groups/list/";
		
		public static final String GROUPS_NEW_GROUP = "/web/loggedIn/groups/new/";
		
		public static final String GROUPS_EDIT_GROUP = "/web/loggedIn/groups/edit";
		
		public static final String GROUPS_REMOVE_GROUP = "/web/loggedIn/groups/remove";
		
		
		// users
		public static final String USERS_LIST_USERS = "/web/loggedIn/users/list/";
		
		public static final String USERS_NEW_USER = "/web/loggedIn/users/new/";
		
		public static final String USERS_EDIT_USER = "/web/loggedIn/users/edit";
		
		public static final String USERS_REMOVE_USER = "/web/loggedIn/users/remove";
		
		public static final String USERS_MY_DATA = "/web/loggedIn/users/myData/";
		
	}
	
	public static class WebTemplate{
		
		public static final String TEST = "velocity/test.vm";
		
		public static final String NOT_FOUND = "velocity/notFound.vm";
		
		public static final String LOGGED_IN_INFO_AND_ERROR = "velocity/loggedInInfoAndError.vm";
		
		public static final String NOT_LOGGED_IN_INFO_AND_ERROR = "velocity/notLoggedInInfoAndError.vm";


		
		// login and logout
		public static final String LOGIN = "velocity/login/login.vm";
		
		public static final String FORGOT_PASSWORD = "velocity/login/forgotPassword.vm";
		
		public static final String RESET_PASSWORD = "velocity/login/resetPassword.vm";
		
		public static final String RESET_PASSWORD_SUCCESSFULLY = "velocity/login/resetPasswordSuccessfully.vm";
		
		public static final String WELCOME_USER = "velocity/welcomeUser.vm";
		
		
		// concepts
		public static final String CONCEPTS_NEW_AND_EDIT_CONCEPT = "velocity/concepts/newAndEditConcept.vm";
		
		public static final String CONCEPTS_SHOW_CONCEPT = "velocity/concepts/showConcept.vm";
		
		public static final String CONCEPTS_SHOW_CONCEPT_PUBLIC = "velocity/concepts/showConceptPublic.vm";
		
		public static final String CONCEPTS_LIST_ALL = "velocity/concepts/allConcepts.vm";
		
		public static final String CONCEPTS_LIST_ALL_PUBLIC = "velocity/concepts/allConceptsPublic.vm";
		
		public static final String CONCEPTS_LIST_MY = "velocity/concepts/myConcepts.vm";
		
		public static final String CONCEPTS_SEARCH = "velocity/concepts/searchConcept.vm";
		
		public static final String CONCEPTS_CONCEPT_LOG = "velocity/concepts/conceptLog.vm";
		
		public static final String CONCEPTS_DELETE_CONCEPT = "velocity/concepts/deleteConcept.vm";
		
				
		// groups
		public static final String GROUPS_LIST = "velocity/groups/groupsList.vm";
		
		public static final String GROUPS_NEW_GROUP = "velocity/groups/newGroup.vm";
		
		public static final String GROUPS_EDIT_GROUP = "velocity/groups/editGroup.vm";
		
		
		// users
		public static final String USERS_LIST = "velocity/users/usersList.vm";
		
		public static final String USERS_NEW_USER = "velocity/users/newUser.vm";
		
		public static final String USERS_EDIT_USER = "velocity/users/editUser.vm";
		
		public static final String USERS_MY_DATA = "/velocity/users/myData.vm";
		
		public static final String USERS_DELETE_USER = "/velocity/users/deleteUser.vm";
		
	}
}
