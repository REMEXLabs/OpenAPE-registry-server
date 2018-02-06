var namesCounter = 0;

var definitionsCounter = 0;

var notesCounter = 0;

var examplesCounter = 0;


function insertContentFields(languageCode, content, languageCodePlaceholder, contentPlaceholder, type, deleteButtonText, rows, required){
	var id;
	var parentId;
	var nameLanguageCodeField;
	var nameContentField;
	if(type == 0){
		id = "nameFields" + namesCounter;
		namesCounter++;
		parentId = "names";
		nameLanguageCodeField = "languageCodeName";
		nameContentField = "name";
	}else if(type == 1){
		id = "definitionFields" + definitionsCounter;
		definitionsCounter++;
		parentId = "definitions";
		nameLanguageCodeField = "languageCodeDefinition";
		nameContentField = "definition";
	}else if(type == 2){
		id = "noteFields" + notesCounter;
		notesCounter++;
		parentId = "notes";
		nameLanguageCodeField = "languageCodeNote";
		nameContentField = "note";
	}else if(type == 3){
		id = "exampleFields" + examplesCounter;
		examplesCounter++;
		parentId = "examples";
		nameLanguageCodeField = "languageCodeExample";
		nameContentField = "example";
	}else{
		return;
	}
	var html = "";
	html += "<div class='row uniform' id='" + id + "'>";
	html += "<div class='2u 12u$(small)'>";
	if(languageCode == null){
		languageCode = "";
	}
	html += "<input type='text' name='" + nameLanguageCodeField + "' placeholder='" + languageCodePlaceholder + "' value='" + languageCode + "'>";
	html += "</div>";
	html += "<div class='8u 12u$(small)'>";
	if(content == null){
		content = "";
	}
	html += "<textarea name='" + nameContentField + "' placeholder='" + contentPlaceholder + "' rows='" + rows + "'";
	if(required == true){
		html += " required";
	}
	html += ">" + content + "</textarea>";
	html += "</div>";
	html += "<div class='2u 12u$(small)'>";
	html += "<input type='button' name='remove' value='" + deleteButtonText + "' onClick='deleteFields(\"" + id + "\");'>";
	html += "</div>";
	html += "</div>";
	document.getElementById(parentId).insertAdjacentHTML("beforeend", html);
}

function insertNameFields(languageCode, name, languageCodePlaceholder, namePlaceholder, deleteButtonText){
	var id = "nameFields" + namesCounter;
	var html = "";
	html += "<div class='row uniform' id='" + id + "'>";
	html += "<div class='2u 12u$(small)'>";
	if(languageCode == null){
		languageCode = "";
	}
	html += "<input type='text' name='languageCodeName' placeholder='" + languageCodePlaceholder + "' value='" + languageCode + "'>";
	html += "</div>";
	html += "<div class='8u 12u$(small)'>";
	if(name == null){
		name = "";
	}
	html += "<input type='text' name='name' placeholder='" + namePlaceholder + "' value='" + name + "' required>";
	html += "</div>";
	html += "<div class='2u 12u$(small)'>";
	html += "<input type='button' name='remove' value='" + deleteButtonText + "' onClick='deleteFields(\"" + id + "\");'>";
	html += "</div>";
	html += "</div>";
	document.getElementById("names").insertAdjacentHTML("beforeend", html);
	namesCounter++;
}

function deleteFields(id){
	document.getElementById(id).remove();
}

function toggleCheckboxes(name, newState){
	checkboxes = document.getElementsByName(name);
	for(var i = 0; i < checkboxes.length; i++){
		checkboxes[i].checked = newState;
	}
}

function insertValueSpaceTemplate(){
	var jsonAsStringCommonPart = '{"$schema": "http://json-schema.org/draft-06/schema#", "title":"' + getDefaultTitleForJsonSchema() + '", "description": "' + getDefaultDescriptionForJsonSchema() + '", "type": ';
	var template = document.getElementById("valueSpace").value;
	if(document.getElementById("valueSpaceTemplate").value == "-"){
		template = "";
	}else{
		var jsonAsStringIndividualPart = null;
		if(document.getElementById("valueSpaceTemplate").value == "boolean"){
			jsonAsStringIndividualPart = '"boolean"}';
		}else if(document.getElementById("valueSpaceTemplate").value == "number"){
			jsonAsStringIndividualPart = '"number"}';
		}else if(document.getElementById("valueSpaceTemplate").value == "numberWithMinMax"){
			jsonAsStringIndividualPart = '"number", "minimum": 0, "maximum": 1}';
		}else if(document.getElementById("valueSpaceTemplate").value == "string"){
			jsonAsStringIndividualPart = '"string"}';
		}else if(document.getElementById("valueSpaceTemplate").value == "stringWithFixedValues"){
			jsonAsStringIndividualPart = '"string", "enum": ["String 1", "String 2"]}';
		}
		if(jsonAsStringIndividualPart != null){
			var jsonAsString = jsonAsStringCommonPart + jsonAsStringIndividualPart;
			template = JSON.stringify(JSON.parse(jsonAsString), null, "\t");
		}
	}
	document.getElementById("valueSpace").value = template;
}

function getDefaultTitleForJsonSchema(){
	var defaultTitle = "a title";
	var languageCodes = document.getElementsByName("languageCodeName");
	var names = document.getElementsByName("name");
	for(var i = 0; i < languageCodes.length; i++){
		if(languageCodes[i] != null && names[i] != null){
			var code = languageCodes[i].value;
			var isEn = code.match(/en/i);
			if((i == 0 || isEn != null) && !(names[i].value === "")){
				defaultTitle = names[i].value;
			}
			if(isEn != null){
				break;
			}
		}
	}
	return defaultTitle;
}

function getDefaultDescriptionForJsonSchema(){
	var defaultDescription = "";
	if(document.getElementById("id").value != null){
		var conceptId = document.getElementById("id").value;
		if(conceptId != null && !(conceptId === "")){
			defaultDescription = "Term registered on gpii registry under https://terms.gpii.eu/web/loggedIn/concepts/showConcept?conceptId=" + conceptId;
		}	
	}
	return defaultDescription;
}

function insertFormattedValueSpace(valueSpace){
	document.getElementById("valueSpace").value = JSON.stringify(valueSpace, null, 2);
}