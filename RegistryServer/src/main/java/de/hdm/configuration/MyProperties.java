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
package de.hdm.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import de.hdm.datatypes.IConcept;
import de.hdm.datatypes.ValueSpace;

/**
 * This class loads the configuration parameters for the registry server from an Java properties file. It also makes
 * them accessible for the whole application via static getters.
 * 
 * The properties file has to be placed in the package of this class. It's name should be config.properties.
 * 
 * The property file has to contain the following parameters (brackets with content has to be removed by the values):
 * registryDatabaseUrl=[Url for the registry database.]
 * registryDatabaseUserName=[User name for the registry database.]
 * registryDatabasePassword=[Password for the registry database.]
 * registryDatabaseDriverClassName=[Class name of the driver, which is needed to connect to the registry database.]
 * registryDatabaseMaxActiveConnections=[Maximum number of allowed active connections to the registry database.]
 * registryDatabaseMaxIdleConnections=[Maximum number of allowed idle connections to the registry database.]
 * cacheLanguages=[true if the language property files should be cached and false if not.]
 * registryServerUriUntilApi=[the part of the url to request a concept as json object until /api/record/...]
 * conceptIdGenerationPrefix=[String as prefix for the generated concept ids. For example "C".]
 * conceptIdGenerationMaxNumberOfIterations=[Number of tries, the server will do, when it generates a concept id, which
 * already exists. The number has to be positive and whole numbered.]
 * addNewUserToAnAllUserGroup=[true if new users should be added to an all users group and false if not.]
 * allUsersGroupId=[Id of the group "_AllUsers". This is the group, which contains all registered users.]
 * readRightForAllUsersGroupPerDefault=[If it is true the read access right check box of the "_AllUsers" group in the create concept web interface is checked by default and if it is false not.]
 * prefixForHashOfPassword=[Prefix for the hashed passwords. The Prefix will not be hashed with the password.]
 * suffixForPassword=[Suffix for the passwords. The suffix will be hashed together with the password.]
 * resetPasswordPeriodOfValidityInSeconds=[Period of validity in seconds for a reset password.]
 * resetPasswordMailUser=[User name for the mail account, which is used for sending reset password mails.]
 * resetPasswordMailPassword=[Password for the mail account, which is used for sending reset password mails.]
 * resetPasswordSessionTimeoutInSeconds=[Timeout in seconds for a reset password session.]
 * maxNumberOfAllowedFailedLogins=[The maximum number of allowed failed logins. It must be greater 0.]
 * valueSpaceDefaultDescription=[The default text for the description in the JSON schema, which defines the value space of a concept. If the id of the concept {@link IConcept#getId()} should be included use this placeholder: ${conceptId}]
 * 
 * If one of the properties is not found, the whole application is terminated by an exception.
 * There are no checks, whether the values of the properties are well formed. The property file's editor has to ensure
 * this.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class MyProperties {

    // ***********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // attributes
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    /**
     * Url for the registry database.
     */
    private static String REGISTRY_DATABASE_URL;

    /**
     * User name for the registry database.
     */
    private static String REGISTRY_DATABASE_USER_NAME;

    /**
     * Password for the registry database. It has to belong to assigned user name in
     * {@link #REGISTRY_DATABASE_USER_NAME}.
     */
    private static String REGISTRY_DATABASE_PASSWORD;

    /**
     * Driver class name of the database driver.
     */
    private static String REGISTRY_DATABASE_DRIVER_CLASS_NAME;

    /**
     * Number of maximum active connections to the database.
     */
    private static int REGISTRY_DATABASE_MAX_ACTIVE_CONNECTIONS;

    /**
     * Number of maximum idle connections to the database.
     */
    private static int REGISTRY_DATABASE_MAX_IDLE_CONNECTIONS;

    /**
     * When it is true, all language property files are loaded at the beginning. Otherwise they are loaded on demand.
     */
    private static boolean CACHE_LANGUAGES;

    /**
     * The part of the uri of the get concept as json rest call, which comes before "/api/record/...".
     */
    private static String REGISTRY_SERVER_URI_UNTIL_API;

    /**
     * Prefix of the generated concept ids. It is the part, which comes before the number.
     */
    private static String CONCEPT_ID_GENERATION_PREFIX;

    /**
     * The number of tries, to generate a concept id. If the value is 10 for example, the server will increment the
     * number, which is combined with the prefix {@link #CONCEPT_ID_GENERATION_PREFIX}, up to ten times in case, that
     * the generated concept id already exists.
     */
    private static int CONCEPT_ID_GENERATION_MAX_NUMBER_OF_ITERAIONS;

    /**
     * When it is true, new users will be automatically added to an all users group which is defined by the property
     * {@link #ALL_USERS_GROUP_ID}.
     */
    private static boolean ADD_NEW_USER_TO_AN_ALL_USERS_GROUP;

    /**
     * Id of the group "_AllUsers". This group contains all registered users. It is used to define default access rights
     * for the concepts.
     */
    private static int ALL_USERS_GROUP_ID;
    
    /**
     * If it is true the read access right check box of the "_AllUsers" group in the create concept web interface is
     * checked by default and if it is false not. The "_AllUsers" group is specified by the property
     * {@link #ALL_USERS_GROUP_ID}.
     */
    private static boolean READ_RIGHT_FOR_ALL_USERS_GROUP_PER_DEFAULT;

    /**
     * Prefix for the hash of a password. It is not hashed with the password.
     */
    private static String PREFIX_FOR_HASH_OF_PASSWORD;

    /**
     * Suffix for a password. It will be hashed together with the password.
     */
    private static String SUFFIX_FOR_PASSWORD;

    /**
     * Period of validity in seconds for a reset password.
     */
    private static int RESET_PASSWORD_PERIOD_OF_VALIDITY_IN_SECONDS;

    /**
     * User name for the mail account, which is used for sending reset password mails.
     */
    private static String RESET_PASSWORD_MAIL_USER;

    /**
     * Password for the mail account, which is used for sending reset password mails.
     */
    private static String RESET_PASSWORD_MAIL_PASSWORD;

    /**
     * Timeout in seconds for a reset password session.
     */
    private static int RESET_PASSWORD_SESSION_TIMEOUT_IN_SECONDS;

    /**
     * The maximum number of allowed failed login. A login fails, if the password is wrong. If the number of failed
     * logins exceeds this number, the user account will be locked.
     */
    private static int MAX_NUMBER_OF_ALLOWED_FAILED_LOGINS;
    
    /**
     * The default text for the description in the JSON schema, which
     * defines the value space {@link ValueSpace} of a concept. The default text will be set as description if the user
     * defines a value space without a description. It can contain the placeholder ${conceptId} which shall be replaced by the concept's id {@link IConcept#getId()}.
     */
    private static String VALUE_SPACE_DEFAULT_DESCRIPTION;




    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // constructors
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    /**
     * Initializes the configuration parameters, when the application is started. If a required parameter is not found
     * or not well formed, it terminates the application by throwing an exception.
     */
    static {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            // use this to run on server
            inputStream = MyProperties.class.getResourceAsStream("config.properties");
            
            // use this for testing and development
            //inputStream = MyProperties.class.getResourceAsStream("testConfig.properties");
            
            properties.load(inputStream);

            REGISTRY_DATABASE_URL = loadPropertyAsString(properties, "registryDatabaseUrl");

            REGISTRY_DATABASE_USER_NAME = loadPropertyAsString(properties, "registryDatabaseUserName");

            REGISTRY_DATABASE_PASSWORD = loadPropertyAsString(properties, "registryDatabasePassword");

            REGISTRY_DATABASE_DRIVER_CLASS_NAME = loadPropertyAsString(properties, "registryDatabaseDriverClassName");

            REGISTRY_DATABASE_MAX_ACTIVE_CONNECTIONS = loadPropertyAsInt(properties,
                    "registryDatabaseMaxActiveConnections");

            REGISTRY_DATABASE_MAX_IDLE_CONNECTIONS = loadPropertyAsInt(properties,
                    "registryDatabaseMaxIdleConnections");

            CACHE_LANGUAGES = loadPropertyAsBoolean(properties, "cacheLanguages");

            REGISTRY_SERVER_URI_UNTIL_API = loadPropertyAsString(properties, "registryServerUriUntilApi");

            CONCEPT_ID_GENERATION_PREFIX = loadPropertyAsString(properties, "conceptIdGenerationPrefix");

            CONCEPT_ID_GENERATION_MAX_NUMBER_OF_ITERAIONS = loadPropertyAsInt(properties,
                    "conceptIdGenerationMaxNumberOfIterations");

            ADD_NEW_USER_TO_AN_ALL_USERS_GROUP = loadPropertyAsBoolean(properties, "addNewUserToAnAllUserGroup");

            ALL_USERS_GROUP_ID = loadPropertyAsInt(properties, "allUsersGroupId");

            READ_RIGHT_FOR_ALL_USERS_GROUP_PER_DEFAULT = loadPropertyAsBoolean(properties,
                    "readRightForAllUsersGroupPerDefault");

            PREFIX_FOR_HASH_OF_PASSWORD = loadPropertyAsString(properties, "prefixForHashOfPassword");

            SUFFIX_FOR_PASSWORD = loadPropertyAsString(properties, "suffixForPassword");

            RESET_PASSWORD_PERIOD_OF_VALIDITY_IN_SECONDS = loadPropertyAsInt(properties,
                    "resetPasswordPeriodOfValidityInSeconds");

            RESET_PASSWORD_MAIL_USER = loadPropertyAsString(properties, "resetPasswordMailUser");

            RESET_PASSWORD_MAIL_PASSWORD = loadPropertyAsString(properties, "resetPasswordMailPassword");

            RESET_PASSWORD_SESSION_TIMEOUT_IN_SECONDS = loadPropertyAsInt(properties,
                    "resetPasswordSessionTimeoutInSeconds");

            MAX_NUMBER_OF_ALLOWED_FAILED_LOGINS = loadPropertyAsInt(properties, "maxNumberOfAllowedFailedLogins");

            VALUE_SPACE_DEFAULT_DESCRIPTION = loadPropertyAsString(properties, "valueSpaceDefaultDescription");

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Properties file which contains the configuration could not be loaded!");
        }
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
     * Static getter for the url of the registry database.
     * 
     * @return registry database's url
     */
    public static String getRegistryDatabaseUrl() {
        return REGISTRY_DATABASE_URL;
    }

    /**
     * Static getter for the user name of the registry database.
     * 
     * @return user name for the registry database
     */
    public static String getRegistryDatabaseUserName() {
        return REGISTRY_DATABASE_USER_NAME;
    }

    /**
     * Static getter for the password of the registry database.
     * 
     * @return password belonging to the user name returned by {@link #getRegistryDatabaseUserName} for the registry
     *         database
     */
    public static String getRegistryDatabasePassword() {
        return REGISTRY_DATABASE_PASSWORD;
    }

    /**
     * Static getter for the registry database driver class name.
     * 
     * @return driver class name for the registry database
     */
    public static String getRegistryDatabaseDriverClassName() {
        return REGISTRY_DATABASE_DRIVER_CLASS_NAME;
    }

    /**
     * Static getter for the maximum number of allowed active connections to the registry database.
     * 
     * @return maximum number of active connections to the registry database
     */
    public static int getRegistryDatabaseMaxActiveConnections() {
        return REGISTRY_DATABASE_MAX_ACTIVE_CONNECTIONS;
    }

    /**
     * Static getter for the maximum number of allowed idle connections to the registry database.
     * 
     * @return maximum number of idle connections to the registry database
     */
    public static int getRegistryDatabaseMaxIdleConnections() {
        return REGISTRY_DATABASE_MAX_IDLE_CONNECTIONS;
    }

    /**
     * Static getter for the boolean property whether the language caching should be enabled or disabled.
     * 
     * @return true if language caching is enabled and false if disabled
     */
    public static Boolean getCacheLanguages() {
        return CACHE_LANGUAGES;
    }

    /**
     * Static getter for part of the uri of the get concept as json rest call, which comes before "/api/record/...".
     * 
     * @return part of the concept a json rest call's before "/api/record/..."
     */
    public static String getRegistryServerUriUntilApi() {
        return REGISTRY_SERVER_URI_UNTIL_API;
    }

    /**
     * Static getter for the prefix of the generated concept ids.
     * 
     * @return prefix for generated concept ids
     */
    public static String getConceptIdGenerationPrefix() {
        return CONCEPT_ID_GENERATION_PREFIX;
    }

    /**
     * Static getter for maximal number of tries, to generate a concept id in case, that the generated concept ids
     * already exist.
     * 
     * @return number of tries to generate a concept id
     */
    public static int getConceptIdGenerationMaxNumberOfIterations() {
        return CONCEPT_ID_GENERATION_MAX_NUMBER_OF_ITERAIONS;
    }

    /**
     * Static getter for the boolean property whether new users should be added to an all users group or not. If it is
     * true, the all users group is defined by the property {@link #ALL_USERS_GROUP_ID} which is accessible via the
     * getter {@link #getAllUsersGroupId()}.
     * 
     * @return true if new users will be added to an all users group and false if not
     */
    public static boolean getAddNewUserToAnAllUserGroup() {
        return ADD_NEW_USER_TO_AN_ALL_USERS_GROUP;
    }

    /**
     * Static getter for the id of the group "_AllUsers".
     * 
     * @return id of the group "_AllUsers"
     */
    public static int getAllUsersGroupId() {
        return ALL_USERS_GROUP_ID;
    }
    
    /**
     * Static getter for the boolean property whether read access right check box of the "_AllUsers" group in the create
     * concept web interface shall checked by default or not. If the property is true, it shall be checked and if it is
     * false then not. The "_AllUsers" group is specified by the property {@link #getAllUsersGroupId()}.
     * 
     * @return true if the read access right check box of the "_AllUsers" group should be checked by default and false
     *         if not.
     */
    public static boolean isReadRightForAllUsersGroupPerDefault() {
        return READ_RIGHT_FOR_ALL_USERS_GROUP_PER_DEFAULT;
    }
    
    /**
     * Static getter for the prefix for the hash of a password.
     * 
     * @return prefix for the password hashes
     */
    public static String getPrefixForHashOfPassword() {
        return PREFIX_FOR_HASH_OF_PASSWORD;
    }

    /**
     * Static getter for the suffix of a password.
     * 
     * @return suffix for the passwords
     */
    public static String getSuffixForPassword() {
        return SUFFIX_FOR_PASSWORD;
    }

    /**
     * Static getter for the period of validity in seconds of a reset password.
     * 
     * @return period of validity in seconds
     */
    public static int getResetPasswordPeriodOfValidityInSeconds() {
        return RESET_PASSWORD_PERIOD_OF_VALIDITY_IN_SECONDS;
    }

    /**
     * Static getter for the user name of the mail account, which is used for sending reset password mails.
     * 
     * @return user name for mail account
     */
    public static String getResetPasswordMailUser() {
        return RESET_PASSWORD_MAIL_USER;
    }

    /**
     * Static getter for the password of the mail account, which is used for sending reset password mails.
     * 
     * @return password for mail account
     */
    public static String getResetPasswordMailPassword() {
        return RESET_PASSWORD_MAIL_PASSWORD;
    }

    /**
     * Static getter for reset password session timeout in seconds.
     * 
     * @return timeout in seconds
     */
    public static int getResetPasswordSessionTimeoutInSeconds() {
        return RESET_PASSWORD_SESSION_TIMEOUT_IN_SECONDS;
    }

    /**
     * Static getter for the maximum number of allowed failed logins.
     * 
     * @return maximum number of allowed failed logins
     */
    public static int getMaxNumberOfAllowedFailedLogins() {
        return MAX_NUMBER_OF_ALLOWED_FAILED_LOGINS;
    }
    
    /**
     * Static getter for the default text of the description in the JSON schema, which
     * defines the value space {@link ValueSpace} of a concept. It can contain the placeholder ${conceptId} which shall be replaced by the concept's id {@link IConcept#getId()}. 
     * @return text for the default description in a value space
     */
    public static String getValueSpaceDefaultDescription(){
        return VALUE_SPACE_DEFAULT_DESCRIPTION;
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

    private static String loadPropertyAsString(Properties properties, String propertyName) {
        String value = properties.getProperty(propertyName);
        if (value == null) {
            throw new RuntimeException("Property " + propertyName + " was not found!");
        }
        return value;
    }

    private static int loadPropertyAsInt(Properties properties, String propertyName) {
        return Integer.valueOf(loadPropertyAsString(properties, propertyName));
    }
    
    private static boolean loadPropertyAsBoolean(Properties properties, String propertyName){
        String propertyAsString = loadPropertyAsString(properties, propertyName);
        return Boolean.valueOf(propertyAsString);
    }




    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // inner classes
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************




}
