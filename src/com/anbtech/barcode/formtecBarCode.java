package com.anbtech.barcode;
import java.io.*;
import java.io.File;
import java.util.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Point;
import java.text.DecimalFormat;

import com.anbtech.report.module.itext.*;


public class formtecBarCode
{
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//�����Է�
	private com.anbtech.text.StringProcess prs = new com.anbtech.text.StringProcess();			//���ڿ�
	private com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat();			//����
	private com.anbtech.barcode.mmToPointUnit unit = new com.anbtech.barcode.mmToPointUnit();	//PDF����
	private Document document = null;				//Document object
	private PdfWriter writer = null;				//writer object
	private PdfContentByte cb = null;				//pdfcontentbyte object
	private Font TITLEfont = null;					//��ǰ�� �ۼ� ��Ʈ
	private Font SNfont = null;						//Serial No �ۼ� ��Ʈ

	private String file_path = "";					//������ ����path
	private String save_file = "";					//barcode�������ϸ�
	private String original_file = "";				//barcode����ں���� ���ϸ�(�Ϸù�ȣ������)
	private String file_size = "";					//�������� ũ��
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public formtecBarCode() 
	{
		
	}

	//--------------------------------------------------------------------
	//
	//		1.Document �԰� ���ϱ�
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	Document LayOut���ϱ� (�԰ݿ��� �� �¿���ϸ���)
	// paper_type : A0~A10,B0~B5  width:���α���(mm), height:���α���(mm)
	// left_margin:�¿���,right_margin:�쿩��,upper_margin:��ܿ���,lower_margin:�ϴܿ��� (����:mm)
	//*******************************************************************/
	public void setLayOutDocument(String paper_type,String width,String height,
		String left_margin,String right_margin,String upper_margin,String lower_margin) throws Exception
	{
		Rectangle pageSize = null;

		//mm������ point�� �ٲٱ�
		int lm_point = unit.changeMMtoPOINT(Double.parseDouble(left_margin));
		int rm_point = unit.changeMMtoPOINT(Double.parseDouble(right_margin));
		int um_point = unit.changeMMtoPOINT(Double.parseDouble(upper_margin));
		int dm_point = unit.changeMMtoPOINT(Double.parseDouble(lower_margin));
		
		//���� Type
		if(paper_type.length() != 0) pageSize = setLayOutFormPaper(paper_type);	//�԰ݿ���
		else pageSize =setLayOutRawPaper(width, height);						//��԰ݿ���

		//����� ��Ʈ ���� (batang.ttc,1 : ����ü,
		//font_name�� �־��� True Type�ѱ���Ʈ ã��
		String font_name = "����ü";
		String true_type_font="";
		HashMap hm = new HashMap();
		BaseFontFactory bff = new BaseFontFactory();
		hm = bff.getHanguelFont();										//�ѱ���Ʈ �б�
		Iterator it = hm.keySet().iterator();
        while (it.hasNext()) {
              String fontName = (String) it.next();
			  if(font_name.equals(fontName)) true_type_font = hm.get(fontName).toString();
        }

		//��Ʈ ũ�� ����
		BaseFont bf = BaseFont.createFont(true_type_font,BaseFont.IDENTITY_H,BaseFont.EMBEDDED); 
		//BaseFont bf = BaseFont.createFont("d:/win2k/fonts/batang.ttc,1", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
		TITLEfont = new Font(bf, 10, Font.NORMAL, new Color(0, 0, 0));	//��ǰ��:ũ��:10, Style:NORMAL, ����:����
		SNfont = new Font(bf, 8, Font.NORMAL, new Color(0, 0, 0));		//�Ϸù�ȣ:ũ��:8, Style:NORMAL, ����:����
		

		document = new Document(pageSize,lm_point,rm_point,um_point,dm_point);
	}

