   
nudge4j lets you talk to a java application from the browser.
It lets you send code from the browser and gets it executed on the JVM.

There is nothing to download.
All you have to do is add this bit of java code somewhere in your program where it's going to be executed.

 


files and folders in nudge4j.


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
