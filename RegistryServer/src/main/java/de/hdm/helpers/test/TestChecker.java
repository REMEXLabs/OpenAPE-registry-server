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

import java.util.regex.Pattern;

import de.hdm.helpers.Checker;

public class TestChecker {

	public static void main(String[] args) {
		/*System.out.println(Checker.isStringInteger("01"));
		System.out.println(Checker.isStringInteger("00"));
		System.out.println(Checker.isStringInteger(""));
		System.out.println(Checker.isStringInteger("a"));
		System.out.println(Checker.isStringInteger("0"));
		System.out.println(Checker.isStringInteger("1"));
		System.out.println(Checker.isStringInteger("10"));
		System.out.println(Checker.isStringInteger("2034"));*/
		
		
		
		String regex = "[^ ]+([ ]admin)?";
		String line1 = "dd";
		System.out.println(line1.matches(regex));
		String line2 = "d";
		System.out.println(line2.matches(regex));
		String line3 = "d d";
		System.out.println(line3.matches(regex));
		String line4 = "";
		System.out.println(line4.matches(regex));
		String line5 = " ";
		System.out.println(line5.matches(regex));
		String line6 = "skjdf ";
		System.out.println(line6.matches(regex));
		String line7 = "dkjd adm";
		System.out.println(line7.matches(regex));
		String line8 = "dfkh admin ";
		System.out.println(line8.matches(regex));
		String line9 = "sddhd adminn";
		System.out.println(line9.matches(regex));
		String line10 = "djsh admin";
		System.out.println(line10.matches(regex));
		
		String line = "user admin";
		System.out.println(line.substring(0, line.indexOf(" ")));
		
		String regex2 = "[^ ]+[ ]admin[ ]";
		String line11  = "user admin ";
		System.out.println(line11.matches(regex2));
		String line12  = "user admin";
		System.out.println(line12.matches(regex2));
	}

}
