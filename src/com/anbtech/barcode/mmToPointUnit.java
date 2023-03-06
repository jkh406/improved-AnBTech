package com.anbtech.barcode;
import java.io.*;
import java.io.File;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Point;
import java.text.DecimalFormat;


public class mmToPointUnit
{
	private com.anbtech.text.StringProcess prs = new com.anbtech.text.StringProcess();

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public mmToPointUnit() 
	{
		
	}

	//*******************************************************************
	//	�����ٲٱ� (mm --> point) :PDF������ ũ������� point��
	//*******************************************************************/
	public int changeMMtoPOINT(double mm) throws Exception
	{
		DecimalFormat fmt = new DecimalFormat("0");
		double point = 0;

		point = (mm / 25.4) * 72;
		
		return Integer.parseInt(fmt.format(point));
	}

	//*******************************************************************
	//	�����ٲٱ� (point --> mm) :PDF������ ũ������� point��
	//*******************************************************************/
	public int changePOINTtoMM(double point) throws Exception
	{
		DecimalFormat fmt = new DecimalFormat("0");
		double mm = 0;

		mm = (point * 25.4) / 72;
		
		return Integer.parseInt(fmt.format(mm));
	}

}
