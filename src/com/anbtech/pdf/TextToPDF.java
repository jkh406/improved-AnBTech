package com.anbtech.pdf;
import java.io.*;
import java.io.File;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Point;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;
import java.util.*;


public class TextToPDF
{
	private com.anbtech.text.StringProcess prs = new com.anbtech.text.StringProcess();

	private String file_path = "";					//������ ����path
	private String save_file = "";					//barcode�������ϸ�
	private String original_file = "";				//barcode����ں���� ���ϸ�(�Ϸù�ȣ������)
	private String file_size = "";					//�������� ũ��

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public TextToPDF() 
	{
		
	}

	//*******************************************************************
	//	Text������ PDF���Ϸ� �����
	//*******************************************************************/
	public void changeTextToPDF(MultipartRequest multi,String filepath) throws Exception
	{
		//�����ʱ�ȭ
		com.lowagie.text.Document document = new com.lowagie.text.Document();

		//������ ���ϻ���
		String file_name = multi.getFilesystemName("file_name");	//�����̸�
		String read_file = filepath+"/"+file_name;					//������������

		//Ȯ���� ã�� (xls : excel �׿� : text�� �ν�)
		String ext_name = file_name.substring(file_name.lastIndexOf(".")+1,file_name.length());

		this.file_path = filepath;									//���� PATH
		int dot_no = file_name.indexOf(".");	if(dot_no == -1) dot_no=file_name.length();
		String org_file = file_name.substring(0,dot_no);			//original ���ϸ�
		this.save_file = "textToPdf.pdf";							//���� PDF��ȯ�� default ���ϸ�
		this.original_file = org_file+".pdf";						//PDF��ȯ�� orginal ���ϸ�

		String output_pdf_file = file_path + "/" + save_file; 
		com.lowagie.text.pdf.PdfWriter.getInstance(document,new FileOutputStream(output_pdf_file));

		//HEADER 
		HeaderFooter header = new HeaderFooter(new Phrase(" "), false); 
		header.setBorder(Rectangle.BOTTOM);			//���λ缱 �Ʒ��������� (default:��,�Ʒ�)
		document.setHeader(header);

		//FOOTER
		HeaderFooter footer = new HeaderFooter(new Phrase("page "), true); 
		footer.setBorder(Rectangle.TOP);			//���λ缱 ���������� (default:��,�Ʒ�)
		footer.setAlignment(Element.ALIGN_CENTER);  //�ش�String���� : ���
		document.setFooter(footer);

		//�����ۼ��� ���� ����
		document.open();

		//��Ʈ ���� (batang.ttc,1 : ����ü,
		BaseFont bf = BaseFont.createFont("d:/win2k/fonts/batang.ttc,1", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
		Font font = new Font(bf, 10, Font.NORMAL, new Color(0, 0, 0));	//ũ��:10, Style:NORMAL, ����:����

		//�����о� PDF�� ��ȯ�ϱ�
		FileInputStream fin = new FileInputStream(new File(read_file)); 
		BufferedReader d = new BufferedReader(new InputStreamReader(fin));
		String text = "";
		while((text = d.readLine()) != null) {
			text = prs.repWord(text,"	","    ");					//tab �� space4ĭ���� ��ȯ
			Paragraph content = new Paragraph(text+"\n", font);
			document.add(content);
		}
		d.close();
		fin.close();	

		//�����ۼ����� �ݱ�
		document.close();  
		
		//����ũ�� ���ϱ�
		File size = new File(output_pdf_file);	
		this.file_size = Double.toString(size.length());
		this.file_size = file_size.substring(0,file_size.indexOf("."));

		//Text File �����ϱ�
		File FN = new File(read_file);
		if(FN.exists()) FN.delete();	

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
	//	������ ���ϸ� ���ϱ�
	//*******************************************************************/
	public String getOriginalFile() throws Exception
	{
		return this.original_file;
	}
	//*******************************************************************
	//	������� ũ�� ���ϱ�
	//*******************************************************************/
	public String getFileSize() throws Exception
	{
		return this.file_size;
	}

	//*******************************************************************
	// main
	//*******************************************************************
	public static void main(String args[]) throws Exception
	{
		TextToPDF app = new TextToPDF();
		try{
			
		}catch (Exception e) {
			System.out.println(e);
		}
	}

	  /*------------------------- end --------------------------*/
}
