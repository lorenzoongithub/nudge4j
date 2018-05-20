//
//Returns something like:
//[
// { ref: "c:\temp\test.jar",  classes: [], warn: "MISSING FILE" },
// { ref: "c:\temp\test2.jar", classes: [ "com/test/Main", "com/test/UI" , ... ] },
// { ref: "c:\temp\test2.jar", classes: [], warn: "DUPLICATE OCCURRENCE" }
//]
//
(function() {
  function toClassPathArray() {
    var classpath = java.lang.System.getProperty("java.class.path");
    return classpath.split(java.lang.System.getProperty("path.separator"));
  }

  //given a reference to a jar file, we traverse the jar and return the total number of classes in the jar
  //and the list of packages.
  function countClassesAndPackages(strFile) {
    var file = new java.io.File(strFile);
    if (file.exists() == false) {
      return -1;
    }
    var jarFile = new java.util.jar.JarFile(file);
    var entries = jarFile.entries();
    var count = 0;
    var set = {};
    while (entries.hasMoreElements()) {
      var entry = entries.nextElement();
      if (entry.getSize() != 0) {
        var name = entry.getName();
        if (name.endsWith(".class")) {
          if (name.lastIndexOf("$") == -1) {
            // exclude innerclasses. (lazy approach)
            var pkg = name.substring(0, name.lastIndexOf("/"));
            set[pkg] = 1;
            count++;
          }
        }
      }
    }
    jarFile.close();

    var pkgs = [];
    for (var i in set) {
      pkgs.push(i);
    }

    return {
      count: count,
      pkgs: pkgs
    };
  }

  var dupList = {};
  var response = [];
  var array = toClassPathArray();
  for (var i = 0; i < array.length; i++) {
    if (array[i].endsWith(".jar")) {
      if (dupList[array[i]]) {
        response.push({
          ref: array[i],
          count: -2
        });
      } else {
        dupList[array[i]] = 1;

        var oj = countClassesAndPackages(array[i]);

        if (oj == -1) {
          response.push({
            ref: array[i],
            count: -1
          });
        } else {
          response.push({
            ref: array[i],
            count: oj.count,
            pkgs: oj.pkgs
          });
        }
      }
    }
  }
  var json = JSON.stringify(response, null, 2);
  return json;
})();