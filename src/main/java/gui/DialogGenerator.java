package gui;

import gui.Models.Link;
import gui.Models.LinkStore;
import gui.Models.Node;
import gui.Models.NodeStore;
import gui.Models.Settings;

import java.util.ArrayList;

import static java.util.Objects.isNull;

import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class DialogGenerator {

    final Settings settings;
    NodeStore nodeStore;
    LinkStore linkStore;
    FSMControls rootControls;
    MenuController menu;

    public DialogGenerator(
            FSMControls rootControls,
            NodeStore nodeStore,
            LinkStore linkStore,
            Settings settings,
            MenuController menu) {
        this.rootControls = rootControls;
        this.settings = settings;
        this.nodeStore = nodeStore;
        this.linkStore = linkStore;
        this.menu = menu;
    }

    // STATE BUILDER helper functions
    public void createNewState(boolean i) {
        stateBuilder(i, null);
    }

    public void modifyState(StackPane sp) {
        stateBuilder(false, sp);
    }

    // 
    public void stateBuilder(boolean i, StackPane rootPane) {
        // Prepare a fresh node in case this is a new state
        gui.Models.Node activeNode = new gui.Models.Node();
        // If a state was given, find its node
        if (!isNull(rootPane))
            activeNode = nodeStore.findNodeFromText(((Text) rootPane.getChildren().get(1)).getText());

        Dialog<gui.Models.Node> dialog = new Dialog<>();
        dialog.setTitle("State Builder");
        dialog.setHeight(200);

        // Set the icon
        //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);
        final Button submitButton = (Button) dialog.getDialogPane().lookupButton(submitButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField text = new TextField();
        text.setText(activeNode.getText());
        text.requestFocus();

        ComboBox<String> acceptState = new ComboBox<>();
        acceptState.getItems().addAll("No", "Yes");

        // Check if a selection has been made (modify)
        if (activeNode.getAccept() || i == true)
            acceptState.setValue("Yes");
        else
            acceptState.setValue("No");

        grid.add(new Label("State name:"), 0, 0);
        grid.add(text, 1, 0);
        grid.add(new Label("Accept State?"), 0, 1);
        grid.add(acceptState, 1, 1);
        dialog.getDialogPane().setContent(grid);

        // Set focus on text
        Platform.runLater(() -> text.requestFocus());

        // Convert the result to the desired data structure
        dialog.setResultConverter(buttonType -> {
            if (buttonType == submitButtonType) {
                gui.Models.Node tempNode = new gui.Models.Node();
                tempNode.setAccept(acceptState.getValue().equals("Yes"));
                tempNode.setText(text.getText());
                return tempNode;
            }
            return null;
        });

        submitButton.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    if (text.getText().isEmpty() || !nodeStore.nodeExists(text.getText())) {
                        errorAlert("Invalid state name", "State name must be unique.");
                        event.consume();
                    }
                }
        );

        final Optional<gui.Models.Node> result = dialog.showAndWait();

        if (result.isPresent())
            this.rootControls.createState(((gui.Models.Node) result.get()).getText(), ((gui.Models.Node) result.get()).getAccept());

    }

    // Create a new link
    public void createNewLink() {
        ArrayList<String> existingNodes = this.nodeStore.getNodes();
        // Ensure atleast one state exists
        if (existingNodes.size() <= 0) {
            errorAlert("No existing state nodes", "Creating a link requires atleast one existing state node.");
            return;
        }
        Dialog<Link> dialog = new Dialog<>();
        dialog.setTitle("Create new link");

        // Set the icon
        //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);
        final Button submitButton = (Button) dialog.getDialogPane().lookupButton(submitButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField text = new TextField();
        text.setPromptText("Text");

        ComboBox<String> linkStart = new ComboBox<>();
        ComboBox<String> linkEnd = new ComboBox<>();
        // Populate links
        existingNodes.forEach((x) -> linkStart.getItems().add(x));
        linkStart.setValue(existingNodes.iterator().next());
        existingNodes.forEach((x) -> linkEnd.getItems().add(x));
        linkEnd.setValue(existingNodes.iterator().next());

        grid.add(new Label("Text:"), 0, 0);
        grid.add(text, 1, 0);
        grid.add(new Label("Start:"), 0, 1);
        grid.add(linkStart, 1, 1);
        grid.add(new Label("End:"), 0, 2);
        grid.add(linkEnd, 1, 2);

        dialog.getDialogPane().setContent(grid);
        // Set focus on text input
        Platform.runLater(() -> text.requestFocus());

        // Convert the result to the desired data structure
        dialog.setResultConverter(buttonType -> {
            if (buttonType == submitButtonType) {

                // Locate the associated node
                Node startNode = nodeStore.findNodeFromText(linkStart.getValue());
                Node endNode = nodeStore.findNodeFromText(linkEnd.getValue());

                Link link = new Link();
                link.setStart(startNode);
                link.setEnd(endNode);
                link.setText(text.getText());

                return link;

            }
            return null;
        });

        submitButton.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    if (text.getText().isEmpty() || !linkStore.linkExists(linkStart.getValue(), linkEnd.getValue())) {
                        errorAlert("Invalid link", "This link already exists.");
                        event.consume();
                    }
                }
        );

        final Optional<Link> result = dialog.showAndWait();

        if (result.isPresent()) {
            Node startNode = nodeStore.findNodeFromText(linkStart.getValue());
            Node endNode = nodeStore.findNodeFromText(linkEnd.getValue());

            this.rootControls.linkGenerator(startNode, endNode, text.getText());
        }

    }

    // TODO Create new functions
    /*
    // Create a new function
    public void createNewFunction() {

        Dialog<Link> dialog = new Dialog<>();
        dialog.setTitle("Create new function");

        // Set the icon
        //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);
        final Button submitButton = (Button) dialog.getDialogPane().lookupButton(submitButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField text = new TextField();
        text.setPromptText("Text");
        ComboBox<String> linkStart = new ComboBox<>();
        // Populate links
        this.rootControls.nodes.forEach((x) -> linkStart.getItems().add(x.getText()));
        linkStart.setValue(this.rootControls.nodes.iterator().next().getText());
        // Populate links
        ComboBox<String> linkEnd = new ComboBox<>();
            this.rootControls.nodes.forEach((x) -> linkStart.getItems().add(x.getText()));
        linkStart.setValue(this.rootControls.nodes.iterator().next().getText());

        grid.add(new Label("Text:"), 0, 0);
        grid.add(text, 1, 0);
        grid.add(new Label("Start:"), 0, 1);
        grid.add(linkStart, 1, 1);
        grid.add(new Label("End:"), 0, 2);
        grid.add(linkEnd, 1, 2);

        dialog.getDialogPane().setContent(grid);
        
        // Set focus on text input
        Platform.runLater(() -> text.requestFocus());

        // Convert the result to the desired data structure
        dialog.setResultConverter(buttonType -> {
            if (buttonType == submitButtonType) {
                
                // Locate the associated node
                Node startNode = this.rootControls.findNodeFromText(linkStart.getValue());
                Node endNode = this.rootControls.findNodeFromText(linkEnd.getValue());
                
                Link link = new Link();
                link.setStart(startNode);
                link.setEnd(endNode);
                link.setText(text.getText());
                
                return link;
                
            }
            return null;
        });
        
        submitButton.addEventFilter(
            ActionEvent.ACTION, 
            event -> {
                if (text.getText().isEmpty() || !this.rootControls.verifyLink(linkStart.getValue(), linkEnd.getValue(), text.getText())) {
                    dialog.setHeaderText("A unique, valid state name is required. Please choose another name.");
                    // Prevent dialog closing - error
                    event.consume();
                }
            }
        );
        
        final Optional<Link> result = dialog.showAndWait();
        
        if (result.isPresent()) {
            Node startNode = this.rootControls.findNodeFromText(linkStart.getValue());
            Node endNode = this.rootControls.findNodeFromText(linkEnd.getValue());
            this.rootControls.submitLink(startNode, endNode, text.getText());
        }
            
    }
    */

    /******** MODIFICATION DIALOGS ********/

    // Modify an existing state
    public void modifyStates(StackPane rootPane) {
        // Obtain existing state data
        gui.Models.Node activeNode = nodeStore.findNodeFromText(((Text) rootPane.getChildren().get(1)).toString());

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Modify existing state");
        dialog.setContentText("State name:");

        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        final TextField inputField = dialog.getEditor();

        inputField.setText(activeNode.getText());

        okButton.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    if (inputField.getText().isEmpty() || !nodeStore.nodeExists(inputField.getText())) {
                        errorAlert("Invalid state name", "State name must be unique.");
                        event.consume();
                    }
                }
        );
        final Optional<String> result = dialog.showAndWait();

        if (result.isPresent())
            this.rootControls.createState(result.get(), false);
    }

    /**
     * MENU DIALOGS
     **/

    public void exportCode() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);
        Button confirm = (Button) dialog.getDialogPane().lookupButton(submitButtonType);

        dialog.setTitle("Export Code");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> language = new ComboBox<>();
        // Populate links
        language.getItems().add("Python");
        // Default value
        language.setValue(language.getItems().get(0));

        final ToggleGroup type = new ToggleGroup();
        RadioButton sourceOnly = new RadioButton("Source code only");
        sourceOnly.setToggleGroup(type);
        sourceOnly.setSelected(true);
        RadioButton rawOnly = new RadioButton("Raw FSM code only");
        rawOnly.setToggleGroup(type);
        RadioButton both = new RadioButton("Source and FSM code");
        both.setToggleGroup(type);

        grid.add(new Label("Language:"), 0, 0);
        grid.add(language, 1, 0);
        grid.add(new Label("Export type:"), 0, 1);
        grid.add(sourceOnly, 0, 2);
        grid.add(rawOnly, 1, 2);
        grid.add(both, 2, 2);

        dialog.getDialogPane().setContent(grid);
        // Set focus on text input
        Platform.runLater(() -> confirm.requestFocus());

        // Convert the result to the desired data structure
        dialog.setResultConverter(buttonType -> {
            if (buttonType == submitButtonType) {
                return (new Pair<>(language.getValue(),
                        String.valueOf(type.getToggles().indexOf(type.getSelectedToggle()))));

            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(options -> menu.generateFSM("Python"));
    }

    /**
     * ERROR NOTICE
     **/
    public void errorAlert(String type, String error) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(type);
        alert.setContentText(error);
        alert.setResizable(false);
        alert.showAndWait();
    }

}
