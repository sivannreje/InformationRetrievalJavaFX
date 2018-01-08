package partA;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.util.Map;
import java.util.Observable;
import java.util.Optional;

public class GUI extends Application {
    private String dataSetPath;
    private String locationPath;
    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //list View for educational qualification
//


        controller = new Controller();
        //Creating containers
        GridPane mainGridPane = new GridPane();
        GridPane gridPaneforButtons = new GridPane();
        VBox vBox = new VBox();
        HBox hBox = new HBox();



        //Label dataset
        Text datasetLabel = new Text("Choose DataSet");

        //Text field for dataset
        TextField dataSetTextField = new TextField();

        //Label for location
        Text locationFileLabel = new Text("Choose Files Location");

        //Text field for location
        TextField locationTextField = new TextField();

        //check box for stemming
        CheckBox stemmingCheckBox = new CheckBox("Enable Stemming");
        stemmingCheckBox.setIndeterminate(false);

        //create buttons
        Button dataSetBrowse = new Button("Browse");
        Button filesLocationBrowse = new Button("Browse");
        Button startBTN = new Button("Start");
        Button showDictionary = new Button("Show Dictionary"); // todo event hander
        Button showCache = new Button("Show Cache"); // todo event handler
        Button load = new Button("Load"); // todo event handler
        Button save = new Button("Save"); // todo event handler
        Button reset = new Button("Reset"); // todo event handler


        //change buttons settings
        startBTN.setDefaultButton(true);

        startBTN.setMinWidth(100);
        load.setMinWidth(100);
        save.setMinWidth(100);
        reset.setMinWidth(100);

        startBTN.setPadding(new Insets(5));
        load.setPadding(new Insets(5));
        save.setPadding(new Insets(5));
        reset.setPadding(new Insets(5));
        startBTN.setDisable(true);
        save.setVisible(false);
        reset.setDisable(true);

        //default not shown
        showCache.setVisible(false);
        showDictionary.setVisible(false);

        //add buttons to grid
        gridPaneforButtons.add(startBTN, 0, 0);
        gridPaneforButtons.add(reset, 1, 0);
        gridPaneforButtons.add(load, 2, 0);
        gridPaneforButtons.add(save, 3, 0);
        gridPaneforButtons.add(showDictionary, 6, 0);
        gridPaneforButtons.add(showCache, 7, 0);

        //Setting the padding
        mainGridPane.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns
        mainGridPane.setVgap(20);
        mainGridPane.setHgap(20);

        //Setting the Grid alignment
        mainGridPane.setAlignment(Pos.TOP_LEFT);
        gridPaneforButtons.setAlignment(Pos.TOP_LEFT);

        //Arranging all the nodes in the grid
        mainGridPane.add(datasetLabel, 0, 1);
        mainGridPane.add(dataSetTextField, 1, 1);
        mainGridPane.add(dataSetBrowse, 2, 1);

        mainGridPane.add(locationFileLabel, 0, 2);
        mainGridPane.add(locationTextField, 1, 2);
        mainGridPane.add(filesLocationBrowse, 2, 2);

        mainGridPane.add(stemmingCheckBox, 0, 3);


        hBox.getChildren().add(mainGridPane);

        //create grid with 2 button

