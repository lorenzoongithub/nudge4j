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
 * Compiles N4J.java and generates the n4j.snippet
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
        
        System.out.println("compiled - bytecode:" + bytecode.length);
        
        String hexCode = toHEX(bytecode);
        
        String snippet =( 
                        "// Nudge4J - integration script 1.0.0 - START\n"+
                        "//\n"+
                        "try {\n"+
                        "    new ClassLoader() {\n"+
                        "        public Class<?> findClass(String c) {\n"+
                        "            String code = $$0;\n"+
                        "            byte array[] = new byte[$$1];\n"+
                        "            for (int i = 0; i < array.length; i++) {\n"+
                        "                array[i] = (byte) (\"0123456789ABCDEF\".indexOf(code.charAt(i * 2)) * 16\n"+
                        "                                 + \"0123456789ABCDEF\".indexOf(code.charAt(i * 2 + 1)));\n"+
                        "            }\n"+
                        "            return defineClass(\"N4J\", array, 0, array.length);\n"+
                        "        }\n"+
                        "    }.loadClass(\"N4J\").getMethods()[0].invoke(null, 5050, null);\n"+
                        "    System.out.println(\"N4J started successfully\");\n"+
                        "} catch (Exception e) {\n"+
                        "    throw new RuntimeException(e);\n"+
                        "}\n//END."+
                        "")       
                        .replace("$$0", hexCode)
                        .replace("$$1", "" + bytecode.length);
        
        System.out.println("Writing "+fileOut.toAbsolutePath());
        Files.write(fileOut ,snippet.getBytes(Charset.forName("UTF-8")));
        
        // To Do 
        // I need to write the html snippet as well.
        
        
        // testSnippet(snippet);
        
        System.out.println("All done...");
    }
        
        
    public static void testSnippet(String snippet) throws Exception {    
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
                        return "public class Test { public static void test() { "+snippet+ "}}";
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


    
    public static String toHEX(byte array[]) {
        String code = javax.xml.bind.DatatypeConverter.printHexBinary(array);
        String INDENT = "\"\n            + \"";
        StringBuilder sb = new StringBuilder("\"" + code.substring(0, 8) + INDENT); // CAFEBABE
        code = code.substring(8);
        for (int i = 0; i < code.length(); i++) {
            sb.append(code.charAt(i));
            if ((i + 1) % 80 == 0) { 
                sb.append(INDENT);// http://stackoverflow.com/questions/578059/studies-on-optimal-code-width
            }
        }
        return sb.toString() + "\"";
    }

}    
