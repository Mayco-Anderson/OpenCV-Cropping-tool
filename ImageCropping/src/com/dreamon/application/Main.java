package com.dreamon.application;

import java.io.IOException;

import com.dreamon.view.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


/**
 * This class will simply launch a root layout and than insert the main stage into it.
 * The main code is in the {@link MainController}
 * @author Mayco
 *
 */
public class Main extends Application {

	/*
	 * Window stage and root layout
	 */
	private Stage primaryStage;
	private BorderPane rootLayout;

	private MainController mainController;

	public static void main(String[] args) {
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Marking tool");
		initRootLayout();
		showContent();
	}


	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {

		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainController.class.getResource("RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load up the initial content of the window.
	 */
	public void showContent() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainController.class.getResource("MainStage.fxml"));

			AnchorPane contentView = (AnchorPane) loader.load();

			// Set the content window inside the root center
			rootLayout.setCenter(contentView);
			mainController =  loader.getController();
			mainController.setupController(primaryStage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
