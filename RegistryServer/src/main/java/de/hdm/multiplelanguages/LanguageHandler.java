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

import java.util.*;

import de.hdm.configuration.MyProperties;
import de.hdm.helpers.Checker;

/**
 * This class handles the multiple language support for the application. Therefore it uses the Java language property
 * files. The default language is English. Thus if the translation of a word in an unsupported language is requested,
 * the English version will be returned.
 * 
 * @author Tobias Ableitner
 *
 */
public class LanguageHandler {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	
	/**
	 * When it is true, all language property files are loaded at the beginning. Otherwise they are loaded on demand.
	 */
	private static boolean CACHE_ENABLED = MyProperties.getCacheLanguages();

	/**
	 * Contains the supported languages.
	 */
	private static Map<String, Locale> languages = new HashMap<String, Locale>();

	/**
	 * Contains the loaded language property files if {@link #CACHE_LANGUAGES} is true.
	 */
	private static Map<Locale, ResourceBundle> cache = new HashMap<Locale, ResourceBundle>();



	
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Initializes the language handling. Supported languages are inserted into {@link #languages}.
	 */
	static {
		// add here additional languages

	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// getters and setters
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
	 * Creates a {@link #SingleLanguageHandler} with the preferred or default language, if the former is not supported.
	 * 
	 * @param language
	 *            the preferred language
	 * @return instance of {@link #SingleLanguageHandler}
	 */
	public static SingleLanguageHandler createSingleLanguageHandler(String language) {
		Locale locale = findLocale(language);
		return createSingleLanguageHandler(locale);
	}

	/**
	 * Creates a {@link #SingleLanguageHandler} with the preferred or default language, if the former is not supported.
	 * 
	 * @param locale
	 *            the preferred language
	 * @return instance of {@link #SingleLanguageHandler}
	 */
	public static SingleLanguageHandler createSingleLanguageHandler(Locale locale) {
		ResourceBundle resourceBundle = null;

		if (CACHE_ENABLED) {
			resourceBundle = cache.get(locale);
		}

		if (resourceBundle == null) {
			try {
				resourceBundle = ResourceBundle.getBundle("language", locale);
			}  catch (Exception e) {
				resourceBundle = ResourceBundle.getBundle("language", new ResourceBundle.Control() {
							@Override
							public List<Locale> getCandidateLocales(String name, Locale locale) {
								return Collections.singletonList(Locale.ROOT);
							}
						});
			}
		}

		if (CACHE_ENABLED) {
			cache.put(locale, resourceBundle);
		}

		return new SingleLanguageHandler(resourceBundle);
	}
	
	public static String getWord(String language, String key){
		return getWordByLanguageString(language, key);		
	}
	
	public static String getWord(Locale locale, String key){
		return getWordByLocale(locale, key);
	}

	/**
	 * Finds a word in a preferred language. If the language is not equal to one of the keys in {@link #languages} the default
	 * language, which is defined in {@link #DEFAULT_LOCALE} will be used instead. If the searched key is not included
	 * in at least the base language file, a {@link #java.util.MissingResourceException} will be thrown and terminating
	 * the application.
	 * 
	 * @param language
	 *            preferred language
	 * @param key
	 *            the key of the word
	 * @return searched word in the preferred or default language
	 */
	public static String getWordByLanguageString(String language, String key) {
		Locale locale = findLocale(language);
		return getWordByLocale(locale, key);
	}

	/**
	 * Finds a word in a preferred language. If locale is not equal to one of the keys in {@link #translations} the
	 * default language, which is defined in {@link #DEFAULT_LOCALE} will be used instead.
	 * 
	 * @param locale
	 *            preferred language
	 * @param key
	 *            the key of the word
	 * @return searched word in the preferred or default language
	 * @throws {@link
	 *             #java.util.MissingResourceException} if the key is not included in at least the base language file
	 */
	public static String getWordByLocale(Locale locale, String key) {
		Checker.checkNull(locale, "locale");
		Checker.checkNullAndEmptiness(key, "key");
		ResourceBundle resourceBundle = null;
		if (CACHE_ENABLED) {
			resourceBundle = cache.get(locale);
		}
		if(resourceBundle == null){
			resourceBundle = ResourceBundle.getBundle("language", locale);
			if(CACHE_ENABLED){
				cache.put(locale, resourceBundle);
			}
		}
		return resourceBundle.getString(key);
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

	/**
	 * Returns the instance of {@link #java.util.Locale}, which belongs to the preferred language. If it is not
	 * supported, a empty Locale instance is returned which will result in using the default language file.
	 * 
	 * @param language
	 *            the preferred language
	 * @return instance of {@link #java.util.Locale} which belongs to the preferred or default language
	 */
	private static Locale findLocale(String language) {
		Locale locale = null;

		if(language != null && language.isEmpty() == false){
			locale = languages.get(language);	
		}
		if (locale == null) {
			// if language is unknown use default language file
			locale = new Locale("");
		}
		return locale;
	}




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