	//*******************************************************************
	//	�԰ݿ��� LayOut���ϱ� (�԰ݿ��� �� �¿���ϸ���)
	//*******************************************************************/
	public Rectangle setLayOutFormPaper(String paper_type) throws Exception
	{
		Rectangle pageSize = null;

		if(paper_type.equals("A0")) pageSize = PageSize.A0;
		else if(paper_type.equals("A1")) pageSize = PageSize.A1;
		else if(paper_type.equals("A2")) pageSize = PageSize.A2;
		else if(paper_type.equals("A3")) pageSize = PageSize.A3;
		else if(paper_type.equals("A4")) pageSize = PageSize.A4;
		else if(paper_type.equals("A5")) pageSize = PageSize.A5;
		else if(paper_type.equals("A6")) pageSize = PageSize.A6;
		else if(paper_type.equals("A7")) pageSize = PageSize.A7;
		else if(paper_type.equals("A8")) pageSize = PageSize.A8;
		else if(paper_type.equals("A9")) pageSize = PageSize.A9;
		else if(paper_type.equals("A10")) pageSize = PageSize.A10;
		else if(paper_type.equals("B0")) pageSize = PageSize.B0;
		else if(paper_type.equals("B1")) pageSize = PageSize.B1;
		else if(paper_type.equals("B2")) pageSize = PageSize.B2;
		else if(paper_type.equals("B3")) pageSize = PageSize.B3;
		else if(paper_type.equals("B4")) pageSize = PageSize.B4;
		else if(paper_type.equals("B5")) pageSize = PageSize.B5;

		return pageSize;
	}
	//*******************************************************************
	//	��԰ݿ��� LayOut���ϱ� (�԰ݿ��� �� �¿���ϸ���)
	//*******************************************************************/
	public Rectangle setLayOutRawPaper(String width, String height) throws Exception
	{
		int width_point = unit.changeMMtoPOINT(Double.parseDouble(width));
		int width_height = unit.changeMMtoPOINT(Double.parseDouble(height));

		Rectangle pageSize = new Rectangle(width_point,width_height);
		return pageSize;
	}

	//--------------------------------------------------------------------
	//
	//		2.Writer Object ���ϱ� (������ ��������)
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	writer object���� �� �������ϸ� ���ϱ�
	//*******************************************************************/
	public void setSaveFile(String upload_path, String login_id) throws Exception
	{
		//������ ���ϸ�
		this.file_path = upload_path+"/barcode/"+login_id;		//���� PATH
		//this.save_file = anbdt.getID()+".pdf";				//�������ϸ�
		this.save_file = "barcode.pdf";							//default ���ϸ�

		String file_name = file_path + "/" + save_file;

		// ���丮�� �̹� �ִ��� ??  ������ ���� ����   
		File dir = new File(file_path); 
    	if (!dir.exists()) dir.mkdirs();

		writer = PdfWriter.getInstance(document, new FileOutputStream(file_name));
	}

	//*******************************************************************
	//	��������PATH ���ϱ�
	//*******************************************************************/
	public String getFilePath() throws Exception
	{
		return this.file_path;
	}

	//*******************************************************************
	//	�������ϸ� ���ϱ�
	//*******************************************************************/
	public String getSaveFile() throws Exception
	{
		return this.save_file;
	}
	//*******************************************************************
	//	������� ũ�� ���ϱ�
	//*******************************************************************/
	public String getFileSize() throws Exception
	{
		return this.file_size;
	}

	//--------------------------------------------------------------------
	//
	//		3.PdfContentByte Object ���ϱ� (������ �׸����� �׸�)
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	������ �׸����� �׸� ���ϱ�
	//*******************************************************************/
	public void setPdfContentByte() throws Exception
	{
		cb = writer.getDirectContent();		//������� ����
	}

