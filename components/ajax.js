/* ------Ajax Component------
 * Supports infinite simultaneous calls
 * Usage:
 * 		Call using ajaxRequest(url,handlerFunction) for plain ajax call 
 * 		or ajaxRequestEscaped(url,handlerFunction) for escaped response content
 *		For POST transfer, add parameters true and parameter/query string to ajaxRequest[Escaped] function call.
 * 		url: (string) url of the target of the request
 * 		handlerFunction: (string) name of the function to handle the response, must accept (string) for catching response
 * Warning:
 * 		Restricted handler function names: ajaxRequest, send, createRequest, getXmlHttpRequest
 */
/* Make the entire Ajax call to url, pass control to handlerFunction on completion */
function ajaxRequest(url,handlerFunction,post,params){
	var aReq=new createRequest();
	aReq.setXhr(getXmlHttpRequest());
	aReq.setHandlerFunction(handlerFunction);
	if(post) sendPost(url,params,aReq);
	else send(url,aReq);
}
function ajaxRequestEscaped(url,handlerFunction,post,params){
	var aReq=new createRequest();
	aReq.setXhr(getXmlHttpRequest());
	aReq.setHandlerFunction(handlerFunction);
	if(post) sendPost(url,params,aReq,true);
	else send(url,aReq,true);
}

/* Send request using GET */
function send(url,aReq,escaped){
	var xhr=aReq.getXhr();
	if(xhr.readyState!=4 && xhr.readyState!=0){
		send(url,aReq,escaped); // xhr busy, try again
	}
	xhr.onreadystatechange=function(){
		var handlerFunction=aReq.getHandlerFunction();
		if(xhr.readyState==4){
			if(xhr.status==200){
				if(xhr.responseText.indexOf("<html>")==-1 && xhr.responseText.indexOf("<HTML>")==-1){
					if(escaped)
						eval(handlerFunction+"('"+escape(xhr.responseText)+"')");
					else
						eval(handlerFunction+"('"+xhr.responseText+"')");
				}else eval(handlerFunction+"('Unauthorized access. Please reload the page.')");
			}
			else eval(handlerFunction+"('Unable to connect. Please try again.')");
		}
	}
	xhr.open("GET",url,true);
	xhr.send(null);
}
/* Send request using POST */
function sendPost(url,params,aReq,escaped){
	var xhr=aReq.getXhr();
	if(xhr.readyState!=4 && xhr.readyState!=0){
		sendPost(url,params,aReq,escaped); // xhr busy, try again
	}
	xhr.onreadystatechange=function(){
		var handlerFunction=aReq.getHandlerFunction();
		if(xhr.readyState==4){
			if(xhr.status==200)
				if(escaped)
					eval(handlerFunction+"('"+escape(xhr.responseText)+"')");
				else
					eval(handlerFunction+"('"+xhr.responseText+"')");
			else eval(handlerFunction+"('Unable to connect.')");
		}
	}
	xhr.open("POST",url,true);
	xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhr.setRequestHeader("Content-length", params.length);
	xhr.setRequestHeader("Connection", "close");
	xhr.send(params);
}
/* Data store - so that effin stateChanged and send functions can both refer to xhr and other variables with no scope issues */
function createRequest(){
}
/* Function prototyping to add new setter and getter functions */
createRequest.prototype.setXhr=function(xhr){
	this.xhr=xhr;
}
createRequest.prototype.setHandlerFunction=function(handlerFunction){
	this.handlerFunction=handlerFunction;
}
createRequest.prototype.getXhr=function(){
	return this.xhr;
}
createRequest.prototype.getHandlerFunction=function(){
	return this.handlerFunction;
}
/* Generate an XMLHTTP Request object */
function getXmlHttpRequest() {
	var xmlHttp;
	try{	// Firefox, Opera 8.0+, Safari
		xmlHttp=new XMLHttpRequest();
	}catch (e){		// Internet Explorer
	try{
		xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
	}catch (e){
	try{
		xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
	}catch (e){
		alert("Your browser does not support AJAX!");
		return false;
	}}}
	return xmlHttp;
}