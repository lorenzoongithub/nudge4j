package nudge4j;

/**
 * N4J is a class which wraps a snippet of code ready to be added into any java 8 program.
 * In order to allow a truly seamless copy/paste-ability:
 *  
 * - Classes are fully qualified, so that no import operation is required.
 * - Classes from the package 'com.sun.net.httpserver' are accessed via introspection to prevent  
 *   access restrictions from some IDEs (e.g.: Eclipse)
 * - The snippet is wrapped inside a Consumer function to avoid variable scope conflicts.    
 * 
 * 
 * N4J is a class which starts a minimal HttpServer.
 * 
 * The HttpServer is capable of returning client-side javascript 
 * when queried like this: /js?[id]:[code]
 * 
 * Examples:
 * 
 * /js?1001300552:5-2          ==> n4j.on('1001300552',null,'3');
 * /js?1001300554:Math.sqrt(4) ==> n4j.on('1001300554',null,'2');
 * 
 * The [id] is a 10 character long string to uniquely identify the call.
 * The [code] is nashorn/javascript which is executed inside the JVM.
 * 
 * The output produced by the execution of nashorn/javascript is returned 
 * as content-type text/javascript.
 *   
 * n4j.on('[id]',null, ''+output);
 *  
 * If the execution of javascript causes an Exception, the stacktrace is 
 * stringified and returned as a second parameter of the on call
 * n4j.on('[id]',''+exception,null);
 *    
 * Any other call is handled by acting as a proxy for 
 * https://lorenzoongithub.github.io/nudge4j/proxy/
 * 
 * For example:
 * http://localhost:5050/index.html is equivalent (in content) of
 * https://lorenzoongithub.github.com/nudge4j/proxy/index.html
 * 
**/
public class N4J { static {
    
// nudge4j:begin
(new java.util.function.Consumer<Object[]>() { public void accept(Object args[]) {
    try {
        javax.script.ScriptEngine engine = new javax.script.ScriptEngineManager().getEngineByName("JavaScript");
        engine.put("args", args);
        Class<?> HttpHandler= Class.forName("com.sun.net.httpserver.HttpHandler");
        java.lang.reflect.Method 
        getRequestURI =       Class.forName("com.sun.net.httpserver.HttpExchange").getMethod("getRequestURI"),
        getResponseHeaders =  Class.forName("com.sun.net.httpserver.HttpExchange").getMethod("getResponseHeaders"),
        set =                 Class.forName("com.sun.net.httpserver.Headers").     getMethod("set", String.class,String.class),
        sendResponseHeaders = Class.forName("com.sun.net.httpserver.HttpExchange").getMethod("sendResponseHeaders",int.class,long.class),
        getResponseBody =     Class.forName("com.sun.net.httpserver.HttpExchange").getMethod("getResponseBody"),
        getQuery =            Class.forName("java.net.URI").                       getMethod("getQuery"),
        create =              Class.forName("com.sun.net.httpserver.HttpServer").  getMethod("create", java.net.InetSocketAddress.class, int.class),
        createContext =       Class.forName("com.sun.net.httpserver.HttpServer").  getMethod("createContext", String.class, HttpHandler),
        setExecutor =         Class.forName("com.sun.net.httpserver.HttpServer").  getMethod("setExecutor", java.util.concurrent.Executor.class),
        start =               Class.forName("com.sun.net.httpserver.HttpServer").  getMethod("start");
        Object server = create.invoke(null, new java.net.InetSocketAddress((int)args[0]), 0);
        createContext.invoke(server, "/", java.lang.reflect.Proxy.newProxyInstance(
            HttpHandler.getClassLoader(), 
            new Class[] { HttpHandler }, 
            new java.lang.reflect.InvocationHandler() {
                private java.nio.charset.Charset UTF8 = java.nio.charset.Charset.forName("UTF-8");
                private byte data[] = new byte[200000];
                private java.util.function.Function<Object,String> stringify = (oj) -> {
                    return "\""+(""+oj).replace("\\", "\\\\").replace("\n", "\\n").replace("\b", "\\b").
                                        replace("\t", "\\t").replace("\r", "\\r").replace("\f", "\\f").
                                        replace("\"", "\\\"") +"\"";
                };
                
                private void send(Object httpExchange,byte array[],int max, String contentType) throws Exception {
                    set.invoke(getResponseHeaders.invoke(httpExchange), "Content-Type",contentType);
                    sendResponseHeaders.invoke(httpExchange, 200, max);
                    java.io.OutputStream os = (java.io.OutputStream) getResponseBody.invoke(httpExchange); 
                    os.write(array,0, max);
                    os.close();
                }
                
                public synchronized Object invoke(Object pxy, java.lang.reflect.Method mthd, Object[] args) throws Throwable {
                    Object httpExchange = args[0]; 
                    String uri = getRequestURI.invoke(httpExchange).toString();
                    if (uri.startsWith("/js")) {
                        String query = (String) getQuery.invoke(getRequestURI.invoke(httpExchange));
                        String id = '"'+query.substring(0, 10)+'"'; 
                        String code = query.substring(11);
                        Object result = null;
                        try {
                            result = engine.eval(code);
                        } catch (Exception e) {
                            final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                            e.printStackTrace(new java.io.PrintStream(baos));
                            byte array[] = ("n4j.on("+id+","+stringify.apply(baos)+",null)").getBytes(UTF8);
                            send(httpExchange,array,array.length,"application/javascript");
                            return null; 
                        }
                        byte[] array = ("n4j.on("+id+",null,"+stringify.apply(result)+")").getBytes(UTF8);
                        send(httpExchange,array,array.length,"application/javascript");
                        return null; 
                    }
                    String url = "https://lorenzoongithub.github.io/nudge4j/proxy"+uri;
                    java.net.HttpURLConnection con = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
                    con.setRequestMethod("GET");
                    int responseCode = con.getResponseCode();
                    if (responseCode != 200) {
                        sendResponseHeaders.invoke(httpExchange,responseCode,-1);
                        return null; 
                    }
                    java.io.InputStream is = con.getInputStream();
                    int count = 0; 
                    while (true) {
                        int b = is.read();
                        if (b == -1) break; 
                        data[count++] = (byte) b;
                    }
                    is.close();
                    send(httpExchange,data, count,  (
                         (uri.endsWith(".ico")) ? "image/x-icon" :
                         (uri.endsWith(".css")) ? "text/css" :
                         (uri.endsWith(".png")) ? "image/png" :  
                         (uri.endsWith(".js"))  ? "application/javascript" : 
                                                  "text/html"));
                    return null; 
                }
            }
        ));
        setExecutor.invoke(server, new Object[] { null });
        start.invoke(server);
        System.out.println("nudge4j serving on port:"+args[0]);
    } catch (Exception e) {
        throw new InternalError(e);
    }
}}).accept( new Object[] { 5050  }); 
// nudge4j:end
}

public static void main(String args[]) { System.out.println("kicking off nudge4j."); }
    
}