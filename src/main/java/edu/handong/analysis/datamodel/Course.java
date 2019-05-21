package edu.handong.analysis.datamodel;

import edu.handong.analysis.utils.* ;
import java.util.*;

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
	
	public ArrayList<String> parseCourseInfo(){

		ArrayList<String> parsedInfo = new ArrayList<String>();
		parsedInfo.add(studentID);
		parsedInfo.add(yearMonthGraduated);

		return parsedInfo ;
	}

	public int[] parseYearSemester(){

		int[] YearAndSemester = new int[2];
		
		YearAndSemester[0] = yearTaken;
		YearAndSemester[1] = semesterTaken;

		return YearAndSemester; 

	}


	public void save(){

		ArrayList<String> temp = new ArrayList<String>();
		temp.add(studentID);
		temp.add(yearMonthGraduated);
		temp.add(firstMajor);
		temp.add(secondMajor);
		temp.add(courseCode);
		temp.add(courseName);
		temp.add(courseCredit);
		temp.add(Integer.toString(yearTaken));
		temp.add(Integer.toString(semesterTaken));

		Utils.writeAFile(temp,"./data.txt");

	}
}
