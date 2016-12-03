import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.zip.DeflaterOutputStream;

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
        Path fileIn =  Paths.get("src/N4J.java");
        Path fileOut = Paths.get("docs/dist/n4j.snippet.java");
        System.out.println("Compiling " + fileIn.toAbsolutePath());
        
        String code = new String(Files.readAllBytes(fileIn),"UTF-8");
        ByteArrayOutputStream baosN4Jclazz = new ByteArrayOutputStream();
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        javac.getTask(null,
                new ForwardingJavaFileManager<JavaFileManager>(javac.getStandardFileManager(null, null, null)) {
                    @Override
                    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className,
                            JavaFileObject.Kind kind, FileObject sibling) {
                        return new SimpleJavaFileObject(URI.create(""), JavaFileObject.Kind.CLASS) {
                            @Override
                            public OutputStream openOutputStream() {
                                return baosN4Jclazz;
                            }
                        };
                    }
                }, null, Arrays.asList(new String[] { "-g:none" }), null,
                Arrays.asList(new SimpleJavaFileObject(
                        URI.create("string:///N4J.java"),
                        JavaFileObject.Kind.SOURCE) {
                    public CharSequence getCharContent(boolean b) {
                        return code;
                    }
                })).call();
        byte bytecode[] = baosN4Jclazz.toByteArray();
        System.out.println("Compiled - bytecode (size):" + bytecode.length);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream out = new DeflaterOutputStream(baos);
        out.write(bytecode);
        out.close();
        byte zipBytecode[] = baos.toByteArray();
        System.out.println("Zipped - compressed bytecode (size):" + zipBytecode.length);
        
        String base64 = Base64.getEncoder().encodeToString(zipBytecode);
        System.out.println("Base64 - string (size):" + base64.length());
        
        final String INDENT = "            ";
        final String DQUOTE = "\"";
        String code64 = "";
        for (int i=0;i<base64.length();i+=80) {
            if (i+80<=base64.length()) {
                code64+=INDENT+DQUOTE+base64.substring(i,i+80)+DQUOTE+" +\n";
            } else {
                code64+=INDENT+DQUOTE+base64.substring(i)+DQUOTE;
            }
        }
        
        String snippet =(
                "// nudge4j:begin\n"+
                "try {\n"+
                "    new ClassLoader() {\n"+
                "        public Class<?> findClass(String c) {\n"+
                "            String code = \n"+
                "$$0;\n"+
                "            java.io.InputStream iis = new java.util.zip.InflaterInputStream(\n"+
                "                new java.io.ByteArrayInputStream(java.util.Base64.getDecoder().decode(code)));\n"+
                "            try {\n"+
                "                byte array[] = new byte[$$1];\n"+
                "                for (int i=0;i<array.length;i++) array[i] = (byte) iis.read();\n"+
                "                return defineClass(\"N4J\", array, 0, array.length);\n"+
                "            } catch (Exception e) {\n"+
                "                throw new RuntimeException(e);\n"+
                "            }\n"+
                "        }\n"+
                "   }.loadClass(\"N4J\").getMethods()[0].invoke(null, 5050, null);\n"+
                "} catch (Exception e) {\n"+
                "    throw new RuntimeException(e);\n"+
                "}\n"+
                "// nudge4j:end")
                        .replace("$$0", code64)
                        .replace("$$1", "" + bytecode.length);
        
        System.out.println("Writing "+fileOut.toAbsolutePath());
        Files.write(fileOut ,snippet.getBytes(Charset.forName("UTF-8")));
        
        
        // optional tasks (uncomment the ones you require)
        //runGeneratedSnippet(snippet); // wraps the generated script into a class and runs it.
        //printTextareaHTML(snippet);   // prints the HTML snippet to copy paste into index.html
        
        //System.out.println("All done...");
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
