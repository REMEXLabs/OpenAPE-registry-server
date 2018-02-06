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
package de.hdm.server.jsonapi;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hdm.configuration.MyProperties;
import de.hdm.databaseaccess.ConceptFilter;
import de.hdm.databaseaccess.DaoFactory;
import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IConceptDao;
import de.hdm.databaseaccess.IConceptFilter;
import de.hdm.databaseaccess.IGeneratedConceptIdDao;
import de.hdm.databaseaccess.IGroupAccessRightDao;
import de.hdm.databaseaccess.ILogDao;
import de.hdm.databaseaccess.IOwnershipDao;
import de.hdm.databaseaccess.IUnitOfWork;
import de.hdm.databaseaccess.IUserDao;
import de.hdm.databaseaccess.UnitOfWorkStateEnum;
import de.hdm.datatypes.ActionEnum;
import de.hdm.datatypes.IConcept;
import de.hdm.datatypes.IContent;
import de.hdm.datatypes.IGroupAccessRight;
import de.hdm.datatypes.ILog;
import de.hdm.datatypes.Log;
import de.hdm.datatypes.TypeEnum;
import de.hdm.exceptions.RegistryServerException;
import de.hdm.helpers.GregorianCalendarHelper;
import de.hdm.helpers.TemplateFiller;
import de.hdm.multiplelanguages.LanguageHandler;
import de.hdm.server.AuthenticationController;
import de.hdm.server.AuthenticationException;
import de.hdm.server.BadRequestException;
import de.hdm.server.ConceptComparer;
import de.hdm.server.IConceptComparer;
import de.hdm.server.RequestUtil;
import spark.Request;
import spark.Response;

