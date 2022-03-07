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
package de.hdm.server.webinterface;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hdm.configuration.MyProperties;
import de.hdm.databaseaccess.DaoFactory;
import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IConceptDao;
import de.hdm.databaseaccess.IGroupDao;
import de.hdm.databaseaccess.IUnitOfWork;
import de.hdm.databaseaccess.IUserDao;
import de.hdm.datatypes.GroupMember;
import de.hdm.datatypes.IConcept;
import de.hdm.datatypes.IGroup;
import de.hdm.datatypes.IUser;
import de.hdm.datatypes.User;
import de.hdm.exceptions.RegistryServerException;
import de.hdm.helpers.TemplateFiller;
import de.hdm.multiplelanguages.LanguageHandler;
import de.hdm.server.AuthenticationController;
import de.hdm.server.AuthenticationException;
import de.hdm.server.BadRequestException;
import de.hdm.server.Path;
import de.hdm.server.RequestUtil;
import spark.Request;
import spark.Response;

public class HtmlUserController extends HtmlController implements IHtmlUserController {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Logger.
	 */
	private final static Logger logger = LogManager.getLogger(HtmlGroupController.class);
	
	private static final String MODEL_VALUE_KEY_USER_LIST = "userList";
	
	private static final String MODEL_VALUE_KEY_USER_NAME = "userName";
	
	private static final String MODEL_VALUE_KEY_FIRST_NAME = "firstName";
	
	private static final String MODEL_VALUE_KEY_LAST_NAME = "lastName";
	
	private static final String MODEL_VALUE_KEY_INSTITUTE = "institute";
	
	private static final String MODEL_VALUE_KEY_MAIL_ADDRESS = "eMailAddress";
	
	private static final String MODEL_VALUE_KEY_PASSWORD_1 = "password1";
	
	private static final String MODEL_VALUE_KEY_PASSWORD_2 = "password2";
	
	private static final String MODEL_VALUE_KEY_NUMBER_OF_FAILED_LOGINS = "numberOfFailedLogins";
	
	private static final String MODEL_VALUE_KEY_ACCOUNT_LOCKED = "accountLocked";
	
	private static final String MODEL_VALUE_KEY_API_KEY = "apiKey";
	
	private static final String MODEL_VALUE_KEY_GENERATE_API_KEY = "generateApiKey";
	
	private static final String MODEL_VALUE_KEY_API_KEY_ACTION = "apiKeyAction";
	
	private static final String MODEL_VALUE_KEY_API_KEY_ACTION_VALUE_NOTHING = "nothing";
	
	private static final String MODEL_VALUE_KEY_API_KEY_ACTION_VALUE_DELETE = "delete";
	
	private static final String MODEL_VALUE_KEY_API_KEY_ACTION_VALUE_NEW = "new";
	
	private static final String MODEL_VALUE_KEY_SUPER_ADMIN = "superAdmin";
	
	private static final String MODEL_VALUE_KEY_EDIT_USER_ID = "editUserId";
	
	private static final String MODEL_VALUE_KEY_SINGLE_OWNER_CONCEPTS = "singleOwnerConcepts";
	
	/**
	 * Data access object for the users.
	 */
	private IUserDao userDao;

	/**
	 * Data access object for the concepts.
	 */
	private IConceptDao conceptDao;
	
	/**
	 * Data access object for the groups.
	 */
	private IGroupDao groupDao;
	
	

	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	public HtmlUserController(){
		this.userDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createUserDao();
		this.conceptDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createConceptDao();
		this.groupDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createGroupDao();
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

	@Override
	public String getUsersPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.USERS);
		try {
			// authentication
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);
			
			// rights check and users loading
			if(user.isSuperAdmin() == false){
				throw new AuthenticationException("User " + user.getId() + " tried to get the users list page!", false);
			}
			
			// insert success message
			this.handleSuccessMessage(request, model);
			
			// load all users
			List<IUser> userList = this.userDao.selectUsers(null);
			
