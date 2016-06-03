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

# Examples

Copy/Paste the gists into nudge4j's web console

<a href='https://gist.github.com/lorenzoongithub/127278b6478e9b35e3fca13b566f88b5'>local date</a> 

<a href='https://gist.github.com/lorenzoongithub/01fdf87f9f1a4c60a41ba529d9cd534e'>threads</a> 

<a href='https://gist.github.com/lorenzoongithub/5aa2f94967d261a447457500a7536f90'>lambda in Java 8</a> 

<a href='https://gist.github.com/lorenzoongithub/d9964b85069fef1fd3794aa366d95a79'>memory</a>  

<a href='https://gist.github.com/lorenzoongithub/dace58ea8dde941a21209c0fbba4561e'>PNG Encoder</a> requires okio.jar in your classpath

Please contact me if you have a gist you would like to add here.



# About
