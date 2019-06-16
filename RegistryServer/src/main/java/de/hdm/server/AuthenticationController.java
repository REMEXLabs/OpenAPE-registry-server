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

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;

import de.hdm.configuration.MyProperties;
import de.hdm.databaseaccess.DaoFactory;
import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IUserDao;
import de.hdm.datatypes.IUser;
import de.hdm.helpers.Checker;
import de.hdm.helpers.TemplateFiller;
import de.hdm.multiplelanguages.LanguageHandler;
import spark.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * This class provides methods for authentication via user name and password or an api key. The registry server needs
 * this class for the web interface and the JSON Rest api.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class AuthenticationController {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	
	/**
	 * Data access object for the users.
	 */
	private static IUserDao userDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createUserDao();


	private final static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

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

	/**
	 * Checks whether the api key is valid or not.
	 * @param locale user's preferred language for error messages
	 * @param apiKey the api key which should be checked
	 * @return If the api key is valid the user's id which belongs to it. If the api key is not valid an {@link AuthenticationException} will be thrown.
	 * @throws AuthenticationException if the api key is invalid
	 * @throws DataAccessException if an error with the database occurs during api key validation
	 */
	public static int ensureApiKeyIsValid(Locale locale, String apiKey) throws AuthenticationException, DataAccessException{
		Checker.checkNullAndEmptiness(apiKey, "apiKey");
		int userId = -1;
		try {
			userId = userDao.selectUserIdForApiKey(null, apiKey);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw e;
		}
		if(userId == -1){
			String userMessage = LanguageHandler.getWord(locale, "ERROR_AUTHENTICATION_API_KEY_INVALID");
			userMessage = TemplateFiller.fillTemplate(userMessage, apiKey);
			throw new AuthenticationException("Invalid API-Key!", null, userMessage, false);
		}
		return userId;
	}
	
	// TODO java doc
	public static IUser ensureUserIsLoggedIn(Request request) throws AuthenticationException{
		int userId = SessionUtil.getUserId(request);
		IUser user = null;
		if(userId == -1){
			throw new AuthenticationException("User is not logged in!", true);
		}else{
			try {
				String sessionId = SessionUtil.getSessionId(request);
				user = userDao.selectUser(null, userId);
				if(user.getSessionId() == null || user.getSessionId().equals(sessionId) == false){
					throw new AuthenticationException("User is not logged in!", true);
				}
				/*if(user.getSessionId().equals(sessionId) == false){
				    throw new AuthenticationException("User is not logged in!", true);
				}*/
			} catch (DataAccessException e) {
				e.printStackTrace();
				throw new AuthenticationException("Could not load user on account of an error with the databse!", true);
			}	
		}
		return user;
	}
	
	// TODO java doc
	public static boolean authenticate(String password, IUser user) throws AuthenticationException{
		Checker.checkNullAndEmptiness(password, "password");
		Checker.checkNull(user, "user");

		boolean passwordValid = false;
		try {
			if(PasswordEncoder.matches(password, user.getHashOfPassword())){
				passwordValid = true;
			}
		} catch (PasswordStorage.CannotPerformOperationException e) {
			e.printStackTrace();
			throw new AuthenticationException("Password could not be checked!", e, false);
		} catch (PasswordStorage.InvalidHashException e) {
			e.printStackTrace();
			throw new AuthenticationException("Password uses old hash format.", e, false);
		}

		return passwordValid;
	}
	
	// TODO java doc
	public static boolean authenticateViaResetPassword(IUser user, String resetPassword) throws AuthenticationException{
		Checker.checkNull(user, "user");
		Checker.checkNullAndEmptiness(resetPassword, "resetPassword");
		boolean resetPasswordValid = false;
		if(user.getExpiryDateOfResetPasswordInMilliseconds() >= System.currentTimeMillis()){
			try {
				if(PasswordEncoder.matches(resetPassword, user.getHashOfResetPassword())){
					System.out.println("reset password link valid");
					resetPasswordValid = true;
				}else{
					System.out.println("reset password link not valid");
				}
			} catch (PasswordStorage.CannotPerformOperationException | PasswordStorage.InvalidHashException e) {
				e.printStackTrace();
				throw new AuthenticationException("Reset password could not be checked!", e, false);
			}
		}else{
			//System.out.println("reset password link expired");
		}
		return resetPasswordValid;
	}
	
	// TODO java doc
	public static boolean isNewPasswordSecureEnough(String newPassword){
		Checker.checkNullAndEmptiness(newPassword, "newPassword");
		boolean secureEnough = false;
		if(newPassword.length() >= 8){
		    if(newPassword.matches(".*\\d+.*")){
		        boolean containsUppercase = !newPassword.equals(newPassword.toLowerCase());
		        boolean containsLowercase = !newPassword.equals(newPassword.toUpperCase());
		        if(containsLowercase && containsUppercase){
		            secureEnough = true;
		        }
		    }
		}
		return secureEnough;
	}
	
	// TODO java doc
	public static String hashNewPassword(String newPassword) throws PasswordStorage.CannotPerformOperationException {
		Checker.checkNullAndEmptiness(newPassword, "newPassword");
		return PasswordEncoder.encode(newPassword);
	}
	
	// TODO java doc
	public static String generateResetPassword(){
		SecureRandom secureRandom = new SecureRandom();
		String resetPassword = new BigInteger(700, secureRandom).toString(32);
		return resetPassword;
	}
	
	// TODO java doc
	public static String generateApiKey() throws DataAccessException, BadRequestException{
		boolean apiKeyAlreadyExists = true;
		int numberOfIterations = 0;
		String apiKey = null;
		while(apiKeyAlreadyExists == true && numberOfIterations < 3){
			SecureRandom secureRandom = new SecureRandom();
			apiKey = new BigInteger(700, secureRandom).toString(32);	
			if(userDao.selectUserIdForApiKey(null, apiKey) == -1){
				apiKeyAlreadyExists = false;
			}else{
				apiKey = null;
			}
			numberOfIterations++;
		}
		if(apiKey == null){
			throw new BadRequestException("Api-Key generation failed! Generated 3 times an already existing api-key.");
		}
		return apiKey;
	}

	
	

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
