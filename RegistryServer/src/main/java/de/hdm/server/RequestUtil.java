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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

import de.hdm.datatypes.TypeEnum;
import de.hdm.helpers.Checker;
import de.hdm.helpers.GregorianCalendarHelper;
import de.hdm.helpers.TemplateFiller;
import de.hdm.multiplelanguages.LanguageHandler;
import spark.Request;

/**
 * This class provides utility methods to handle a JSON http request.
 *
 * This class is thread safe.
 *
 * @author Tobias Ableitner
 *
 */
public class RequestUtil {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Value of the uri query parameter for the concept type {@link TypeEnum#NEED_AND_PREFERENCE}.
	 */
	private static final String CONCEPT_TYPE_NEED_AND_PREFERENCE_AS_URI_QUERY_PARAMETER_VALUE = "PreferenceStatement";

	/**
	 * Value of the uri query parameter for the concept type {@link TypeEnum#CONTEXT_DESCRIPTION}.
	 */
	private static final String CONCEPT_TYPE_CONTEXT_DESCRIPTION_AS_URI_QUERY_PARAMETER_VALUE = "ContextDescription";

	/**
	 * Value of the uri query parameter for the concept type {@link TypeEnum#RESOURCE_DESCRIPTION}.
	 */
	private static final String CONCEPT_TYPE_RESOURCE_DESCRIPTION_AS_URI_QUERY_PARAMETER_VALUE = "ResourceDescription";

	/**
	 * Regular expression to validate the dates, which are used in a request.
	 */
	private static final String REGEX_DATE = "[0-9]{4}-([1-9]|0[1-9]|1[0-2])-([1-9]|0[1-9]|(1[0-9]|2[0-9]|3[0-1]))";

	/**
	 * Regular expression to validate the times, which are used in a request.
	 */
	private static final String REGEX_TIME = "([0-9]|0[0-9]|1[0-9]|2[0-3]):([0-9]|0[0-9]|[1-5][0-9]):([0-9]|0[0-9]|[1-5][0-9])";

	/**
	 * Regular expression to validate the time zones, which are used in a request.
	 */
	private static final String REGEX_TIMEZONE = "(Z|(\\+|-)((([0-9]|0[0-9]|1[0-9]|2[0-3]):([0-9]|0[0-9]|[1-5][0-9]))|([0-9]|0[0-9]|1[0-9]|2[0-3])))";

	/**
	 * Regular expression to validate the date and time stamps or in other words the updated value, which are used in a
	 * request.
	 */
	private static final String REGEX_QUERY_PARAMETER_UPDATED = REGEX_DATE + "T" + REGEX_TIME + REGEX_TIMEZONE;




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
	 * Extracts the path parameter concept id.
	 *
	 * @param locale
	 *            user's preferred language for error messages
	 * @param request
	 *            the request
	 * @return path parameter concept id
	 * @throws BadRequestException
	 *             if path parameter concept id is missing.
	 */
	public static String getPathParameterConceptId(Locale locale, Request request) throws BadRequestException {
		String conceptId = request.params("conceptId");
		if (conceptId == null || conceptId.isEmpty()) {
			String userMessage = TemplateFiller.fillTemplate(
					LanguageHandler.getWord(locale, "ERROR_BAD_REQUEST_MISSING_URI_PATH_PARAMETER"), "conceptId");
			throw new BadRequestException("The uri path parameter conceptId is missing!", null, userMessage);
		}
		return conceptId;
	}

	/**
	 * Extracts the query parameter api key.
	 *
	 * @param locale
	 *            user's preferred language for error messages
	 * @param request
	 *            the request
	 * @return query parameter api key
	 * @throws BadRequestException
	 *             if query parameter api key is missing.
	 */
	public static String getQueryParameterApiKey(Locale locale, Request request) throws BadRequestException {
		return getQueryParameter(locale, request, "apiKey", true);
	}