	//--------------------------------------------------------------------
	//
	//		4. Type�� ���ڵ�����ϱ�
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	ENA13���ڵ� ���� : 13�ڸ����ڸ� ���� [12�ڸ��Է°�, 1�ڸ������ڵ尪]
	//  [�� �����̿��� �������Թ� �ڸ����� 13���ϸ� Error, �̻��̸� ������]
	//*******************************************************************/
	public Image createEAN13(String barcode) throws Exception
	{
		String check_sum = getEANCheckSum(barcode);	//check sum ���ϱ�
		barcode = barcode + check_sum;

		BarcodeEAN codeEAN = new BarcodeEAN();
        codeEAN.setCodeType(Barcode.EAN13);				//EAN13���ڵ� Ÿ��
        codeEAN.setCode(barcode);					
        Image imageEAN = codeEAN.createImageWithBarcode(cb, null, null);
		imageEAN.scalePercent(100,100);					//�̹��� ũ�����(����%, ����%)
		return imageEAN;
	}

	//*******************************************************************
	//	ENA8���ڵ� ���� : 8�ڸ����ڸ� ���� [7�ڸ��Է°�, 1�ڸ������ڵ尪]
	//  [�� �����̿��� �������Թ� �ڸ����� 8���ϸ� Error, �̻��̸� ������]
	//*******************************************************************/
	public Image createEAN8(String barcode) throws Exception
	{
		String check_sum = getEANCheckSum(barcode);	//check sum ���ϱ�
		barcode = barcode + check_sum;

		BarcodeEAN codeEAN = new BarcodeEAN();
        codeEAN.setCodeType(Barcode.EAN8);			//EAN8 ���ڵ� Ÿ��
        codeEAN.setCode(barcode);					
        Image imageEAN = codeEAN.createImageWithBarcode(cb, null, null);
		imageEAN.scalePercent(100,100);				//�̹��� ũ�����(����%, ����%)
		return imageEAN;
	}
	//*******************************************************************
	//	UPCA���ڵ� ���� : 12�ڸ����ڸ� ���� [11�ڸ��Է°�, 1�ڸ� �����ڵ尪���� �ڵ�������]
	//  [�� �����̿��� �������Թ� �ڸ����� 11���ϸ� Error, 11�ڸ��� �Է��ؾ� �����������ڵ带 �ڵ�������]
	//*******************************************************************/
	public Image createUPCA(String barcode) throws Exception
	{
		String check_sum = getEANCheckSum(barcode);	//check sum ���ϱ�
		barcode = barcode + check_sum;

		BarcodeEAN codeUPCA = new BarcodeEAN();
        codeUPCA.setCodeType(Barcode.UPCA);			//UPCA ���ڵ� Ÿ��
        codeUPCA.setCode(barcode);		
        Image imageUPCA = codeUPCA.createImageWithBarcode(cb, null, null);	
		imageUPCA.scalePercent(100,100);			//�̹��� ũ�����(����%, ����%)
		return imageUPCA;
	}
	//*******************************************************************
	//	CODE128���ڵ� ���� : 140�ڸ�����+���ڸ� ���� [138�ڸ��Է°�, 12�ڸ� ��Ʈ�� �ڵ�������]
	//*******************************************************************/
	public Image createCODE128(String barcode) throws Exception
	{
		Barcode128 code128 = new Barcode128();
		code128.setCode(barcode);		
        Image imageCODE128 = code128.createImageWithBarcode(cb, null, null);
		imageCODE128.scalePercent(100,100);					//�̹��� ũ�����(����%, ����%)
		return imageCODE128;
	}

