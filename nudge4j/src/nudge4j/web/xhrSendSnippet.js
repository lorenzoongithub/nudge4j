//
// Defines the function to post the snippet of code on the serverside. 
//
var xhrSendSnippet = (function() {
	var xhrPost = function(url, postData, callback) {
		var x = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP');
		x.onreadystatechange = function() {
			if (x.readyState != 4) {
				return;
			}
			if (x.status == 200) {
				callback(null, x.responseText);
				return;
			}
			callback('Failure on HTTP Post',null);
		};
		
		try {
			x.open('POST', url, true);
			x.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
			x.send(postData);
		} catch (e) {
			callback('Failure on HTTP Post - '+e,null)
		}
	};
	
	return function(snippet, callback) {	
		xhrPost('/runJS', snippet, function(err, content) {
			if (err != null) {
				callback(err, null);
				return;
			} 
			if (content.substring(0,7) == '$error:') {
				callback(content.substring(7),null); 
			} else {
				callback(null, content);	
			}
		});
    };
})();