function insertFormattedNote(note, noteFieldId){
	document.getElementById(noteFieldId).innerHTML = JSON.stringify(note, null, "  ");
}