	//--------------------------------------------------------------------
	//
	//		5. ǥ�� EAN ���ڵ�ü���� Check sum ���ϱ�
	//         ǥ�� EAN13 : 12�ڸ� + 1(C/D)
	//         ��� EAN8  :  7�ڸ� + 1(C/D)
	//---------------------------------------------------------------------
	//*******************************************************************
	//	ENA ���ڵ�� check sum ���ϱ�
	//*******************************************************************/
	public String getEANCheckSum(String barcode) throws Exception
	{
		String cd = "0";				//checksum��
		int len = barcode.length();		//barcode ���� ���ϱ�
		int odd_sum = 0;				//Ȧ����° ����(����:CD���� 1, ....)
		int even_sum = 0;				//¦����° ����
		int sum = 0;					//�հ�

		//1�ܰ� : ¦����°, Ȧ����° �� ���ϱ� (CD�� ���� �ǿ����ʰ��� ó���д°����� ¦���� ��)
		for(int i=len-1,j=2; i >= 0; i--,j++) {
			if(j % 2 == 0) even_sum += Integer.parseInt(""+barcode.charAt(i));	//¦����
			else odd_sum += Integer.parseInt(""+barcode.charAt(i));				//Ȧ����
		}
		even_sum = even_sum * 3;							//2�ܰ� : ¦���� * 3
		sum = even_sum + odd_sum;							//3�ܰ� : �μ� ���ϱ�
		cd = Integer.toString((10 - (sum % 10)) % 10);		//4�ܰ� : sum�� 10�� ����� �� ���ڸ� ���Ѵ�.

		return cd;
	}

	//--------------------------------------------------------------------
	//
	//		0. ���ڵ� �����ϱ�
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	ENA13���ڵ� ���ؿ���60��/A4 ���� [�Է¹��ڵ尪 : �� ���ڵ庰 ������..]
	//  paper_type : ǥ�ؿ���, ��ǥ�ؿ����� width:����, height:����
	//  left_margin : ������������, right_margin, upper_margin, lower_margin
	//	upload_path : ��µ� �����н�, login_in : ����ڹ�ȣ
	//	barcode : 12�ڸ� ���ڵ�, barcode_count : ������ ����
	//	column_count : ���ΰ���, row_count : ���ΰ���, used_count : ���ؿ��� ���������
	//	goods_name : ��ǰ��, serial_no : �ø����ȣ ���۹�ȣ(12�ڸ�)
	//*******************************************************************/
	public void barcodeFORM_60_A4(String upload_path,String login_id,String barcode, String barcode_count,
		String used_count,String goods_name, String serial_no, String barcode_type) throws Exception
	{
		//���ؿ��� A4�� ����� �԰�����
		String paper_type = "A4";			//�԰ݿ��� A4�� 
		String width = "",height="";		//��԰ݿ�����(paper_type�� �ƴҶ�) ���� X ���� ũ�� (����:mm)
		String left_margin="5",right_margin="5",upper_margin="10",lower_margin="10";//���ؿ��� �԰�
		String column_count="5",row_count="12";//���ؿ����� ���� ���� X ���� ����
		if(barcode.length() == 0) return;

		//document ���� (�������������� ���⼭....)
		setLayOutDocument(paper_type,width,height,left_margin,right_margin,upper_margin,lower_margin);
		setSaveFile(upload_path,login_id);		//writer ���� (�������ϸ� ����)
		document.open();						//���� open
		setPdfContentByte();					//�����׸�����

		//���̺�����Ͽ� ��� (���� ���� ���̹� ũ�� ������ ���⼭.... �� ���ڵ庰 ������ ���⼭ ...)
		barcodeTABLE_60_A4(barcode,barcode_count,column_count,row_count,used_count,goods_name,serial_no,barcode_type);

		//���� TEST�� ���
		//barcodeTABLE_60_A4("Ab=$12.3",barcode_count,column_count,row_count,used_count,"CODE128",serial_no,"CODE128");
		//barcodeTABLE_60_A4(barcode,barcode_count,column_count,row_count,used_count,"EAN13",serial_no,"EAN13");
		//barcodeTABLE_60_A4(barcode,barcode_count,column_count,row_count,used_count,"EAN8",serial_no,"EAN8");
		//barcodeTABLE_60_A4(barcode,barcode_count,column_count,row_count,used_count,"UPCA",serial_no,"UPCA");
		
		//���� �ݱ�
		document.close();  

		//����ũ�� ���ϱ�
		File size = new File(file_path+"/"+save_file);	
		this.file_size = Double.toString(size.length());
		this.file_size = file_size.substring(0,file_size.indexOf("."));
		
	}

