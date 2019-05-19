package edu.handong.analysis.datamodel;

import java.util.* ;
import java.io.* ;

public class Student{

	
	private String studentID ;
	private ArrayList<Course> coursesTaken = new ArrayList<Course>() ;
	private HashMap<String,Integer> semesterByYearAndSemester = new HashMap<String,Integer>() ; 
	//2003-1 is 3rd semester for him.
	
	public Student(String studentID){

		this.studentID = studentID ;
		
		//ArrayList<Course> coursesTaken = new ArrayList<Course>(); 질문 1. 
		
		//semesterByYearAndSemester = new HashMap<String,Integer>() ;
		
	}

	public void addCourse(Course newRecord){

		coursesTaken.add(newRecord) ;

	}


	public void getSemestersByYearAndSemester(){

		int[] yearAndSemester = new int[2];

		if (coursesTaken.isEmpty()){
			System.out.println("coursesTaken is Empty");
			return ;
		}

		for(Course course : coursesTaken){

			yearAndSemester = course.parseYearSemester();
			String yas =Integer.toString(yearAndSemester[0])+ "-" +Integer.toString(yearAndSemester[1]) ;  
			if(!semesterByYearAndSemester.containsKey(yas)){
				semesterByYearAndSemester.put(yas,semesterByYearAndSemester.size()+1) ;

			};

				
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

		String key = new String();
		int index = 0 ;
		Map<String, Integer> sortedMap = new TreeMap<String,Integer>(semesterByYearAndSemester);
		for(String temp : sortedMap.keySet()){
	
			//index ++ ;

			if(++index == semester){

				key = temp ;
				break; 			
			}			
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

	public ArrayList<String> makePersonalInfo(){
		
		getSemestersByYearAndSemester();
		Map<String, Integer> sortedMap = new TreeMap<String,Integer>(semesterByYearAndSemester);
		//System.out.println(sortedMap);
		ArrayList<String> personalInfo = new ArrayList<String>();
		
		String semesterInfo = new String();

		for(String semester : sortedMap.keySet()){
			
			int yourSemester = sortedMap.get(semester) ;
			
			semesterInfo = studentID+","+
				Integer.toString(sortedMap.size())+","+
				Integer.toString(yourSemester)+","+
				Integer.toString(getNumCourseInNthSemester(yourSemester)) ;

			personalInfo.add(semesterInfo);
			
		}

		return personalInfo; 
	}
}
