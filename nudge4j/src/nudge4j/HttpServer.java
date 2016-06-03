package nudge4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.Charset;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * 
 * Nudge4j lets you access a java program from any modern browser. 
 * <p> 
 * The {@code HttpServer} serves the web console and executes the code posted from the browser. 
 * <p>
 * <p>
 * Example:
 * <p><blockquote><pre>
 * import nudge4j.HttpServer;
 * 
 * public class YourProgram {
 *     public static void main(String args[]) {
 *         HttpServer.post(8080); // serves on port 8080
 *     }
 * }
 * </pre></blockquote><p>
 * <p>
 * Example:
 * <p><blockquote><pre>
 * 
 * import nudge4j.HttpServer;
 * 
 * public class YourProgram {
 *     public static void main(String args[]) {
 *         HttpServer.post(8080, "lorenzo",2016);
 * 
 *         // In the browser you can code:
 *         java.lang.System.out.println(args.length); // returns 2
 *         java.lang.System.out.println(args[0]);     // returns "lorenzo"
 *         java.lang.System.out.println(args[1]);     // returns 2016
 *     } 
 * }
 * </pre></blockquote><p>  
**/
public final class HttpServer {
    
    private HttpServer() {}
    
    private static final Charset UTF8 = Charset.forName("UTF-8");

    /**
     * Starts the nudge4j's HttpServer to let an HttpClient (browser) run code on the JVM.
     * 
     * @param port httpServer port
     * @param args java objects you might want your script to access  
    **/
    public static void start(int port,Object... args) {
        try {
            start0(port,args);
        } catch (IOException e) {
            throw new RuntimeException("I/O Failure",e);
        }
    }
    
    private static void start0(int port,Object... args) throws IOException {
        System.out.println("nudge4j - version 1.0");
        System.out.println("          serving on port "+port);
        System.out.println();
        final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript"); 
        engine.put("args", args);
        com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange  httpExchange) throws IOException {
                String uri = httpExchange.getRequestURI().toString();
                if (uri.equals("/runJS")) {
                    String code = new String(read(httpExchange.getRequestBody()), UTF8);
                    Object output; 
                    try {
                        output = engine.eval(code);
                    } catch (Exception e) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        baos.write("$error:".getBytes(UTF8));
                        e.printStackTrace(new PrintStream(baos));
                        write(httpExchange, baos.toByteArray(),"text/plain");
                        return; 
                    }
                    write(httpExchange,(output+"").getBytes(UTF8),"text/plain");
                    return; 
                }
                URL url;
                if ( uri.equals("/") || uri.equals("/web") || (url = HttpServer.class.getResource("web"+uri)) == null) {
                    byte[] data = read(HttpServer.class.getResource("web/console.html").openStream());
                    write(httpExchange,data,"text/html");
                    return;
                } 
                byte[] data = read(url.openStream());
                if      (uri.endsWith(".html"))      write(httpExchange,data,"text/html");
                else if (uri.equals("/favicon.ico")) write(httpExchange,data,"image/x-icon");
                else if (uri.endsWith(".gif"))       write(httpExchange,data,"image/gif");
                else if (uri.endsWith(".js"))        write(httpExchange,data,"application/x-javascript");
            }
        });
        server.setExecutor(null);  
        server.start();
    }
    
    private static final void write(HttpExchange httpExchange, byte[] array, String contentType) throws IOException {
        httpExchange.getResponseHeaders().set("Content-Type", contentType);
        httpExchange.sendResponseHeaders(200, array.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(array);
        os.close();
    }
    
    private static final byte[] read(InputStream inputStream) throws IOException {
        byte[] array = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            int len = inputStream.read(array);
            if (len == -1) break;
            baos.write(array, 0, len);
        }
        inputStream.close();
        return baos.toByteArray();
    }
}