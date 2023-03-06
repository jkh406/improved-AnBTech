package com.anbtech.file;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import jxl.*;
import jxl.biff.*;
import jxl.read.biff.*;

public class excelFileWriter extends Object 
{
	private String file="";					//파일명 (path포함)
	private String sheet_name="";			//Sheet 명
	private String[][] array;				//읽은 파일내용을 배열에 담기 
	private int col_cnt=0;					//읽은 파일의 한 라인당 데이터 갯수 
	private int row_cnt=0;					//읽은 파일의 라인갯수 
	private int sheet_cnt=0;				//Sheet갯수

	private String encoding = "UTF8";
    private jxl.write.WritableWorkbook wb ;
	private jxl.write.WritableSheet sheet ;
	private jxl.write.WritableFont font ;
	private jxl.write.WritableCellFormat cell ;

	/******************************************************
	// 생성자 
	******************************************************/
	public excelFileWriter()
	{
		
	}

	/******************************************************
	// 파일명 생성 (path포함)
	******************************************************/
	public void setFileOpen(String file) throws Exception
	{
		this.file = file;
		wb = Workbook.createWorkbook(new File(file));
	}

	/******************************************************
	// Sheet명 생성 
	******************************************************/
	public void setSheetOpen(String sheet_name,int sheet_no) throws Exception
	{
		this.sheet_name = sheet_name;
		sheet = wb.createSheet(sheet_name,sheet_no);
	}

	/******************************************************
	// 파일 닫기
	******************************************************/
	public void setFileClose() throws Exception
	{
		wb.write();
		wb.close();
	}

	/******************************************************
	// font 및 size 정하기
	// font_size : 글자크기
	// italic : 글자기울기(true or false)
	// bold : 글자 굵기 (true or false)
	******************************************************/
	public void setCellTimesFormat(int font_size,boolean italic,boolean bold) throws Exception
	{
		if(bold == true) {
			font = new jxl.write.WritableFont(jxl.write.WritableFont.TIMES,font_size,jxl.write.WritableFont.BOLD,italic);
		} else {
			font = new jxl.write.WritableFont(jxl.write.WritableFont.TIMES,font_size,jxl.write.WritableFont.NO_BOLD,italic);
		}
		cell = new jxl.write.WritableCellFormat(font);

	}

	/******************************************************
	// Excel의 해당 Sheet no의 cell에 String 기록하기
	*******************************************************/
	public void writeStringCell(int column_no,int row_no,String content) throws Exception
	{
		jxl.write.Label label;

		if(cell == null) { 
			label = new jxl.write.Label(column_no,row_no,content);
		} else { 
			label = new jxl.write.Label(column_no,row_no,content,cell);
		}
		sheet.addCell(label);
	}

	/******************************************************
	// Excel의 해당 Sheet no의 cell에 Number 기록하기
	*******************************************************/
	public void writeNumberCell(int column_no,int row_no,int content) throws Exception
	{
		jxl.write.Number number = new jxl.write.Number(column_no,row_no,content);
		sheet.addCell(number);
	}

	/*****************************************************
	// import한 파일  삭제하기 (path포함)
	******************************************************/	
	public boolean delFilename(String filepath) 
	{
		File FN = new File(filepath);
		if(FN.exists()) return FN.delete();	
		else return false;
	}
}