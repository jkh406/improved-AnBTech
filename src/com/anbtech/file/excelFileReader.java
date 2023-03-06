package com.anbtech.file;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import jxl.*;
import jxl.biff.*;
import jxl.read.biff.*;

public class excelFileReader extends Object 
{
	private String file="";					//파일명 (path포함)
	private String[][] array;				//읽은 파일내용을 배열에 담기 
	private int col_cnt=0;					//읽은 파일의 한 라인당 데이터 갯수 
	private int row_cnt=0;					//읽은 파일의 라인갯수 
	private int sheet_cnt=0;				//Sheet갯수

	private String encoding = "UTF8";
    private Workbook wb ;
		
	/******************************************************
	// 생성자 
	******************************************************/
	public excelFileReader()
	{
		
	}

	/******************************************************
	// 파일 열기 (path포함)
	******************************************************/
	private void setFileOpen(String file) throws Exception
	{
		this.file = file;
		wb = Workbook.getWorkbook(new File(file));
	}

	/******************************************************
	// 파일 닫기
	******************************************************/
	private void setFileClose()
	{
		wb.close();
	}

	/******************************************************
	// Excel의 해당 Sheet no의 내용을 배열에 담기
	*******************************************************/
	public String[][] readSheetArray() 
	{
		return this.array;
	}
	/******************************************************
	// Sheet수량 구하기
	******************************************************/
	public int getSheetCount() 
	{
		return this.sheet_cnt;
	}
	/******************************************************
	// Row수량 구하기
	******************************************************/
	public int getRowCount() 
	{
		return this.row_cnt;
	}
	/******************************************************
	// Column중 최대갯수 구하기
	******************************************************/
	public int getMaxColumnCount() 
	{
		return this.col_cnt;
	}

	/******************************************************
	// Excel의 해당 Sheet no의 내용을 배열에 담기
	*******************************************************/
	public void setSheetArray(String file,int SheetNo) throws Exception
	{
		int rc = 0;			//해당 sheet의 총 row의갯수
		int max_cls_cnt=0;	//해당 row중 컬럼의 최대 컬럼의 갯수
		int tmp_cnt = 0;

		//해당파일 open하기
		setFileOpen(file);

		//row 갯수 구하기
		Sheet s = wb.getSheet(SheetNo);
		this.row_cnt = s.getRows();
		
		//최대 column갯수 구하기
		Cell[] row = null;
		for(int i=0; i<row_cnt; i++) {
			row = s.getRow(i);
			if(row != null) {
				tmp_cnt = row.length; 
				if(tmp_cnt > max_cls_cnt) max_cls_cnt = tmp_cnt;
			}
		}
		this.col_cnt = max_cls_cnt;

		//배열
		this.array = new String[row_cnt][col_cnt];
		for(int i=0; i<row_cnt; i++) for(int j=0; j<col_cnt; j++) array[i][j]="";

		//해당파일의 sheet을 읽어 배열에 담기
		for(int i=0; i<row_cnt; i++) {
			row = s.getRow(i);			//해당 row을 읽는다.
			tmp_cnt = row.length;
			for(int j=0; row!=null && j<tmp_cnt; j++) {
				array[i][j] = row[j].getContents();
				//System.out.print(array[i][j]+", ");
			}
			//System.out.print("\n");
		}

		setFileClose();

	}

	/******************************************************
	// Excel의 해당 Sheet no의 내용을 배열에 담기 : BOM용도로 첫컬럼은 공백으로...
	*******************************************************/
	public void setBomSheetArray(String file,int SheetNo) throws Exception
	{
		int rc = 0;			//해당 sheet의 총 row의갯수
		int max_cls_cnt=0;	//해당 row중 컬럼의 최대 컬럼의 갯수
		int tmp_cnt = 0;

		//해당파일 open하기
		setFileOpen(file);

		//row 갯수 구하기
		Sheet s = wb.getSheet(SheetNo);
		this.row_cnt = s.getRows();
		
		//최대 column갯수 구하기
		Cell[] row = null;
		for(int i=0; i<row_cnt; i++) {
			row = s.getRow(i);
			if(row != null) {
				tmp_cnt = row.length; 
				if(tmp_cnt > max_cls_cnt) max_cls_cnt = tmp_cnt;
			}
		}
		this.col_cnt = max_cls_cnt+1;

		//배열
		this.array = new String[row_cnt][col_cnt];
		for(int i=0; i<row_cnt; i++) for(int j=0; j<col_cnt; j++) array[i][j]="";

		//해당파일의 sheet을 읽어 배열에 담기
		for(int i=0; i<row_cnt; i++) {
			row = s.getRow(i);			//해당 row을 읽는다.
			tmp_cnt = row.length;
			for(int j=0; row!=null && j<tmp_cnt; j++) {
				array[i][j] = row[j].getContents();
				//System.out.print(array[i][j]+", ");
			}
			//System.out.print("\n");
		}

		//한칸씩 우로 shift함 : 첫번째는 공란으로하여 BOM상의 Level No을 입력하기위해
		for(int i=0; i<row_cnt; i++) {
			for(int j=col_cnt-1; j>0; j--) {
				array[i][j] = array[i][j-1];
				System.out.print(i+":"+j+" = "+array[i][j]+", ");
			}
			array[i][0] = "";
			System.out.print(i+":"+"0"+" = "+array[i][0]+"\n");
		}

		setFileClose();

	}

	/******************************************************
	// Sheet수량 구하기
	******************************************************/
	public int getSheetCount(String file) throws Exception
	{
		setFileOpen(file);
		int sheet_cnt = wb.getNumberOfSheets();
		setFileClose();

		return sheet_cnt;
	}

	/******************************************************
	// Sheet명 구하기
	*****************************************************/
	public String getSheetName(String file,int SheetNo) throws Exception
	{
		setFileOpen(file);
		Sheet s = wb.getSheet(SheetNo);
		String sname = s.getName();
		setFileClose();

		return sname;
	}

	/******************************************************
	// Row수량 구하기
	******************************************************/
	public int getRowCount(String file,int SheetNo) throws Exception
	{
		setFileOpen(file);
		Sheet s = wb.getSheet(SheetNo);
		int row_cnt = s.getRows();
		setFileClose();

		return row_cnt;
	}

	/******************************************************
	// Column중 최대갯수 구하기
	******************************************************/
	public int getMaxColumnCount(String file,int SheetNo) throws Exception
	{
		int rc = 0;			//해당 sheet의 총 row의갯수
		int max_cls_cnt=0;	//해당 row중 컬럼의 최대 컬럼의 갯수
		int tmp_cnt = 0;

		setFileOpen(file);
		Sheet s = wb.getSheet(SheetNo);
		rc = s.getRows();
		
		Cell[] row = null;
		for(int i=0; i<rc; i++) {
			row = s.getRow(i);
			if(row != null) {
				tmp_cnt = row.length; 
				if(tmp_cnt > max_cls_cnt) max_cls_cnt = tmp_cnt;
			}
		}

		setFileClose();

		return max_cls_cnt;
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