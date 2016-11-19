import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * N4J's bytecode is distributed as a hexadecimal string in the integration snippet.
 * 
 * The class N4J is accessible only via the static method 'start'.
 * The method 'start' launches a minimal HttpServer capable of 
 * 1. returning the landing page (an HTML page which just wraps the script n4j.js to create the web console)
 * 2. handling http posts /runJS to execute nashorn snippets and return the .toString()
 * 
 * The output produced by the execution of javascript is returned as String (output+"").
 * If the execution of javascript causes an Exception, the stacktrace is stringified and returned prefixed by the string "$error:"     
 *  
**/
public final class N4J implements HttpHandler {
	
	private ScriptEngine engine;
	
	private N4J(ScriptEngine engine) {
		this.engine = engine;
	}
	
	public void handle(final HttpExchange  httpExchange) throws IOException {
        final String uri = httpExchange.getRequestURI().toString();
        if (uri.equals("/runJS")) {
            final InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), UTF8);
            final Object output; 
            try {
            	output = engine.eval(isr);
            } catch (final Exception e) {
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write("$error:".getBytes(UTF8));
                e.printStackTrace(new PrintStream(baos));
                write(httpExchange, baos.toByteArray(),"text/plain");
                return; 
            }
            write(httpExchange,(output+"").getBytes(UTF8),"text/plain");
            return; 
        }
        write(httpExchange,landingPage,"text/html");
    }
	
	private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final byte[] landingPage = "<!doctype html><script src=https://lorenzoongithub.github.io/nudge4j/dist/console.html.js></script>".getBytes(UTF8);
    
    public static final void start(final int port, final Object... args) throws IOException {
    	final HttpServer server = HttpServer.create(new InetSocketAddress(port), 0); // fail fast.
        final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript"); 
        engine.put("args", args);
        server.createContext("/", new N4J(engine));
        server.setExecutor(null);  
        server.start();
        System.out.println("nudge4j serving on port:"+port);
    }
	
    private static final void write(final HttpExchange httpExchange, final byte[] array, final String contentType) throws IOException {
        httpExchange.getResponseHeaders().set("Content-Type", contentType);
        httpExchange.sendResponseHeaders(200, array.length);
        final OutputStream os = httpExchange.getResponseBody();
        os.write(array);
        os.close();
    }
}
