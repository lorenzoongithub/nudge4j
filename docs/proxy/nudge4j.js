//
// nudge4j.js defines a global object named n4j
//
// n4j is used to communicate (AJAX) with the webserver started by nudge4j java snippet.
//
// n4j.execCode(url, code, callback) --> send the code at the webserver's URL (e.g. http://localhost:5050)
// n4j.on(id,err,response) --> this function is invoked automatically by the script returned by nudge4j to full fill the request it received.
//
// 
(function() { 
	
	n4j = {};
	var idCounter = 0; 
	var generateId = function() {
		// Produces a 8-char-string like this "iwdekc40" (related to the current time);
		var s = (new Date().getTime()).toString(36);
		s = s.substring(s.length-8);
		
		// Adds 2 chars (e.g. 'a4', '0c') depending on the idCounter 
		// (the idCounter loops till 1296 before restarting at 0) 
		idCounter=(idCounter+1)%(36*36);
		if (idCounter<36) {
			s = s+'0';
		}
		s = s + idCounter.toString(36);  
		return s;
	}
	
	
	var id2callback = {};
	
	n4j.execCode = function(url, code, callback) {
		var id = generateId();
		
		id2callback[id]= {
			time :     new Date().getTime(), 	
			callback : callback 
		}
		
		var script = document.createElement('script');
		script.type = 'text/javascript'; 
		script.async = true;
	    script.src = url+'/js?'+id+':'+encodeURIComponent(code);
		var scrX = document.getElementsByTagName('script')[0]; 
		scrX.parentNode.insertBefore(script, scrX);
	};
	
	n4j.execFunc = function(url, f, arg, callback) {
		var code = '(function() { var arg = ' + JSON.stringify(arg) +'; var f = '+f +'; return f(arg); })();';
        n4j.execCode(url, code, callback);
    };
	
	n4j.on = function(id, err, res) {
		if (id2callback[id]) {
			id2callback[id].callback(err,res); 
			delete id2callback[id];
		}
	}
	
	//
	// Cycling to verify whether we need to issue any timeout.
	// 
	function loopy() {
		var now = new Date().getTime(); 
		for (var id in id2callback) {
			var v = id2callback[id];
			if (now - v['time'] > 10000) {
				n4j.on(id, "TIMEOUT", null);
			}
		}
		setTimeout(loopy, 5000);
	};
	loopy(); 
})();