			model.put(MODEL_VALUE_KEY_USER_LIST, userList);
		} catch (Exception e) {
			this.handleExpception(locale, null, e, model, request, response, "WEB_USERS_LIST_PAGE_ERROR_MESSAGE_COMMON");
		}
		return ViewUtil.render(request, model, Path.WebTemplate.USERS_LIST, locale);
	}

	@Override
	public String getNewUserPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.USERS);
		try {
			// authentication
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);
			
			if(user.isSuperAdmin() == false){
				throw new AuthenticationException("User " + user.getId() + " tried to get the new user page!", false);	
			}
			
		} catch (Exception e) {
			this.handleExpception(locale, null, e, model, request, response, "WEB_USERS_NEW_USER_PAGE_ERROR_MESSAGE_COMMON_GET");
		}
		return ViewUtil.render(request, model, Path.WebTemplate.USERS_NEW_USER, locale);
	}

	@Override
	public String handleNewUserPost(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.USERS);
		IUnitOfWork unitOfWork = null;
		String userName = null;
		String firstName = null;
		String lastName = null;
		String institute = null;
		String eMailAddress = null;
		String password1 = null;
		String password2 = null;
		boolean locked = false;
		boolean generateApiKey = false;
		boolean superAdmin = false;
		try {
			// authentication and super admin check
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);
			if(user.isSuperAdmin() == false){
				throw new AuthenticationException("User " + user.getId() + " tried to create a new user!", false);	
			}
			
			// get parameter values
			userName = RequestUtil.getQueryParameterOrNullIfEmpty(request, "userName");
			firstName = RequestUtil.getQueryParameterOrNullIfEmpty(request, "firstName");
			lastName = RequestUtil.getQueryParameterOrNullIfEmpty(request, "lastName");
			institute = RequestUtil.getQueryParameterOrNullIfEmpty(request, "institute");
			eMailAddress = RequestUtil.getQueryParameterOrNullIfEmpty(request, "eMailAddress");
			password1 = RequestUtil.getQueryParameterOrNullIfEmpty(request, "password1");
			password2 = RequestUtil.getQueryParameterOrNullIfEmpty(request, "password2");
			locked = RequestUtil.getBooleanFromRadioButtons(request, "accountState");
			generateApiKey = RequestUtil.getBooleanFromRadioButtons(request, "generateApiKey");
			superAdmin = RequestUtil.getBooleanFromRadioButtons(request, "superAdmin");
			
			unitOfWork = this.userDao.createUnitOfWork();
			unitOfWork.start();
			
			// check input values
			this.checkUserName(unitOfWork, locale, userName, true, -1);
			this.checkFirstAndLastNameAndInstitute(locale, firstName, lastName, institute);
			this.checkMailAddress(locale, eMailAddress);
			this.checkPassword(locale, password1, password2);
			
			// create new user
			IUser newUser = new User();
			newUser.setUserName(userName);
			newUser.setFirstName(firstName);
			newUser.setLastName(lastName);
			newUser.setInstitute(institute);
			newUser.seteMailAddress(eMailAddress);
			newUser.setHashOfPassword(AuthenticationController.hashNewPassword(password1));
			newUser.setLocked(locked);
			if(generateApiKey){
				newUser.setApiKey(AuthenticationController.generateApiKey());	
			}
			newUser.setSuperAdmin(superAdmin);
			
			// store new user
			this.userDao.insertUser(unitOfWork, newUser);
			
			// add user to group all users group
			if(MyProperties.getAddNewUserToAnAllUserGroup()){
				IGroup allUsersGroup = this.groupDao.selectGroupById(unitOfWork, MyProperties.getAllUsersGroupId());
				if(allUsersGroup == null){
					throw new RegistryServerException("The new user could not be added to the all users group, because there exists no group with the id " + MyProperties.getAllUsersGroupId() + "!");
				}
				IUser newUserReloadedWithId = this.userDao.selectUser(unitOfWork, newUser.getUserName());
				allUsersGroup.getMembers().add(new GroupMember(newUserReloadedWithId.getId(), false));
				this.groupDao.updateGroup(unitOfWork, allUsersGroup);
			}
			
			// finish unit of work
			unitOfWork.finish();
			
			// create success message and redirect to groups list page
			this.handleSuccess(locale, request, response, "WEB_USERS_NEW_USER_PAGE_SUCCESS_MESSAGE", newUser.getUserName(), Path.Web.USERS_LIST_USERS);
			
		} catch (Exception e) {
			this.fillModel(model, userName, firstName, lastName, institute, eMailAddress, password1, password2, superAdmin);
			model.put(MODEL_VALUE_KEY_GENERATE_API_KEY, generateApiKey);
			model.put(MODEL_VALUE_KEY_ACCOUNT_LOCKED, locked);
			this.handleExpception(locale, unitOfWork, e, model, request, response, "WEB_USERS_NEW_USER_PAGE_ERROR_MESSAGE_COMMON_POST");
		}
		return ViewUtil.render(request, model, Path.WebTemplate.USERS_NEW_USER, locale);
	}

	@Override
	public String getEditUserPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.USERS);
		int editUserId = -1;
		try {
			// authentication and super admin check
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);
			if(user.isSuperAdmin() == false){
				throw new AuthenticationException("User " + user.getId() + " tried to get the edit user page!", false);	
			}
			
			// get parameters
			editUserId = RequestUtil.getQueryParameterPositiveInteger(locale, request, "userId", false);
			if(editUserId == -1){
				throw new AuthenticationException("User " + user.getId() + " called the edit user page without the query parameter user id!", false);
			}
			
			// load the user, who should be edited
			IUser editUser = this.userDao.selectUser(null, editUserId);
			if(editUser == null){
				throw new BadRequestException("An user with the id " + editUserId + " does not exist!");
			}
			
			// fill model
			this.fillModel(model, editUser.getUserName(), editUser.getFirstName(), editUser.getLastName(), editUser.getInstitute(), editUser.geteMailAddress(), null, null, editUser.isSuperAdmin());
			model.put(MODEL_VALUE_KEY_API_KEY, editUser.getApiKey());	
			model.put(MODEL_VALUE_KEY_API_KEY_ACTION, MODEL_VALUE_KEY_API_KEY_ACTION_VALUE_NOTHING);
			model.put(MODEL_VALUE_KEY_EDIT_USER_ID, editUser.getId());
			model.put(MODEL_VALUE_KEY_NUMBER_OF_FAILED_LOGINS, String.valueOf(editUser.getNumberOfFailedLogins()));
			model.put(MODEL_VALUE_KEY_ACCOUNT_LOCKED, editUser.isLocked());
			
		} catch (Exception e) {
			this.handleExpception(locale, null, e, model, request, response, "WEB_USERS_EDIT_USER_PAGE_ERROR_MESSAGE_COMMON_GET");
		}
		return ViewUtil.render(request, model, Path.WebTemplate.USERS_EDIT_USER, locale);
	}

	@Override
	public String handleEditUserPost(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.USERS);
		IUnitOfWork unitOfWork = null;
		IUser user = null;
		int editedUserId = -1;
		String userName = null;
		String firstName = null;
		String lastName = null;
		String institute = null;
		String eMailAddress = null;
		String password1 = null;
		String password2 = null;
		boolean locked = false;
		String apiKeyAction = null;
		boolean superAdmin = false;
		int oldNumberOfFailedLogins = -1;
		try {
			// authentication and super admin check
			user = AuthenticationController.ensureUserIsLoggedIn(request);
			if(user.isSuperAdmin() == false){
				throw new AuthenticationException("User " + user.getId() + " tried to create a new user!", false);	
			}
			
			// get parameter values
			editedUserId = RequestUtil.getQueryParameterPositiveInteger(locale, request, "userId", false);
			userName = RequestUtil.getQueryParameterOrNullIfEmpty(request, "userName");
			firstName = RequestUtil.getQueryParameterOrNullIfEmpty(request, "firstName");
			lastName = RequestUtil.getQueryParameterOrNullIfEmpty(request, "lastName");
			institute = RequestUtil.getQueryParameterOrNullIfEmpty(request, "institute");
			eMailAddress = RequestUtil.getQueryParameterOrNullIfEmpty(request, "eMailAddress");
			password1 = RequestUtil.getQueryParameterOrNullIfEmpty(request, "password1");
			password2 = RequestUtil.getQueryParameterOrNullIfEmpty(request, "password2");
			locked = RequestUtil.getBooleanFromRadioButtons(request, "accountState");
			apiKeyAction = RequestUtil.getStringFromRadioButtons(request, "apiKeyAction");
			superAdmin = RequestUtil.getBooleanFromRadioButtons(request, "superAdmin");
			
			unitOfWork = this.userDao.createUnitOfWork();
			unitOfWork.start();
			
			// check input values
			this.checkUserName(unitOfWork, locale, userName, true, editedUserId);
			this.checkFirstAndLastNameAndInstitute(locale, firstName, lastName, institute);
			this.checkMailAddress(locale, eMailAddress);
			boolean newPassword = false;
			if((password1 != null && password1.isEmpty() == false) || (password2 != null && password2.isEmpty() == false)){
				this.checkPassword(locale, password1, password2);
				newPassword = true;
			}
			
			// load user, who should be edited
			IUser editedUser = this.userDao.selectUser(null, editedUserId);
			if(editedUser == null){
				throw new BadRequestException("An user with the id " + editedUserId + " does not exist!");
			}
			
			// set changes
			editedUser.setUserName(userName);
			editedUser.setFirstName(firstName);
			editedUser.setLastName(lastName);
			editedUser.setInstitute(institute);
			editedUser.seteMailAddress(eMailAddress);
			if(newPassword){
				editedUser.setHashOfPassword(AuthenticationController.hashNewPassword(password1));	
			}
			editedUser.setLocked(locked);
			oldNumberOfFailedLogins = editedUser.getNumberOfFailedLogins();
			if(locked == false){
			    editedUser.setNumberOfFailedLogins(0);
			}
			editedUser.setApiKey(this.checkApiKeyActionAndDoIt(apiKeyAction, editedUser));	
			editedUser.setSuperAdmin(superAdmin);
			
			// save changes in database
			this.userDao.updateUser(unitOfWork, editedUser);
			
			// finish unit of work
			unitOfWork.finish();
			
			// create success message
			if(editedUser.getId() == user.getId() && editedUser.isSuperAdmin() == false){
				// Redirect to info and error page if user is no more a super admin. This can only happen, if an user edits himself.
				this.handleSuccess(locale, request, response, "WEB_USERS_EDIT_USER_PAGE_SUCCESS_MESSAGE", editedUser.getUserName(), Path.Web.LOGGED_IN_INFO_AND_ERROR);
			}else{
				// redirect to users list if user is furthermore super admin
				this.handleSuccess(locale, request, response, "WEB_USERS_EDIT_USER_PAGE_SUCCESS_MESSAGE", editedUser.getUserName(), Path.Web.USERS_LIST_USERS);
			}
			
		} catch (Exception e) {
			this.fillModel(model, userName, firstName, lastName, institute, eMailAddress, password1, password2, superAdmin);
			if(user != null){
				model.put(MODEL_VALUE_KEY_API_KEY, user.getApiKey());	
			}
			model.put(MODEL_VALUE_KEY_API_KEY_ACTION, setApiKeyActionToNothingIfInvalid(apiKeyAction));
			model.put(MODEL_VALUE_KEY_EDIT_USER_ID, editedUserId);
			model.put(MODEL_VALUE_KEY_NUMBER_OF_FAILED_LOGINS, String.valueOf(oldNumberOfFailedLogins));
            model.put(MODEL_VALUE_KEY_ACCOUNT_LOCKED, locked);
			this.handleExpception(locale, unitOfWork, e, model, request, response, "WEB_USERS_EDIT_USER_PAGE_ERROR_MESSAGE_COMMON_POST");
		}
		return ViewUtil.render(request, model, Path.WebTemplate.USERS_EDIT_USER, locale);
	}

	@Override
	public String handleRemoveUser(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.USERS);
		IUnitOfWork unitOfWork = null;
		try {
			// authentication and super admin check
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);
			if(user.isSuperAdmin() == false){
				throw new AuthenticationException("User " + user.getId() + " tried to delete a user!", false);
			}
			
			// get parameter
			int deleteUserId = RequestUtil.getQueryParameterPositiveInteger(locale, request, "userId", false);
			if(deleteUserId == -1){
				throw new AuthenticationException("User " + user.getId() + " tried to delete a user with incorrect parameter!", false);
			}
			
			// delete user
			unitOfWork = this.userDao.createUnitOfWork();
			unitOfWork.start();
			IUser deleteUser = this.userDao.selectUser(unitOfWork, deleteUserId);
			if(deleteUser == null){
				throw new AuthenticationException("User " + user.getId() + " tried to delete user " + deleteUserId + " who does not exist!", false);
			}
			
			List<IConcept> singleOwnerConceptsList = this.conceptDao.selectAllSingleOwnerConceptsOfUser(unitOfWork, deleteUserId);
			if(singleOwnerConceptsList.isEmpty()){
				this.userDao.deleteUser(unitOfWork, deleteUserId);
				if(deleteUser.getId() == user.getId()){
					// user delete's himself
					this.handleSuccess(locale, request, response, "WEB_USERS_DELETE_USER_HIMSELF_SUCCESS_MESSAGE", deleteUser.getUserName(), Path.Web.NOT_LOGGED_IN_INFO_AND_ERROR);
				}else{
					// user deletes another user
					this.handleSuccess(locale, request, response, "WEB_USERS_DELETE_USER_SUCCESS_MESSAGE", deleteUser.getUserName(), Path.Web.USERS_LIST_USERS);
				}
			}else{
				String message = null;
				if(deleteUser.getId() == user.getId()){
					// user want's to delete himself
					message = LanguageHandler.getWord(locale, "WEB_USERS_DELETE_USER_PAGE_MESSAGE_DELETE_HIMSELF_SINGLE_OWNER");
				}else{
					// user want's to delete another user
					message = LanguageHandler.getWord(locale, "WEB_USERS_DELETE_USER_PAGE_MESSAGE_DELETE_USER_IS_SINGLE_OWNER");
					message = TemplateFiller.fillTemplate(message, deleteUser.getUserName(), deleteUser.getUserName());
				}
				model.put(MODEL_VALUE_KEY_INFO_MESSAGE, message);
				model.put(MODEL_VALUE_KEY_SINGLE_OWNER_CONCEPTS, singleOwnerConceptsList);
			}
			
			unitOfWork.finish();
		} catch (Exception e) {
				this.handleExpception(locale, unitOfWork, e, model, request, response, "WEB_USERS_DELETE_USER_ERROR_MESSAGE_COMMON");
		}
		return ViewUtil.render(request, model, Path.WebTemplate.USERS_DELETE_USER, locale);
	}

	@Override
	public String getMyDataPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.MY_ACCOUNT);
		try {
			// authentication and super admin check
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);
			
			// fill model
			this.fillModel(model, user.getUserName(), user.getFirstName(), user.getLastName(), user.getInstitute(), user.geteMailAddress(), null, null, user.isSuperAdmin());
			model.put(MODEL_VALUE_KEY_API_KEY, user.getApiKey());	
			model.put(MODEL_VALUE_KEY_API_KEY_ACTION, MODEL_VALUE_KEY_API_KEY_ACTION_VALUE_NOTHING);
			
		} catch (Exception e) {
			this.handleExpception(locale, null, e, model, request, response, "WEB_USERS_EDIT_USER_PAGE_ERROR_MESSAGE_COMMON_GET");
		}
		return ViewUtil.render(request, model, Path.WebTemplate.USERS_MY_DATA, locale);
	}

	@Override
	public String handleMyDataPost(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.MY_ACCOUNT);
		IUnitOfWork unitOfWork = null;
		IUser user = null;
		String userName = null;
		String firstName = null;
		String lastName = null;
		String institute = null;
		String eMailAddress = null;
		String oldPassword = null;
		String newPassword1 = null;
		String newPassword2 = null;
		String apiKeyAction = null;
		boolean superAdmin = false;
		try {
			// authentication and super admin check
			user = AuthenticationController.ensureUserIsLoggedIn(request);
			
			// get parameter values
			userName = RequestUtil.getQueryParameterOrNullIfEmpty(request, "userName");
			firstName = RequestUtil.getQueryParameterOrNullIfEmpty(request, "firstName");
			lastName = RequestUtil.getQueryParameterOrNullIfEmpty(request, "lastName");
			institute = RequestUtil.getQueryParameterOrNullIfEmpty(request, "institute");
			eMailAddress = RequestUtil.getQueryParameterOrNullIfEmpty(request, "eMailAddress");
			oldPassword = RequestUtil.getQueryParameterOrNullIfEmpty(request, "oldPassword");
			newPassword1 = RequestUtil.getQueryParameterOrNullIfEmpty(request, "newPassword1");
			newPassword2 = RequestUtil.getQueryParameterOrNullIfEmpty(request, "newPassword2");
			if(user.isSuperAdmin()){
				apiKeyAction = RequestUtil.getStringFromRadioButtons(request, "apiKeyAction");
				superAdmin = RequestUtil.getBooleanFromRadioButtons(request, "superAdmin");	
			}
			
			unitOfWork = this.userDao.createUnitOfWork();
			unitOfWork.start();
			
			// check input values
			this.checkUserName(unitOfWork, locale, userName, true, user.getId());
			this.checkFirstAndLastNameAndInstitute(locale, firstName, lastName, institute);
			this.checkMailAddress(locale, eMailAddress);
			boolean newPassword = false;
			if((oldPassword != null && oldPassword.isEmpty() == false) || (newPassword1 != null && newPassword1.isEmpty() == false) || (newPassword2 != null && newPassword2.isEmpty() == false)){
				if(oldPassword == null || oldPassword.isEmpty()){
					String userMessage = LanguageHandler.getWord(locale, "WEB_ACCOUNT_MY_DATA_PAGE_ERROR_MESSAGE_OLD_PASSWORD_MISSING");
					throw new BadRequestException("Old password is missing!", null, userMessage);
				}
				if(AuthenticationController.authenticate(oldPassword, user) == false){
					String userMessage = LanguageHandler.getWord(locale, "WEB_ACCOUNT_MY_DATA_PAGE_ERROR_MESSAGE_OLD_PASSWORD_WRONG");
					throw new BadRequestException("Old password is wrong!", null, userMessage);
				}
				this.checkPassword(locale, newPassword1, newPassword2);
				newPassword = true;
			}
			
			// set changes
			user.setUserName(userName);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setInstitute(institute);
			user.seteMailAddress(eMailAddress);
			if(newPassword){
				user.setHashOfPassword(AuthenticationController.hashNewPassword(newPassword1));	
			}
			if(user.isSuperAdmin()){
				user.setApiKey(this.checkApiKeyActionAndDoIt(apiKeyAction, user));	
				user.setSuperAdmin(superAdmin);	
			}
			
			// save changes in database
			this.userDao.updateUser(unitOfWork, user);
			
			unitOfWork.finish();
			
			model.put(MODEL_VALUE_KEY_INFO_MESSAGE, LanguageHandler.getWord(locale, "WEB_ACCOUNT_MY_DATA_PAGE_SUCCESS_MESSAGE"));
			
		} catch (Exception e) {
			this.handleExpception(locale, null, e, model, request, response, "WEB_USERS_EDIT_USER_PAGE_ERROR_MESSAGE_COMMON_GET");
		}finally{
			this.fillModel(model, userName, firstName, lastName, institute, eMailAddress, null, null, superAdmin);
			model.put(MODEL_VALUE_KEY_API_KEY, user.getApiKey());	
			model.put(MODEL_VALUE_KEY_API_KEY_ACTION, setApiKeyActionToNothingIfInvalid(apiKeyAction));
		}
		return ViewUtil.render(request, model, Path.WebTemplate.USERS_MY_DATA, locale);
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

	/*private void handleExpception(Locale locale, IUnitOfWork unitOfWork, Exception exception, Map<String, Object> model, Request request, Response response, String userMessageTemplateKey){
	    exception.printStackTrace();
		logger.error(exception.getMessage());
		
		this.abortUnitOfWork(unitOfWork);
		
		this.setResponseStatusCodeInErrorCase(response, exception);
		
		if(exception instanceof AuthenticationException){
			this.handleUserIsNotLoggedIn(request, response, (AuthenticationException)exception);	
		}else{
			// create user message if not existing
			String userMessage = null;
			if(exception instanceof RegistryServerException){
				userMessage = ((RegistryServerException)exception).getUserMessage();
			}
			if(userMessage == null || userMessage.isEmpty()){
				userMessage = LanguageHandler.getWord(locale, userMessageTemplateKey);
			}
			
			// insert user message into model
			model.put(MODEL_VALUE_KEY_ERROR_MESSAGE, userMessage);	
		}
	}*/
	
	private String setApiKeyActionToNothingIfInvalid(String apiKeyAction) {
		if(apiKeyAction == null || (apiKeyAction.equals(MODEL_VALUE_KEY_API_KEY_ACTION_VALUE_NOTHING) == false && apiKeyAction.equals(MODEL_VALUE_KEY_API_KEY_ACTION_VALUE_DELETE) == false && apiKeyAction.equals(MODEL_VALUE_KEY_API_KEY_ACTION_VALUE_NEW) == false)){
			apiKeyAction = MODEL_VALUE_KEY_API_KEY_ACTION_VALUE_NOTHING;
		}
		return apiKeyAction;
	}
	
	private boolean checkUserName(IUnitOfWork unitOfWork, Locale locale, String userName, boolean checkAlreadyExists, int userId) throws BadRequestException, DataAccessException {
		System.out.println("in checkUserName");
		if(userName == null || userName.isEmpty()){
			String message = "User name is empty!";
			String userMessage = LanguageHandler.getWord(locale, "WEB_USERS_NEW_AND_EDIT_USER_PAGE_ERROR_MESSAGE_TYPE_IN_USER_NAME");
			throw new BadRequestException(message, null, userMessage);
		}
		if(checkAlreadyExists){
			IUser user = this.userDao.selectUser(unitOfWork, userName);
			if(user != null && user.getId() != userId){
				String message = "User name already exists!";
				String userMessage = LanguageHandler.getWord(locale, "WEB_USERS_NEW_AND_EDIT_USER_PAGE_ERROR_MESSAGE_USER_NAME_ALREADY_EXISTS");
				userMessage = TemplateFiller.fillTemplate(userMessage, userName);
				throw new BadRequestException(message, null, userMessage);
			}	
		}
		return false;
	}
	
	private void checkFirstAndLastNameAndInstitute(Locale locale, String firstName, String lastName, String institute) throws BadRequestException {
		boolean instituteOk = false;
		if(institute != null && institute.isEmpty() == false){
			instituteOk = true;
		}
		boolean namesOk = false;
		if(firstName != null && firstName.isEmpty() == false && lastName != null && lastName.isEmpty() == false){
			namesOk = true;
		}
		if(instituteOk == false && namesOk == false){
			String message = "Institute or first and last name missing!";
			String userMessage = LanguageHandler.getWord(locale, "WEB_USERS_NEW_AND_EDIT_USER_PAGE_ERROR_MESSAGE_TYPE_IN_NAME_OR_INSTITUTE");
			throw new BadRequestException(message, null, userMessage);
		}
	}
	
	private void checkMailAddress(Locale locale, String mailAddress) throws BadRequestException {
		if(mailAddress == null || mailAddress.isEmpty()){
			String message = "E-Mail address is missing!";
			String userMessage = LanguageHandler.getWord(locale, "WEB_USERS_NEW_AND_EDIT_USER_PAGE_ERROR_MESSAGE_TYPE_IN_MAIL_ADDRESS");
			throw new BadRequestException(message, null, userMessage);
		}
		boolean valid = true;
		if(mailAddress.contains("@") == false){
			System.out.println("if 1");
			valid = false;
		}
		if(mailAddress.contains(".") == false){
			System.out.println("if 2");
			valid = false;
		}
		if(mailAddress.length() < 5){
			System.out.println("if 3");
			valid = false;
		}
		int indexAt = mailAddress.indexOf("@");
		int indexLastDot = mailAddress.indexOf(".", indexAt);
		System.out.println("indexAt = " + indexAt);
		System.out.println("indexLastDot = " + indexLastDot);
		if(indexAt >= indexLastDot){
			System.out.println("if 4");
			valid = false;
		}
		if(indexAt == 0 || indexLastDot - 1 == indexAt || indexLastDot == mailAddress.length() - 1){
			System.out.println("if 5");
			valid = false;
		}
		if(valid == false){
			String message = "E-Mail address is invalid!";
			String userMessage = LanguageHandler.getWord(locale, "WEB_USERS_NEW_AND_EDIT_USER_PAGE_ERROR_MESSAGE_MAIL_ADDRESS_INVALID");
			userMessage = TemplateFiller.fillTemplate(userMessage, mailAddress);
			throw new BadRequestException(message, null, userMessage);
		}
	}
	
	private void checkPassword(Locale locale, String password1, String password2) throws BadRequestException {
		if(password1 == null || password1 .isEmpty() || password2 == null || password2.isEmpty()){
			String message = "One or both passwords are missing!";
			String userMessage = LanguageHandler.getWord(locale, "WEB_USERS_NEW_AND_EDIT_USER_AND_MY_DATA_PAGE_ERROR_MESSAGE_NEW_PASSWORD_MISSING");
			throw new BadRequestException(message, null, userMessage);
		}
		if(password1.equals(password2) == false){
			String message = "password1 and password2 are not equal!";
			String userMessage = LanguageHandler.getWord(locale, "WEB_USERS_NEW_AND_EDIT_USER_AND_MY_DATA_PAGE_ERROR_MESSAGE_NEW_PASSWORD_REPEAT_WRONG");
			throw new BadRequestException(message, null, userMessage);
		}
		if(AuthenticationController.isNewPasswordSecureEnough(password1) == false){
			String message = "Password is not secure enough!";
			String userMessage = LanguageHandler.getWord(locale, "WEB_USERS_NEW_AND_EDIT_USER_AND_MY_DATA_PAGE_ERROR_MESSAGE_NEW_PASSWORD_NOT_SECURE");
			throw new BadRequestException(message, null, userMessage);
		}
	}
	
	private String checkApiKeyActionAndDoIt(String apiKeyAction, IUser editedUser) throws BadRequestException, DataAccessException {
		String apiKey;
		if(apiKeyAction.equals(MODEL_VALUE_KEY_API_KEY_ACTION_VALUE_NOTHING)){
			apiKey = editedUser.getApiKey();
		}else if(apiKeyAction.equals(MODEL_VALUE_KEY_API_KEY_ACTION_VALUE_DELETE)){
			apiKey = null;
		}else if(apiKeyAction.equals(MODEL_VALUE_KEY_API_KEY_ACTION_VALUE_NEW)){
			apiKey = AuthenticationController.generateApiKey();
		}else{
			throw new BadRequestException("The value (" + apiKeyAction + ") of the parameter apiKeyAction is invalid!");
		}
		return apiKey;
	}
	
	private void fillModel(Map<String, Object> model, String userName, String firstName, String lastName, String institute, String eMailAddress, String password1, String password2, boolean superAdmin){
		model.put(MODEL_VALUE_KEY_USER_NAME, userName);
		model.put(MODEL_VALUE_KEY_FIRST_NAME, firstName);
		model.put(MODEL_VALUE_KEY_LAST_NAME, lastName);
		model.put(MODEL_VALUE_KEY_INSTITUTE, institute);
		model.put(MODEL_VALUE_KEY_MAIL_ADDRESS, eMailAddress);
		if(password1 != null){
			model.put(MODEL_VALUE_KEY_PASSWORD_1, password1);	
		}
		if(password2 != null){
			model.put(MODEL_VALUE_KEY_PASSWORD_2, password2);	
		}
		model.put(MODEL_VALUE_KEY_SUPER_ADMIN, superAdmin);
	}






	


	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
