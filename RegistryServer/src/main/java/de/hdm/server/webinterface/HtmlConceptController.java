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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hdm.configuration.MyProperties;
import de.hdm.databaseaccess.DaoFactory;
import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IConceptDao;
import de.hdm.databaseaccess.IGeneratedConceptIdDao;
import de.hdm.databaseaccess.IGroupAccessRightDao;
import de.hdm.databaseaccess.IGroupDao;
import de.hdm.databaseaccess.ILanguageDao;
import de.hdm.databaseaccess.ILogDao;
import de.hdm.databaseaccess.IOwnershipDao;
import de.hdm.databaseaccess.IUnitOfWork;
import de.hdm.databaseaccess.IUserDao;
import de.hdm.datatypes.ActionEnum;
import de.hdm.datatypes.Concept;
import de.hdm.datatypes.DataTypeEnum;
import de.hdm.datatypes.Definition;
import de.hdm.datatypes.Example;
import de.hdm.datatypes.GroupAccessRight;
import de.hdm.datatypes.IConcept;
import de.hdm.datatypes.IContent;
import de.hdm.datatypes.IGroup;
import de.hdm.datatypes.IGroupAccessRight;
import de.hdm.datatypes.ILog;
import de.hdm.datatypes.IUser;
import de.hdm.datatypes.IValueSpace;
import de.hdm.datatypes.Log;
import de.hdm.datatypes.Name;
import de.hdm.datatypes.Note;
import de.hdm.datatypes.SubTypeEnum;
import de.hdm.datatypes.TypeEnum;
import de.hdm.datatypes.ValueSpace;
import de.hdm.helpers.Checker;
import de.hdm.helpers.TemplateFiller;
import de.hdm.multiplelanguages.LanguageHandler;
import de.hdm.server.AuthenticationController;
import de.hdm.server.AuthenticationException;
import de.hdm.server.BadRequestException;
import de.hdm.server.ConceptComparer;
import de.hdm.server.IConceptComparer;
import de.hdm.server.Path;
import de.hdm.server.RequestUtil;
import de.hdm.server.SessionUtil;
import spark.Request;
import spark.Response;

public class HtmlConceptController extends HtmlController implements IHtmlConceptController {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Logger.
	 */
	private final static Logger logger = LoggerFactory.getLogger(HtmlConceptController.class);

	private static final String MODEL_VALUE_KEY_CONCEPTS = "concepts";

	private static final String MODEL_VALUE_KEY_GROUPS = "groups";

	private static final String MODEL_VALUE_KEY_USERS_GROUPS = "usersGroups";

	private static final String MODEL_VALUE_KEY_LOCALE = "locale";

	private static final String MODEL_VALUE_KEY_CONCEPT_ID = "id";

	private static final String MODEL_VALUE_KEY_LANGUAGE_CODE_NAMES = "languageCodeNames";

	private static final String MODEL_VALUE_KEY_NAMES = "names";

	private static final String MODEL_VALUE_KEY_LANGUAGE_CODE_DEFINITIONS = "languageCodeDefinitions";

	private static final String MODEL_VALUE_KEY_DEFINITIONS = "definitions";

	private static final String MODEL_VALUE_KEY_TYPE = "type";

	private static final String MODEL_VALUE_KEY_SUB_TYPE = "subType";

	private static final String MODEL_VALUE_KEY_DATA_TYPE = "dataType";

	private static final String MODEL_VALUE_KEY_ORIGIN = "origin";

	private static final String MODEL_VALUE_KEY_VALUE_SPACE = "valueSpace";

	private static final String MODEL_VALUE_KEY_VALUE_SPACE_VALID = "valueSpaceValid";

	private static final String MODEL_VALUE_KEY_OWNERS = "owners";

	private static final String MODEL_VALUE_KEY_AUTHORS = "authors";

	private static final String MODEL_VALUE_KEY_TRANSFORMATIONS = "transformations";

	private static final String MODEL_VALUE_KEY_TRANSFORMES_CONCEPT_ID_READ_RIGHT_MAP = "transformesReadRightsMap";

	private static final String MODEL_VALUE_KEY_TRANSFORMED_BY_CONCEPT_ID_READ_RIGHT_MAP = "transformedByReadRightsMap";

	private static final String MODEL_VALUE_KEY_REFINEMENTS = "refinements";

	private static final String MODEL_VALUE_KEY_REFINES_CONCEPT_ID_READ_RIGHT_MAP = "refinesReadRightsMap";

	private static final String MODEL_VALUE_KEY_REFINED_BY_CONCEPT_ID_READ_RIGHT_MAP = "refinedByReadRightsMap";

	private static final String MODEL_VALUE_KEY_LANGUAGE_CODE_NOTES = "languageCodeNotes";

	private static final String MODEL_VALUE_KEY_NOTES = "notes";

	private static final String MODEL_VALUE_KEY_LANGUAGE_CODE_EXAMPLES = "languageCodeExamples";

	private static final String MODEL_VALUE_KEY_EXAMPLES = "examples";

	private static final String MODEL_VALUE_KEY_READ_RIGHTS = "readRights";

	private static final String MODEL_VALUE_KEY_UPDATE_RIGHTS = "updateRights";

	private static final String MODEL_VALUE_KEY_DELETE_RIGHTS = "deleteRights";

	private static final String MODEL_VALUE_KEY_CHANGE_RIGHTS_RIGHTS = "changeRightsRights";

	private static final String MODEL_VALUE_KEY_EDIT_MODE = "editMode";

	private static final String MODEL_VALUE_KEY_GROUP_ACCESS_RIGHTS_ENABLED = "groupAccessRightsEnabled";

	private static final String MODEL_VALUE_KEY_CONCEPT = "concept";

	private static final String MODEL_VALUE_KEY_LOGS = "logs";

	private static final String MODEL_VALUE_KEY_USER_ID_TO_USER_MAPPING = "usersMap";

	private static final String MODEL_VALUE_KEY_REFINED_CONCEPTS = "refinedConcepts";

	private static final String MODEL_VALUE_KEY_TRANSFORMED_CONCEPTS = "transformedConcepts";

	private static final String MODEL_VALUE_KEY_SEARCH_TERM = "searchTerm";

	private static final String MODEL_VALUE_KEY_SEARCH_OPTION = "searchOption";

	private static final String MODEL_VALUE_KEY_SEARCH_OPTION_VALUE_ID = "id";

	private static final String MODEL_VALUE_KEY_SEARCH_OPTION_VALUE_NAME = "name";

	private static final String MODEL_VALUE_KEY_SEARCH_OPTION_VALUE_ID_AND_NAME = "idAndName";

	private static final String MODEL_VALUE_KEY_UPDATE_RIGHT = "updateRight";

	/**
	 * Data access object for the concepts.
	 */
	private IConceptDao conceptDao;

	/**
	 * Data access object for the groups.
	 */
	private IGroupDao groupDao;

	/**
	 * Data access object for the generated concept ids.
	 */
	private IGeneratedConceptIdDao generatedConceptIdDao;

	/**
	 * Data access object for the users.
	 */
	private IUserDao userDao;

	/**
	 * Data access object for the languages.
	 */
	private ILanguageDao languageDao;

	/**
	 * Data access object for the logs.
	 */
	private ILogDao logDao;

	/**
	 * Data access object for the group access rights.
	 */
	private IGroupAccessRightDao groupAccessRightDao;

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
	 * Creates a new HtmlConceptController and initializes the required dependencies.
	 */
	public HtmlConceptController() {
		this.conceptDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createConceptDao();
		this.groupDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createGroupDao();
		this.groupAccessRightDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createGroupAccessRightDao();
		this.ownershipDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createOwnershipDao();
		this.generatedConceptIdDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createGeneratedConceptIdDao();
		this.userDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createUserDao();
		this.languageDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createLanguageDao();
		this.logDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createLogDao();
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
	public String getAllConceptsPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.CONCEPTS);

		try {
			// authentication
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);

			List<IConcept> concepts = this.conceptDao.selectAllConcepts(null, user.getId());

			List<IGroup> usersGroups = this.groupDao.selectAllGroupsWhereUserIsMember(null, user.getId());

