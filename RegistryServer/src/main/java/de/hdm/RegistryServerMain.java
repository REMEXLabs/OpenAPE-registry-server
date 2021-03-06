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
package de.hdm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hdm.server.SuperRestInterface;
import spark.debug.DebugScreen;

/**
 * This is the main class of the registry server.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class RegistryServerMain {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Logger.
	 */
	private final static Logger logger = LoggerFactory.getLogger(RegistryServerMain.class);



	
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
	 * Main method.
	 * 
	 * @param args
	 *            start parameters
	 */
	public static void main(String[] args) {
	    System.setProperty("log.name", "Registry-Server");
	    logger.info("Starting registry server application");
        logger.debug("Working directory: " + System.getProperty("user.dir"));

		// Configure Spark
		//Spark.port(4567);
		//Spark.staticFileLocation("/public");
		// staticFiles.expireTime(600L);
		//DebugScreen.enableDebugScreen();

		// Set up before-filters (called before each get/post)
		// before("*", Filters.addTrailingSlashes);
		// before("*", Filters.handleLocaleChange);

		new SuperRestInterface();
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
