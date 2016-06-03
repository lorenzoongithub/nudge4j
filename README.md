# nudge4j
a web console for Java 8 applications

nudge4j provides an environment for experimenting with code. You can write, run, and examine the results of code from a browser.

It executes code written in javascript (<a href='http://www.oracle.com/technetwork/articles/java/jf14-nashorn-2126515.html'>nashorn</a>) and comes with an <a href='http://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/package-summary.html'>Http Server</a> and an <a href='https://ace.c9.io'>Ace Editor</a> to let code fly from the browser to any java application.

![nudge4j web console](nudge4j.console.png "nudge4j web console in action")

# download

get the <a href='https://github.com/lorenzoongithub/nudge4j/blob/master/nudge4j/dist/nudge4j.jar?raw=true'>latest jar</a>

# get started

Add <a href='https://github.com/lorenzoongithub/nudge4j/blob/master/nudge4j/dist/nudge4j.jar?raw=true'>nudge4j.jar</a> to your classpath and call nudge4j from your code.

```java

import nudge4j.HttpServer;

public class YourProgram
{
   public static void main(String[] args) throws Exception
   {
       // This runs the nudge4j HTTP server on port 8080</span>
       HttpServer.start(8080);
       
       // rest of the code
       // ...
   }
}
```

<a name="Examples"></a>
# Examples

Copy/Paste the gists into nudge4j's web console

<a href='#'>local date</a> 

<a href='#'>threads</a> 

<a href='#'>lambda in Java 8</a> 

<a href='#'>memory</a>  

<a href='#'>PNG Encoder</a> 

<a href='#'>Rx</a>  

# About
