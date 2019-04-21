
package gui;

import gui.Models.Anchor;
import gui.Models.LinkStore;
import gui.Models.NodeStore;
import gui.Models.Settings;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {
    FSMControls rootControls;
    Settings settings;
    LinkStore linkStore;
    NodeStore nodeStore;
    Stage rootStage;

    public XMLParser(Stage rootStage,
                     FSMControls rootControls,
                     LinkStore linkStore,
                     NodeStore nodeStore,
                     Settings settings) {
        this.rootControls = rootControls;
        this.linkStore = linkStore;
        this.nodeStore = nodeStore;
        this.settings = settings;
    }

    public File chooseFile() {
        // Choose where to save file
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Finite State Machine design file", ".XML"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("FSM Design File", "*.fxml"),
                new FileChooser.ExtensionFilter("XML file", "*.xml"),
                new FileChooser.ExtensionFilter("Text File", "*.txt"));

        // Select file
        final File file = fileChooser.showOpenDialog(rootStage);
        /// Overwrite existing file
        //if (file != null) { readFile(file); }
        return file;
    }

    public void initParser(File file) {
        Document doc = readFile(file);
        NodeList stateNodes = doc.getElementsByTagName("NODE");
        //NodeList linkNodes = doc.getElementsByTagName("LINK");
        //NodeList functionNodes = doc.getElementsByTagName("FUNCTION");
        parseStates(stateNodes);
        //parseLinks(linkNodes);
        //parseFunctions
        System.out.println("Done");
    }

    // Read XML file
    public Document readFile(File file) {
        /*
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int i = -1;
            while (( i = br.read()) != -1) {  
              System.out.print((char)i);  
            }  
        } catch (IOException e) { e.printStackTrace(); }
        */

        Document doc = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(file);
            //if (!doc || !doc.hasChildNodes()) {  }
            doc.getDocumentElement().normalize();


        } catch (Exception e) {
        }

        return doc;
    }

    public void parseStates(NodeList stateNodes) {
        for (int i = 0; i < stateNodes.getLength(); i++) {
            Node curr = stateNodes.item(i);
            gui.Models.Node newNode = new gui.Models.Node();
            // Verify element found
            if (curr.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) curr;
                newNode.setAccept(e.getAttribute("accept").equals("true"));
                newNode.setText(e.getAttribute("name"));
                Node a = ((Node) e.getElementsByTagName("ANCHOR").item(0));
                Element anchor = (Element) a.getChildNodes();
                if (anchor.getNodeType() == Node.ELEMENT_NODE) {
                    double x = Double.parseDouble(((Element) anchor.getElementsByTagName("X").item(0)).getTextContent());
                    double y = Double.parseDouble(((Element) anchor.getElementsByTagName("Y").item(0)).getTextContent());
                    newNode.setAnchor(new Anchor(x, y));
                }
                if (!newNode.getText().isEmpty())
                    nodeStore.addNode(newNode);
            }
        }

    }

}
