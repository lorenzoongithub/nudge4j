import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

/**
 * Compiles N4J.java and generates the file 'n4j.snippet.java'
 * n4j.snippet.java represents the piece of java code that needs to be added to any java application to
 * enable nudge4j.
**/
public class BuilderN4J {
    public static void main(String args[]) throws Exception {
        System.out.println("BuilderN4J");
        System.out.println("==========");
        System.out.println();
        Path fileIn =  Paths.get("src/nudge4j/N4J.java");
        Path fileOut = Paths.get("docs/dist/n4j.snippet.java");
        System.out.println("Compiling " + fileIn.toAbsolutePath());
        
        String code = new String(Files.readAllBytes(fileIn),"UTF-8");
        int start = code.indexOf("// nudge4j:begin");
        int end =   code.indexOf("// nudge4j:end") + "// nudge4j:end".length();
        
        String snippet = code.substring(start,end);
        
        System.out.println("Writing "+fileOut.toAbsolutePath());
        Files.write(fileOut ,snippet.getBytes(Charset.forName("UTF-8")));
        
        
        // optional tasks (uncomment the ones you require)
        // runGeneratedSnippet(snippet); // wraps the generated script into a class and runs it.
        printTextareaHTML(snippet);   // prints the HTML snippet to copy paste into index.html
        
        System.out.println("All done...");
    }
    
    public static void printTextareaHTML(String snippet) {
        String html="<textarea id='taCode' readonly cols='100' rows='20' >";
        html+=(snippet.replace("<","&lt;").replace(">", "&gt;"));
        html+="</textarea>";
        System.out.println(html);;
    }
        
    public static void runGeneratedSnippet(String snippet) throws Exception {    
        System.out.println("Testing the script");
        System.out.println("Creating and Running a new class Test which embeds the script");
        HashMap<String,ByteArrayOutputStream> map = new HashMap<String,ByteArrayOutputStream>();
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        javac.getTask(null,
                new ForwardingJavaFileManager<JavaFileManager>(javac.getStandardFileManager(null, null, null)) {
                    @Override
                    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className,
                            JavaFileObject.Kind kind, FileObject sibling) {
                        return new SimpleJavaFileObject(URI.create(""), JavaFileObject.Kind.CLASS) {
                            @Override
                            public OutputStream openOutputStream() {
                                if (map.containsKey(className) == false) {
                                    map.put(className,new ByteArrayOutputStream());
                                }
                                return map.get(className);
                            }
                        };
                    }
                }, null, Arrays.asList(new String[] { "-g:none" }), null,
                Arrays.asList(new SimpleJavaFileObject(
                        URI.create("string:///Test.java"),
                        JavaFileObject.Kind.SOURCE) {
                    public CharSequence getCharContent(boolean b) {
                        return "public class Test { public static void test() { \n"+snippet+ "\n}}";
                    }
                })).call();

         
        new ClassLoader() {
            @Override
            public Class<?> findClass(String className) {
                byte array[] = map.get(className).toByteArray();
                return defineClass(className, array, 0, array.length); 
            }
        }.loadClass("Test").getMethods()[0].invoke(null);
        
        System.out.println("done");
    }


    
 

}    
