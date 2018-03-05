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

import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hdm.server.jsonapi.IJsonConceptController;
import de.hdm.server.jsonapi.JsonConceptController;
import de.hdm.server.webinterface.HtmlConceptController;
import de.hdm.server.webinterface.HtmlGroupController;
import de.hdm.server.webinterface.HtmlInfoAndErrorController;
import de.hdm.server.webinterface.HtmlLoginController;
import de.hdm.server.webinterface.HtmlUserController;
import de.hdm.server.webinterface.IHtmlConceptController;
import de.hdm.server.webinterface.IHtmlGroupController;
import de.hdm.server.webinterface.IHtmlInfoAndErrorController;
import de.hdm.server.webinterface.IHtmlLoginController;
import de.hdm.server.webinterface.IHtmlUserController;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * This class encapsulates all rest end points, filters and configurations of the registry server. To initialize them,
 * call the constructor {@link SuperRestInterface#SuperRestInterface()} of this class. After initialization this class
 * routes all requests of the JSON Rest API and the web user interface
 * to the relevant classes, interfaces and / or methods.
 * 
 * @author Tobias Ableitner
 *
 */
public class SuperRestInterface {

    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // attributes
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    /**
     * Logger.
     */
    private final static Logger logger = LoggerFactory.getLogger(SuperRestInterface.class);

    /**
     * Handles the JSON Rest API requests.
     */
    private static IJsonConceptController jsonConceptController = new JsonConceptController();
   
    /**
     * Handles the Rest API concept requests of the web user interface.
     */
    private static IHtmlConceptController htmlConceptController = new HtmlConceptController();
    
    /**
     * Handles the Rest API login and reset password requests of the web user interface.
     */
    private static IHtmlLoginController htmlLoginController = new HtmlLoginController();
    
    /**
     * Handles the Rest API group requests of the web user interface.
     */
    private static IHtmlGroupController htmlGroupController = new HtmlGroupController();
    
    /**
     * Handles the Rest API user management requests of the web user interface.
     */
    private static IHtmlUserController htmlUserController = new HtmlUserController();
    
    /**
     * Handles the Rest API info and error requests of the web user interface.
     */
    private static IHtmlInfoAndErrorController htmlInfoAndErrorController = new HtmlInfoAndErrorController();



    
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // constructors
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    /**
     * Creates and initializes the super rest interface.
     */
    public SuperRestInterface(){
        SuperRestInterface.logger.info("Setting up Rest API");
        Spark.staticFiles.location("/public");
        this.initBeforeFilters();
        this.initRestEndPointsForJsonAPI();
        this.initRestEndPointsForWebUserInterface();
    }


    

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

    private void initBeforeFilters() {
        before("*", Filters.addMissingSlash);
    }
    
    private void initRestEndPointsForJsonAPI(){
        post(Path.Json.CREATE_CONCEPT_RECORD, "application/json", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return jsonConceptController.postConceptAsJson(request, response);
            }
        });

        put(Path.Json.UPDATE_CONCEPT_RECORD, "application/json", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return jsonConceptController.putConceptAsJson(request, response);
            }
        });

        delete(Path.Json.DELETE_CONCEPT_RECORD, "application/json", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return jsonConceptController.deleteConceptAsJson(request, response);
            }
        });

        get(Path.Json.GET_CONCEPT_RECORD, "application/json", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return jsonConceptController.getConceptAsJson(request, response);
            }
        });

        get(Path.Json.GET_LIST_OF_CONCEPT_RECORDS, "application/json", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return jsonConceptController.getConceptsAsJson(request, response);
            }
        });
        
        // return 404 if no path was found in the url
        get("*", "application/json", new Route(){
            @Override
            public Object handle(Request request, Response response){
                return jsonConceptController.getNotFoundAsJson(request, response);
            }
        });

    }

    private void initRestEndPointsForWebUserInterface(){
        get(Path.Web.ROOT, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlLoginController.getLoginPage(request, response);
            }
        });
        
        get(Path.Web.NOT_LOGGED_IN_INFO_AND_ERROR, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlInfoAndErrorController.getNotLoggedInInfoAndErrorPage(request, response);
            }
        });
        
        get(Path.Web.LOGGED_IN_INFO_AND_ERROR, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlInfoAndErrorController.getLoggedInInfoAndErrorPage(request, response);
            }
        });
        
        get(Path.Web.LOGIN, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlLoginController.getLoginPage(request, response);
            }
        });
        
        post(Path.Web.LOGIN, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlLoginController.handleLoginPost(request, response);
            }
        });
        
        get(Path.Web.FORGOT_PASSWORD, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlLoginController.getForgotPasswordPage(request, response);
            }
        });
        
        post(Path.Web.FORGOT_PASSWORD, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlLoginController.handleForgotPasswordPost(request, response);
            }
        });
        
        get(Path.Web.RESET_PASSWORD, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlLoginController.getResetPasswordPage(request, response);
            }
        });
        
        post(Path.Web.RESET_PASSWORD, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlLoginController.handleResetPasswordPost(request, response);
            }
        });
        
        get(Path.Web.LOGOUT, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlLoginController.handleLogoutPost(request, response);
            }
        });
        
        get(Path.Web.WELCOME_USER, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlLoginController.welcomeUser(request, response);
            }
        });
        
        get(Path.Web.CONCEPTS_LIST_ALL, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlConceptController.getAllConceptsPage(request, response);
            }
        });
        
        get(Path.Web.CONCEPTS_LIST_MY, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlConceptController.getMyConceptsPage(request, response);
            }
        });
        
        get(Path.Web.CONCEPTS_NEW_CONCEPT, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlConceptController.getNewConceptPage(request, response);
            }
        });
        
        post(Path.Web.CONCEPTS_NEW_CONCEPT, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlConceptController.handleNewConceptPost(request, response);
            }
        });
        
        get(Path.Web.CONCEPTS_EDIT_CONCEPT, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlConceptController.getEditConceptPage(request, response);
            }
        });
        
        post(Path.Web.CONCEPTS_EDIT_CONCEPT, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlConceptController.handleEditConceptPost(request, response);
            }
        });
        
        get(Path.Web.CONCEPTS_SEARCH, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlConceptController.getSearchConceptPage(request, response);
            }
        });
        
        post(Path.Web.CONCEPTS_SEARCH, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlConceptController.handleSearchConceptPage(request, response);
            }
        });
        
        get(Path.Web.CONCEPTS_SHOW_CONCEPT, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlConceptController.getShowConceptPage(request, response);
            }
        });
        
        get(Path.Web.CONCEPTS_CONCEPT_LOG, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlConceptController.getConceptLogPage(request, response);
            }
        });
        
        get(Path.Web.CONCEPTS_DELETE_CONCEPT, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlConceptController.handleDeleteConcept(request, response);
            }
        });
        
        get(Path.Web.GROUPS_LIST_GROUPS, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlGroupController.getGroupsPage(request, response);
            }
        });
        
        get(Path.Web.GROUPS_NEW_GROUP, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlGroupController.getNewGroupPage(request, response);
            }
        });
        
        post(Path.Web.GROUPS_NEW_GROUP, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlGroupController.handleNewGroupPost(request, response);
            }
        });
        
        get(Path.Web.GROUPS_EDIT_GROUP, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlGroupController.getEditGroupPage(request, response);
            }
        });
        
        post(Path.Web.GROUPS_EDIT_GROUP, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlGroupController.handleEditGroupPost(request, response);
            }
        });
        
        get(Path.Web.GROUPS_REMOVE_GROUP, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlGroupController.handleRemoveGroup(request, response);
            }
        });
        
        
        get(Path.Web.USERS_LIST_USERS, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlUserController.getUsersPage(request, response);
            }
        });
        
        get(Path.Web.USERS_NEW_USER, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlUserController.getNewUserPage(request, response);
            }
        });
        
        post(Path.Web.USERS_NEW_USER, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlUserController.handleNewUserPost(request, response);
            }
        });
        
        get(Path.Web.USERS_EDIT_USER, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlUserController.getEditUserPage(request, response);
            }
        });
        
        post(Path.Web.USERS_EDIT_USER, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlUserController.handleEditUserPost(request, response);
            }
        });
        
        get(Path.Web.USERS_REMOVE_USER, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlUserController.handleRemoveUser(request, response);
            }
        });
        
        get(Path.Web.USERS_MY_DATA, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlUserController.getMyDataPage(request, response);
            }
        });
        
        post(Path.Web.USERS_MY_DATA, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlUserController.handleMyDataPost(request, response);
            }
        });
        
        // not needed by Stuttgart Media University
        /*get(Path.Web.LEGAL_NOTICE, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlInfoAndErrorController.getLegalNoticePage(request, response);
            }
        });
        
        get(Path.Web.PRIVACY_POLICY, "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlInfoAndErrorController.getPrivacyPolicyPage(request, response);
            }
        });*/
         
        // show error page if no path was found in the url
        get("*", "application/html", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception{
                return htmlInfoAndErrorController.getNotFoundPage(request, response);
            }
        });
        
    }


    
    
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // inner classes
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

}
