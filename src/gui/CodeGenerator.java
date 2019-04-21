package gui;

import gui.Models.LinkStore;
import gui.Models.NodeStore;
import gui.Models.Settings;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/** TODO
 *** Add FunctionStore
 *** Check for commence & conclude functions
 */
public final class CodeGenerator {
    final FSMControls rootControls;
    final LinkStore linkStore;
    final NodeStore nodeStore;
    final Settings settings;
    final Stage rootStage;
    
    String newLine = System.getProperty("line.separator");
    String sp = " ";
    String t = "\t";
    
    public CodeGenerator(Stage rootStage,
                         FSMControls rootControls, 
                         LinkStore linkStore, 
                         NodeStore nodeStore, 
                         Settings settings) {
        this.rootControls = rootControls;
        this.linkStore = linkStore;
        this.nodeStore = nodeStore;
        this.settings = settings;
        this.rootStage = rootStage;
    }
    
    void beginProcessing(String type) {
        System.out.println("Beginning Processing");
        StringBuilder header = traceHeaderNodes();
        StringBuilder transitions = traceTransitionNodes();
        //StringBuilder functions = traceFunctions();
        // Combine StringBuilders
        StringBuilder output = new StringBuilder();
        output.append(header.toString());
        output.append(transitions.toString());
        //output.append(functions.toString());
        // Initiate save
        chooseFile(output.toString(), true);
    }
    
    public void chooseFile(String source, boolean save) {
        if (save) {
            // Choose where to save file
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save file");
            fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
            ); 
            fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Finite State Machine file", ".fsm"));
            // Select file
            final File file = fileChooser.showSaveDialog(rootStage);
            /// Overwrite existing file
            if (file != null) { createFile(file, source); }
        } else {
            try {
                final File file = File.createTempFile("FSM", ".fsm");
                if (file.canWrite()) 
                    createFile(file, source);
                else 
                    System.out.println("Error: Can not write to file.");
                
            } catch(IOException e) { e.printStackTrace(); }
        }
    }
    
    // Create FSM file
    public void createFile(File file, String source) {
        
        try (BufferedWriter bw = new BufferedWriter(new PrintWriter(file))) {
            bw.write(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int i = -1;
            while (( i = br.read()) != -1) {  
              System.out.print((char)i);  
            }  
        } catch (IOException e) { e.printStackTrace(); }
    }
    
    // TODO Add module name ability
    // Create top of file
    public StringBuilder traceHeaderNodes() {
        System.out.println("Tracing header nodes");
        StringBuilder sb = new StringBuilder();   
        // Start file with module name
        sb.append("FSM")
          .append(newLine);
        // Find Accept State nodes
        ArrayList<String> acceptNodes = nodeStore.printAccept();
        sb.append(t)
          .append("accept {");
            if (acceptNodes.size() > 0) {
                acceptNodes.forEach(a -> {
                    sb.append(a)
                      .append(sp);
                });
                // Remove extra space in set
                sb.deleteCharAt(sb.length() - 1);
            }
        sb.append("}")
          .append(newLine);
        
        return sb;
    }
    
    // Create transitions
    public StringBuilder traceTransitionNodes() {
        System.out.println("Tracing transition nodes");
        StringBuilder sb = new StringBuilder();
        sb.append("TRANS").append(newLine);
        
        ArrayList<String> linkNodes = linkStore.printNodes();
        linkNodes.forEach(s -> { 
            sb.append(s);
        });
        
        return sb;
    }
    
    // Dump functions
    // TODO Search for commence function
    public StringBuilder traceFunctionNodes() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("CODE")
          .append(newLine);
        /*
        ArrayList<String> fxNodes = functionStore.printNodes();
        fxNodes.forEach(f -> { 
            sb.append("def ")
              .append(f)
              .append(newLine);
        });
        */
        
        return sb;
    }
    
    
        
        
        
        
        
        
    // Launch backend
    /*
        public int run(String clazz) throws IOException, InterruptedException {        
            ProcessBuilder pb = new ProcessBuilder("java", clazz);
            pb.redirectError();
            pb.directory(new File("src"));
            Process p = pb.start();
            InputStreamConsumer consumer = new InputStreamConsumer(p.getInputStream());
            consumer.start();

            int result = p.waitFor();

            consumer.join();

            System.out.println(consumer.getOutput());

            return result;
        }
    */
}
