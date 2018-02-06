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
package de.hdm.multiplelanguages;

import java.util.Locale;

public class TestLanguageHandler {

	public static void main(String[] args) {
		String header = "de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4";
		//Locale locale = Locale.getLocaleObjFromAcceptLangHeader(header);
		
		Locale locale = new Locale("de", "DE");
		Locale locale1 = new Locale("en", "GB");
		Locale locale2 = new Locale("en", "GB");
		
		//LanguageHandler.getWord("Deutsch", "ERROR_AUTHENTICATION_COMMON");
		System.out.println(LanguageHandler.getWordByLocale(locale, "ERROR_AUTHENTICATION_COMMON"));
		
		System.out.println(locale1.equals(locale2));
		
		System.out.println(LanguageHandler.getWord("German", "ERROR_AUTHENTICATION_COMMON"));
		System.out.println(LanguageHandler.getWord("English", "ERROR_AUTHENTICATION_COMMON"));
		System.out.println(LanguageHandler.getWord("plapla", "ERROR_AUTHENTICATION_COMMON"));
		
		System.out.println(LanguageHandler.getWordByLocale(locale, "ERROR_AUTHENTICATION_COMMON"));
		System.out.println(LanguageHandler.getWordByLocale(locale1, "ERROR_AUTHENTICATION_COMMON"));

	}

}