			model.put(MODEL_VALUE_KEY_LOCALE, locale);
			model.put(MODEL_VALUE_KEY_CONCEPTS, concepts);
			model.put(MODEL_VALUE_KEY_USERS_GROUPS, usersGroups);
		} catch (Exception e) {
			this.handleExpception(locale, null, e, model, request, response, "WEB_CONCEPTS_LIST_ALL_PAGE_ERROR_MESSAGE_COMMON");
		}

		return ViewUtil.render(request, model, Path.WebTemplate.CONCEPTS_LIST_ALL, locale);
	}

	@Override
	public String getMyConceptsPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.CONCEPTS);

		try {
			// authentication
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);

			List<IConcept> concepts = this.conceptDao.selectAllConceptsOfUser(null, user.getId());

			List<IGroup> usersGroups = this.groupDao.selectAllGroupsWhereUserIsMember(null, user.getId());

			model.put(MODEL_VALUE_KEY_LOCALE, locale);
			model.put(MODEL_VALUE_KEY_CONCEPTS, concepts);
			model.put(MODEL_VALUE_KEY_USERS_GROUPS, usersGroups);
		} catch (Exception e) {
			this.handleExpception(locale, null, e, model, request, response, "WEB_CONCEPTS_LIST_MY_PAGE_ERROR_MESSAGE_COMMON");
		}

		return ViewUtil.render(request, model, Path.WebTemplate.CONCEPTS_LIST_MY, locale);
	}

	@Override
	public String getNewConceptPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.CONCEPTS);

		try {
			// authentication
			AuthenticationController.ensureUserIsLoggedIn(request);

			// load all groups
			List<IGroup> groups = this.groupDao.selectAllGroups(null);
			model.put(MODEL_VALUE_KEY_GROUPS, groups);

			// set read right for "_AllUsers" and "_AnonymousUsers" group per default if enabled
			if(MyProperties.isReadRightForAllUsersGroupPerDefault() || MyProperties.isReadRightForAnonymousUsersGroupPerDefault()) {
			    String[] readRights;
			    if(MyProperties.isReadRightForAllUsersGroupPerDefault() && MyProperties.isReadRightForAnonymousUsersGroupPerDefault()) {
			        readRights = new String[]{String.valueOf(MyProperties.getAllUsersGroupId()), String.valueOf(MyProperties.getAnonymousUsersGroupId())};
			    }else if(MyProperties.isReadRightForAllUsersGroupPerDefault()){
	                readRights = new String[]{String.valueOf(MyProperties.getAllUsersGroupId())};
	            }else {
	                readRights = new String[]{String.valueOf(MyProperties.getAnonymousUsersGroupId())};
	            }
			    model.put(MODEL_VALUE_KEY_READ_RIGHTS, readRights);
			}

		} catch (Exception e) {
			this.handleExpception(locale, null, e, model, request, response, "WEB_CONCEPTS_NEW_CONCEPT_PAGE_ERROR_MESSAGE_COMMON_GET");
		}

		return ViewUtil.render(request, model, Path.WebTemplate.CONCEPTS_NEW_AND_EDIT_CONCEPT, locale);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String handleNewConceptPost(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.CONCEPTS);
		IUnitOfWork unitOfWork = null;

		IUser user = null;
		String conceptId = null;
		String[] languageCodeNames = null;
		String[] names = null;
		String[] languageCodeDefinitions = null;
		String[] definitions = null;
		String type = null;
		String subType = null;
		String dataType = null;
		String origin = null;
		String valueSpace = null;
		String transformations = null;
		String refinements = null;
		String owners = null;
		String authors = null;
		String[] languageCodeNotes = null;
		String[] notes = null;
		String[] languageCodeExamples = null;
		String[] examples = null;
		String[] readRights = null;
		String[] updateRights = null;
		String[] deleteRights = null;
		String[] changeRightsRights = null;

		try {
			// authentication
			user = AuthenticationController.ensureUserIsLoggedIn(request);

			// get request parameters
			conceptId = request.queryParams("id");
			languageCodeNames = request.queryParamsValues("languageCodeName");
			names = request.queryParamsValues("name");
			languageCodeDefinitions = request.queryParamsValues("languageCodeDefinition");
			definitions = request.queryParamsValues("definition");
			type = request.queryParams("type");
			subType = request.queryParams("subType");
			dataType = request.queryParams("dataType");
			origin = request.queryParams("origin");
			valueSpace = request.queryParams("valueSpace");
			owners = request.queryParams("owners");
			authors = request.queryParams("authors");
			transformations = request.queryParams("transformations");
			refinements = request.queryParams("refinements");
			languageCodeNotes = request.queryParamsValues("languageCodeNote");
			notes = request.queryParamsValues("note");
			languageCodeExamples = request.queryParamsValues("languageCodeExample");
			examples = request.queryParamsValues("example");
			String[] groupIds = request.queryParamsValues("groupId");
			readRights = request.queryParamsValues("readRight");
			updateRights = request.queryParamsValues("updateRight");
			deleteRights = request.queryParamsValues("deleteRight");
			changeRightsRights = request.queryParamsValues("changeRightsRight");

			// check whether concept id is already in use
			if(conceptId != null && !conceptId.isEmpty()){
				if(this.conceptDao.existsConcept(null, conceptId)){
					// concept id is already in use -> abort
					String message = "The concept could not be created, because it's id " + conceptId + " is already in use by another concept!";
					String userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_NEW_CONCEPT_PAGE_ERROR_MESSAGE_CONCEPT_ID_IS_ALREADY_IN_USE");
					userMessage = TemplateFiller.fillTemplate(userMessage, conceptId);
					throw new BadRequestException(message, null, userMessage);
				}
			}

			// build new concept
			IConcept newConcept = new Concept(conceptId);
			if(conceptId == null || conceptId.isEmpty()){
				// generate a concept id by the server, if no one is defined in the request
				this.setCounterConceptIdIfNeeded(null, newConcept, locale);
				conceptId = newConcept.getId();
			}
			newConcept.setNames(this.checkContentAndConvert(languageCodeNames, names, "Name", true, newConcept.getId(), locale));
			newConcept.setDefinitions(this.checkContentAndConvert(languageCodeDefinitions, definitions, "Definition", true, newConcept.getId(), locale));
			newConcept.setType(this.checkTypeAndConvert(type, locale));
			newConcept.setSubType(this.checkSubTypeAndConvert(subType, locale));
			newConcept.setDataType(this.checkDataTypeAndConvert(dataType, locale));
			newConcept.setOrigin(this.checkOriginAndReturnIt(origin, locale));
			newConcept.setValueSpace(this.checkValueSpaceAndConvert(valueSpace, locale, model, newConcept));
			newConcept.setOwners(this.checkOwnersAndConvert(owners, locale));
			newConcept.setAuthors(this.checkAuthorsAndConvert(authors, locale));
			newConcept.setConceptsWhichAreTransformedByThisConcept(this.checkConceptIdsAndConvert(transformations, locale));
			newConcept.setConceptsWhichTransformThisConcept(new ArrayList<String>());
			newConcept.setConceptsWhichAreRefinedByThisConcept(this.checkConceptIdsAndConvert(refinements, locale));
			newConcept.setConceptsWhichRefineThisConcept(new ArrayList<String>());
			newConcept.setNotes(this.checkContentAndConvert(languageCodeNotes, notes, "Note", false, newConcept.getId(), locale));
			newConcept.setExamples(this.checkContentAndConvert(languageCodeExamples, examples, "Example", false, newConcept.getId(), locale));
			newConcept.setGroupAccessRights(this.checkGroupAccessRightsAndConvert(groupIds, readRights, updateRights, deleteRights, changeRightsRights, newConcept.getId()));

			// insert new concept into the database
			unitOfWork = this.conceptDao.createUnitOfWork();
			unitOfWork.start();
			this.conceptDao.insertConcept(unitOfWork, newConcept);
			this.insertLog(unitOfWork, newConcept.getId(), user.getId(), ActionEnum.CREATE, true);
			unitOfWork.finish();

			// create success message and redirect to create concept page
			String successMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_NEW_CONCEPT_PAGE_SUCCESS_MESSAGE");
			successMessage = TemplateFiller.fillTemplate(successMessage, newConcept.getId());
			SessionUtil.setSuccessMessage(request, successMessage);
			response.redirect(Path.Web.CONCEPTS_SHOW_CONCEPT + "?conceptId=" + newConcept.getId());

		} catch (Exception e) {
			this.insertFailLog(ActionEnum.CREATE, user, conceptId, e);

			try {
				List<IGroup> groups = this.groupDao.selectAllGroups(null);
				model.put(MODEL_VALUE_KEY_GROUPS, groups);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}

			model.put(MODEL_VALUE_KEY_CONCEPT_ID, conceptId);
			model.put(MODEL_VALUE_KEY_LANGUAGE_CODE_NAMES, languageCodeNames);
            model.put(MODEL_VALUE_KEY_NAMES, this.prepareLineBreaksForJavaScript(this.convertStringArrayToList(names)));
            model.put(MODEL_VALUE_KEY_LANGUAGE_CODE_DEFINITIONS, languageCodeDefinitions);
            model.put(MODEL_VALUE_KEY_DEFINITIONS, this.prepareLineBreaksForJavaScript(this.convertStringArrayToList(definitions)));
			model.put(MODEL_VALUE_KEY_TYPE, type);
			model.put(MODEL_VALUE_KEY_SUB_TYPE, subType);
			model.put(MODEL_VALUE_KEY_DATA_TYPE, dataType);
			model.put(MODEL_VALUE_KEY_ORIGIN, origin);
			model.put(MODEL_VALUE_KEY_VALUE_SPACE, valueSpace);
			model.put(MODEL_VALUE_KEY_OWNERS, owners);
			model.put(MODEL_VALUE_KEY_AUTHORS, authors);
			model.put(MODEL_VALUE_KEY_TRANSFORMATIONS, transformations);
			model.put(MODEL_VALUE_KEY_REFINEMENTS, refinements);
			model.put(MODEL_VALUE_KEY_LANGUAGE_CODE_NOTES, languageCodeNotes);
            model.put(MODEL_VALUE_KEY_NOTES, this.prepareLineBreaksForJavaScript(this.convertStringArrayToList(notes)));
            model.put(MODEL_VALUE_KEY_LANGUAGE_CODE_EXAMPLES, languageCodeExamples);
            model.put(MODEL_VALUE_KEY_EXAMPLES, this.prepareLineBreaksForJavaScript(this.convertStringArrayToList(examples)));
			model.put(MODEL_VALUE_KEY_READ_RIGHTS, readRights);
			model.put(MODEL_VALUE_KEY_UPDATE_RIGHTS, updateRights);
			model.put(MODEL_VALUE_KEY_DELETE_RIGHTS, deleteRights);
			model.put(MODEL_VALUE_KEY_CHANGE_RIGHTS_RIGHTS, changeRightsRights);

			this.handleExpception(locale, null, e, model, request, response, "WEB_CONCEPTS_NEW_CONCEPT_PAGE_ERROR_MESSAGE_COMMON_POST");
		}

		return ViewUtil.render(request, model, Path.WebTemplate.CONCEPTS_NEW_AND_EDIT_CONCEPT, locale);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getEditConceptPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.CONCEPTS);
		IUnitOfWork unitOfWork = null;
		IUser user = null;
		String conceptId = null;

		try {
			// authentication
			user = AuthenticationController.ensureUserIsLoggedIn(request);

			// get request parameters
			conceptId = request.queryParams("conceptId");
			if(conceptId == null || conceptId.isEmpty()){
				throw new AuthenticationException("User " + user.getId() + " called the edit concept page without the query parameter concept id!", false);
			}

			// start unit of work
			unitOfWork = this.conceptDao.createUnitOfWork();
			unitOfWork.start();

			// load concept, which should be edited
			IConcept concept = this.conceptDao.selectConcept(unitOfWork, conceptId);
			if(concept == null){
				throw new AuthenticationException("A concept with the id " + conceptId + " does not exist!", false);
			}

			// throw exception if user has no update right
			if(!user.isSuperAdmin() && !this.groupAccessRightDao.hasUserUpdateRightForConcept(null, user.getId(), conceptId) && !this.ownershipDao.isUserOwnerOfConcept(null, user.getId(), conceptId)){
				// user has not right to update the concept
				throw new AuthenticationException("User " + user.getId() + " tried to get the edit page for the concept " + conceptId + " without update right!", false);
			}

			// insert log entry, that concept was red
			this.insertLog(unitOfWork, System.currentTimeMillis(), conceptId, user.getId(), ActionEnum.READ, true);

			// finish unit of work
			unitOfWork.finish();

			// fill model
			model.put(MODEL_VALUE_KEY_EDIT_MODE, true);
			if(!this.userDao.isUserSuperAdmin(null, user.getId()) && !this.groupAccessRightDao.hasUserChangeRightsRightForConcept(null, user.getId(), conceptId) && !this.ownershipDao.isUserOwnerOfConcept(null, user.getId(), conceptId)){
				// user has not right to change access rights for the concept
				model.put(MODEL_VALUE_KEY_GROUP_ACCESS_RIGHTS_ENABLED, false);
			}else{
				model.put(MODEL_VALUE_KEY_GROUP_ACCESS_RIGHTS_ENABLED, true);
			}
			model.put(MODEL_VALUE_KEY_CONCEPT_ID, concept.getId());
			model.put(MODEL_VALUE_KEY_LANGUAGE_CODE_NAMES, this.putLanguageCodeFromContentsToList(concept.getNames()));
			model.put(MODEL_VALUE_KEY_NAMES, this.putContentFromContentsToList(concept.getNames()));
			model.put(MODEL_VALUE_KEY_LANGUAGE_CODE_DEFINITIONS, this.putLanguageCodeFromContentsToList(concept.getDefinitions()));
			model.put(MODEL_VALUE_KEY_DEFINITIONS, this.prepareLineBreaksForJavaScript(this.putContentFromContentsToList(concept.getDefinitions())));
			model.put(MODEL_VALUE_KEY_TYPE, concept.getType().toString());
			model.put(MODEL_VALUE_KEY_SUB_TYPE, concept.getSubType().toString());
			model.put(MODEL_VALUE_KEY_DATA_TYPE, concept.getDataType().toString());
			model.put(MODEL_VALUE_KEY_ORIGIN, concept.getOrigin());
			if(concept.getValueSpace() != null){
				if(ValueSpace.isJsonSchemaValid(concept.getValueSpace().getJsonSchema())){
				    model.put(MODEL_VALUE_KEY_VALUE_SPACE_VALID, true);
				}
			    model.put(MODEL_VALUE_KEY_VALUE_SPACE, concept.getValueSpace().getJsonSchema());
			}
			model.put(MODEL_VALUE_KEY_OWNERS, this.ownersToStringValue(concept.getOwners()));
			model.put(MODEL_VALUE_KEY_AUTHORS, this.listWithStringsToStringValue(concept.getAuthors()));
			model.put(MODEL_VALUE_KEY_TRANSFORMATIONS, this.listWithStringsToStringValue(concept.getConceptsWhichAreTransformedByThisConcept()));
			model.put(MODEL_VALUE_KEY_REFINEMENTS, this.listWithStringsToStringValue(concept.getConceptsWhichAreRefinedByThisConcept()));
			model.put(MODEL_VALUE_KEY_LANGUAGE_CODE_NOTES, this.putLanguageCodeFromContentsToList(concept.getNotes()));
			model.put(MODEL_VALUE_KEY_NOTES, this.prepareLineBreaksForJavaScript(this.putContentFromContentsToList(concept.getNotes())));
			model.put(MODEL_VALUE_KEY_LANGUAGE_CODE_EXAMPLES, this.putLanguageCodeFromContentsToList(concept.getExamples()));
			model.put(MODEL_VALUE_KEY_EXAMPLES, this.prepareLineBreaksForJavaScript(this.putContentFromContentsToList(concept.getExamples())));

			if(user.isSuperAdmin() || this.groupAccessRightDao.hasUserChangeRightsRightForConcept(null, user.getId(), conceptId) || this.ownershipDao.isUserOwnerOfConcept(null, user.getId(), conceptId)){
				List<IGroup> groups = this.groupDao.selectAllGroups(null);
				List[] lists = this.convertGroupAccessRightToList(concept);
				model.put(MODEL_VALUE_KEY_GROUPS, groups);
				model.put(MODEL_VALUE_KEY_READ_RIGHTS, lists[0]);
				model.put(MODEL_VALUE_KEY_UPDATE_RIGHTS, lists[1]);
				model.put(MODEL_VALUE_KEY_DELETE_RIGHTS, lists[2]);
				model.put(MODEL_VALUE_KEY_CHANGE_RIGHTS_RIGHTS, lists[3]);
			}

		}catch (Exception e) {
			this.insertFailLog(ActionEnum.UPDATE, user, conceptId, e);
			this.handleExpception(locale, unitOfWork, e, model, request, response, "WEB_CONCEPTS_EDIT_CONCEPT_PAGE_ERROR_MESSAGE_COMMON_GET");
		}

		return ViewUtil.render(request, model, Path.WebTemplate.CONCEPTS_NEW_AND_EDIT_CONCEPT, locale);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String handleEditConceptPost(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.CONCEPTS);
		IUnitOfWork unitOfWork = null;
		IConceptComparer conceptComparer = new ConceptComparer();
		IUser user = null;

		String conceptId = null;
		String[] languageCodeNames = null;
		String[] names = null;
		String[] languageCodeDefinitions = null;
		String[] definitions = null;
		String type = null;
		String subType = null;
		String dataType = null;
		String origin = null;
		String valueSpace = null;
		String transformations = null;
		String refinements = null;
		String owners = null;
		String authors = null;
		String[] languageCodeNotes = null;
		String[] notes = null;
		String[] languageCodeExamples = null;
		String[] examples = null;
		String[] readRights = null;
		String[] updateRights = null;
		String[] deleteRights = null;
		String[] changeRightsRights = null;

		try {
			// authentication
			user = AuthenticationController.ensureUserIsLoggedIn(request);

			// get request parameters
			conceptId = request.queryParams("conceptId");
			if(conceptId == null || conceptId.isEmpty()){
				throw new AuthenticationException("User " + user.getId() + " did an edit concept post without the query parameter concept id!", false);
			}

			// start unit of work
			unitOfWork = this.conceptDao.createUnitOfWork();
			unitOfWork.start();

			// load concept, which should be edited
			IConcept oldConcept = this.conceptDao.selectConcept(unitOfWork, conceptId);
			if(oldConcept == null){
				throw new AuthenticationException("A concept with the id " + conceptId + " does not exist!", false);
			}

			// throw exception if user has no update right
			if(!user.isSuperAdmin() && !this.groupAccessRightDao.hasUserUpdateRightForConcept(null, user.getId(), conceptId) && !this.ownershipDao.isUserOwnerOfConcept(null, user.getId(), conceptId)){
				// user has not right to update the concept
				throw new AuthenticationException("User " + user.getId() + " tried to update the concept " + conceptId + " without update right!", false);
			}

			// build update concept
			languageCodeNames = request.queryParamsValues("languageCodeName");
			names = request.queryParamsValues("name");
			languageCodeDefinitions = request.queryParamsValues("languageCodeDefinition");
			definitions = request.queryParamsValues("definition");
			type = request.queryParams("type");
			subType = request.queryParams("subType");
			dataType = request.queryParams("dataType");
			origin = request.queryParams("origin");
			valueSpace = request.queryParams("valueSpace");
			owners = request.queryParams("owners");
			authors = request.queryParams("authors");
			transformations = request.queryParams("transformations");
			refinements = request.queryParams("refinements");
			languageCodeNotes = request.queryParamsValues("languageCodeNote");
			notes = request.queryParamsValues("note");
			languageCodeExamples = request.queryParamsValues("languageCodeExample");
			examples = request.queryParamsValues("example");

			String[] groupIds = request.queryParamsValues("groupId");
			readRights = request.queryParamsValues("readRight");
			updateRights = request.queryParamsValues("updateRight");
			deleteRights = request.queryParamsValues("deleteRight");
			changeRightsRights = request.queryParamsValues("changeRightsRight");

			IConcept conceptToUpdate = new Concept(conceptId);
			conceptToUpdate.setNames(this.checkContentAndConvert(languageCodeNames, names, "Name", true, conceptToUpdate.getId(), locale));
			conceptToUpdate.setDefinitions(this.checkContentAndConvert(languageCodeDefinitions, definitions, "Definition", true, conceptToUpdate.getId(), locale));
			conceptToUpdate.setType(this.checkTypeAndConvert(type, locale));
			conceptToUpdate.setSubType(this.checkSubTypeAndConvert(subType, locale));
			conceptToUpdate.setDataType(this.checkDataTypeAndConvert(dataType, locale));
			conceptToUpdate.setOrigin(this.checkOriginAndReturnIt(origin, locale));
			conceptToUpdate.setValueSpace(this.checkValueSpaceAndConvert(valueSpace, locale, model, conceptToUpdate));
			conceptToUpdate.setOwners(this.checkOwnersAndConvert(owners, locale));
			conceptToUpdate.setAuthors(this.checkAuthorsAndConvert(authors, locale));
			conceptToUpdate.setConceptsWhichAreTransformedByThisConcept(this.checkConceptIdsAndConvert(transformations, locale));
			conceptToUpdate.setConceptsWhichAreRefinedByThisConcept(this.checkConceptIdsAndConvert(refinements, locale));
			conceptToUpdate.setNotes(this.checkContentAndConvert(languageCodeNotes, notes, "Note", false, conceptToUpdate.getId(), locale));
			conceptToUpdate.setExamples(this.checkContentAndConvert(languageCodeExamples, examples, "Example", false, conceptToUpdate.getId(), locale));
			if(!this.userDao.isUserSuperAdmin(null, user.getId()) && !this.groupAccessRightDao.hasUserChangeRightsRightForConcept(null, user.getId(), conceptId) && !this.ownershipDao.isUserOwnerOfConcept(null, user.getId(), conceptId)){
				conceptToUpdate.setGroupAccessRights(oldConcept.getGroupAccessRights());
			}else{
				conceptToUpdate.setGroupAccessRights(this.checkGroupAccessRightsAndConvert(groupIds, readRights, updateRights, deleteRights, changeRightsRights, conceptToUpdate.getId()));
			}

			// check if the requesters version of the concept is based on the newest one
			if(this.logDao.updatedSinceUserRed(unitOfWork, user.getId(), conceptId)){
				// concept was already updated by another user, since requester has red it
				String message = "The concept " + conceptId + " could not be updated, because there was another update, since user " + user.getId() + " red it!";
				String userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_EDIT_CONCEPT_PAGE_ERROR_MESSAGE_UPDATE_SINCE_LAST_READ");
				userMessage = TemplateFiller.fillTemplate(userMessage, conceptId);
				throw new BadRequestException(message, null, userMessage);
			}

			// execute update in database
			String note = conceptComparer.compareConcepts(oldConcept, conceptToUpdate);
			this.conceptDao.updateConcept(unitOfWork, conceptToUpdate);
			this.insertLog(unitOfWork, System.currentTimeMillis(), conceptId, user.getId(), ActionEnum.UPDATE, true, note);

			// finish unit of work
			unitOfWork.finish();

			// create success message and redirect to create concept page
			String successMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_EDIT_CONCEPT_PAGE_SUCCESS_MESSAGE");
			successMessage = TemplateFiller.fillTemplate(successMessage, conceptToUpdate.getId());
			SessionUtil.setSuccessMessage(request, successMessage);
			response.redirect(Path.Web.CONCEPTS_SHOW_CONCEPT + "?conceptId=" + conceptId);

		}catch (Exception e) {
			this.insertFailLog(ActionEnum.UPDATE, user, conceptId, e);

			try {
				List<IGroup> groups = this.groupDao.selectAllGroups(null);
				model.put(MODEL_VALUE_KEY_GROUPS, groups);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}

			model.put(MODEL_VALUE_KEY_CONCEPT_ID, conceptId);
			model.put(MODEL_VALUE_KEY_LANGUAGE_CODE_NAMES, languageCodeNames);
			model.put(MODEL_VALUE_KEY_NAMES, this.prepareLineBreaksForJavaScript(this.convertStringArrayToList(names)));
			model.put(MODEL_VALUE_KEY_LANGUAGE_CODE_DEFINITIONS, languageCodeDefinitions);
			model.put(MODEL_VALUE_KEY_DEFINITIONS, this.prepareLineBreaksForJavaScript(this.convertStringArrayToList(definitions)));
			model.put(MODEL_VALUE_KEY_TYPE, type);
			model.put(MODEL_VALUE_KEY_SUB_TYPE, subType);
			model.put(MODEL_VALUE_KEY_DATA_TYPE, dataType);
			model.put(MODEL_VALUE_KEY_ORIGIN, origin);
			model.put(MODEL_VALUE_KEY_VALUE_SPACE, valueSpace);
			model.put(MODEL_VALUE_KEY_OWNERS, owners);
			model.put(MODEL_VALUE_KEY_AUTHORS, authors);
			model.put(MODEL_VALUE_KEY_TRANSFORMATIONS, transformations);
			model.put(MODEL_VALUE_KEY_REFINEMENTS, refinements);
			model.put(MODEL_VALUE_KEY_LANGUAGE_CODE_NOTES, languageCodeNotes);
			model.put(MODEL_VALUE_KEY_NOTES, this.prepareLineBreaksForJavaScript(this.convertStringArrayToList(notes)));
			model.put(MODEL_VALUE_KEY_LANGUAGE_CODE_EXAMPLES, languageCodeExamples);
			model.put(MODEL_VALUE_KEY_EXAMPLES, this.prepareLineBreaksForJavaScript(this.convertStringArrayToList(examples)));
			model.put(MODEL_VALUE_KEY_READ_RIGHTS, readRights);
			model.put(MODEL_VALUE_KEY_UPDATE_RIGHTS, updateRights);
			model.put(MODEL_VALUE_KEY_DELETE_RIGHTS, deleteRights);
			model.put(MODEL_VALUE_KEY_CHANGE_RIGHTS_RIGHTS, changeRightsRights);

			this.handleExpception(locale, unitOfWork, e, model, request, response, "WEB_CONCEPTS_EDIT_CONCEPT_PAGE_ERROR_MESSAGE_COMMON_POST");
		}
		return ViewUtil.render(request, model, Path.WebTemplate.CONCEPTS_NEW_AND_EDIT_CONCEPT, locale);
	}

	@Override
	public String getShowConceptPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.CONCEPTS);
		IUnitOfWork unitOfWork = null;
		String conceptId = null;
		IUser user = null;

		try{
			// authentication
			user = AuthenticationController.ensureUserIsLoggedIn(request);

			// insert success message
			this.handleSuccessMessage(request, model);

			// get request parameters
			conceptId = request.queryParams("conceptId");
			if(conceptId == null || conceptId.isEmpty()){
				throw new AuthenticationException("User " + user.getId() + " did an show concept request without the query parameter concept id!", false);
			}

			// load concept, which should be shown
			IConcept concept = this.conceptDao.selectConcept(unitOfWork, conceptId);
			if(concept == null){
				throw new AuthenticationException("A concept with the id " + conceptId + " does not exist!", false);
			}

			// throw exception if user has no update right
			if(!user.isSuperAdmin() && !this.groupAccessRightDao.hasUserReadRightForConcept(null, user.getId(), conceptId) && !this.ownershipDao.isUserOwnerOfConcept(null, user.getId(), conceptId)){
				// user has not right to update the concept
				throw new AuthenticationException("User " + user.getId() + " tried to show the concept " + conceptId + " without read right!", false);
			}

			// check, whether user has update right for the concept
			// this is needed for the edit concept button
			boolean updateRight = true;
			if(!user.isSuperAdmin() && !concept.getOwners().contains(user.getId())){
				updateRight = this.groupAccessRightDao.hasUserUpdateRightForConcept(null, user.getId(), conceptId);
			}

			// fill model with concept attributes which has to be converted into a string for presentation
			model.put(MODEL_VALUE_KEY_OWNERS, this.ownersToUsersList(concept.getOwners()));
			model.put(MODEL_VALUE_KEY_TYPE, this.convertConceptTypeToString(concept.getType()));
			model.put(MODEL_VALUE_KEY_SUB_TYPE, this.convertConceptSubTypeToString(concept.getSubType()));
			model.put(MODEL_VALUE_KEY_DATA_TYPE, this.convertConceptDataTypeToString(concept.getDataType()));
			model.put(MODEL_VALUE_KEY_TRANSFORMES_CONCEPT_ID_READ_RIGHT_MAP, this.conceptIdsAndReadRight(concept.getConceptsWhichAreTransformedByThisConcept(), user, concept));
			model.put(MODEL_VALUE_KEY_TRANSFORMED_BY_CONCEPT_ID_READ_RIGHT_MAP, this.conceptIdsAndReadRight(concept.getConceptsWhichTransformThisConcept(), user, concept));
			model.put(MODEL_VALUE_KEY_REFINES_CONCEPT_ID_READ_RIGHT_MAP, this.conceptIdsAndReadRight(concept.getConceptsWhichAreRefinedByThisConcept(), user, concept));
			model.put(MODEL_VALUE_KEY_REFINED_BY_CONCEPT_ID_READ_RIGHT_MAP, this.conceptIdsAndReadRight(concept.getConceptsWhichRefineThisConcept(), user, concept));
			model.put(MODEL_VALUE_KEY_UPDATE_RIGHT, updateRight);
			model.put(MODEL_VALUE_KEY_CONCEPT, concept);

		}catch(Exception e){
			this.insertFailLog(ActionEnum.READ, user, conceptId, e);
			this.handleExpception(locale, unitOfWork, e, model, request, response, "WEB_CONCEPTS_SHOW_CONCEPT_PAGE_ERROR_MESSAGE_COMMON");
		}

		return ViewUtil.render(request, model, Path.WebTemplate.CONCEPTS_SHOW_CONCEPT, locale);
	}

	@Override
	public String getSearchConceptPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.CONCEPTS);

		try{
			// authentication
			AuthenticationController.ensureUserIsLoggedIn(request);
		}catch(Exception e){
			this.handleExpception(locale, null, e, model, request, response, "WEB_CONCEPTS_SEARCH_CONCEPT_PAGE_ERROR_MESSAGE_COMMON_GET");
		}finally{
			model.put(MODEL_VALUE_KEY_SEARCH_OPTION, MODEL_VALUE_KEY_SEARCH_OPTION_VALUE_ID_AND_NAME);
		}

		return ViewUtil.render(request, model, Path.WebTemplate.CONCEPTS_SEARCH, locale);
	}

	@Override
	public String handleSearchConceptPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.CONCEPTS);
		String searchTerm = null;
		String searchOption = null;
		boolean searchInIds = false;
		boolean searchInNames = false;
		try{
			// authentication
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);

			// get request parameters
			searchTerm = request.queryParams("searchTerm");
			searchOption = request.queryParams("searchOption");

			// check parameters
			this.checkSearchTerm(searchTerm, locale);
			this.checkSearchOption(searchOption);

			// search concept(s)
			if(searchOption.equals(MODEL_VALUE_KEY_SEARCH_OPTION_VALUE_ID)){
				searchInIds = true;
			}else if(searchOption.equals(MODEL_VALUE_KEY_SEARCH_OPTION_VALUE_NAME)){
				searchInNames = true;
			}else{
				searchInIds = true;
				searchInNames = true;
			}
			List<IConcept> concepts = this.conceptDao.searchConcepts(null, user.getId(), searchTerm, searchInIds, searchInNames);

			// load user's groups
			List<IGroup> usersGroups = this.groupDao.selectAllGroupsWhereUserIsMember(null, user.getId());

			// fill model with values which are needed in success case
			model.put(MODEL_VALUE_KEY_CONCEPTS, concepts);
			model.put(MODEL_VALUE_KEY_LOCALE, locale);
			model.put(MODEL_VALUE_KEY_USERS_GROUPS, usersGroups);

		}catch(Exception e){
			this.handleExpception(locale, null, e, model, request, response, "WEB_CONCEPTS_SEARCH_CONCEPT_PAGE_ERROR_MESSAGE_COMMON_POST");
		}finally{
			// fill model with values which are needed in error and success case
			model.put(MODEL_VALUE_KEY_SEARCH_TERM, searchTerm);
			model.put(MODEL_VALUE_KEY_SEARCH_OPTION, searchOption);
		}

		return ViewUtil.render(request, model, Path.WebTemplate.CONCEPTS_SEARCH, locale);
	}

	@Override
	public String getConceptLogPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.CONCEPTS);
		String conceptId = null;

		try{
			// authentication
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);

			// get parameters
			conceptId = request.queryParams("conceptId");
			if(conceptId == null || conceptId.isEmpty()){
				throw new AuthenticationException("User " + user.getId() + " did an show concept log request without the query parameter concept id!", false);
			}

			// rights check
			if(!user.isSuperAdmin() && !this.ownershipDao.isUserOwnerOfConcept(null, user.getId(), conceptId)){
				throw new AuthenticationException("User " + user.getId() + " did an show concept log request for the concept " + conceptId + " though he is neither a super admin nor an owner of it!", false);
			}

			// load logs
			List<ILog> logs = this.logDao.selectAllLogsForConcept(null, conceptId, true);

			// fill model
			model.put(MODEL_VALUE_KEY_LOGS, logs);
			model.put(MODEL_VALUE_KEY_USER_ID_TO_USER_MAPPING, this.getAllUserIdsAndUsers());

		}catch(Exception e){
			this.handleExpception(locale, null, e, model, request, response, "WEB_CONCEPTS_CONCEPT_LOG_PAGE_ERROR_MESSAGE_COMMON");
		}

		return ViewUtil.render(request, model, Path.WebTemplate.CONCEPTS_CONCEPT_LOG, locale);
	}

	@Override
	public String handleDeleteConcept(Request request, Response response){
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		this.setOpenedMenu(request, model, MenuEnum.CONCEPTS);
		IUser user = null;
		String conceptId = null;
		IUnitOfWork unitOfWork = null;

		try{
			// authentication
			user = AuthenticationController.ensureUserIsLoggedIn(request);

			// get parameters
			conceptId = request.queryParams("conceptId");
			if(conceptId == null || conceptId.isEmpty()){
				throw new AuthenticationException("User " + user.getId() + " did an show concept log request without the query parameter concept id!", false);
			}

			unitOfWork = this.conceptDao.createUnitOfWork();
			unitOfWork.start();

			if(!this.conceptDao.existsConcept(unitOfWork, conceptId)){
				String message = "A concept with the id " + conceptId + " does not exist!";
				String userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_DELETE_CONCEPT_PAGE_ERROR_MESSAGE_CONCEPT_DOES_NOT_EXIST");
				userMessage = TemplateFiller.fillTemplate(userMessage, conceptId);
				throw new BadRequestException(message, null, userMessage);
			};

			// rights check
			if(!user.isSuperAdmin() && !this.groupAccessRightDao.hasUserDeleteRightForConcept(unitOfWork, user.getId(), conceptId) && !this.ownershipDao.isUserOwnerOfConcept(unitOfWork, user.getId(), conceptId)){
				throw new AuthenticationException("User " + user.getId() + " did a delete concept request for the concept " + conceptId + " without delete right!", false);
			}

			List<IConcept> refinements = this.conceptDao.selectConceptsWhichRefineConcept(unitOfWork, conceptId);
			List<IConcept> transformations = this.conceptDao.selectConceptsWhichTransformConcept(unitOfWork, conceptId);
			if(!refinements.isEmpty() || !transformations.isEmpty()){
				model.put( MODEL_VALUE_KEY_REFINED_CONCEPTS, refinements);
				model.put( MODEL_VALUE_KEY_TRANSFORMED_CONCEPTS, transformations);
				String infoMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_DELETE_CONCEPT_PAGE_MESSAGE_CONCEPT_IS_REFERENCED_BY_OTHER_CONCEPTS");
				infoMessage = TemplateFiller.fillTemplate(infoMessage, conceptId);
				model.put(MODEL_VALUE_KEY_INFO_MESSAGE, infoMessage);
			}else{
				this.conceptDao.deleteConcept(unitOfWork, conceptId);
				this.insertLog(unitOfWork, System.currentTimeMillis(), conceptId, user.getId(), ActionEnum.DELETE, true);
				String successMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_DELETE_CONCEPT_PAGE_SUCCESS_MESSAGE");
				successMessage = TemplateFiller.fillTemplate(successMessage, conceptId);
				model.put(MODEL_VALUE_KEY_INFO_MESSAGE, successMessage);
			}

			unitOfWork.finish();

		}catch(Exception e){
		    this.insertFailLog(ActionEnum.DELETE, user, conceptId, e);
			this.handleExpception(locale, null, e, model, request, response, "WEB_CONCEPTS_CONCEPT_LOG_PAGE_ERROR_MESSAGE_COMMON");
		}

		return ViewUtil.render(request, model, Path.WebTemplate.CONCEPTS_DELETE_CONCEPT, locale);
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

	private void insertFailLog(ActionEnum action, IUser user, String conceptId, Exception exception){
		int userId = -1;
		if(user != null){
			userId = user.getId();
		}
		if(conceptId != null && conceptId.isEmpty()){
			conceptId = null;
		}
		try {
			this.insertLog(null, System.currentTimeMillis(), conceptId, userId, action, false, exception.getMessage());
		} catch (DataAccessException e1) {
			e1.printStackTrace();
		}
	}

	private void setCounterConceptIdIfNeeded(IUnitOfWork unitOfWork, IConcept newConcept, Locale locale) throws DataAccessException, BadRequestException {
		if(newConcept.getId() == null || newConcept.getId().isEmpty()){
	        String generatedConceptId = null;
			int maxIterations = MyProperties.getConceptIdGenerationMaxNumberOfIterations();
			int iterations = 0;
			while(iterations < maxIterations && (generatedConceptId == null || this.conceptDao.existsConcept(unitOfWork, generatedConceptId))){
				 generatedConceptId = MyProperties.getConceptIdGenerationPrefix();
				 generatedConceptId += String.valueOf(this.generatedConceptIdDao.selectNumberAndIncrementIt(unitOfWork));
				 iterations++;
			}
			if(generatedConceptId == null){
				String message = "It was not possible to generate a concept id, which not already exists!";
				String userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_NEW_CONCEPT_PAGE_ERROR_MESSAGE_CONCEPT_ID_COULD_NOT_BE_GENERATED");
				throw new BadRequestException(message, null, userMessage);
			}
			newConcept.setId(generatedConceptId);
			if(newConcept.getGroupAccessRights() != null){
				for(IGroupAccessRight n : newConcept.getGroupAccessRights()){
					n.setConceptId(generatedConceptId);
				}
			}
		}
	}

	private String checkOriginAndReturnIt(String origin, Locale locale) throws BadRequestException{
		if(origin == null || origin.isEmpty()){
			String message = "Parameter origin is missing!";
			String userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_NEW_CONCEPT_PAGE_ERROR_MESSAGE_MISSING_ORIGIN");
			throw new BadRequestException(message, null, userMessage);
		}
		return origin;
	}

	private IValueSpace checkValueSpaceAndConvert(String valueSpaceAsString, Locale locale, Map<String, Object> model, IConcept concept) throws BadRequestException{
		IValueSpace valueSpace = null;
		if(valueSpaceAsString != null && valueSpaceAsString.isEmpty() == false){
			if(!ValueSpace.isJsonSchemaValid(valueSpaceAsString)){
			    model.put(MODEL_VALUE_KEY_VALUE_SPACE_VALID, false);
			    String message = "The parameter valueSpace is no valid JSON schema!";
			    String userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_NEW_CONCEPT_PAGE_ERROR_MESSAGE_INVALID_VALUE_SPACE");
			    throw new BadRequestException(message, null, userMessage);
			}
			model.put(MODEL_VALUE_KEY_VALUE_SPACE_VALID, true);
		    valueSpace = new ValueSpace(valueSpaceAsString);
		    valueSpace.setDefaultDesciptionIfThereExistsNoOther(concept.getId());
		}
		return valueSpace;
	}

	private TypeEnum checkTypeAndConvert(String typeAsString, Locale locale) throws BadRequestException, AuthenticationException{
		if(typeAsString == null || typeAsString.isEmpty()){
			String message = "The parameter type is null or empty!";
			String userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_NEW_AND_EDIT_CONCEPT_PAGE_ERROR_MESSAGE_NO_TYPE_SELECTED");
			throw new BadRequestException(message, null, userMessage);
		}
		try{
			return TypeEnum.valueOf(typeAsString);
		}catch(IllegalArgumentException e){
			e.printStackTrace();
			String message = typeAsString + " is no valid type!";
			throw new AuthenticationException(message, false);
		}
	}

	private SubTypeEnum checkSubTypeAndConvert(String subTypeAsString, Locale locale) throws BadRequestException, AuthenticationException{
		if(subTypeAsString == null || subTypeAsString.isEmpty()){
			String message = "The parameter subType is null or empty!";
			String userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_NEW_AND_EDIT_CONCEPT_PAGE_ERROR_MESSAGE_NO_SUBTYPE_SELECTED");
			throw new BadRequestException(message, null, userMessage);
		}
		try{
			return SubTypeEnum.valueOf(subTypeAsString);
		}catch(IllegalArgumentException e){
			e.printStackTrace();
			String message = subTypeAsString + " is no valid sub type!";
			throw new AuthenticationException(message, false);
		}
	}

	private DataTypeEnum checkDataTypeAndConvert(String dataTypeAsString, Locale locale) throws BadRequestException, AuthenticationException{
		if(dataTypeAsString == null || dataTypeAsString.isEmpty()){
			String message = "The parameter dataType is null or empty!";
			String userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_NEW_AND_EDIT_CONCEPT_PAGE_ERROR_MESSAGE_NO_DATATYPE_SELECTED");
			throw new BadRequestException(message, null, userMessage);
		}
		try{
			return DataTypeEnum.valueOf(dataTypeAsString);
		}catch(IllegalArgumentException e){
			e.printStackTrace();
			String message = dataTypeAsString + " is no valid data type!";
			throw new AuthenticationException(message, false);
		}
	}

	private List<Integer> checkOwnersAndConvert(String ownersAsString, Locale locale) throws DataAccessException, BadRequestException{
		Map<String, IUser> users = this.getAllUserNamesAndUsers();
		List<Integer> ownersList = new ArrayList<Integer>();

		if(ownersAsString != null && ownersAsString.isEmpty() == false && ownersAsString.equals("User name[linebreak]") == false){
			String[] lines = ownersAsString.split(new String(new char[]{13, 10}));
			for(String line : lines){
				// check whether group member exists or not
				if(users.containsKey(line)){
					ownersList.add(users.get(line).getId());
				}else{
					String message = "Unknown userName = " + line + "!";
					String userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_NEW_CONCEPT_PAGE_ERROR_MESSAGE_UNKNOWN_OWNER");
					userMessage = TemplateFiller.fillTemplate(userMessage, line);
					throw new BadRequestException(message, null, userMessage);
				}
			}// end for loop
		}
		if(ownersAsString.isEmpty()){
			String message = "At least one owner is required!";
			String userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_NEW_CONCEPT_PAGE_ERROR_MESSAGE_OWNER_REQUIRED");
			throw new BadRequestException(message, null, userMessage);
		}
		return ownersList;
	}

	private Map<String, IUser> getAllUserNamesAndUsers() throws DataAccessException{
		List<IUser> usersAsList = this.userDao.selectUsers(null);
		Map<String, IUser> usersAsMap = new HashMap<String, IUser>();
		for(IUser user : usersAsList){
			usersAsMap.put(user.getUserName(), user);
		}
		return usersAsMap;
	}

	private List<String> checkAuthorsAndConvert(String authorsAsString, Locale locale) throws BadRequestException{
		List<String> authorsAsList = new ArrayList<String>();
		if(authorsAsString != null && authorsAsString.isEmpty() == false){
			String[] lines = authorsAsString.split(new String(new char[]{13, 10}));
			for(String line : lines){
				if(line.isEmpty() == false){
					authorsAsList.add(line);
				}
			}// end for loop
		}
		// use this code if at least one author shall be inserted
		/*if(authorsAsList.isEmpty()){
			String message = "At least one author is required!";
			String userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_NEW_CONCEPT_PAGE_ERROR_MESSAGE_AUTHOR_REQUIRED");
			throw new BadRequestException(message, null, userMessage);
		}*/
		return authorsAsList;
	}

	private List<String> checkConceptIdsAndConvert(String conceptIdsAsString, Locale locale) throws DataAccessException, BadRequestException{
		List<String> conceptIdsAsList = new ArrayList<String>();
		if(conceptIdsAsString != null && conceptIdsAsString.isEmpty() == false){
			String[] lines = conceptIdsAsString.split(new String(new char[]{13, 10}));
			for(String line : lines){
				if(line.isEmpty() == false){
					if(this.conceptDao.existsConcept(null, line)){
						conceptIdsAsList.add(line);
					}else{
						String message = "A concept with the id " + line + " does not exist!";
						String userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_NEW_CONCEPT_PAGE_ERROR_MESSAGE_UNKNOWN_CONCEPT_ID");
						userMessage = TemplateFiller.fillTemplate(userMessage, line);
						throw new BadRequestException(message, null, userMessage);
					}
				}
			}// end for loop
		}
		return conceptIdsAsList;
	}

	@SuppressWarnings("rawtypes")
	private List checkContentAndConvert(String[] languageCodes, String[] contents, String contentType, boolean required, String conceptId, Locale locale) throws BadRequestException, DataAccessException{
		List<IContent> contentsAsList = new ArrayList<IContent>();
		if(languageCodes == null || contents == null){
			if(required){
				String message = "Missing parameter(s) for " + contentType + "(s)!";
				String userMessage = null;
				if(contentType.equals("Name")){
				    userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_NEW_AND_EDIT_CONCEPT_PAGE_ERROR_MESSAGE_MISSING_NAME");
				}else if(contentType.equals("Definition")){
				    userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_NEW_AND_EDIT_CONCEPT_PAGE_ERROR_MESSAGE_MISSING_DEFINITION");
				}
				throw new BadRequestException(message, null, userMessage);
			}else{
				return contentsAsList;
			}
		}
		if(languageCodes.length != contents.length){
			String message = "Number of language codes differ from number of " + contentType + "s!";
			throw new BadRequestException(message);
		}
		if(required && languageCodes.length == 0){
			String message = "At least one " + contentType + " is required!";
			throw new BadRequestException(message);
		}
		for(int i = 0; i < languageCodes.length; i++){
			String languageCode = languageCodes[i];
			if(languageCode != null){
				if(languageCode.isEmpty()){
					languageCode = null;
				}else if(this.languageDao.isLanguageCodeValid(null, languageCode) == false){
					String message = "The language code " + languageCode + " is invalid!";
					String userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_NEW_CONCEPT_PAGE_ERROR_MESSAGE_INVALID_LANGUAGE_CODE");
					userMessage = TemplateFiller.fillTemplate(userMessage, languageCode);
					throw new BadRequestException(message, null, userMessage);
				}
			}
			String content = contents[i];
			if(content != null && content.isEmpty() == false){
				if(contentType.equals("Name")){
					contentsAsList.add(new Name(languageCode, conceptId, content));
				}else if(contentType.equals("Definition")){
					contentsAsList.add(new Definition(languageCode, conceptId, content));
				}else if(contentType.equals("Note")){
					contentsAsList.add(new Note(languageCode, conceptId, content));
				}else if(contentType.equals("Example")){
					contentsAsList.add(new Example(languageCode, conceptId, content));
				}else{
					throw new RuntimeException("The content type " + contentType + " is unknown!");
				}
			}else if(languageCode != null){
				String message = "At least one " + contentType + " field is empty though a language code is defined!";
				String userMessage = LanguageHandler.getWord(locale, "WEB_CONCEPTS_NEW_CONCEPT_PAGE_ERROR_MESSAGE_EMPTY_CONTENT_FIELD");
				userMessage = TemplateFiller.fillTemplate(userMessage, contentType.toLowerCase());
				throw new BadRequestException(message, null, userMessage);
			}
		}// end for loop
		return contentsAsList;
	}

	private List<IGroupAccessRight> checkGroupAccessRightsAndConvert(String[] groupIds, String[] readRights, String[] updateRights, String[] deleteRights, String[] changeRightsRights, String conceptId) throws AuthenticationException{
		List<IGroupAccessRight> groupAccessRights = new ArrayList<IGroupAccessRight>();
		List<String> groupIdsAsList = this.checkArrayAndConvert(groupIds);
		List<String> readRightsAsList = this.checkArrayAndConvert(readRights);
		List<String> updateRightsAsList = this.checkArrayAndConvert(updateRights);
		List<String> deleteRightsAsList = this.checkArrayAndConvert(deleteRights);
		List<String> changeRightsRightsAsList = this.checkArrayAndConvert(changeRightsRights);

		for(String groupIdAsString : groupIdsAsList){
			if(Checker.isStringInteger(groupIdAsString) == false){
				String message = "Group access right parameters contain " + groupIdAsString + " as group id, which is an invalid group id!";
				throw new AuthenticationException(message, false);
			}
			int groupId = Integer.valueOf(groupIdAsString);

			// this ensures that a user cannot assign another right than the read right to the _AnonymousUsersGroup
			if(groupId == MyProperties.getAnonymousUsersGroupId()) {
			    if(this.hasRight(updateRightsAsList, groupId)||this.hasRight(deleteRightsAsList, groupId)||this.hasRight(changeRightsRightsAsList, groupId)) {
			        String message = "Someone tried to assign another access right than the read right to the _AnonymousUsersGroup with the group id " + groupIdAsString + "!";
	                throw new AuthenticationException(message, false);
			    }
			}

			IGroupAccessRight groupAccessRight = new GroupAccessRight(groupId, conceptId, this.hasRight(readRightsAsList, groupId), this.hasRight(updateRightsAsList, groupId), this.hasRight(deleteRightsAsList, groupId), this.hasRight(changeRightsRightsAsList, groupId));
			groupAccessRights.add(groupAccessRight);
		}
		return groupAccessRights;
	}

	private List<String> checkArrayAndConvert(String[] array){
		List<String> list;
		if(array == null){
			list = new ArrayList<String>();
		}else{
			list = new ArrayList<String>(Arrays.asList(array));
		}
		return list;
	}

	private boolean hasRight(List<String> list, int groupId){
		boolean result = false;
		if(list.contains(String.valueOf(groupId))){
			result = true;
		}
		return result;
	}

	private void insertLog(IUnitOfWork unitOfWork, String conceptId, int userId, ActionEnum type, boolean success) throws DataAccessException{
		this.insertLog(unitOfWork, System.currentTimeMillis(), conceptId, userId, type, success, null);
	}

	private void insertLog(IUnitOfWork unitOfWork, long timeStamp, String conceptId, int userId, ActionEnum type, boolean success) throws DataAccessException{
		this.insertLog(unitOfWork, timeStamp, conceptId, userId, type, success, null);
	}

	private void insertLog(IUnitOfWork unitOfWork, long timeStamp, String conceptId, int userId, ActionEnum type, boolean success, String note) throws DataAccessException{
		ILog log = new Log();
		log.setAction(type);
		log.setUserId(userId);
		log.setConceptId(conceptId);
		log.setSuccess(success);
		log.setDateTimeStampInMilliseconds(timeStamp);
		log.setNote(note);
		this.logDao.insertLog(unitOfWork, log);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<String> putLanguageCodeFromContentsToList(List contents) {
		List<String> languageCodes = new ArrayList<String>();
		for (IContent content : ((List<IContent>) contents)) {
			if (content.getLanguageCode() == null) {
				languageCodes.add("");
			} else {
				languageCodes.add(content.getLanguageCode());
			}
		}
		return languageCodes;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<String> putContentFromContentsToList(List contents) {
		List<String> listWithContents = new ArrayList<String>();
		for (IContent content : ((List<IContent>) contents)) {
			listWithContents.add(content.getContent());
		}
		return listWithContents;
	}

	private String listWithStringsToStringValue(List<String> list){
		String stringValue = "";
		for(String s : list){
			stringValue += s + "\n";
		}
		return stringValue;
	}

	private String ownersToStringValue(List<Integer> owners) throws DataAccessException {
		Map<Integer, IUser> users = this.getAllUserIdsAndUsers();
		String ownersAsString = "";
		for(int userId : owners){
			IUser user = users.get(userId);
			if(user != null){
				ownersAsString += user.getUserName();
				ownersAsString += "\n";
			}
		}
		return ownersAsString;
	}

	private List<IUser> ownersToUsersList(List<Integer> owners) throws DataAccessException {
		Map<Integer, IUser> usersAsMap = this.getAllUserIdsAndUsers();
		List<IUser> usersAsList = new ArrayList<IUser>();
		for(int userId : owners){
			IUser user = usersAsMap.get(userId);
			if(user != null){
				usersAsList.add(user);
			}
		}
		return usersAsList;
	}

	private Map<Integer, IUser> getAllUserIdsAndUsers() throws DataAccessException{
		List<IUser> usersAsList = this.userDao.selectUsers(null);
		Map<Integer, IUser> usersAsMap = new HashMap<Integer, IUser>();
		for(IUser user : usersAsList){
			usersAsMap.put(user.getId(), user);
		}
		return usersAsMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List[] convertGroupAccessRightToList(IConcept concept){
		List[] lists = {new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>()};
		for(IGroupAccessRight groupAccessRight : concept.getGroupAccessRights()){
			if(groupAccessRight.hasReadRight()){
				lists[0].add(String.valueOf(groupAccessRight.getGroupId()));
			}
			if(groupAccessRight.hasUpdateRight()){
				lists[1].add(String.valueOf(groupAccessRight.getGroupId()));
			}
			if(groupAccessRight.hasDeleteRight()){
				lists[2].add(String.valueOf(groupAccessRight.getGroupId()));
			}
			if(groupAccessRight.hasChangeRightsRight()){
				lists[3].add(String.valueOf(groupAccessRight.getGroupId()));
			}
		}
		return lists;
	}

	private String convertConceptTypeToString(TypeEnum type){
		String typeAsString;
		switch(type){
		case NEED_AND_PREFERENCE:
			typeAsString = "PreferenceStatement";
			break;
		case CONTEXT_DESCRIPTION:
			typeAsString = "ContextDescription";
			break;
		case RESOURCE_DESCRIPTION:
			typeAsString = "ResourceDescription";
			break;
		default:
			throw new RuntimeException("The type " + type.toString() + " is unknown for this method!");
		}
		return typeAsString;
	}

	private String convertConceptSubTypeToString(SubTypeEnum subType){
		String subTypeAsString;
		switch(subType){
		case TERM:
			subTypeAsString = "Term";
			break;
		case TRANSFORM:
			subTypeAsString = "Transform";
			break;
		default:
			throw new RuntimeException("The subtype " + subType.toString() + " is unknown for this method!");
		}
		return subTypeAsString;
	}

	private String convertConceptDataTypeToString(DataTypeEnum dataType){
		String dataTypeAsString;
		switch(dataType){
		case BOOLEAN:
			dataTypeAsString = "Boolean";
			break;
		case NUMBER:
			dataTypeAsString = "Number";
			break;
		case STRING:
			dataTypeAsString = "String";
			break;
		default:
			throw new RuntimeException("The datatype " + dataType.toString() + " is unknown for this method!");
		}
		return dataTypeAsString;
	}

    private void checkSearchTerm(String searchTerm, Locale locale) throws AuthenticationException, BadRequestException {
        if (searchTerm == null) {
            throw new AuthenticationException("The parameter searchTerm must not be null!", false);
        }
        if (searchTerm.isEmpty()) {
            String message = "The parameter searchTerm must not be empty!";
            String userMessage = LanguageHandler.getWord(locale,
                    "WEB_CONCEPTS_SEARCH_CONCEPT_PAGE_ERROR_MESSAGE_EMPTY_SEARCH_TERM");
            throw new BadRequestException(message, null, userMessage);
        }
    }

	private void checkSearchOption(String searchOption) throws AuthenticationException {
		if(searchOption.equals(MODEL_VALUE_KEY_SEARCH_OPTION_VALUE_ID)){
			return;
		}else if(searchOption.equals(MODEL_VALUE_KEY_SEARCH_OPTION_VALUE_NAME)){
			return;
		}else if(searchOption.equals(MODEL_VALUE_KEY_SEARCH_OPTION_VALUE_ID_AND_NAME)){
			return;
		}else{
			throw new AuthenticationException("The value (" + searchOption + ") of the parameter searchOption is invalid!", false);
		}
	}

	private Map<String, Boolean> conceptIdsAndReadRight(List<String> listWithConceptIds, IUser user, IConcept concept) throws DataAccessException{
		Map<String, Boolean> transformationsMap = new HashMap<String, Boolean>();
		for(String conceptId : listWithConceptIds){
			boolean readRight = true;
			if(!user.isSuperAdmin() && !this.ownershipDao.isUserOwnerOfConcept(null, user.getId(), conceptId)){
				readRight = this.groupAccessRightDao.hasUserReadRightForConcept(null, user.getId(), conceptId);
			}
			transformationsMap.put(conceptId, readRight);
		}
		return transformationsMap;
	}

	private List<String> convertStringArrayToList(String[] stringArray){
	    List<String> list = null;
	    if(stringArray != null){
	        list = new ArrayList<String>(Arrays.asList(stringArray));
	    }
	    return list;
	}

	private List<String> prepareLineBreaksForJavaScript(List<String> stringList){
		if(stringList != null){
		    for(int i = 0; i < stringList.size(); i++){
	            String tmp = stringList.get(i).replace(new String(new char[]{13, 10}), "\\n");
	            stringList.remove(i);
	            stringList.add(i, tmp);
	        }
		}
		return stringList;
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
