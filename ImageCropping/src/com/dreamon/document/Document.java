package com.dreamon.document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.shape.Rectangle;

/**
 * This Document keeps track of the source folder containing all the images
 * and also the destination file that will used to store information about each image. 
 * @author Mayco
 */
public class Document {


	/*
	 * The source folder
	 */
	private File sourceFolder;

	/*
	 * THis list contains the path for each image of the source folder
	 */
	private ArrayList<String> imagePaths;

	/*
	 * The destination file used to store the marked information about each image
	 */
	private File destinationFile;

	/*
	 * A HashMap that keep track of the marked area of each image
	 * The key is the name os the image and the value is a rectangle object that represents the 
	 * marked area
	 */
	private Map<String, Rectangle> markedImages = new HashMap<>();

	public Document (File file){
		this.sourceFolder = file;
		openSourceFolder();
	}

	/**
	 * Open the source folder extract the absolute path for each image
	 */
	public void openSourceFolder(){
		File[] files = sourceFolder.listFiles();
		imagePaths = new ArrayList<>(files.length);
		for (File file : files){
			imagePaths.add(file.getAbsolutePath());
		}
	}

	/**
	 * Read a file and try to retrieve information about each image.
	 * @param file
	 */
	public void loadFile() {

		if (destinationFile == null || !destinationFile.exists()){	return;	}

		try (BufferedReader br = new BufferedReader(new FileReader(destinationFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				translateLine(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Reads a line in the following pattern
	 * img1.jpg  1  140 100 45 45
	 * This line must always have 6 parts separated by simple spaces where the first one
	 * is the name of the image, the second is always 1 as we are only handling with single objects in one image.
	 * The other numbers are the X, Y width and height of the marked area
	 * Successfully marked
	 * @param line
	 */
	private void translateLine(String line) {
		String [] parts = line.split(" ");
		if (parts.length != 6){
			throw new IllegalArgumentException("Unable to read line: "+line);
		}
		else{
			Rectangle rectangle =  new Rectangle();
			rectangle.setX(Double.parseDouble(parts[2]));
			rectangle.setY(Double.parseDouble(parts[3]));
			rectangle.setWidth(Double.parseDouble(parts[4]));
			rectangle.setHeight(Double.parseDouble(parts[5]));
			markedImages.put(parts[0], rectangle);
		}
	}

	/**
	 * Writes the data in the marked images HashMap into the specified destination file
	 */
	public void save(){
		if (destinationFile == null || !destinationFile.exists()){	return;	}
		try {
			FileWriter fileWriter = new FileWriter(destinationFile);
			for (Map.Entry<String, Rectangle> entry : markedImages.entrySet()) {
				String imageName = entry.getKey();
				Rectangle rectangle = entry.getValue();
				String data = data2String(imageName, rectangle);
				fileWriter.append(data);
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	/**
	 * Get data stored in the marked images HashMap and write a line in the following pattern
	 * img1.jpg  1  140 100 45 45
	 * This line must always have 6 parts separated by simple spaces where the first one
	 * is the name of the image, the second is always 1 as we are only handling with single objects in one image.
	 * The other numbers are the X, Y width and height of the marked area
	 * @param line
	 * @return A string in containing the name of each image and the information about the marked area
	 */
	private String data2String(String imageName, Rectangle rectangle) {
		String ret = imageName + " 1 " 
				+ rectangle.getX() + " "
				+ rectangle.getY() + " "
				+ rectangle.getWidth() + " "
				+ rectangle.getHeight() + "\n";

		return ret;
	}

	/*
	 * Getters and setters
	 */
	
	public ArrayList<String >getFileNames(){
		return imagePaths;
	}


	public File getDestinationFile() {
		return destinationFile;
	}

	public void setDestinationFile(File destinationFile) {
		this.destinationFile = destinationFile;
	}

	public Map<String, Rectangle> getCroppedImages() {
		return markedImages;
	}

	public void setCroppedImages(Map<String, Rectangle> croppedImages) {
		this.markedImages = croppedImages;
	}


	public void setSourceFolder(File file){
		this.sourceFolder = file;
	}


}
