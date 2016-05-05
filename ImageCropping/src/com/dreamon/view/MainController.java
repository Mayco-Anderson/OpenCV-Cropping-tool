package com.dreamon.view;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/*
 * This class handles all inputs from the Main Stage.
 */
public class MainController implements Initializable{


	/*
	 * A reference to the stage. Needed to open file chooser.
	 */
	Stage stage;

	/*
	 * The document is where all information is stored.
	 */
	Document document;

	/*
	 * References to the components of the view
	 */
	@FXML
	ListView<String> listView;
	@FXML
	Label folderLocation;
	@FXML
	Label fileLocation;
	@FXML 
	Label fileLabel;
	@FXML
	ImageView imageView;
	@FXML
	AnchorPane imagePane;
	@FXML
	Label coordinatesLabel;


	/*
	 * The marked area is represented by a rectangle.
	 * Its properties are listed below
	 */
	Rectangle rect;
	SimpleDoubleProperty rectinitX = new SimpleDoubleProperty();
	SimpleDoubleProperty rectinitY = new SimpleDoubleProperty();
	SimpleDoubleProperty rectX = new SimpleDoubleProperty();
	SimpleDoubleProperty rectY = new SimpleDoubleProperty();

	/*
	 * Keep track of the initial values of a drag movement
	 */
	private double iniX = 0;
	private double iniY = 0;

	/**
	 * This method is called when the user press the open folder button.
	 * Request the file chooser to get a folder containing images
	 * @param e
	 */
	@FXML
	protected void openFolder(ActionEvent e) {
		File file = getSourceFolder();
		if(file != null){
			folderLocation.setText(file.getAbsolutePath());
			diplaySourceContent();
		}
	}

	/**
	 * Retrieve or create a new file to store the information about the 
	 * cropped are for each image
	 * @param e
	 */
	@FXML
	protected void openFile(ActionEvent e){
		File file = loadFile();
		if (file != null &&  file.exists()){
			fileLocation.setText(file.getAbsolutePath());
			document.loadFile();
		}
		
	}


	@FXML
	private void save(ActionEvent event) {

		Rectangle currentRectangle = getCurrentRectangule();
		if (currentFile != null && currentFile.exists() && currentRectangle != null){
			System.out.println("inserting data. File name: : "+currentFile.getName() + getCurrentRectangule());
			document.getCroppedImages().put(currentFile.getName(), getCurrentRectangule());
			document.save();
		}
	}

	File currentFile;

	private void openImage(File file){
		currentFile = file;
		Image image = new Image("file:" + file.getAbsolutePath());
		imageView.setImage(image);
		System.out.println("Changing image. Loading rectangle...");
		// Load a rectangle if exists one. Or clear the image
		Rectangle rectangle = document.getCroppedImages().get(file.getName());
		if (rectangle == null){
			rectangle = getNewRectangle();
		}

		System.out.println("Rectangle recovered: "+rectangle);
		setRectanguleCoordinates(
				rectangle.getX(),
				(rectangle.getX() + rectangle.getWidth()),
				rectangle.getY(),
				(rectangle.getY() + rectangle.getHeight())
				);
	}

	/*
	 * Configures a handler for the mouse event
	 */
	EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent mouseEvent) {
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
				iniX = mouseEvent.getX();
				iniY = mouseEvent.getY();			

			} else if (
					(mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) ||
					(mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED)
					)
			{
				drawnRectangle(mouseEvent.getX(), mouseEvent.getY());
			}

		}
	};

	/**
	 * Drawn a rectangle based on the initial coordinates and the current coordinates of the mouse drag.
	 * @param x
	 * @param y
	 */
	private void drawnRectangle(double x, double y){
		double leftX, topY, rightX, bottomY =0;

		if(x > iniX){
			leftX = iniX;
			rightX = x;
		}
		else {
			leftX = x;
			rightX = iniX;
		}

		if(y > iniY){
			topY = iniY;
			bottomY = y;
		}
		else {
			topY = y;
			bottomY = iniY;
		}

		setRectanguleCoordinates(leftX, rightX, topY, bottomY);
		writeCoordinates();
	}

	private void setRectanguleCoordinates(double left, double right, double top, double bottom){
		rectX.set(right);
		rectY.set(bottom);
		rectinitX.set(left);
		rectinitY.set(top);
		rect.setX(left);
		rect.setY(top);
	}

	private Rectangle getCurrentRectangule(){
		return new Rectangle(
				rectinitX.get(),
				rectinitY.get(),
				(rectX.get() - rectinitX.get()),
				(rectY.get() - rectinitY.get()));
	}

	/**
	 * Create a new rectangle with a blue background and stroke
	 * @return
	 */
	private Rectangle getNewRectangle() {
		Rectangle r = new Rectangle();
		r.setFill(Color.web("blue", 0.1));
		r.setStroke(Color.BLUE);
		return r;
	}

	/**
	 * Write the coordinates of a rectangle in a label
	 */
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


	/**
	 * Display the name of all images in the list view
	 */
	private void diplaySourceContent(){
		ObservableList<String> items = FXCollections.observableArrayList (document.getFileNames());
		listView.setItems(items);
	}

	/**
	 * Used to initialize this view.
	 * Programmatically add a rectangle to the image pane
	 * @param scene
	 */
	public void  setupController(Stage stage){
		this.stage = stage;
		imagePane.setOnMouseDragged(mouseHandler);
		imagePane.setOnMousePressed(mouseHandler);
		imagePane.setOnMouseReleased(mouseHandler);

		rect = getNewRectangle();
		rect.widthProperty().bind(rectX.subtract(rectinitX));
		rect.heightProperty().bind(rectY.subtract(rectinitY));
		imagePane.getChildren().add(rect);
	}

	/**
	 * Called when the view is loaded.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setUpListItemHandler();
	}

	/**
	 * Set up a handler to deal with selection action in the list view
	 */
	private void setUpListItemHandler(){
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null){
					File file = new File(newValue);
					fileLabel.setText(file.getName());
					openImage(file);
				}	
			}
		});
	}


	/**
	 * Helper method to create a point2D object in the image coordinates based on the touch event coordinates
	 */
	private Point2D getPointInImage(double inPanex, double inPaneY){
		double pointInImageX = inPanex - imageView.getLayoutX();
		double pointInImageY = inPaneY - imageView.getLayoutY();
		Point2D point2d = new Point2D(pointInImageX, pointInImageY);
		return point2d;
	}

	/**
	 * Helper method to open a folder chooser popup and get a folder.
	 * @return The file representing a folder
	 */
	public File  getSourceFolder() {
		DirectoryChooser folderChoser = new DirectoryChooser();
		folderChoser.setTitle("Open File");
		File file = folderChoser.showDialog(stage);
		if (file != null){
			document = new Document(file);
			document.setSourceFolder(file);
			return file;
		}
		return null;
	}

	/**
	 * Load the file where the marks properties will be stored.
	 * It can also be used to load previous properties.
	 * @return
	 */
	public File loadFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select File to load and save data");
		File file = fileChooser.showOpenDialog(stage);
		document.setDestinationFile(file);
		return file;
	}

}
