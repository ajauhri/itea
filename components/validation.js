/* Component: Validation of Forms 
 * Validate form fields - checks empty fields, number fields and emails
 * 
 * Validation works in two forms: INDIVIDUAL, DETECT or COMBINED. This choice can be specified when calling any function.
 * COMBINED: returns a false on invalid form and true otherwise. Can, therefore, be used directly with onsubmit() attribute of <form>
 * DETECT: gives the id of the first element detected as empty. It returns a null otherwise.
 * 	You can do whatever you want with this id.
 * INDIVIDUAL: you must have for each element 'x' in the form with id 'xid' an element
 * 	with the id 'xid_error' containing the error message, set to default as style="display:none".
 * 	For each empty element, INDIVIDUAL will unhide these _error companion elements, making visible the error messages.
 *  It also returns a false.
 *  It returns a true otherwise.
 *
 * Requirements: all elements must have a name and an id. For using "INDIVIDUAL" type validation as discussed above, elements must have
 * associated _error companions (if element's id is "xid" the companion's id must be "xid_error") with default styling as display:none. 
 * Select lists - for every illegal option, the value should be empty (null) or -1.
 * 
 * Functions to check empty fields: 
 * 1. validateAll(type, form_id)
 * 		Validates ALL elements for content in a form.
 * 		Parameters: type and form id. Type can be either "INDIVIDUAL", "DETECT" or "COMBINED".
 * 		Returns:When type is "COMBINED"
 * 					returns false if any field is empty, true otherwise. 
 * 				When type is "DETECT"
 * 					returns null if all fields are filled, or
 * 					returns id of the first empty element detected, if any
 * 				 When type is "INDIVIDUAL"
 * 					all elements' _error companions (described above) are unhidden, whereever content is empty.
 * 					returns true if no element is empty, false otherwise
 * 	
 * 2. validateThese(type, element1_id, element2_id, ...)
 * 		Validates only specified elements - specified by their ids passed as parameters
 * 		Parameters: type, and one parameter per element. Type can be either "INDIVIDUAL", "DETECT" or "COMBINED".
 * 		Returns: When type is "COMBINED", returns false if any of the specified field is empty, false otherwise.
 * 				 When type is "DETECT", returns null if all specifed fields are filled, or id of the first empty element detected.
 * 				 When type is "INDIVIDUAL", sets all error messages to be displayed (as described above). Returns false on empty, true otherwise.
 * 
 * 3. validateAllExcept(type, form_id, element1_id, element2_id, ...)
 * 		Same as previous, but validates all elements OTHER than those specified.
 * 
 * Function to check for numbers:
 * validateNumber(element_id)
 * 		Returns true if content is numeral, false otherwise.
 * 
 * Function to check for email address:
 * validateEmail(element_id)
 * 		Returns true if valid content is a email address format, false otherwise.
 * 
 * Function to check for phone no.:
 * validatePhone(element_id)
 *		Returns true if content is a numeric phone no. of <=10 chars, may include only " ","+","-","(",")" special chars.
 * Functions for forced reset of all error fields:
 * resetAll(form_id) 
 * resetThese(element1_id, element2_id, ...)
 */


