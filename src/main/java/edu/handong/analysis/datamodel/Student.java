package edu.handong.analysis.datamodel;

import java.util.* ;
import java.io.* ;
import com.google.common.collect.*;

public class Student{

	
	private String studentID ;
	private ArrayList<Course> coursesTaken ;
	private HashMap<String,Integer> semesterByYearAndSemester ; 
	//2003-1 is 3rd semester for him.
	
	public Student(String studentID){

		this.studentID = studentID ;
		
		coursesTaken = new ArrayList<Course>(); //질문 1. 
		
		semesterByYearAndSemester = new HashMap<String,Integer>() ;
		
	}

	public void addCourse(Course newRecord){
		
		if (newRecord.exists())	coursesTaken.add(newRecord) ;
		else System.out.println("course is not exist");

	}


	public void getSemestersByYearAndSemester(){

		int[] yearAndSemester = new int[2];
		TreeSet<String> yas = new TreeSet<String>() ;
		

		if (coursesTaken.isEmpty()){
			System.out.println("coursesTaken is Empty");
			return ;
		}

		for(Course course : coursesTaken){
		
			yearAndSemester = course.parseYearSemester();
			yas.add(Integer.toString(yearAndSemester[0])+ "-" +Integer.toString(yearAndSemester[1])) ;

		}

		int index = 0 ;

		for(String key : yas){

			semesterByYearAndSemester.put(key,++index) ;
		
		}

		

	}


	public int getNumCourseInNthSemester(int semester){


		int count =0;
		
		if (semesterByYearAndSemester.isEmpty()){
			
			System.out.println("call getSemestersByYearAndSemester() first");
			return 0x0; 
		}
		
		if (semesterByYearAndSemester.size() < semester){

			System.out.println("you input wrong semester. this person graduated earlier. ");
			return 0x0;
		}

		BiMap<String, Integer> biMap = HashBiMap.create(semesterByYearAndSemester);	
		String key = new String();

		for(int yourSemester : biMap.inverse().keySet()){

			if(yourSemester == semester) key = biMap.inverse().get(yourSemester);	

		}
		

		if(coursesTaken.isEmpty()){
			System.out.println("you don't have courses data");
			return 0x0;
		}

		for(Course course : coursesTaken){
			
			int[] yas = new int[2];
			yas = course.parseYearSemester();
			String YAS =Integer.toString(yas[0])+ "-" +Integer.toString(yas[1]);

			if(key.compareTo(YAS)==0) count ++ ;	
		}

		return count ;
	}

	public ArrayList<String> makePersonalInfo(int startYear, int endYear){
		
		//getSemestersByYearAndSemester();
		Map<String, Integer> sortedMap = new TreeMap<String,Integer>(semesterByYearAndSemester);
		ArrayList<String> personalInfo = new ArrayList<String>();
		
		String semesterInfo = new String();

		for(String semester : sortedMap.keySet()){
			
			int yourSemester = sortedMap.get(semester) ;
			
			int year = Integer.parseInt(semester.substring(0,4));
			if(year < startYear || year > endYear) continue;
			
			semesterInfo = studentID+","+
				Integer.toString(sortedMap.size())+","+
				Integer.toString(yourSemester)+","+
				Integer.toString(getNumCourseInNthSemester(yourSemester)) ;

			personalInfo.add(semesterInfo);
			
		}

		return personalInfo; 
	}

	public boolean checkedRegister(String YAS){
	
			//getSemestersByYearAndSemester();
			if(semesterByYearAndSemester.keySet().contains(YAS)) return true;
			else return false;

	}
}
