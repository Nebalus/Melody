package de.melody.datamanagment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.melody.core.Constants;
import de.melody.tools.Utils.IDGenerator;

public class GenerateFile {

	private final File file;
	
	public GenerateFile(String format) {
		File newfile = new File(Constants.TEMP_DIRECTORY + "/" + IDGenerator.generateID() + "."+format);
		do {
			newfile = new File(Constants.TEMP_DIRECTORY + "/" + IDGenerator.generateID() + "."+format);
		}while(newfile.exists());
		
		try {
			newfile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		file = newfile;
	}
	
	public File getFile() {
		return file;
	}
	
	public void writeStringToFile(String content) {
		try {
			FileWriter myWriter = new FileWriter(file);
			myWriter.write(content);
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
