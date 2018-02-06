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

import java.util.HashMap;
import java.util.Map;

import de.hdm.helpers.TemplateFiller;

public class TestTemplateFiller {

	public static void main(String[] args){
		System.out.println(TemplateFiller.fillTemplate("Hallo ${name}!", "Tobias"));
		System.out.println("-------------------------------------------------------");
		System.out.println(TemplateFiller.fillTemplate("Hallo ${name}", "Tobias"));
		System.out.println("-------------------------------------------------------");
		System.out.println(TemplateFiller.fillTemplate("${name} ist mein Name.", "Tobias"));
		System.out.println("-------------------------------------------------------");
		
		
		Map<String, String> values = new HashMap<String, String>();
		values.put("name", "Tobias");
		values.put("param", "param");
		System.out.println(TemplateFiller.fillTemplate("Hallo ${name}!", values));
		System.out.println("-------------------------------------------------------");
		System.out.println(TemplateFiller.fillTemplate("Hallo ${name}", values));
		System.out.println("-------------------------------------------------------");
		System.out.println(TemplateFiller.fillTemplate("${name} ist mein Name.", values));
		System.out.println("-------------------------------------------------------");
		System.out.println(TemplateFiller.fillTemplate("${pla} ist mein Name.", values));
		
		String test = "test";
		System.out.println(test.indexOf("t", 10));
	}
}