	/**
	 * Extracts the query parameter updated.
	 *
	 * @param locale
	 *            user's preferred language for error messages
	 * @param request
	 *            the request
	 * @return query parameter updated or null, if the request does not contain it
	 * @throws BadRequestException
	 *             if query parameter updated value's is invalid. It is invalid it does not matches
	 *             {@link #REGEX_QUERY_PARAMETER_UPDATED}.
	 */
	public static GregorianCalendar getQueryParameterUpdated(Locale locale, Request request)
			throws BadRequestException {
		String updatedAsString = getQueryParameter(locale, request, "updated", false);
		GregorianCalendar updated = null;
		if (updatedAsString != null) {
			if (updatedAsString.matches(REGEX_QUERY_PARAMETER_UPDATED)) {
				updated = convertDateTimeStringFromRequestToGregorianCalendar(updatedAsString);
			} else {
				String userMessage = TemplateFiller.fillTemplate(
						LanguageHandler.getWord(locale, "ERROR_BAD_REQUEST_INVALID_URI_QUERY_PARAMETER_VALUE"),
						"updated", updatedAsString);
				throw new BadRequestException(
						"The value " + updatedAsString + " of the query parameter updated is invalid!", null,
						userMessage);
			}
		}
		return updated;
	}

	/**
	 * Extracts the query parameters type. This query parameter can occur multiple times in one request. Thus the
	 * value(s) are returned as list.
	 *
	 * @param locale
	 *            user's preferred language for error messages
	 * @param request
	 *            the request
	 * @return null if the request does not contain the query parameter type or a list with the values of it
	 * @throws BadRequestException
	 *             if a value of the query parameter type is not a valid concept type. This is the case, when the value
	 *             does not match one of the following three regular expressions
	 *             {@link #CONCEPT_TYPE_CONTEXT_DESCRIPTION_AS_URI_QUERY_PARAMETER_VALUE},
	 *             {@link #CONCEPT_TYPE_NEED_AND_PREFERENCE_AS_URI_QUERY_PARAMETER_VALUE} or
	 *             {@link #CONCEPT_TYPE_RESOURCE_DESCRIPTION_AS_URI_QUERY_PARAMETER_VALUE}.
	 */
	public static List<TypeEnum> getQueryParameterTypes(Locale locale, Request request) throws BadRequestException {
		String[] typeValues = request.queryParamsValues("type");
		List<TypeEnum> types = null;
		if (typeValues != null) {
			types = new ArrayList<TypeEnum>();
			for (String n : typeValues) {
				types.add(convertUriQueryParameterValueToConceptType(locale, n));
			}
		}
		return types;
	}

	/**
	 * Extracts the query parameter offset.
	 *
	 * @param locale
	 *            user's preferred language for error messages
	 * @param request
	 *            the request
	 * @return offset
	 * @throws BadRequestException
	 *             if the value of the query parameter offset is not a positive integer.
	 */
	public static int getQueryParameterOffset(Locale locale, Request request) throws BadRequestException {
		return getQueryParameterPositiveInteger(locale, request, "offset", false);
	}

	/**
	 * Extracts the query parameter limit.
	 *
	 * @param locale
	 *            user's preferred language for error messages
	 * @param request
	 *            the request
	 * @return limit
	 * @throws BadRequestException
	 *             if the value of the query parameter limit is not a positive integer.
	 */
	public static int getQueryParameterLimit(Locale locale, Request request) throws BadRequestException {
		return getQueryParameterPositiveInteger(locale, request, "limit", false);
	}

	/**
	 * Extracts user's preferred language.
	 *
	 * @param request
	 *            the request
	 * @return user's preferred language
	 */
	public static Locale getHeaderFieldAcceptLanguage(Request request) {
		HttpServletRequest httpServletRequest = request.raw();
		return httpServletRequest.getLocale();
	}

	public static String getQueryParameterLoginUserName(Locale locale, Request request) throws BadRequestException{
		return getQueryParameter(locale, request, "userName", false);
	}

