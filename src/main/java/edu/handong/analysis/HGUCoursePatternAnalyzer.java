package edu.handong.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.io.*;

import org.apache.commons.cli.*;
import org.apache.commons.csv.*;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

public class HGUCoursePatternAnalyzer {
	
	private CSVParser parseData ;
	private HashMap<String,Student> students;
	private ArrayList<Course> courses ;
	//filed for cliOptions 
	private String dataPath ;
	private String resultPath ;
	private int analysisOption ;
	private String courseCodeName ;
	private int startYear ;
	private int endYear ;
	private boolean help ;

	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 */
	public void run(String[] args) {
		
		Options options = createOptions();


		if(parseOptions(options,args)){

			if(help){
				printHelp(options);
				return;
			}

			if(analysisOption ==2 && courseCodeName==null){
				System.out.println("please input courseCodeName when activate option 2");
				System.out.println();
				printHelp(options);
				return;
			}
		}else return;
		
		try{
			Reader in = new FileReader(dataPath);
			parseData = CSVParser.parse(in,CSVFormat.DEFAULT.withFirstRecordAsHeader());

		}catch(IOException e){
			e.printStackTrace();
			System.exit(0);

		}
		
		students = loadStudentCourseRecords(parseData);
		// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
		Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
		ArrayList<String> linesToBeSaved ;

		if(analysisOption == 1){
			// Generate result lines to be saved.
			linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
		}
		else{
			linesToBeSaved = makeCourseInfoDevidedByYear(sortedStudents);	
		}	
		Utils.writeAFile(linesToBeSaved, resultPath);

	}

	
	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(CSVParser parseData ) {
		
		HashMap<String,Student> students = new HashMap<String,Student>(); 
		courses = new ArrayList<Course>(); 

		for(CSVRecord parsedLine : parseData){
			
			String Id = parsedLine.get(0) ;
			Student student ;
			if(!students.containsKey(Id)){

				student = new Student(Id) ;
				students.put(Id,student);  //if call by reference only this is okay
			}
			else{
				student = students.get(Id);
			}

			Course course = new Course(parsedLine) ;
			courses.add(course);//for course data parsing
			student.addCourse(course) ;

		}

		return students; // do not forget to return a proper variable.
	}

