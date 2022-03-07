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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hdm.databaseaccess.DaoFactory;
import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IGroupDao;
import de.hdm.databaseaccess.IUnitOfWork;
import de.hdm.databaseaccess.IUserDao;
import de.hdm.datatypes.Group;
import de.hdm.datatypes.GroupMember;
import de.hdm.datatypes.IGroup;
import de.hdm.datatypes.IGroupMember;
import de.hdm.datatypes.IUser;
import de.hdm.helpers.TemplateFiller;
import de.hdm.multiplelanguages.LanguageHandler;
import de.hdm.server.AuthenticationController;
import de.hdm.server.AuthenticationException;
import de.hdm.server.BadRequestException;
import de.hdm.server.Path;
import de.hdm.server.RequestUtil;
import de.hdm.server.SessionUtil;
import spark.Request;
import spark.Response;

public class HtmlGroupController extends HtmlController implements IHtmlGroupController {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Logger.
	 */
	private final static Logger logger = LogManager.getLogger(HtmlGroupController.class);
	
	private static final String MODEL_VALUE_KEY_GROUP_NAME = "groupName";
	
	private static final String MODEL_VALUE_KEY_GROUP_MEMBERS = "groupMembers";
	
	private static final String MODEL_VALUE_KEY_GROUP_ID = "groupId";
	
	private static final String MODEL_VALUE_KEY_GROUP_MEMBER_ROWS = "groupMemberRows";
	
	private static final String MODEL_VALUE_KEY_GROUPS = "groups";
	
	private IGroupDao groupDao;

	private IUserDao userDao;
	


	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	public HtmlGroupController(){
		this.groupDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createGroupDao();
		this.userDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createUserDao();
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
	public String getGroupsPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.GROUPS);
		try {
			// authentication
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);
			
			// insert success message
			this.handleSuccessMessage(request, model);
			