        //Setting the padding
        gridPaneforButtons.setPadding(new Insets(50, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns
        gridPaneforButtons.setVgap(5);
        gridPaneforButtons.setHgap(5);

        //Setting the Grid alignment
        gridPaneforButtons.setAlignment(Pos.CENTER);


        //add the grid to vBox
        vBox.getChildren().add(gridPaneforButtons);
        hBox.getChildren().add(vBox);
        HBox.setHgrow(vBox, Priority.ALWAYS);
        VBox.setVgrow(vBox, Priority.ALWAYS);
        vBox.setPrefWidth(1000);
        vBox.setPrefHeight(1000);

        //events to buttons

        dataSetBrowse.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                dataSetPath = controller.chooseFolder();
                dataSetTextField.setText(dataSetPath);
                if(locationPath != null)
                    startBTN.setDisable(false);

            }
        }));

        filesLocationBrowse.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                locationPath = controller.chooseFolder(); // todo maybe another function
                locationTextField.setText(locationPath);
                if(dataSetPath != null)
                    startBTN.setDisable(false);
            }
        }));

        startBTN.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                if(!showCache.isVisible() || !showDictionary.isVisible()|| !save.isVisible()){
                    showCache.setVisible(true);
                    showDictionary.setVisible(true);
                    save.setVisible(true);
                }
                if(reset.isDisable()) {
                    reset.setDisable(false);
                }


                    controller.startIndexing(dataSetPath, locationPath);

            }
        }));
        save.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                controller.saveDictionaryAndCache();

            }
        }));

        load.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                controller.loadDictionaryAndCache();
                showCache.setVisible(true);
                showDictionary.setVisible(true);

            }
        }));

        reset.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                controller.reset();

            }
        }));
        dataSetTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            dataSetPath = newValue;

            if(locationPath != null)
                startBTN.setDisable(false);
        });

        locationTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            locationPath = newValue;

            if(dataSetPath != null)
                startBTN.setDisable(false);
        });

        showDictionary.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Stage stage = new Stage();
                Scene scene = new Scene(createDictionaryView(), 400, 600);
                stage.setScene(scene);
                stage.show();
            }
        }));
        showCache.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                Stage stage = new Stage();
                Scene scene = new Scene(getCacheTable(), 400, 600);
                stage.setScene(scene);
                stage.show();
            }
        }));

        stemmingCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                controller.setToStemm(newValue);
            }
        });

        //Styling nodes
        startBTN.setStyle(
                "-fx-background-color: #4a8af4; -fx-textfill: black; -fx-color: black; -fx-alignment: top-center;");

        dataSetBrowse.setStyle(
                "-fx-background-color: white; -fx-border-color: #9e9e9e; -fx-border-radius: 5");
        filesLocationBrowse.setStyle(
                "-fx-background-color: white; -fx-border-color: #9e9e9e; -fx-border-radius: 5");
        load.setStyle(
                "-fx-background-color: #4a8af4; -fx-textfill: black; -fx-color: black; -fx-alignment: top-center;");

        save.setStyle(
                "-fx-background-color: #4a8af4; -fx-textfill: black; -fx-color: black; -fx-alignment: top-center;");

        reset.setStyle(
                "-fx-background-color: #4a8af4; -fx-textfill: black; -fx-color: black; -fx-alignment: top-center;");

        datasetLabel.setStyle("-fx-font: normal 15px 'serif' ");
        locationFileLabel.setStyle("-fx-font: normal 15px 'serif' ");
        dataSetTextField.setStyle("-fx-font: normal 15px 'serif' ");
        locationTextField.setStyle("-fx-font: normal 15px 'serif' ");
        mainGridPane.setStyle("-fx-background-color: whitesmoke;");
        showDictionary.setStyle(
                "-fx-background-color: white; -fx-border-color: #9e9e9e; -fx-border-radius: 5");

        showCache.setStyle(
                "-fx-background-color: white; -fx-border-color: #9e9e9e; -fx-border-radius: 5");


        //Creating a scene object
        FXMLLoader fxl = new FXMLLoader();

        //set the stage and scene
        primaryStage.setTitle("Information Retrieval - Part A");
        Scene scene = new Scene(hBox);
        primaryStage.setScene(scene);
        //SetStageCloseEvent(primaryStage);
        //Controller controller = fxl.getController();
        //controller.setResizeEvent(scene);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        primaryStage.setResizable(true);
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());

        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public TableView<Term> createDictionaryView() {

        //create the Table
        TableView<Term> tableDict = new TableView<Term>();
        //create the observable list from the dictionary
        ObservableList<Term> data = FXCollections.observableArrayList(controller.showDictionary());

        //create the Term Column
        TableColumn<Term, String> termName = new TableColumn<>("Term");
        termName.setMinWidth(200);
        termName.setCellValueFactory(new PropertyValueFactory<>("term"));

        //create the df Column
        TableColumn<Term, Integer> termDF = new TableColumn<>("Document Frequency");
        termDF.setMinWidth(200);
        termDF.setCellValueFactory(new PropertyValueFactory<>("df"));

        //create the Term Column
        TableColumn<Term, Integer> termFIC = new TableColumn<>("frequency In Corpus");
        termFIC.setMinWidth(200);
        termFIC.setCellValueFactory(new PropertyValueFactory<>("frequencyInCorpus"));

        tableDict.setItems(data);
        tableDict.getColumns().addAll(termName, termDF, termFIC);


        return tableDict;
    }
    TableView<Map.Entry<String,String>> getCacheTable() {

        // use fully detailed type for Map.Entry<String, String>
        TableColumn<Map.Entry<String, String>, String> column1 = new TableColumn<>("Term");
        column1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                // this callback returns property for just one cell, you can't use a loop here
                // for first column we use key
                return new SimpleStringProperty(p.getValue().getKey());
            }
        });

        TableColumn<Map.Entry<String, String>, String> column2 = new TableColumn<>("Posting");
        column2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                // for second column we use value
                return new SimpleStringProperty(p.getValue().getValue());
            }
        });

        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(controller.showCache().entrySet());
        TableView<Map.Entry<String,String>> table = new TableView<>(items);

        table.getColumns().setAll(column1, column2);
        return table;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
