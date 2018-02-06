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
package de.hdm.helpers.test;

import java.util.GregorianCalendar;
import java.util.TimeZone;

import de.hdm.helpers.GregorianCalendarHelper;

public class TestGregorianCalendarHelper {

	public static void main(String[] args){
		String date = "2010-10-31";
		String time = "23:54:50";
		String timezone = "+01:00";
		String dateTimeTimezone = date + "T" + time + timezone; 
		String regexDate = "[0-9]{4}-([1-9]|0[1-9]|1[0-2])-([1-9]|0[1-9]|(1[0-9]|2[0-9]|3[0-1]))";
		String regexTime = "([0-9]|0[0-9]|1[0-9]|2[0-3]):([0-9]|0[0-9]|[1-5][0-9]):([0-9]|0[0-9]|[1-5][0-9])";
		
		String regexTimezone = "(Z|(\\+|-)((([0-9]|0[0-9]|1[0-9]|2[0-3]):([0-9]|0[0-9]|[1-5][0-9]))|([0-9]|0[0-9]|1[0-9]|2[0-3])))";
		
		System.out.println(date.matches(regexDate));
		System.out.println(time.matches(regexTime));
		System.out.println(timezone.matches(regexTimezone));
		System.out.println(dateTimeTimezone.matches(regexDate+"T"+regexTime+regexTimezone));
		
		
		
		String date1 = "2010-10-01T23:30:44:009";
		GregorianCalendar g1 = GregorianCalendarHelper.convertDateTimeStringToGregorianCalendar(date1, "-", "T", ":", null);
		System.out.println(GregorianCalendarHelper.convertDateAndTimeToString(g1, "-", "T", ":"));
		
		String date2 = "2010-10-01T23:30";
		GregorianCalendar g2 = GregorianCalendarHelper.convertDateTimeStringToGregorianCalendar(date2, "-", "T", ":", null);
		System.out.println(GregorianCalendarHelper.convertDateAndTimeToString(g2, "-", "T", ":"));
		
		/*String date3 = "2010-10-01T23:30:00+01:30";
		GregorianCalendar g3 = GregorianCalendarHelper.convertDateTimeStringFromRequestToGregorianCalendar(date3);
		System.out.println(GregorianCalendarHelper.convertDateAndTimeToString(g3, "-", "T", ":"));
		
		String date4 = "2010-10-01T23:30:00+01";
		GregorianCalendar g4 = GregorianCalendarHelper.convertDateTimeStringFromRequestToGregorianCalendar(date4);
		System.out.println(GregorianCalendarHelper.convertDateAndTimeToString(g4, "-", "T", ":"));
		
		String date5 = "2010-10-01T23:30:00-01:00";
		GregorianCalendar g5 = GregorianCalendarHelper.convertDateTimeStringFromRequestToGregorianCalendar(date5);
		System.out.println(GregorianCalendarHelper.convertDateAndTimeToString(g5, "-", "T", ":"));
		
		String date6 = "2010-10-01T23:30:00Z";
		GregorianCalendar g6 = GregorianCalendarHelper.convertDateTimeStringFromRequestToGregorianCalendar(date6);
		System.out.println(GregorianCalendarHelper.convertDateAndTimeToString(g6, "-", "T", ":"));
		*/
		GregorianCalendar g7 = new GregorianCalendar();
		g7.setTimeInMillis(System.currentTimeMillis());
		
		System.out.println("System.currentTimeMillis()=" + System.currentTimeMillis());
		System.out.println("g7.getTimeInMillis()=" + g7.getTimeInMillis());
		
		GregorianCalendar g8 = GregorianCalendarHelper.convertDateTimeStringFromDatabaseToGregorianCalendar("2017-04-25 16:34:08.260");
		System.out.println("g8.getTimeInMillis()=" + g8.getTimeInMillis());
		
		GregorianCalendar g9 = new GregorianCalendar(2017, 3, 25, 18, 0, 0);
		//System.out.println("g9.getTimeInMillis()=" + g9.getTimeInMillis());
		g9.setTimeZone(TimeZone.getTimeZone("UTC"));
		System.out.println("g9.getTimeInMillis()=" + g9.getTimeInMillis());
		
		//String tmp = GregorianCalendarHelper.convertGregorianCalendarToStringForJsonResponse(g7);
		//System.out.println(GregorianCalendarHelper.convertGregorianCalendarToStringForJsonResponse(g7));
		//System.out.println(System.currentTimeMillis());
		//System.out.println(GregorianCalendarHelper.convertGregorianCalendarToStringForJsonResponse(GregorianCalendarHelper.convertDateTimeStringFromRequestToGregorianCalendar("2017-03-29T19:38:54")));
	
		GregorianCalendar gt = new GregorianCalendar();
		System.out.println(gt.getTimeZone().getID());
	}
}
