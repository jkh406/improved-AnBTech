package com.anbtech.gw.business;
import java.io.*;
import java.util.*;
import java.sql.*;
import com.anbtech.text.*;
import com.anbtech.file.FileWriteString;
import com.anbtech.file.textFileReader;
import com.anbtech.date.anbDate;

public class ApprovalToEmailBO
{
	// Database Wrapper Class 선언
	private com.anbtech.date.anbDate anbdt = null;					//일자 처리
	private com.anbtech.text.StringProcess str = null;				//문자,문자열에 관련된 연산자
	private com.anbtech.file.FileWriteString text;					//내용을 파일로 담기
	
	/***************************************************************************
	 * 생성자 메소드
	 **************************************************************************/
	public ApprovalToEmailBO() 
	{	
		anbdt = new com.anbtech.date.anbDate();			//날자처리
		str = new com.anbtech.text.StringProcess();		//문자,문자열에 관련된 연산자
		text = new com.anbtech.file.FileWriteString();	//내용을 파일로 담기
	}


}