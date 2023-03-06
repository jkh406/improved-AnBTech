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

	private String file_path = "";					//저장할 파일path
	private String save_file = "";					//barcode저장파일명
	private String original_file = "";				//barcode사용자보기용 파일명(일련번호시작점)
	private String file_size = "";					//저장파일 크기

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public TextToPDF() 
	{
		
	}

	//*******************************************************************
	//	Text파일을 PDF파일로 만들기
	//*******************************************************************/
	public void changeTextToPDF(MultipartRequest multi,String filepath) throws Exception
	{
		//문서초기화
		com.lowagie.text.Document document = new com.lowagie.text.Document();

		//저장할 파일생성
		String file_name = multi.getFilesystemName("file_name");	//파일이름
		String read_file = filepath+"/"+file_name;					//읽을파일지정

		//확장자 찾기 (xls : excel 그외 : text로 인식)
		String ext_name = file_name.substring(file_name.lastIndexOf(".")+1,file_name.length());

		this.file_path = filepath;									//파일 PATH
		int dot_no = file_name.indexOf(".");	if(dot_no == -1) dot_no=file_name.length();
		String org_file = file_name.substring(0,dot_no);			//original 파일명
		this.save_file = "textToPdf.pdf";							//저장 PDF변환될 default 파일명
		this.original_file = org_file+".pdf";						//PDF변환될 orginal 파일명

		String output_pdf_file = file_path + "/" + save_file; 
		com.lowagie.text.pdf.PdfWriter.getInstance(document,new FileOutputStream(output_pdf_file));

		//HEADER 
		HeaderFooter header = new HeaderFooter(new Phrase(" "), false); 
		header.setBorder(Rectangle.BOTTOM);			//가로사선 아래에만있음 (default:위,아래)
		document.setHeader(header);

		//FOOTER
		HeaderFooter footer = new HeaderFooter(new Phrase("page "), true); 
		footer.setBorder(Rectangle.TOP);			//가로사선 위에만있음 (default:위,아래)
		footer.setAlignment(Element.ALIGN_CENTER);  //해당String정렬 : 가운데
		document.setFooter(footer);

		//문서작성할 파일 열기
		document.open();

		//폰트 정의 (batang.ttc,1 : 바탕체,
		BaseFont bf = BaseFont.createFont("d:/win2k/fonts/batang.ttc,1", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
		Font font = new Font(bf, 10, Font.NORMAL, new Color(0, 0, 0));	//크기:10, Style:NORMAL, 색상:검정

		//파일읽어 PDF로 변환하기
		FileInputStream fin = new FileInputStream(new File(read_file)); 
		BufferedReader d = new BufferedReader(new InputStreamReader(fin));
		String text = "";
		while((text = d.readLine()) != null) {
			text = prs.repWord(text,"	","    ");					//tab 을 space4칸으로 변환
			Paragraph content = new Paragraph(text+"\n", font);
			document.add(content);
		}
		d.close();
		fin.close();	

		//문서작성파일 닫기
		document.close();  
		
		//파일크기 구하기
		File size = new File(output_pdf_file);	
		this.file_size = Double.toString(size.length());
		this.file_size = file_size.substring(0,file_size.indexOf("."));

		//Text File 삭제하기
		File FN = new File(read_file);
		if(FN.exists()) FN.delete();	

	}

	//*******************************************************************
	//	저장파일PATH 구하기
	//*******************************************************************/
	public String getFilePath() throws Exception
	{
		return this.file_path;
	}

	//*******************************************************************
	//	저장파일명 구하기
	//*******************************************************************/
	public String getSaveFile() throws Exception
	{
		return this.save_file;
	}

	//*******************************************************************
	//	변경전 파일명 구하기
	//*******************************************************************/
	public String getOriginalFile() throws Exception
	{
		return this.original_file;
	}
	//*******************************************************************
	//	출력파일 크기 구하기
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