	public static String getQueryParameterLoginPassword(Locale locale, Request request) throws BadRequestException{
		return getQueryParameter(locale, request, "password", false);
	}

	public static String getQueryParameterForgotPasswordUserName(Request request) throws BadRequestException{
		return getQueryParameter(null, request, "userName", false);
	}

	public static String getQueryParameterForgotPasswordUserName(Locale locale, Request request) throws BadRequestException{
		return getQueryParameter(locale, request, "userNameOrMailAddress", false);
	}

	public static String getQueryParameterResetPasswordUserName(Locale locale, Request request) throws BadRequestException{
		return getQueryParameter(locale, request, "userName", true);
	}

	public static String getQueryParameterResetPasswordResetPassword(Locale locale, Request request) throws BadRequestException{
		return getQueryParameter(locale, request, "resetPassword", true);
	}

	public static String getQueryParameterResetPasswordPassword1(Locale locale, Request request) throws BadRequestException{
		return getQueryParameter(locale, request, "password1", false);
	}

	public static String getQueryParameterResetPasswordPassword2(Locale locale, Request request) throws BadRequestException{
		return getQueryParameter(locale, request, "password2", false);
	}

	public static String getQueryParameterGroupName(Locale locale, Request request) throws BadRequestException{
		return getQueryParameter(locale, request, "groupName", true);
	}

	public static String getQueryParameterGroupMembers(Locale locale, Request request) throws BadRequestException{
		return getQueryParameter(locale, request, "groupMembers", false);
	}

	public static int getQueryParameterGroupId(Locale locale, Request request) throws BadRequestException{
		return getQueryParameterPositiveInteger(locale, request, "groupId", true);
	}

	public static String getQueryParameterUserNameForNewUser(Locale locale, Request request)throws BadRequestException{
		return getQueryParameter(locale, request, "userName", false);
	}

	public static String getQueryParameterFirstNameForNewUser(Locale locale, Request request)throws BadRequestException{
		return getQueryParameter(locale, request, "firstName", false);
	}

	public static String getQueryParameterLastNameForNewUser(Locale locale, Request request)throws BadRequestException{
		return getQueryParameter(locale, request, "lastName", false);
	}

	public static String getQueryParameterInstituteForNewUser(Locale locale, Request request)throws BadRequestException{
		return getQueryParameter(locale, request, "institute", false);
	}