	//*******************************************************************
	//	TABLE �����Ͽ� �ۼ��ϱ� : ��µ� ���ذ���ũ�� ������ ���⼭ 
	//  ��> ������ .... c1.setLeading(8);	//��ȣ�� ���ڷ� ������������ �ϸ�
	//  barcodeFORM_60_A4() �� ���ӵǾ� ����
	//*******************************************************************/
	public void barcodeTABLE_60_A4(String barcode, String barcode_count,String column_count,String row_count,
		String used_count,String goods_name, String serial_no,String barcode_type) throws Exception
	{
		//�ʱ�ȭ
		int R=0,G=0,B=255;									//����
		nfm.setFormat("00000");								//�����ʱ�ȭ (�Ϸù�ȣ ������)
		int cnt = Integer.parseInt(barcode_count);			//��������
		String bar_body = "";								//�ø����ȣ ������
		String bar_serial = "";								//�ø����ȣ ������

		int col_cnt = Integer.parseInt(column_count);		//table�� �÷�����
		int row_cnt = Integer.parseInt(row_count);			//table�� row ���� : �����ϸ� �ڵ��߰��Ǵ��׸���
		int skip_cnt = Integer.parseInt(used_count);		//table�� skip�� ����

		//�Ϸù�ȣ ����
		if(serial_no.length() >= 12) {
			bar_body = serial_no.substring(0,7);
			bar_serial = serial_no.substring(7,12);
		} 

		//----------------------------------------------------------
		//		BAR �ڵ� �����ϱ� : ���� �ٸ�
		//----------------------------------------------------------
		//Table �����ϱ� (���ؿ����� �µ���)
		Table table = new Table(col_cnt,row_cnt);					//���� ���� * ���� ����� ����
		table.setBorderWidth(1);									//���� Layout���� �β�
		table.setBorderColor(new Color(R,G,B));						//���� Layout ����
		table.setPadding(2);										//���� ���ڰ� ����
		table.setSpacing(0);										//���� ����
        table.setWidth(100);										//�־��� ������ ���ũ�� 100%

		//barcode �����ϱ�
		int n = 0;						//serial no �Ϸù�ȣ �����ʱⰪ, ����� ����
		int s = 1;						//skip�� ��
		//for(int r=0; r<row_cnt; r++) {
		for(; n<cnt; ) {
				//cell�� �ֱ�
				if(s > skip_cnt) {
					if(serial_no.length() >= 12) serial_no = "S/N :"+bar_body + nfm.toDigits(Integer.parseInt(bar_serial)+n);	//�Ϸù�ȣ����
					else serial_no=" ";

					//��ǰ�� �Է� insertTable�����
					Paragraph Title = new Paragraph(goods_name, TITLEfont);	//��ǰ��
					Table t1=new Table(1);
					Cell c1 = new Cell(Title);
					c1.setBorderColor(new Color(R,G,B));					//cell����
					c1.setLeading(8f);										//cell height
					t1.addCell(c1);
					
					//���ڵ� �Է� insertTable�����
					Table t2=new Table(1);
					//Cell c2 = new Cell(new Phrase(new Chunk(createEAN13(barcode),0,0)));
					Cell c2 = new Cell(selectBarCode(barcode_type,barcode));
					c2.setBorderColor(new Color(R,G,B));					//cell����
					c2.setLeading(33f);										//cell height
					c2.setHorizontalAlignment(Element.ALIGN_CENTER);		//��������
					c2.setVerticalAlignment(Element.ALIGN_BOTTOM);			//��������
					t2.addCell(c2);

					//S/N �Է� insertTable�����
					Paragraph SN = new Paragraph(serial_no, SNfont);		//S/N��ȣ
					Table t3=new Table(1);
					Cell c3 = new Cell(SN);
					c3.setBorderColor(new Color(R,G,B));					//cell����
					c3.setLeading(8f);										//cell height
					t3.addCell(c3);
					
					//�� insertTable�� ���� ���̺� insertTable�����
					//(������ �κ��� �Ʒ��ش������ ������ �ȴ�.)
					Table t4=new Table(1,2);
					t4.insertTable(t1);										//��ǰ�� �Է¿��� (������ ����...)
					t4.insertTable(t2);										//���ڵ� �Է¿���
					t4.insertTable(t3);										//S/N �Է¿���
					
					//��ü������ ���̺� ����ϱ�
					//table.insertTable(t4,new Point(r,c));				
					table.insertTable(t4);	
					
					n++;					//Serial no ������ȣ
				} 
				//cell skip�ϱ�
				else {
					//��ǰ�� �Է°��� insertTable�����
					Table t1=new Table(1);
					Cell c1 = new Cell(new Paragraph("	\n"));
					c1.setBorderColor(new Color(255,255,255));
					c1.setLeading(8f);										//cell height
					t1.addCell(c1);

					//���ڵ� �Է°��� insertTable�����
					Table t2=new Table(1);
					Cell c2 = new Cell("	\n");
					c2.setBorderColor(new Color(255,255,255));
					c2.setLeading(33f);										//cell height
					t2.addCell(c2);

					//S/N �Է°��� insertTable�����
					Table t3=new Table(1);
					Cell c3 = new Cell(new Paragraph("	\n"));
					c3.setBorderColor(new Color(255,255,255));
					c3.setLeading(8f);										//cell height
					t3.addCell(c3);

					//�� insertTable�� ���� ���̺� insertTable�����
					//(������ �κ��� �Ʒ��ش������ ������ �ȴ�.)
					Table t4=new Table(1,2);
					t4.insertTable(t1);										//��ǰ�� �Է¿��� (������ ����...)
					t4.insertTable(t2);										//���ڵ� �Է¿���
					t4.insertTable(t3);										//S/N �Է¿���
					table.insertTable(t4);	

					s++;					//skip ����
				}
				if(n > cnt-1) break;		//�䱸������ŭ ����� �����ϱ�
		}	//outter for
		document.add(table);	
	}

