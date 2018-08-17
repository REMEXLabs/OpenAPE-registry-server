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

import spark.Filter;
import spark.Request;
import spark.Response;

/**
 * This class contains helper functions, which can be called before or after routing a request.
 * 
 * @author Tobias Ableiter
 *
 */
public class Filters {

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // attributes
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    /**
     * Adds a "/" to the end of the request path, if missing and redirects the requests. If the request path already
     * ends with "/", nothing will be done.
     */
    protected static Filter addMissingSlash = (Request request, Response respone) -> {
        if(!request.pathInfo().endsWith("/")) {
            respone.redirect(request.pathInfo() + "/");
        }
    };

    


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
