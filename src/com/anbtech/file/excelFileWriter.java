package com.anbtech.file;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import jxl.*;
import jxl.biff.*;
import jxl.read.biff.*;

public class excelFileWriter extends Object 
{
	private String file="";					//���ϸ� (path����)
	private String sheet_name="";			//Sheet ��
	private String[][] array;				//���� ���ϳ����� �迭�� ��� 
	private int col_cnt=0;					//���� ������ �� ���δ� ������ ���� 
	private int row_cnt=0;					//���� ������ ���ΰ��� 
	private int sheet_cnt=0;				//Sheet����

	private String encoding = "UTF8";
    private jxl.write.WritableWorkbook wb ;
	private jxl.write.WritableSheet sheet ;
	private jxl.write.WritableFont font ;
	private jxl.write.WritableCellFormat cell ;

	/******************************************************
	// ������ 
	******************************************************/
	public excelFileWriter()
	{
		
	}

	/******************************************************
	// ���ϸ� ���� (path����)
	******************************************************/
	public void setFileOpen(String file) throws Exception
	{
		this.file = file;
		wb = Workbook.createWorkbook(new File(file));
	}

	/******************************************************
	// Sheet�� ���� 
	******************************************************/
	public void setSheetOpen(String sheet_name,int sheet_no) throws Exception
	{
		this.sheet_name = sheet_name;
		sheet = wb.createSheet(sheet_name,sheet_no);
	}

	/******************************************************
	// ���� �ݱ�
	******************************************************/
	public void setFileClose() throws Exception
	{
		wb.write();
		wb.close();
	}

	/******************************************************
	// font �� size ���ϱ�
	// font_size : ����ũ��
	// italic : ���ڱ���(true or false)
	// bold : ���� ���� (true or false)
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
	// Excel�� �ش� Sheet no�� cell�� String ����ϱ�
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
	// Excel�� �ش� Sheet no�� cell�� Number ����ϱ�
	*******************************************************/
	public void writeNumberCell(int column_no,int row_no,int content) throws Exception
	{
		jxl.write.Number number = new jxl.write.Number(column_no,row_no,content);
		sheet.addCell(number);
	}

	/*****************************************************
	// import�� ����  �����ϱ� (path����)
	******************************************************/	
	public boolean delFilename(String filepath) 
	{
		File FN = new File(filepath);
		if(FN.exists()) return FN.delete();	
		else return false;
	}
}