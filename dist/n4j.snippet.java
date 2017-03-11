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
        m8 = HD.getMethod("set", String.class, String.class);
        Object server = m4.invoke(null, new java.net.InetSocketAddress((int)args[0]), 0);
        m5.invoke(server, "/", java.lang.reflect.Proxy.newProxyInstance(
            HH.getClassLoader(), 
            new Class[] { HH }, 
            new java.lang.reflect.InvocationHandler() {          
                java.nio.charset.Charset UTF8 = java.nio.charset.StandardCharsets.UTF_8;
                byte data[] = new byte[10000];
                java.util.function.Function<Object,String> stringify = (oj) -> 
                    '"'+(""+oj).replace("\\", "\\\\").
                                replace("\"", "\\\"").
                                replace("\n", "\\n").
                                replace("\b", "\\b").
                                replace("\t", "\\t").
                                replace("\r", "\\r").
                                replace("\f", "\\f") + '"';
                void send(Object httpExchange,byte array[],int max, String contentType) throws Exception {
                    m8.invoke(m1.invoke(httpExchange), "Content-Type",contentType);
                    m2.invoke(httpExchange, 200, max);
                    try (java.io.OutputStream os = (java.io.OutputStream) m3.invoke(httpExchange)) { 
                        os.write(array,0, max); 
                    }
                }
                public synchronized Object invoke(Object pxy, java.lang.reflect.Method m, Object[] args) throws Exception {
                    Object httpExchange = args[0]; 
                    String uri = m0.invoke(httpExchange).toString();
                    if (uri.startsWith("/js")) {
                        String query = ((java.net.URI) m0.invoke(httpExchange)).getQuery();
                        String id = '"'+query.substring(0, 10)+'"'; 
                        String code = query.substring(11);
                        Object result = null;
                        byte array[]; 
                        try {
                            result = engine.eval(code);
                            array = ("n4j.on("+id+",null,"+stringify.apply(result)+")").getBytes(UTF8);
                        } catch (Exception e) {
                            e.printStackTrace(new java.io.PrintStream(
                                (java.io.OutputStream)(result = new java.io.ByteArrayOutputStream())));
                            array = ("n4j.on("+id+","+stringify.apply(result)+",null)").getBytes(UTF8);
                        }
                        send(httpExchange,array,array.length,"application/javascript");
                        return null; 
                    }
                    String url = "https://lorenzoongithub.github.io/nudge4j/proxy"+uri;
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
}}).accept( new Object[] { 5050  }); 
// nudge4j:end