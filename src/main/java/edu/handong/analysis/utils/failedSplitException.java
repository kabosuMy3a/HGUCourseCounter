package edu.handong.analysis.utils ;


public class failedSplitException extends Exception {


	public failedSplitException(){

		super("failed Split by 9") ;
	}

	public failedSplitException(String msg){

		super(msg);
	}
}