	/**
	 * This method generate the number of courses taken by a student in each semester. The result file look like this:
	 * StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9
     * 0001,14,2,8
	 * ....
	 * 
	 * 0001,14,1,9  this means, 0001 student registered 14 semeters in total. In the first semeter (1), the student took 9 courses.
	 * 
	 * 
	 * @param sortedStudents
	 * @return
	 */
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents){
		
		ArrayList<String> personalInfo = new ArrayList<String>() ;
		ArrayList<String> peopleInfo = new ArrayList<String>() ;
		for(String Id : sortedStudents.keySet()){
			
			Student student = sortedStudents.get(Id);
			personalInfo = student.makePersonalInfo(startYear,endYear);
			
			for(String line : personalInfo){
				peopleInfo.add(line) ;
			}
		}

		return peopleInfo; // do not forget to return a proper variable.
	}

	private ArrayList<String> makeCourseInfoDevidedByYear(Map<String,Student>sortedStudents){

		//startYear //endYear
		//courseCodeName == courseCode
		String courseName ="" ;
		boolean setCourseName = false;
		int[] yas = new int[2] ;
		HashMap<String,Integer[]> courseInfo = new HashMap<String,Integer[]>();

		for(Course course : courses){

			yas = course.parseYearSemester() ;
			String YAS = yas[0]+"-"+yas[1];
			if (yas[0] < startYear || yas[0] > endYear) continue ;
			
			if(courseCodeName.equals(course.getCourseCode())){
				
				if(!setCourseName){
				       	courseName = course.getCourseName() ;
					setCourseName = true;
				}

				if(courseInfo.keySet().contains(YAS)){
					Integer[] totalAndTaken = new Integer[2] ;
					totalAndTaken = courseInfo.get(YAS) ;
					totalAndTaken[1] += 1 ;
					courseInfo.put(YAS,totalAndTaken);
				}
				else{
					Integer[] totalAndTaken = {0,1};
					courseInfo.put(YAS,totalAndTaken) ;
				}
			}
		}
		
		for(String YAS : courseInfo.keySet()){
			for(String id : sortedStudents.keySet()){			
				if(sortedStudents.get(id).checkedRegister(YAS)){
					Integer[] totalAndTaken = new Integer[2];
					totalAndTaken = courseInfo.get(YAS);
					totalAndTaken[0] += 1;
					courseInfo.put(YAS,totalAndTaken);		
				}	
			}
		}

		ArrayList<String> courseInfoToString = new ArrayList<String>();
		Map<String,Integer[]> sortedCourseInfo = new TreeMap<String,Integer[]>(courseInfo);
		
		courseInfoToString.add("Year,Semester,CouseCode, CourseName,TotalStudents,StudentsTaken,Rate");
		for(String key : sortedCourseInfo.keySet()){

			int totalStudents = sortedCourseInfo.get(key)[0] ;
			int studentsTaken = sortedCourseInfo.get(key)[1] ;
				
			double rate = (double)studentsTaken/totalStudents * 100 ; 
			String srate = String.format("%.1f",rate);

			String info = key.substring(0,4)+","+
					key.substring(5)+","+
					courseCodeName+","+
					courseName+","+
					Integer.toString(totalStudents)+","+
					Integer.toString(studentsTaken)+","+
					srate+"%";
			
			courseInfoToString.add(info);
		}

		return courseInfoToString ;

	}

	
	// Definition Options
	private Options createOptions() {
		
		Options options = new Options();

		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("Input path")
				.required()
				.build());
		
		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path")
				.hasArg()
				.argName("Output path")
				.required()
				.build());

		options.addOption(Option.builder("a").longOpt("analysis")
				.desc("1: Count courses per semester, 2: Count per course name and year")
				.hasArg()
				.argName("Anaylsis option")
				.required()
				.build());		

		options.addOption(Option.builder("c").longOpt("coursecode")
				.desc("Course code for '-a 2' option")
				.hasArg()
				.argName("Course code")
				//.required()
				.build());

		options.addOption(Option.builder("s").longOpt("startyear")
				.desc("Set the start year for analysis e.g., -s 2002")
				.hasArg()
				.argName("Start year for analysis")
				.required()
				.build());

		options.addOption(Option.builder("e").longOpt("endyear")
				.desc("Set the end year for analysis e.g., -e 2005")
				.hasArg()
				.argName("End year for analysis")
				.required()
				.build());

		options.addOption(Option.builder("h").longOpt("help")
				.desc("Show a Help page")
				.build());

		return options ;
	}

	private void printHelp(Options options){

		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Course Analyzer";
		String footer = "";
		
		formatter.printHelp("HGUCourseCounter", header, options, footer ,true);

	}

	private boolean parseOptions(Options options, String[] args){

		CommandLineParser parser = new DefaultParser();

		try{
			CommandLine cmd = parser.parse(options, args);

			dataPath = cmd.getOptionValue("i");
			resultPath = cmd.getOptionValue("o");
			analysisOption = Integer.parseInt(cmd.getOptionValue("a")) ;
			courseCodeName = cmd.getOptionValue("c");
			startYear = Integer.parseInt(cmd.getOptionValue("s"));
			endYear = Integer.parseInt(cmd.getOptionValue("e")) ;
			help = cmd.hasOption("h");


		}catch(NumberFormatException e){

			System.out.println(e);
			System.out.println();
			printHelp(options);
			return false ;
	
		}catch(Exception e){

			printHelp(options);
			return false;
		}

		return true;
	}

}
