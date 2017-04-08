package nudge4j;

/**
 * N4J is a class which wraps a snippet of code to copy/paste into any java 8 program.
 * Since it makes no assumptions on the destination's code:
 *  
 * - Classes are fully qualified. 
 * - Classes from the package 'com.sun.net.httpserver' are accessed via introspection to prevent  
 *   access restrictions from some IDEs (e.g.: Eclipse)
 * - The snippet is wrapped inside a Consumer function to avoid variable scope conflicts.    
 * - Security Concerns (eg. XSRF, XSS, CORS) are addressed.
 * 
 * N4J is a class which starts a minimal HttpServer.
 * 
 * The HttpServer is capable of executing server-side JavaScript (Nashorn) when posted on /js
 * The output produced by the execution is returned as 'text/plain'
 * Any exception is returned prefixed by the string 'err::' 
 *    
 * Any other call is handled by acting as a proxy for 
 * https://lorenzoongithub.github.io/nudge4j/localhost.port/
 * 
 * For example:
 * http://localhost:5050/index.html is equivalent (in content) of
 * https://lorenzoongithub.github.com/nudge4j/localhost.port/index.html
 * 
**/
public class N4J { static {
    
// nudge4j:begin
(new java.util.function.Consumer<Object[]>() { public void accept(Object args[]) {
    try {
        javax.script.ScriptEngine engine = new javax.script.ScriptEngineManager().getEngineByName("JavaScript");
        engine.put("args", args);
        String p = "com.sun.net.httpserver.";
        Class<?> 
        HH = Class.forName(p+"HttpHandler"),
        HE = Class.forName(p+"HttpExchange"),
        HD = Class.forName(p+"Headers"), 
        HS = Class.forName(p+"HttpServer");
        java.lang.reflect.Method 
        m0 = HE.getMethod("getRequestURI"),
        m1 = HE.getMethod("getResponseHeaders"),
        m2 = HE.getMethod("sendResponseHeaders", int.class, long.class),
        m3 = HE.getMethod("getResponseBody"),
        m4 = HS.getMethod("create", java.net.InetSocketAddress.class, int.class),
        m5 = HS.getMethod("createContext", String.class, HH),
        m6 = HS.getMethod("setExecutor", java.util.concurrent.Executor.class),
        m7 = HS.getMethod("start"),
        m8 = HD.getMethod("set", String.class, String.class),
        m9 = HE.getMethod("getRequestBody"),
        mA = HE.getMethod("getRequestHeaders");
        Object server = m4.invoke(null, new java.net.InetSocketAddress(
                java.net.InetAddress.getLoopbackAddress(), (int) args[0]), 0);
        m5.invoke(server, "/", java.lang.reflect.Proxy.newProxyInstance(
            HH.getClassLoader(), 
            new Class[] { HH }, 
            new java.lang.reflect.InvocationHandler() {          
                java.nio.charset.Charset UTF8 = java.nio.charset.StandardCharsets.UTF_8;
                byte data[] = new byte[10000];
                void send(Object httpExchange,byte array[],int max, String contentType) throws Exception {
                    m8.invoke(m1.invoke(httpExchange), "Content-Type",contentType);
                    m2.invoke(httpExchange, 200, max);
                    try (java.io.OutputStream os = (java.io.OutputStream) m3.invoke(httpExchange)) { 
                        os.write(array,0, max); 
                    }
                }
                @SuppressWarnings("rawtypes")
                public synchronized Object invoke(Object pxy, java.lang.reflect.Method m, Object[] params) throws Exception {
                    Object httpExchange = params[0];
                    {//DEBUG
                        System.out.println("URI: "+m0.invoke(httpExchange).toString());
                        java.util.Map requestHeaders = (java.util.Map)mA.invoke(httpExchange);
                        for (Object k : requestHeaders.keySet()) {
                            System.out.println(k+":::"+requestHeaders.get(k));
                        }
                        System.out.println();
                        System.out.println();
                    }//END-DEBUG
                    String uri = m0.invoke(httpExchange).toString();
                    if (uri.startsWith("/js")) {
                        java.util.Map requestHeaders = (java.util.Map)mA.invoke(httpExchange);
                        if (requestHeaders.containsKey("Origin")) {
                            String origin = ""+((java.util.List)requestHeaders.get("Origin")).get(0);
                            if (origin.equals("http://localhost:"+args[0]) == false &&  
                                origin.equals("http://127.0.0.1:"+args[0]) == false) return null; 
                        } else if (requestHeaders.containsKey("Referer")) {
                            String referer = ""+((java.util.List)requestHeaders.get("Referer")).get(0);
                            if (referer.startsWith("http://localhost:"+args[0]) == false &&  
                                referer.startsWith("http://127.0.0.1:"+args[0]) == false) return null;
                        } else return null; 

                        byte array[]; 
                        try (java.io.Reader r = new java.io.InputStreamReader( (java.io.InputStream) m9 .invoke(httpExchange), UTF8 )) {
                            array = (""+engine.eval(r)).getBytes(UTF8); 
                        } catch (Exception e) {
                            java.io.OutputStream os = new java.io.ByteArrayOutputStream();
                            e.printStackTrace(new java.io.PrintStream(os));
                            array = ("err::"+os).getBytes(UTF8); 
                        }
                        send(httpExchange,array,array.length,"text/plain");
                        return null; 
                    }
                    if ("/".equals(uri)) uri ="/index.html";
                    String url = "https://lorenzoongithub.github.io/nudge4j/localhost.port"+uri;
                    java.net.HttpURLConnection c = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
                    c.setRequestMethod("GET");
                    int responseCode = c.getResponseCode();
                    if (responseCode != 200) {
                        m2.invoke(httpExchange,responseCode,-1);
                        return null; 
                    }
                    int count =0;
                    try (java.io.InputStream is = c.getInputStream()) {
                        for (int b=is.read();b!=-1;b=is.read()) data[count++] = (byte) b;
                    }
                    send(httpExchange,data, count, (
                         (uri.endsWith(".ico")) ? "image/x-icon" :
                         (uri.endsWith(".css")) ? "text/css" :
                         (uri.endsWith(".png")) ? "image/png" :  
                         (uri.endsWith(".js"))  ? "application/javascript" : 
                                                  "text/html"));
                    return null; 
                }
            }
        ));
        m6.invoke(server, new Object[] { null });
        m7.invoke(server);
        System.out.println("nudge4j serving on port:"+args[0]);
    } catch (Exception e) {
        throw new InternalError(e);
    }
}}).accept( new Object[] { 5050 }); 
// nudge4j:end
}

public static void main(String args[]) { System.out.println("kicking off nudge4j."); }
    
}