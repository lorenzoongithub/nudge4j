// nudge4j - bootstrap
try {
  new javax.script.ScriptEngineManager()
    .getEngineByName("JavaScript")
    .eval("load('https://lorenzoongithub.github.io/nudge4j/twigs/n4j.boot.js')");
} catch (javax.script.ScriptException e) {
  throw new RuntimeException(e);
}