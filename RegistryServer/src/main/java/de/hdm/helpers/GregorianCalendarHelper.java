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
package de.hdm.helpers;

import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * This class converts dates and times from a String to a {@link GregorianCalendar} and the other way. The registry
 * server use's this class to convert the date time stamps of a request to a {@link GregorianCalendar} or in the other
 * way for a response. It also use's to transform the date and time stamps from the database to a
 * {@link GregorianCalendar} and the other way.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class GregorianCalendarHelper {

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
	
	/**
	 * Converts a {@link GregorianCalendar} instance into a date and time string. This string will contain year, month,
	 * day of month, hour, minute, and second. How the date and time values are separated can be decided by the caller
	 * of this method.
	 * Example: If the given date is 2017.04.29 and time is 11:23:34.234, the in date separator is "-", the in time
	 * separator is ":" and the dateTimeSeparator is " ", then the returned string is ""2017-04-29 11:23:34".
	 * 
	 * @param gregorianCalendar
	 *            the date and time.
	 * @param inDateSeparator
	 *            String to separate the values year, month and day of month. It must not be null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @param dateTimeSeparator
	 *            String to separate the date and time values. It must not be null or empty and unique in comparison to the separators. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @param inTimeSeparator
	 *            inTimeSeparator String to separate the values hour, minute, second. It must not be null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @return String containing the values year, month, day of month, hour, minute and second.
	 * @throws {@link
	 *             IllegalArgumentException} if one of the parameters is illicit null or the dateTimeSeparator is empty or not unique.
	 */
	public static String convertDateAndTimeToString(GregorianCalendar gregorianCalendar, String inDateSeparator,
			String dateTimeSeparator, String inTimeSeparator) {
		return convertDateAndTimeToString(gregorianCalendar, inDateSeparator, dateTimeSeparator, inTimeSeparator, false,
				".");
	}

	/**
	 * Converts a {@link GregorianCalendar} instance into a date and time string. This string will contain year, month,
	 * day of month, hour, minute, second and optionally fractional second. How the date, time and fractional second
	 * values are separated can be decided by the caller of this method.
	 * Example: If the given date is 2017.04.29 and time is 11:23:34.234, the in date separator is "-", the in time
	 * separator is ":", the dateTimeSeparator is " ", the fractionalSecondsSeparator is "." and withFractionalSeconds
	 * is true, then the returned string is ""2017-04-29 11:23:34.234".
	 * 
	 * @param gregorianCalendar
	 *            the date and time.
	 * @param inDateSeparator
	 *            String to separate the values year, month and day of month. It must not be null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @param dateTimeSeparator
	 *            String to separate the date and time values. It must not be null or empty and unique in comparison to the separators. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @param inTimeSeparator
	 *            inTimeSeparator String to separate the values hour, minute, second. It must not be null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @param withFractionalSeconds
	 *            true if the returned string should contain fractional seconds and false if not.
	 * @param fractionalSecondsSeparator
	 *            String to separate the fractional seconds from the other values. It must not be null, if the parameter
	 *            withFractionalSeconds is true. Otherwise an {@link IllegalArgumentException} will be thrown.
	 * @return String containing the values year, month, day of month, hour, minute, second and optionally fractional
	 *         second.
	 * @throws {@link
	 *             IllegalArgumentException} if one of the parameters is illicit null or the dateTimeSeparator is empty or not unique.
	 */
	public static String convertDateAndTimeToString(GregorianCalendar gregorianCalendar, String inDateSeparator,
			String dateTimeSeparator, String inTimeSeparator, boolean withFractionalSeconds,
			String fractionalSecondsSeparator) {
		Checker.checkNullAndEmptiness(dateTimeSeparator, "dateTimeSeparator");
		String result = convertDateToString(gregorianCalendar, inDateSeparator);
		result += dateTimeSeparator;
		result += convertTimeToString(gregorianCalendar, inTimeSeparator, withFractionalSeconds,
				fractionalSecondsSeparator);
		return result;
	}

	/**
	 * Converts a {@link GregorianCalendar} instance into a date string. The date string will only contain year, month
	 * and day of month. How this three values are separated can be decided by the caller of this method.
	 * Example: If the given date is 2017.04.29 and the in date separator is "-", then the returned string is
	 * "2017-04-29".
	 * 
	 * @param gregorianCalendar
	 *            the date. It must not be null. Otherwise an {@link IllegalArgumentException} will be thrown.
	 * @param inDateSeparator
	 *            String to separate the values year, month and day of month. It must not be null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @return String, containing the values year, month and day of month.
	 * @throws {@link
	 *             IllegalArgumentException} if one or both parameters are null.
	 */
	public static String convertDateToString(GregorianCalendar gregorianCalendar, String inDateSeparator) {
		Checker.checkNull(gregorianCalendar, "gregorianCalendar");
		Checker.checkNull(inDateSeparator, "inDateSeparator");
		String result = String.valueOf(gregorianCalendar.get(GregorianCalendar.YEAR));
		result += "-" + getGregorianCalendarValueWithLeadingZero(gregorianCalendar.get(GregorianCalendar.MONTH) + 1);
		result += "-" + getGregorianCalendarValueWithLeadingZero(gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH));
		return result;
	}

	/**
	 * Converts a {@link GregorianCalendar} instance into a time string. The time string will only contain hour, minute
	 * and second. How this three values are separated can be decided by the caller of this method.
	 * Example: If the given time is 11:23:34.234 and the in time separator is ":", then the returned string is
	 * "11:23:34".
	 * 
	 * @param gregorianCalendar
	 *            the time. It must not be null. Otherwise an {@link IllegalArgumentException} will be thrown.
	 * @param inTimeSeparator
	 *            the time String to separate the values hour, minute, second. It must not be null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @return String, containing the values hour, minute and second.
	 * @throws {@link
	 *             IllegalArgumentException} if one or both parameters are null.
	 */
	public static String convertTimeToStringWithoutFractionalSeconds(GregorianCalendar gregorianCalendar,
			String inTimeSeparator) {
		return convertTimeToString(gregorianCalendar, inTimeSeparator, false, ".");
	}

	/**
	 * Converts a {@link GregorianCalendar} instance into a time string. The time string will only contain hour, minute,
	 * second and fractional second. How the first three values are separated can be decided by the caller of this
	 * method by the parameter inTimSeparator. How the fractional seconds are separated from the other values can the
	 * caller of this method decide by the parameter fractionalSecondsParameter.
	 * Example: If the given time is 11:23:34.234, the in time separator is ":" and the fractionalSecondsSeparator is
	 * ".", then the returned string is "11:23:34.234".
	 * 
	 * @param gregorianCalendar
	 *            the time. It must not be null. Otherwise an {@link IllegalArgumentException} will be thrown.
	 * @param inTimeSeparator
	 *            String to separate the values hour, minute, second. It must not be null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @param fractionalSecondsSeparator
	 *            String to separate the fractional seconds from the other values. It must not be null. Otherwise an
	 *            {@link IllegalArgumentException} will be thrown.
	 * @return String, containing the values hour, minute, second and fractional second.
	 * @throws {@link
	 *             IllegalArgumentException} if one or more parameters are null.
	 */
	public static String convertTimeToStringWithFractionalSeconds(GregorianCalendar gregorianCalendar,
			String inTimeSeparator, String fractionalSecondsSeparator) {
		return convertTimeToString(gregorianCalendar, inTimeSeparator, true, fractionalSecondsSeparator);
	}
		
	public static GregorianCalendar convertDateTimeStringToGregorianCalendar(String dateTime, String inDateSeparator, String dateTimeSeparator, String inTimeSeparator, TimeZone timeZone){
		Checker.checkNullAndEmptiness(dateTime, "dateTime");
		Checker.checkNull(dateTimeSeparator, "dateTimeSeparator");
		Checker.checkNull(inDateSeparator, "inDateSeparator");
		Checker.checkNull(inTimeSeparator, "inTimeSeparator");
		if(dateTimeSeparator.equalsIgnoreCase(inDateSeparator) || dateTimeSeparator.equalsIgnoreCase(inTimeSeparator)){
			throw new RuntimeException("The parameter dateTimeSeparator has to differ from the parameters inDateSeparator and inTimeSeparator!");
		}
		String[] dateAndTime = dateTime.split(dateTimeSeparator);
		String[] dateValues = dateAndTime[0].split(inDateSeparator);
		String[] timeValues = dateAndTime[1].split(inTimeSeparator);
		int year = removeLeadingZerosAndConvertToInteger(dateValues[0]);
		int month = removeLeadingZerosAndConvertToInteger(dateValues[1]);
		month -= 1;
		int dayOfMonth = removeLeadingZerosAndConvertToInteger(dateValues[2]);
		int hourOfDay = removeLeadingZerosAndConvertToInteger(timeValues, 0);
		int minute = removeLeadingZerosAndConvertToInteger(timeValues, 1);
		int second = removeLeadingZerosAndConvertToInteger(timeValues, 2);
		int millisecond = removeLeadingZerosAndConvertToInteger(timeValues, 3);
		GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second);
		gregorianCalendar.set(GregorianCalendar.MILLISECOND, millisecond);
		if(timeZone != null){
			gregorianCalendar.setTimeZone(timeZone);
		}
		return gregorianCalendar;
	}
	
	/**
	 * Converts a date as string into an instance of {@link GregorianCalendar}. The date string has to contain the values year, month and day of month and also in the same order as written here.
	 * Example: If the inDateSeparator is "-", then dateAsString should look like "2017-04-29".
	 * @param date string containing year, month and day of month
	 * @param inDateSeparator String to separate the values year, month and day of month. It must not be null or empty. Otherwise an {@link IllegalArgumentException} will be thrown.
	 * @param timeZone the time zone. This parameter is optionally. If it is null, no time zone will be set. Instead {@link GregorianCalendar} will set the time zone by it self.
	 * @return instance of {@link GregorianCalendar} containing the date from the string
	 */
	public static GregorianCalendar convertDateStringToGregorianCalendar(String date, String inDateSeparator, TimeZone timeZone){
		Checker.checkNullAndEmptiness(date, "date");
		Checker.checkNullAndEmptiness(inDateSeparator, "inDateSeparator");
		String[] dateValues = date.split(inDateSeparator);
		int year = removeLeadingZerosAndConvertToInteger(dateValues[0]);
		int month = removeLeadingZerosAndConvertToInteger(dateValues[1]);
		month -= 1;
		int dayOfMonth = removeLeadingZerosAndConvertToInteger(dateValues[2]);
		GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, dayOfMonth);
		if(timeZone != null){
			gregorianCalendar.setTimeZone(timeZone);
		}
		return gregorianCalendar;
	}
	
	public static GregorianCalendar convertDateTimeStringFromRequestToGregorianCalendar(String dateTimeString, String inDateSeparator, String dateTimeSeparator, String inTimeSeparator){
		Checker.checkNullAndEmptiness(dateTimeString, "dateTimeString");
		Checker.checkNullAndEmptiness(dateTimeSeparator, "dateTimeSeparator");
		Checker.checkNullAndEmptiness(inDateSeparator, "inDateSeparator");
		Checker.checkNullAndEmptiness(inTimeSeparator, "inTimeSeparator");
		String dateTimeAsStringWithoutTimezone = null;
		String timezone = null;
		if(dateTimeString.contains("+")){
			dateTimeAsStringWithoutTimezone = dateTimeString.substring(0, dateTimeString.indexOf("+"));
			timezone = dateTimeString.substring(dateTimeString.indexOf("+"));
		}else if(dateTimeString.contains("Z")){
			dateTimeAsStringWithoutTimezone = dateTimeString.substring(0, dateTimeString.indexOf("Z"));
		}else if(dateTimeString.substring(dateTimeString.length() - 6, dateTimeString.length() - 1).contains("-")){
			dateTimeAsStringWithoutTimezone = dateTimeString.substring(0, dateTimeString.lastIndexOf("-"));
			timezone = dateTimeString.substring(dateTimeString.lastIndexOf("-"));
		}
		GregorianCalendar gregorianCalendar;
		if(dateTimeAsStringWithoutTimezone != null){
			//System.out.println("dateTimeAsStringWithoutTimezone != null");
			GregorianCalendar gregorianCalendarTmp = convertDateTimeStringToGregorianCalendar(dateTimeAsStringWithoutTimezone, inDateSeparator, dateTimeSeparator, inTimeSeparator, null);	
			if(timezone != null){
				gregorianCalendar = new GregorianCalendar();
				gregorianCalendar.setTimeInMillis(gregorianCalendarTmp.getTimeInMillis() + convertTimezoneStringToLong(timezone));	
			}else{
				gregorianCalendar = gregorianCalendarTmp;
			}
		}else{
			//System.out.println("dateTimeAsStringWithoutTimezone == null");
			gregorianCalendar = convertDateTimeStringToGregorianCalendar(dateTimeString, inDateSeparator, dateTimeSeparator, inTimeSeparator, null);
		}
		return gregorianCalendar;
	}
	
	/*public static GregorianCalendar convertDateTimeStringFromDatabaseToGregorianCalendar(String dateTimeString){
		//System.out.println("dateTimeString = " + dateTimeString);
		String dateTimeStringWithoutFractionalSecond;
		long fractionalSecondAsLong = 0;
		GregorianCalendar gregorianCalendar = null;
		if(dateTimeString.contains(".")){
			String fractionalSecondAsString = dateTimeString.substring(dateTimeString.indexOf(".") + 1);
			//System.out.println("fractionalSecondAsString = " + fractionalSecondAsString);
			fractionalSecondAsLong = Long.valueOf(removeLeadingZerosAndConvertToInteger(fractionalSecondAsString));
			dateTimeStringWithoutFractionalSecond = dateTimeString.substring(0, dateTimeString.indexOf("."));
			GregorianCalendar gregorianCalendarTmp = convertDateTimeStringFromRequestToGregorianCalendar(dateTimeStringWithoutFractionalSecond, "-", " ", ":");
			gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTimeInMillis(gregorianCalendarTmp.getTimeInMillis() + fractionalSecondAsLong);
		}else{
			dateTimeStringWithoutFractionalSecond = dateTimeString;
			gregorianCalendar = convertDateTimeStringFromRequestToGregorianCalendar(dateTimeStringWithoutFractionalSecond, "-", " ", ":");
		}
		
		return gregorianCalendar;
	}*/
	
	public static GregorianCalendar convertDateTimeStringFromDatabaseToGregorianCalendar(String dateTimeString){
		String dateTimeStringWithoutFractionalSecond;
		long fractionalSecondAsLong = 0;
		String fractionalSecondAsString = dateTimeString.substring(dateTimeString.indexOf(".") + 1);
		fractionalSecondAsLong = Long.valueOf(removeLeadingZerosAndConvertToInteger(fractionalSecondAsString));
		dateTimeStringWithoutFractionalSecond = dateTimeString.substring(0, dateTimeString.indexOf("."));
		GregorianCalendar gregorianCalendarTmp = convertDateTimeStringToGregorianCalendar(dateTimeStringWithoutFractionalSecond, "-", " ", ":", TimeZone.getTimeZone("UTC"));
		//gregorianCalendarTmp.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTimeInMillis(gregorianCalendarTmp.getTimeInMillis() + fractionalSecondAsLong);
		return gregorianCalendar;
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

	private static String convertTimeToString(GregorianCalendar gregorianCalendar, String inTimeSeparator, boolean withFractionalSeconds, String fractionalSecondsSeparator){
		Checker.checkNull(gregorianCalendar, "gregorianCalendar");
		Checker.checkNull(inTimeSeparator, "inTimeSeparator");
		if(withFractionalSeconds){
			Checker.checkNull(fractionalSecondsSeparator, "fractionalSecondsSeparator");	
		}
		String result = getGregorianCalendarValueWithLeadingZero(gregorianCalendar.get(GregorianCalendar.HOUR_OF_DAY));
		result += ":" + getGregorianCalendarValueWithLeadingZero(gregorianCalendar.get(GregorianCalendar.MINUTE));
		result += ":" + getGregorianCalendarValueWithLeadingZero(gregorianCalendar.get(GregorianCalendar.SECOND));
		if(withFractionalSeconds){
			String millisecondsAsString = String.valueOf(gregorianCalendar.get(GregorianCalendar.MILLISECOND));
			while(millisecondsAsString.length() < 3){
				millisecondsAsString = "0" + millisecondsAsString;
			}
			result += fractionalSecondsSeparator + millisecondsAsString;
		}
		return result;
		
	}
	
	private static String getGregorianCalendarValueWithLeadingZero(int value){
		String valueAsString = String.valueOf(value);
		if(value < 10){
			valueAsString = "0" + valueAsString;
		}
		return valueAsString;
	}
	
	private static int removeLeadingZerosAndConvertToInteger(String value){
		if(value.startsWith("0", 0) && value.length() > 1){
			value = value.substring(1);
		}
		return Integer.valueOf(value);
	}
	
	private static int removeLeadingZerosAndConvertToInteger(String[] values, int indexOfValue){
		int result = 0;
		if(indexOfValue < values.length){
			String value = values[indexOfValue];
			while(value.startsWith("0", 0) && value.length() > 1){
				value = value.substring(1);
			}
			result = Integer.valueOf(value);
		}
		return result;
	}

	private static long convertTimezoneStringToLong(String timezoneString){
		int distanceToUtcInMilliseconds = 0;
		String timezoneStringWithoutSign = timezoneString.substring(1);
		int hours = 0;
		int minutes = 0;
		if(timezoneStringWithoutSign.contains(":")){
			String[]hoursAndMinutes = timezoneStringWithoutSign.split(":");
			hours = removeLeadingZerosAndConvertToInteger(hoursAndMinutes[0]);
			minutes = removeLeadingZerosAndConvertToInteger(hoursAndMinutes[1]);
		}else{
			hours = removeLeadingZerosAndConvertToInteger(timezoneStringWithoutSign);
		}
		distanceToUtcInMilliseconds += hours * 60 * 60 * 1000;
		distanceToUtcInMilliseconds += minutes * 60 * 1000;
		if(timezoneString.contains("+")){
			distanceToUtcInMilliseconds *= -1;
		}
		return distanceToUtcInMilliseconds;
	}


	
	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
