//
// nudge4j.js defines a global object named n4j
// n4j is used to communicate (AJAX) with the webserver started by nudge4j java snippet.
// n4j.execCode(code, callback)      --> send the code at the webserver's URL (e.g. http://localhost:5050)
// n4j.execFunc(func, arg, callback) --> serializes the function and the argument into a code which is then passed to execCode
// 
var n4j = {};

n4j.execCode = function(code, callback) {
	var x = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP');
	x.onreadystatechange = function() {
		if (x.readyState != 4) {
			return;
		}
		if (x.status == 200) {
			if (x.responseText.substring(0,5)==='err::') {
				callback(x.responseText.substring(5),null);	
			} else {
				callback(null,x.responseText);
			}
			return;
		}
		callback('Http Error. Status:'+x.status,null);
	};
	
	try {
		x.open('POST', 'js', true);
		x.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
		x.send(code);
	} catch (e) {
		callback('Http Exception: '+e,null);
	}
}

n4j.execFunc = function(f, arg, callback) {
	var code = '(function() { var arg = ' + JSON.stringify(arg) +'; var f = '+f +'; return f(arg); })();';
    n4j.execCode(code, callback);
};
