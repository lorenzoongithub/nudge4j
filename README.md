Nudge4J
=======

nudge4j lets you talk to a java application from the browser.
It lets you send code from the browser and gets it executed on the JVM.

There is nothing to download.
All you have to do is add this bit of java code in your program and get it executed.

```java
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
```


![nudge4j's console](nudge4j.console.png "nudge4j's console ")










Twitter: <a href='https://twitter.com/nudge4jofficial'>nudge4j</a>
