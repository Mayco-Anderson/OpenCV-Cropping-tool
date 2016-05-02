package com.dreamon.view;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import com.dreamon.application.Main;
import com.dreamon.document.Document;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TelaPrincipalController implements Initializable{

	Stage stage;

	Main main;

	Document document;

	@FXML
	ListView<String> listView;

	@FXML
	Button selectFolder;

	@FXML
	Label label;

	@FXML 
	Label fileLabel;

	@FXML
	ImageView imageView;

	@FXML
	AnchorPane imagePane;
	
	@FXML
	Label coordinatesLabel;

	Rectangle rect;
	SimpleDoubleProperty rectinitX = new SimpleDoubleProperty();
	SimpleDoubleProperty rectinitY = new SimpleDoubleProperty();
	SimpleDoubleProperty rectX = new SimpleDoubleProperty();
	SimpleDoubleProperty rectY = new SimpleDoubleProperty();


	@FXML
	protected void openFile(ActionEvent e) {
		System.out.println("Oppening file!");
		File file = getFile();
		if(file != null){
			label.setText(file.getAbsolutePath());
			document = new Document(file);
			displayDocument();
		}
	}

	private void openImage(File file){
		System.out.println(file.getPath());
		System.out.println(file.getAbsolutePath());
		Image image = new Image("file:" + file.getAbsolutePath());
		System.out.println("Image: "+image);
		imageView.setImage(image);		
	}


	EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
	
		

		@Override
		public void handle(MouseEvent mouseEvent) {
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
				rect.setX(mouseEvent.getX());
				rect.setY(mouseEvent.getY());
				rectinitX.set(mouseEvent.getX());
				rectinitY.set(mouseEvent.getY());
			} else if (
					(mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) ||
					(mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED)
					)
			{
				rectX.set(mouseEvent.getX());
				rectY.set(mouseEvent.getY());
			}
			writeCoordinates();
		}
	};

	private Rectangle getNewRectangle() {
		Rectangle r = new Rectangle();
		r.setFill(Color.web("blue", 0.1));
		r.setStroke(Color.BLUE);
		return r;
	}


	private void setUpListItemHandler(){
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if (newValue != null){
					File file = new File(newValue);
					fileLabel.setText(file.getName());
					openImage(file);
				}	

			}

		});

	}

	private void displayDocument(){
		ObservableList<String> items =FXCollections.observableArrayList (document.getFileNames());
		listView.setItems(items);
	}


	public void  setupController(Stage scene){
		this.stage = scene;
		imagePane.setOnMouseDragged(mouseHandler);
		imagePane.setOnMousePressed(mouseHandler);
		imagePane.setOnMouseReleased(mouseHandler);

		rect = getNewRectangle();
		rect.widthProperty().bind(rectX.subtract(rectinitX));
		rect.heightProperty().bind(rectY.subtract(rectinitY));
		imagePane.getChildren().add(rect);
	}

	private Point2D getPointInImage(double inPanex, double inPaneY){
		System.out.println(imageView.getLayoutX());
		System.out.println(imageView.getLayoutY());
		double pointInImageX = inPanex - imageView.getLayoutX();
		double pointInImageY = inPaneY - imageView.getLayoutY();
		Point2D point2d = new Point2D(pointInImageX, pointInImageY);
		return point2d;
	}
	private void writeCoordinates(){
		String content = "";
		
		Point2D initialPoint = getPointInImage(rectinitX.get(), rectinitY.get());
		Point2D finalPoint = getPointInImage(rectX.get(), rectY.get());
		
		content += "Coordinates";
		content += "\nInitial X = " + initialPoint.getX();
		content += "\nInitial Y = " + initialPoint.getY();
		content += "\nFInal X = " + finalPoint.getX() ;
		content += "\nFInal Y = " + finalPoint.getY();
		coordinatesLabel.setText(content);
	}


	public File getFile() {
		DirectoryChooser folderChoser = new DirectoryChooser();
		folderChoser.setTitle("Open File");
		File File = folderChoser.showDialog(stage);
		return File;
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setUpListItemHandler();

	}


}
