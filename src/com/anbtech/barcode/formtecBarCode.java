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
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//일자입력
	private com.anbtech.text.StringProcess prs = new com.anbtech.text.StringProcess();			//문자열
	private com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat();			//포멧
	private com.anbtech.barcode.mmToPointUnit unit = new com.anbtech.barcode.mmToPointUnit();	//PDF단위
	private Document document = null;				//Document object
	private PdfWriter writer = null;				//writer object
	private PdfContentByte cb = null;				//pdfcontentbyte object
	private Font TITLEfont = null;					//상품명 작성 폰트
	private Font SNfont = null;						//Serial No 작성 폰트

	private String file_path = "";					//저장할 파일path
	private String save_file = "";					//barcode저장파일명
	private String original_file = "";				//barcode사용자보기용 파일명(일련번호시작점)
	private String file_size = "";					//저장파일 크기
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public formtecBarCode() 
	{
		
	}

	//--------------------------------------------------------------------
	//
	//		1.Document 규격 정하기
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	Document LayOut정하기 (규격용지 및 좌우상하마진)
	// paper_type : A0~A10,B0~B5  width:가로길이(mm), height:세로길이(mm)
	// left_margin:좌여백,right_margin:우여백,upper_margin:상단여백,lower_margin:하단여백 (단위:mm)
	//*******************************************************************/
	public void setLayOutDocument(String paper_type,String width,String height,
		String left_margin,String right_margin,String upper_margin,String lower_margin) throws Exception
	{
		Rectangle pageSize = null;

		//mm단위를 point로 바꾸기
		int lm_point = unit.changeMMtoPOINT(Double.parseDouble(left_margin));
		int rm_point = unit.changeMMtoPOINT(Double.parseDouble(right_margin));
		int um_point = unit.changeMMtoPOINT(Double.parseDouble(upper_margin));
		int dm_point = unit.changeMMtoPOINT(Double.parseDouble(lower_margin));
		
		//용지 Type
		if(paper_type.length() != 0) pageSize = setLayOutFormPaper(paper_type);	//규격용지
		else pageSize =setLayOutRawPaper(width, height);						//비규격용지

		//사용할 폰트 정의 (batang.ttc,1 : 바탕체,
		//font_name로 주어진 True Type한글폰트 찾기
		String font_name = "바탕체";
		String true_type_font="";
		HashMap hm = new HashMap();
		BaseFontFactory bff = new BaseFontFactory();
		hm = bff.getHanguelFont();										//한글폰트 읽기
		Iterator it = hm.keySet().iterator();
        while (it.hasNext()) {
              String fontName = (String) it.next();
			  if(font_name.equals(fontName)) true_type_font = hm.get(fontName).toString();
        }

		//폰트 크기 조정
		BaseFont bf = BaseFont.createFont(true_type_font,BaseFont.IDENTITY_H,BaseFont.EMBEDDED); 
		//BaseFont bf = BaseFont.createFont("d:/win2k/fonts/batang.ttc,1", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
		TITLEfont = new Font(bf, 10, Font.NORMAL, new Color(0, 0, 0));	//상품명:크기:10, Style:NORMAL, 색상:검정
		SNfont = new Font(bf, 8, Font.NORMAL, new Color(0, 0, 0));		//일련번호:크기:8, Style:NORMAL, 색상:검정
		

		document = new Document(pageSize,lm_point,rm_point,um_point,dm_point);
	}

	//*******************************************************************
	//	규격용지 LayOut정하기 (규격용지 및 좌우상하마진)
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
	//	비규격용지 LayOut정하기 (규격용지 및 좌우상하마진)
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
	//		2.Writer Object 정하기 (저장할 파일포함)
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	writer object생성 및 저장파일명 정하기
	//*******************************************************************/
	public void setSaveFile(String upload_path, String login_id) throws Exception
	{
		//저장할 파일명
		this.file_path = upload_path+"/barcode/"+login_id;		//파일 PATH
		//this.save_file = anbdt.getID()+".pdf";				//저장파일명
		this.save_file = "barcode.pdf";							//default 파일명

		String file_name = file_path + "/" + save_file;

		// 디렉토리가 이미 있는지 ??  없으면 새로 생성   
		File dir = new File(file_path); 
    	if (!dir.exists()) dir.mkdirs();

		writer = PdfWriter.getInstance(document, new FileOutputStream(file_name));
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
	//	출력파일 크기 구하기
	//*******************************************************************/
	public String getFileSize() throws Exception
	{
		return this.file_size;
	}

	//--------------------------------------------------------------------
	//
	//		3.PdfContentByte Object 정하기 (저장할 그림담을 그릇)
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	저장할 그림담을 그릇 구하기
	//*******************************************************************/
	public void setPdfContentByte() throws Exception
	{
		cb = writer.getDirectContent();		//출력파일 정의
	}

	//--------------------------------------------------------------------
	//
	//		4. Type별 바코드생성하기
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	ENA13바코드 생성 : 13자리숫자만 가능 [12자리입력값, 1자리검증코드값]
	//  [단 숫자이외의 문자포함및 자릿수가 13이하면 Error, 이상이면 무시함]
	//*******************************************************************/
	public Image createEAN13(String barcode) throws Exception
	{
		String check_sum = getEANCheckSum(barcode);	//check sum 구하기
		barcode = barcode + check_sum;

		BarcodeEAN codeEAN = new BarcodeEAN();
        codeEAN.setCodeType(Barcode.EAN13);				//EAN13바코드 타입
        codeEAN.setCode(barcode);					
        Image imageEAN = codeEAN.createImageWithBarcode(cb, null, null);
		imageEAN.scalePercent(100,100);					//이미지 크기비율(가로%, 세로%)
		return imageEAN;
	}

	//*******************************************************************
	//	ENA8바코드 생성 : 8자리숫자만 가능 [7자리입력값, 1자리검증코드값]
	//  [단 숫자이외의 문자포함및 자릿수가 8이하면 Error, 이상이면 무시함]
	//*******************************************************************/
	public Image createEAN8(String barcode) throws Exception
	{
		String check_sum = getEANCheckSum(barcode);	//check sum 구하기
		barcode = barcode + check_sum;

		BarcodeEAN codeEAN = new BarcodeEAN();
        codeEAN.setCodeType(Barcode.EAN8);			//EAN8 바코드 타입
        codeEAN.setCode(barcode);					
        Image imageEAN = codeEAN.createImageWithBarcode(cb, null, null);
		imageEAN.scalePercent(100,100);				//이미지 크기비율(가로%, 세로%)
		return imageEAN;
	}
	//*******************************************************************
	//	UPCA바코드 생성 : 12자리숫자만 가능 [11자리입력값, 1자리 검증코드값으로 자동생성됨]
	//  [단 숫자이외의 문자포함및 자릿수가 11이하면 Error, 11자리만 입력해야 마지막검증코드를 자동생성함]
	//*******************************************************************/
	public Image createUPCA(String barcode) throws Exception
	{
		String check_sum = getEANCheckSum(barcode);	//check sum 구하기
		barcode = barcode + check_sum;

		BarcodeEAN codeUPCA = new BarcodeEAN();
        codeUPCA.setCodeType(Barcode.UPCA);			//UPCA 바코드 타입
        codeUPCA.setCode(barcode);		
        Image imageUPCA = codeUPCA.createImageWithBarcode(cb, null, null);	
		imageUPCA.scalePercent(100,100);			//이미지 크기비율(가로%, 세로%)
		return imageUPCA;
	}
	//*******************************************************************
	//	CODE128바코드 생성 : 140자리영문+숫자만 가능 [138자리입력값, 12자리 컨트롤 자동생성됨]
	//*******************************************************************/
	public Image createCODE128(String barcode) throws Exception
	{
		Barcode128 code128 = new Barcode128();
		code128.setCode(barcode);		
        Image imageCODE128 = code128.createImageWithBarcode(cb, null, null);
		imageCODE128.scalePercent(100,100);					//이미지 크기비율(가로%, 세로%)
		return imageCODE128;
	}

	//--------------------------------------------------------------------
	//
	//		5. 표준 EAN 바코드체계의 Check sum 구하기
	//         표준 EAN13 : 12자리 + 1(C/D)
	//         축소 EAN8  :  7자리 + 1(C/D)
	//---------------------------------------------------------------------
	//*******************************************************************
	//	ENA 바코드용 check sum 구하기
	//*******************************************************************/
	public String getEANCheckSum(String barcode) throws Exception
	{
		String cd = "0";				//checksum값
		int len = barcode.length();		//barcode 길이 구하기
		int odd_sum = 0;				//홀수번째 총합(순서:CD부터 1, ....)
		int even_sum = 0;				//짝수번째 총합
		int sum = 0;					//합계

		//1단계 : 짝수번째, 홀수번째 값 구하기 (CD을 빼면 맨오른쪽값이 처음읽는값으로 짝수가 됨)
		for(int i=len-1,j=2; i >= 0; i--,j++) {
			if(j % 2 == 0) even_sum += Integer.parseInt(""+barcode.charAt(i));	//짝수합
			else odd_sum += Integer.parseInt(""+barcode.charAt(i));				//홀수합
		}
		even_sum = even_sum * 3;							//2단계 : 짝수합 * 3
		sum = even_sum + odd_sum;							//3단계 : 두수 더하기
		cd = Integer.toString((10 - (sum % 10)) % 10);		//4단계 : sum이 10의 배수가 될 숫자를 구한다.

		return cd;
	}

	//--------------------------------------------------------------------
	//
	//		0. 바코드 생성하기
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	ENA13바코드 폼텍용지60용/A4 생성 [입력바코드값 : 각 바코드별 별도로..]
	//  paper_type : 표준용지, 비표준용지시 width:가로, height:세로
	//  left_margin : 용지좌측여백, right_margin, upper_margin, lower_margin
	//	upload_path : 출력될 파일패스, login_in : 사용자번호
	//	barcode : 12자리 바코드, barcode_count : 생성할 갯수
	//	column_count : 가로갯수, row_count : 세로갯수, used_count : 폼텍용지 빈공란갯수
	//	goods_name : 상품명, serial_no : 시리얼번호 시작번호(12자리)
	//*******************************************************************/
	public void barcodeFORM_60_A4(String upload_path,String login_id,String barcode, String barcode_count,
		String used_count,String goods_name, String serial_no, String barcode_type) throws Exception
	{
		//폼텍용지 A4용 양식지 규격정의
		String paper_type = "A4";			//규격용지 A4용 
		String width = "",height="";		//비규격용지시(paper_type이 아닐때) 가로 X 세로 크기 (단위:mm)
		String left_margin="5",right_margin="5",upper_margin="10",lower_margin="10";//폼텍용지 규격
		String column_count="5",row_count="12";//폼텍용지내 개별 가로 X 세로 갯수
		if(barcode.length() == 0) return;

		//document 생성 (용지여백조정은 여기서....)
		setLayOutDocument(paper_type,width,height,left_margin,right_margin,upper_margin,lower_margin);
		setSaveFile(upload_path,login_id);		//writer 생성 (저장파일명 생성)
		document.open();						//문서 open
		setPdfContentByte();					//담을그릇생성

		//테이블생성하여 담기 (폼텍 개별 높이및 크기 조정은 여기서.... 또 바코드별 조정도 여기서 ...)
		barcodeTABLE_60_A4(barcode,barcode_count,column_count,row_count,used_count,goods_name,serial_no,barcode_type);

		//개별 TEST용 출력
		//barcodeTABLE_60_A4("Ab=$12.3",barcode_count,column_count,row_count,used_count,"CODE128",serial_no,"CODE128");
		//barcodeTABLE_60_A4(barcode,barcode_count,column_count,row_count,used_count,"EAN13",serial_no,"EAN13");
		//barcodeTABLE_60_A4(barcode,barcode_count,column_count,row_count,used_count,"EAN8",serial_no,"EAN8");
		//barcodeTABLE_60_A4(barcode,barcode_count,column_count,row_count,used_count,"UPCA",serial_no,"UPCA");
		
		//문서 닫기
		document.close();  

		//파일크기 구하기
		File size = new File(file_path+"/"+save_file);	
		this.file_size = Double.toString(size.length());
		this.file_size = file_size.substring(0,file_size.indexOf("."));
		
	}

	//*******************************************************************
	//	TABLE 생성하여 작성하기 : 출력될 폼텍개별크기 조절은 여기서 
	//  예> 조정은 .... c1.setLeading(8);	//갈호안 숫자로 셀높이조절만 하면
	//  barcodeFORM_60_A4() 에 종속되어 사용됨
	//*******************************************************************/
	public void barcodeTABLE_60_A4(String barcode, String barcode_count,String column_count,String row_count,
		String used_count,String goods_name, String serial_no,String barcode_type) throws Exception
	{
		//초기화
		int R=0,G=0,B=255;									//색상
		nfm.setFormat("00000");								//포멧초기화 (일련번호 생성용)
		int cnt = Integer.parseInt(barcode_count);			//생성갯수
		String bar_body = "";								//시리얼번호 고정값
		String bar_serial = "";								//시리얼번호 변동값

		int col_cnt = Integer.parseInt(column_count);		//table의 컬럼갯수
		int row_cnt = Integer.parseInt(row_count);			//table의 row 갯수 : 부족하면 자동추가되는항목임
		int skip_cnt = Integer.parseInt(used_count);		//table의 skip할 갯수

		//일련번호 정의
		if(serial_no.length() >= 12) {
			bar_body = serial_no.substring(0,7);
			bar_serial = serial_no.substring(7,12);
		} 

		//----------------------------------------------------------
		//		BAR 코드 생성하기 : 각기 다름
		//----------------------------------------------------------
		//Table 생성하기 (폼텍용지에 맞도록)
		Table table = new Table(col_cnt,row_cnt);					//용지 가로 * 세로 출력할 수량
		table.setBorderWidth(1);									//용지 Layout보더 두께
		table.setBorderColor(new Color(R,G,B));						//용지 Layout 색상
		table.setPadding(2);										//셀과 글자간 간격
		table.setSpacing(0);										//셀간 간격
        table.setWidth(100);										//주어진 용지에 출력크기 100%

		//barcode 생성하기
		int n = 0;						//serial no 일련번호 증가초기값, 출력할 갯수
		int s = 1;						//skip할 값
		//for(int r=0; r<row_cnt; r++) {
		for(; n<cnt; ) {
				//cell에 넣기
				if(s > skip_cnt) {
					if(serial_no.length() >= 12) serial_no = "S/N :"+bar_body + nfm.toDigits(Integer.parseInt(bar_serial)+n);	//일련번호증가
					else serial_no=" ";

					//상품명 입력 insertTable만들기
					Paragraph Title = new Paragraph(goods_name, TITLEfont);	//상품명
					Table t1=new Table(1);
					Cell c1 = new Cell(Title);
					c1.setBorderColor(new Color(R,G,B));					//cell색상
					c1.setLeading(8f);										//cell height
					t1.addCell(c1);
					
					//바코드 입력 insertTable만들기
					Table t2=new Table(1);
					//Cell c2 = new Cell(new Phrase(new Chunk(createEAN13(barcode),0,0)));
					Cell c2 = new Cell(selectBarCode(barcode_type,barcode));
					c2.setBorderColor(new Color(R,G,B));					//cell색상
					c2.setLeading(33f);										//cell height
					c2.setHorizontalAlignment(Element.ALIGN_CENTER);		//수평정렬
					c2.setVerticalAlignment(Element.ALIGN_BOTTOM);			//수직정렬
					t2.addCell(c2);

					//S/N 입력 insertTable만들기
					Paragraph SN = new Paragraph(serial_no, SNfont);		//S/N번호
					Table t3=new Table(1);
					Cell c3 = new Cell(SN);
					c3.setBorderColor(new Color(R,G,B));					//cell색상
					c3.setLeading(8f);										//cell height
					t3.addCell(c3);
					
					//각 insertTable을 담을 테이블 insertTable만들기
					//(생략할 부분은 아래해당라인을 막으면 된다.)
					Table t4=new Table(1,2);
					t4.insertTable(t1);										//상품명 입력여부 (생략시 막어...)
					t4.insertTable(t2);										//바코드 입력여부
					t4.insertTable(t3);										//S/N 입력여부
					
					//전체마스터 테이블에 등록하기
					//table.insertTable(t4,new Point(r,c));				
					table.insertTable(t4);	
					
					n++;					//Serial no 증가번호
				} 
				//cell skip하기
				else {
					//상품명 입력공란 insertTable만들기
					Table t1=new Table(1);
					Cell c1 = new Cell(new Paragraph("	\n"));
					c1.setBorderColor(new Color(255,255,255));
					c1.setLeading(8f);										//cell height
					t1.addCell(c1);

					//바코드 입력공란 insertTable만들기
					Table t2=new Table(1);
					Cell c2 = new Cell("	\n");
					c2.setBorderColor(new Color(255,255,255));
					c2.setLeading(33f);										//cell height
					t2.addCell(c2);

					//S/N 입력공란 insertTable만들기
					Table t3=new Table(1);
					Cell c3 = new Cell(new Paragraph("	\n"));
					c3.setBorderColor(new Color(255,255,255));
					c3.setLeading(8f);										//cell height
					t3.addCell(c3);

					//각 insertTable을 담을 테이블 insertTable만들기
					//(생략할 부분은 아래해당라인을 막으면 된다.)
					Table t4=new Table(1,2);
					t4.insertTable(t1);										//상품명 입력여부 (생략시 막어...)
					t4.insertTable(t2);										//바코드 입력여부
					t4.insertTable(t3);										//S/N 입력여부
					table.insertTable(t4);	

					s++;					//skip 조건
				}
				if(n > cnt-1) break;		//요구수량만큼 출력후 종료하기
		}	//outter for
		document.add(table);	
	}

	//*******************************************************************
	//	BARCODE TYPE별 생성 바코드 선택 : 공통사용
	//*******************************************************************/
	public Phrase selectBarCode(String barcode_type,String barcode) throws Exception
	{
		Phrase p = null;

		//barcode type별 생성할 바코드 선택하기
		//EAN13 (13숫자) ---> 입력: 12자리숫자만 가능, checksum: 1자리 자동생성됨.
		if(barcode_type.equals("EAN13")) {
			if(barcode.length() > 12) barcode = barcode.substring(0,12);
			p = new Phrase(new Chunk(createEAN13(barcode),0,0));
		} 
		//EAN8 (8숫자) ---> 입력: 7자리숫자만 가능, checksum: 1자리 자동생성됨.
		else if(barcode_type.equals("EAN8")) { 
			if(barcode.length() > 7) barcode = barcode.substring(0,7);
			p = new Phrase(new Chunk(createEAN8(barcode),0,0));
		}
		//UPCA (11숫자) ---> 입력: 11자리숫자만 가능, checksum: 1자리 자동생성됨.
		else if(barcode_type.equals("UPCA")) { 
			if(barcode.length() > 11) barcode = barcode.substring(0,11);
			p = new Phrase(new Chunk(createUPCA(barcode),0,0));
		}
		//CODE128 (128 영문+숫자) ---> 입력: 128자리숫자만 가능, control: 12자리 자동생성됨.
		//단, 셀크기를 벗어나면 출력되지 않음. 
		else if(barcode_type.equals("CODE128")) { 
			if(barcode.length() > 128) barcode = barcode.substring(0,128);
			p = new Phrase(new Chunk(createCODE128(barcode),0,0));
		}
		
		return p;
	}

}