	//*******************************************************************
	//	BARCODE TYPE�� ���� ���ڵ� ���� : ������
	//*******************************************************************/
	public Phrase selectBarCode(String barcode_type,String barcode) throws Exception
	{
		Phrase p = null;

		//barcode type�� ������ ���ڵ� �����ϱ�
		//EAN13 (13����) ---> �Է�: 12�ڸ����ڸ� ����, checksum: 1�ڸ� �ڵ�������.
		if(barcode_type.equals("EAN13")) {
			if(barcode.length() > 12) barcode = barcode.substring(0,12);
			p = new Phrase(new Chunk(createEAN13(barcode),0,0));
		} 
		//EAN8 (8����) ---> �Է�: 7�ڸ����ڸ� ����, checksum: 1�ڸ� �ڵ�������.
		else if(barcode_type.equals("EAN8")) { 
			if(barcode.length() > 7) barcode = barcode.substring(0,7);
			p = new Phrase(new Chunk(createEAN8(barcode),0,0));
		}
		//UPCA (11����) ---> �Է�: 11�ڸ����ڸ� ����, checksum: 1�ڸ� �ڵ�������.
		else if(barcode_type.equals("UPCA")) { 
			if(barcode.length() > 11) barcode = barcode.substring(0,11);
			p = new Phrase(new Chunk(createUPCA(barcode),0,0));
		}
		//CODE128 (128 ����+����) ---> �Է�: 128�ڸ����ڸ� ����, control: 12�ڸ� �ڵ�������.
		//��, ��ũ�⸦ ����� ��µ��� ����. 
		else if(barcode_type.equals("CODE128")) { 
			if(barcode.length() > 128) barcode = barcode.substring(0,128);
			p = new Phrase(new Chunk(createCODE128(barcode),0,0));
		}
		
		return p;
	}

}
