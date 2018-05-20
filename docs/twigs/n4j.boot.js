(function() {
	var System = Java.type("java.lang.System"); 
	
	if (System.getProperty("nudge4j.start")) {
		System.err.println("[Warning] Attempt to start nj again. Ignoring the request.");
		return; 
	} 
	System.setProperty("nudge4j.start", System.currentTimeMillis());
	
	var array = java.lang.Thread.currentThread().getStackTrace();
	for (var i=1;i<array.length;i++) {
		var className = array[i].getClassName();
		if (className.startsWith("java."))  continue;
		if (className.startsWith("javax.")) continue;
		if (className.startsWith("jdk."))   continue;
		if (className.startsWith("sun."))   continue;
		if ((array[i-1].toString()).indexOf(".eval") != -1) {
			System.setProperty("nudge4j.hook.location", ""+array[i]);	
		}
		break;
	}

	var port = 5050; 
	if (System.getProperty("nudge4j.port") === null) {
		System.setProperty("nudge4j.port", 5050);	
	} else {
		port = +System.getProperty("nudge4j.port");
	}
	
	var staticURL = System.getProperty("nudge4j.staticURL");
	if (staticURL === null) {
		staticURL = "https://lorenzoongithub.github.io/nudge4j/localhost.port"; 
	}
	
	var server = com.sun.net.httpserver.HttpServer.create(new java.net.InetSocketAddress(port), 0);
	var engine = new javax.script.ScriptEngineManager().getEngineByName("JavaScript");
	var data =   java.lang.reflect.Array.newInstance( java.lang.Byte.TYPE , 10000);
	var UTF8 = java.nio.charset.StandardCharsets.UTF_8;
	
	function send(httpExchange,array,max, contentType) {
		httpExchange.getResponseHeaders().set("Content-Type",contentType);
		if (contentType == "text/plain") {                                     // Quick Hack
			httpExchange.getResponseHeaders().set("Cache-Control", "no-cache"); // no cache if sent by /js URI
		}
		httpExchange.sendResponseHeaders(200, max);
		httpExchange.getResponseBody().write(array,0,max);
		httpExchange.getResponseBody().close();
	}
	
	function inputStreamToString(inputStream) {
		var buffer = java.lang.reflect.Array.newInstance(java.lang.Character.TYPE, 1024); 
		var out = new java.lang.StringBuilder(); 
		var rin = new java.io.InputStreamReader(inputStream, UTF8);
		while (true) {
			var rsz = rin.read(buffer, 0, buffer.length); 
			if (rsz < 0) break;
			out.append(buffer, 0, rsz); 
		}
		rin.close();
		return out.toString(); 
	}
	
	var cache4scripts = {}; 
	function loadScript(strURL) {
		var code = cache4scripts[strURL];
		if (code === undefined) {
			var inputStream = new java.net.URL(strURL).openConnection();
			code = inputStreamToString(inputStream);
			cache4scripts[strURL] = code;
		}
		return engine.eval(code); 
	}
	engine.put("loadScript",loadScript);
	
	server.createContext("/", new com.sun.net.httpserver.HttpHandler() {
		handle : function(httpExchange)  {
	    	var uri = httpExchange.getRequestURI().toString();
	    	if (uri.startsWith("/js")) {
		    										// HTTP POST                                           // HTTP GET
		    	var payload = (uri.equals("/js")) ? inputStreamToString( httpExchange.getRequestBody() ) : uri.substring(4);
		    	
				// Searching for the snippets in the payload
				var snippets = []; 
				var params = payload.split("&"); 
				for (var i=0;i<params.length;i++) {
					var param = params[i];
					if (param.substring(0,5) == "code=") {
						snippets = param.substring(5).split(",");
						break;
					}
				}
				
				var $0 = null; 
				for (var i=0;i<snippets.length;i++) {
					engine.put("$0",$0);
					var code = decodeURIComponent(snippets[i]);
					try {
						$0 = engine.eval(code);
					} catch (e) {
						// fail fast.
						var os = new java.io.ByteArrayOutputStream();
						e.printStackTrace(new java.io.PrintStream(os));
						var array = ("err::"+os).getBytes(UTF8);
						send(httpExchange, array, array.length, "text/plain");
						return; 
					}	
				}
				var array = ( "" + $0 ).getBytes(UTF8);
				send(httpExchange, array, array.length, "text/plain");
				return; 
			}
	    	
	        if ("/".equals(uri)) uri ="/index.html";
	        var url = staticURL + uri;
       
	        var c = new java.net.URL(url).openConnection();
	        c.setRequestMethod("GET");
	        var responseCode = c.getResponseCode();
	        if (responseCode != 200) {
	        	httpExchange.sendResponseHeaders(responseCode,-1);
	            return; 
	        }
	        
	        while (true) {
	        	var b = is.read();
	        	if (b == -1) break;
	        	data[count++] = b;
	        }
	        
	        if      (uri.endsWith(".ico")) send(httpExchange, data, count, "image/x-icon");
	        else if (uri.endsWith(".css")) send(httpExchange, data, count, "text/css");
	        else if (uri.endsWith(".png")) send(httpExchange, data, count, "image/png");
	        else if (uri.endsWith(".js"))  send(httpExchange, data, count, "application/javascript");
	        else                           send(httpExchange, data, count, "text/html");
	    }
	});
	server.start();
	System.out.println("nudge4j started on port:"+port);
})();
