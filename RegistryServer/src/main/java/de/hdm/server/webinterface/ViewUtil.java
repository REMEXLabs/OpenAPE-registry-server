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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.http.HttpStatus;

import de.hdm.configuration.MyProperties;
import de.hdm.databaseaccess.DaoFactory;
import de.hdm.databaseaccess.DataAccessException;
import de.hdm.databaseaccess.IUserDao;
import de.hdm.datatypes.IUser;
import de.hdm.helpers.Checker;
import de.hdm.helpers.TemplateFiller;
import de.hdm.multiplelanguages.LanguageHandler;
import de.hdm.server.Path;
import de.hdm.server.SessionUtil;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.velocity.VelocityTemplateEngine;

public class ViewUtil {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	private static IUserDao userDao = DaoFactory.createDaoFactory(DaoFactory.Database.DEFAULT).createUserDao();


	
	
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

	public static String render(Request request, Map<String, Object> model, String templatePath, Locale locale){
		//System.out.println("in render templatePath = " + templatePath);
	    Checker.checkNull(request, "request");
		if(model == null){
			model = new HashMap<String, Object>();
		}
		Checker.checkNullAndEmptiness(templatePath, "templatePath");
		
		model.put("lh", LanguageHandler.createSingleLanguageHandler(locale));
		model.put("tf", new TemplateFiller());
		model.put("locale", locale);
		model.put("mp", MyProperties.class);
		
		int userId = SessionUtil.getUserId(request);
		if(userId != -1){
			IUser user = null;
			try {
				user = userDao.selectUser(null, userId);
				model.put("user", user);
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
			if(user == null){
				System.out.println("user is null");
				// TODO show error page
			}
		}
		
		return createVelocityTemplateEngine().render(new ModelAndView(model, templatePath));
	}
	
	public static String notFound(Request request, Response response, Locale locale){
		response.status(HttpStatus.NOT_FOUND_404);
		return render(request, null, Path.WebTemplate.NOT_FOUND, locale);
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

	private static VelocityTemplateEngine createVelocityTemplateEngine(){
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("runtime.references.strict", true);
		velocityEngine.setProperty("resource.loader", "class");
		velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		return new VelocityTemplateEngine(velocityEngine);
	}

	
	

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// inner classes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

}
