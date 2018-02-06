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

import java.util.List;
import java.util.Map;

/**
 * This class provides different methods to fill a template, which contains place holders, with values. The registry
 * server use's this class to fill error and other messages for the user with values. For example an error message
 * template for the case, that a requested concept id does not exist, with the requested concept id.
 * Such a template can look like "A concept with the id ${conceptId} does not exist!" After calling one of the methods
 * from this class, the error message, based on the example template, would look like
 * "A concept with the id C12345 does not exist!"
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class TemplateFiller {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Default begin tag of a place holder.
	 */
	public static final String DEFAULT_PLACE_HOLDER_BEGIN = "${";

	/**
	 * Default end tag of a place holder.
	 */
	public static final String DEFAULT_PLACE_HOLDER_END = "}";




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
	 * Fills a template with values by using the default place holder begin and end tags of the class
	 * {@link TemplateFiller} which are
	 * defined in {@link #DEFAULT_PLACE_HOLDER_BEGIN} and {@link #DEFAULT_PLACE_HOLDER_END}. The place holders in the
	 * template will be filled with values of the list values. The first place holder will be replaced with the value at
	 * index 0 in the list values and so on. Place holders which are not equal to the default place holders will be
	 * ignored. This method will replace as many place holders as values in the list values are available. This means,
	 * that if the list values contains only 3 values but the template 5 place holders, the last two of the latter will
	 * not be replaced.
	 * 
	 * @param template
	 *            the template
	 * @param values
	 *            list of values
	 * @return template filled with the values
	 * @throws @throws
	 *             IllegalArgumentException if template and / or values are null or template is empty
	 */
	public static String fillTemplate(String template, List<String> values) {
		Checker.checkNull(values, "values");
		return fillTemplate(template, DEFAULT_PLACE_HOLDER_BEGIN, DEFAULT_PLACE_HOLDER_END,
				(String[]) values.toArray());
	}

	/**
	 * Fills a template with values by using user defined place holder begin and end tags. The place holders in the
	 * template will be filled with values of the list values. The first place holder will be replaced with the value at
	 * index 0 in the list values and so on. Place holders which are not equal to the default place holders will be
	 * ignored. This method will replace as many place holders as values in the list values are available. This means,
	 * that if the list values contains only 3 values but the template 5 place holders, the last two of the latter will
	 * not be replaced.
	 * 
	 * @param template
	 *            the template
	 * @param placeHolderBegin
	 *            begin tag of a place holder
	 * @param placeHolderEnd
	 *            end tag of a place holder
	 * @param values
	 *            list of values
	 * @return template filled with the values
	 * @throws @throws
	 *             IllegalArgumentException if template, placeHolderBegin, placeHolderEnd is null or empty or values is
	 *             null
	 */
	public static String fillTemplate(String template, String placeHolderBegin, String placeHolderEnd,
			List<String> values) {
		Checker.checkNull(values, "values");
		return fillTemplate(template, placeHolderBegin, placeHolderEnd, (String[]) values.toArray());
	}

	/**
	 * Fills a template with values by using the default place holder begin and end tags of the class
	 * {@link TemplateFiller} which are
	 * defined in {@link #DEFAULT_PLACE_HOLDER_BEGIN} and {@link #DEFAULT_PLACE_HOLDER_END}. The place holders in the
	 * template will be filled with values of the list values. The first place holder will be replaced with the value at
	 * index 0 in the list values and so on. Place holders which are not equal to the default place holders will be
	 * ignored. This method will replace as many place holders as values in the list values are available. This means,
	 * that if the list values contains only 3 values but the template 5 place holders, the last two of the latter will
	 * not be replaced.
	 * 
	 * @param template
	 *            the template
	 * @param values
	 *            the values which will replace the place holders
	 * @return template filled with the values
	 * @throws IllegalArgumentException
	 *             if template is null or empty or values is
	 *             null
	 */
	public static String fillTemplate(String template, String... values) {
		return fillTemplate(template, DEFAULT_PLACE_HOLDER_BEGIN, DEFAULT_PLACE_HOLDER_END, values);
	}

	/**
	 * Fills a template with values by using user defined place holder begin and end tags. The place holders in the
	 * template will be filled with values of the array values. The first place holder will be replaced with the value
	 * at
	 * index 0 in the array values and so on. Place holders which are not equal to the default place holders will be
	 * ignored. This method will replace as many place holders as values in the array values are available. This means,
	 * that if the list values contains only 3 values but the template 5 place holders, the last two of the latter will
	 * not be replaced.
	 * 
	 * @param template
	 *            the template
	 * @param placeHolderBegin
	 *            begin tag of a place holder
	 * @param placeHolderEnd
	 *            end tag of a place holder
	 * @param values
	 *            the values which will replace the place holders
	 * @return template filled with the values
	 * @throws IllegalArgumentException
	 *             if template, placeHolderBegin, placeHolderEnd is null or empty or values is
	 *             null
	 */
	public static String fillTemplate(String template, String placeHolderBegin, String placeHolderEnd,
			String[] values) {
		Checker.checkNullAndEmptiness(template, "template");
		Checker.checkNullAndEmptiness(placeHolderBegin, "placeHolderBegin");
		Checker.checkNullAndEmptiness(placeHolderEnd, "placeHolderEnd");
		Checker.checkNull(values, "values");
		String filledTemplate = template;
		for (String value : values) {
			int startIndex = filledTemplate.indexOf(placeHolderBegin);
			int endIndex = filledTemplate.indexOf(placeHolderEnd);
			// System.out.println("startIndex = " + startIndex);
			// System.out.println("endIndex = " + endIndex);
			String tmpBefore = "";
			if (startIndex != 0) {
				tmpBefore = filledTemplate.substring(0, startIndex);
			}
			String tmpAfter = filledTemplate.substring(endIndex + 1);
			filledTemplate = tmpBefore + value + tmpAfter;
			// System.out.println("tmpBefore = " + tmpBefore);
			// System.out.println("tmpAfter = " + tmpAfter);
		}
		return filledTemplate;
	}

	/**
	 * Fills a template with values by using the default place holder begin and end tags of the class
	 * {@link TemplateFiller} which are defined in {@link #DEFAULT_PLACE_HOLDER_BEGIN} and
	 * {@link #DEFAULT_PLACE_HOLDER_END}. This method will parse the
	 * template, looking for place holders and using their text between a begin and end tag as key to get a value from
	 * the map values to replace the place holder. If the map values does not contain the key of a place holder, it will
	 * not be replaced. It is possible to use the same key in more than one place holder.
	 * 
	 * @param template
	 *            the template
	 * @param values
	 *            values which will replace the place holders as key value pairs
	 * @return template filled with the values
	 * @throws IllegalArgumentException
	 *             if template is null or empty or values is
	 *             null
	 */
	public static String fillTemplate(String template, Map<String, String> values) {
		return fillTemplate(template, DEFAULT_PLACE_HOLDER_BEGIN, DEFAULT_PLACE_HOLDER_END, values);
	}

	/**
	 * Fills a template with values by using user defined place holder begin and end tags. This method will parse the
	 * template, looking for place holders and using their text between a begin and end tag as key to get a value from
	 * the map values to replace the place holder. If the map values does not contain the key of a place holder, it will
	 * not be replaced. It is possible to use the same key in more than one place holder.
	 * 
	 * @param template
	 *            the template
	 * @param placeHolderBegin
	 *            begin tag of a place holder
	 * @param placeHolderEnd
	 *            end tag of a place holder
	 * @param values
	 *            values which will replace the place holders as key value pairs
	 * @return template filled with the values
	 * @throws IllegalArgumentException
	 *             if template, placeHolderBegin, placeHolderEnd is null or empty or values is
	 *             null
	 */
	public static String fillTemplate(String template, String placeHolderBegin, String placeHolderEnd,
			Map<String, String> values) {
		Checker.checkNullAndEmptiness(template, "template");
		Checker.checkNullAndEmptiness(placeHolderBegin, "placeHolderBegin");
		Checker.checkNullAndEmptiness(placeHolderEnd, "placeHolderEnd");
		Checker.checkNull(values, "values");

		String filledTemplate = template;
		int startIndex = 0;
		int endIndex = 0;
		while (startIndex > -1 && endIndex > -1) {
			startIndex = filledTemplate.indexOf(placeHolderBegin, startIndex);
			endIndex = filledTemplate.indexOf(placeHolderEnd, endIndex);
			// System.out.println("startIndex = " + startIndex);
			// System.out.println("endIndex = " + endIndex);
			if (startIndex > -1 && endIndex > -1) {
				String tmpBefore = "";
				if (startIndex != 0) {
					tmpBefore = filledTemplate.substring(0, startIndex);
				}
				String tmpAfter = filledTemplate.substring(endIndex + 1);
				String key = filledTemplate.substring(startIndex + placeHolderBegin.length(), endIndex);
				String value = values.get(key);
				if (value == null) {
					value = placeHolderBegin + key + placeHolderEnd;
				}
				filledTemplate = tmpBefore + value + tmpAfter;
				// System.out.println("key = " + key);
				// System.out.println("value = " + value);
				// System.out.println("tmpBefore = " + tmpBefore);
				// System.out.println("tmpAfter = " + tmpAfter);

				// increment to jump over keys which are not contained in the values map
				startIndex++;
				endIndex++;
			}
		}
		return filledTemplate;
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
