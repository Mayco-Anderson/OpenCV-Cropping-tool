package com.dreamon.document;

import java.io.File;
import java.util.ArrayList;

public class Document {
	
	String path;
	File file;
	private ArrayList<String> filesNames;
	
	public Document (File file){
		this.file = file;
		openDocument();
	}
	
	public void openDocument(){
		File[] files = file.listFiles();
		filesNames = new ArrayList<>(files.length);
		for (File file : files){
			filesNames.add(file.getAbsolutePath());
		}
	}
	
	public ArrayList<String >getFileNames(){
		return filesNames;
	}

}