/* Validate Every Single Element In The Form */
function validateAll(type,formId){
	// Type: INDIVIDUAL, COMBINED or DETECT
	var errorFound=0;
	if(type=="INDIVIDUAL") resetAll(formId); // reset all elems first
	var form=document.getElementById(formId);
	for(var i=0;i<form.elements.length;i++){
		var elem=form.elements[i];
		if(elem.type.toUpperCase()=="submit".toUpperCase() || elem.type.toUpperCase()=="reset".toUpperCase()) continue; // skip submit/reset buttons
		if(type=="INDIVIDUAL")
			var errorElem=document.getElementById(elem.id+"_error");
		// Text fields
		if(elem.tagName.toUpperCase()=="input".toUpperCase() && elem.type.toUpperCase()=="text".toUpperCase() && (elem.value=="" || elem.value==null)){
			if(type=="INDIVIDUAL")
				errorElem.style.display="block";
			else if(type=="DETECT")
				return elem.id;
			else if(type=="COMBINED")
				return false;
			errorFound=1;
		}
		// Password fields
		if(elem.tagName.toUpperCase()=="input".toUpperCase() && elem.type.toUpperCase()=="password".toUpperCase() && (elem.value=="" || elem.value==null)){
			if(type=="INDIVIDUAL")
				errorElem.style.display="block";
			else if(type=="DETECT")
				return elem.id;
			else if(type=="COMBINED")
				return false;
			errorFound=1;
		}
		// Text areas
		if(elem.tagName.toUpperCase()=="textarea".toUpperCase() && (elem.value==null || elem.value=="")){
			if(type=="INDIVIDUAL")
				errorElem.style.display="block";
			else if(type=="DETECT")
				return elem.id;
			else if(type=="COMBINED")
				return false;
			errorFound=1;
		}
		// Radios -- so far no way to check. ALT: have one radio selected by default
		// Checkboxes -- they are optional anyway, isn't it?
		// Select lists
		if(elem.tagName.toUpperCase()=="select".toUpperCase() && (elem.value==null || elem.value=="" || elem.value=="-1")){
			if(type=="INDIVIDUAL")
				errorElem.style.display="block";
			else if(type=="DETECT")
				return elem.id;
			else if(type=="COMBINED")
				return false;
			errorFound=1;
		}
	}
	if(type=="INDIVIDUAL")
		if(errorFound==0) return true;
		else return false;
	else if(type=="DETECT") return null;
	else return true;
}
function resetAll(formId){
	var form=document.getElementById(formId);
	for(var i=0;i<form.elements.length;i++){
		var elem=form.elements[i];
		var errorElem=document.getElementById(elem.id+"_error");
		if(errorElem!=null) errorElem.style.display="none";
	}
}
function resetThese(){
	for(var i=0;i<resetThese.arguments.length;i++){
		var elem=document.getElementById(resetThese.arguments[i]);
		var errorElem=document.getElementById(elem.id+"_error");
		if(errorElem!=null) errorElem.style.display="none";
	}
}
function validateThese(type,list_of_elements){
	// Provide as many element ids as desired
	var errorFound=0;
	var i=0;
	if(type=="INDIVIDUAL"){
		var argumentList="";
		for(i=1;i<validateThese.arguments.length;i++)
			argumentList+="\""+validateThese.arguments[i]+"\",";	// populate list of arguments to be used in resetThese function
		argumentList=argumentList.substring(0,argumentList.length-1);	// strip off the trailing comma
		eval("resetThese("+argumentList+")");
	}
	for(i=1;i<validateThese.arguments.length;i++){
		var elem=document.getElementById(validateThese.arguments[i]);
		if(elem==null) continue;
		if(type=="INDIVIDUAL")
			var errorElem=document.getElementById(elem.id+"_error");
		// Text fields
		if(elem.tagName.toUpperCase()=="input".toUpperCase() && elem.type.toUpperCase()=="text".toUpperCase() && (elem.value=="" || elem.value==null)){
			if(type=="INDIVIDUAL")
				errorElem.style.display="block";
			else if(type=="DETECT")
				return elem.id;
			else if(type=="COMBINED")
				return false;
			errorFound=1;
		}
		// Password fields
		if(elem.tagName.toUpperCase()=="input".toUpperCase() && elem.type.toUpperCase()=="password".toUpperCase() && (elem.value=="" || elem.value==null)){
			if(type=="INDIVIDUAL")
				errorElem.style.display="block";
			else if(type=="DETECT")
				return elem.id;
			else if(type=="COMBINED")
				return false;
			errorFound=1;
		}
		// Text areas
		if(elem.tagName.toUpperCase()=="textarea".toUpperCase() && (elem.value=="" || elem.value==null)){
			if(type=="INDIVIDUAL")
				errorElem.style.display="block";
			else if(type=="DETECT")
				return elem.id;
			else if(type=="COMBINED")
				return false;
			errorFound=1;
		}
		// Radios -- so far no way to check. ALT: have one radio selected by default
		// Checkboxes -- they are optional anyway, isn't it?
		// Select lists
		if(elem.tagName.toUpperCase()=="select".toUpperCase() && (elem.value==null || elem.value=="" || elem.value=="-1")){
			if(type=="INDIVIDUAL")
				errorElem.style.display="block";
			else if(type=="DETECT")
				return elem.id;
			else if(type=="COMBINED")
				return false;
			errorFound=1;
		}		
	}
	if(type=="INDIVIDUAL")
		if(errorFound==0) return true;
		else return false;
	else if(type=="DETECT") return null;
	else return true;
}
function resetThese(list_of_elements){
	for(i=0;i<resetThese.arguments.length;i++){
		var elem=document.getElementById(resetThese.arguments[i]);
		var errorElem=document.getElementById(elem.id+"_error");
		if(errorElem!=null) errorElem.style.display="none";
	}
}
function validateAllExcept(type,formId,list_of_elements){
	// Provide as many element ids as desired
	var errorFound=0;
	var i=0;
	if(type=="INDIVIDUAL"){
		var argumentList="";
		for(i=0;i<validateAllExcept.arguments.length;i++)
			argumentList+="\""+validateAllExcept.arguments[i]+"\",";	// populate list of arguments to be used in resetThese function
		argumentList=argumentList.substring(0,argumentList.length-1);	// strip off the trailing comma
		eval("resetAllExcept(\""+formId+"\","+argumentList+")");
	}
	var inArguments=0;
	var form=document.getElementById(formId);
	for(i=0;i<form.elements.length;i++){
		var elem=form.elements[i];
		// Check if exists in exception list
		for(var j=1;j<validateAllExcept.arguments.length;j++){
			argElem=document.getElementById(validateAllExcept.arguments[j]);
			if(argElem==null) continue;
			if(elem.id==argElem.id)
				inArguments=1;
		}
		// Yes, match found -- skip it
		if(inArguments==1){
			inArguments=0;
			continue;
		}
		// Else validate
		if(type=="INDIVIDUAL")
			var errorElem=document.getElementById(elem.id+"_error");
		// Text fields
		if(elem.tagName.toUpperCase()=="input".toUpperCase() && elem.type.toUpperCase()=="text".toUpperCase() && (elem.value=="" || elem.value==null)){
			if(type=="INDIVIDUAL")
				errorElem.style.display="block";
			else if(type=="DETECT")
				return elem.id;
			else if(type=="COMBINED")
				return false;
			errorFound=1;
		}
		// Password fields
		if(elem.tagName.toUpperCase()=="input".toUpperCase() && elem.type.toUpperCase()=="password".toUpperCase() && (elem.value=="" || elem.value==null)){
			if(type=="INDIVIDUAL")
				errorElem.style.display="block";
			else if(type=="DETECT")
				return elem.id;
			else if(type=="COMBINED")
				return false;
			errorFound=1;
		}
		// Text areas
		if(elem.tagName.toUpperCase()=="textarea".toUpperCase() && (elem.value=="" || elem.value==null)){
			if(type=="INDIVIDUAL")
				errorElem.style.display="block";
			else if(type=="DETECT")
				return elem.id;
			else if(type=="COMBINED")
				return false;
			errorFound=1;
		}
		// Radios -- so far no way to check. ALT: have one radio selected by default
		// Checkboxes -- they are optional anyway, isn't it?
		// Select lists
		if(elem.tagName.toUpperCase()=="select".toUpperCase() && (elem.value==null || elem.value=="" || elem.value=="-1")){
			if(type=="INDIVIDUAL")
				errorElem.style.display="block";
			else if(type=="DETECT")
				return elem.id;
			else if(type=="COMBINED")
				return false;
			errorFound=1;
		}		
	}
	if(type=="INDIVIDUAL")
		if(errorFound==0) return true;
		else return false;
	else if(type=="DETECT") return null;
	else return true;
}
function resetAllExcept(formId,list_of_elements){
	var form=document.getElementById(formId);
	var inArguments=0;
	for(var i=0;i<form.elements.length;i++){
		var elem=form.elements[i];
		// Check if exists in exception list
		for(var j=1;j<resetAllExcept.arguments.length;j++){
			argElem=document.getElementById(resetAllExcept.arguments[j]);
			if(argElem==null) continue;
			if(elem.id==argElem.id)
				inArguments=1;
		}
		// Yes, match found -- skip it
		if(inArguments==1){
			inArguments=0;
			continue;
		}
		var errorElem=document.getElementById(elem.id+"_error");
		if(errorElem!=null) errorElem.style.display="none";
	}
}
function validateNumber(elemId){
	var elem=document.getElementById(elemId);
	var value=elem.value;
	if(value=="" || value==null) return false;
	if(isNaN(elem.value)) return false;
	return true;
}
function validateEmail(elemId){
	/* Email format: address@site.comm
	 * Examples:
	 * pk@gmail.com
	 * pulkit.karwal@email.co.in
	 */
	var elem=document.getElementById(elemId);
	var value=elem.value;
	if(value==null || value=="") return false;
	var at_pos=value.indexOf("@");
	var host=value.substring(value.indexOf("@"),value.length);
	var dot_pos=host.indexOf(".")+value.indexOf("@");
	if(at_pos==-1 || dot_pos==-1) return false;
	if(!(0<at_pos && at_pos<dot_pos-1 && dot_pos<value.length-1))
		return false;
	return true;
}
function validatePhone(elemId){
	var elem=document.getElementById(elemId);
	var value=elem.value;
	if(value==null || value=="") return false;
	var stripped = value.replace(/[\(\)\.\-\ \+]/g, '');
	if(isNaN(parseInt(stripped)))
		return false;
	return true;
}