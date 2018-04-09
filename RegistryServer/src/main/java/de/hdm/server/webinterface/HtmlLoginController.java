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

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hdm.configuration.MyProperties;
import de.hdm.databaseaccess.DaoFactory;
import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IUnitOfWork;
import de.hdm.databaseaccess.IUserDao;
import de.hdm.datatypes.IUser;
import de.hdm.exceptions.RegistryServerException;
import de.hdm.multiplelanguages.LanguageHandler;
import de.hdm.server.AuthenticationController;
import de.hdm.server.AuthenticationException;
import de.hdm.server.BadRequestException;
import de.hdm.server.MailUtil;
import de.hdm.server.Path;
import de.hdm.server.RequestUtil;
import de.hdm.server.SessionUtil;
import spark.Request;
import spark.Response;

public class HtmlLoginController extends HtmlController implements IHtmlLoginController {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	
    /**
     * Logger.
     */
    private final static Logger logger = LoggerFactory.getLogger(HtmlLoginController.class);
    
	private static final String MODEL_VALUE_KEY_AUTHENTICATION_FAILED = "authenticationFailed";
	
	private static final String MODEL_VALUE_KEY_MISSING_USER_NAME_MESSAGE = "missingUserNameMessage";
	
	private static final String MODEL_VALUE_KEY_UNKNOWN_USER_NAME = "userNameIsUnknown";
	
	private static final String MODEL_VALUE_KEY_USER_NAME = "userName";
	
	private static final String MODEL_VALUE_KEY_MISSING_PASSWORD_MESSAGE = "missingPasswordMessage";
	
	private static final String MODEL_VALUE_KEY_INVALID_PASSWORD = "passwordInvalid";
	
	private static final String MODEL_VALUE_KEY_REDIRECT = "redirect";
	
	private static final String MODEL_VALUE_KEY_MISSING_USER_NAME_OR_MAIL_ADDRESS_MESSAGE = "missingUserNameOrMailAddress";
	
	private static final String MODEL_VALUE_KEY_REQUESTED_NEW_PASSWORD = "requestedNewPassword";
	
	private static final String MODEL_VALUE_KEY_RESET_PASSWORD_LINK_IS_VALID = "resetPasswordLinkIsValid";
	
	private static final String MODEL_VALUE_KEY_NEW_PASSWORD_PROBLEM_MESSAGE = "newPasswordProblemMessage";
	
	private static final String MODEL_VALUE_KEY_USER_IS_LOCKED_MESSAGE = "userIsLockedMessage";
	
	private IUserDao userDao;


	
	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	public HtmlLoginController() {
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
	public String getLoginPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		
		Map<String, Object> model = new HashMap<String, Object>();
		if(SessionUtil.getSessionAttributeLoggedOut(request)){
			model.put(MODEL_VALUE_KEY_INFO_MESSAGE, LanguageHandler.getWord(locale, "WEB_LOGIN_PAGE_MESSAGE_LOGOUT"));
			SessionUtil.removeLoggedOut(request);
		}
		if(SessionUtil.getRedirect(request) != null){
		    model.put(MODEL_VALUE_KEY_ERROR_MESSAGE, LanguageHandler.getWord(locale, "WEB_SESSION_TIMEOUT_ERROR_MESSAGE"));
		}
		model.put(MODEL_VALUE_KEY_AUTHENTICATION_FAILED, false);
		model.put(MODEL_VALUE_KEY_UNKNOWN_USER_NAME, false);
		model.put(MODEL_VALUE_KEY_INVALID_PASSWORD, false);
		
		return ViewUtil.render(request, model, Path.WebTemplate.LOGIN, locale);
	}

