import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

/**
 * Compiles N4J.java and zips the class into a n4j.jar
**/
public class BuilderN4Jjar {
    public static void main(String args[]) throws Exception {
        System.out.println("BuilderN4Jjar");
        System.out.println("=============");
        System.out.println();
        
        File fileIn =  new File(System.getProperty("user.dir")+"/src/N4J.java"); 
        File fileOut = new File(System.getProperty("user.dir")+"/docs/dist/n4j.jar");

        System.out.println("Compiling " + fileIn);
        String code = load(fileIn);
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

        ByteArrayOutputStream baosJAR = new ByteArrayOutputStream();

        JarOutputStream jar = new JarOutputStream(baosJAR);
        JarEntry entry = new JarEntry("N4J.class");
        entry.setTime(fileIn.lastModified());
        jar.putNextEntry(entry);
        jar.write(baosN4Jclazz.toByteArray());
        jar.closeEntry();
        jar.close();

        FileOutputStream fos = new FileOutputStream(fileOut);
        fos.write(baosJAR.toByteArray());
        fos.close();

        System.out.println("N4J.class (size) " + baosN4Jclazz.toByteArray().length);
        System.out.println("N4J.jar   (size) " + baosJAR.toByteArray().length);
        System.out.println("File Written "+fileOut);
        System.out.println("done");
    }

    private static String load(File f) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            sb.append(line);
            sb.append('\n');
        }
        br.close();
        return sb.toString();
    }
}