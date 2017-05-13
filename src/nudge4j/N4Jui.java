package nudge4j;

/**
 * N4Jui is a class which wraps a snippet of code to copy/paste into any java 8 program.
 * Since it makes no assumptions on the destination's code:
 *  
 * - Classes are fully qualified. 
 * - The snippet is wrapped inside a Consumer function to avoid variable scope conflicts.    
 * 
 * N4Jui is a class which launches a Swing JFrame with two text areas.
 * 
 * The top area (input) is for writing JavaScript (Nashorn) to get executed by the JVM.
 * The bottom area (output) is for displaying the outcome of the execution.
 * 
 * N4Jui is a light alternative (serverless) to the original snippet provided in N4J.java 
 * 
**/
public class N4Jui { static {
    
// nudge4j-UI:begin    
(new java.util.function.Consumer<Object[]>() { public void accept(Object args[]) {
    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
            try { javax.swing.UIManager.setLookAndFeel(info.getClassName());
            } catch (Exception e) { ; }
            break;
        }
    }
    javax.script.ScriptEngine engine = new javax.script.ScriptEngineManager().getEngineByName("JavaScript");
    engine.put("args", args);
    javax.swing.JTextArea ta = new javax.swing.JTextArea( String.join("\n",
        "//",
        "// Write code to execute by the JVM here.",
        "// Here are some examples: http://.../",
        "//",
        "java.lang.Thread.activeCount();"));
    ta.setFont(new java.awt.Font("monospaced", 0, 12));
    javax.swing.JTextArea taOut = new javax.swing.JTextArea();
    taOut.setBackground(java.awt.Color.LIGHT_GRAY);
    taOut.setFont(ta.getFont());
    javax.swing.JButton btn = new javax.swing.JButton("Execute Snippet");
    btn.setCursor(new java.awt.Cursor(12));
    btn.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent ae) {
            String response; 
            taOut.setForeground(java.awt.Color.BLACK);
            try {
                response = "" + engine.eval(ta.getText());
            } catch (Exception e) {
                taOut.setForeground(java.awt.Color.RED);
                java.io.OutputStream os = new java.io.ByteArrayOutputStream();
                e.printStackTrace(new java.io.PrintStream(os));
                response = ""+os;
            } 
            taOut.setText(response);
        }
    }); 
    javax.swing.JPanel p = new javax.swing.JPanel(new java.awt.BorderLayout());
    p.add(new javax.swing.JScrollPane(ta));
    p.add(btn, java.awt.BorderLayout.SOUTH);
    javax.swing.JSplitPane split = new javax.swing.JSplitPane(0,
        p, new javax.swing.JScrollPane(taOut));
    split.setResizeWeight(.5d);
    javax.swing.JFrame f = new javax.swing.JFrame("nudge4j UI");
    f.setDefaultCloseOperation(2);
    f.add(split); 
    f.setSize(720, 500);
    f.setLocationRelativeTo(null);
    f.setVisible(true);
}}).accept( new Object[] { /* any arguments here */ });
// nudge4j-UI:end
}}
