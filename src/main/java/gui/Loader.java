package gui;

import gui.Models.LinkStore;
import gui.Models.NodeStore;
import gui.Models.Settings;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * TODO
 * ** Generate XML
 * ** Generate FSM
 * ** Upload changes & download Data side
 * ** Implement export as Python option
 * ** Implement command line program launcher
 **/

public class Loader extends Application {

    // Not fully implemented
    Settings settings;
    FSMControls rootControls;
    DialogGenerator dialogGenerator;
    NodeStore nodeStore;
    LinkStore linkStore;
    Stage rootStage;
    MenuController menu;
    CanvasPane visualEditor;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.rootStage = primaryStage;
        this.settings = new Settings();
        this.nodeStore = new NodeStore();
        this.linkStore = new LinkStore();

        this.visualEditor = new CanvasPane(this.nodeStore, this.linkStore, this.settings);
        this.rootControls = new FSMControls(rootStage, nodeStore, linkStore, settings);
        this.menu = new MenuController(rootStage, rootControls, linkStore, nodeStore, settings);
        this.dialogGenerator = new DialogGenerator(this.rootControls, this.nodeStore, this.linkStore, this.settings, this.menu);

        this.rootControls.setCanvasPane(visualEditor);
        this.visualEditor.setRootControls(this.rootControls);
        this.visualEditor.setMenu(this.menu);
        this.visualEditor.initCanvas();
        this.menu.setLoader(this);
        this.menu.setDialogGenerator(dialogGenerator);

        BorderPane primaryPane = generateLaunchPane();
        Scene primaryScene = new Scene(primaryPane);

        primaryStage.setTitle("FSM Builder");
        primaryStage.setScene(primaryScene);
        primaryStage.setMaximized(true);
        //primaryStage.setResizable(false);
        primaryStage.show();

        // DEBUG-ONLY
        //rootControls.createState("a", false);
        //rootControls.createState("b", false);
    }

    public Scene generateScene(BorderPane pane) {
        return (new Scene(pane, rootStage.getScene().getWidth(), rootStage.getScene().getHeight()));
    }

    public void setScene(Scene s) {
        this.rootStage.setScene(s);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public GridPane setupPrimaryControls() {
        GridPane primaryControlPane = new GridPane();
        return primaryControlPane;
    }

    // Base pane for application
    public BorderPane generateLaunchPane() {
        BorderPane root = new BorderPane();
        // Menu
        root.setTop(generateMenuBar());
        // Canvas
        root.setCenter(generateLauncher());

        return root;
    }

    public BorderPane generateProjectPane() {
        BorderPane root = new BorderPane();
        // Menu
        root.setTop(generateMenuBar());
        // Creator tools
        root.setLeft(generateFSMControls());
        // Canvas
        root.setCenter(this.visualEditor);

        // DEBUG-ONLY
        //rootControls.createState("a", false);
        //rootControls.createState("b", false);

        return root;
    }

    public MenuBar generateMenuBar() {
        MenuBar primaryMenuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New project");
        newItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        newItem.setOnAction((ActionEvent e) -> {
        }); //dialogGenerator.newProject(); }; );
        MenuItem loadItem = new MenuItem("Load project");
        loadItem.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
        loadItem.setOnAction((ActionEvent e) -> {
            menu.loadProject();
        });
        MenuItem saveItem = new MenuItem("Save project");
        saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        saveItem.setOnAction((ActionEvent e) -> {
            menu.saveProject();
        });
        MenuItem closeItem = new MenuItem("Close project");
        closeItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        closeItem.setOnAction((ActionEvent e) -> {
        }); //dialogGenerator.closeProject(); }; );

        Menu exportMenu = new Menu("Export");
        MenuItem imageItem = new MenuItem("Generate visual FSM");
        imageItem.setAccelerator(KeyCombination.keyCombination("Alt+I"));
        imageItem.setOnAction((ActionEvent e) -> {
        }); //dialogGenerator.exportImage(); }; );
        MenuItem fsmItem = new MenuItem("Generate FSM file");
        fsmItem.setAccelerator(KeyCombination.keyCombination("Alt+V"));
        fsmItem.setOnAction((ActionEvent e) -> {
            menu.generateFSM("Python");
        });
        MenuItem codeItem = new MenuItem("Convert to code");
        codeItem.setAccelerator(KeyCombination.keyCombination("Alt+V"));
        codeItem.setOnAction((ActionEvent e) -> {
        }); //menu.generateFSM("Python"); }); // TODO allow alternative languages

        Menu toolsMenu = new Menu("Settings");
        toolsMenu.setOnAction((ActionEvent e) -> {
            menu.editSettings();
        });

        exportMenu.getItems().addAll(imageItem, fsmItem, codeItem);
        fileMenu.getItems().addAll(newItem, saveItem, loadItem, closeItem, exportMenu);
        primaryMenuBar.getMenus().addAll(fileMenu, toolsMenu);

        return primaryMenuBar;
    }

    public VBox generateFSMControls() {
        VBox root = new VBox();

        Button newState = new Button("Add State");
        newState.setOnAction((ActionEvent e) -> {
            dialogGenerator.createNewState(false);
        });
        /*
        Button addFunction = new Button("Add Function");
        addFunction.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                dialogGenerator.createNewFunction();
            }
        });
        */
        Button link = new Button("Add Link");
        link.setOnAction((ActionEvent e) -> {
            dialogGenerator.createNewLink();
        });

        //root.getChildren().addAll(newState, acceptState, link, addFunction);
        root.getChildren().addAll(newState, link);
        root.setSpacing(50);
        root.setAlignment(Pos.CENTER);

        root.setStyle("-fx-padding: 0 10 0 10;" +
                "-fx-border-style: solid;" +
                "-fx-border-width: 0 2 0 0;" +
                "-fx-border-right-color: black;");
        return root;
    }

    public VBox generateLauncher() {
        VBox root = new VBox();

        Button newProject = new Button("New Project");
        newProject.setOnAction((ActionEvent e) -> {
            menu.newProject();
        });

        Button loadProject = new Button("Import Project");
        loadProject.setOnAction((ActionEvent e) -> {
            menu.loadProject();
        });
        
        /*
        Button addFunction = new Button("Add Function");
        addFunction.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                dialogGenerator.createNewFunction();
            }
        });
        */
        root.getChildren().addAll(newProject, loadProject);
        root.setSpacing(50);
        root.setAlignment(Pos.CENTER);
        return root;
    }


}