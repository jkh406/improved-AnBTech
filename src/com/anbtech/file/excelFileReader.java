package com.anbtech.file;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import jxl.*;
import jxl.biff.*;
import jxl.read.biff.*;

public class excelFileReader extends Object 
{
	private String file="";					//���ϸ� (path����)
	private String[][] array;				//���� ���ϳ����� �迭�� ��� 
	private int col_cnt=0;					//���� ������ �� ���δ� ������ ���� 
	private int row_cnt=0;					//���� ������ ���ΰ��� 
	private int sheet_cnt=0;				//Sheet����

	private String encoding = "UTF8";
    private Workbook wb ;
		
	/******************************************************
	// ������ 
	******************************************************/
	public excelFileReader()
	{
		
	}

	/******************************************************
	// ���� ���� (path����)
	******************************************************/
	private void setFileOpen(String file) throws Exception
	{
		this.file = file;
		wb = Workbook.getWorkbook(new File(file));
	}

	/******************************************************
	// ���� �ݱ�
	******************************************************/
	private void setFileClose()
	{
		wb.close();
	}

	/******************************************************
	// Excel�� �ش� Sheet no�� ������ �迭�� ���
	*******************************************************/
	public String[][] readSheetArray() 
	{
		return this.array;
	}
	/******************************************************
	// Sheet���� ���ϱ�
	******************************************************/
	public int getSheetCount() 
	{
		return this.sheet_cnt;
	}
	/******************************************************
	// Row���� ���ϱ�
	******************************************************/
	public int getRowCount() 
	{
		return this.row_cnt;
	}
	/******************************************************
	// Column�� �ִ밹�� ���ϱ�
	******************************************************/
	public int getMaxColumnCount() 
	{
		return this.col_cnt;
	}

	/******************************************************
	// Excel�� �ش� Sheet no�� ������ �迭�� ���
	*******************************************************/
	public void setSheetArray(String file,int SheetNo) throws Exception
	{
		int rc = 0;			//�ش� sheet�� �� row�ǰ���
		int max_cls_cnt=0;	//�ش� row�� �÷��� �ִ� �÷��� ����
		int tmp_cnt = 0;

		//�ش����� open�ϱ�
		setFileOpen(file);

		//row ���� ���ϱ�
		Sheet s = wb.getSheet(SheetNo);
		this.row_cnt = s.getRows();
		
		//�ִ� column���� ���ϱ�
		Cell[] row = null;
		for(int i=0; i<row_cnt; i++) {
			row = s.getRow(i);
			if(row != null) {
				tmp_cnt = row.length; 
				if(tmp_cnt > max_cls_cnt) max_cls_cnt = tmp_cnt;
			}
		}
		this.col_cnt = max_cls_cnt;

		//�迭
		this.array = new String[row_cnt][col_cnt];
		for(int i=0; i<row_cnt; i++) for(int j=0; j<col_cnt; j++) array[i][j]="";

		//�ش������� sheet�� �о� �迭�� ���
		for(int i=0; i<row_cnt; i++) {
			row = s.getRow(i);			//�ش� row�� �д´�.
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
	// Excel�� �ش� Sheet no�� ������ �迭�� ��� : BOM�뵵�� ù�÷��� ��������...
	*******************************************************/
	public void setBomSheetArray(String file,int SheetNo) throws Exception
	{
		int rc = 0;			//�ش� sheet�� �� row�ǰ���
		int max_cls_cnt=0;	//�ش� row�� �÷��� �ִ� �÷��� ����
		int tmp_cnt = 0;

		//�ش����� open�ϱ�
		setFileOpen(file);

		//row ���� ���ϱ�
		Sheet s = wb.getSheet(SheetNo);
		this.row_cnt = s.getRows();
		
		//�ִ� column���� ���ϱ�
		Cell[] row = null;
		for(int i=0; i<row_cnt; i++) {
			row = s.getRow(i);
			if(row != null) {
				tmp_cnt = row.length; 
				if(tmp_cnt > max_cls_cnt) max_cls_cnt = tmp_cnt;
			}
		}
		this.col_cnt = max_cls_cnt+1;

		//�迭
		this.array = new String[row_cnt][col_cnt];
		for(int i=0; i<row_cnt; i++) for(int j=0; j<col_cnt; j++) array[i][j]="";

		//�ش������� sheet�� �о� �迭�� ���
		for(int i=0; i<row_cnt; i++) {
			row = s.getRow(i);			//�ش� row�� �д´�.
			tmp_cnt = row.length;
			for(int j=0; row!=null && j<tmp_cnt; j++) {
				array[i][j] = row[j].getContents();
				//System.out.print(array[i][j]+", ");
			}
			//System.out.print("\n");
		}

		//��ĭ�� ��� shift�� : ù��°�� ���������Ͽ� BOM���� Level No�� �Է��ϱ�����
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
	// Sheet���� ���ϱ�
	******************************************************/
	public int getSheetCount(String file) throws Exception
	{
		setFileOpen(file);
		int sheet_cnt = wb.getNumberOfSheets();
		setFileClose();

		return sheet_cnt;
	}

	/******************************************************
	// Sheet�� ���ϱ�
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
	// Row���� ���ϱ�
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
	// Column�� �ִ밹�� ���ϱ�
	******************************************************/
	public int getMaxColumnCount(String file,int SheetNo) throws Exception
	{
		int rc = 0;			//�ش� sheet�� �� row�ǰ���
		int max_cls_cnt=0;	//�ش� row�� �÷��� �ִ� �÷��� ����
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
	// import�� ����  �����ϱ� (path����)
	******************************************************/	
	public boolean delFilename(String filepath)
	{
		File FN = new File(filepath);
		if(FN.exists()) return FN.delete();	
		else return false;
	}

}