			// rights check and groups loading
			List<IGroup> groups;
			if(user.isSuperAdmin()){
				// load all groups
				groups = this.groupDao.selectAllGroups(null);
			}else{
				// load his groups
				groups = this.groupDao.selectGroupsWhereUserIsAdmin(null, user.getId());
				if(groups.isEmpty()){
					throw new AuthenticationException("User " + user.getId() + " tried to which does not exist!", false);	
				}
			}
			model.put(MODEL_VALUE_KEY_GROUPS, groups);
		} catch (Exception e) {
			this.handleExpception(locale, null, e, model, request, response, "WEB_USERS_LIST_PAGE_ERROR_MESSAGE_COMMON");
		}
		return ViewUtil.render(request, model, Path.WebTemplate.GROUPS_LIST, locale);
	}

	@Override
	public String getNewGroupPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.GROUPS);
		try {
			// authentication and rights check
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);
			if(user.isSuperAdmin() == false){
				throw new AuthenticationException("User " + user.getId() + " is no super admin, thus he is not allowed to create a new group!", false);
			}
		} catch (Exception e) {
			this.handleExpception(locale, null, e, model, request, response, "WEB_GROUPS_NEW_GROUP_PAGE_ERROR_MESSAGE_COMMON_GET");
		}
		return ViewUtil.render(request, model, Path.WebTemplate.GROUPS_NEW_GROUP, locale);
	}

	@Override
	public String handleNewGroupPost(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.GROUPS);
		String groupName = null;
		String groupMembersAsString = null;
		IUnitOfWork unitOfWork = null;
		try {
			// authentication and rights check
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);
			if(user.isSuperAdmin() == false){
				throw new AuthenticationException("User " + user.getId() + " is no super admin, thus he is not allowed to create a new group!", false);
			}
			
			// get parameters
			groupName = RequestUtil.getQueryParameterGroupName(locale, request);
			groupMembersAsString = RequestUtil.getQueryParameterGroupMembers(locale, request);
			
			// start unit of work
			unitOfWork = this.groupDao.createUnitOfWork();
			unitOfWork.start();
			
			// check group name
			this.checkGroupName(unitOfWork, groupName, null, locale);
			
			// store new group in database
			List<IGroupMember> groupMembers = this.createGroupMembersFromTextArea(groupMembersAsString, locale);
			IGroup group = new Group(groupName, groupMembers);
			this.groupDao.insertGroup(unitOfWork, group);
			unitOfWork.finish();
			
			// create success message and redirect to groups list page
			String successMessage = LanguageHandler.getWord(locale, "WEB_GROUPS_NEW_GROUP_SUCCESS_MESSAGE");
			successMessage = TemplateFiller.fillTemplate(successMessage, groupName);
			SessionUtil.setSuccessMessage(request, successMessage);
			response.redirect(Path.Web.GROUPS_LIST_GROUPS);
			
		} catch(Exception e){
			model.put(MODEL_VALUE_KEY_GROUP_NAME, groupName);
			model.put(MODEL_VALUE_KEY_GROUP_MEMBERS, groupMembersAsString);
			this.handleExpception(locale, unitOfWork, e, model, request, response, "WEB_GROUPS_NEW_GROUP_PAGE_ERROR_MESSAGE_COMMON_POST");
		}
		return ViewUtil.render(request, model, Path.WebTemplate.GROUPS_NEW_GROUP, locale);
	}
	
	@Override
	public String getEditGroupPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.GROUPS);
		try {
			// authentication
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);
			
			// get parameters
			int groupId = RequestUtil.getQueryParameterGroupId(locale, request);
			if(groupId == -1){
				throw new AuthenticationException("User " + user.getId() + " called the edit group page without the query parameter group id!", false);
			}
			
			// load group, which should be edited
			IGroup group = this.groupDao.selectGroupById(null, groupId);
			if(group == null){
				throw new BadRequestException("An group with the id " + groupId + " does not exist!");
			}
			
			// edit group right check
			if(user.isSuperAdmin() == false && group.isUserGroupAdmin(user.getId())){
				throw new AuthenticationException("User " + user.getId() + " is neither super admin nor group admin, thus he is not allowed to edit the group " + group.getId() + "!", false);	
			}
			
			// fill model
			model.put(MODEL_VALUE_KEY_GROUP_ID, group.getId());
			model.put(MODEL_VALUE_KEY_GROUP_NAME, group.getName());
			if(group.getMembers().isEmpty() == false){
				model.put(MODEL_VALUE_KEY_GROUP_MEMBERS, this.groupMembersToStringValue(group.getMembers()));
				model.put(MODEL_VALUE_KEY_GROUP_MEMBER_ROWS, group.getMembers().size() + 3);
			}else{
				model.put(MODEL_VALUE_KEY_GROUP_MEMBER_ROWS, 10);
			}
		} catch (Exception e) {
			this.handleExpception(locale, null, e, model, request, response, "WEB_GROUPS_EDIT_GROUP_PAGE_ERROR_MESSAGE_COMMON_GET");
		}
		return ViewUtil.render(request, model, Path.WebTemplate.GROUPS_EDIT_GROUP, locale);
	}
	
	@Override
	public String handleEditGroupPost(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.GROUPS);
		int groupId = -1;
		String groupName = null;
		String groupMembersAsString = null;
		IUnitOfWork unitOfWork = null;
		try {
			// authentication
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);
			
			// get parameters
			groupId = RequestUtil.getQueryParameterGroupId(locale, request);
			if(groupId == -1){
				throw new AuthenticationException("User " + user.getId() + " called the edit group page without the query parameter group id!", false);
			}
			groupName = RequestUtil.getQueryParameterGroupName(locale, request);
			groupMembersAsString = RequestUtil.getQueryParameterGroupMembers(locale, request);
			
			// load group
			IGroup group = this.groupDao.selectGroupById(null, groupId);
			if(group == null){
				throw new BadRequestException("An group with the id " + groupId + " does not exist!");
			}
			
			// check edit right
			if(user.isSuperAdmin() == false && group.isUserGroupAdmin(user.getId()) == false){
				throw new AuthenticationException("User " + user.getId() + " is neither super admin nor group admin, thus he is not allowed to edit the group " + group.getId() + "!", false);
			}
			
			// start unit of work
			unitOfWork = this.groupDao.createUnitOfWork();
			unitOfWork.start();
			
			// check group name
			this.checkGroupName(unitOfWork, groupName, group, locale);
			
			// store edited group in database
			group.setName(groupName);
			group.setMembers(this.createGroupMembersFromTextArea(groupMembersAsString, locale));
			this.groupDao.updateGroup(unitOfWork, group);
			unitOfWork.finish();
			
			// create success message
			if(user.isSuperAdmin() || this.groupDao.isUserAdminOfAtLeastOneGroup(unitOfWork, user.getId())){
				this.handleSuccess(locale, request, response, "WEB_GROUPS_EDIT_GROUP_SUCCESS_MESSAGE", groupName, Path.Web.GROUPS_LIST_GROUPS);
			}else{
				this.handleSuccess(locale, request, response, "WEB_GROUPS_EDIT_GROUP_SUCCESS_MESSAGE", groupName, Path.Web.LOGGED_IN_INFO_AND_ERROR);
			}
			
		} catch (Exception e) {
			model.put(MODEL_VALUE_KEY_GROUP_ID, groupId);
			model.put(MODEL_VALUE_KEY_GROUP_NAME, groupName);
			model.put(MODEL_VALUE_KEY_GROUP_MEMBERS, groupMembersAsString);
			this.handleExpception(locale, unitOfWork, e, model, request, response, "WEB_GROUPS_EDIT_GROUP_PAGE_ERROR_MESSAGE_COMMON_POST");
		}
		return ViewUtil.render(request, model, Path.WebTemplate.GROUPS_EDIT_GROUP, locale);
	}
	
	@Override
	public String handleRemoveGroup(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.GROUPS);
		IUnitOfWork unitOfWork = null;
		try {
			// authentication
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);
			
			// get parameter
			int deleteGroupId = RequestUtil.getQueryParameterPositiveInteger(locale, request, "groupId", false);
			if(deleteGroupId == -1){
				throw new AuthenticationException("User " + user.getId() + " tried to delete a group with incorrect parameter!", false);
			}
			
			// delete group and rights check
			unitOfWork = this.groupDao.createUnitOfWork();
			unitOfWork.start();
			IGroup deleteGroup = this.groupDao.selectGroupById(unitOfWork, deleteGroupId);
			if(deleteGroup == null){
				throw new AuthenticationException("User " + user.getId() + " tried to delete group " + deleteGroupId + " which does not exist!", false);
			}
			if(user.isSuperAdmin() || deleteGroup.isUserGroupAdmin(user.getId())){
				this.groupDao.deleteGroup(unitOfWork, deleteGroupId);
			}else{
				throw new AuthenticationException("User " + user.getId() + " is neither group admin nor group admin of group " + deleteGroupId + ". Despite he tried to delete this group!", false);
			}
			
			// create success message
			if(user.isSuperAdmin() || this.groupDao.isUserAdminOfAtLeastOneGroup(unitOfWork, user.getId())){
				this.handleSuccess(locale, request, response, "WEB_GROUPS_DELETE_GROUP_SUCCESS_MESSAGE", deleteGroup.getName(), Path.Web.GROUPS_LIST_GROUPS);
			}else{
				this.handleSuccess(locale, request, response, "WEB_GROUPS_DELETE_GROUP_SUCCESS_MESSAGE", deleteGroup.getName(), Path.Web.LOGGED_IN_INFO_AND_ERROR);
			}
			
			unitOfWork.finish();
			
		} catch (Exception e) {
			this.handleExpception(locale, unitOfWork, e, model, request, response, "WEB_GROUPS_DELETE_GROUP_ERROR_MESSAGE_COMMON");
		}
		return ViewUtil.render(request, model, Path.WebTemplate.LOGGED_IN_INFO_AND_ERROR, locale);
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

	private void checkGroupName(IUnitOfWork unitOfWork, String groupName, IGroup group, Locale locale) throws BadRequestException, DataAccessException{
		if(groupName.isEmpty()){
			String message = "The groupName is empty!";
			String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(locale, "WEB_GROUPS_NEW_GROUP_PAGE_ERROR_MESSAGE_TYPE_IN_A_GROUP_NAME"), groupName);
			throw new BadRequestException(message, null, userMessage);
		}
		if(group == null || groupName.equals(group.getName()) == false){
			if(this.groupDao.containsGroupName(unitOfWork, groupName)){
				String message = "The groupName = " + groupName + " already exists!";
				String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(locale, "WEB_GROUPS_NEW_GROUP_PAGE_ERROR_MESSAGE_GROUP_NAME_ALREADY_EXISTS"), groupName);
				throw new BadRequestException(message, null, userMessage);
			}	
		}
	}
	
	private List<IGroupMember> createGroupMembersFromTextArea(String groupMembersAsString, Locale locale) throws BadRequestException, DataAccessException{
		Map<String, IUser> users = this.getAllUserNamesAndUsers();
		List<IGroupMember> groupMembers = new ArrayList<IGroupMember>();
		String regexLine = "[^ ]+([ ]admin)?";
		
		String[] lines = groupMembersAsString.split(new String(new char[]{13, 10}));
		
		if(groupMembersAsString.isEmpty() == false && groupMembersAsString.equals("username [admin][linebreak]") == false){
			for(String line : lines){
				
				// check line format
				if(line.matches(regexLine) == false){
					String message = "Invalid format in line = " + line + "!";
					String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(locale, "WEB_GROUPS_NEW_GROUP_PAGE_ERROR_MESSAGE_INVALID_LINE"), line);
					throw new BadRequestException(message, null, userMessage);
				}
				
				// check whether group member should be group admin
				boolean admin = false;
				String userName = line;
				if(line.contains(" admin")){
					userName = line.substring(0, line.indexOf(" "));
					admin = true;
				}
				
				// check whether group member exists or not
				if(users.containsKey(userName)){
					groupMembers.add(new GroupMember(users.get(userName).getId(), admin));	
				}else{
					String message = "Unknown userName = " + userName + "!";
					String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(locale, "WEB_GROUPS_NEW_GROUP_PAGE_ERROR_MESSAGE_UNKNOWN_USER_NAME"), userName);
					throw new BadRequestException(message, null, userMessage);
				}
				
			}// end for loop
		}
		return groupMembers;
	}
	
	private Object groupMembersToStringValue(List<IGroupMember> members) throws DataAccessException {
		Map<Integer, IUser> users = this.getAllUserIdsAndUsers();
		String groupMembersAsString = "";
		for(IGroupMember groupMember : members){
			IUser user = users.get(groupMember.getUserId());
			if(user != null){
				groupMembersAsString += user.getUserName();
				if(groupMember.isGroupAdmin()){
					groupMembersAsString += " admin";
				}
				groupMembersAsString += "\n";
			}
		}
		return groupMembersAsString;
	}
	
	private Map<String, IUser> getAllUserNamesAndUsers() throws DataAccessException{
		List<IUser> usersAsList = this.userDao.selectUsers(null);
		Map<String, IUser> usersAsMap = new HashMap<String, IUser>();
		for(IUser user : usersAsList){
			usersAsMap.put(user.getUserName(), user);
		}
		return usersAsMap;
	}
	
	private Map<Integer, IUser> getAllUserIdsAndUsers() throws DataAccessException{
		List<IUser> usersAsList = this.userDao.selectUsers(null);
		Map<Integer, IUser> usersAsMap = new HashMap<Integer, IUser>();
		for(IUser user : usersAsList){
			usersAsMap.put(user.getId(), user);
		}
		return usersAsMap;
	}
	
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




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
