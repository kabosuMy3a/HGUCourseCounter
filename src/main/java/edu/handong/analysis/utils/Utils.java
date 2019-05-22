package edu.handong.analysis.utils ;

import java.util.* ;
import java.io.* ;

public class Utils {
		

	public static ArrayList<String> getLines(String filepath, boolean removeHeader){

		ArrayList<String> extractedList = new ArrayList<String>();
		String csvLine = "";

		try{
			Scanner inputStream = new Scanner(new File(filepath)) ;
			if(removeHeader && inputStream.hasNextLine()){
				csvLine = inputStream.nextLine(); 	
			}

			while(inputStream.hasNextLine()){
				
				csvLine = inputStream.nextLine();
				extractedList.add(csvLine) ;

			}

			inputStream.close();


		} catch (FileNotFoundException e){

			System.out.println("The file path does not exist. Please check your CLI argument!");

		} catch (Exception e){

			System.out.println(e);
		}
		
		return extractedList ;
	}



	public static void writeAFile(ArrayList<String> lines, String targetFileName){
	

		PrintWriter outputStream = null;

		try{
			File file = new File(targetFileName) ;	
			File abFile = file.getAbsoluteFile();

			if(!abFile.getParentFile().exists()){
					abFile.getParentFile().mkdirs();
			}


			if(!file.exists()){
				outputStream = new PrintWriter(targetFileName);
			}

			else{
				FileWriter filewriter = new FileWriter(targetFileName, true);
				outputStream = new PrintWriter(filewriter);

			}

		}

		catch(FileNotFoundException e){

			System.out.println("The file path does not exist. Please check your CLI argument!");
			System.exit(0); 
		}
		
		catch(Exception e){
			System.out.println(e) ;
			System.out.println("Error opening the file " + targetFileName);
			System.exit(0);
		}

		for(String line : lines){
	
			outputStream.println(line) ;

		}

		outputStream.close();
		System.out.println("File Writtened Successfully");
	}
}