/**
 * This class handles the JSON api requests.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class JsonConceptController implements IJsonConceptController {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	
	/**
	 * Logger.
	 */
	private final static Logger logger = LoggerFactory.getLogger(JsonConceptController.class);
	
	/**
	 * Data access object for the concepts.
	 */
	private IConceptDao conceptDao;
	
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
	
	/**
	 * Data access object for the generated concept ids.
	 */
	private IGeneratedConceptIdDao generatedConceptIdDao;
	
	/**
	 * Data access object for the users.
	 */
	private IUserDao userDao;
	
	
	
	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Default Constructor. It initializes all required objects.
	 */
	public JsonConceptController(){
		this.conceptDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createConceptDao();
		this.logDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createLogDao();
		this.groupAccessRightDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createGroupAccessRightDao();
		this.ownershipDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createOwnershipDao();
		this.generatedConceptIdDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createGeneratedConceptIdDao();
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

	/*
	 * (non-Javadoc)
	 * @see de.hdm.server.IJsonConceptController#postConceptAsJson(spark.Request, spark.Response)
	 */
	@Override
	public String postConceptAsJson(Request request, Response response){
		String responseContent = null;
		IUnitOfWork unitOfWork = null;
		Locale locale = null;
		IJsonHandler jsonHandler = null;
		int userId = -1;
		String conceptId = null;
		try {
			locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
			String apiKey = RequestUtil.getQueryParameterApiKey(locale, request);
			userId = AuthenticationController.ensureApiKeyIsValid(locale, apiKey);
			jsonHandler = new JsonHandler(locale);
			
			// only for logging
			//this.logParamsForPostConceptAsJson(apiKey, userId, locale);
			
			IConcept newConcept = jsonHandler.jsonToConcept(request.body(), false);			
			conceptId = newConcept.getId();
			
			unitOfWork = this.conceptDao.createUnitOfWork();
            unitOfWork.start();
			
			// check whether concept id is already in use
			if(conceptId != null){
				if(this.conceptDao.existsConcept(unitOfWork, conceptId)){
					// concept id is already in use -> abort
					String message = "The concept could not be created, because it's id " + conceptId + " is already in use by another concept!";
					String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(locale, "ERROR_BAD_REQUEST_CONCEPT_ID_IS_ALREADY_IN_USE"), conceptId);
					throw new BadRequestException(message, null, userMessage);
				}
			}
			
			// if conceptId is null or not already in use -> insert new concept
			
			
			// generate a concept id by the server, if no one is defined in the request
			this.setCounterConceptIdIfNeeded(unitOfWork, newConcept, locale);
			conceptId = newConcept.getId(); // for handleException
			
			// set requester as owner, if no owner is defined
			this.setRequesterAsOwnerIfNeeded(newConcept, userId);
			
			// set default description in JSON schema if needed
			if(newConcept.getValueSpace() != null){
			    newConcept.getValueSpace().setDefaultDesciptionIfThereExistsNoOther(newConcept.getId());
			}
						
			// insert new concept into the database
			this.conceptDao.insertConcept(unitOfWork, newConcept);
			this.insertLog(unitOfWork, newConcept.getId(), userId, ActionEnum.CREATE, true);
			IConcept insertedConcept = this.conceptDao.selectConcept(unitOfWork, newConcept.getId());
			unitOfWork.finish();
			
			// build response
			response.status(200);
			String message = LanguageHandler.getWord(locale, "RESPONSE_JSON_POST_CONCEPT");
			responseContent = jsonHandler.createPostAndPutConceptSuccessJsonResponse(insertedConcept, message);
			response.header("Content-Type", "application/json");
			response.header("Location", MyProperties.getRegistryServerUriUntilApi() + "/api/record/" + newConcept.getId());
			
		}catch (Exception e){
			responseContent = this.handleExpception(locale, unitOfWork, response, e, conceptId, userId, ActionEnum.CREATE);
		}
		return responseContent;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.server.IJsonConceptController#putConceptAsJson(spark.Request, spark.Response)
	 */
	@Override
	public String putConceptAsJson(Request request, Response response){
		String responseContent = null;
		IUnitOfWork unitOfWork = null;
		Locale locale = null;
		IJsonHandler jsonHandler = null;
		int userId = -1;
		String conceptId = null;
		IConceptComparer conceptComparer = new ConceptComparer();
		try {
			locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
			String apiKey = RequestUtil.getQueryParameterApiKey(locale, request);
			userId = AuthenticationController.ensureApiKeyIsValid(locale, apiKey);
			jsonHandler = new JsonHandler(locale);
			
			// only for logging
			this.logParamsForPostConceptAsJson(apiKey, userId, locale);
			
			// convert json to concept
			IConcept conceptToUpdate = jsonHandler.jsonToConcept(request.body(), true);
			conceptId = conceptToUpdate.getId();
			
			// throw exception when concept is not existing
			if(this.conceptDao.existsConcept(unitOfWork, conceptId) == false){
				// concept not found
				String conceptIdForErrorMessage = conceptId;
				conceptId = null;
				this.handleConceptNotFound(locale, "ERROR_BAD_REQUEST_CONCEPT_NOT_FOUND", conceptIdForErrorMessage);
			}
			
			// throw exception if user has no write right
			if(!this.userDao.isUserSuperAdmin(null, userId) && !this.groupAccessRightDao.hasUserUpdateRightForConcept(null, userId, conceptId) && !this.ownershipDao.isUserOwnerOfConcept(null, userId, conceptId)){
				// user has not right to update the concept
				this.handleMissingRight(locale, "ERROR_AUTHENTICATION_NO_UPDATE_RIGHT", conceptId, "update");
			}	
			
			IConcept oldConcept = this.conceptDao.selectConcept(null, conceptId);
			
			// check whether requester changed the concept's access rights
			if(conceptComparer.hasGroupAccessRightsChanged(oldConcept.getGroupAccessRights(), conceptToUpdate.getGroupAccessRights())){
				// access rights are changed -> checker if requester is allowed to change access rights
				if(!this.userDao.isUserSuperAdmin(null, userId) && !this.groupAccessRightDao.hasUserChangeRightsRightForConcept(null, userId, conceptId) && !this.ownershipDao.isUserOwnerOfConcept(null, userId, conceptId)){
					// user has not right to change access rights for the concept
					this.handleMissingRight(locale, "ERROR_AUTHENTICATION_NO_CHANGE_RIGHTS_RIGHT", conceptId, "change rights");
				}
			}
			
			// check if the requesters version of the concept is based on the newest one
			if(this.logDao.updatedSinceUserRed(unitOfWork, userId, conceptId)){
				// concept was already updated by another user, since requester has red it
				String message = "The concept " + conceptId + " could not be updated, because there was another update, since user " + userId + " red it!";
				String userMessage = LanguageHandler.getWord(locale, "ERROR_BAD_REQUEST_UPDATE_SINCE_LAST_READ");
				throw new BadRequestException(message, null, userMessage);
			}
			
			// set default description in JSON schema if needed
            if(conceptToUpdate.getValueSpace() != null){
                conceptToUpdate.getValueSpace().setDefaultDesciptionIfThereExistsNoOther(conceptToUpdate.getId());
            }
			
			// set requester as owner, if no owner is defined
			this.setRequesterAsOwnerIfNeeded(conceptToUpdate, userId);
			
			// execute update in database
			String note = conceptComparer.compareConcepts(oldConcept, conceptToUpdate);
			unitOfWork = this.conceptDao.createUnitOfWork();
			unitOfWork.start();
			this.conceptDao.updateConcept(unitOfWork, conceptToUpdate);
			this.insertLog(unitOfWork, System.currentTimeMillis(), conceptId, userId, ActionEnum.CREATE, true, note);
			IConcept updatedConcept = this.conceptDao.selectConcept(unitOfWork, conceptId);
			unitOfWork.finish();
			
			// build response
			response.status(200);
			String message = LanguageHandler.getWord(locale, "RESPONSE_JSON_PUT_CONCEPT");
			responseContent = jsonHandler.createPostAndPutConceptSuccessJsonResponse(updatedConcept, message);
			response.header("Content-Type", "application/json");				
			
		}catch (Exception e){
			responseContent = this.handleExpception(locale, unitOfWork, response, e, conceptId, userId, ActionEnum.UPDATE);
		}
		return responseContent;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.server.IJsonConceptController#deleteConceptAsJson(spark.Request, spark.Response)
	 */
	@Override
	public String deleteConceptAsJson(Request request, Response response){
		String responseContent = null;
		IUnitOfWork unitOfWork = null;
		Locale locale = null;
		IJsonHandler jsonHandler = null;
		int userId = -1;
		String conceptId = null;
		try {
			locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
			String apiKey = RequestUtil.getQueryParameterApiKey(locale, request);
			userId = AuthenticationController.ensureApiKeyIsValid(locale, apiKey);
			conceptId = RequestUtil.getPathParameterConceptId(locale, request);
			jsonHandler = new JsonHandler(locale);
			
			// only for logging
			this.logParamsForDeleteConceptAsJson(apiKey, userId, locale, conceptId);
			
			// throw exception when concept is not existing
			if(this.conceptDao.existsConcept(unitOfWork, conceptId) == false){
				// concept not found
				String conceptIdForErrorMessage = conceptId;
				conceptId = null;
				// TODO change response code to 404
				this.handleConceptNotFound(locale, "ERROR_BAD_REQUEST_CONCEPT_NOT_FOUND", conceptIdForErrorMessage);
			}
			
			// throw exception if user has no write right
			if(!this.userDao.isUserSuperAdmin(null, userId) && !this.groupAccessRightDao.hasUserDeleteRightForConcept(null, userId, conceptId) && !this.ownershipDao.isUserOwnerOfConcept(null, userId, conceptId)){
				// user has no write right
				this.handleMissingRight(locale, "ERROR_AUTHENTICATION_NO_DELETE_RIGHT", conceptId, "delete");
			}	
			
			// concept exists and user has write right -> delete concept from database
			unitOfWork = this.conceptDao.createUnitOfWork();
			unitOfWork.start();
			this.conceptDao.deleteConcept(unitOfWork, conceptId);
			this.insertLog(unitOfWork, conceptId, userId, ActionEnum.DELETE, true);
			unitOfWork.finish();
			
			// build response
			// TODO change response code to 204?
			response.status(200);
			responseContent = jsonHandler.createDeleteConceptSuccessJsonResponse(TemplateFiller.fillTemplate(LanguageHandler.getWord(locale, "RESPONSE_JSON_DELETE_CONCEPT"), conceptId));
			response.header("Content-Type", "application/json");
			
		}catch (Exception e){
			responseContent = this.handleExpception(locale, unitOfWork, response, e, conceptId, userId, ActionEnum.DELETE);
		}
		return responseContent;	
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.server.IJsonConceptController#getConceptAsJson(spark.Request, spark.Response)
	 */
	@Override
	public String getConceptAsJson(Request request, Response response){
		String responseContent = null;
		IUnitOfWork unitOfWork = null;
		Locale locale = null;
		IJsonHandler jsonHandler = null;
		int userId = -1;
		String conceptId = null;
		try {
			locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
			String apiKey = RequestUtil.getQueryParameterApiKey(locale, request);
			userId = AuthenticationController.ensureApiKeyIsValid(locale, apiKey);
			conceptId = RequestUtil.getPathParameterConceptId(locale, request);
			jsonHandler = new JsonHandler(locale);
			
			// only for logging
			this.logParamsForGetConceptAsJson(apiKey, userId, locale, conceptId);
			
			// throw exception when concept is not existing
			if(this.conceptDao.existsConcept(unitOfWork, conceptId) == false){
				// concept not found
				String conceptIdForErrorMessage = conceptId;
				conceptId = null;
				this.handleConceptNotFound(locale, "ERROR_BAD_REQUEST_CONCEPT_NOT_FOUND", conceptIdForErrorMessage);
			}
			
			// throw exception if user is not allowed to read the concept
			if(!this.userDao.isUserSuperAdmin(null, userId) && !this.groupAccessRightDao.hasUserReadRightForConcept(null, userId, conceptId) && !this.ownershipDao.isUserOwnerOfConcept(null, userId, conceptId)){
				this.handleMissingRight(locale, "ERROR_AUTHENTICATION_NO_READ_RIGHT", conceptId, "read");
			}
			
			// concept exists and user has read right -> select concept from database
			unitOfWork = this.conceptDao.createUnitOfWork();
			unitOfWork.start();
			long timeStamp = System.currentTimeMillis();
			IConcept concept = this.conceptDao.selectConcept(unitOfWork, conceptId);
			this.insertLog(unitOfWork, timeStamp, conceptId, userId, ActionEnum.READ, true);
			unitOfWork.finish();
			
			// build response
			response.status(200);
			responseContent = jsonHandler.createGetConceptSuccessJsonResponse(concept, timeStamp);
			response.header("Content-Type", "application/json");
			
		}catch (Exception e){
			responseContent = this.handleExpception(locale, unitOfWork, response, e, conceptId, userId, ActionEnum.READ);
		}
		return responseContent;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hdm.server.IJsonConceptController#getConceptsAsJson(spark.Request, spark.Response)
	 */
	@Override
	public Object getConceptsAsJson(Request request, Response response){
		String responseContent = null;
		IUnitOfWork unitOfWork = null;
		Locale locale = null;
		IJsonHandler jsonHandler = null;
		int userId = -1;
		String conceptId = null;
		try{
			locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
			String apiKey = RequestUtil.getQueryParameterApiKey(locale, request);
			userId = AuthenticationController.ensureApiKeyIsValid(locale, apiKey);
			GregorianCalendar updated = RequestUtil.getQueryParameterUpdated(locale, request);
			List<TypeEnum> types = RequestUtil.getQueryParameterTypes(locale, request);
			int offset = RequestUtil.getQueryParameterOffset(locale, request);
			int limit = RequestUtil.getQueryParameterLimit(locale, request);
			jsonHandler = new JsonHandler(locale);
			
			// only for logging
			this.logParamsForGetConceptsAsJson(apiKey, userId, locale, updated, types, offset, limit);
			
			IConceptFilter conceptFilter = new ConceptFilter(updated, types, limit, offset);
			
			// select concepts from database
			unitOfWork = this.conceptDao.createUnitOfWork();
			unitOfWork.start();
			long timeStamp = System.currentTimeMillis();
			List<IConcept> concepts = this.conceptDao.selectConcepts(unitOfWork, userId, conceptFilter);
			int totalNumberOfConcepts = this.conceptDao.selectTotalNumberOfConceptsForWhichUserHasReadRight(unitOfWork, userId);
			for(IConcept concept : concepts){
				this.insertLog(unitOfWork, timeStamp, concept.getId(), userId, ActionEnum.READ, true);
			}
			unitOfWork.finish();
			
			// build response
			response.status(200);
			responseContent = jsonHandler.createGetConceptsSuccessJsonResponse(concepts, conceptFilter, timeStamp, totalNumberOfConcepts);
			response.header("Content-Type", "application/json");
			
		}catch (Exception e){
			responseContent = this.handleExpception(locale, unitOfWork, response, e, conceptId, userId, ActionEnum.READ);
		}
		return responseContent;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hdm.server.jsonapi.IJsonConceptController#getNotFoundAsJson(spark.Request, spark.Response)
	 */
	@Override
	public Object getNotFoundAsJson(Request request, Response response){
	    Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
	    String responseContent = LanguageHandler.getWord(locale, "ERROR_BAD_REQUEST_INVALID_URL");
	    responseContent = TemplateFiller.fillTemplate(responseContent, request.url());
	    response.header("Content-Type", "text/plain");
        response.status(404);
        return responseContent;
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
	
	private void handleMissingRight(Locale locale, String templateKey, String conceptId, String missingRight) throws AuthenticationException{
		String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(locale, templateKey), conceptId);
		String message =  "Missing " + missingRight + " right for the concept with the id " + conceptId + "!";;
		throw new AuthenticationException(message, null, userMessage, false);
	}
	
	private void handleConceptNotFound(Locale locale, String templateKey, String conceptId) throws BadRequestException{
		String userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(locale, templateKey), conceptId);
		String message = "The concept with the id " + conceptId + " does not exist!";
		throw new BadRequestException(message, null, userMessage);
	}
	
	private String handleExpception(Locale locale, IUnitOfWork unitOfWork, Response response, Exception exception, String conceptId, int userId, ActionEnum action){
		if(unitOfWork != null && unitOfWork.getState() == UnitOfWorkStateEnum.STARTED){
			try {
				unitOfWork.abort();
			} catch (DataAccessException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		exception.printStackTrace();
		logger.error(exception.getMessage());
		
		try {
			String note = exception.getClass().getName() + ": " + exception.getMessage();
			this.insertLog(null, System.currentTimeMillis(), conceptId, userId, action, false, note);
		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error(exception.getMessage());
		}
		
		String commonMessageTemplateKey = null;
		int statusCode;
		
		if(exception instanceof BadRequestException){
			commonMessageTemplateKey = "ERROR_BAD_REQUEST_COMMON";
			statusCode = 403;
		}else if(exception instanceof AuthenticationException){
			commonMessageTemplateKey = "ERROR_AUTHENTICATION_COMMON";
			statusCode = 403;
		}else if(exception instanceof DataAccessException){
			commonMessageTemplateKey = "ERROR_DATABASE_COMMON";
			statusCode = 404;
		}else if(exception instanceof JsonHandlerException){
			commonMessageTemplateKey = "ERROR_BAD_REQUEST_JSON_DATA_COMMON";
			statusCode = 403;
		}else{
			commonMessageTemplateKey = "ERROR_COMMON";
			statusCode = 404;
		}
		
		String userMessage = null;
		if(exception instanceof RegistryServerException){
			userMessage = ((RegistryServerException)exception).getUserMessage();
		}
		if(userMessage == null || userMessage.isEmpty()){
			userMessage = LanguageHandler.getWord(locale, commonMessageTemplateKey);
		}
		response.status(statusCode);
		response.header("Content-Type", "text/plain");
		return userMessage;
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
	
	private void setRequesterAsOwnerIfNeeded(IConcept concept, int requesterId){
		if(concept.getOwners() == null || concept.getOwners().isEmpty()){
			if(concept.getOwners() == null){
				concept.setOwners(new ArrayList<Integer>());
			}
			concept.getOwners().add(requesterId);
		}
	}

	private void setCounterConceptIdIfNeeded(IUnitOfWork unitOfWork, IConcept newConcept, Locale locale) throws DataAccessException, BadRequestException {
		if(newConcept.getId() == null){
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
				String userMessage = LanguageHandler.getWord(locale, "ERROR_BAD_REQUEST_CONCEPT_ID_COULD_NOT_BE_GENERATED");
				throw new BadRequestException(message, null, userMessage);
			}
			newConcept.setId(generatedConceptId);
			if(newConcept.getGroupAccessRights() != null){
				for(IGroupAccessRight n : newConcept.getGroupAccessRights()){
					n.setConceptId(generatedConceptId);
				}	
			}
			this.setConceptIdInContents(newConcept.getNames(), newConcept.getId());
			this.setConceptIdInContents(newConcept.getDefinitions(), newConcept.getId());
			this.setConceptIdInContents(newConcept.getNotes(), newConcept.getId());
			this.setConceptIdInContents(newConcept.getExamples(), newConcept.getId());
		}
	}
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void setConceptIdInContents(List contents, String conceptId){
	    if(contents != null){
	        for(IContent content : (List<IContent>)contents){
	            content.setConceptId(conceptId);
	        }
	    }
	}
	
	// the following methods are only for logging
	
	private void logParamsForPostConceptAsJson(String apiKey, int userId, Locale locale) {
		this.logApiKeyUserIdAndAcceptedLanguages(apiKey, userId, locale);
	}
	
	private void logParamsForDeleteConceptAsJson(String apiKey, int userId, Locale locale, String conceptId) {
		this.logApiKeyUserIdAndAcceptedLanguages(apiKey, userId, locale);
		this.logConceptId(conceptId);
	}
	
	private void logParamsForGetConceptAsJson(String apiKey, int userId, Locale locale, String conceptId) {
		this.logApiKeyUserIdAndAcceptedLanguages(apiKey, userId, locale);
		this.logConceptId(conceptId);
	}

	private void logParamsForGetConceptsAsJson(String apiKey, int userId, Locale locale, GregorianCalendar updated, List<TypeEnum> types, int offset, int limit) {
		this.logApiKeyUserIdAndAcceptedLanguages(apiKey, userId, locale);
		if(updated != null){
			logger.info("updated = " + GregorianCalendarHelper.convertDateAndTimeToString(updated, "-", "T", ":"));
		}else{
			logger.info("updated = null");
		}
		if(types != null){
			for(TypeEnum type : types){
				logger.info("type = " + type.toString());
			}	
		}else{
			logger.info("type bzw. types = null");
		}
		logger.info("offset = " + offset);
		logger.info("limit = " + limit);
	}
	
	private void logApiKeyUserIdAndAcceptedLanguages(String apiKey, int userId, Locale locale){
		logger.info("apiKey = " + apiKey);
		logger.info("userId = " + userId);
		if(locale != null){
			logger.info("acceptedLanguage = " + locale.toString());
		}else{
			logger.info("acceptedLanguages = null");
		}
	}
	
	private void logConceptId(String conceptId){
		logger.info("conceptId = " + conceptId);
	}
	
	
	
	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
