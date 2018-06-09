//
// Inspired by this gist: 
// https://gist.github.com/sundararajana/f01c3edd941c1af99e0c/216c9c92e349f13a0478f96d65dd67df6ed2c9b8
//
function dumpAllClasses(options) {
  var EnumSet =              java.util.EnumSet; 
  var JavaFileObject =       javax.tools.JavaFileObject;
  var ToolProvider =         javax.tools.ToolProvider;
  var DiagnosticCollector =  javax.tools.DiagnosticCollector;
  var StandardLocation =     javax.tools.StandardLocation;
  
  var compiler = ToolProvider.getSystemJavaCompiler();
  var fm = compiler.getStandardFileManager(new DiagnosticCollector(), null, null); 
  var kinds =  EnumSet.of(JavaFileObject.Kind.CLASS);
  var itr = fm.list(StandardLocation.PLATFORM_CLASS_PATH, '', kinds, true).iterator();
  var sb = new java.lang.StringBuilder();
  var count = 0;
  while(itr.hasNext()) {
    var jfo = itr.next();
    var uri = jfo.toUri();
    if (uri.getScheme().equals("jar")) {
      var schemeSpecPart = uri.getSchemeSpecificPart();
      if (options && options.skipJDK && 
          schemeSpecPart.contains("/jre/lib/") || 
          schemeSpecPart.contains("/rt.jar/")) {
          continue; 
      }
    }
    var name =  fm.inferBinaryName(StandardLocation.PLATFORM_CLASS_PATH, jfo);
    if (name.contains('$')) continue; // skip inner classes.
    count++;
    sb.append( name +"\n" );
  }
  return '[Total:'+count+']\n'+sb.toString();
}