	@Override
	public String handleLoginPost(Request request, Response response) {
		String result = null;
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String userName = RequestUtil.getQueryParameterLoginUserName(locale, request);
			String password = RequestUtil.getQueryParameterLoginPassword(locale, request);
			if((userName == null || userName.isEmpty()) && (password == null || password.isEmpty())){
				model.put(MODEL_VALUE_KEY_AUTHENTICATION_FAILED, true);
				String message = "The parameters userMame and password are missing!";
				model.put(MODEL_VALUE_KEY_MISSING_USER_NAME_MESSAGE, LanguageHandler.getWord(locale, "WEB_LOGIN_PAGE_ERROR_MESSAGE_NO_USER_NAME"));
				model.put(MODEL_VALUE_KEY_MISSING_PASSWORD_MESSAGE, LanguageHandler.getWord(locale, "WEB_LOGIN_PAGE_ERROR_MESSAGE_NO_PASSWORD"));
				throw new BadRequestException(message);
			}
			else if(userName == null || userName.isEmpty()){
				model.put(MODEL_VALUE_KEY_AUTHENTICATION_FAILED, true);
				String message = "The parameter userName is missing!";
				model.put(MODEL_VALUE_KEY_MISSING_USER_NAME_MESSAGE, LanguageHandler.getWord(locale, "WEB_LOGIN_PAGE_ERROR_MESSAGE_NO_USER_NAME"));
				throw new BadRequestException(message);
			}else if(password == null || password.isEmpty()){
				model.put(MODEL_VALUE_KEY_AUTHENTICATION_FAILED, true);
				String message = "The parameter password is missing!";
				model.put(MODEL_VALUE_KEY_MISSING_PASSWORD_MESSAGE, LanguageHandler.getWord(locale, "WEB_LOGIN_PAGE_ERROR_MESSAGE_NO_PASSWORD"));
				throw new BadRequestException(message);
			}
			
			// check if user exists
			IUser user = this.userDao.selectUser(null, userName);
			if(user == null){
				model.put(MODEL_VALUE_KEY_AUTHENTICATION_FAILED, true);
				model.put(MODEL_VALUE_KEY_UNKNOWN_USER_NAME, true);
				String message = "An user with the user name " + userName + " does not exist!";
				throw new AuthenticationException(message, false);
			}else{
				model.put(MODEL_VALUE_KEY_UNKNOWN_USER_NAME, false);
				model.put(MODEL_VALUE_KEY_USER_NAME, userName);
			}
			
			// authenticate
			if(AuthenticationController.authenticate(password, user) == false){
				user.setNumberOfFailedLogins(user.getNumberOfFailedLogins() + 1);
				if(user.getNumberOfFailedLogins() > MyProperties.getMaxNumberOfAllowedFailedLogins()){
				    user.setLocked(true);
				}
				if(user.isLocked() == false){
				    this.userDao.updateUser(null, user);
	                model.put(MODEL_VALUE_KEY_AUTHENTICATION_FAILED, true);
	                model.put(MODEL_VALUE_KEY_INVALID_PASSWORD, true);
	                String message = "The password for the user with the id " + user.getId() + " is wrong!";
	                throw new AuthenticationException(message, false);    
	            }
			}else if(user.isLocked() == false){
			    user.setNumberOfFailedLogins(0);
			}
			
			// check, whether user is locked or not
			if(user.isLocked()){
			    model.put(MODEL_VALUE_KEY_AUTHENTICATION_FAILED, true);
			    String userIsLockedMessage = LanguageHandler.getWord(locale, "WEB_LOGIN_PAGE_ERROR_MESSAGE_USER_IS_LOCKED");
			    model.put(MODEL_VALUE_KEY_USER_IS_LOCKED_MESSAGE, userIsLockedMessage);
			    model.put(MODEL_VALUE_KEY_USER_NAME, userName);
			    String message = "The user with the id " + user.getId() + " tried to login, but he is locked!";
			    throw new AuthenticationException(message, false);
			}
			
			// authentication was successful
			SessionUtil.setUserId(request, user.getId());
			
			// store session id in database to prevent account sharing
			user.setSessionId(SessionUtil.getSessionId(request));
			this.userDao.updateUser(null, user);
			
			// redirect or render welcome user view
			String redirect = SessionUtil.getRedirect(request);
			if(redirect != null){
			    SessionUtil.removeRedirect(request);
			    response.redirect(redirect);
			}else{
			    result = ViewUtil.render(request, model, Path.WebTemplate.WELCOME_USER, locale);    
			}
			
		} catch (BadRequestException e) {
			e.printStackTrace();
			result = ViewUtil.render(request, model, Path.WebTemplate.LOGIN, locale);
		} catch (DataAccessException e) {
			e.printStackTrace();
			String userMessage = LanguageHandler.getWord(locale, "WEB_LOGIN_PAGE_ERROR_MESSAGE_DATABASE");
			model.put(MODEL_VALUE_KEY_ERROR_MESSAGE, userMessage);
			result = ViewUtil.render(request, model, Path.WebTemplate.LOGIN, locale);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			result = ViewUtil.render(request, model, Path.WebTemplate.LOGIN, locale);
		}
		return result;
	}

	@Override
	public String handleLogoutPost(Request request, Response response) {
	    Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
	    String result = null;
		try {
			IUser user = AuthenticationController.ensureUserIsLoggedIn(request);
			SessionUtil.removeUserId(request);
			
			user.setSessionId(null);
			this.userDao.updateUser(null, user);
			SessionUtil.setSessionAttributeLoggedOut(request);
			response.redirect(Path.Web.LOGOUT_SUCCESS);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			this.handleAuthenticationException(request, response, e, locale);
		} catch (DataAccessException e) {
			// no special error handling needed, because the session id in the database will be overwritten, if the user logs in again
			e.printStackTrace();
			response.redirect(Path.Web.LOGOUT_SUCCESS);
		}
		return result;
	}

	@Override
	public String getForgotPasswordPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		try{
			String userName = RequestUtil.getQueryParameterForgotPasswordUserName(request);
			if(userName != null && userName.isEmpty() == false){
				model.put(MODEL_VALUE_KEY_USER_NAME, userName);
			}
		} catch (BadRequestException e) {
			// this exception will never been thrown, because RequestUtil will not throw an exception, if the query parameter userName is missing
			e.printStackTrace();
		}
		return ViewUtil.render(request, model, Path.WebTemplate.FORGOT_PASSWORD, locale);
	}

	@Override
	public String handleForgotPasswordPost(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		IUser user = null;
		try {
			String userName = RequestUtil.getQueryParameterForgotPasswordUserName(locale, request);
			if(userName == null || userName.isEmpty()){
				model.put(MODEL_VALUE_KEY_REQUESTED_NEW_PASSWORD, false);
				model.put(MODEL_VALUE_KEY_MISSING_USER_NAME_OR_MAIL_ADDRESS_MESSAGE, LanguageHandler.getWord(locale, "WEB_FORGOT_PASSWORD_PAGE_ERROR_MESSAGE_MISSING_USER_NAME"));
			}
			
			// check if user exists
			user = this.userDao.selectUser(null, userName);
			if(user == null){
				model.put(MODEL_VALUE_KEY_REQUESTED_NEW_PASSWORD, false);
				model.put(MODEL_VALUE_KEY_UNKNOWN_USER_NAME, true);
				String message = "An user with the user name " + userName + " does not exist!";
				throw new BadRequestException(message);
			}else{
				model.put(MODEL_VALUE_KEY_REQUESTED_NEW_PASSWORD, true);
				//model.put(MODEL_VALUE_KEY_USER_NAME, userName);
				String resetPassword = AuthenticationController.generateResetPassword();
				user.setHashOfResetPassword(AuthenticationController.hashNewPassword(resetPassword));
				user.setExpiryDateOfResetPasswordInMilliseconds(System.currentTimeMillis() + MyProperties.getResetPasswordPeriodOfValidityInSeconds() * 1000);
				String mailMessage = request.host() + "/web/resetPassword?userName=" + user.getUserName() + "&resetPassword=" + resetPassword;
				MailUtil.sendMail(user.geteMailAddress(), "Reset Registry-Server Password", mailMessage);
				this.userDao.updateUser(null, user);
			}
			
		} catch (BadRequestException e) {
			// this exception will never been thrown, because RequestUtil will not throw an exception, if the query parameter userName is missing
			e.printStackTrace();
		} catch (DataAccessException e) {
			e.printStackTrace();
			model.put(MODEL_VALUE_KEY_REQUESTED_NEW_PASSWORD, false);
			String userMessage = LanguageHandler.getWord(locale, "WEB_FORGOT_PASSWORD_PAGE_ERROR_MESSAGE_DATABASE");
			model.put(MODEL_VALUE_KEY_ERROR_MESSAGE, userMessage);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ViewUtil.render(request, model, Path.WebTemplate.FORGOT_PASSWORD, locale);
	}

	@Override
	public String getResetPasswordPage(Request request, Response response) {
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String userName = RequestUtil.getQueryParameterResetPasswordUserName(locale, request);
			String resetPassword = RequestUtil.getQueryParameterResetPasswordResetPassword(locale, request);
			
			// check if user exists
			IUser user = this.userDao.selectUser(null, userName);
			if(user == null){
			    String userMessage = LanguageHandler.getWord(locale, "WEB_RESET_PASSWORD_PAGE_ERROR_MESSAGE_INVALID_LINK");
			    throw new BadRequestException("The user " + userName + " does not exist!", null, userMessage);
			}
			
			// check reset password
			boolean resetPasswordValid = AuthenticationController.authenticateViaResetPassword(user, resetPassword);
			
			// remove hash of reset password
			if(user != null){
                user.setHashOfResetPassword(null);
                this.userDao.updateUser(null, user);
            }
			
			if(!resetPasswordValid){
				String userMessage = LanguageHandler.getWord(locale, "WEB_RESET_PASSWORD_PAGE_ERROR_MESSAGE_INVALID_LINK");
				throw new AuthenticationException("The reset password is invalid or expired!", null, userMessage, false);
			}
			
			request.session(true);
			HttpSession session = request.session().raw();
			session.setMaxInactiveInterval(MyProperties.getResetPasswordSessionTimeoutInSeconds());
			SessionUtil.setUserIdForPasswordReset(request, user.getId());
			model.put(MODEL_VALUE_KEY_RESET_PASSWORD_LINK_IS_VALID, true);
		} catch(Exception e){
		    model.put(MODEL_VALUE_KEY_RESET_PASSWORD_LINK_IS_VALID, false);
		    this.handleExpception(locale, null, e, model, request, response, "WEB_RESET_PASSWORD_PAGE_ERROR_MESSAGE_COMMON_GET");
		}
		return ViewUtil.render(request, model, Path.WebTemplate.RESET_PASSWORD, locale);
	}

	@Override
	public String handleResetPasswordPost(Request request, Response response) {
		String result = null;
		Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			int userId = SessionUtil.getUserIdForPasswordReset(request);
			if(userId == -1){
				String userMessage = LanguageHandler.getWord(locale, "WEB_RESET_PASSWORD_PAGE_ERROR_MESSAGE_SESSION_TIMEOUT");
			    throw new AuthenticationException("User is not logged in!", null, userMessage, false);
			}
			model.put(MODEL_VALUE_KEY_RESET_PASSWORD_LINK_IS_VALID, true);
			
			String password1 = RequestUtil.getQueryParameterResetPasswordPassword1(locale, request);
			String password2 = RequestUtil.getQueryParameterResetPasswordPassword2(locale, request);
			if(password1 == null || password2 == null || password1.isEmpty() || password2.isEmpty()){
				model.put(MODEL_VALUE_KEY_NEW_PASSWORD_PROBLEM_MESSAGE, LanguageHandler.getWord(locale, "WEB_RESET_PASSWORD_PAGE_ERROR_MESSAGE_PASSWORD_FIELD_EMPTY"));
				BadRequestException badRequestException = new BadRequestException("Both password input fields have to be filled out!");
				badRequestException.setShowUserMessage(false);
				throw badRequestException;
			}
			if(password1.equals(password2) == false){
				model.put(MODEL_VALUE_KEY_NEW_PASSWORD_PROBLEM_MESSAGE, LanguageHandler.getWord(locale, "WEB_RESET_PASSWORD_PAGE_ERROR_MESSAGE_PASSWORDS_NOT_EQUAL"));
				BadRequestException badRequestException = new BadRequestException("Password1 and Password2 are not equal!");
				badRequestException.setShowUserMessage(false);
                throw badRequestException;
			}
			if(AuthenticationController.isNewPasswordSecureEnough(password1) == false){
				model.put(MODEL_VALUE_KEY_NEW_PASSWORD_PROBLEM_MESSAGE, LanguageHandler.getWord(locale, "WEB_RESET_PASSWORD_PAGE_ERROR_MESSAGE_PASSWORD_NOT_SECURE_ENOUGH"));
				BadRequestException badRequestException = new BadRequestException("New password is not secure enough!");
				badRequestException.setShowUserMessage(false);
                throw badRequestException;
			}
			
			IUser user = this.userDao.selectUser(null, userId);
			if(user != null){
				user.setHashOfPassword(AuthenticationController.hashNewPassword(password1));
				this.userDao.updateUser(null, user);
			}else{
			    String userMessage = LanguageHandler.getWord(locale, "WEB_RESET_PASSWORD_PAGE_ERROR_MESSAGE_NO_RIGHT");
				throw new BadRequestException("User " + userId + " exists no more in the database!", null, userMessage);
			}
			
			result = ViewUtil.render(request, model, Path.WebTemplate.RESET_PASSWORD_SUCCESSFULLY, locale);
			
		} catch(Exception e){
		    this.handleExpception(locale, null, e, model, request, response, "WEB_RESET_PASSWORD_PAGE_ERROR_MESSAGE_COMMON_POST");
		    result = ViewUtil.render(request, model, Path.WebTemplate.RESET_PASSWORD, locale);
		}
		return result;
	}
	
    @Override
    public String welcomeUser(Request request, Response response) {
        Locale locale = RequestUtil.getHeaderFieldAcceptLanguage(request);
        // authentication
        try {
            AuthenticationController.ensureUserIsLoggedIn(request);
        } catch (AuthenticationException e) {
            this.handleExpception(locale, null, e, null, request, response, null);
        }
        return ViewUtil.render(request, null, Path.WebTemplate.WELCOME_USER, locale);
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
        }else if(exception instanceof RegistryServerException == false || ((RegistryServerException)exception).isShowUserMessage()){
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
