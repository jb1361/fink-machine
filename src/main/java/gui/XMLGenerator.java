package gui;

import gui.Models.Link;
import gui.Models.LinkStore;
import gui.Models.Node;
import gui.Models.NodePair;
import gui.Models.NodeStore;
import gui.Models.Settings;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class XMLGenerator {
    FSMControls rootControls;
    Settings settings;
    LinkStore linkStore;
    NodeStore nodeStore;
    Stage rootStage;
    
    // Convenience
    String sp = " ";
    String tab = "\t";
    String dTab = "\t\t";
    String tTab = "\t\t\t";
    String newLine = System.getProperty("line.separator");
    
    
    public XMLGenerator(Stage rootStage,
                        FSMControls rootControls,
                        LinkStore linkStore,
                        NodeStore nodeStore,
                        Settings settings) {
        this.rootControls = rootControls;
        this.linkStore = linkStore;
        this.nodeStore = nodeStore;
        this.settings = settings;
    }
    
    public void generateXML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<FSM>").append(newLine);
            sb.append(processNodes());
            sb.append(processLinks());
            //sb.append(processFunctions());
        sb.append("</FSM>");
        
        chooseFile(sb.toString());
    }
    
    public String processNodes() {
        StringBuilder sb = new StringBuilder();
        HashMap<String, Node> nodeMap = nodeStore.getList();
        
        nodeMap.forEach((s, n) -> {
            sb.append(tab).append("<NODE name='").append(n.getText()).append("' accept='").append(n.getAccept()).append("'>").append(newLine);
                sb.append(dTab).append("<ANCHOR>").append(newLine);
                    sb.append(tTab).append("<X> ").append(n.getAnchor().getX()).append(" </X>").append(newLine);
                    sb.append(tTab).append("<Y> ").append(n.getAnchor().getX()).append(" </Y>").append(newLine);
                sb.append(dTab).append("</ANCHOR>").append(newLine);
            sb.append(tab).append("</NODE>").append(newLine);
        });
        
        return sb.toString();
    }
    
    public String processLinks() {
        StringBuilder sb = new StringBuilder();
        HashMap<NodePair, Link> linkMap = linkStore.getList();
        
        linkMap.forEach((p, l) -> {
            sb.append(tab).append("<LINK name='").append(l.getText())
              .append("' start='").append(l.getStart().getText())
              .append("' end='").append(l.getEnd().getText()).append("'>").append(newLine);
                sb.append(dTab).append("<ANCHOR>").append(newLine);
                    sb.append(tTab).append("<START>").append(newLine);
                        sb.append(tab).append(tTab).append("<X> ").append(l.getStartAnchor().getX()).append(" </X>").append(newLine);
                        sb.append(tab).append(tTab).append("<Y> ").append(l.getStartAnchor().getY()).append(" </Y>").append(newLine);
                    sb.append(tTab).append("</START>").append(newLine);
                    sb.append(tTab).append("<END>").append(newLine);
                        sb.append(tab).append(tTab).append("<X> ").append(l.getEndAnchor().getX()).append(" </X>").append(newLine);
                        sb.append(tab).append(tTab).append("<Y> ").append(l.getEndAnchor().getY()).append(" </Y>").append(newLine);
                    sb.append(tTab).append("</END>").append(newLine);
                    sb.append(tTab).append("<ARC>").append(newLine);
                        sb.append(tab).append(tTab).append("<X> ").append(l.getArcAnchor().getX()).append(" </X>").append(newLine);
                        sb.append(tab).append(tTab).append("<Y> ").append(l.getArcAnchor().getY()).append(" </Y>").append(newLine);
                    sb.append(tTab).append("</ARC>").append(newLine);
                sb.append(dTab).append("</ANCHOR>").append(newLine);
                sb.append(dTab).append("<ANGLE>").append(newLine);
                    sb.append(tTab).append("<PARALLEL> ").append(l.getParallelAngle()).append(" </PARALLEL>").append(newLine);
                    sb.append(tTab).append("<PERPENDICULAR> ").append(l.getPerpendicularAngle()).append(" </PERPENDICULAR>").append(newLine);
                    sb.append(tTab).append("<ARC> ").append(l.getArcAngle()).append(" </ARC>").append(newLine);
                sb.append(dTab).append("</ANGLE>").append(newLine);
            sb.append(tab).append("</LINK>").append(newLine);
        });
        
        return sb.toString();
    }
    
    public void processFunctions() {
        StringBuilder sb = new StringBuilder();
        
    }
    
        
    public void chooseFile(String source) {
        // Choose where to save file
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home"))
        ); 
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FSM Design File", ".fxml"));
        fileChooser.setInitialFileName("*.fxml");
        // Select file
        final File file = fileChooser.showSaveDialog(rootStage);
        /// Overwrite existing file
        if (file != null) { createFile(file, source); }
    }
    
    // Create XML file
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
    
    
    
}
