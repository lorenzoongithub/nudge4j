//
// load.examples.js syncs up the ACE editor's content with the URL's search (eg ?example3)
//
// The function code below is just a placeholder to put the code for every example
// in a multiline fashion (see: https://github.com/sindresorhus/multiline) 
// Examples are separated by the string '###############################'
//


// String trim polyfill (see: https://developer.mozilla.org/en/docs/Web/JavaScript/Reference/Global_Objects/String/trim)
if (!String.prototype.trim) { 
  String.prototype.trim = function () {
    return this.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, '');
  };
}


var code = function() {
/*

//
// Write javascript to execute by the JVM  here.
//
java.lang.Thread.activeCount();

###############################

//
// Reporting the 'args' passed to nudge4j
// 
"Nudge4J was initialized with "+args.length+" argument/s." +
"\nThose are: "+java.util.Arrays.toString(args) +
"\nNote: the first argument '"+args[0]+"' is always required. It is the web server's port";

###############################

//
// JVM Uptime
//
var String =            java.lang.String; 
var Integer =           java.lang.Integer;
var ManagementFactory = java.lang.management.ManagementFactory; 

var rt = ManagementFactory.getRuntimeMXBean();
var seconds = Math.floor(rt.getUptime() / 1000);
var s = seconds % 60;
var m = Math.floor(seconds / 60) % 60;
var h = Math.floor(seconds / (60 * 60)) % 24;
var hhmmss = String.format("%02d:%02d:%02d", new Integer(h), new Integer(m), new Integer(s));

'JVM uptime ['+hhmmss+']';

###############################

//	
// System.out/System.err.println
//
java.lang.System.out.println("a message in System.out");
java.lang.System.err.println("a message in System.err");
'now check your JVM log'

###############################

//
// An example of java 8 new Date/Time API.
//
var LocalDate =  Java.type('java.time.LocalDate');
var Month =      Java.type('java.time.Month'); 
var ChronoUnit = Java.type('java.time.temporal.ChronoUnit'); 

var today = LocalDate.now();
var firstDayThisYear = LocalDate.of(today.getYear(), Month.JANUARY, 1);

var days = ChronoUnit.DAYS.between(firstDayThisYear, today);
days + " days have passed since the beginning of the year."

###############################

//
// Thread Dump
//
var map = java.lang.Thread.getAllStackTraces();
var threads = map.keySet().toArray();
var str ='';
for (var i=0;i<threads.length;i++) {
  str+=threads[i]+' - '+threads[i].getState()+'\n';
  var stack = map.get(threads[i]);
  for (var j=0;j<stack.length;j++) {
      str+='   '+stack[j]+'\n';
  }
  str+='\n'
}
str;

###############################

//
// Memory
//
var mem = java.lang.management.ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
var usd = Math.round( mem.getUsed() / 1048576 )
var cmt = Math.round( mem.getCommitted()  / 1048576 )
var max = Math.round( mem.getMax() / 1048576 )

'Used:'+usd+'MB, Committed:'+cmt+'MB, Max:'+max+'MB';

###############################

//
// Binary Numbers
//
var str ='';
for (var i=0;i<1000;i++) {
  str+=i+'='+java.lang.Integer.toBinaryString(i)+'\n'; 
}
str;

###############################
//
// class loading: OKIO.jar
// 
function addURL(urlString) {
  var ucl = java.lang.Class.forName('java.net.URLClassLoader');
  var addURL = ucl.getDeclaredMethod("addURL", [ java.lang.Class.forName('java.net.URL') ]);
  addURL.setAccessible(true);
  var SystemClassLoader = java.lang.ClassLoader.getSystemClassLoader(); 
  addURL.invoke(SystemClassLoader, new java.net.URL(urlString)); // note if the URL is already there it won't be added.
}

addURL('https://search.maven.org/remote_content?g=com.squareup.okio&a=okio&v=LATEST'); 

Packages.okio.ByteString.decodeHex("89504e470d0a1a0a");

###############################

//
// Reactive/RxJava (on version 1.2.6)
// 
function addURL(urlString) {
  var ucl = java.lang.Class.forName('java.net.URLClassLoader');
  var addURL = ucl.getDeclaredMethod("addURL", [ java.lang.Class.forName('java.net.URL') ]);
  addURL.setAccessible(true);
  var SystemClassLoader = java.lang.ClassLoader.getSystemClassLoader(); 
  addURL.invoke(SystemClassLoader, new java.net.URL(urlString)); // note if the URL is already there it won't be added.
}

addURL('https://search.maven.org/remotecontent?filepath=io/reactivex/rxjava/1.2.6/rxjava-1.2.6.jar'); 

var array=[]; 

var observer = new Packages.rx.Observer({ 
   onCompleted : function()  { array.push('done') },
   onError :     function(e) { array.push('onError:'+e) },
   onNext :      function(t) { array.push(t); }
});

Java.type('rx.Observable').just("Hello", "World").subscribe(observer);

array.join('\n')

*/}



var m = /\/\*!?(?:\@preserve)?[ \t]*(?:\r\n|\n)([\s\S]*?)(?:\r\n|\n)[ \t]*\*\//;
var arr = m.exec(code.toString())[1].split('###############################');
var src = location.search;
console.log(src);
if (src == '')          editor.setValue(arr[0].trim());
if (src == '?example1') editor.setValue(arr[1].trim());
if (src == '?example2') editor.setValue(arr[2].trim());
if (src == '?example3') editor.setValue(arr[3].trim());
if (src == '?example4') editor.setValue(arr[4].trim());
if (src == '?example5') editor.setValue(arr[5].trim());
if (src == '?example6') editor.setValue(arr[6].trim());
if (src == '?example7') editor.setValue(arr[7].trim());
if (src == '?example8') editor.setValue(arr[8].trim());
if (src == '?example9') editor.setValue(arr[9].trim());
editor.clearSelection();