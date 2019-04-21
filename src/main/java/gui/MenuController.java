
package gui;

import gui.Models.LinkStore;
import gui.Models.NodeStore;
import gui.Models.Settings;
import java.io.File;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MenuController {
    
    final FSMControls rootControls;
    final NodeStore nodeStore;
    final LinkStore linkStore;
    final Settings settings;
    final Stage rootStage;
    Loader loader;
    DialogGenerator dg;
    
    public MenuController ( Stage rootStage,
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
    // Load code designer project
    public void loadProject() { 
        XMLParser x = new XMLParser(rootStage, rootControls, linkStore, nodeStore, settings);
        File file = x.chooseFile();
        if (file != null)
            x.initParser(file);
        else
            System.out.println("File Error"); // TODO add real error detection
        
        saveProject();
        
    }
    // Save code designer project
    public void saveProject() { 
        XMLGenerator x = new XMLGenerator(rootStage, rootControls, linkStore, nodeStore, settings); 
        x.generateXML();
    }
    // Generate .fsm file
    public void generateFSM(String type) {
        System.out.println("Launching FSM Generator");
        FSMGenerator cg = new FSMGenerator(rootStage, rootControls, linkStore, nodeStore, settings);
        cg.beginProcessing(type);
    }
    
    public void newProject() {
        Scene s = loader.generateScene(loader.generateProjectPane());
        loader.setScene(s);
    }
    
    public void closeProject() {
        
    }
    
    // Open dialog
    public void editSettings() {
        
    }
    
    // TODO Add functionality to each menu item
    public void createContextMenu(StackPane sp) {
        DialogGenerator dialogGenerator = this.dg;
        ContextMenu menu = new ContextMenu();
        
        MenuItem editNode = new MenuItem("Edit Node");
        MenuItem deleteNode = new MenuItem("Delete Node");
        MenuItem editLink = new MenuItem("Edit Link");
        
        editNode.setOnAction((ActionEvent e) -> { dialogGenerator.modifyState(sp); });
        
        deleteNode.setOnAction((ActionEvent e) -> { //dialogGenerator.deleteState(sp);
        });
        
        editLink.setOnAction((ActionEvent e) -> { //dialogGenerator.modifyLink(sp);
        });
        
        menu.getItems().addAll(editNode, deleteNode, editLink);
        
        // Assign this menu to provided sp
        sp.setOnContextMenuRequested((ContextMenuEvent event) -> {
            menu.show(sp, event.getScreenX(), event.getScreenY());
        });
        
        // Move menu with node if dragged while open
        sp.layoutXProperty().addListener((ObservableValue<? extends Number> arg0, Number oldX, Number newX) -> {
            double delta = oldX.doubleValue() - newX.doubleValue();
            if (menu.isShowing()) menu.setX(menu.getX() - delta);
        });
        
        sp.layoutYProperty().addListener((ObservableValue<? extends Number> arg0, Number oldY, Number newY) -> {
            double delta = oldY.doubleValue() - newY.doubleValue();
            if (menu.isShowing()) menu.setY(menu.getY() - delta);
        });
    }
    
    public void createCanvasContextMenu(Canvas c) {
        ContextMenu cMenu = new ContextMenu();
        MenuItem newNode = new MenuItem("Create Node");
        MenuItem newLink = new MenuItem("Create Link");
        
        newNode.setOnAction((ActionEvent e) -> { dg.createNewState(false); });
        newLink.setOnAction((ActionEvent e) -> { dg.createNewLink(); });
        cMenu.getItems().addAll(newNode, newLink);
        // Assign this menu to provided sp
        c.setOnContextMenuRequested((ContextMenuEvent event) -> {
            cMenu.show(c, event.getScreenX(), event.getScreenY());
        });
    }
    
    public void setDialogGenerator(DialogGenerator dg) { this.dg = dg; }
    public void setLoader(Loader l) { this.loader = l; }
}
