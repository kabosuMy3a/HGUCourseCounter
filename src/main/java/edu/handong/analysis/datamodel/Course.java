package edu.handong.analysis.datamodel;

import edu.handong.analysis.utils.* ;
import java.util.*;
import org.apache.commons.csv.*;


public class Course{



	private String studentID = null ;
	private String yearMonthGraduated = null ;
	private String firstMajor = null ;
	private String secondMajor = null ;
	private String courseCode = null ;
	private String courseName = null ;
	private String courseCredit = null ;
	private int yearTaken = 0x0 ;
	private int semesterTaken = 0x0 ;
	private boolean courseExists = true ;

	public Course(CSVRecord parsedLine){

		if (parsedLine == null){
		 	System.out.println("CSVRecord line for making course is null");
			System.exit(0);
		}
		
		try{
			if(parsedLine.size()!=9) throw new Exception("Failed parse course it's not 9");
		
		}catch(Exception e){
			e.printStackTrace();
			courseExists = false ;
			return ;
		}

		
		studentID = parsedLine.get(0).trim() ;
	       	yearMonthGraduated = parsedLine.get(1).trim();
		firstMajor = parsedLine.get(2).trim();
		secondMajor = parsedLine.get(3).trim();
		courseCode = parsedLine.get(4).trim();
		courseName = parsedLine.get(5).trim();
		courseCredit = parsedLine.get(6).trim();

		try{
			yearTaken = Integer.parseInt(parsedLine.get(7).trim());
			semesterTaken = Integer.parseInt(parsedLine.get(8).trim());

		}catch (Exception e){
			System.out.println(e);
		}
	}

	public Course(String line){

		if (line == null) System.exit(0);
		ArrayList<String> splitedLines = null;
		
		try{
			String[] temp = line.split(",") ;
			splitedLines = new ArrayList<String>(Arrays.asList(temp));
			
			if(splitedLines.size() != 9) throw new failedSplitException() ;

		}catch (failedSplitException e){

			Utils.writeAFile(splitedLines,"./errorlog/splitedErrorList.txt") ;
			System.out.println(e);
			System.exit(0);
		
		}catch (Exception e){

			System.out.println(e); 
			System.exit(0);
		}
		
		studentID = splitedLines.get(0).trim() ;
	       	yearMonthGraduated = splitedLines.get(1).trim();
		firstMajor = splitedLines.get(2).trim();
		secondMajor = splitedLines.get(3).trim();
		courseCode = splitedLines.get(4).trim();
		courseName = splitedLines.get(5).trim();
		courseCredit =splitedLines.get(6).trim();

		try{
			yearTaken = Integer.parseInt(splitedLines.get(7).trim());
			semesterTaken = Integer.parseInt(splitedLines.get(8).trim());

		}catch (Exception e){
			System.out.println(e);
		
		}
	}
	

	public int[] parseYearSemester(){

		int[] YearAndSemester = new int[2];
		
		YearAndSemester[0] = yearTaken;
		YearAndSemester[1] = semesterTaken;

		return YearAndSemester; 

	}

	public String getCourseCode(){
		return courseCode;
	}

	public String getCourseName(){
		return courseName;
	}

	public boolean exists(){
		
		return courseExists ;
	}


}