	public static String getQueryParameterMailAddressForNewUser(Locale locale, Request request)throws BadRequestException{
		return getQueryParameter(locale, request, "mailAddress", false);
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

	public static boolean getBooleanFromCheckbox(Request request, String parameterName){
		boolean result = false;
		String parameterValueAsString = request.queryParams(parameterName);
		if(parameterValueAsString != null && parameterValueAsString.equals("on")){
			result = true;
		}
		return result;
	}

	public static String getStringFromRadioButtons(Request request, String parameterName) throws BadRequestException{
		String parameterValueAsString = request.queryParams(parameterName);
		if(parameterValueAsString == null || parameterValueAsString.isEmpty()){
			throw new BadRequestException("The value of the radio buttons " + parameterName + " is null or empty which is invalid!");
		}
		return parameterValueAsString;
	}

	public static boolean getBooleanFromRadioButtons(Request request, String parameterName) throws BadRequestException{
		boolean result;
		String parameterValueAsString = request.queryParams(parameterName);
		if(parameterValueAsString != null && parameterValueAsString.equals("yes")){
			result = true;
		}else if(parameterValueAsString != null && parameterValueAsString.equals("no")){
			result = false;
		}else{
			throw new BadRequestException("The value of the radio buttons " + parameterName + " is invalid!");
		}
		return result;
	}

	public static int getQueryParameterPositiveInteger(Locale locale, Request request, String parameterName,
			boolean throwExceptionIfMissing) throws BadRequestException {
		String integerAsString = getQueryParameter(locale, request, parameterName, throwExceptionIfMissing);
		int integer = -1;
		if (integerAsString != null) {
			if (Checker.isStringInteger(integerAsString) == false) {
				String userMessage = TemplateFiller.fillTemplate(
						LanguageHandler.getWord(locale, "ERROR_BAD_REQUEST_INVALID_URI_QUERY_PARAMETER_VALUE"),
						parameterName, integerAsString);
				throw new BadRequestException(
						"The value " + integerAsString + " of the query parameter " + parameterName + " is invalid!",
						null, userMessage);
			} else {
				integer = Integer.valueOf(integerAsString);
			}
		}
		return integer;
	}

	@Deprecated
	public static String getQueryParameter(Locale locale, Request request, String parameterName,
			boolean throwExceptionIfMissing) throws BadRequestException {
		return getQueryParameter(locale, request, parameterName, throwExceptionIfMissing, true);
	}

	public static String getQueryParameterOrNullIfEmpty(Request request, String parameterName) throws BadRequestException{
		String parameter = getQueryParameter(null, request, parameterName, false, false);
		if(parameter.isEmpty()){
			parameter = null;
		}
		return parameter;
	}

	public static String getQueryParameter(Locale locale, Request request, String parameterName,
			boolean throwExceptionIfMissing, boolean withUserMessage) throws BadRequestException {
		String parameter = request.queryParams(parameterName);
		/*System.out.println("parameter = " + parameter);
		if(parameter == null){
			System.out.println("query parameter " + parameterName + " is null");
		}
		if(parameter != null && parameter.isEmpty()){
			System.out.println("query parameter " + parameterName + " is empty");
		}*/
		if (throwExceptionIfMissing && (parameter == null || parameter.isEmpty())) {
			String userMessage = null;
			if(withUserMessage){
				userMessage = TemplateFiller.fillTemplate(LanguageHandler.getWord(locale, "ERROR_BAD_REQUEST_MISSING_URI_QUERY_PARAMETER"), parameterName);
			}
			throw new BadRequestException("The uri query parameter " + parameterName + " is missing!", null, userMessage);
		}
		return parameter;
	}

	private static TypeEnum convertUriQueryParameterValueToConceptType(Locale locale, String typeAsString)
			throws BadRequestException {
		TypeEnum type;
		switch (typeAsString) {
		case CONCEPT_TYPE_NEED_AND_PREFERENCE_AS_URI_QUERY_PARAMETER_VALUE:
			type = TypeEnum.NEED_AND_PREFERENCE;
			break;
		case CONCEPT_TYPE_CONTEXT_DESCRIPTION_AS_URI_QUERY_PARAMETER_VALUE:
			type = TypeEnum.CONTEXT_DESCRIPTION;
			break;
		case CONCEPT_TYPE_RESOURCE_DESCRIPTION_AS_URI_QUERY_PARAMETER_VALUE:
			type = TypeEnum.RESOURCE_DESCRIPTION;
			break;
		default:
			String userMessage = TemplateFiller.fillTemplate(
					LanguageHandler.getWord(locale, "ERROR_BAD_REQUEST_INVALID_URI_QUERY_PARAMETER_VALUE"), "type",
					typeAsString);
			throw new BadRequestException("The value " + typeAsString + " of the query parameter type is invalid!",
					null, userMessage);
		}
		return type;
	}

	/**
	 * Converts the date and time stamp from the request into an instance of {@link GregorianCalendar}.
	 *
	 * @param dateTimeString
	 *            the date and time stamp as string
	 * @return date and time stamp as instance of {@link GregorianCalendar}
	 */
	private static GregorianCalendar convertDateTimeStringFromRequestToGregorianCalendar(String dateTimeString) {
		return GregorianCalendarHelper.convertDateTimeStringFromRequestToGregorianCalendar(dateTimeString, "-", "T",
				":");
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
