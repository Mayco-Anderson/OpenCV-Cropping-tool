package com.dreamon.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import com.dreamon.view.TelaPrincipalController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {

	/*
	 * Window stage and root layout
	 */
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private TelaPrincipalController interfaceController;


	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Cropping tool...");
		initRootLayout();
		showContent();
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {

		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(TelaPrincipalController.class.getResource("RootLayout.fxml"));
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
			loader.setLocation(TelaPrincipalController.class.getResource("TelaPrincipal.fxml"));

			AnchorPane contentView = (AnchorPane) loader.load();

			// Set the content window inside the root center
			rootLayout.setCenter(contentView);
			// the the loader`s controller
			interfaceController =  loader.getController();
			interfaceController.setupController(primaryStage);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getFile() {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Salvar arquivo");
		fileChooser.setInitialFileName("Relatorio.txt");
		File file = fileChooser.showOpenDialog(primaryStage);
		return file;
	}
}
