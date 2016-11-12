Nudge4J
=======

nudge4j lets you talk to a java application from the browser.
It lets you send code from the browser and gets it executed on the JVM.

There is nothing to download.
All you have to do is add this bit of java code somewhere in your program where it's going to be executed.

{code}
/* nudge4j - integration */ 
{
   try {
      java.net.URL             n4j_jar = new java.net.URL("https://lorenzoongithub.github.io/nudge4j/dist/n4j.jar");
      java.net.URL[]           n4j_jars = new java.net.URL[] { n4j_jar };
      java.net.URLClassLoader  n4j_loader = new java.net.URLClassLoader(n4j_jars);
      java.lang.Class<?>       n4j_class = n4j_loader.loadClass("N4J");
      java.lang.reflect.Method n4j_start = n4j_class.getMethod("start", int.class, Object[].class);
      n4j_start.invoke(null, 5050, null); // you can customize these parameters: port and arguments.
      n4j_loader.close();
   } catch (Exception e) {
      throw new RuntimeException(e);
   }
}
{code}


##files and folders in nudge4j.


 \src
   N4J.java                     (the webserver)
   console.html                 (the console to talk to the webserver)

 \build
   BuilderN4Jjar.java           (a stand-alone java program to build N4J.jar from N4J.java
   transpilerHTML2JS.html       (a stand-alone web page to use to manually compile console.html into n4j.js)
  
 \docs 
   ...                          manually crafted documentation and resources

 \docs\dist\ 
   console.html.js              the build artifacts
   n4j.jar

Twitter: <a href='https://twitter.com/nudge4jofficial'>nudge4j</a>
