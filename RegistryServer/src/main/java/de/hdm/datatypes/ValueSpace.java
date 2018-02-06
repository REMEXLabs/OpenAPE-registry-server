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
package de.hdm.datatypes;

import java.io.IOException;
import java.io.InputStream;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import de.hdm.configuration.MyProperties;
import de.hdm.helpers.Checker;
import de.hdm.helpers.TemplateFiller;

/**
 * This class defines the value space of a concept's {@link Concept} value. Therefore it contains the value space
 * definition in form of a JSON schema as string {@link #jsonSchema}.
 * 
 * This class is thread safe.
 * 
 * @author Tobias Ableitner
 *
 */
public class ValueSpace implements IValueSpace {

	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// attributes
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Value space defintion as JSON schema.
	 */
	private String jsonSchema;
	
	/**
	 * The JSON field name of a description in the a JSON schema.
	 */
	private static final String JSON_SCHEMA_FIELD_NAME_DESCRIPTION = "description";




	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************
	// constructors
	// *********************************************************************************************************************************************
	// *********************************************************************************************************************************************

	/**
	 * Creates a new ValueSpace. The JSON schema must not be null or empty. Otherwise an
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param jsonSchema
	 *            the value space definition as json schema
	 */
	public ValueSpace(String jsonSchema) {
		this.setJsonSchema(jsonSchema);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hdm.datatypes.IValueSpace#getJsonSchema()
	 */
	@Override
	public String getJsonSchema() {
		return this.jsonSchema;
	}

	/*
     * (non-Javadoc)
     * @see de.hdm.datatypes.IValueSpace#setJsonSchema(java.lang.String)
     */
    @Override
    public void setJsonSchema(String jsonSchema) {
        Checker.checkNullAndEmptiness(jsonSchema, "jsonSchema");
        this.jsonSchema = jsonSchema;
    }
    
    /*
     * (non-Javadoc)
     * @see de.hdm.datatypes.IValueSpace#setDefaultDesciptionIfThereExistsNoOther(java.lang.String)
     */
    @Override
    public void setDefaultDesciptionIfThereExistsNoOther(String conceptId) {
        Checker.checkNullAndEmptiness(conceptId, "conceptId");
        if (this.jsonSchema != null && !this.jsonSchema.isEmpty()) {
            if (isJsonSchemaValid(this.jsonSchema)) {
                JSONObject schemaAsJsonObject = new JSONObject(this.jsonSchema);
                String description = schemaAsJsonObject.getString(JSON_SCHEMA_FIELD_NAME_DESCRIPTION);
                if (description == null || description.isEmpty()) {
                    description = TemplateFiller.fillTemplate(MyProperties.getValueSpaceDefaultDescription(), conceptId);
                    schemaAsJsonObject.put(JSON_SCHEMA_FIELD_NAME_DESCRIPTION, description);
                    this.jsonSchema = schemaAsJsonObject.toString();
                }
            }
        }
    }

    


    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************
    // public methods
    // *********************************************************************************************************************************************
    // *********************************************************************************************************************************************

    // TODO java doc
    public static boolean isJsonSchemaValid(String jsonSchemaAsString) {
        boolean result = false;
        if (jsonSchemaAsString != null && !jsonSchemaAsString.isEmpty()) {
            InputStream inputStream = null;
            try {
                inputStream = ValueSpace.class.getResourceAsStream("/private/schema.json");
                JSONObject metaSchemaAsJsonObject = new JSONObject(new JSONTokener(inputStream));
                JSONObject schemaAsJsonObject = new JSONObject(jsonSchemaAsString);
                SchemaLoader schemaLoader = SchemaLoader.builder()
                        .schemaJson(metaSchemaAsJsonObject)
                        .draftV6Support()
                        .build();
                Schema metaSchema = schemaLoader.load().build();
                metaSchema.validate(schemaAsJsonObject);
                
                // use this instead of the builder to support Draft 3, 4 and 6 and not only 6
                //SchemaLoader.load(metaSchemaAsJsonObject).validate(schemaAsJsonObject);
                
                result = true;
            } catch(Exception e){
                e.printStackTrace();
                // do nothing
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }// end inner try-catch
                }// end if
            }// end outer try-catch
        }// end if
        return result;
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
