package nudge4j;

/**
 * N4J: A class which wraps the nudge4j snippet of code to copy/paste into any java 8 program.
**/
public class N4J { static {
    // nudge4j - bootstrap
    try {
      new javax.script.ScriptEngineManager()
        .getEngineByName("JavaScript")
        .eval("load('https://lorenzoongithub.github.io/nudge4j/twigs/n4j.boot.js')");
    } catch (javax.script.ScriptException e) {
      throw new RuntimeException(e);
    }    
}

public static void main(String args[]) { System.out.println("kicking off nudge4j."); }
    
}