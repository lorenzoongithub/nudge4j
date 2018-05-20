laserBeam = (function() {
	var clazzObject =  Java.type('java.lang.Object').class;
	var clazzBoolean = Java.type('java.lang.Boolean').class;
	var clazzChar =    Java.type('java.lang.Character').class; 
	var clazzNumber =  Java.type('java.lang.Number').class;
	var clazzByte =    Java.type('java.lang.Byte').class;
	var clazzString =  Java.type('java.lang.String').class;
	var _toHexString = Java.type('java.lang.Integer').toHexString;

	//
	// Returns the simple name for any Java class (and dealing well with arrays). 
	// Examples: String, HashMap, boolean[][], double[]   
	//
	function typeToString(clz) {
		var dim = ''; 
		while (clz.isArray()) {
			clz = clz.getComponentType();
			dim+='[]'; 
		}
		return clz.getSimpleName()+dim; 
	}

	//
	// Returns a string representation of a 'pristine' object or null otherwise.
	// For 'pristine' we intend 
	// null, a native Javascript, boolean, number (int, short, long, float or double), character, byte, string. 
	//
	function stringifyIfPristine(oj) {
	  if (oj == null)                         return 'null';
	  // if (Java.isScriptObject(oj))            return 'JavaScript:'+oj; // <-- it does not work??? EXAMPLE
	  try { 
	  var clz = oj.getClass();
	  } catch (e) {
		  return 'No Clazz:'+oj;
	  }
	  if (clz == clazzBoolean )               return ''+oj;
	  if (clz.getSuperclass() == clazzNumber) return ''+oj; 
	  if (clz == clazzByte )                  return ''+oj; 
	  if (clz == clazzString )                return JSON.stringify(oj);
	  if (clz == clazzChar )                  { var t = JSON.stringify(''+oj); return '\''+t.substring(1,t.length-1)+'\''; } 
	  return null;
	}


	function explore(oj, level) {
	  if (level == null) level = 0; 
	  var str =  stringifyIfPristine(oj);
	  if (str !== null) return str; 
	  var clz = oj.getClass();
	  if (clz.isArray()) {
	    var length = java.lang.reflect.Array.getLength(oj);
	    var name   = clz.getComponentType().getSimpleName();
	    var array = [];
	    for (var i=0;i<length;i++) {
	      array.push( explore(oj[i], level+1) );
	    }
	    return {
	      name : name,
	      array : array
	    };
	  }
	  
	  var output = {
	    name : clz.getSimpleName(),
	    hash : _toHexString(oj.hashCode()),
	    lines : []
	  };
	  
	  while (true) {
		if (clz == clazzObject) break;  
	    var fields = clz.getDeclaredFields();
	    var names = [];
	    for (var i=0;i<fields.length;i++) {
	      var f = fields[i];
	      if (f.getName().equals("this$0")) { continue; }       // for inner classes exclude the reference to outer class.
	      if (level > 0 && f.getModifiers() &  8) { continue; } // Skip static fields for any non-root level.
	      f.setAccessible(true);
	      
	      (function(xoj, level) {
	    	  var str = stringifyIfPristine(xoj);
	    	  if (str !== null) {
	    		  output.lines.push({
	                  modifiers : f.getModifiers(),
	                  type : typeToString( f.getType() ),
	                  name : f.getName(),
	                  str  : str });
	    		  return;
	    	  } 
		    	  
	    	  var clz = xoj.getClass();
	    	  
	    	  if (clz.isArray()) {
	    	      var componentType = clz.getComponentType();

	    	      // change this to 2 and JPanel breaks (unless I am filtering out static) :-(
	    	      if (level > 1) {
	    	    	  output.lines.push({
	                      modifiers : f.getModifiers(),
	                      type : typeToString( f.getType() ),
	                      name : f.getName(),
	                      str  : clz.getName() + "@" + _toHexString(xoj.hashCode()) });
	    	    	  return; 
	    	      } 
	    	      var len = java.lang.reflect.Array.getLength(xoj);
	    	      var array = [];
	    	      for (var i=0;i<len;i++) {
	    	          array.push( explore(xoj[i],level+1) ); 
	    	      }
	    	      output.lines.push({
	                  modifiers : f.getModifiers(),
	                  type : typeToString( f.getType() ),
	                  name : f.getName(),
	                  str : ''+xoj,
	                  arr : array });
	    	      return;
	    	  } 
	    	  
	    	  output.lines.push({
	              modifiers : f.getModifiers(),
	              type : typeToString( f.getType() ),
	              name : f.getName(),
	              str : clz.getName() + "@" + _toHexString(xoj.hashCode()), 
	              val : (level > 2) ? undefined : explore(xoj,level+1) });  
	      })(f.get(oj), level);      
	      

	      f.setAccessible(false); 
	    }
	    clz = clz.getSuperclass();
	  }
	  return output;
	}

	return function(oj) {
		return JSON.stringify(explore(oj),null,2); 
	};
})();
